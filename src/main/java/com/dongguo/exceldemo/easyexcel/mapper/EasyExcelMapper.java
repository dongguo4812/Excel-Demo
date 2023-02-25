package com.dongguo.exceldemo.easyexcel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dongguo.exceldemo.easyexcel.entity.ProductSpu;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @author dongguo
 * @date 2023/2/3
 * @description:
 */
@Mapper
public interface EasyExcelMapper extends BaseMapper<ProductSpu> {
    /**
     * 批量插入（mysql）
     * @param entityList
     * @return
     */
    Integer insertBatchSomeColumn(List<ProductSpu> entityList);
}
