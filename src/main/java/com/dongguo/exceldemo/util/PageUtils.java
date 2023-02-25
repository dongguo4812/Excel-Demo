package com.dongguo.exceldemo.util;

import java.util.List;

/**
 * @author dongguo
 * @date 2023/2/25
 * @description:
 */
public class PageUtils {

    /**
     * @param list     分页前的集合
     * @param pageNum  页码
     * @param pageSize 页数
     * @param <T>
     * @return 分页后的集合
     */
    public static <T> List<T> pageList(List<T> list, int pageNum, int pageSize) {
        //计算总页数
        int page = list.size() % pageSize == 0 ? list.size() / pageSize : list.size() / pageSize + 1;
        //兼容性分页参数错误
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageNum = pageNum >= page ? page : pageNum;
        // 开始索引
        int begin = 0;
        // 结束索引
        int end = 0;
        if (pageNum != page) {
            begin = (pageNum - 1) * pageSize;
            end = begin + pageSize;
        } else {
            begin = (pageNum - 1) * pageSize;
            end = list.size();
        }
        return list.subList(begin, end);
    }
}
