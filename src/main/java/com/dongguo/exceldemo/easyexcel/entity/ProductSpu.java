package com.dongguo.exceldemo.easyexcel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;


import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@TableName("product_spu")
public class ProductSpu{
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String name;

    private Integer needCheck;

    private Integer productStatus;

    private Integer needLedger;

    private String taxCode;

    private BigDecimal taxRate;

    private String category;

    private String traceCode;

    private Integer delFlag;

    private Integer isStandard;

    private String alias;

    private String bigCategoryTaxCode;


    private String smallCategoryTaxCode;

    private BigDecimal bigCategoryTaxRate;

    private BigDecimal smallCategoryTaxRate;


    private String bigCategory;


    private String smallCategory;


    @TableField(exist = false)
    private Integer used;


    private String remark;

    private Integer examineStatus;
}
