package nz.co.rubz.kiwi.service.biz;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.mongodb.morphia.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nz.co.rubz.kiwi.KiwiBiz;
import nz.co.rubz.kiwi.MsgConstants;
import nz.co.rubz.kiwi.bean.Comment;
import nz.co.rubz.kiwi.dao.CommentDao;
import nz.co.rubz.kiwi.protocol.beans.Content;
import nz.co.rubz.kiwi.protocol.converter.ResponseContentHelper;

@Service
public class CommentServiceImpl {

	private Logger log = Logger.getLogger(CommentServiceImpl.class);

	@Autowired
	private CommentDao commentDao;

	@Autowired
	private ClassuDataWrapper dataWrapper;

	@Autowired
	private BizNoticePublisher noticePublisher;
	

	@KiwiBiz("save")
	public Content sendComment(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		String notiId = (String) contentMap.get("noti_id");
		Comment comment = dataWrapper.boxCommentData(contentMap);
		if (isValidComment(contentMap)) {
			Key<Comment> result = commentDao.save(comment);
			if (result.getId() != null) {
				resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
				//@Id field is filled in for you (after the save), if you didn't set it.  
				resultMap.put(MsgConstants.COMMENTID, comment.getId().toHexString()); 
				resultMap.put("ctime", comment.getCtime());
				log.info( "relay [ " + notiId
						+ " ] comment successed!");
				resultContent.setData(resultMap);
				// 给老师发通知
				noticePublisher.pubCommentNotificationNotice(comment);
			}
		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_4,
							"can not send comment, need more params.");
		}
		return resultContent;
	}

	@KiwiBiz("remove")
	public Content deleteComment(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		String cid = (String) contentMap.get("cid");
		// TODO 发通知
		if (!StringUtils.isBlank(cid)) {
			if (commentDao.deleteComment(cid)) {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_0, "delete [ " + cid
										+ " ] comment successed!");
			}
		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_4,
							"can not send comment, need more params.");
		}
		return resultContent;
	}

	private boolean isValidComment(HashMap<String, Object> contentMap) {
		Object classId = contentMap.get("class_id");
		Object content = contentMap.get("comment");
		Object notiId = contentMap.get("noti_id");

		if (classId == null || content == null || notiId == null) {
			log.info("classId : " + classId + "comment : " + content + "notiId : "+notiId);
			return false;
		}
		
		return true;
	}

	
}
