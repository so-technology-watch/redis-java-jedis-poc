
package org.demo.persistence.impl.redis.commons;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Generic abstract class for basic REDIS DAO
 * 
 * @author telosys
 *
 * @param <T>
 */
public abstract class GenericDAO<T> {

	private Class<T> type;
	private String entity;
	private final static String ALL = "*";
	private final static String SEPARATOR = ":";
	private final static String NX = "nx";
	private final static String XX = "xx";
	private final static String INCR = "incr.";
	private final static String INITIAL_CURSOR = "0";
	private final static ObjectMapper mapper = new ObjectMapper();

	protected GenericDAO(String entity, Class<T> type) {
		this.entity = entity;
		this.type = type;
	}

	/**
	 * calculate a redis key for given bean
	 * 
	 * @return the redis key format for given bean
	 */
	protected abstract String getKey(T bean);

	/**
	 * Get connection to Redis
	 * 
	 * @return Connection to Redis by Jedis pool
	 */
	private JedisPool getConnection() {
		return ClientProvider.getJedisInstance();
	}

	/**
	 * Loads the given bean from the database using its primary key The given
	 * 
	 * @param bean
	 * @return bean if found and loaded, null if not found
	 */
	protected T doSelect(T bean) {
		try (Jedis jedis = getConnection().getResource()) {
			String key = entity + SEPARATOR + getKey(bean);
			String beanAsJson = jedis.get(key);
			return mapper.readValue(beanAsJson, type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns all the occurrences existing in the database
	 * 
	 * @return
	 */
	protected List<T> doSelectAll() {
		try (Jedis jedis = getConnection().getResource()) {
			List<T> list = new LinkedList<T>();
			List<String> keys = getAllKeys();
			String beanAsJson;
			for (String key : keys) {
				beanAsJson = jedis.get(key);
				list.add(mapper.readValue(beanAsJson, type));
			}
			return list;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Inserts the given bean in the database (Redis)
	 * 
	 * @param bean
	 */
	protected boolean doInsertIncr(T bean) {
		try (Jedis jedis = getConnection().getResource()) {
			long nextId = autoIncr();
			setBeanId(bean, (int) nextId);
			String key = entity + SEPARATOR + getKey(bean);
			while (jedis.exists(key)) {
				nextId = autoIncr();
				setBeanId(bean, (int) nextId);
				key = entity + SEPARATOR + getKey(bean);
			}
			String result = jedis.set(key, mapper.writeValueAsString(bean), NX);
			return "OK".equals(result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Inserts the given bean in the database (Redis)
	 * 
	 * @param bean
	 * @return Indicates if the insert is successful
	 */
	protected boolean doInsert(T bean) {
		try (Jedis jedis = getConnection().getResource()) {
			String key = entity + SEPARATOR + getKey(bean);
			if (jedis.exists(key))
				throw new RuntimeException("this key already exist");
			String result = jedis.set(key, mapper.writeValueAsString(bean), NX);
			return "OK".equals(result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Updates the given bean in the database (Redis)
	 * 
	 * @param bean
	 * @return the Redis return code (i.e. the row count affected by the UPDATE
	 */
	protected boolean doUpdate(T bean) {
		try (Jedis jedis = getConnection().getResource()) {
			String key = entity + SEPARATOR + getKey(bean);
			String result = jedis.set(key, mapper.writeValueAsString(bean), XX);
			return "OK".equals(result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Deletes the given bean in the database (Redis)
	 * 
	 * @param bean
	 * @return the Redis return code (i.e. the row count affected by the DELETE
	 *         operation : 0 or 1 )
	 */
	protected long doDelete(T bean) {
		try (Jedis jedis = getConnection().getResource()) {
			String key = entity + SEPARATOR + getKey(bean);
			return jedis.del(key);
		} catch (JedisException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Checks if the given bean exists in the database
	 * 
	 * @param bean
	 * @return true if bean exist false else
	 */
	protected boolean doExists(T bean) {
		try (Jedis jedis = getConnection().getResource()) {
			String key = entity + SEPARATOR + getKey(bean);
			return jedis.exists(key);
		} catch (JedisException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Counts all the occurrences in the table
	 * 
	 * @return
	 */
	protected int doCountAll() {
		return getAllKeys().size();
	}

	/**
	 * calcul the next value of id
	 * 
	 * @return next key available
	 */
	protected long autoIncr() {
		long result = 1L;
		String keyIncr = INCR + entity;
		try (Jedis jedis = getConnection().getResource()) {
			if (jedis.get(keyIncr) != null) {
				result = jedis.incr(keyIncr);
			} else {
				jedis.set(keyIncr, String.valueOf(result), NX);
			}
			return result;
		} catch (JedisException e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract void setBeanId(T bean, Integer id);

	/**
	 * Returns all keys existing in the database
	 * 
	 * @return all key
	 */
	private List<String> getAllKeys() {
		try (Jedis jedis = getConnection().getResource()) {
			ScanParams params = new ScanParams();
			String select_all = entity + SEPARATOR + ALL;
			params.match(select_all);
			String cursor = redis.clients.jedis.ScanParams.SCAN_POINTER_START;
			boolean cycleIsFinished = false;
			List<String> results = new ArrayList<String>();
			while (!cycleIsFinished) {
				ScanResult<String> scanResult = jedis.scan(cursor, params);
				List<String> result = scanResult.getResult();
				for (String res : result) {
					results.add(res);
				}
				cursor = scanResult.getStringCursor();
				if (cursor.equals(INITIAL_CURSOR)) {
					cycleIsFinished = true;
				}
			}
			return results;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
