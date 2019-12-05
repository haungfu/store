package sjzc.hf.store.utils;

public class PageBean {
    private int pageSize;
    private int pageNum;
    //排序字段
    private String sort;
    //排序方式（升序、降序）
    private String order;

    public PageBean() {
    }

    public PageBean(int pageSize, int pageNum, String sort, String order) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.sort = sort;
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
