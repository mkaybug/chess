package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.LoginRequest;
import model.RegisterRequest;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
    MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    private final GameService gameService = new GameService(memoryAuthDAO, memoryGameDAO, memoryUserDAO);
    private final UserService userService = new UserService(memoryAuthDAO, memoryUserDAO);
    private final ClearService clearService = new ClearService(memoryAuthDAO, memoryGameDAO, memoryUserDAO);


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");


        Spark.delete("/db", this::clearDatabase);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
//        Spark.exception(ResponseException.class, this::exceptionHandler);

//        Spark.delete("/db", (request, response) -> new DatabaseHandler(authService, gameService, userService).clear(request, response));

        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

//    private void exceptionHandler(ResponseException ex, Request req, Response res) {
//        res.status(ex.StatusCode());
//    }

    private Object clearDatabase(Request req, Response res) throws DataAccessException {
        clearService.clearDatabase();
        res.status(204);
        return "";
    }

    private Object register(Request req, Response res) throws DataAccessException {
        RegisterRequest user = new Gson().fromJson(req.body(), RegisterRequest.class);
        AuthData auth = userService.register(user);
//        webSocketHandler.makeNoise(pet.name(), pet.sound());
        return new Gson().toJson(auth);
    }

    private Object login(Request req, Response res) throws DataAccessException {
        LoginRequest user = new Gson().fromJson(req.body(), LoginRequest.class);
        AuthData auth = userService.login(user);
//        webSocketHandler.makeNoise(pet.name(), pet.sound());
        return new Gson().toJson(auth);
    }

    private Object logout(Request req, Response res) throws DataAccessException {
        LoginRequest user = new Gson().fromJson(req.body(), LoginRequest.class);

        // FIXME where I left off: how do I get the user from the header information?
//        userService.logout();
        res.status(204);
        return "";
    }

    private Object listGames(Request req, Response res) throws DataAccessException {
//        res.type("application/json");
//        var list = service.listPets().toArray();
//        return new Gson().toJson(Map.of("pet", list));
      return null;
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
//        LoginRequest user = new Gson().fromJson(req.body(), LoginRequest.class);
//        AuthData auth = userService.login(user);
////        webSocketHandler.makeNoise(pet.name(), pet.sound());
//        return new Gson().toJson(auth);
      return null;
    }


    private Object joinGame(Request req, Response res) throws DataAccessException {
        return null;
    }
}
