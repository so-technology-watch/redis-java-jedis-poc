package dao;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;

public class DaoFactory {

private static String url = "redis://localhost:6379";


    RedisConnection getConnection(){
     
    	RedisClient redisClient = new RedisClient(RedisURI.create(url));
    	RedisConnection<String, String> connection = redisClient.connect();
    	
    	return connection;
    }

    /*
     * Méthodes de récupération de l'implémentation des différents DAO (un seul
     * pour le moment)
     */
 
}