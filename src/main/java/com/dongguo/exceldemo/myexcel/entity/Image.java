package com.dongguo.exceldemo.myexcel.entity;

import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import lombok.Data;

import java.io.InputStream;

/**
 * @author dongguo
 * @date 2023/4/15
 * @description:
 */
@Data
public class Image {
    @ExcelColumn(index=0)
    private  Long  id;
    @ExcelColumn(index=1)
    private  String name;
    @ExcelColumn(index=2)
    InputStream image;
}
