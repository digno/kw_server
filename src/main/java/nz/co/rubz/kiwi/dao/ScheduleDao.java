package nz.co.rubz.kiwi.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mongodb.WriteResult;

import nz.co.rubz.kiwi.bean.Schedule;

@Repository
public class ScheduleDao extends BasicDAO<Schedule, Datastore> {

	private Logger log = Logger.getLogger(ScheduleDao.class);

	@Autowired(required=true)
	protected ScheduleDao(Datastore ds) {
		super(ds);
	}

	public List<Schedule> findValidSchedule() {
		Query<Schedule> q = getDs().createQuery(Schedule.class);
		q.field("corn_expression").notEqual(null);
		q.field("action_time").greaterThan(new Date());
		QueryResults<Schedule> qr = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return qr.asList();
	}

	public boolean updateSchedule(Schedule schedule) {
		Query<Schedule> q = getDs().createQuery(Schedule.class);
		q.field("noti_id").equal(schedule.getJobName());
		UpdateOperations<Schedule> ops = getDs().createUpdateOperations(Schedule.class);
		ops.set("cron_expression", schedule.getCronExpression());
		UpdateResults result = getDs().update(q, ops, false); 
		return result.getWriteResult().getN()>0;
	}

	public Schedule findScheduleById(String scheduleJobId) {
		Query<Schedule> q = getDs().createQuery(Schedule.class);
		q.field("_id").equal(new ObjectId(scheduleJobId));
		return findOne(q);
	}
	

	public boolean deleteSchedule(String scheduleJobId) {
		Query<Schedule> q = getDs().createQuery(Schedule.class);
		q.field("_id").equal(new ObjectId(scheduleJobId));
		WriteResult rs = deleteByQuery(q);
		return rs.getN()>0;
	}

	public boolean deleteScheduleByNoti(String notiId) {
		Query<Schedule> q = getDs().createQuery(Schedule.class);
		q.field("job_name").equal(notiId);
		WriteResult rs = deleteByQuery(q);
		return rs.getN()>0;
	}

	public Schedule findScheduleByNotiId(String notiId) {
		Query<Schedule> q = getDs().createQuery(Schedule.class);
		q.field("job_name").equal(notiId);
		DaoLogHelper.logSimpleExplain(q);
		return findOne(q);
	}



	
}