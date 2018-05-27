package cn.itcast.bos.action.base;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.xml.resolver.apps.resolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.action.base.utils.BaseAction;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;

@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
public class AreaAction extends BaseAction<Area>{
	//注入service层
	@Autowired
	private AreaService areaService ;
	public void setAreaService(AreaService areaService) {
		this.areaService = areaService;
	}
	
	//分页显示+条件查询分页显示
	@Action(value="area_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		//封装分页参数
		Pageable pageable=new PageRequest(page-1, rows);
		//构造条件查询
		Specification<Area> specification=new Specification<Area>() {

			@Override
			public Predicate toPredicate(Root<Area> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				if (StringUtils.isNotBlank(model.getProvince())) {
					Predicate p1 = cb.like(root.get("province")
							.as(String.class), "%" + model.getProvince() + "%");
					list.add(p1);
				}
				if (StringUtils.isNotBlank(model.getCity())) {
					Predicate p2 = cb.like(root.get("city").as(String.class),
							"%" + model.getCity() + "%");
					list.add(p2);
				}
				if (StringUtils.isNotBlank(model.getDistrict())) {
					Predicate p3 = cb.like(root.get("district")
							.as(String.class), "%" + model.getDistrict() + "%");
					list.add(p3);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		// 调用业务层完成查询
		Page<Area> pageData = areaService.findPageData(specification, pageable);
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
	
	
	
	//属性驱动获取上传的文件
	private File file;
	public void setFile(File file) {
		this.file = file;
	}
	/**
	 * 一键批量导入区域数据,
	 */
	@Action(value="area_batchImport")
	public String batchImport() throws FileNotFoundException, IOException{
		//System.out.println("一键导入方法执行了");
		//创建一个容器，解析出来的数据封装成对象存入容器，最后遍历存入
		List<Area> areas=new ArrayList<Area>();
		//基于解析代码逻辑
		//基于。xls格式解析HSSF
		//1，加载excel文件
		HSSFWorkbook hssfWorkbook=new HSSFWorkbook(new FileInputStream(file));
		//2，读取一个sheet,的第一页
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
			Area area=new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			//利用pinyin4J
			String province = area.getProvince();//获得省份
			 String city = area.getCity();//获得城市
			 String district = area.getDistrict();//获得区
			 //利用字符串切割掉省,市..最后一个字.
			 province = province.substring(0, province.length()-1);
			 city = city.substring(0, city.length()-1);
			 district = district.substring(0, district.length()-1);
			 //生成简码,只有首字母
			 //1,获取每个汉字首字母的数组
			 String[] headByString = PinYin4jUtils.getHeadByString(province+city+district);
			//2-创建StringBuffer拼接
			 StringBuffer buffer=new StringBuffer();
			for (String string : headByString) {
				buffer.append(string);
			}
			//3-将拼接好的转成字符串
			String shortcode = buffer.toString();
			//4-给对象赋值
			area.setShortcode(shortcode);
			
			//城市编码
			String citycode = PinYin4jUtils.hanziToPinyin(city);
			area.setCitycode(citycode);
			 //将对象放入集合
			areas.add(area);
		}
		areaService.saveBatch(areas);
		return NONE;
	}
	
	
}
