package com.dongguo.exceldemo.easyexcel.controller;

import com.dongguo.exceldemo.easyexcel.service.EasyExcelService;
import com.dongguo.exceldemo.easyexcel.service.EasyExcelV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dongguo
 * @date 2023/2/4
 * @description: easyExcel 3.1.x - 3.2.x
 */
@RestController
@RequestMapping("/easyExcelV3")
public class EasyExcelV3Controller {

    @Autowired
    private EasyExcelV3Service easyExcelV3Service;

    /**
     * 最简单的导入
     * 所有导入工作表有一个行头，这是默认的，可以再导入时.headRowNumber(1)设置跳过的表头行数
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    @ResponseBody
    public String upload(MultipartFile file) throws IOException {
        easyExcelV3Service.upload(file);
        return "success";
    }

    /**
     * 导入 读多个sheet
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadReadAllSheet")
    @ResponseBody
    public String uploadReadAllSheet(MultipartFile file) throws IOException {
        easyExcelV3Service.uploadReadAllSheet(file);
        return "success";
    }

    /**
     * 导入多行头
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadComplexHeaderRead")
    @ResponseBody
    public String uploadComplexHeaderRead(MultipartFile file) throws IOException {
        easyExcelV3Service.uploadComplexHeaderRead(file);
        return "success";
    }

    /**
     * 同步的返回  获取所有的数据再进行导入
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/synchronousRead")
    @ResponseBody
    public String synchronousRead(MultipartFile file) throws IOException {
        easyExcelV3Service.synchronousRead(file);
        return "success";
    }

    /**
     * 导入 读取表头数据
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/headerRead")
    @ResponseBody
    public String headerRead(MultipartFile file) throws IOException {
        easyExcelV3Service.headerRead(file);
        return "success";
    }

    /**
     * 导入  额外信息（批注、超链接、合并单元格信息读取）
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/extraRead")
    @ResponseBody
    public String extraRead(MultipartFile file) throws IOException {
        easyExcelV3Service.extraRead(file);
        return "success";
    }

    /**
     * 导入 不创建对象的读
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/noModelRead")
    @ResponseBody
    public String noModelRead(MultipartFile file) throws IOException {
        easyExcelV3Service.noModelRead(file);
        return "success";
    }

    /**
     * 导入 最简单的写
     * 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入
     *
     * @param response
     */
    @PostMapping("/simpleWrite")
    public void simpleWrite(HttpServletResponse response) {
        easyExcelV3Service.simpleWrite(response);
    }

    /**
     * 导出  报错输出错误信息
     */
    @PostMapping("/exportSafe")
    public void exportSafe(HttpServletResponse response) {
        easyExcelV3Service.exportSafe(response);
    }

    /**
     * 导出   根据参数只导出指定列(实质上是排除指定列)
     * 和    在导出对象实体中使用@ExcelIgnore一个效果
     */
    @PostMapping("/excludeOrIncludeWrite")
    public void excludeOrIncludeWrite(HttpServletResponse response) {
        easyExcelV3Service.excludeOrIncludeWrite(response);
    }


    /**
     * 导出    重复多次写入(写到单个或者多个Sheet)
     */
    @PostMapping("/repeatedWrite")
    public void repeatedWrite(HttpServletResponse response) {
        easyExcelV3Service.repeatedWrite(response);
    }

    /**
     * 导出 图片导出
     */
    @PostMapping("/imageWrite")
    public void imageWrite(HttpServletResponse response) {
        easyExcelV3Service.imageWrite(response);
    }


    /**
     * 导出 超链接、备注、公式、指定单个单元格的样式、单个单元格多种样式
     * @param response
     */
    @PostMapping("/writeCellDataWrite")
    public void writeCellDataWrite(HttpServletResponse response) {
        easyExcelV3Service.writeCellDataWrite(response);
    }

    /**
     * 导出 根据模板写入
     * @param response
     */
    @PostMapping("/templateWrite")
    public void templateWrite(HttpServletResponse response) {
        easyExcelV3Service.templateWrite(response);
    }

    /**
     * 导出    注解形式自定义样式
     * @param response
     */
    @PostMapping("/annotationStyleWrite")
    public void annotationStyleWrite(HttpServletResponse response) {
        easyExcelV3Service.annotationStyleWrite(response);
    }


    /**
     * 导出      自定义样式
     * @param response
     */
    @PostMapping("/handlerStyleWrite")
    public void handlerStyleWrite(HttpServletResponse response) {
        easyExcelV3Service.handlerStyleWrite(response);
    }

    /**
     * 导出 合并单元格  覆盖行或列
     * 属性注解 将该列 每两行并成一个单元格  @ContentLoopMerge(eachRow = 2)   两列合并@ContentLoopMerge(columnExtend = 2)
     *
     * 类注解  将第6-7行的2-3列合并成一个单元格   @OnceAbsoluteMerge(firstRowIndex = 5, lastRowIndex = 6, firstColumnIndex = 1, lastColumnIndex = 2)
     * @param response
     */
    @PostMapping("/mergeWrite")
    public void mergeWrite(HttpServletResponse response) {
        easyExcelV3Service.mergeWrite(response);
    }

    /**
     * 导出 使用table去写入   一个sheet有多个表头
     * @param response
     */
    @PostMapping("/tableWrite")
    public void tableWrite(HttpServletResponse response) {
        easyExcelV3Service.tableWrite(response);
    }


    /**
     * 导出  动态头，实时生成头写入
     * @param response
     */
    @PostMapping("/dynamicHeadWrite")
    public void dynamicHeadWrite(HttpServletResponse response) {
        easyExcelV3Service.dynamicHeadWrite(response);
    }
    /**
     * 导出自动列宽(不太精确)   不好用
     * @param response
     */
    @PostMapping("/longestMatchColumnWidthWrite")
    public void longestMatchColumnWidthWrite(HttpServletResponse response) {
        easyExcelV3Service.longestMatchColumnWidthWrite(response);
    }


    /**
     * 导出    自定义拦截器（上面几点都不符合但是要对单元格进行操作的参照这个）
     * @param response
     */
    @PostMapping("/customHandlerWrite")
    public void customHandlerWrite(HttpServletResponse response) {
        easyExcelV3Service.customHandlerWrite(response);
    }

    /**
     * 导出     插入批注
     * @param response
     */
    @PostMapping("/commentWrite")
    public void commentWrite(HttpServletResponse response) {
        easyExcelV3Service.commentWrite(response);
    }

    /**
     * 导出      可变标题处理(包括标题国际化等)
     * @param response
     */
    @PostMapping("/variableTitleWrite")
    public void variableTitleWrite(HttpServletResponse response) {
        easyExcelV3Service.variableTitleWrite(response);
    }
}
