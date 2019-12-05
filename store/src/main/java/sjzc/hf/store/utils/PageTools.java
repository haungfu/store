package sjzc.hf.store.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageTools {

	// 逻辑分页
	public static Map<String, Object> page(List list, PageBean pageBean) {
		Map<String, Object> map = new HashMap<String, Object>();
		int pageNum = 1;
		int pageSize = 15;
		int total=0;
		if (pageBean != null) {
			if (pageBean.getPageNum() > 0) {
				pageNum = pageBean.getPageNum();
			}
			if (pageBean.getPageSize() > 0) {
				pageSize = pageBean.getPageSize();
			}
		}
		total=list.size();
		
		int min=(pageNum-1)*pageSize;
		int max=pageNum*pageSize;
		if(min>total) {
			max=total;
			if(min-max>=total) {
				min=max-total;
			}else {
				min=0;
			}
		}
		
		if(total<max) {
			max=total;
		}
		map.put("data", list.subList(min, max));
		map.put("total", total);
		return map;
	}
}
