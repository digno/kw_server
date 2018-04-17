package nz.co.rubz.kiwi.dao;
import org.apache.log4j.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import nz.co.rubz.kiwi.model.Location;

public class LocationDao extends BasicDAO<Location, Datastore>  {

	private Logger log = Logger.getLogger(LocationDao.class);

	protected LocationDao(Datastore ds) {
		super(ds);
	}

	public Location getMemberLocationByAid(String aid, String mobile) {
		Query<Location> q = getDatastore().createQuery(Location.class).order("-utime")
				.filter("aid", aid).filter("mobile", mobile);
		DaoLogHelper.logSimpleExplain(q);
		return q.get();
	}
}
