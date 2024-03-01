package server;

import com.google.gson.Gson;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import server.handler.AuthHandler;
import server.handler.GameHandler;
import server.handler.UserHandler;
import service.AuthService;
import service.GameService;
import service.UserService;
import server.handler.DatabaseHandler;
import spark.*;

public class Server {
    private final GameService gameService = new GameService(new MemoryAuthDAO(), new MemoryGameDAO(), new MemoryUserDAO());
    private final UserService userService = new UserService(new MemoryAuthDAO(), new MemoryUserDAO());

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

//        Spark.delete("/db", (request, response) -> new DatabaseHandler(authService, gameService, userService).clear(request, response));

        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
