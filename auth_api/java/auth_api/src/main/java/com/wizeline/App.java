package com.wizeline;
import com.wizeline.exception.BusinessException;
import com.wizeline.exception.NotFoundException;
import com.wizeline.exception.UnauthorizedException;
import io.jsonwebtoken.JwtException;

import static com.wizeline.JsonUtil.*;
import static com.wizeline.Methods.*;
import static com.wizeline.Response.*;
import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
      System.out.println( "Listening on: http://localhost:8000/" );

      port(8000);
      get("/", App::routeRoot);
      get("/_health", App::routeRoot);
      post("/login", App::urlLogin, json());
      get("/protected", App::protect, json());

    exception(NotFoundException.class, (e, request, response) -> {
        response.status(404);
        response.type("application/json");
        response.body(e.getMessage());
    });

    exception(UnauthorizedException.class, (e, request, response) -> {
        response.status(403);
        response.type("application/json");
        response.body(e.getMessage());
    });

    exception(BusinessException.class, (e, request, response) -> {
        response.status(500);
        response.type("application/json");
        response.body(e.getMessage());
    });

    exception(JwtException.class, (e, request, response) -> {
        response.status(403);
        response.type("application/json");
        response.body(e.getMessage());
    });

    }

    public static Object routeRoot(spark.Request req, spark.Response res) throws Exception {
      return "OK";
    }

    public static Object urlLogin(spark.Request req, spark.Response res) throws Exception {
      String username = req.queryParams("username");
      String password = req.queryParams("password");
      Response r = new Response(Methods.generateToken(username, password));
      res.type("application/json");
      return r;
    }

    public static Object protect(spark.Request req, spark.Response res) throws Exception {
      String authorization = req.headers("Authorization").split(" ")[1];
      Response r = new Response(Methods.accessData(authorization));
      res.type("application/json");
      return r;
    }
}
