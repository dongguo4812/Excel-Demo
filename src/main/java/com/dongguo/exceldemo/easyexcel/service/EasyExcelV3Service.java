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

    void synchronousRead(MultipartFile file);

    void headerRead(MultipartFile file);

    void extraRead(MultipartFile file);

    void noModelRead(MultipartFile file);

    void simpleWrite(HttpServletResponse response);

    void exportSafe(HttpServletResponse response);

    void excludeOrIncludeWrite(HttpServletResponse response);

    void repeatedWrite(HttpServletResponse response);

    void imageWrite(HttpServletResponse response);
}
