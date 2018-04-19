package nz.co.rubz.kiwi.service.biz;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nz.co.rubz.kiwi.KiwiBiz;
import nz.co.rubz.kiwi.protocol.beans.Content;

@Service
public class RestaurantServiceImpl {

	private Logger log = Logger.getLogger(RestaurantServiceImpl.class);
	
	@Autowired
	private KiwiDataWrapper dataWrapper;
	
	
	/**
	 * https://openapi-doc.faas.ele.me/v2/api/search.html#api-restaurant-foods-search
	 * @param contentMap
	 * @return
	 * @throws Exception
	 */
	@KiwiBiz("foods")
	public Content queryRestaurantsAndFoods(HashMap<String, Object> contentMap)
			throws Exception {
		
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		return resultContent;
	}
	
	/**
	 * https://openapi-doc.faas.ele.me/v2/api/search.html#restaurants-search
	 * @param contentMap
	 * @return
	 * @throws Exception
	 */
	@KiwiBiz("search")
	public Content queryRestaurants(HashMap<String, Object> contentMap)
			throws Exception {
		
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		return resultContent;
	}
	

	/**
	 * https://openapi-doc.faas.ele.me/v2/api/search.html#restaurant-get
	 * @param contentMap
	 * @return
	 * @throws Exception
	 */
	@KiwiBiz("detail")
	public Content getRestaurantDetail(HashMap<String, Object> contentMap)
			throws Exception {
		
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		return resultContent;
	}
	
	/**
	 * https://openapi-doc.faas.ele.me/v2/api/search.html#restaurant-food-category-list
	 * @param contentMap
	 * @return
	 * @throws Exception
	 */
	@KiwiBiz("food_categories")
	public Content getRestaurantFoodCategories(HashMap<String, Object> contentMap)
			throws Exception {
		
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		return resultContent;
	}
	
	/**
	 * https://openapi-doc.faas.ele.me/v2/api/search.html#api-restaurant-multi-spec-menu
	 * @param contentMap
	 * @return
	 * @throws Exception
	 */
	
	@KiwiBiz("multi_spec_menu")
	public Content getRestaurantMultiSpceMenu(HashMap<String, Object> contentMap)
			throws Exception {
		
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		return resultContent;
	}
	
	/**
	 * https://openapi-doc.faas.ele.me/v2/api/search.html#restaurant-photo-list
	 * @param contentMap
	 * @return
	 * @throws Exception
	 */
	@KiwiBiz("photos")
	public Content queryRestaurantPhotos(HashMap<String, Object> contentMap)
			throws Exception {
		
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		return resultContent;
	}
	
	
	/**
	 * https://openapi-doc.faas.ele.me/v2/api/search.html#restaurant-category-list
	 * @param contentMap
	 * @return
	 * @throws Exception
	 */
	@KiwiBiz("cagetories")
	public Content queryRestaurantCategories(HashMap<String, Object> contentMap)
			throws Exception {
		
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		return resultContent;
	}
	
	
	/**
	 * https://openapi-doc.faas.ele.me/v2/api/search.html#restaurant-deliver-amount
	 * @param contentMap
	 * @return
	 * @throws Exception
	 */
	@KiwiBiz("deliver_amount")
	public Content queryRestaurantDelver_amount(HashMap<String, Object> contentMap)
			throws Exception {
		
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		return resultContent;
	}
	
	 
}
