
package org.demo.persistence.impl.redis.commons;

import java.io.IOException;
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

	/**
	 * Nom de l'instance
	 * 
	 * @return
	 */
	protected abstract T newInstance();

	/**
	 * Returns the REQUEST to be used to retrieve all the occurrences
	 * 
	 * @return
	 */
	protected abstract String getSelectAll();

	/**
	 * Returns the SQL SELECT REQUEST to be used to retrieve all the occurrences
	 * 
	 * @return
	 */
	protected abstract String getEntity();

	/**
	 * Populates the bean attributes from the given ResultSet
	 * 
	 * @param rs
	 * @param bean
	 * @return
	 */
	protected abstract String getSetValuesForId(T bean);

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
			String id = getSetValuesForId(bean);
			if (!jedis.exists(id) || !checkId(id)) {
				throw new RuntimeException("this id doesn't exist or check id's format");
			}
			String beanAsJson = jedis.get(id);
			return jsonToBean(beanAsJson);
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
			for (String key : keys) {
				String beanAsJson = jedis.get(key);
				T bean = jsonToBean(beanAsJson);
				list.add(bean);
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
			String id = getSetValuesForId(bean);
			while (jedis.exists(id)) {
				nextId = autoIncr();
				setBeanId(bean, (int) nextId);
				id = getSetValuesForId(bean);
			}
			if (!checkId(id)) {
				throw new RuntimeException("check id's format");
			}
			String beanAsJson = beanAsJson(bean);
			String result = jedis.set(id, beanAsJson);
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
			String id = getSetValuesForId(bean);
			if (jedis.exists(id) || !checkId(id)) {
				throw new RuntimeException("this bean already exist or check id's format");
			}
			String beanAsJson = beanAsJson(bean);
			String result = jedis.set(id, beanAsJson);
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
			String id = getSetValuesForId(bean);
			if (!jedis.exists(id) || !checkId(id)) {
				throw new RuntimeException("this bean doesn't exist or check id's format");
			}
			String beanAsJson = beanAsJson(bean);
			String result = jedis.set(id, beanAsJson);
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
			String id = getSetValuesForId(bean);
			if (!jedis.exists(id) || !checkId(id)) {
				throw new RuntimeException("this bean doesn't exist or check id's format");
			}
			return jedis.del(id);
		} catch (JedisException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Checks if the given bean exists in the database
	 * 
	 * @param bean
	 * @return
	 */
	protected boolean doExists(T bean) {
		try (Jedis jedis = getConnection().getResource()) {
			String id = getSetValuesForId(bean);
			return jedis.exists(id);
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
		try (Jedis jedis = getConnection().getResource()) {
			List<String> keys = getAllKeys();
			return keys.size();
		} catch (JedisException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * calcul the next value of id
	 * 
	 * @return
	 */
	protected long autoIncr() {
		long result = 1L;
		try (Jedis jedis = getConnection().getResource()) {
			if (jedis.get(getEntity()) != null) {
				result = jedis.incr(getEntity());
				return result;
			}
			jedis.set(getEntity(), "1");
			return result;
		} catch (JedisException e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract void setBeanId(T bean, Integer id);

	/**
	 * Returns all keys existing in the database
	 * 
	 * @return
	 */
	private List<String> getAllKeys() {
		try (Jedis jedis = getConnection().getResource()) {
			ScanParams params = new ScanParams();
			params.match(getSelectAll());
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
				if (cursor.equals("0")) {
					cycleIsFinished = true;
				}
			}
			return results;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * check the id format
	 * 
	 * @return true if the good format
	 */
	private boolean checkId(String id) {
		String tab[] = id.split(":");
		Integer number = null;
		if (tab[1].equals("null") || tab[0].equals("null")) {
			return false;
		}
		number = Integer.parseInt(tab[1]);
		System.out.println(number);
		String entity = tab[0].trim();
		return (getEntity().equals(entity) && number.equals(null));
	}

	/**
	 * 
	 * serialize bean as Json format
	 * 
	 * @param bean
	 * @return json format of bean
	 */
	private String beanAsJson(T bean) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(bean);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 
	 * deserialize bean as Json format
	 * 
	 * @param bean
	 * @return json format of bean
	 */
	@SuppressWarnings("unchecked")
	private T jsonToBean(String json) {
		T bean = newInstance();
		ObjectMapper mapper = new ObjectMapper();
		try {
			return (T) mapper.readValue(json, bean.getClass());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bean;
	}

}
