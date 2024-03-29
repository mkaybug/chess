package ui;

import model.AuthData;
import model.GameData;
import model.response.GamesResponse;
import ui.exception.ResponseException;
import ui.server.ServerFacade;
import static ui.EscapeSequences.*;

import java.util.Arrays;
import java.util.Collection;

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
        case "register" -> register(params);
        case "login" -> login(params);
        case "logout" -> logout();
        case "createGame" -> createGame(params);
        case "listGames" -> listGames();
        case "joinGame" -> joinGame(params);
        case "quit" -> "quit";
        default -> help();
      };
    } catch (ResponseException e) {
      return e.getMessage();
    }
  }

  private String register(String[] params) throws ResponseException {
    server.register(params[0], params[1], params[2]);
    return "  Registration successful, login to play.";
  }

  private String login(String[] params) throws ResponseException {
    System.out.print(SET_TEXT_COLOR_YELLOW + "  Logging in...\n");

    AuthData newAuth = server.login(params[0], params[1]);

    username = params[0];
    authToken = newAuth.authToken();
    state = State.SIGNEDIN;
    return String.format("  Welcome %s! Type help to begin playing.", username);
  }

  private String logout() throws ResponseException {
    System.out.print(SET_TEXT_COLOR_YELLOW + "  Logging out...\n");
    server.logout(authToken);

    username = null;
    authToken = null;
    state = State.SIGNEDOUT;
    return (" You successfully logged out.");
  }

  private String createGame(String[] params) throws ResponseException {
    System.out.print(SET_TEXT_COLOR_YELLOW + "  Creating new game...\n");

    StringBuilder gameName = new StringBuilder();
    for (int i = 0; i < params.length; i++) {
      gameName.append(params[i]);
      if (i < params.length - 1) {
        gameName.append(" ");
      }
    }

    GameData newGame = server.createGame(authToken, String.valueOf(gameName));
    return String.format("  You created a new game \"%s\" the gameID is: %s. Use this ID to join the game.", newGame.gameName(), newGame.gameID());
  }

  private String listGames() throws ResponseException {
    System.out.print(SET_TEXT_COLOR_YELLOW + "  Listing games...\n");

    GamesResponse gamesResponse = server.listGames(authToken);
    Collection<GameData> games = gamesResponse.games();
    StringBuilder listGames = new StringBuilder();

    int i = 1;
    for (GameData game : games) {
      if (i != 1) {
        listGames.append("\n");
      }
      listGames.append("  ").append(i).append(": ").append(game.toString());
      i += 1;
    }
    return String.valueOf(listGames);
  }

  private String joinGame(String[] params) throws ResponseException {
    PrintChessBoard printBoard = new PrintChessBoard();

    System.out.print(SET_TEXT_COLOR_YELLOW + "  Joining game...\n");
    if (params.length > 1) {
      server.joinGame(authToken, params[0], params[1]);
      return printBoard.printBothChessBoards() + String.format("You joined on team %s", params[1]);
    }
    else {
      server.joinGame(authToken, params[0], null);
      return printBoard.printBothChessBoards() + "You joined as an observer.";
    }
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
