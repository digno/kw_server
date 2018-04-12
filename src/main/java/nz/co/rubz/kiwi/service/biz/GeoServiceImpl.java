package nz.co.rubz.kiwi.service.biz;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.mongodb.morphia.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nz.co.rubz.kiwi.KiwiBiz;
import nz.co.rubz.kiwi.MsgConstants;
import nz.co.rubz.kiwi.bean.Geometry;
import nz.co.rubz.kiwi.bean.Location;
import nz.co.rubz.kiwi.dao.LocationDao;
import nz.co.rubz.kiwi.protocol.beans.Content;
import nz.co.rubz.kiwi.protocol.converter.ResponseContentHelper; 


@Service
public class GeoServiceImpl {
	
	private Logger log = Logger.getLogger(GeoServiceImpl.class); 
	
	@Autowired
	private LocationDao locDao;
	
	@Autowired
	private BizNoticePublisher noticePublisher;
	
	private String loc_type = "user_loc_notify";
	
	@KiwiBiz("report")
	public Content geoReport(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		try {
			String did = (String) contentMap.get("did");
			String mobile = (String) contentMap.get("mobile");
			String address = (String) contentMap.get("address");
			Location saveLocation = new Location();
			saveLocation.setAddress(address);
			saveLocation.setAid(did);
			saveLocation.setMobile(mobile);
			saveLocation.setUtime(new Date());
			if (saveLocation.getAid() !=null &&  saveLocation.getMobile() != null ){
				Geometry geometry = new Geometry();
				BeanUtils.populate(geometry, (Map) contentMap.get("loc"));
				if (geometry.getCoordinates()==null || geometry.getCoordinates().size()!=2){
					resultContent = ResponseContentHelper
							.genSimpleResponseContentWithoutType(
									MsgConstants.ERROR_CODE_5,
									"report GEOInfo failed, require [<longitude> , <latitude>]");
					return resultContent;
				}
				saveLocation.setLoc(geometry);
				Key<Location> result = locDao.save(saveLocation);
				if (result.getId()!=null){
				log.info("Key :" +result.getId()+ " aid :" +did + " member :" + mobile + " loc :" + geometry.toString());
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_0,
								"did " +did + " member " + mobile + " report GEOInfo  successed!");
//				Activity a = activityDao.findActiviytByAid(aid);	
//				if(a!=null){
//					noticePublisher.pubGeoUpdateNotice(did, loc_type, geometry);
//				}
				}
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_4,
								"report GEOInfo failed, mobile and aid.");
			}
			
			
		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("geoReport Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}
	
	private Content genSimpleNotificationContent(String loc_type,
			HashMap<String,Object> contentMap) {
		Content resultContent = new Content();
		resultContent.setType(loc_type);
		resultContent.setData(contentMap);
		return resultContent;
	}

}
