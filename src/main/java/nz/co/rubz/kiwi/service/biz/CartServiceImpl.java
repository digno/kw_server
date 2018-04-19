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
public class CartServiceImpl {

	private Logger log = Logger.getLogger(CartServiceImpl.class);
	
	@Autowired
	private KiwiDataWrapper dataWrapper;
	
	
	
	/**
	 * https://openapi-doc.faas.ele.me/v2/api/cart.html#cart-create
	 * @param contentMap
	 * @return
	 * @throws Exception
	 */
	@KiwiBiz("create")
	public Content createCart(HashMap<String, Object> contentMap)
			throws Exception {
		
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		return resultContent;
	}
	
	/**
	 * https://openapi-doc.faas.ele.me/v2/api/cart.html#cart-update
	 * @param contentMap
	 * @return
	 * @throws Exception
	 */
	@KiwiBiz("update")
	public Content updateCart(HashMap<String, Object> contentMap)
			throws Exception {
		
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		return resultContent;
	}
	
	/**
	 * https://openapi-doc.faas.ele.me/v2/api/cart.html#batch-cart-update
	 * @param contentMap
	 * @return
	 * @throws Exception
	 */
	@KiwiBiz("batch_update")
	public Content batchUpdateCart(HashMap<String, Object> contentMap)
			throws Exception {
		
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		return resultContent;
	}
	
	/**
	 * https://openapi-doc.faas.ele.me/v2/api/cart.html#api-cart-pre-checkout
	 * @param contentMap
	 * @return
	 * @throws Exception
	 */
	@KiwiBiz("pre_checkout")
	public Content preCheckoutCart(HashMap<String, Object> contentMap)
			throws Exception {
		
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		return resultContent;
	}
	
	
}
