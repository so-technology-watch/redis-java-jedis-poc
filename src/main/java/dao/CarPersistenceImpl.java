package dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import bean.Car;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

public class CarPersistenceImpl implements CarPersistence {
	private final static String SELECT_ALL = "car:*"; 

		private final static String SQL_SELECT = "car:";

		private final static String SQL_INSERT = 
			"insert into Car ( id, name, driver, other ) values ( ?, ?, ?, ? )";

		private final static String SQL_UPDATE = 
			"update Car set name = ?, driver = ?, other = ? where id = ?";

		private final static String SQL_DELETE = 
			"delete from Car where id = ?";

		private final static String SQL_COUNT_ALL = 
			"select count(*) from Car";

		private final static String SQL_COUNT = 
			"select count(*) from Car where id = ?";
	
	
	

	private Jedis jedis;

	public CarPersistenceImpl(Jedis jedis) {
		this.jedis = jedis;
	}

	public Car findById(Integer id) throws JsonParseException, JsonMappingException, IOException {
		Car car = new Car();
		if (jedis.exists("car:" + id.toString())) {
			Map<String, String> properties = jedis.hgetAll("car:" + id.toString());
			car.setId(id);
			car.setName(properties.get("name"));
			car.setDriver(Integer.parseInt(properties.get("driver")));

			return car;
		}

		return car;
	}

	public List<Car> findAll() throws JsonParseException, JsonMappingException, IOException {
		
		List<Car> list = new LinkedList<Car>();
		ScanParams params = new ScanParams();
		params.match("car:*");
		ScanResult<String> scanResult = jedis.scan("0", params);
		List<String> keys = scanResult.getResult();
		for(String key : keys){ 
			Map<String, String> properties = jedis.hgetAll(key);

			list.add(findById(Integer.parseInt(properties.get("id"))));
		}
			
		return list;
	}

	public Car save(Car entity) throws JsonGenerationException, JsonMappingException, IOException {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		if (jedis.hexists("user:" + entity.getId().toString(), mapper.writeValueAsString(entity)))
			update(entity);
		create(entity);

		return entity;
	}

	public boolean update(Car entity) {
		// TODO Auto-generated method stub

		Map<String, String> properties = jedis.hgetAll("user:" + entity.getId().toString());
		properties.put("id", entity.getId().toString());
		properties.put("name", entity.getName());
		properties.put("driver", entity.getDriver().toString());

		String success = jedis.hmset("car:" + entity.getId().toString(), properties);

		return (success.equals("OK"));
	}

	public Car create(Car entity) throws JsonGenerationException, JsonMappingException, IOException {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		if (!jedis.exists("car:" + entity.getId().toString())) {
			Map<String, String> carProperties = new HashMap<String, String>();
			carProperties.put("id", entity.getId().toString());
			carProperties.put("name", entity.getName());
			carProperties.put("driver", entity.getDriver().toString());
			jedis.hmset("car:" + entity.getId().toString(), carProperties);
			jedis.lpush("car", mapper.writeValueAsString(entity));
			

		}
		return entity;
	}

	public boolean deleteById(Integer id) throws JsonGenerationException, JsonMappingException, JsonParseException, IOException {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		if (jedis.exists("car:" + id.toString())) {
			long del = jedis.del("car:" + id.toString());
			return (del == 1);
		}
		return false;
	}

	public boolean delete(Car entity) {
		// TODO Auto-generated method stub
		System.out.println("car:" + entity.getId().toString());
		if (jedis.exists("car:" + entity.getId().toString())) {
			System.out.println("car:");
			long del = jedis.del("car:" + entity.getId().toString());
			return (del == 1);
		}
		
		return false;
	}
	


}
