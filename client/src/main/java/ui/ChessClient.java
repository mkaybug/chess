package ui;

import exception.ResponseException;
import server.ServerFacade;

import java.util.Arrays;

public class ChessClient {
  private String username = null;
  private final ServerFacade server;
  private final String serverUrl;
  private State state = State.SIGNEDOUT;

  public ChessClient(String serverUrl) {
    server = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
  }

  public String eval(String input) {
    try {
      String[] tokens = input.toLowerCase().split(" ");
      String cmd = (tokens.length > 0) ? tokens[0] : "help";
      String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        // Prelogin UIs
        case "login" -> login(params);
        case "signout" -> signOut();
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

  private String login(String[] params) throws ResponseException {
    if (params.length >= 1) {
      state = State.SIGNEDIN;
      username = String.join("-", params);
      return String.format("You signed in as %s.", username);
    }
    return null;
  }

  private String signOut() {
    return null;
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
            createGame <GAME_NAME>
            listGames
            joinGame <GAME_ID> [WHITE|BLACK|<empty>] - specify your team color
            observe <ID> - observe a game being played
            logout
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
