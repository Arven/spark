package sparkfive;

import sparkfive.Spark;
import sparkfive.Request;
import sparkfive.Response;
import sparkfive.Filter;
import sparkfive.Route;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sparkfive.util.SparkTestUtil;

public class BodyAvailabilityTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BodyAvailabilityTest.class);

    private static final String BODY_CONTENT = "the body content";

    private static SparkTestUtil testUtil;

    private static String beforeBody = null;
    private static String routeBody = null;
    private static String afterBody = null;

    @AfterClass
    public static void tearDown() {
        Spark.stop();

        beforeBody = null;
        routeBody = null;
        afterBody = null;
    }

    @BeforeClass
    public static void setup() {
        LOGGER.debug("setup()");

        testUtil = new SparkTestUtil(4567);

        beforeBody = null;
        routeBody = null;
        afterBody = null;
                
        Spark.before("/hello", new Filter(){
            @Override
            public void handle(Request request, Response response) throws Exception {
                LOGGER.debug("before-req.body() = " + request.body());
                beforeBody = request.body();
            }
        });

        Spark.post("/hello", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                LOGGER.debug("get-req.body() = " + req.body());
                routeBody = req.body();
                return req.body();
            }
        });
        
        Spark.after("/hello", new Filter(){
            @Override
            public void handle(Request req, Response res) throws Exception {
                LOGGER.debug("after-before-req.body() = " + req.body());
                afterBody = req.body();
            }
        });

        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }
    }

    @Test
    public void testPost() {
        try {
            SparkTestUtil.UrlResponse response = testUtil.doMethod("POST", "/hello", BODY_CONTENT);
            LOGGER.info(response.body);
            Assert.assertEquals(200, response.status);
            Assert.assertTrue(response.body.contains(BODY_CONTENT));

            Assert.assertEquals(BODY_CONTENT, beforeBody);
            Assert.assertEquals(BODY_CONTENT, routeBody);
            Assert.assertEquals(BODY_CONTENT, afterBody);

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}