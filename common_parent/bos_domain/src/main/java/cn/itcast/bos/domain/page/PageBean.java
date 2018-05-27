package cn.itcast.bos.domain.page;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.transport.servlet.CXFServlet;

import cn.itcast.bos.domain.take_delivery.Promotion;
@XmlRootElement(name="pageBean")
@XmlSeeAlso({Promotion.class})
public class PageBean<T> {

	private long totalcount;//总数量
	private List<T> pageData;//当前页的数据
	public long getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(long totalcount) {
		this.totalcount = totalcount;
	}
	public List<T> getPageData() {
		return pageData;
	}
	public void setPageData(List<T> pageData) {
		this.pageData = pageData;
	}
	
	
}
