package com.dongguo.exceldemo.easyexcel.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dongguo.exceldemo.easyexcel.common.BooleanTypeEnum;
import com.dongguo.exceldemo.easyexcel.convert.ProductSpuConvert;
import com.dongguo.exceldemo.easyexcel.entity.ProductExportVO;
import com.dongguo.exceldemo.easyexcel.entity.ProductSpu;
import com.dongguo.exceldemo.easyexcel.entity.ProductUploadVO;
import com.dongguo.exceldemo.easyexcel.mapper.EasyExcelMapper;
import com.dongguo.exceldemo.easyexcel.service.EasyExcelService;
import com.dongguo.exceldemo.easyexcel.service.ProductUploadService;
import com.dongguo.exceldemo.util.EasyExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dongguo
 * @date 2023/2/3
 * @description:
 */
@Service
public class EasyExcelServiceImpl extends ServiceImpl<EasyExcelMapper, ProductSpu> implements EasyExcelService {

    @Autowired
    private ProductUploadService productUploadService;

    @Override
    public void export(HttpServletResponse response) {
        //查数据
        List<ProductExportVO> exportVOList = getExportVOList();
        //导出
        String fileName = "商品清单";
        EasyExcelUtils.export(response, exportVOList, fileName, ProductExportVO.class);
    }

    @Override
    public void exportSafe(HttpServletResponse response) {
        //查数据
        List<ProductExportVO> exportVOList = getExportVOList();
        //导出
        String fileName = "商品清单";
        EasyExcelUtils.exportSafe(response, exportVOList, fileName, ProductExportVO.class);
    }

    /**
     * 查数据库获得导出的List
     * @return
     */
    private List<ProductExportVO> getExportVOList() {
        List<ProductSpu> productSpuList = list();
        List<ProductExportVO> exportVOList = new ArrayList<>(productSpuList.size());
        for (ProductSpu spu : productSpuList) {
            ProductExportVO exportVO = ProductSpuConvert.INSTANCE.poToVo(spu);
            exportVO.setNeedCheck(BooleanTypeEnum.parseDesc(spu.getNeedCheck()));
            exportVO.setNeedLedger(BooleanTypeEnum.parseDesc(spu.getNeedLedger()));
            exportVO.setIsStandard(BooleanTypeEnum.parseDesc(spu.getIsStandard()));
            exportVOList.add(exportVO);
        }
        return exportVOList;
    }

    @Override
    public void upload(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            /**
             * 方式一
             * 这种方式局限性太高 ,但是可以指定批量插入的个数
             */
//            EasyExcel.read(inputStream, ProductUploadVO.class, new UploadDataListener(productUploadService)).sheet().doRead();
            /**
             * 方式二
             * 一次性插入
             */
            List<ProductUploadVO> list = EasyExcel.read(file.getInputStream()).head(ProductUploadVO.class).sheet().doReadSync();
            productUploadService.save(list);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
