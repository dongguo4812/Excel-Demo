package com.dongguo.exceldemo.myexcel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dongguo.exceldemo.easyexcel.entity.ProductSpu;
import com.dongguo.exceldemo.myexcel.entity.Image;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author dongguo
 * @date 2023/4/15
 * @description:
 */
@Mapper
public interface MyExcelImageMapper extends BaseMapper<Image> {
}
