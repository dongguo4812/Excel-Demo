package com.dongguo.exceldemo.easyexcel.convert;

import com.dongguo.exceldemo.easyexcel.entity.ProductExportVO;
import com.dongguo.exceldemo.easyexcel.entity.ProductSpu;
import com.dongguo.exceldemo.myexcel.entity.ProductExportMyExcelVO;
import com.dongguo.exceldemo.myexcel.entity.ProductUploadMyExcelVO;
import com.dongguo.exceldemo.easyexcel.entity.ProductUploadVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author dongguo
 * @date 2023/2/3
 * @description:
 */
@Mapper
public interface ProductSpuConvert {
    ProductSpuConvert INSTANCE = Mappers.getMapper(ProductSpuConvert.class);
    ProductSpu voToPo(ProductExportVO vo);
    ProductExportVO poToVo(ProductSpu po);
    ProductSpu voToPo(ProductUploadVO vo);
    List<ProductSpu> voToPoListV3(List<ProductUploadVO> boList);

}
