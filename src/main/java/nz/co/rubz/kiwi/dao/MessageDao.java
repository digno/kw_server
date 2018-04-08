package nz.co.rubz.kiwi.dao;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.WriteResult;

import nz.co.rubz.kiwi.protocol.beans.KiwiMessage;
import nz.co.rubz.kiwi.protocol.converter.ProtocolConverter;

@Repository
public class MessageDao{

	private Logger log = Logger.getLogger(MessageDao.class);
	
	private Datastore ds ;
	
	
	@Autowired(required=true)
	protected MessageDao(Datastore ds) {
		this.ds = ds;
	}
 
	public void saveMessage(String mobile,KiwiMessage message){
		DBCollection dbc = ds.getDB().getCollection("UndeliverMsgs");
		BasicDBObject bdb = new BasicDBObject();
		HashMap<String,Object> map = ProtocolConverter.convertWhereMessageToMap(message);
		bdb.putAll(map);
		dbc.insert(bdb);
//		WriteResult result = dbc.insert(bdb);
//		if(result.getN() == 0){
//			log.info("save undeliver message failed!");
//		} else {
//			log.info("save undeliver message successed!");
//		}
	}
	
	public void saveMessage(KiwiMessage message){
		DBCollection dbc = ds.getDB().getCollection("UndeliverMsgs");
		BasicDBObject bdb = new BasicDBObject();
		HashMap<String,Object> map = ProtocolConverter.convertWhereMessageToMap(message);
		bdb.putAll(map);
		dbc.insert(bdb);
//		WriteResult result = dbc.insert(bdb);
//		if(result.getN() == 0){
//			log.info("save undeliver message failed!");
//		} else {
//			log.info("save undeliver message successed!");
//		}
	}
	
	public DBCursor findUndeliverMessages(String mobile,String type){
		DBCollection dbc = ds.getDB().getCollection("UndeliverMsgs");
		BasicDBObject bdb = new BasicDBObject();
		bdb.append("to", mobile);
		if (!"".equals(type)){
			bdb.append("content.type", type);
		}
		BasicDBObject key=new BasicDBObject("_id",0);//指定需要显示列  
		DBCursor cursor = dbc.find(bdb,key);
		DaoLogHelper.logSimpleExplain(cursor);
		return cursor;
	}
	
	public void saveDeliveredMessage(KiwiMessage message){
		DBCollection dbc = ds.getDB().getCollection("DeliveredMsgs");
		
		BasicDBObject bdb = new BasicDBObject();
		HashMap<String,Object> map = ProtocolConverter.convertWhereMessageToMap(message);
		bdb.putAll(map);
		dbc.insert(bdb);
//		WriteResult result = dbc.insert(bdb);
//		if(result.getN()== 0){
//			log.info("save delivered message failed!");
//		}
	}
	
	public void removeMessage(String id) {
		DBCollection dbc = ds.getDB().getCollection("UndeliverMsgs");
		BasicDBObject bdb = new BasicDBObject();
		bdb.append("msg_id", id);
		WriteResult result = dbc.remove(bdb);
		if(result.getN()== 0){
			log.info("delete undeliver message failed!");
		} else {
			log.info("delete undeliver message successed!");
		}
	}
	
}
