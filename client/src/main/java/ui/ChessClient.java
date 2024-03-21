package ui;

import model.AuthData;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import ui.exception.ResponseException;
import ui.server.ServerFacade;
import static ui.EscapeSequences.*;

import java.util.Arrays;

public class ChessClient {
  private String username = null;
  private String authToken = null;
  private final ServerFacade server;
  private final String serverUrl;
  private State state = State.SIGNEDOUT;

  public ChessClient(String serverUrl) {
    server = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
  }

  public String eval(String input) {
    try {
      String[] tokens = input.split(" ");
      String cmd = (tokens.length > 0) ? tokens[0] : "help";
      String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        // Prelogin UIs
        case "register" -> register(params);
        case "login" -> login(params);
        case "logout" -> logout();
        case "quit" -> "quit";
        default -> help();
        // Postlogin UIs
        // help
        // logout
        // createGame
        // listGames
        // joinGame
        // observe
      };
    } catch (ResponseException e) {
      return e.getMessage();
    }
  }

  private String register(String[] params) throws ResponseException {
    RegisterRequest registerRequest = new RegisterRequest(params[0], params[1], params[2]);
    System.out.println(server.register(registerRequest));
    return "You registered.";
  }

  private String login(String[] params) throws ResponseException {
    System.out.print(SET_TEXT_COLOR_YELLOW + "Logging in...\n");

    LoginRequest loginRequest = new LoginRequest(params[0], params[1]);
    AuthData newAuth = server.login(loginRequest);

    authToken = newAuth.authToken();
    state = State.SIGNEDIN;
    username = params[0];
    return String.format("Welcome %s! Type help to begin playing.", username);
  }

  private String logout() throws ResponseException {
    System.out.print(SET_TEXT_COLOR_YELLOW + "Logging out...\n");

    LogoutRequest logoutRequest = new LogoutRequest(authToken);
    server.logout(logoutRequest);

    state = State.SIGNEDOUT;
    return ("You successfully logged out.");
  }

  private String help() {
    if (state == State.SIGNEDOUT) {
      return """
                register <USERNAME> <PASSWORD <EMAIL> - to create an account
                login <USERNAME> <PASSWORD - login
                quit - exit program
                help - display possible commands
              """;
    }
    return """
              createGame <GAME_NAME> - start a new game
              listGames - list all games
              joinGame <GAME_ID> [WHITE|BLACK|<empty>] - specify your team color
              observe <ID> - observe a game being played
              logout - logout
              quit - exit program
              help - display possible commands
            """;
  }

  public String printState() {
    if (state == State.SIGNEDOUT) {
      return "LOGGED_OUT";
    }
    return "LOGGED_IN";
  }
}
