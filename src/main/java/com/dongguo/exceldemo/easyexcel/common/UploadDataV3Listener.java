package com.dongguo.exceldemo.easyexcel.common;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.dongguo.exceldemo.easyexcel.entity.ProductUploadVO;
import com.dongguo.exceldemo.easyexcel.service.ProductUploadService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
@Slf4j
public class UploadDataV3Listener implements ReadListener<ProductUploadVO> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;

    private int TOTAL_COUNT = 0;
    /**
     * 缓存的数据
     */
    private List<ProductUploadVO> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private ProductUploadService productUploadService;

    public UploadDataV3Listener() {
        // 这里是demo，所以随便new一个。实际使用如果到了spring,请使用下面的有参构造函数
    }

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param productUploadService
     */
    public UploadDataV3Listener(ProductUploadService productUploadService) {
        this.productUploadService = productUploadService;
    }

    /**
     * 这个每一条数据解析都会来调用  用于读取数据
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(ProductUploadVO data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 这里会一行行的返回头  用于读取表头数据
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        // 如果想转成成 Map<Integer,String>
        // 方案1： 不要implements ReadListener 而是 extends AnalysisEventListener
        // 方案2： 调用 ConverterUtils.convertToStringMap(headMap, context) 自动会转换
        Map<Integer, String> map = ConverterUtils.convertToStringMap(headMap, context);
        log.info("解析到一条头数据:{}", JSON.toJSONString(map));
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
        cachedDataList.clear();
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        TOTAL_COUNT += cachedDataList.size();
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        productUploadService.save(cachedDataList);
        log.info("存储数据库成功！当前第{}条数据", TOTAL_COUNT);
    }
}
