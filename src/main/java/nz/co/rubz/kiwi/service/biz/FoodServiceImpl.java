package nz.co.rubz.kiwi.service.biz;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nz.co.rubz.kiwi.KiwiBiz;
import nz.co.rubz.kiwi.protocol.beans.Content;

@Service
public class FoodServiceImpl {

	private Logger log = Logger.getLogger(FoodServiceImpl.class);
	
	@Autowired
	private KiwiDataWrapper dataWrapper;
	
	/**
	 * https://openapi-doc.faas.ele.me/v2/api/search.html#food-category-get
	 * @param contentMap
	 * @return
	 * @throws Exception
	 */
	@KiwiBiz("cagetory_detail")
	public Content getFoodCagetoryDetail(HashMap<String, Object> contentMap)
			throws Exception {
		
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		return resultContent;
	}
	
	
	/**
	 * https://openapi-doc.faas.ele.me/v2/api/search.html#food-category-food-list
	 * @param contentMap
	 * @return
	 * @throws Exception
	 */
	@KiwiBiz("foods_in_category")
	public Content getFoodsInCagetory(HashMap<String, Object> contentMap)
			throws Exception {
		
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		return resultContent;
	}
	
 
	/**
	 * https://openapi-doc.faas.ele.me/v2/api/search.html#food-get
	 * @param contentMap
	 * @return
	 * @throws Exception
	 */
	@KiwiBiz("detail")
	public Content getFoodDetail(HashMap<String, Object> contentMap)
			throws Exception {
		
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		return resultContent;
	}
	
	/**
	 * https://openapi-doc.faas.ele.me/v2/api/search.html#food-batch-get
	 * @param contentMap
	 * @return
	 * @throws Exception
	 */
	
	@KiwiBiz("get_batch")
	public Content getFoodsByIds(HashMap<String, Object> contentMap)
			throws Exception {
		
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		return resultContent;
	}
	
	
}
