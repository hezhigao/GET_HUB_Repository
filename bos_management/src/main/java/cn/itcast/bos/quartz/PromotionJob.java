package cn.itcast.bos.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.dao.take_delivery.PromotionRepository;
import cn.itcast.bos.service.take_delivery.PromotionService;

/**
 * 根据活动的有效时间,定时更新活动的状态.
 * 
 * @author lenovo
 *
 */
public class PromotionJob implements Job {
	@Autowired
	private PromotionService promotionService;
	
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
System.out.println("活動過期處理程序執行");
		// 每分钟比对一下,看看现在的时间是否大于活动的时间,如果大于,需要将活动的状态改为已结束
promotionService.updateStatus(new Date());

	}

}
