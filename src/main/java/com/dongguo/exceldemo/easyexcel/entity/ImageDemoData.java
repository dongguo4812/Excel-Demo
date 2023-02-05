package com.dongguo.exceldemo.easyexcel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.converters.string.StringImageConverter;
import com.alibaba.excel.metadata.data.WriteCellData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

@Data
@EqualsAndHashCode
@ContentRowHeight(100)
@ColumnWidth(100 / 8)
public class ImageDemoData {
//    private File file;
//    private InputStream inputStream;
    /**
     * 如果string类型 必须指定转换器，string默认转换成string
     */
//    @ExcelProperty(converter = StringImageConverter.class)
//    private String string;
//    private byte[] byteArray;
    /**
     * 根据url导出
     *
     * @since 2.1.1
     */
    @ExcelProperty(value = "通过URL导出图片")
    private URL url;

    /**
     * 根据文件导出 并设置导出的位置。
     *
     * @since 3.0.0-beta1
     */
//    private WriteCellData<Void> writeCellDataFile;
}
