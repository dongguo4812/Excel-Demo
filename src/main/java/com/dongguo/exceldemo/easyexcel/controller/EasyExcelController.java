package com.dongguo.exceldemo.easyexcel.controller;

import com.dongguo.exceldemo.easyexcel.entity.ProductSpu;
import com.dongguo.exceldemo.easyexcel.service.EasyExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author dongguo
 * @date 2023/2/3
 * @description:
 */
@RestController
@RequestMapping("/easyExcel")
public class EasyExcelController {

    @Autowired
    private EasyExcelService easyExcelService;

    /**
     * 查询List
     * @return
     */
    @PostMapping("/getSpuList")
    public List<ProductSpu> getSpuList(){
       return easyExcelService.list();
    }

    /**
     * 导出
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response){
        easyExcelService.export(response);
    }

    /**
     * 导出，失败返回失败信息
     */
    @PostMapping("/exportSafe")
    public void exportSafe(HttpServletResponse response){
        easyExcelService.exportSafe(response);
    }

    /**
     * 上传
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    @ResponseBody
    public String upload(MultipartFile file)throws IOException {
        easyExcelService.upload(file);
        return"success";
    }

}
