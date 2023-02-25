package com.dongguo.exceldemo.myexcel.controller;

import com.dongguo.exceldemo.myexcel.service.MyExcelService;
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
 * @date 2023/2/24
 * @description:
 */
@RestController
@RequestMapping("/myExcel")
public class MyExcelController {

    @Autowired
    private MyExcelService myExcelService;
    /**
     * 上传
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    @ResponseBody
    public String upload(MultipartFile file)throws IOException {
        myExcelService.upload(file);
        return"success";
    }

    /**
     * sax上传
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/saxUpload")
    @ResponseBody
    public String saxUpload(MultipartFile file)throws IOException {
        myExcelService.saxUpload(file);
        return"success";
    }

    /**
     * 默认导出
     * @param response
     */
    @PostMapping("/defaultExport")
    public void defaultExport(HttpServletResponse response) {
        myExcelService.defaultExport(response);
    }
    /**
     * 流式导出
     * @param response
     */
    @PostMapping("/streamExport")
    public void streamExport(HttpServletResponse response) {
        myExcelService.streamExport(response);
    }
}
