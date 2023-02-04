package com.dongguo.exceldemo.easyexcel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dongguo.exceldemo.easyexcel.entity.ProductUploadVO;

import java.util.List;
import java.util.Map;

/**
 * @author dongguo
 * @date 2023/2/4
 * @description:
 */
public interface ProductUploadService extends IService<ProductUploadVO> {
    void save(List<ProductUploadVO> list);

    void saveMap(List<Map<Integer, String>> cachedDataList);
}
