package com.dongguo.exceldemo.easyexcel.service.impl;

import com.dongguo.exceldemo.easyexcel.entity.DemoData;
import com.dongguo.exceldemo.easyexcel.entity.ProductSpu;
import com.dongguo.exceldemo.easyexcel.mapper.EasyExcelMapper;
import com.dongguo.exceldemo.easyexcel.service.EasyExcelService;
import com.dongguo.exceldemo.easyexcel.service.ProductUploadService;
import com.dongguo.exceldemo.easyexcel.service.TestService;
import com.dongguo.exceldemo.util.ThreadLocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dongguo
 * @date 2023/2/8
 * @description:
 */
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private EasyExcelService easyExcelService;

    @Autowired
    private EasyExcelMapper easyExcelMapper;
    @Override
    public void getBatchId() {
        Object batchId = ThreadLocalUtil.get("batchId");
        System.out.println(batchId);
        ThreadLocalUtil.remove("batchId");
        batchId = ThreadLocalUtil.get("batchId");
        System.out.println(batchId);
    }

    @Override
    public void saveBatch() {
//        DemoData data = new DemoData();
        List<ProductSpu> spuList = new ArrayList<>();
      for(int i =1;i<5 ;i++){
          ProductSpu spu = new ProductSpu();
          spu.setName("商品"+i);
          spuList.add(spu);


      }
//        List<DemoData> dataList = spuList.stream().map(spu -> {
//            DemoData data1 = new DemoData();
//            BeanUtils.copyProperties(data,data1);
//            data1.setString(spu.getName());
//            return data1;
//        }).collect(Collectors.toList());
//
//        System.out.println(dataList);

        easyExcelService.saveBatch(spuList);
//        easyExcelMapper.insertBatchSomeColumn(spuList);
        System.out.println(spuList);
    }
}
