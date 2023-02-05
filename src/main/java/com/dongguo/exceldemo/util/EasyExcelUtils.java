package com.dongguo.exceldemo.util;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson2.JSON;
import com.dongguo.exceldemo.easyexcel.entity.ImageDemoData;
import com.dongguo.exceldemo.easyexcel.entity.ProductExportVO;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

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
    private static OutputStream getOutputStream(HttpServletResponse response, String fileName) throws IOException {

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
//            try (ExcelWriter excelWriter = EasyExcel.write(outputStream, clazz).build()) {
//                // 这里注意 如果同一个sheet只要创建一次
//                WriteSheet writeSheet = EasyExcel.writerSheet(fileName).build();
//                // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来
//
//                //获得导出数据的条数，并根据总条数和一页显示条数得到页数
//                for (int i = 0; i < 5; i++) {
//                    // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
//                    List<ProductExportVO> data = (List<ProductExportVO>) list;
//                    excelWriter.write(data, writeSheet);
//                }
//            }

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
            try (ExcelWriter excelWriter = EasyExcel.write(outputStream).build()) {
                // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
                for (int i = 0; i < 5; i++) {
                    // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样。这里注意DemoData.class 可以每次都变，我这里为了方便 所以用的同一个class
                    // 实际上可以一直变
                    WriteSheet writeSheet = EasyExcel.writerSheet(i, fileName + i).head(clazz).build();
                    // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                    List<ProductExportVO> data = (List<ProductExportVO>) list;
                    excelWriter.write(data, writeSheet);
                }
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
            EasyExcel.write(outputStream, ImageDemoData.class).sheet().doWrite(list);
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
}
