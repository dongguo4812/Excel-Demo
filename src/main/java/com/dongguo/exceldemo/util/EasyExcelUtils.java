package com.dongguo.exceldemo.util;


import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dongguo
 * @date 2023/2/3
 * @description:
 */
public class EasyExcelUtils {

    /**
     * 导出通用接口  文件下载（失败了会返回一个有部分数据的Excel）
     * @param list
     * @param fileName
     * @param clzz
     */
    public static void export(HttpServletResponse response, List<?> list, String fileName, Class clzz){
        OutputStream outputStream =null;
        try {
            fileName = encodeFileName(fileName);
            outputStream = getOutputStream(response, fileName);

            EasyExcel.write(outputStream, clzz).sheet(fileName).doWrite(list);
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
     * @param list
     * @param fileName
     * @param clzz
     */
    public static void exportSafe(HttpServletResponse response, List<?> list, String fileName, Class clzz) {
        OutputStream outputStream =null;
        try {
            fileName = encodeFileName(fileName);
            outputStream = getOutputStream(response,fileName);
            // 这里需要设置不关闭流
            EasyExcel.write(outputStream, clzz).autoCloseStream(Boolean.FALSE).sheet(fileName).doWrite(list);
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
     * @param fileName
     * @return
     * @throws IOException
     */
    private static OutputStream getOutputStream(HttpServletResponse response,String fileName) throws IOException {
        OutputStream outputStream = response.getOutputStream();
        // 导出EXCEL
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
//        response.setContentType("application/vnd.ms-excel");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止浏览器端导出excel文件名中文乱码 当然和easyexcel没有关系
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        return outputStream;
    }

    /**
     * encode fileName
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String encodeFileName(String fileName) throws UnsupportedEncodingException {
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        return fileName;
    }
}
