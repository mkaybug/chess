package server.handler;

import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class DatabaseHandler {
  private final GameService gameService;
  private final UserService userService;

  public DatabaseHandler(GameService gameService, UserService userService) {
    this.gameService = gameService;
    this.userService = userService;
  }
  public Object clear(Request request, Response response) {
//    try {
//      userService.deleteAllUsers();
//      authService.deleteAllAuthTokens();
//      gameService.deleteAllGames();
//      response.status(200);
//      return "Database cleared successfully.";
//    }
//    catch (DataAccessException e){
//      response.status(500);
//      return e.getMessage();
//    }
    return null;
  }
}
