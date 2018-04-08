package nz.co.rubz.kiwi.dao;

import org.apache.log4j.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import nz.co.rubz.kiwi.bean.Client;

@Repository
public class ClientDao extends BasicDAO<Client, Datastore> {

	private Logger log = Logger.getLogger(ClientDao.class);

	@Autowired(required = true)
	public ClientDao(@Qualifier("dataStore") Datastore ds) {
		super(ds);
	}

	public Client findByUserId(String userId) {
		Query<Client> q = getDs().createQuery(Client.class);
		q.field("user_id").equal(userId);
		QueryResults<Client> result = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return result.get();
	}
	
	public boolean updateClientInfo(String userId,Client clientInfo) {
		Query<Client> q = getDs().createQuery(Client.class);
		q.field("user_id").equal(userId);
		UpdateOperations<Client> ops = getDs().createUpdateOperations(Client.class);
		ops.set("client_name", clientInfo.getClientName());
		ops.set("client_ver", clientInfo.getClientVer());
		ops.set("device_id", clientInfo.getDeviceId());
		ops.set("device_token", clientInfo.getDeviceToken());
		ops.set("phone_type", clientInfo.getPhoneType());
		ops.set("report_time", clientInfo.getReportTime());
		
		UpdateResults result = getDs().update(q, ops, true);  
		return result.getWriteResult().getN()>0;
		

	}

}