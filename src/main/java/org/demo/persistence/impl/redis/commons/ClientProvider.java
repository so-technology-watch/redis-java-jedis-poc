package org.demo.persistence.impl.redis.commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class ClientProvider {

	/**
	 * The properties file containing the Redis configuration
	 */
	private final static String REDIS_PROPERTIES_FILE_NAME = "/redis.properties";

	/**
	 * The Jedis instance
	 */
	private final static JedisPool redisPool = createJedisClient();

	/**
	 * Returns the jedis instance
	 * 
	 * @return
	 */
	public static JedisPool getJedisInstance() {
		return redisPool;
	}

	/**
	 * Returns the Redis properties file name
	 * 
	 * @return
	 */
	public static String getJdbcPrpertiesFileName() {
		return REDIS_PROPERTIES_FILE_NAME;
	}

	/**
	 * Loads the JDBC properties using the class-path to find the file<br>
	 * 
	 * @throws RuntimeException
	 *             if the properties file cannot be found
	 * @return
	 */
	public static Properties loadJdbcPropertiesFromClassPath() {
		return loadPropertiesFromClassPath(REDIS_PROPERTIES_FILE_NAME);
	}

	/**
	 * Creates a Jedis <br>
	 * 
	 * @return
	 */
	private static JedisPool createJedisClient() {
		Properties env = loadJdbcPropertiesFromClassPath();
		JedisPool pool = new JedisPool(new JedisPoolConfig(), env.getProperty("redis.hostname"));

		return pool;

	}

	private static Properties loadPropertiesFromClassPath(String fileName) {
		Properties properties = new Properties();
		InputStream is = ClientProvider.class.getResourceAsStream(fileName);
		if (is != null) {
			try {
				properties.load(ClientProvider.class.getResourceAsStream(fileName));
			} catch (IOException e) {
				throw new RuntimeException("Cannot load '" + fileName + "'", e);
			}
			return properties;
		} else {
			throw new RuntimeException("Cannot found '" + fileName + "' (InputStream is null)");
		}
	}

}
