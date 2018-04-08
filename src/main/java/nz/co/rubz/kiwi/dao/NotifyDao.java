package nz.co.rubz.kiwi.dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mongodb.WriteResult;

import nz.co.rubz.kiwi.annotations.Config;
import nz.co.rubz.kiwi.bean.Notification;

@Repository
public class NotifyDao extends BasicDAO<Notification, Datastore> {

	private Logger log = Logger.getLogger(NotifyDao.class);

	@Autowired(required = true)
	public NotifyDao(@Qualifier("dataStore") Datastore ds) {
		super(ds);
	}

	@Config("page_size")
	private int pageSize = 20;
	
	public boolean deleteNoti(String notiId) {
		Query<Notification> q = getDs().createQuery(Notification.class);
		q.field("_id").equal(new ObjectId(notiId));
		WriteResult results = deleteByQuery(q);
		return results.getN() > 0 ? true : false;
	}

	public List<Notification> findNotisContainsClassId(String classId,int offset) {
		// mongodb 查询的值必须是常量，未提供
		// db.notify.find({comments.relay:this.comments.relay_to}) 的查询
		// 取出comment时应该过滤，这个值不是所有的。
		Query<Notification> q = getDs().createQuery(Notification.class);
		q.field("class_id").equal(classId);
		q.order("-ctime");
		if(offset >=0){
			q.offset(offset);
			q.limit(pageSize);
		}
		QueryResults<Notification> result = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return result.asList();
	}
	
	public List<Notification> findNotisByClassId(String classId,int offset) {
		// mongodb 查询的值必须是常量，未提供
		// db.notify.find({comments.relay:this.comments.relay_to}) 的查询
		// 取出comment时应该过滤，这个值不是所有的。
		Query<Notification> q = getDs().createQuery(Notification.class);
		q.field("class_id").equal(new String[]{classId});
		q.order("-ctime");
		if(offset >=0){
			q.offset(offset);
			q.limit(pageSize);
		}
		QueryResults<Notification> result = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return result.asList();
	}
	
	

	public Notification findNotiById(String notiId) {
		Query<Notification> q = getDs().createQuery(Notification.class);
		q.field("_id").equal(new ObjectId(notiId));
		QueryResults<Notification> result = find(q);
		return CollectionUtils.isEmpty(result.asList()) ? null : result.get();
	}

	public List<Notification> findNotisByClassIds(List<String> classIds,int offset) {
		StringBuilder sb = new StringBuilder("classIds : [");
		for (String a : classIds) {
			sb.append(a);
			sb.append(",");
		}
		sb.append(" ]");
		log.info(sb.toString());
		if (CollectionUtils.isEmpty(classIds)) {
			return null;
		}
		Query<Notification> q = getDs().createQuery(Notification.class);
		q.field("class_id").in(classIds);
		q.offset(offset);
		q.limit(pageSize);
		q.order("-ctime");
		QueryResults<Notification> result = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return result.asList();
	}

//	public boolean deleteNotiByClassId(String classId) {
//		List<String> cids = new ArrayList<String>();
//		cids.add(classId);
//		Query<Notification> q = getDs().createQuery(Notification.class);
//		q.field("class_id").equal(cids);
//		WriteResult result = deleteByQuery(q);
//		return result.getN()>0;
//	}

}