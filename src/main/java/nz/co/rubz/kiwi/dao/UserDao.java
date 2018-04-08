package nz.co.rubz.kiwi.dao;

import java.util.ArrayList;
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

import nz.co.rubz.kiwi.bean.ClassInfo;
import nz.co.rubz.kiwi.bean.User;

@Repository
public class UserDao extends BasicDAO<User, Datastore> {

	private Logger log = Logger.getLogger(UserDao.class);

	@Autowired(required = true)
	public UserDao(@Qualifier("dataStore") Datastore ds) {
		super(ds);
	}

	public User findByMobile(String mobile) {
		Query<User> q = getDs().createQuery(User.class);
		q.field("mobile").equal(mobile);
		QueryResults<User> users = find(q);
		if (users.asList().size() > 1) {
			log.warn("user has dirty data. same mobile used by different account.");
		}
		DaoLogHelper.logSimpleExplain(q);
		return users.get();
	}

	public List<User> findByIds(List<String> ids) {
		Query<User> q = getDs().createQuery(User.class);
		List<ObjectId> oids = new ArrayList<ObjectId>();
		for (String id : ids) {
			if (StringUtils.isBlank(id)){
				continue;
			}
			oids.add(new ObjectId(id));
		}
		q.field("_id").in(oids);
		QueryResults<User> results = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return results.asList();
	}

	public User findById(String id) {

		if (ObjectId.isValid(id)) {
			return findOne("_id", new ObjectId(id));
		}
		return null;

	}

	@Deprecated
	public boolean updateUserByMobile(String mobile, User user) {
		boolean result = false;
		Query<User> q = getDs().find(User.class, "mobile", mobile);
		UpdateOperations<User> ops = getDs().createUpdateOperations(User.class);
		if (StringUtils.isNotBlank(user.getName())) {
			ops.set("name", user.getName());
		}
		if (StringUtils.isNotBlank(user.getGender())) {
			ops.set("gender", user.getGender());
		}
		if (StringUtils.isNotBlank(user.getPassword())) {
			ops.set("password", user.getPassword());
			log.info("set new password .");
		}
		if (StringUtils.isNotBlank(user.getType())) {
			ops.set("type", user.getType());
		}
		if (StringUtils.isNotBlank(user.getPushflag())) {
			ops.set("pushflag", user.getPushflag());
		}
		if (StringUtils.isNotBlank(user.getSmsflag())) {
			ops.set("smsflag", user.getSmsflag());
		}
		if (StringUtils.isNotBlank(user.getEmailflag())) {
			ops.set("emailflag", user.getEmailflag());
		}
		if (StringUtils.isNotBlank(user.getEmail())) {
			ops.set("email", user.getEmail());
		}
		UpdateResults updateResult = update(q, ops);
		result = updateResult.getUpdatedExisting();
		return result;

	}

	public boolean updateUserById(String userId, User user) {
		boolean result = false;
		Query<User> q = getDs().find(User.class, "_id", new ObjectId(userId));
		UpdateOperations<User> ops = getDs().createUpdateOperations(User.class);
		if (StringUtils.isNotBlank(user.getMobile())) {
			ops.set("mobile", user.getMobile());
		}
		if (StringUtils.isNotBlank(user.getName())) {
			ops.set("name", user.getName());
		}
		if (StringUtils.isNotBlank(user.getGender())) {
			ops.set("gender", user.getGender());
		}
		if (StringUtils.isNotBlank(user.getPassword())) {
			ops.set("password", user.getPassword());
			log.info("set new password .");
		}
		if (StringUtils.isNotBlank(user.getType())) {
			ops.set("type", user.getType());
		}
		if (StringUtils.isNotBlank(user.getPushflag())) {
			ops.set("pushflag", user.getPushflag());
		}
		if (StringUtils.isNotBlank(user.getSmsflag())) {
			ops.set("smsflag", user.getSmsflag());
		}
		if (StringUtils.isNotBlank(user.getEmailflag())) {
			ops.set("emailflag", user.getEmailflag());
		}
		if (StringUtils.isNotBlank(user.getEmail())) {
			ops.set("email", user.getEmail());
		}
		UpdateResults updateResult = update(q, ops);
		result = updateResult.getUpdatedExisting();
		return result;

	}

	@Deprecated
	public UpdateResults addUserClassInfo(String mobile, ClassInfo classInfo) {
		Query<User> q = getDs().createQuery(User.class);
		q.field("mobile").equal(mobile);
		// q.field("classes.class_id").notEqual(classInfo.getClassId());
		UpdateOperations<User> ops = getDs().createUpdateOperations(User.class);
		ops.add("classes", classInfo, false); // 第三个参数：是否增加重复数据
		return update(q, ops);

	}

}