package nz.co.rubz.kiwi.service.biz;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nz.co.rubz.kiwi.KiwiBiz;
import nz.co.rubz.kiwi.MsgConstants;
import nz.co.rubz.kiwi.dao.ClientDao;
import nz.co.rubz.kiwi.dao.SuggestDao;
import nz.co.rubz.kiwi.model.Client;
import nz.co.rubz.kiwi.model.Suggest;
import nz.co.rubz.kiwi.protocol.beans.Content;
import nz.co.rubz.kiwi.protocol.converter.ResponseContentHelper;

@Service
public class ClientServiceImpl {

	private Logger log = Logger.getLogger(ClientServiceImpl.class);

	@Autowired
	private ClientDao clientDao;

	@Autowired
	private SuggestDao suggestDao;
	
	@Autowired
	private ClassuDataWrapper dataWrapper;

	@KiwiBiz("report")
	public Content clientReport(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		String userId = (String) contentMap.get("user_id");
		if (StringUtils.isEmpty(userId)) {
			return ResponseContentHelper.genSimpleResponseContentWithoutType(
					MsgConstants.ERROR_CODE_3, "user_id is null.");
		}
		
		Client saveClient = dataWrapper.convertMapToClient(contentMap);
		saveClient.setReportTime(new Date());
		if (clientDao.updateClientInfo(userId, saveClient)) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_0,
							"report client info successed.");
		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(MsgConstants.ERROR_CODE_1,
							"report client info failed.");

		}
		return resultContent;
	}

	@KiwiBiz("suggest")
	public Content clientSuggest(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		String userId = (String) contentMap.get("user_id");
		if (StringUtils.isEmpty(userId)) {
			return ResponseContentHelper.genSimpleResponseContentWithoutType(
					MsgConstants.ERROR_CODE_3, "userId is null.");
		}
		Suggest saveSuggest = new Suggest();
		BeanUtils.populate(saveSuggest, contentMap);
		saveSuggest.setUserId(userId);
		saveSuggest.setCtime(new Date());
		suggestDao.save(saveSuggest);
		resultContent = ResponseContentHelper
				.genSimpleResponseContentWithoutType(MsgConstants.ERROR_CODE_0,
						"suggest upload successed!");

		return resultContent;
	}

	
	
}
