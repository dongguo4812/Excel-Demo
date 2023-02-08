package com.dongguo.exceldemo.util;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalUtil {

    private ThreadLocalUtil() {
    }

    /**
     * 注意右边new的不是原生的ThreadLocal，而是我自定义的MapThreadLocal，它继承自ThreadLocal
     */
    private final static ThreadLocal<Map<String, Object>> THREAD_CONTEXT = new MapThreadLocal();
    /**
     * 根据key获取value
     */
    public static Object get(String key) {
        return getContextMap().get(key);
    }

    /**
     * put操作，原理同上
     *
     * @param key
     * @param value
     */
    public static void put(String key, Object value) {
        getContextMap().put(key, value);
    }

    /**
     * 清除map里的某个值
     */
    public static Object remove(String key) {
        return getContextMap().remove(key);
    }

    /**
     * 清除整个Map<String, Object>
     */
    public static void remove() {
        getContextMap().clear();
    }

    /**
     * 从ThreadLocalMap中清除当前ThreadLocal存储的内容
     */
    public static void clear() {
        THREAD_CONTEXT.remove();
    }

    /**
     * 从ThreadLocalMap中获取Map<String, Object>
    */
    private static Map<String, Object> getContextMap() {
        return THREAD_CONTEXT.get();
    }

    /**
     * 重写原生ThreadLocal的initialValue()
     */
    private static class MapThreadLocal extends ThreadLocal<Map<String, Object>> {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<String, Object>(8) {
                private static final long serialVersionUID = 3637958959138295593L;
                @Override
                public Object put(String key, Object value) {
                    return super.put(key, value);
                }
            };
        }
    }
}
