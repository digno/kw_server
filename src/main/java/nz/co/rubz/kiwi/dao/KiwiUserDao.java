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

import nz.co.rubz.kiwi.model.KiwiUser;

@Repository
public class KiwiUserDao extends BasicDAO<KiwiUser, Datastore> {

	private Logger log = Logger.getLogger(KiwiUserDao.class);

	@Autowired(required = true)
	public KiwiUserDao(@Qualifier("dataStore") Datastore ds) {
		super(ds);
	}

	public KiwiUser findByMobile(String mobile) {
		Query<KiwiUser> q = getDatastore().createQuery(KiwiUser.class);
		q.field("mobile").equal(mobile);
		QueryResults<KiwiUser> users = find(q);
		if (users.asList().size() > 1) {
			log.warn("user has dirty data. same mobile used by different account.");
		}
		DaoLogHelper.logSimpleExplain(q);
		return users.get();
	}

	public List<KiwiUser> findByIds(List<String> ids) {
		Query<KiwiUser> q = getDatastore().createQuery(KiwiUser.class);
		List<ObjectId> oids = new ArrayList<ObjectId>();
		for (String id : ids) {
			if (StringUtils.isBlank(id)) {
				continue;
			}
			oids.add(new ObjectId(id));
		}
		q.field("_id").in(oids);
		QueryResults<KiwiUser> results = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return results.asList();
	}

	public KiwiUser findById(String id) {

		if (ObjectId.isValid(id)) {
			return findOne("_id", new ObjectId(id));
		}
		return null;

	}

	public boolean updateUserById(String userId, KiwiUser user) {
		boolean result = false;
		Query<KiwiUser> q = getDatastore().find(KiwiUser.class).filter("_id", new ObjectId(userId));
		UpdateOperations<KiwiUser> ops = getDatastore().createUpdateOperations(KiwiUser.class);
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

		if (StringUtils.isNotBlank(user.getEmail())) {
			ops.set("email", user.getEmail());
		}
		UpdateResults updateResult = update(q, ops);
		result = updateResult.getUpdatedExisting();
		return result;

	}

}