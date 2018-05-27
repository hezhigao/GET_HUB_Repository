package cn.itcast.bos.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.action.base.utils.BaseAction;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;

/**
 * 宣传活动的管理
 * 
 * @author lenovo
 *
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {
	// 定义一个file接受上传的宣传图片
	private File titleImgFile;

	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}

	// 属性注入图片名字
	private String titleImgFileFileName;

	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}

	// 注入Service层
	@Autowired
	private PromotionService promotionService;

	@Action(value = "promotion_save", results = { @Result(name = "success", type = "redirect", location = "./pages/take_delivery/promotion.html") })
	public String save() throws IOException {
		// 获取去绝对路径,保存图片时使用
		String savePath = ServletActionContext.getServletContext().getRealPath(
				"/upload/");
		// 获取相对路径,同一个服务下,可以根据相对路径获取图片
		String saveUrl = ServletActionContext.getRequest().getContextPath()
				+ "/upload/";
		// 生成随机图片名
		UUID uuid = UUID.randomUUID();
		// 获取文件扩展名,用于和生成的UUID拼接
		String ext = titleImgFileFileName.substring(titleImgFileFileName
				.lastIndexOf("."));
		// 拼接出来保存时文件的名字
		String randomFileName = uuid + ext;

		// 保存图片 (绝对路径)
		File destFile = new File(savePath + "/" + randomFileName);
		// System.out.println(destFile.getAbsolutePath());

		FileUtils.copyFile(titleImgFile, destFile);
		// 将保存图片的绝对路径或者相对路径保存到model中
		model.setTitleImg(ServletActionContext.getRequest().getContextPath()
				+ "/upload/" + randomFileName);
		// 调用Service保存
		promotionService.save(model);

		return SUCCESS;

	}
	//查询所有的活动
	@Action(value="promotion_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		//构建分页的参数
		Pageable pageable=new PageRequest(page-1, rows);
		//掉方法查询数据
		Page<Promotion> pageData=promotionService.findPageData(pageable);
		ServletActionContext.getContext().getValueStack().push(pageData);
		return SUCCESS;
	}
}
