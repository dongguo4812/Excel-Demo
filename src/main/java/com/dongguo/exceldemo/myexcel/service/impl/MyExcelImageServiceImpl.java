package com.dongguo.exceldemo.myexcel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dongguo.exceldemo.easyexcel.entity.ProductSpu;
import com.dongguo.exceldemo.myexcel.entity.Image;
import com.dongguo.exceldemo.myexcel.mapper.MyExcelImageMapper;
import com.dongguo.exceldemo.myexcel.mapper.MyExcelMapper;
import com.dongguo.exceldemo.myexcel.service.MyExcelImageService;
import com.dongguo.exceldemo.myexcel.service.MyExcelService;
import com.github.liaochong.myexcel.core.DefaultExcelReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dongguo
 * @date 2023/4/15
 * @description:
 */
@Service
public class MyExcelImageServiceImpl extends ServiceImpl<MyExcelImageMapper, Image> implements MyExcelImageService {
    @Override
    public void uploadImage(MultipartFile file) {
        try {
            List<Image> voList = new ArrayList<>();

            DefaultExcelReader.of(Image.class)
                    .sheet(0) // 0代表第一个，如果为0，可省略该操作，也可sheet("名称")读取
                    .rowFilter(row -> row.getRowNum() > 0) // 如无需过滤，可省略该操作，0代表第一行
                    .readThen(file.getInputStream(), vo -> {voList.add(vo);});// 可接收inputStream
            saveBatch(voList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
