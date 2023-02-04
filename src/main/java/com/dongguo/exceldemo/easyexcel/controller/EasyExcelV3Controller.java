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
     * 上传  最简单的导入
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
    public String upuploadReadAllSheetload(MultipartFile file)throws IOException {
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
     * 导出
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response){
        easyExcelV3Service.export(response);
    }
}
