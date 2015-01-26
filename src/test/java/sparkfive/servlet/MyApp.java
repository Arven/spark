package sparkfive.servlet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import sparkfive.Filter;
import sparkfive.Request;
import sparkfive.Response;
import sparkfive.Route;

import static sparkfive.Spark.*;

public class MyApp implements SparkApplication {

    @Override
    public void init() {
        try {
            externalStaticFileLocation(System.getProperty("java.io.tmpdir"));
            staticFileLocation("/public");

            File tmpExternalFile = new File(System.getProperty("java.io.tmpdir"), "externalFile.html");
            FileWriter writer = new FileWriter(tmpExternalFile);
            writer.write("Content of external file");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        before("/protected/*", new Filter(){
            @Override
            public void handle(Request request, Response response) throws Exception {
                halt(401, "Go away!");
            }
        });

        get("/hi", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return "Hello World!";
            }
        });

        get("/:param", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return "echo: " + request.params(":param");
            }
        });

        get("/", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return "Hello Root!";
            }
        });

        post("/poster", new Route(){
            @Override
            public Object handle(Request request, Response response) throws Exception {
                response.status(201);
                return "Body was: " + request.body();
            }
        });

        after("/hi", new Filter(){
            @Override
            public void handle(Request request, Response response) throws Exception {
                response.header("after", "foobar");
            }
        });

        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }
    }

}
