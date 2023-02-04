package com.dongguo.exceldemo.easyexcel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dongguo.exceldemo.easyexcel.entity.ProductSpu;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * @author dongguo
 * @date 2023/2/3
 * @description:
 */
public interface EasyExcelV3Service extends IService<ProductSpu> {
    /**
     * 简单导入
     * @param file
     */
    void upload(MultipartFile file);

    /**
     * 导入读多个sheet
     * @param file
     */
    void uploadReadAllSheet(MultipartFile file);

    /**
     * 多行头导入
     * @param file
     */
    void uploadComplexHeaderRead(MultipartFile file);


    /**
     * 导出
     */
    void export(HttpServletResponse response);

    void exportSafe(HttpServletResponse response);
}
