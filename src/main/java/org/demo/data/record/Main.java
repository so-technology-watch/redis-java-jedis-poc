package org.demo.data.record;

import org.demo.persistence.impl.redis.CarPersistenceRedis;

public class Main {

	public static void main(String[] args) {

		CarRecord car = new CarRecord("terrence", 12);
		CarRecord car1 = new CarRecord("terrence", 12);
		CarRecord car2 = new CarRecord("terrence", 12);

		CarPersistenceRedis persist = new CarPersistenceRedis();

		System.out.println(persist.findAll());

	}
}
