package dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import redis.clients.jedis.Jedis;

public class ClientProvider {
	
	/**
	 * The properties file containing the Redis configuration
	 */
	private final static String REDIS_PROPERTIES_FILE_NAME = "/redis.properties" ;
	
	/**
	 * The Jedis instance 
	 */
	private final static Jedis jedis = createJedisClient() ;
	
	/**
	 * Returns the jedis  instance
	 * @return
	 */
	public static Jedis getJedisInstance() {
		return jedis;
	}
	
	/**
	 * Returns the Redis properties file name
	 * @return
	 */
	public static String getJdbcPrpertiesFileName() {
		return REDIS_PROPERTIES_FILE_NAME ;
	}
	
	/**
	 * Loads the JDBC properties using the class-path to find the file<br>
	 * @throws RuntimeException if the properties file cannot be found 
	 * @return
	 */
	public static Properties loadJdbcPropertiesFromClassPath() {
		return loadPropertiesFromClassPath(REDIS_PROPERTIES_FILE_NAME);
	}
	/**
	 * Creates a Jedis <br>
	 * @return
	 */
	private static Jedis createJedisClient() {
		Properties env = loadJdbcPropertiesFromClassPath();

		// Apache DBCP BasicDataSource
		String hostname = env.getProperty("redis.hostname");
		int port = Integer.parseInt(env.getProperty("redis.port"));
		String password = env.getProperty("redis.password");
		Jedis jedisClient = new Jedis(hostname,port);
		jedisClient.auth(password);
		
		return jedisClient ;
	}
	
	private static Properties loadPropertiesFromClassPath(String fileName) {
		Properties properties = new Properties();
		InputStream is = ClientProvider.class.getResourceAsStream(fileName) ;
		if ( is != null ) {
			try {
				properties.load( ClientProvider.class.getResourceAsStream(fileName) );
			} catch (IOException e) {
				throw new RuntimeException("Cannot load '" + fileName + "'", e);
			}
			return properties ;
		}
		else {
			throw new RuntimeException("Cannot found '" + fileName + "' (InputStream is null)" );
		}
	}
	
	

}
