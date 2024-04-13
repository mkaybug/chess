package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.AuthData;
import model.GameData;
import model.response.GamesResponse;
import ui.exception.ResponseException;
import ui.server.ServerFacade;
import ui.websocket.WebSocketFacade;
import ui.websocket.messageHandler.ServerMessageHandler;

import static ui.EscapeSequences.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class ChessClient {
  private String username = null;
  private String authToken = null;
  private int gameID = 0;
  private PrintChessBoard printChessBoard = null;
  private final ServerFacade server;
  private final String serverUrl;
  private final ServerMessageHandler messageHandler;
  private WebSocketFacade ws;
  private SignedState signedState = SignedState.SIGNEDOUT;
  private GameState gameState = GameState.INACTIVE;

  public ChessClient(String serverUrl, ServerMessageHandler messageHandler) {
    server = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
    this.messageHandler = messageHandler;
  }

  public PrintChessBoard getPrintChessBoard() {
    return printChessBoard;
  }

  public void setPrintChessBoard(PrintChessBoard printChessBoard) {
    this.printChessBoard = printChessBoard;
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
        case "leave" -> leaveGame();
        case "redraw" -> redrawGame();
        case "makeMove" -> makeMove(params);
        case "resign" -> resign();
        case "highlight" -> highlight(params);
        case "quit" -> "quit";
        default -> help();
      };
    } catch (ResponseException e) {
      // Replace this with switch statement to print proper error messages.
      if (Objects.equals(e.getMessage(), "failure: 400")) {
        return "Error: bad request";
      }
      else if (Objects.equals(e.getMessage(), "failure: 401"))
        return "Error: unauthorized, please login";
      else if (Objects.equals(e.getMessage(), "failure: 403")) {
        return "Error: unauthorized";
      }
      return e.getMessage();
    }
  }

  private String register(String[] params) throws ResponseException {
    if (params.length < 3) {
      throw new ResponseException(500, "Error: too few arguments");
    }
    else if (params.length > 3) {
      throw new ResponseException(500, "Error: too many arguments");
    }

    AuthData newAuth = server.register(params[0], params[1], params[2]);

    username = params[0];
    authToken = newAuth.authToken();
    signedState = SignedState.SIGNEDIN;
    return "  Registration successful, you are now logged in.";
  }

  private String login(String[] params) throws ResponseException {
    System.out.print(SET_TEXT_COLOR_YELLOW + "  Logging in...\n");

    AuthData newAuth = server.login(params[0], params[1]);

    username = params[0];
    authToken = newAuth.authToken();
    signedState = SignedState.SIGNEDIN;
    return String.format("  Welcome %s! Type help to begin playing.", username);
  }

  private String logout() throws ResponseException {
    if (gameState == GameState.ACTIVE) {
      leaveGame();
    }

    System.out.print(SET_TEXT_COLOR_YELLOW + "  Logging out...\n");
    server.logout(authToken);

    username = null;
    authToken = null;
    signedState = SignedState.SIGNEDOUT;
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
    System.out.print(SET_TEXT_COLOR_YELLOW + "  Joining game...\n");
    if (params.length == 0) {
      throw new ResponseException(500, "Error: too few arguments");
    }
    else if (params.length > 2) {
      throw new ResponseException(500, "Error: too many arguments");
    }
    else if (params.length == 2) {
      // Call server join API
      server.joinGame(authToken, params[0], params[1]);
      // Open WebSocket connection with the server
      ws = new WebSocketFacade(serverUrl, messageHandler);
      // Send JOIN_PLAYER WebSocket message to the server
      if (Objects.equals(params[1], "WHITE")) {
        ws.joinPlayer(authToken, Integer.parseInt(params[0]), ChessGame.TeamColor.WHITE);
      }
      else if (Objects.equals(params[1], "BLACK")) {
        ws.joinPlayer(authToken, Integer.parseInt(params[0]), ChessGame.TeamColor.BLACK);
      }
      gameID = Integer.parseInt(params[0]);
      gameState = GameState.ACTIVE;
      return String.format("You joined on team %s", params[1]);
    }
    else {
      // Call server join API
      server.joinGame(authToken, params[0], null);
      // Open WebSocket connection with the server
      ws = new WebSocketFacade(serverUrl, messageHandler);
      // Send JOIN_OBSERVER WebSocket message to the server
      ws.joinObserver(authToken, Integer.parseInt(params[0]));
      gameID = Integer.parseInt(params[0]);
      gameState = GameState.ACTIVE;
      return "You joined as an observer.";
    }
  }

  private String leaveGame() throws ResponseException {
    if (gameState == GameState.INACTIVE) {
      return "You are not currently playing a game.";
    }

    System.out.print(SET_TEXT_COLOR_YELLOW + "  Leaving game...\n");

    ws.leaveGame(authToken, gameID);
    gameID = 0;
    gameState = GameState.INACTIVE;
    return "You successfully left the game.";
  }

  private String redrawGame() throws ResponseException {
    if (gameState == GameState.INACTIVE) {
      return "You are not currently playing a game.";
    }

    return printChessBoard.printBoard();
  }

  private String makeMove(String[] params) throws ResponseException {
    if (gameState == GameState.INACTIVE) {
      return "You are not currently playing a game.";
    }

    if (params.length < 4) {
      throw new ResponseException(500, "Error: too few arguments");
    }
    else if (params.length > 4) {
      throw new ResponseException(500, "Error: too many arguments");
    }

    int startColumn = columnLetterToInt(params[0]);
    int startRow = Integer.parseInt(params[1]);
    int endColumn = columnLetterToInt(params[2]);
    int endRow = Integer.parseInt(params[3]);

    ChessPosition startPosition = new ChessPosition(startRow, startColumn);
    ChessPosition endPosition = new ChessPosition(endRow, endColumn);
    try {
      ws.makeMove(authToken, gameID, new ChessMove(startPosition, endPosition, null));
    }
    catch (Exception e){
      throw new ResponseException(500, e.getMessage());
    }

    return "";
  }

  private String resign() throws ResponseException {
    if (gameState == GameState.INACTIVE) {
      return "You are not currently playing a game.";
    }

    System.out.print(SET_TEXT_COLOR_YELLOW + "  Resigning from game...\n");

    ws.resign(authToken, gameID);
    gameID = 0;
    gameState = GameState.INACTIVE;
    return "Success.";
  }

  private String highlight(String[] params) {
    if(gameState == GameState.INACTIVE) {
      return "You are not currently playing a game.";
    }

    int column = columnLetterToInt(params[0]);
    int row = Integer.parseInt(params[1]);
    return printChessBoard.highlightPossibleMoves(row, column);
  }

  private int columnLetterToInt(String columnLetter) {
    return switch (columnLetter) {
      case "a" -> 1;
      case "b" -> 2;
      case "c" -> 3;
      case "d" -> 4;
      case "e" -> 5;
      case "f" -> 6;
      case "g" -> 7;
      case "h" -> 8;
      default -> throw new IllegalStateException("Unexpected value: " + columnLetter);
    };
  }

    String help() {
    if (signedState == SignedState.SIGNEDOUT) {
      return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - login
                quit - exit program
                help - display possible commands
              """;
    }
    if (gameState == GameState.ACTIVE) {
      return """
              redraw - redraw the chessboard
              leave - leave the game
              move <START_ROW> <START_COLUMN> <END_ROW> <END_COLUMN> - make a move from one coordinate to another
              resign - forfeit but don't leave the game, ends the game
              highlight <ROW> <COLUMN> - show possible moves for piece at this co-ordinate
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
    if (signedState == signedState.SIGNEDOUT) {
      return "LOGGED_OUT";
    }
    return "LOGGED_IN";
  }

  public String printChessBoard(GameData gameData) {
    if (Objects.equals(gameData.blackUsername(), username)) {
      printChessBoard = new PrintChessBoard(gameData.game(), ChessGame.TeamColor.BLACK);
    }
    else {
      printChessBoard = new PrintChessBoard(gameData.game(), ChessGame.TeamColor.WHITE);
    }
    return printChessBoard.printBoard();
  }
}