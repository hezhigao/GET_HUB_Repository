package bos_management;

import org.apache.struts2.ServletActionContext;

public class test {

	public static void main(String[] args) {
		// String savePath=ServletActionContext.getServletContext().getRealPath("/pages");
		//String saveUrl = ServletActionContext.getRequest().getContextPath()+ "/upload/";
		System.out.println("aaaaa");
		System.out.println(ServletActionContext.getServletContext().getRealPath("/pages"));
	}
}
