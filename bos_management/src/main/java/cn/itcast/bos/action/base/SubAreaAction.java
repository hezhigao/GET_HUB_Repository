package cn.itcast.bos.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.action.base.utils.BaseAction;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.utils.PinYin4jUtils;

@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
public class SubAreaAction extends BaseAction<SubArea> {

	private static final long serialVersionUID = -3405334178497345577L;
	/**
	 * 一键批量导入分区域数据,
	 */
	//属性驱动获取上传的文件
		private File file;
		public void setFile(File file) {
			this.file = file;
		}
	/*@Action(value="subArea_batchImport")
	public String batchImport() throws FileNotFoundException, IOException{
		//System.out.println("一键导入方法执行了");
		//创建一个容器，解析出来的数据封装成对象存入容器，最后遍历存入
		List<SubArea> subAreas=new ArrayList<Area>();
		//基于解析代码逻辑
		//基于。xls格式解析HSSF
		//1，加载excel文件
		HSSFWorkbook hssfWorkbook=new HSSFWorkbook(new FileInputStream(file));
		//2，读取一个sheet
		HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
		//3,读取每一行
		for (Row row : sheetAt) {
			//跳过第一行的数据，因为一班第一行是标题
			if(row.getRowNum()==0){
				continue;
			}
			//跳过空行
			if(row.getCell(0)==null&&
					StringUtils.isNotBlank(row.getCell(0).getStringCellValue())){
				continue;
			};
			//创建对象封装解析的数据
			SubArea subArea=new SubArea();
								//private String id;
								//private String startNum; // 起始号
								//private String endNum; // 终止号
								//private Character single; // 单双号
								//private String keyWords; // 关键字
								//private String assistKeyWords; // 辅助关键字
								//private Area area; // 区域
								//private FixedArea fixedArea; // 定区
			
			subAreas.add(area);
		}
		//将封装好的list调用方法保存
		areaService.saveBatch(areas);
		return NONE;
	}*/
	
	
}
