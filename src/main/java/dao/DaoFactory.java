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
     * M�thodes de r�cup�ration de l'impl�mentation des diff�rents DAO (un seul
     * pour le moment)
     */
 
}