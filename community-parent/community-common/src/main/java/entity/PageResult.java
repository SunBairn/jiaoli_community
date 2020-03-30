package entity;
public class PageResult<T> {

    private Long total;//总记录数
    private Page<T> page;//page对象

    public PageResult(Long total, Page<T> page) {
        this.total = total;
        this.page=page;
    }

    public PageResult() {
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }
}
