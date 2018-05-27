package cn.itcast.bos.action.base.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javassist.expr.NewArray;

import org.springframework.data.domain.Page;

import cn.itcast.bos.domain.base.Standard;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public abstract class BaseAction<T> extends ActionSupport implements
		ModelDriven<T> {
	// 模型驱动
	protected T model;

	@Override
	public T getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	// 创建方法，实例化model
	public BaseAction() {
		// 获取继承此类的泛型
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		// 获取泛型类型的第一个参数
		ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
		Class<T> class1 = (Class<T>) parameterizedType.getActualTypeArguments()[0];
		try {
			model = class1.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			System.out
					.println("模型驱动注入失败cn.itcast.bos.action.base.utils.BaseAction.java");
			e.printStackTrace();
		}

	}

	// 接受分页查询参数
	protected int page;
	protected int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	// 将分页结果压入值站
	protected void pushPageDataToValueStack(Page<T> pageDate) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (pageDate == null) {
			result.put("total",0);
			result.put("rows",new ArrayList<>());
		} else {
			result.put("total", pageDate.getTotalElements());
			result.put("rows", pageDate.getContent());
		}
		ActionContext.getContext().getValueStack().push(result);
	}

}
