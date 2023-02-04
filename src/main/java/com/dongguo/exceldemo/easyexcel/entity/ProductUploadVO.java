package com.dongguo.exceldemo.easyexcel.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author dongguo
 * @date 2023/2/4
 * @description:
 */
@Data
public class ProductUploadVO {
    @ExcelIgnore
    @TableId("id")
    private Long id;
    @ExcelProperty(index = 0, value = "产品大类")
    private String bigCategory;

    @ExcelProperty(index = 1, value = "产品小类")
    private String smallCategory;

    @ExcelProperty(index = 2, value = "产品名称")
    private String name;

    @ExcelProperty(index = 3, value = "大类税收分类编码")
    private String bigCategoryTaxCode;

    @ExcelProperty(index = 4, value = "小类税收分类编码")
    private String smallCategoryTaxCode;

    @ExcelProperty(index = 5, value = "商品税收分类编码")
    private String taxCode;

    @ExcelProperty(index = 6, value = "大类税率")
    private String bigCategoryTaxRate;

    @ExcelProperty(index = 7, value = "小类税率")
    private String smallCategoryTaxRate;

    @ExcelProperty(index = 8, value = "商品税率")
    private String taxRate;

    @ExcelProperty(index = 9, value = "产品检测")
    private String needCheckStr;

    @ExcelProperty(index = 10, value = "产品台账")
    private String needLedgerStr;

    @ExcelProperty(index = 11, value = "是否标品")
    private String isStandardStr;

    @ExcelProperty(index = 12, value = "产品别名")
    private String alias;

    @ExcelProperty(index = 13, value = "备注")
    private String remark;
}
