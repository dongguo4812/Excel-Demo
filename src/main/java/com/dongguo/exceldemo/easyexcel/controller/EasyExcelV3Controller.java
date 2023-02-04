package com.dongguo.exceldemo.easyexcel.controller;

import com.dongguo.exceldemo.easyexcel.service.EasyExcelService;
import com.dongguo.exceldemo.easyexcel.service.EasyExcelV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dongguo
 * @date 2023/2/4
 * @description: easyExcel 3.1.x - 3.2.x
 */
@RestController
@RequestMapping("/easyExcelV3")
public class EasyExcelV3Controller {

    @Autowired
    private EasyExcelV3Service easyExcelV3Service;

    /**
     * 最简单的导入
     *       所有导入工作表有一个行头，这是默认的，可以再导入时.headRowNumber(1)设置跳过的表头行数
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    @ResponseBody
    public String upload(MultipartFile file)throws IOException {
        easyExcelV3Service.upload(file);
        return"success";
    }

    /**
     * 导入 读多个sheet
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadReadAllSheet")
    @ResponseBody
    public String uploadReadAllSheet(MultipartFile file)throws IOException {
        easyExcelV3Service.uploadReadAllSheet(file);
        return"success";
    }

    /**
     * 导入多行头
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadComplexHeaderRead")
    @ResponseBody
    public String uploadComplexHeaderRead(MultipartFile file)throws IOException {
        easyExcelV3Service.uploadComplexHeaderRead(file);
        return"success";
    }

    /**
     * 同步的返回  获取所有的数据再进行导入
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/synchronousRead")
    @ResponseBody
    public String synchronousRead(MultipartFile file)throws IOException {
        easyExcelV3Service.synchronousRead(file);
        return"success";
    }
    /**
     * 读取表头数据
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/headerRead")
    @ResponseBody
    public String headerRead(MultipartFile file)throws IOException {
        easyExcelV3Service.headerRead(file);
        return"success";
    }

    /**
     * 额外信息（批注、超链接、合并单元格信息读取）
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/extraRead")
    @ResponseBody
    public String extraRead(MultipartFile file)throws IOException {
        easyExcelV3Service.extraRead(file);
        return"success";
    }

    /**
     * 不创建对象的读
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/noModelRead")
    @ResponseBody
    public String noModelRead(MultipartFile file)throws IOException {
        easyExcelV3Service.noModelRead(file);
        return"success";
    }

    /**
     * 导出
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response){
        easyExcelV3Service.export(response);
    }
}
