/*
 * Created on 2017-02-20 ( Date ISO 2017-02-20 - Time 21:59:28 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.0.0
 */
package org.demo.persistence.impl.redis;

import java.util.List;

import javax.inject.Named;

import org.demo.data.record.CarRecord;
import org.demo.persistence.CarPersistence;
import org.demo.persistence.impl.redis.commons.GenericDAO;

@Named("CarPersistence")
public class CarPersistenceRedis extends GenericDAO<CarRecord> implements CarPersistence {

	/**
	 * DAO constructor
	 */
	public CarPersistenceRedis() {
		super("car", CarRecord.class);
	}

	/**
	 * make key in a good format
	 * 
	 * @param bean;
	 * @return
	 * @return key with a good format
	 */
	@Override
	protected String getKey(CarRecord bean) {
		return bean.getId().toString();
	}

	/**
	 * Creates a new instance of the bean primary value(s)
	 * 
	 * @param key;
	 * @return the new instance
	 */
	private CarRecord newInstanceWithPrimaryKey(Integer id) {
		CarRecord car = new CarRecord();
		car.setId(id);
		return car;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see interface
	 */
	@Override
	public CarRecord findById(Integer id) {
		CarRecord car = newInstanceWithPrimaryKey(id);

		return super.doSelect(car);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see interface
	 */
	@Override
	public List<CarRecord> findAll() {

		return super.doSelectAll();
	}

	/**
	 * Loads the given bean, it is supposed to contains the primary key value(s)
	 * in its attribute(s)<br>
	 * If found, the given instance is populated with the values retrieved from
	 * the database<br>
	 * If not found, the given instance remains unchanged
	 */
	public CarRecord load(CarRecord car) {
		return super.doSelect(car);
	}

	/**
	 * Inserts the given bean in the database
	 * 
	 */
	public boolean insert(CarRecord car) {
		if (car.getId() != null)
			return super.doInsert(car);
		else
			return super.doInsertIncr(car);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see interface
	 */
	@Override
	public CarRecord save(CarRecord car) {
		if (super.doExists(car)) {
			super.doUpdate(car);
		} else {
			insert(car);
		}
		return car;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see interface
	 */
	@Override
	public boolean update(CarRecord car) {
		return super.doUpdate(car);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see interface
	 */
	@Override
	public CarRecord create(CarRecord car) {
		insert(car);
		return car;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see interface
	 */
	@Override
	public boolean deleteById(Integer id) {
		CarRecord car = newInstanceWithPrimaryKey(id);
		long r = super.doDelete(car);
		return r > 0L;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see interface
	 */
	@Override
	public boolean delete(CarRecord car) {
		long r = super.doDelete(car);
		return r > 0L;
	}

	/**
	 * Checks the existence of a record in the database using the given primary
	 * key value(s)
	 * 
	 * @return
	 */
	public boolean exists(Integer id) {
		CarRecord car = newInstanceWithPrimaryKey(id);

		return super.doExists(car);
	}

	/**
	 * Checks the existence of the given bean in the database
	 * 
	 * @return
	 */
	public boolean exists(CarRecord car) {
		return super.doExists(car);
	}

	/**
	 * Counts all the records present in the database
	 * 
	 * @return
	 */
	public long count() {
		return super.doCountAll();
	}

	@Override
	protected void setBeanId(CarRecord car, Integer id) {
		car.setId((int) id);
	}
}
