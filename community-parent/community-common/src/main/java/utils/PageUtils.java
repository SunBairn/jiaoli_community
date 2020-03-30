package utils;

import entity.Page;
import entity.PageResult;

/**
 * 封装分页工具
 */
public class PageUtils {
    // 将数据封装到PageResult中
    public static PageResult packagingPageResult(Page page){
        PageResult pageResult = new PageResult<>(page.getTotal(), page);
        return pageResult;
    }

}
