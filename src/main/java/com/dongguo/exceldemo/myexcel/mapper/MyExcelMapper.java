package com.dongguo.exceldemo.myexcel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dongguo.exceldemo.easyexcel.entity.ProductSpu;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author dongguo
 * @date 2023/2/24
 * @description:
 */
@Mapper
public interface MyExcelMapper extends BaseMapper<ProductSpu> {
}
