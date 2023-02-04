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
public interface EasyExcelService extends IService<ProductSpu> {
    /**
     * 导出
     */
    void export(HttpServletResponse response);

    void exportSafe(HttpServletResponse response);

    void upload(MultipartFile file);


}
