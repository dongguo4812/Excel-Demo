package com.dongguo.exceldemo.myexcel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dongguo.exceldemo.easyexcel.common.BooleanTypeEnum;
import com.dongguo.exceldemo.easyexcel.entity.ProductSpu;
import com.dongguo.exceldemo.easyexcel.service.EasyExcelService;
import com.dongguo.exceldemo.myexcel.common.ProductSpuConvert;
import com.dongguo.exceldemo.myexcel.entity.ProductExportMyExcelVO;
import com.dongguo.exceldemo.myexcel.entity.ProductUploadMyExcelVO;
import com.dongguo.exceldemo.myexcel.mapper.MyExcelMapper;
import com.dongguo.exceldemo.myexcel.service.MyExcelService;
import com.dongguo.exceldemo.util.PageUtils;
import com.github.liaochong.myexcel.core.*;
import com.github.liaochong.myexcel.core.templatehandler.FreemarkerTemplateHandler;
import com.github.liaochong.myexcel.utils.AttachmentExportUtil;
import com.github.liaochong.myexcel.utils.FileExportUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author dongguo
 * @date 2023/2/24
 * @description:
 */
@Service
@Slf4j
public class MyExcelServiceImpl extends ServiceImpl<MyExcelMapper, ProductSpu> implements MyExcelService {
    @Autowired
    private EasyExcelService easyExcelService;
    @Override
    public void upload(MultipartFile file) {

        try {
            // 方式一：全部读取后处理  不好处理数据
//            List<ProductUploadMyExcelVO> result = DefaultExcelReader.of(ProductUploadMyExcelVO.class)
//                    .sheet(0) // 0代表第一个，如果为0，可省略该操作，也可sheet("名称")读取
//                    .rowFilter(row -> row.getRowNum() > 0) // 如无需过滤，可省略该操作，0代表第一行.
//                    .read(file.getInputStream());// 可接收inputStream
//
//            List<ProductSpu> spuList = new ArrayList<>(result.size());
//            result.stream().forEach(uploadVO ->{
//                ProductSpu spu = ProductSpuConvert.INSTANCE.voToPo(uploadVO);
//                spu.setNeedCheck(BooleanTypeEnum.parseType(uploadVO.getNeedCheckStr()));
//                spu.setNeedLedger(BooleanTypeEnum.parseType(uploadVO.getNeedLedgerStr()));
//                spu.setIsStandard(BooleanTypeEnum.parseType(uploadVO.getIsStandardStr()));
//                spu.setCategory(uploadVO.getBigCategory() +"/"+ uploadVO.getSmallCategory());
//                spuList.add(spu);
//            });
//            saveBatch(spuList);

            // 方式二：读取一行处理一行，可自行决定终止条件
            // readThen有两种重写接口，返回Boolean型接口允许在返回False情况下直接终止读取
            List<ProductUploadMyExcelVO> voList = new ArrayList<>();

            DefaultExcelReader.of(ProductUploadMyExcelVO.class)
                    .sheet(0) // 0代表第一个，如果为0，可省略该操作，也可sheet("名称")读取
                    .rowFilter(row -> row.getRowNum() > 0) // 如无需过滤，可省略该操作，0代表第一行
                    .beanFilter(ProductUploadMyExcelVO::hasTaxRate) // bean过滤
                    .readThen(file.getInputStream(), vo -> {voList.add(vo);});// 可接收inputStream
            List<ProductSpu> spuList = ProductSpuConvert.INSTANCE.voToPoList(voList);
            saveBatch(spuList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saxUpload(MultipartFile file) {
        try {
            // 方式一：全部读取后处理，SAX模式，避免OOM，建议大量数据使用
//            List<ProductUploadMyExcelVO> result = SaxExcelReader.of(ProductUploadMyExcelVO.class)
//                    .sheet(0) // 0代表第一个，如果为0，可省略该操作，也可sheet("名称")读取，.csv文件无效
//                    .rowFilter(row -> row.getRowNum() > 0) // 如无需过滤，可省略该操作，0代表第一行
//                    .ignoreBlankRow() // 是否忽略空行，可选，默认不忽略
//                    .stopReadingOnBlankRow() // 是否遇到空行则停止读取，可选，默认为否
//                    .read(file.getInputStream());// 可接收inputStream
//            List<ProductSpu> spuList = new ArrayList<>(result.size());
//            result.stream().forEach(uploadVO ->{
//                ProductSpu spu = ProductSpuConvert.INSTANCE.voToPo(uploadVO);
//                spu.setNeedCheck(BooleanTypeEnum.parseType(uploadVO.getNeedCheckStr()));
//                spu.setNeedLedger(BooleanTypeEnum.parseType(uploadVO.getNeedLedgerStr()));
//                spu.setIsStandard(BooleanTypeEnum.parseType(uploadVO.getIsStandardStr()));
//                spu.setCategory(uploadVO.getBigCategory() +"/"+ uploadVO.getSmallCategory());
//                spuList.add(spu);
//            });
//            saveBatch(spuList);

            List<ProductSpu> spuList = new ArrayList<>();
            SaxExcelReader.of(ProductUploadMyExcelVO.class)
                    .sheet(0) // 0代表第一个，如果为0，可省略该操作，也可sheet("名称")读取，.csv文件无效
                    .rowFilter(row -> row.getRowNum() > 0) // 如无需过滤，可省略该操作，0代表第一行
                    .ignoreBlankRow() // 是否忽略空行，可选，默认不忽略
                    .stopReadingOnBlankRow() // 是否遇到空行则停止读取，可选，默认为否
                    .readThen(file.getInputStream() , uploadVO -> {
                        ProductSpu spu = ProductSpuConvert.INSTANCE.voToPo(uploadVO);
                        spu.setNeedCheck(BooleanTypeEnum.parseType(uploadVO.getNeedCheckStr()));
                        spu.setNeedLedger(BooleanTypeEnum.parseType(uploadVO.getNeedLedgerStr()));
                        spu.setIsStandard(BooleanTypeEnum.parseType(uploadVO.getIsStandardStr()));
                        spu.setCategory(uploadVO.getBigCategory() +"/"+ uploadVO.getSmallCategory());
                        spuList.add(spu);
                    });// 可接收inputStream
            saveBatch(spuList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void defaultExport(HttpServletResponse response) {
        List<ProductExportMyExcelVO> dataList = this.getExportVOList();

        Workbook workbook = DefaultExcelBuilder.of(ProductExportMyExcelVO.class).build(dataList);
        //1附件导出
        AttachmentExportUtil.export(workbook, "商品清单", response);
        //2附件加密导出
//        AttachmentExportUtil.encryptExport(workbook, "商品清单", response,"123456");
    }
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    @Override
    public void streamExport(HttpServletResponse response) {
        try (DefaultStreamExcelBuilder<ProductExportMyExcelVO> defaultExcelBuilder = DefaultStreamExcelBuilder.of(ProductExportMyExcelVO.class)
                .threadPool(executorService)
                .noStyle()
                .start()) {

            List<ProductExportMyExcelVO> dataList = this.getExportVOList();

            for (ProductExportMyExcelVO vo: dataList) {
//                defaultExcelBuilder.append(vo);
                defaultExcelBuilder.asyncAppend(vo::getVo);
            }
            Workbook workbook = defaultExcelBuilder.build();
            AttachmentExportUtil.export(workbook, "商品清单", response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查数据库获得导出的List
     *
     * @return
     */
    private List<ProductExportMyExcelVO> getExportVOList() {
        List<ProductSpu> productSpuList = easyExcelService.list();
        List<ProductExportMyExcelVO> exportVOList = new ArrayList<>(productSpuList.size());
        for (ProductSpu spu : productSpuList) {
            ProductExportMyExcelVO exportVO = ProductSpuConvert.INSTANCE.poToVoMyExcel(spu);
            exportVO.setNeedCheck(BooleanTypeEnum.parseDesc(spu.getNeedCheck()));
            exportVO.setNeedLedger(BooleanTypeEnum.parseDesc(spu.getNeedLedger()));
            exportVO.setIsStandard(BooleanTypeEnum.parseDesc(spu.getIsStandard()));
            exportVOList.add(exportVO);
        }
        return exportVOList;
    }
}
