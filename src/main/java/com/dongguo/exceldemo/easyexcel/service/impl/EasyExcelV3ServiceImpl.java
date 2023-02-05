package com.dongguo.exceldemo.easyexcel.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.metadata.data.*;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dongguo.exceldemo.easyexcel.common.*;
import com.dongguo.exceldemo.easyexcel.convert.ProductSpuConvert;
import com.dongguo.exceldemo.easyexcel.entity.*;
import com.dongguo.exceldemo.easyexcel.mapper.EasyExcelMapper;
import com.dongguo.exceldemo.easyexcel.service.EasyExcelService;
import com.dongguo.exceldemo.easyexcel.service.EasyExcelV3Service;
import com.dongguo.exceldemo.easyexcel.service.ProductUploadService;
import com.dongguo.exceldemo.util.EasyExcelUtils;
import com.dongguo.exceldemo.util.TestFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
    public void exportSafe(HttpServletResponse response) {
        //查数据
        List<ProductExportVO> exportVOList = getExportVOList();
        //导出
        String fileName = FILE_NAME;
        EasyExcelUtils.exportSafe(response, exportVOList, fileName, ProductExportVO.class);
    }

    @Override
    public void excludeOrIncludeWrite(HttpServletResponse response) {
        //查数据
        List<ProductExportVO> exportVOList = getExportVOList();
        //导出
        String fileName = FILE_NAME;
        EasyExcelUtils.excludeOrIncludeWrite(response, exportVOList, fileName, ProductExportVO.class);
    }

    @Override
    public void repeatedWrite(HttpServletResponse response) {
        //查数据
        List<ProductExportVO> exportVOList = getExportVOList();
        //导出
        String fileName = FILE_NAME;
        EasyExcelUtils.repeatedWrite(response, exportVOList, fileName, ProductExportVO.class);
    }

    @Override
    public void imageWrite(HttpServletResponse response) {
        //查数据
        List<ImageDemoData> list = new ArrayList<>();
        try {
            /**
             * URL只要是个图片即可，不要求后缀等
             */
            URL url = new URL("https://www.baidu.com/img/flexible/logo/pc/result.png");
            ImageDemoData imageDemoData = new ImageDemoData();
            imageDemoData.setUrl(url);
            list.add(imageDemoData);
            URL url2 = new URL("https://img2.baidu.com/it/u=3334115308,1054927257&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500");
            ImageDemoData imageDemoData2 = new ImageDemoData();
            imageDemoData2.setUrl(url2);
            list.add(imageDemoData2);
            //导出
            String fileName = "导出图片";
            EasyExcelUtils.imageWrite(response, list, fileName, ImageDemoData.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void writeCellDataWrite(HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            String fileName = "导出图片";
            outputStream = EasyExcelUtils.getOutputStream(response, fileName);
            WriteCellDemoData writeCellDemoData = new WriteCellDemoData();

            // 设置超链接
            WriteCellData<String> hyperlink = new WriteCellData<>("官方网站");
            writeCellDemoData.setHyperlink(hyperlink);
            HyperlinkData hyperlinkData = new HyperlinkData();
            hyperlink.setHyperlinkData(hyperlinkData);
            hyperlinkData.setAddress("https://github.com/alibaba/easyexcel");
            hyperlinkData.setHyperlinkType(HyperlinkData.HyperlinkType.URL);

            // 设置备注
            WriteCellData<String> comment = new WriteCellData<>("备注的单元格信息");
            writeCellDemoData.setCommentData(comment);
            CommentData commentData = new CommentData();
            comment.setCommentData(commentData);
            commentData.setAuthor("Jiaju Zhuang");
            commentData.setRichTextStringData(new RichTextStringData("这是一个备注"));
            // 备注的默认大小是按照单元格的大小 这里想调整到4个单元格那么大 所以向后 向下 各额外占用了一个单元格
            commentData.setRelativeLastColumnIndex(1);
            commentData.setRelativeLastRowIndex(1);

            // 设置公式
            WriteCellData<String> formula = new WriteCellData<>();
            writeCellDemoData.setFormulaData(formula);
            FormulaData formulaData = new FormulaData();
            formula.setFormulaData(formulaData);
            // 将 123456789 中的第一个数字替换成 2
            // 这里只是例子 如果真的涉及到公式 能内存算好尽量内存算好 公式能不用尽量不用
            formulaData.setFormulaValue("REPLACE(123456789,1,1,2)");

            // 设置单个单元格的样式 当然样式 很多的话 也可以用注解等方式。
            WriteCellData<String> writeCellStyle = new WriteCellData<>("单元格样式");
            writeCellStyle.setType(CellDataTypeEnum.STRING);
            writeCellDemoData.setWriteCellStyle(writeCellStyle);
            WriteCellStyle writeCellStyleData = new WriteCellStyle();
            writeCellStyle.setWriteCellStyle(writeCellStyleData);
            // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.
            writeCellStyleData.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
            // 背景绿色
            writeCellStyleData.setFillForegroundColor(IndexedColors.GREEN.getIndex());

            // 设置单个单元格多种样式
            WriteCellData<String> richTest = new WriteCellData<>();
            richTest.setType(CellDataTypeEnum.RICH_TEXT_STRING);
            writeCellDemoData.setRichText(richTest);
            RichTextStringData richTextStringData = new RichTextStringData();
            richTest.setRichTextStringDataValue(richTextStringData);
            richTextStringData.setTextString("红色绿色默认");
            // 前2个字红色
            WriteFont writeFont = new WriteFont();
            writeFont.setColor(IndexedColors.RED.getIndex());
            richTextStringData.applyFont(0, 2, writeFont);
            // 接下来2个字绿色
            writeFont = new WriteFont();
            writeFont.setColor(IndexedColors.GREEN.getIndex());
            richTextStringData.applyFont(2, 4, writeFont);

            List<WriteCellDemoData> data = new ArrayList<>();
            data.add(writeCellDemoData);
            EasyExcel.write(outputStream, WriteCellDemoData.class).inMemory(true).sheet("模板").doWrite(data);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void templateWrite(HttpServletResponse response) {
        //查数据
        List<ProductExportVO> list = getExportVOList();
        //导出
        EasyExcelUtils.templateWrite(response, list, FILE_NAME, ProductExportVO.class);

    }

    @Override
    public void annotationStyleWrite(HttpServletResponse response) {
        //查数据
        List<DemoData> list = EasyExcelUtils.data();
        //导出
        EasyExcelUtils.annotationStyleWrite(response, list, FILE_NAME, DemoStyleData.class);
    }

    @Override
    public void extraRead(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            // 这里 需要指定读用哪个class去读，然后读取第一个sheet
            EasyExcel.read(inputStream, DemoExtraData.class, new DemoExtraListener())
                    // 需要读取批注 默认不读取
                    .extraRead(CellExtraTypeEnum.COMMENT)
                    // 需要读取超链接 默认不读取
                    .extraRead(CellExtraTypeEnum.HYPERLINK)
                    // 需要读取合并单元格信息 默认不读取
                    .extraRead(CellExtraTypeEnum.MERGE).sheet().doRead();

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
    public void noModelRead(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            //读取第一个sheet 同步读取会自动finish
            EasyExcel.read(inputStream, new NoModelDataListener(productUploadService)).sheet().doRead();
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
    public void simpleWrite(HttpServletResponse response) {
        //查数据
        List<ProductExportVO> exportVOList = getExportVOList();
        //导出
        String sheetName = FILE_NAME;
        EasyExcelUtils.export(response, exportVOList, sheetName, ProductExportVO.class);
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
//            EasyExcel.read(inputStream, ProductUploadVO.class,
//                    new PageReadListener<ProductUploadVO>(dataList -> {
//                        productUploadService.save(dataList);
//                    })).sheet().doRead();
            /**
             * web中的读
             */
            EasyExcel.read(inputStream, ProductUploadVO.class, new UploadDataV3Listener(productUploadService)).sheet().doRead();
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
            // 写法1
//            try (ExcelReader excelReader = EasyExcel.read(inputStream).build()) {
//                // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
//                ReadSheet readSheet1 = EasyExcel.readSheet(0).head(ProductUploadVO.class).registerReadListener(new UploadDataV3Listener(productUploadService)).build();
//                ReadSheet readSheet2 = EasyExcel.readSheet(1).head(ProductUploadVO.class).registerReadListener(new UploadDataV3Listener(productUploadService)).build();
//                // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
//                excelReader.read(readSheet1, readSheet2);
//            }

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
            EasyExcel.read(inputStream, ProductUploadVO.class, new UploadDataV3Listener(productUploadService)).ignoreEmptyRow(true).sheet()
                    // 这里可以设置1，因为头就是一行。如果多行头，可以设置其他值。不传入也可以，因为默认会根据DemoData 来解析，他没有指定头，也就是默认1行
                    .headRowNumber(2).doRead();
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
    public void synchronousRead(MultipartFile file) {

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
            List<ProductUploadVO> list = EasyExcel.read(inputStream).head(ProductUploadVO.class).sheet().doReadSync();
            productUploadService.save(list);

            // 这里 也可以不指定class，返回一个list，然后读取第一个sheet 同步读取会自动finish
//            List<Map<Integer, String>> listMap = EasyExcel.read(inputStream).sheet().doReadSync();
//            for (Map<Integer, String> data : listMap) {
//                // 返回每条数据的键值对 表示所在的列 和所在列的值
//                log.info("读取到数据:{}", JSON.toJSONString(data));
//            }
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
    public void headerRead(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            EasyExcel.read(inputStream, ProductUploadVO.class, new UploadDataV3Listener(productUploadService)).sheet().doRead();
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
