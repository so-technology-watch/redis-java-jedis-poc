package org.demo.data.record;

import java.io.Serializable;

/*
 * Created on 2017-01-20 ( Date ISO 2017-01-20 - Time 09:29:19 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.0.0
 */

/**
 * Java bean for entity "Car" <br>
 * Contains only "wrapper types" (no primitive types) <br>
 * Can be used both as a "web form" and "persistence record" <br>
 * 
 *
 * 
 */
public class CarRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id; // int // Id or Primary Key

	private String name; // String

	private Integer driver; // Integer

	public CarRecord(String name, Integer driver) {
		this.name = name;
		this.driver = driver;
	}

	public CarRecord() {
		// TODO Auto-generated constructor stub
	}

	// ----------------------------------------------------------------------
	// GETTER(S) & SETTER(S) FOR ID OR PRIMARY KEY
	// ----------------------------------------------------------------------
	/**
	 * Set the "id" field value This field is mapped on the database column "id"
	 * ( type "", NotNull : true )
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Get the "id" field value This field is mapped on the database column "id"
	 * ( type "", NotNull : true )
	 * 
	 * @return the field value
	 */
	public Integer getId() {
		return this.id;
	}

	public Integer getDriver() {
		return this.driver;
	}
	// ----------------------------------------------------------------------
	// GETTER(S) & SETTER(S) FOR OTHER DATA FIELDS
	// ----------------------------------------------------------------------

	/**
	 * Set the "name" field value This field is mapped on the database column
	 * "name" ( type "", NotNull : true )
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the "name" field value This field is mapped on the database column
	 * "name" ( type "", NotNull : true )
	 * 
	 * @return the field value
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the "driver" field value This field is mapped on the database column
	 * "driver" ( type "", NotNull : false )
	 * 
	 * @param driver
	 */
	public void setDriver(Integer driver) {
		this.driver = driver;
	}

	/**
	 * Get the "driver" field value This field is mapped on the database column
	 * "driver" ( type "", NotNull : false )
	 * 
	 * @return the field value
	 */
	public Integer carProperties() {
		return this.driver;
	}

	// ----------------------------------------------------------------------
	// toString METHOD
	// ----------------------------------------------------------------------
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(id);
		sb.append("|");
		sb.append(name);
		sb.append("|");
		sb.append(driver);
		return sb.toString();
	}

}
