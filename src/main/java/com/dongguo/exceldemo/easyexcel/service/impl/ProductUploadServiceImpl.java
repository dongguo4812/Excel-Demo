package com.dongguo.exceldemo.easyexcel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dongguo.exceldemo.easyexcel.common.BooleanTypeEnum;
import com.dongguo.exceldemo.easyexcel.convert.ProductSpuConvert;
import com.dongguo.exceldemo.easyexcel.entity.ProductSpu;
import com.dongguo.exceldemo.easyexcel.entity.ProductUploadVO;
import com.dongguo.exceldemo.easyexcel.mapper.ProductUploadMapper;
import com.dongguo.exceldemo.easyexcel.service.EasyExcelService;
import com.dongguo.exceldemo.easyexcel.service.ProductUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author dongguo
 * @date 2023/2/4
 * @description:
 */
@Service
public class ProductUploadServiceImpl  extends ServiceImpl<ProductUploadMapper, ProductUploadVO> implements ProductUploadService {

   @Autowired
   private EasyExcelService easyExcelService;
    @Override
    public void save(List<ProductUploadVO> list) {
        // 如果是mybatis,尽量别直接调用多次insert,自己写一个mapper里面新增一个方法batchInsert,所有数据一次性插入
        List<ProductSpu> spuList = new ArrayList<>(list.size());
        list.stream().forEach(uploadVO ->{
            ProductSpu spu = ProductSpuConvert.INSTANCE.voToPo(uploadVO);
            spu.setNeedCheck(BooleanTypeEnum.parseType(uploadVO.getNeedCheckStr()));
            spu.setNeedLedger(BooleanTypeEnum.parseType(uploadVO.getNeedLedgerStr()));
            spu.setIsStandard(BooleanTypeEnum.parseType(uploadVO.getIsStandardStr()));
            spu.setCategory(uploadVO.getBigCategory() +"/"+ uploadVO.getSmallCategory());
            spuList.add(spu);
        });
        easyExcelService.saveBatch(spuList);
    }

    @Override
    public void saveMap(List<Map<Integer, String>> cachedDataList) {
        cachedDataList.stream().forEach(map->{
           //存数据库
        });
    }
}
