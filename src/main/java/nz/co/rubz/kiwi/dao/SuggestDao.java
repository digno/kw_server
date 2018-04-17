package nz.co.rubz.kiwi.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import nz.co.rubz.kiwi.model.Suggest;

@Repository
public class SuggestDao extends BasicDAO<Suggest, Datastore> {

	private Logger log = Logger.getLogger(SuggestDao.class);

	@Autowired(required = true)
	public SuggestDao(@Qualifier("dataStore") Datastore ds) {
		super(ds);
	}

	public List<Suggest> findByUserId(String userId) {
		Query<Suggest> q = getDs().createQuery(Suggest.class);
		q.field("user_id").equal(userId);
		QueryResults<Suggest> result = find(q);
		return result.asList();

	}
	
	 

}