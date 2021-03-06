package cn.itcast.bos.service.take_delivery.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.take_delivery.WayBillService;

/**
 * WayBill运单Service
 * 
 * @author lenovo
 *
 */
@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
	// 注入WayBillRepository操作數據庫中的表
	@Autowired
	private WayBillRepository wayBillRepository;
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;

	/**
	 * 查詢所有運單,
	 */
	@Override
	public Page<WayBill> findPageData(WayBill wayBill, Pageable pageable) {
		// 判断WayBill 中条件是否存在
		if (StringUtils.isBlank(wayBill.getWayBillNum())
				&& StringUtils.isBlank(wayBill.getSendAddress())
				&& StringUtils.isBlank(wayBill.getRecAddress())
				&& StringUtils.isBlank(wayBill.getSendProNum())
				&& (wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)) {
			// 无条件查询 、查询数据库
			return wayBillRepository.findAll(pageable);
		} else {
			// 查询条件
			// must 条件必须成立 and
			// must not 条件必须不成立 not
			// should 条件可以成立 or
			BoolQueryBuilder query = new BoolQueryBuilder(); // 布尔查询 ，多条件组合查询
			// 向组合查询对象添加条件
			if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
				// 运单号查询
				QueryBuilder tempQuery = new TermQueryBuilder("wayBillNum",
						wayBill.getWayBillNum());
				query.must(tempQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
				// 发货地 模糊查询
				// 情况一： 输入"北" 是查询词条一部分， 使用模糊匹配词条查询
				QueryBuilder wildcardQuery = new WildcardQueryBuilder(
						"sendAddress", "*" + wayBill.getSendAddress() + "*");

				// 情况二： 输入"北京市海淀区" 是多个词条组合，进行分词后 每个词条匹配查询
				QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(
						wayBill.getSendAddress()).field("sendAddress")
						.defaultOperator(Operator.AND);

				// 两种情况取or关系
				BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
				boolQueryBuilder.should(wildcardQuery);
				boolQueryBuilder.should(queryStringQueryBuilder);

				query.must(boolQueryBuilder);
			}
			if (StringUtils.isNoneBlank(wayBill.getRecAddress())) {
				// 收货地 模糊查询
				QueryBuilder wildcardQuery = new WildcardQueryBuilder(
						"recAddress", "*" + wayBill.getRecAddress() + "*");
				query.must(wildcardQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
				// 速运类型 等值查询
				QueryBuilder termQuery = new TermQueryBuilder("sendProNum",
						wayBill.getSendProNum());
				query.must(termQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
				// 速运类型 等值查询
				QueryBuilder termQuery = new TermQueryBuilder("sendProNum",
						wayBill.getSendProNum());
				query.must(termQuery);
			}
			if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
				// 签收状态查询
				QueryBuilder termQuery = new TermQueryBuilder("signStatus",
						wayBill.getSignStatus());
				query.must(termQuery);
			}

			SearchQuery searchQuery = new NativeSearchQuery(query);
			searchQuery.setPageable(pageable); // 分页效果
			// 有条件查询 、查询索引库
			return wayBillIndexRepository.search(searchQuery);
		}

	}

	/**
	 * 保存運單,
	 */
	@Override
	public void save(WayBill wayBill) {
		// TODO Auto-generated method stub
		// 判断运单号是否存在
		WayBill persistWayBill = wayBillRepository.findByWayBillNum(wayBill
				.getWayBillNum());
		if (persistWayBill == null || persistWayBill.getId() == null) {
			// 运单不存在
			wayBillRepository.save(wayBill);
			// 保存索引
			wayBillIndexRepository.save(wayBill);
		} else {
			// 运单存在
			try {
				Integer id = persistWayBill.getId();
				BeanUtils.copyProperties(persistWayBill, wayBill);
				persistWayBill.setId(id);
				// 保存索引
				wayBillIndexRepository.save(persistWayBill);

			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
	}

	/**
	 * 根据WayBillNum(运单号)查询WayBill(运单)
	 */
	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		// TODO Auto-generated method stub
		return wayBillRepository.findByWayBillNum(wayBillNum);
	}
}
