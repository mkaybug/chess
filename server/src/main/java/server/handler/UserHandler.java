package server.handler;

import com.google.gson.Gson;
import service.UserService;
import spark.Request;
import spark.Response;

public class UserHandler {
  private final UserService userService;
  private final Gson gson;

  public UserHandler(UserService userService) {
    this.userService = userService;
    this.gson = new Gson();
  }
  public Object clear(Request request, Response response) {

    return null;
  }
}
