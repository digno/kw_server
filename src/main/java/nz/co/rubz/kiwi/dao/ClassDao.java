package nz.co.rubz.kiwi.dao;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import nz.co.rubz.kiwi.bean.Classu;
import nz.co.rubz.kiwi.bean.ClassuMember;
import nz.co.rubz.kiwi.service.biz.BizConstants;

@Repository
public class ClassDao extends BasicDAO<Classu, Datastore> {

	private Logger log = Logger.getLogger(ClassDao.class);

	@Autowired(required=true)
	protected ClassDao(Datastore ds) {
		super(ds);
	}



	public Classu findClassById(String classId ,boolean withMembers) {
		Query<Classu> q = getDs().createQuery(Classu.class);
		q.field("class_id").equal(classId);
		if(!withMembers){
			q.retrievedFields(true, new String[]{"class_id","class_name","owner","owner_name"});
		}
		QueryResults<Classu> results = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return results.get();
	}
	
	public Classu findClassNameOwnerById(String classId ) {
		Query<Classu> q = getDs().createQuery(Classu.class);
		q.field("class_id").equal(classId);
		q.retrievedFields(true, new String[]{"class_name","owner"});
		QueryResults<Classu> results = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return results.get();
	}
	
	public List<Classu> findMyClass(String userId) {
		Query<Classu> q = getDs().createQuery(Classu.class);
		q.or(q.criteria("owner").equal(userId),q.criteria("members.user_id").equal(userId));
//		q.field("members.user_id").equal(userId);
//		q.retrievedFields(true, new String[]{"class_id","class_name","members"});
		QueryResults<Classu> result = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return result.asList();
	}


	public UpdateResults addClassuMember(String cid, ClassuMember user) {
		Query<Classu> q = getDs().createQuery(Classu.class);
		q.field("class_id").equal(cid);
//		q.field("members.mobile").notEqual(user.getMobile());
		UpdateOperations<Classu> ops = getDs().createUpdateOperations(
				Classu.class);
		ops.add("members", user, false);
		UpdateResults results = update(q, ops);

		return results;
	}
	
	public List<Classu> findClassByMember(String userId) {
		Query<Classu> q = getDs().createQuery(Classu.class);
		q.field("members.user_id").equal(userId);
		q.retrievedFields(true, new String[]{"class_id","class_name","owner"});
		QueryResults<Classu> result = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return result.asList();
	}
	public List<Classu> findClassByClassIdAndMember(String classId,String mobile) {
		Query<Classu> q = getDs().createQuery(Classu.class);
		q.disableValidation();
		q.field("members.mobile").equal(mobile);
		q.field("class_id").equal(classId);
		q.retrievedFields(true, new String[]{"class_id","class_name","owner","members.$"});
		QueryResults<Classu> result = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return result.asList();
	}
	
	
	public List<Classu> findClassByOwner(String userId) {
		Query<Classu> q = getDs().createQuery(Classu.class);
		q.field("owner").equal(userId);
		q.retrievedFields(true, new String[]{"class_id","class_name","members"});
		QueryResults<Classu> result = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return result.asList();
	}

	public boolean removeClassuMember(String classId, String owner,String userId) {
		boolean result = false;
		Query<Classu> q = getDs().createQuery(Classu.class);
		q.field("class_id").equal(classId);
		if (StringUtils.isNotBlank(owner)){
			q.field("owner").equal(owner);
		}
		UpdateOperations<Classu> ops = getDs().createUpdateOperations(
				Classu.class);
		Classu a = q.get();
		if (a != null && a.getMembers()!=null) {
			List<ClassuMember> am = a.getMembers();
			// 不可用list.remove();
			for (Iterator<ClassuMember> it = am.iterator(); it.hasNext();) {
				if (userId.equals(it.next().getUserId())) {
					it.remove();
				}
			}
			ops.set("members", am);
			UpdateResults results = update(q, ops);
			result = results.getUpdatedExisting();
		} else {
			log.info("can not find Class with classId : " + classId);
		}
		return result;
	}

	public boolean deleteClass(String classId,String owner) {
		Query<Classu> q = getDs().createQuery(Classu.class);
		q.field("class_id").equal(classId);
		q.field("owner").equal(owner);
		WriteResult results = deleteByQuery(q);
		return results.getN() > 0 ? true : false;
	}


	

	public boolean updateClassu(String classId, Classu classu) {
		boolean result = false;
		Query<Classu> q = getDs().find(Classu.class, "class_id", classId);
		UpdateOperations<Classu> ops = getDs().createUpdateOperations(Classu.class);
		if (StringUtils.isNotBlank(classu.getClassName())){
			ops.set("class_name", classu.getClassName());
		}
		if (StringUtils.isNotBlank(classu.getOwner())){
			ops.set("owner", classu.getOwner());
		}
		if (StringUtils.isNotBlank(classu.getOwnerName())){
			ops.set("owner_name", classu.getOwnerName());
		}
		if (StringUtils.isNotBlank(classu.getContent())){
			ops.set("content", classu.getContent());
		}
		UpdateResults updateResult = update(q, ops);
		result = updateResult.getUpdatedExisting();
		return result;
	}
	
	public List<Classu> findJoinedClasses(String args) {
		Query<Classu> q = getDs().createQuery(Classu.class);
//		q.field("members.mobile").equal(args);
		q.field("members.user_id").equal(args);
//		q.retrievedFields(false, new String[]{"members"});
		QueryResults<Classu> result = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return result.asList();
	}
	
	public List<Classu> findCreatedClasses(String args) {
		Query<Classu> q = getDs().createQuery(Classu.class);
		q.field("owner").equal(args);
		QueryResults<Classu> result = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return result.asList();
	}

	@SuppressWarnings("unchecked")
	public List<String> findChildren(String classId) {
		 DBCollection dbc = getDs().getDB().getCollection(BizConstants.TNAME_CLASS);
		 DBObject query = new BasicDBObject();
		 query.put("class_id", classId);
		 List<String> children=  dbc.distinct("members.child_name", query);
		return children;
		
	}
}