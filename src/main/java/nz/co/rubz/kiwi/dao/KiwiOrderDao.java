/**
 * Project Name:kw-server
 * File Name:KiwiOrderDao.java
 * Package Name:nz.co.rubz.kiwi.dao
 * Date:2018年4月23日下午3:57:09
 * Copyright (c) 2018, zjk All Rights Reserved.
 *
*/

package nz.co.rubz.kiwi.dao;
/**
 * ClassName:KiwiOrderDao <br/>
 * Function: 订单dao. <br/>
 * Date:     2018年4月23日 下午3:57:09 <br/>
 * @author   zhangjiakun
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import nz.co.rubz.kiwi.model.Order;
@Repository
public class KiwiOrderDao extends BasicDAO<Order, Datastore> {
	private Logger log = Logger.getLogger(KiwiOrderDao.class);

	@Autowired(required = true)
	public KiwiOrderDao(@Qualifier("dataStore") Datastore ds) {
		super(ds);
	}

	/**
	 * 
	 * findById:根据Id查询. <br/>
	 *
	 * @author zhangjiakun
	 * @param string
	 * @return
	 * @since JDK 1.8
	 */
	public Order findById(String id) {

		if (ObjectId.isValid(id)) {
			return findOne("_id", new ObjectId(id));
		}
		return null;
	}

	/**
	 * 
	 * findByIds:根据ids查询. <br/>
	 *
	 * @author zhangjiakun
	 * @param string
	 * @return
	 * @since JDK 1.8
	 */
	public List<Order> findByIds(String ids) {
		Query<Order> query = getDatastore().createQuery(Order.class);
		String[] split = ids.split(",");
		List<String> listId = Arrays.asList(split);
		query.filter("_id in", listId);
		List<Order> list = query.asList();
		return list;
	}

	/**
	 * 
	 * updataStatus:根据id更新订单状态. <br/>
	 *
	 * @author zhangjiakun
	 * @param id
	 * @param value
	 * @return
	 * @since JDK 1.8
	 */
	public boolean updataStatus(String id, int value) {
		boolean result = false;
		Query<Order> q = getDatastore().find(Order.class).filter("_id", new ObjectId(id));
		UpdateOperations<Order> ops = getDatastore().createUpdateOperations(Order.class);
		ops.set("status", value);
		UpdateResults updateResult = update(q, ops);
		result = updateResult.getUpdatedExisting();
		return result;

	}

}
