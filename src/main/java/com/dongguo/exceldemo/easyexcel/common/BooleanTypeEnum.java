package com.dongguo.exceldemo.easyexcel.common;

import lombok.Getter;

/**
 * @author dongguo
 * @date 2023/2/3
 * @description:
 */
public enum BooleanTypeEnum {

    No(0, "否"),
    YES(1, "是"),
    ;

    @Getter
    private Integer type;
    @Getter
    private String desc;

    BooleanTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static String parseDesc(Integer type) {
        for (BooleanTypeEnum typeEnum : BooleanTypeEnum.values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum.getDesc();
            }
        }
        return null;
    }
    public static Integer parseType(String desc) {
        for (BooleanTypeEnum typeEnum : BooleanTypeEnum.values()) {
            if (typeEnum.getDesc().equals(desc)) {
                return typeEnum.getType();
            }
        }
        return null;
    }

}
