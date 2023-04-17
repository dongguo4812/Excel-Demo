package com.dongguo.exceldemo.myexcel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dongguo.exceldemo.easyexcel.entity.ProductSpu;
import com.dongguo.exceldemo.myexcel.entity.Image;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author dongguo
 * @date 2023/4/15
 * @description:
 */
public interface MyExcelImageService extends IService<Image> {

    void uploadImage(MultipartFile file);
}
