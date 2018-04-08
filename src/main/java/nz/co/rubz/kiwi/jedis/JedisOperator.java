package nz.co.rubz.kiwi.jedis;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nz.co.rubz.kiwi.utils.DateUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

/**
 * 将热点数据存储到redis 使用的redis 来实现Queue 需要aop一下。
 * 
 * @author lvqi
 *
 */

@Component
public class JedisOperator {

	private Logger log = Logger.getLogger(JedisOperator.class);

	@Autowired
	private JedisSentinelPool pool;

	// get classu:user:{user_id}:logouttime
	public String getLastLogoutTime(String userId) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.get(RedisKeyConstant.USER + userId
					+ RedisKeyConstant.LOGOUT_SUFFIX);
		} catch (Exception e) {
			log.info("cannot get jedis instance.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	// set classu:user:{user_id}:logouttime
	public String setLastLogoutTime(String userId) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.set(RedisKeyConstant.USER + userId
					+ RedisKeyConstant.LOGOUT_SUFFIX,
					DateUtils.getGdadcTimeStamp());
		} catch (Exception e) {
			log.info("cannot get jedis instance.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	// set noti:557fce4e0cf237c53345701e:viewed-s
	public Long incrViewedNoti(String notiId) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.incr(RedisKeyConstant.NOTI + notiId
					+ RedisKeyConstant.VIEWED_SUFFIX);
		} catch (Exception e) {
			log.info("cannot get jedis instance.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	// get noti:557fce4e0cf237c53345701e:viewed-s
	public String getViewedNoti(String notiId) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.get(RedisKeyConstant.NOTI + notiId
					+ RedisKeyConstant.VIEWED_SUFFIX);
		} catch (Exception e) {
			log.info("cannot get jedis instance.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	// sadd noti:557fce4e0cf237c53345701e:confirmed-S
	public Long addConfirmNoti(String notiId, String mobile) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.sadd(RedisKeyConstant.NOTI + notiId
					+ RedisKeyConstant.CONFIRM_SUFFIX, mobile);
		} catch (Exception e) {
			log.info("cannot get jedis instance.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}
	
	// sadd noti:557fce4e0cf237c53345701e:confirmed-S
		public Long removeConfirmNoti(String notiId, String mobile) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.srem(RedisKeyConstant.NOTI + notiId
						+ RedisKeyConstant.CONFIRM_SUFFIX, mobile);
			} catch (Exception e) {
				log.info("cannot get jedis instance.", e);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
			return null;
		}

	// scard sadd noti:557fce4e0cf237c53345701e:confirmed-S
	public Long cardConfirmNoti(String notiId) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.scard(RedisKeyConstant.NOTI + notiId
					+ RedisKeyConstant.CONFIRM_SUFFIX);
		} catch (Exception e) {
			log.info("cannot get jedis instance.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	// scard sadd noti:557fce4e0cf237c53345701e:confirmed-S
	public Set<String> getConfirmNoti(String notiId) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.smembers(RedisKeyConstant.NOTI + notiId
					+ RedisKeyConstant.CONFIRM_SUFFIX);
		} catch (Exception e) {
			log.info("cannot get jedis instance.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}
	
	// scard sadd noti:557fce4e0cf237c53345701e:confirmed-S
	public boolean isConfirmedNoti(String notiId,String userId) {
			Jedis jedis = null;
			try {
				jedis = pool.getResource();
				return jedis.sismember(RedisKeyConstant.NOTI + notiId
						+ RedisKeyConstant.CONFIRM_SUFFIX,userId);
			} catch (Exception e) {
				log.info("cannot get jedis instance.", e);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
			return false;
		}

	// sadd noti:557fce4e0cf237c53345701e:comment:ids-i
	public Long getNotiCommentId(String notiId) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.incr(RedisKeyConstant.NOTI + notiId
					+ RedisKeyConstant.COMMENTID_SUFFIX);
		} catch (Exception e) {
			log.info("cannot get jedis instance.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	public void delNotiKeys(String notiId) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			for (String key : jedis.keys(RedisKeyConstant.NOTI + notiId + "*")) {
				jedis.del(key);
			}
		} catch (Exception e) {
			log.info("cannot get jedis instance.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public void lpushUserNotice(String userId, String msg) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.lpush(RedisKeyConstant.USER + userId
					+ RedisKeyConstant.MAILBOX_SUFFIX, new String[] { msg });
		} catch (Exception e) {
			log.info("cannot get jedis instance.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public String rpopUserNotice(String userId) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.rpop(RedisKeyConstant.USER + userId
					+ RedisKeyConstant.MAILBOX_SUFFIX);
		} catch (Exception e) {
			log.info("cannot get jedis instance.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	public long llenUserNotice(String userId) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.llen(RedisKeyConstant.USER + userId
					+ RedisKeyConstant.MAILBOX_SUFFIX);
		} catch (Exception e) {
			log.info("cannot get jedis instance.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return 0;
	}

	public String setUserVerifyCode(String userId, String verifyCode) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			// 300 秒过期
			return jedis.setex(RedisKeyConstant.USER + userId
					+ RedisKeyConstant.VERIFY_CODE_SUFFIX, 300, verifyCode);
		} catch (Exception e) {
			log.info("cannot get jedis instance.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return "";
	}

	// get classu:user:{user_id}:verifycode-s
	public String getUserVerifyCode(String userId) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.get(RedisKeyConstant.USER + userId
					+ RedisKeyConstant.VERIFY_CODE_SUFFIX);
		} catch (Exception e) {
			log.info("cannot get jedis instance.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return "";
	}

}
