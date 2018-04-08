package nz.co.rubz.kiwi.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import nz.co.rubz.kiwi.bean.Child;
import nz.co.rubz.kiwi.bean.Parent;

@Repository
public class ChildDao extends BasicDAO<Child, Datastore> {

	private Logger log = Logger.getLogger(ChildDao.class);

	@Autowired(required = true)
	public ChildDao(@Qualifier("dataStore") Datastore ds) {
		super(ds);
	}

	public List<Child> findByParent(String args) {
		Query<Child> q = getDs().createQuery(Child.class);
//		q.field("parents.mobile").equal(mobile);
		q.field("parents.pid").equal(args);
//		q.retrievedFields(true, new String[] { "child_id","child_name", "child_photo" ,"parents" });
		QueryResults<Child> result = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return result.asList();

	}
	
	// 孩子姓名和家长共同确认孩子，因为有可能有重名
	public Child findByChildNameAndParent(String childName,String parentId) {
		Query<Child> q = getDs().createQuery(Child.class);
		q.field("child_name").equal(childName);
//		q.field("parents.mobile").equal(mobile);
		q.field("parents.pid").equal(parentId);
		QueryResults<Child> result = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return result.get();

	}
	
	public List<Child> findChildsByPid(String parentId) {
		Query<Child> q = getDs().createQuery(Child.class);
//		q.field("child_name").equal(childName);
//		q.field("parents.mobile").equal(mobile);
		q.field("parents.pid").equal(parentId);
		QueryResults<Child> result = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return result.asList();

	}
	
	public Child findChildById(String childId) {
		Query<Child> q = getDs().createQuery(Child.class);
		q.field("_id").equal(new ObjectId(childId));
		Child result = findOne(q);
		DaoLogHelper.logSimpleExplain(q);
		return result;

	}
	
	
	public UpdateResults modifyChildPhoto(String childName,String childPhoto,Parent parent) {
		Query<Child> q = getDs().createQuery(Child.class);
		q.field("child_name").equal(childName);
		UpdateOperations<Child> ops = getDs().createUpdateOperations(Child.class);
		ops.set("child_photo", childPhoto);
		ops.set("child_name", childName);
		ops.add("parents", parent,false);
		UpdateResults result = getDs().update(q, ops, true); // 如果没这个孩子，则增加一个新的
		return result;
		

	}
	
	public boolean modifyChildPhoto(String childId,String childPhoto,String childName) {
		Query<Child> q = getDs().createQuery(Child.class);
		q.field("_id").equal(new ObjectId(childId));
		UpdateOperations<Child> ops = getDs().createUpdateOperations(Child.class);
		if (StringUtils.isNotBlank(childPhoto)){
			ops.set("child_photo", childPhoto);
		}
		if (StringUtils.isNotBlank(childName)){
			ops.set("child_name", childName);
		}
		
		UpdateResults result = getDs().update(q, ops, true); // 如果没这个孩子，则增加一个新的
		return result.getWriteResult().getN()>0;
		

	}

}