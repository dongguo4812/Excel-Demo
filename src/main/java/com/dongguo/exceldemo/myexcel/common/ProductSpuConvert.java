package com.dongguo.exceldemo.myexcel.common;


import com.dongguo.exceldemo.easyexcel.entity.ProductSpu;

import com.dongguo.exceldemo.myexcel.entity.ProductExportMyExcelVO;
import com.dongguo.exceldemo.myexcel.entity.ProductUploadMyExcelVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductSpuConvert {
    ProductSpuConvert INSTANCE = Mappers.getMapper(ProductSpuConvert.class);
    ProductExportMyExcelVO poToVoMyExcel(ProductSpu po);


    ProductSpu voToPo(ProductUploadMyExcelVO vo);

    List<ProductSpu> voToPoList(List<ProductUploadMyExcelVO> vo);

}
