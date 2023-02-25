package com.dongguo.exceldemo.myexcel.entity;

import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.github.liaochong.myexcel.core.annotation.ExcelModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author dongguo
 * @date 2023/2/25
 * @description:
 */
@Data
@Accessors(chain = true)
@ExcelModel(sheetName = "商品清单")
public class ProductExportMyExcelVO {

    @ExcelColumn(index = 2, title = "产品名称")
    private String name;

    @ExcelColumn(index = 0, title = "产品大类")
    private String bigCategory;

    @ExcelColumn(index = 1, title = "产品小类")
    private String smallCategory;

    @ExcelColumn(index = 3, title = "大类税收分类编码")
    private String bigCategoryTaxCode;

    @ExcelColumn(index = 4, title = "小类税收分类编码")
    private String smallCategoryTaxCode;

    @ExcelColumn(index = 5, title = "商品税收分类编码")
    private String taxCode;

    @ExcelColumn(index = 8, title = "商品税率")
    private String taxRate;

    @ExcelColumn(index = 6, title = "大类税率")
    private String bigCategoryTaxRate;

    @ExcelColumn(index = 7, title = "小类税率")
    private String smallCategoryTaxRate;

    @ExcelColumn(index = 9, title = "产品检测")
    private String needCheck;

    @ExcelColumn(index = 10, title = "产品台账")
    private String needLedger;

    @ExcelColumn(index = 11, title = "是否标品")
    private String isStandard;

    @ExcelColumn(index = 12, title = "产品别名")
    private String alias;

    @ExcelColumn(index = 13, title = "备注")
    private String remark;

    public ProductExportMyExcelVO getVo(){
        return this;
    }
}
