package com.dongguo.exceldemo.util;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.BooleanUtils;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.merge.LoopMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson2.JSON;
import com.dongguo.exceldemo.easyexcel.common.CommentWriteHandler;
import com.dongguo.exceldemo.easyexcel.common.CustomCellWriteHandler;
import com.dongguo.exceldemo.easyexcel.common.CustomSheetWriteHandler;
import com.dongguo.exceldemo.easyexcel.entity.*;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dongguo
 * @date 2023/2/3
 * @description:
 */
public class EasyExcelUtils {

    /**
     * 导出通用接口  文件下载（失败了会返回一个有部分数据的Excel）
     *
     * @param list
     * @param fileName
     * @param clazz
     */
    public static void export(HttpServletResponse response, List<?> list, String fileName, Class clazz) {
        OutputStream outputStream = null;
        try {
            outputStream = getOutputStream(response, fileName);
            //方法一：
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            // 如果这里想使用03 则 传入excelType参数即可
//            EasyExcel.write(outputStream, clazz)
//                    .sheet(fileName)
//                    .doWrite(() -> {
//                        // 分页查询数据
//                        return list;
//                    });


            // 写法2
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            // 如果这里想使用03 则 传入excelType参数即可
//            EasyExcel.write(outputStream, clazz).sheet(fileName).doWrite(list);


            // 写法3
            // 这里 需要指定写用哪个class去写
            try (ExcelWriter excelWriter = EasyExcel.write(outputStream, clazz).build()) {
                WriteSheet writeSheet = EasyExcel.writerSheet(fileName).build();
                excelWriter.write(list, writeSheet);
            }
            outputStream.flush();
        } catch (Exception e) {
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

    /**
     * 导出通用接口  文件下载（失败了会返回一个有部分数据的Excel）
     *
     * @param list
     * @param fileName
     * @param clazz
     */
    public static void exportSafe(HttpServletResponse response, List<?> list, String fileName, Class clazz) {
        OutputStream outputStream = null;
        try {
            outputStream = getOutputStream(response, fileName);
            // 这里需要设置不关闭流
            EasyExcel.write(outputStream, clazz).autoCloseStream(Boolean.FALSE).sheet(fileName).doWrite(list);
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>(2);
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            try {
                response.getWriter().println(JSON.toJSONString(map));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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

    /**
     * 获取OutputStream并设置excel格式
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static OutputStream getOutputStream(HttpServletResponse response, String fileName) throws IOException {

        OutputStream outputStream = response.getOutputStream();
        // 导出EXCEL
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
//        response.setContentType("application/vnd.ms-excel");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        // 这里URLEncoder.encode可以防止浏览器端导出excel文件名中文乱码 当然和easyexcel没有关系
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodeFileName(fileName) + ".xlsx");
        return outputStream;
    }

    /**
     * encode fileName
     *
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String encodeFileName(String fileName) throws UnsupportedEncodingException {
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        return fileName;
    }

    public static void excludeOrIncludeWrite(HttpServletResponse response, List<?> list, String fileName, Class clazz) {
        OutputStream outputStream = null;
        try {
            outputStream = getOutputStream(response, fileName);
            // 这里需要注意 在使用ExcelProperty注解的使用，如果想不空列则需要加入order字段，而不是index,order会忽略空列，然后继续往后，而index，不会忽略空列，在第几列就是第几列。

            // 根据用户传入字段 假设我们要忽略 remark
            Set<String> excludeColumnFiledNames = new HashSet<String>();
            excludeColumnFiledNames.add("remark");
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭  excludeColumnFiledNames已经废弃
            EasyExcel.write(outputStream, clazz).excludeColumnFiledNames(excludeColumnFiledNames).sheet(fileName).doWrite(list);
            //使用excludeColumnFieldNames
//            EasyExcel.write(outputStream, clazz).excludeColumnFieldNames(excludeColumnFiledNames).sheet(fileName).doWrite(list);
            outputStream.flush();
        } catch (Exception e) {
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

    public static void repeatedWrite(HttpServletResponse response, List<?> list, String fileName, Class clazz) {
        OutputStream outputStream = null;
        try {
            outputStream = getOutputStream(response, fileName);
            // 方法1: 分页查询数据 根据页数分页写到同一个sheet
            // 这里 需要指定写用哪个class去写
            try (ExcelWriter excelWriter = EasyExcel.write(outputStream, clazz).build()) {
                // 这里注意 如果同一个sheet只要创建一次
                WriteSheet writeSheet = EasyExcel.writerSheet(fileName).build();
                // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来

                //获得导出数据的条数，并根据总条数和一页显示条数得到页数
                long pageSize = 3000L;
                long totalPageNo = (list.size() / pageSize) + 1;
                for (long pageNo = 1; pageNo <= totalPageNo; pageNo ++){
                    long skipSize = pageSize * (pageNo - 1);
                    List<ProductExportVO> data = (List<ProductExportVO>)list.stream().skip(skipSize).limit(pageSize).collect(Collectors.toList());
                    excelWriter.write(data, writeSheet);
                }
            }

            // 方法2: 分页查询数据 根据页数分页写到不同的sheet 同一个对象
            // 这里 指定文件
//            try (ExcelWriter excelWriter = EasyExcel.write(outputStream, clazz).build()) {
//                // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
//                for (int i = 0; i < 5; i++) {
//                    // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样
//                    WriteSheet writeSheet = EasyExcel.writerSheet(i, fileName + i).build();
//                    // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
//                    List<ProductExportVO> data = (List<ProductExportVO>) list;
//                    excelWriter.write(data, writeSheet);
//                }
//            }

            // 方法3 分页查询数据 根据页数分页写到不同的sheet 不同的对象
            // 这里 指定文件
//            try (ExcelWriter excelWriter = EasyExcel.write(outputStream).build()) {
//                // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
//                for (int i = 0; i < 5; i++) {
//                    // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样。这里注意DemoData.class 可以每次都变，我这里为了方便 所以用的同一个class
//                    // 实际上可以一直变
//                    WriteSheet writeSheet = EasyExcel.writerSheet(i, fileName + i).head(clazz).build();
//                    // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
//                    List<ProductExportVO> data = (List<ProductExportVO>) list;
//                    excelWriter.write(data, writeSheet);
//                }
//            }
            outputStream.flush();
        } catch (Exception e) {
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

    public static void imageWrite(HttpServletResponse response, List<?> list, String fileName, Class clazz) {
        OutputStream outputStream = null;
        try {
            outputStream = getOutputStream(response, fileName);

//                List<ImageDemoData> list =  ListUtils.newArrayList();
//                ImageDemoData imageDemoData = new ImageDemoData();
//                list.add(imageDemoData);
//                // 放入五种类型的图片 实际使用只要选一种即可
//                imageDemoData.setByteArray(FileUtils.readFileToByteArray(new File(imagePath)));
//                imageDemoData.setFile(new File(imagePath));
//                imageDemoData.setString(imagePath);
//                imageDemoData.setInputStream(inputStream);
//                imageDemoData.setUrl(new URL(
//                        "https://raw.githubusercontent.com/alibaba/easyexcel/master/src/test/resources/converter/img.jpg"));
//
//                // 这里演示
//                // 需要额外放入文字
//                // 而且需要放入2个图片
//                // 第一个图片靠左
//                // 第二个靠右 而且要额外的占用他后面的单元格
//                WriteCellData<Void> writeCellData = new WriteCellData<>();
//                imageDemoData.setWriteCellDataFile(writeCellData);
//                // 这里可以设置为 EMPTY 则代表不需要其他数据了
//                writeCellData.setType(CellDataTypeEnum.STRING);
//                writeCellData.setStringValue("额外的放一些文字");
//
//                // 可以放入多个图片
//                List<ImageData> imageDataList = new ArrayList<>();
//                ImageData imageData = new ImageData();
//                imageDataList.add(imageData);
//                writeCellData.setImageDataList(imageDataList);
//                // 放入2进制图片
//                imageData.setImage(FileUtils.readFileToByteArray(new File(imagePath)));
//                // 图片类型
//                imageData.setImageType(ImageType.PICTURE_TYPE_PNG);
//                // 上 右 下 左 需要留空
//                // 这个类似于 css 的 margin
//                // 这里实测 不能设置太大 超过单元格原始大小后 打开会提示修复。暂时未找到很好的解法。
//                imageData.setTop(5);
//                imageData.setRight(40);
//                imageData.setBottom(5);
//                imageData.setLeft(5);
//
//                // 放入第二个图片
//                imageData = new ImageData();
//                imageDataList.add(imageData);
//                writeCellData.setImageDataList(imageDataList);
//                imageData.setImage(FileUtils.readFileToByteArray(new File(imagePath)));
//                imageData.setImageType(ImageType.PICTURE_TYPE_PNG);
//                imageData.setTop(5);
//                imageData.setRight(5);
//                imageData.setBottom(5);
//                imageData.setLeft(50);
//                // 设置图片的位置 假设 现在目标 是 覆盖 当前单元格 和当前单元格右边的单元格
//                // 起点相对于当前单元格为0 当然可以不写
//                imageData.setRelativeFirstRowIndex(0);
//                imageData.setRelativeFirstColumnIndex(0);
//                imageData.setRelativeLastRowIndex(0);
//                // 前面3个可以不写  下面这个需要写 也就是 结尾 需要相对当前单元格 往右移动一格
//                // 也就是说 这个图片会覆盖当前单元格和 后面的那一格
//                imageData.setRelativeLastColumnIndex(1);

            // 写入数据
            EasyExcel.write(outputStream, clazz).sheet(fileName).doWrite(list);
            outputStream.flush();
        } catch (Exception e) {
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

    public static void templateWrite(HttpServletResponse response, List<?> list, String fileName, Class clazz) {
        OutputStream outputStream = null;
        try {
            outputStream = getOutputStream(response, fileName);
            String templateFileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";

            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            EasyExcel.write(outputStream, clazz).withTemplate(templateFileName).sheet().doWrite(list);
            outputStream.flush();
        } catch (Exception e) {
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

    public static void annotationStyleWrite(HttpServletResponse response, List<?> list, String fileName, Class clazz) {
        OutputStream outputStream = null;
        try {
            outputStream = getOutputStream(response, fileName);

            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            EasyExcel.write(outputStream, clazz).sheet(fileName).doWrite(list);
            outputStream.flush();
        } catch (Exception e) {
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

    public static List<DemoData> data() {
        List<DemoData> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }
    public static List<ConverterData> convertData() {
        List<ConverterData> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            ConverterData data = new ConverterData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

    public static void handlerStyleWrite(HttpServletResponse response, List<?> list, String fileName, Class clazz) {
        OutputStream outputStream = null;
        try {
            outputStream = getOutputStream(response, fileName);
            // 方法1 使用已有的策略 推荐
            // HorizontalCellStyleStrategy 每一行的样式都一样 或者隔行一样
            // AbstractVerticalCellStyleStrategy 每一列的样式都一样 需要自己回调每一页
            // 头的策略
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            // 背景设置为红色
            headWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            WriteFont headWriteFont = new WriteFont();
            headWriteFont.setFontHeightInPoints((short) 20);
            headWriteCellStyle.setWriteFont(headWriteFont);
            // 内容的策略
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
            contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
            // 背景绿色
            contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            WriteFont contentWriteFont = new WriteFont();
            // 字体大小
            contentWriteFont.setFontHeightInPoints((short) 10);
            contentWriteCellStyle.setWriteFont(contentWriteFont);
            // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
            HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                    new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            EasyExcel.write(outputStream, clazz)
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .sheet(fileName)
                    .doWrite(list);


            // 方法2: 使用easyexcel的方式完全自己写 不太推荐 尽量使用已有策略
            // @since 3.0.0-beta2
//            EasyExcel.write(outputStream, clazz)
//                    .registerWriteHandler(new CellWriteHandler() {
//                        @Override
//                        public void afterCellDispose(CellWriteHandlerContext context) {
//                            // 当前事件会在 数据设置到poi的cell里面才会回调
//                            // 判断不是头的情况 如果是fill 的情况 这里会==null 所以用not true
//                            if (BooleanUtils.isNotTrue(context.getHead())) {
//                                // 第一个单元格
//                                // 只要不是头 一定会有数据 当然fill的情况 可能要context.getCellDataList() ,这个需要看模板，因为一个单元格会有多个 WriteCellData
//                                WriteCellData<?> cellData = context.getFirstCellData();
//                                // 这里需要去cellData 获取样式
//                                // 很重要的一个原因是 WriteCellStyle 和 dataFormatData绑定的 简单的说 比如你加了 DateTimeFormat
//                                // ，已经将writeCellStyle里面的dataFormatData 改了 如果你自己new了一个WriteCellStyle，可能注解的样式就失效了
//                                // 然后 getOrCreateStyle 用于返回一个样式，如果为空，则创建一个后返回
//                                WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
//                                writeCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
//                                // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND
//                                writeCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
//                                // 这样样式就设置好了 后面有个FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到 cell里面去 所以可以不用管了
//                            }
//                        }
//                    }).sheet(fileName)
//                    .doWrite(list);


            // 方法3: 使用poi的样式完全自己写 不推荐
            // @since 3.0.0-beta2
            // 坑1：style里面有dataformat 用来格式化数据的 所以自己设置可能导致格式化注解不生效
            // 坑2：不要一直去创建style 记得缓存起来 最多创建6W个就挂了
//            EasyExcel.write(outputStream, clazz)
//                    .registerWriteHandler(new CellWriteHandler() {
//                        @Override
//                        public void afterCellDispose(CellWriteHandlerContext context) {
//                            // 当前事件会在 数据设置到poi的cell里面才会回调
//                            // 判断不是头的情况 如果是fill 的情况 这里会==null 所以用not true
//                            if (BooleanUtils.isNotTrue(context.getHead())) {
//                                Cell cell = context.getCell();
//                                // 拿到poi的workbook
//                                Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
//                                // 这里千万记住 想办法能复用的地方把他缓存起来 一个表格最多创建6W个样式
//                                // 不同单元格尽量传同一个 cellStyle
//                                CellStyle cellStyle = workbook.createCellStyle();
//                                cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
//                                // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND
//                                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//                                cell.setCellStyle(cellStyle);
//
//                                // 由于这里没有指定dataformat 最后展示的数据 格式可能会不太正确
//
//                                // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
//                                // cell里面去 会导致自己设置的不一样
//                                context.getFirstCellData().setWriteCellStyle(null);
//                            }
//                        }
//                    }).sheet(fileName)
//                    .doWrite(list);

            outputStream.flush();
        } catch (Exception e) {
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

    public static void mergeWrite(HttpServletResponse response, List<?> list, String fileName, Class clazz) {
        OutputStream outputStream = null;
        try {
            outputStream = getOutputStream(response, fileName);

            // 方法1 注解
            // 在DemoStyleData里面加上ContentLoopMerge注解
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
//            EasyExcel.write(outputStream, clazz).sheet(fileName).doWrite(list);

            // 方法2 自定义合并单元格策略
            // 每隔2行会合并 把eachRow 设置成 2 也就是我们数据的长度，所以就第一列会合并。当然其他合并策略也可以自己写
            LoopMergeStrategy loopMergeStrategy = new LoopMergeStrategy(2, 0);
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            //多个策略可以一直.registerWriteHandler
            EasyExcel.write(outputStream, clazz).registerWriteHandler(loopMergeStrategy).sheet(fileName).doWrite(list);
            outputStream.flush();
        } catch (Exception e) {
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

    public static void tableWrite(HttpServletResponse response, List<?> list, String fileName, Class clazz) {

        OutputStream outputStream = null;
        try {
            outputStream = getOutputStream(response, fileName);

            // 方法1 这里直接写多个table的案例了，如果只有一个 也可以直一行代码搞定，参照其他案
            // 这里 需要指定写用哪个class去写
            try (ExcelWriter excelWriter = EasyExcel.write(outputStream, clazz).build()) {
                // 把sheet设置为不需要头 不然会输出sheet的头 这样看起来第一个table 就有2个头了
                WriteSheet writeSheet = EasyExcel.writerSheet(fileName).needHead(Boolean.FALSE).build();
                // 这里必须指定需要头，table 会继承sheet的配置，sheet配置了不需要，table 默认也是不需要
                WriteTable writeTable0 = EasyExcel.writerTable(0).needHead(Boolean.TRUE).build();
                WriteTable writeTable1 = EasyExcel.writerTable(1).needHead(Boolean.TRUE).build();
                // 第一次写入会创建头
                excelWriter.write(list, writeSheet, writeTable0);
                // 第二次写如也会创建头，然后在第一次的后面写入数据
                excelWriter.write(list, writeSheet, writeTable1);
            }
            outputStream.flush();
        } catch (Exception e) {
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

    public static void dynamicHeadWrite(HttpServletResponse response, List<?> list, String fileName, Class clazz) {
        OutputStream outputStream = null;
        try {
            outputStream = getOutputStream(response, fileName);
            EasyExcel.write(outputStream, clazz)
                    // 这里放入动态头
                    .head(head()).sheet(fileName)
                    // 当然这里数据也可以用 List<List<String>> 去传入
                    .doWrite(list);
            outputStream.flush();
        } catch (Exception e) {
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

    private static List<List<String>> head() {
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> head0 = new ArrayList<String>();
        head0.add("字符串" + System.currentTimeMillis());
        List<String> head1 = new ArrayList<String>();
        head1.add("数字" + System.currentTimeMillis());
        List<String> head2 = new ArrayList<String>();
        head2.add("日期" + System.currentTimeMillis());
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }

    public static void longestMatchColumnWidthWrite(HttpServletResponse response, List<?> list, String fileName, Class clazz) {
        OutputStream outputStream = null;
        try {
            outputStream = getOutputStream(response, fileName);
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            EasyExcel.write(outputStream, clazz).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet(fileName).doWrite(list);

            outputStream.flush();
        } catch (Exception e) {
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

    public static void customHandlerWrite(HttpServletResponse response, List<?> list, String fileName, Class clazz) {
        OutputStream outputStream = null;
        try {
            outputStream = getOutputStream(response, fileName);
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            EasyExcel.write(outputStream, clazz).registerWriteHandler(new CustomSheetWriteHandler())
                    .registerWriteHandler(new CustomCellWriteHandler()).sheet(fileName).doWrite(data());

            outputStream.flush();
        } catch (Exception e) {
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

    public static void commentWrite(HttpServletResponse response, List<?> list, String fileName, Class clazz) {
        OutputStream outputStream = null;
        try {
            outputStream = getOutputStream(response, fileName);
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            // 这里要注意inMemory 要设置为true，才能支持批注。目前没有好的办法解决 不在内存处理批注。这个需要自己选择。
            EasyExcel.write(outputStream,clazz).inMemory(Boolean.TRUE).registerWriteHandler(new CommentWriteHandler())
                    .sheet(fileName).doWrite(list);
            outputStream.flush();
        } catch (Exception e) {
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

    public static void variableTitleWrite(HttpServletResponse response, List<?> list, String fileName, Class clazz) {
        OutputStream outputStream = null;
        try {
            outputStream = getOutputStream(response, fileName);
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            EasyExcel.write(outputStream,clazz).head(variableTitleHead()).sheet(fileName).doWrite(convertData());
            outputStream.flush();
        } catch (Exception e) {
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

    private static List<List<String>> variableTitleHead() {
        List<List<String>> list = ListUtils.newArrayList();
        List<String> head0 = ListUtils.newArrayList();
        head0.add("string" + System.currentTimeMillis());
        List<String> head1 = ListUtils.newArrayList();
        head1.add("number" + System.currentTimeMillis());
        List<String> head2 = ListUtils.newArrayList();
        head2.add("date" + System.currentTimeMillis());
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }
}
