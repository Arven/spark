package sparkfive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sparkfive.SparkInstance;

/**
 * Created by Arven on 2015-01-19.
 */
public class LambdaInstanceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LambdaTest.class);
    public static void main(String[] args) {
        
        SparkInstance in = new SparkInstance();
        
        in.get("/hello", new Route() {

            @Override
            public Object handle(Request request, Response response) throws Exception {
                LOGGER.info("request = " + request.pathInfo());
                return "Hello World";
            }
            
        });

    }

}