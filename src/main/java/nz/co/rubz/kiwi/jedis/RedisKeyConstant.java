package nz.co.rubz.kiwi.jedis;

public class RedisKeyConstant {

	// key 类型
	// d->Date S->Set i-int s->String  l->List SS->SortSet
	
	public static final String USER = "classu:user:"; 

	public static final String NOTI = "classu:notify:";
	public static final String LOGOUT_SUFFIX = ":logouttime-d";
	public static final String VIEWED_SUFFIX= ":viewed-S"; 
	public static final String CONFIRM_SUFFIX= ":confirmed-S"; 
	public static final String COMMENTID_SUFFIX= ":comment:ids-i"; 
	public static final String MAILBOX_SUFFIX = ":mailbox-l"; 
	public static final String VERIFY_CODE_SUFFIX = ":verifycode-s"; 

//	public static final String LOGOUT_KEY_PREFIX = "user:logut:"; 
//	public static final String LOGOUT_KEY_PREFIX = "user:logut:"; 	
//	public static final String LOGOUT_KEY_PREFIX = "user:logut:"; 
//	public static final String LOGOUT_KEY_PREFIX = "user:logut:"; 
//	public static final String LOGOUT_KEY_PREFIX = "user:logut:"; 
//	public static final String LOGOUT_KEY_PREFIX = "user:logut:"; 
//	public static final String LOGOUT_KEY_PREFIX = "user:logut:"; 
	
	
}
