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
public class MenuServiceImpl {

	private Logger log = Logger.getLogger(MenuServiceImpl.class);
	
	@Autowired
	private KiwiDataWrapper dataWrapper;
	
}
