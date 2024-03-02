package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.*;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;

import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.Collection;
import java.util.Objects;

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

        // Register endpoints
        Spark.delete("/db", this::clearDatabase);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clearDatabase(Request req, Response res) throws DataAccessException {
        clearService.clearDatabase();
        res.status(200);
        return "";
    }

    private Object register(Request req, Response res) throws DataAccessException {
        try {
            RegisterRequest user = new Gson().fromJson(req.body(), RegisterRequest.class);
            AuthData auth = userService.register(user);
            return new Gson().toJson(auth);
        }
        catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: bad request")) {
                res.status(400);
            }
            else {
                res.status(403);
            }
          return new Gson().toJson(new ExceptionMessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ExceptionMessageResponse(e.getMessage()));
        }
    }

    private Object login(Request req, Response res) throws DataAccessException {
        try {
            LoginRequest user = new Gson().fromJson(req.body(), LoginRequest.class);
            AuthData auth = userService.login(user);
            return new Gson().toJson(auth);
        }
        catch (DataAccessException e) {
            res.status(401);
            return new Gson().toJson(new ExceptionMessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ExceptionMessageResponse(e.getMessage()));
        }
    }

    private Object logout(Request req, Response res) throws DataAccessException {
        try {
            String authToken = req.headers("authorization");
            userService.logout(authToken);
            res.status(200);
            return "";
        }
        catch (DataAccessException e) {
            res.status(401);
            return new Gson().toJson(new ExceptionMessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ExceptionMessageResponse(e.getMessage()));
        }
    }

    private Object listGames(Request req, Response res) throws DataAccessException {
        try {
            String authToken = req.headers("authorization");
            Collection<GameData> games = gameService.listGames(authToken);
            return new Gson().toJson(new GamesResponse(games));
        }
        catch (DataAccessException e) {
            res.status(401);
            return new Gson().toJson(new ExceptionMessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ExceptionMessageResponse(e.getMessage()));
        }
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
        try {
            String authToken = req.headers("authorization");
            CreateGameRequest gameName = new Gson().fromJson(req.body(), CreateGameRequest.class);
            GameData game = gameService.createGame(authToken, gameName);
            return new Gson().toJson(game);
        }
        catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: bad request")) {
                res.status(400);
            }
            else {
                res.status(401);
            }
            return new Gson().toJson(new ExceptionMessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ExceptionMessageResponse(e.getMessage()));
        }
    }


    private Object joinGame(Request req, Response res) throws DataAccessException {
        try {
            String authToken = req.headers("authorization");
            JoinGameRequest gameName = new Gson().fromJson(req.body(), JoinGameRequest.class);
            gameService.joinGame(authToken, gameName);
            res.status(200);
            return "{}";
        }
        catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: bad request")) {
                res.status(400);
            }
            else if (Objects.equals(e.getMessage(), "Error: unauthorized")) {
                res.status(401);
            }
            else {
                res.status(403);
            }
            return new Gson().toJson(new ExceptionMessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ExceptionMessageResponse(e.getMessage()));
        }
    }
}
