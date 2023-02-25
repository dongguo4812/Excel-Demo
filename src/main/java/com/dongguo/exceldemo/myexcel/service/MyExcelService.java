package com.dongguo.exceldemo.myexcel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dongguo.exceldemo.easyexcel.entity.ProductSpu;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author dongguo
 * @date 2023/2/24
 * @description:
 */
public interface MyExcelService extends IService<ProductSpu> {
    void upload(MultipartFile file);

    void saxUpload(MultipartFile file);

    void defaultExport(HttpServletResponse response);

    void streamExport(HttpServletResponse response);
}
