package com.dongguo.exceldemo.easyexcel.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dongguo.exceldemo.easyexcel.common.BooleanTypeEnum;
import com.dongguo.exceldemo.easyexcel.common.UploadDataV3Listener;
import com.dongguo.exceldemo.easyexcel.convert.ProductSpuConvert;
import com.dongguo.exceldemo.easyexcel.entity.ProductExportVO;
import com.dongguo.exceldemo.easyexcel.entity.ProductSpu;
import com.dongguo.exceldemo.easyexcel.entity.ProductUploadVO;
import com.dongguo.exceldemo.easyexcel.mapper.EasyExcelMapper;
import com.dongguo.exceldemo.easyexcel.service.EasyExcelService;
import com.dongguo.exceldemo.easyexcel.service.EasyExcelV3Service;
import com.dongguo.exceldemo.easyexcel.service.ProductUploadService;
import com.dongguo.exceldemo.util.EasyExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.dongguo.exceldemo.easyexcel.common.Const.FILE_NAME;

/**
 * @author dongguo
 * @date 2023/2/3
 * @description:
 */
@Service
@Slf4j
public class EasyExcelV3ServiceImpl extends ServiceImpl<EasyExcelMapper, ProductSpu> implements EasyExcelV3Service {

    @Autowired
    private ProductUploadService productUploadService;
    @Autowired
    private EasyExcelService easyExcelService;

    @Override
    public void export(HttpServletResponse response) {
        //查数据
        List<ProductExportVO> exportVOList = getExportVOList();
        //导出
        String fileName = FILE_NAME;
        EasyExcelUtils.export(response, exportVOList, fileName, ProductExportVO.class);
    }

    @Override
    public void exportSafe(HttpServletResponse response) {
        //查数据
        List<ProductExportVO> exportVOList = getExportVOList();
        //导出
        String fileName = FILE_NAME;
        EasyExcelUtils.exportSafe(response, exportVOList, fileName, ProductExportVO.class);
    }

    /**
     * 查数据库获得导出的List
     *
     * @return
     */
    private List<ProductExportVO> getExportVOList() {
        List<ProductSpu> productSpuList = easyExcelService.list();
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

    /**
     * easyExcel导入提供4中方式
     *
     * @param file
     */
    @Override
    public void upload(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();

            /**
             * 方式二 内部类创建Listener
             * 同方式三
             */
            /**
             * 方式三
             * 每100条执行一次新增
             */
//            EasyExcel.read(inputStream, ProductUploadVO.class, new UploadDataV3Listener(productUploadService)).sheet().doRead();
            /**
             * 方式四
             */
//            try (ExcelReader excelReader = EasyExcel.read(inputStream, ProductUploadVO.class, new UploadDataV3Listener(productUploadService)).build()) {
//                // 构建一个sheet 这里可以指定名字或者no
//                ReadSheet readSheet = EasyExcel.readSheet(FILE_NAME).build();
//                // 读取一个sheet
//                excelReader.read(readSheet);
//            }

            /**
             * 方式一
             *写法1：JDK8+ ,不用额外写一个DemoDataListener
             *since: 3.0.0-beta1
             *这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
             *这里每次会读取100条数据 然后返回过来 直接调用使用数据就行
             */
            EasyExcel.read(inputStream, ProductUploadVO.class,
                    new PageReadListener<ProductUploadVO>(dataList -> {
                        productUploadService.save(dataList);
                    })).sheet().doRead();
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

    @Override
    public void uploadReadAllSheet(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            // 读取全部sheet
            // 这里需要注意 DemoDataListener的doAfterAllAnalysed 会在每个sheet读取完毕后调用一次。然后所有sheet都会往同一个DemoDataListener里面写
            EasyExcel.read(inputStream, ProductUploadVO.class, new UploadDataV3Listener(productUploadService)).doReadAll();
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

    @Override
    public void uploadComplexHeaderRead(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            EasyExcel.read(inputStream, ProductUploadVO.class, new UploadDataV3Listener(productUploadService)).sheet()
                    // 这里可以设置1，因为头就是一行。如果多行头，可以设置其他值。不传入也可以，因为默认会根据DemoData 来解析，他没有指定头，也就是默认1行
                    .headRowNumber(1).doRead();
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
