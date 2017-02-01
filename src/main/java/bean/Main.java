package bean;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParser;

import dao.CarPersistenceImpl;
import redis.clients.jedis.Jedis;

public class Main {

	@SuppressWarnings("unchecked")
	public static void main(String[] args)
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {
	
		  Jedis jedis = new Jedis("localhost", 6379);
		   // jedis.auth("mypass");
		    System.out.println("Connected to Redis");
		    jedis.set("foo", "bar");
		    String value = jedis.get("foo");
		    jedis.set("foo", "bar12");
		    System.out.println(jedis.get("foo"));
		  }
	  
/*
		JSONObject parser1 = new JSONObject();
		Car car = new Car(21, "terrence", 14);
		parser1.put("driver", car.getDriver());
		parser1.put("id", car.getId());
		
		// Car car1 = new Car ();
		String s = parser1.toJSONString();
		System.out.println(s);

		JSONObject obj = (JSONObject) JSONValue.parse(s);
		// parser1.parse(s, (ContainerFactory) car1);
		System.out.println(obj.get("driver"));
		System.out.println(obj.get("id"));*/

	}

	/*
	 * RedisClient redisClient = new
	 * RedisClient(RedisURI.create("redis://localhost:6379"));
	 * RedisConnection<String, String> connection = redisClient.connect();
	 * 
	 * System.out.println("Connected to Redis"); connection.set("foo", "bar");
	 * String value = connection.get("foo"); System.out.println(value);
	 * 
	 * connection.close(); redisClient.shutdown();
	 */

