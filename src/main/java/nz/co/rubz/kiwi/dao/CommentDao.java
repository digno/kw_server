package nz.co.rubz.kiwi.dao;

import java.util.List;

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

import nz.co.rubz.kiwi.bean.Comment;

@Repository
public class CommentDao extends BasicDAO<Comment, Datastore> {

	private Logger log = Logger.getLogger(CommentDao.class);

	@Autowired(required = true)
	public CommentDao(@Qualifier("dataStore") Datastore ds) {
		super(ds);
	}

	public boolean deleteComment(String cid) {
		Query<Comment> q = getDs().createQuery(Comment.class);
		q.field("_id").equal(new ObjectId(cid));
		WriteResult results = deleteByQuery(q);
		return results.getN() > 0 ? true : false;
	}

	public List<Comment> findCommentAboutMe(String notiId, String owner,
			String userId) {
		Query<Comment> q = getDs().createQuery(Comment.class);
		q.field("noti_id").equal(notiId);
		if (!owner.equals(userId)) {
			q.or(q.criteria("relay").equal(userId), q.criteria("relay_to")
					.equal(userId));
		}
		QueryResults<Comment> result = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return result.asList();
	}

	public boolean deleteCommentByNotiId(String notiId) {
		Query<Comment> q = getDs().createQuery(Comment.class);
		q.field("noti_id").equal(notiId);
		WriteResult results = deleteByQuery(q);
		return results.getN() > 0 ? true : false;
	}

	public List<Comment> findComment(String args) {
		Query<Comment> q = getDs().createQuery(Comment.class);
		q.or(q.criteria("relay").equal(args),
				q.criteria("relay_to").equal(args));
		QueryResults<Comment> result = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return result.asList();
	}
	
	

}