/*
 * Created on 2017-01-27 ( Date ISO 2017-01-27 - Time 10:07:27 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.0.0
 */

package org.demo.persistence.impl.redis;

import java.io.IOException;
import java.util.List;

import javax.inject.Named;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.demo.data.record.CarRecord;
import org.demo.persistence.CarPersistence;
import org.demo.persistence.impl.redis.commons.GenericDAO;

/**
 * Car persistence implementation
 * 
 * @author Telosys
 *
 */
@Named("CarPersistence")
public class CarPersistenceRedis extends GenericDAO<CarRecord> implements CarPersistence {

	private final static String SELECT_ALL = "car:*";

	// ----------------------------------------------------------------------
	/**
	 * DAO constructor
	 */
	public CarPersistenceRedis() {
		super();
	}

	// ---------------------------------------------------------------
	/**
	 * Creates a new instance of the bean and populates it with the given
	 * primary value(s)
	 * 
	 * @param id;
	 * @return the new instance
	 */

	@Override
	protected String getSetValuesForId(CarRecord bean) {
		// TODO Auto-generated method stub
		if (bean != null) {
			return "car:" + bean.getId();
		}
		return "";
	}

	// ---------------------------------------------------------------
	/**
	 * Creates a new instance of the bean and populates it with the given
	 * primary value(s)
	 * 
	 * @param id;
	 * @return the new instance
	 */
	private CarRecord newInstanceWithPrimaryKey(Integer id) {
		CarRecord car = new CarRecord();
		car.setId(id);
		return car;
	}

	// ----------------------------------------------------------------------
	@Override
	protected CarRecord newInstance() {
		return new CarRecord();
	}

	// ----------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see interface
	 */
	public CarRecord findById(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// TODO Auto-generated method stub
		CarRecord car = newInstanceWithPrimaryKey(id);

		return super.doSelect(car);
	}

	// ----------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see interface
	 */
	public List<CarRecord> findAll() throws JsonParseException, JsonMappingException, IOException {
		// TODO Auto-generated method stub
		return super.doSelectAll();
	}

	// ----------------------------------------------------------------------
	/**
	 * Loads the given bean, it is supposed to contains the primary key value(s)
	 * in its attribute(s)<br>
	 * If found, the given instance is populated with the values retrieved from
	 * the database<br>
	 * If not found, the given instance remains unchanged
	 * 
	 * @param car
	 * @return true if found, false if not found
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public CarRecord load(CarRecord car) throws JsonParseException, JsonMappingException, IOException {
		return super.doSelect(car);
	}

	// ----------------------------------------------------------------------
	/**
	 * Inserts the given bean in the database
	 * 
	 * @param car
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public String insert(CarRecord car) throws JsonGenerationException, JsonMappingException, IOException {
		return super.doInsert(car);
	}

	// ----------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see interface
	 */
	public CarRecord save(CarRecord car) throws JsonGenerationException, JsonMappingException, IOException {
		// TODO Auto-generated method stub
		if (super.doExists(car)) {
			super.doUpdate(car);
		} else {
			super.doInsert(car);
		}
		return car;
	}

	// ----------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see interface
	 */
	public boolean update(CarRecord car) throws JsonGenerationException, JsonMappingException, IOException {
		// TODO Auto-generated method stub
		String r = super.doUpdate(car);
		return r.equals("OK");
	}

	// ----------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see interface
	 */
	public CarRecord create(CarRecord car) throws JsonGenerationException, JsonMappingException, IOException {
		// TODO Auto-generated method stub
		insert(car);
		return car;
	}

	// ----------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see interface
	 */
	public boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		CarRecord car = newInstanceWithPrimaryKey(id);
		long r = super.doDelete(car);
		return r > 0;
	}

	// ----------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see interface
	 */
	public boolean delete(CarRecord car) {
		// TODO Auto-generated method stub
		long r = super.doDelete(car);
		return r > 0;
	}

	// ----------------------------------------------------------------------
	/**
	 * Checks the existence of a record in the database using the given primary
	 * key value(s)
	 * 
	 * @param id;
	 * @return
	 */
	// @Override
	public boolean exists(Integer id) {
		CarRecord car = newInstanceWithPrimaryKey(id);
		return super.doExists(car);
	}

	// ----------------------------------------------------------------------
	/**
	 * Checks the existence of the given bean in the database
	 * 
	 * @param car
	 * @return
	 */
	// @Override
	public boolean exists(CarRecord car) {
		return super.doExists(car);
	}

	// ----------------------------------------------------------------------
	/**
	 * Counts all the records present in the database
	 * 
	 * @return
	 */
	// @Override
	public long count() {
		return super.doCountAll();
	}

	@Override
	protected String getSelectAll() {
		return SELECT_ALL;
	}

}
