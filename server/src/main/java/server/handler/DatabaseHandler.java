package server.handler;

import com.google.gson.Gson;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import dataAccess.DataAccessException;

import javax.xml.crypto.Data;

public class DatabaseHandler {
  private final AuthService authService;
  private final GameService gameService;
  private final UserService userService;

  public DatabaseHandler(AuthService authService, GameService gameService, UserService userService) {
    this.authService = authService;
    this.gameService = gameService;
    this.userService = userService;
  }
  public Object clear(Request request, Response response) {
    try {
      userService.deleteAllUsers();
      authService.deleteAllAuthTokens();
      gameService.deleteAllGames();
      response.status(200);
      return "Database cleared successfully.";
    }
    catch (DataAccessException e){
      response.status(500);
      return e.getMessage();
    }
    return null;
  }
}
