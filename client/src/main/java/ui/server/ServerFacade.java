package ui.server;

import com.google.gson.Gson;
import model.AuthData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.GamesResponse;
import ui.exception.ResponseException;
import model.GameData;

import java.io.*;
import java.net.*;
import java.util.Objects;

public class ServerFacade {

  private final String serverUrl;

  public ServerFacade(String url) {
    serverUrl = url;
  }

  public AuthData register(String username, String password, String email) throws ResponseException {
    var path = "/user";
    RegisterRequest registerRequest = new RegisterRequest(username, password, email);
    return this.makeRequest("POST", path, null, registerRequest, AuthData.class);
  }

  public AuthData login(String username, String password) throws ResponseException {
    String path = "/session";
    LoginRequest loginRequest = new LoginRequest(username, password);
    return this.makeRequest("POST", path, null, loginRequest, AuthData.class);
  }

  public void logout(String authToken) throws ResponseException {
    var path = "/session";
    this.makeRequest("DELETE", path, authToken, null, null);
  }

  public GamesResponse listGames(String authToken) throws ResponseException {
    var path = "/game";
    record ListGamesResponse(GameData[] game) {
    }
    var response = this.makeRequest("GET", path, authToken, null, GamesResponse.class);
    return response;
  }

  public GameData createGame(String authToken, String gameName) throws ResponseException {
    var path = "/game";
    CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
    return this.makeRequest("GET", path, authToken, createGameRequest, GameData.class);
  }

  public void joinGame(String authToken, String gameID, String teamColor) throws ResponseException {
    var path = "/game";
    JoinGameRequest joinGameRequest = null;
    if (Objects.equals(teamColor, "BLACK")) {
      joinGameRequest = new JoinGameRequest(teamColor, Integer.parseInt(gameID));
    }
    else if (Objects.equals(teamColor, "WHITE")) {
      joinGameRequest = new JoinGameRequest(teamColor, Integer.parseInt(gameID));
    }
    else {
      joinGameRequest = new JoinGameRequest(null, Integer.parseInt(gameID));
    }
    this.makeRequest("PUT", path, authToken, joinGameRequest, null);
  }

  private <T> T makeRequest(String method, String path, String authToken, Object request, Class<T> responseClass) throws ResponseException {
    try {
      // URL of the endpoint
      URL url = (new URI(serverUrl + path)).toURL();
      // Open the connection
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      // Set the request method (e.g., GET, POST, PUT, DELETE)
      http.setRequestMethod(method);
      // Set the authorization header -> if an authToken is given
      if (authToken != null) {
        http.setRequestProperty("authorization", authToken);
      }
      // Set the flag indicating that http will be used for output -> allow to write data to server
      http.setDoOutput(true);

      writeBody(request, http);
      // Send the request
      http.connect();
      throwIfNotSuccessful(http);
      return readBody(http, responseClass);
    } catch (Exception ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }


  private static void writeBody(Object request, HttpURLConnection http) throws IOException {
    if (request != null) {
      http.addRequestProperty("Content-Type", "application/json");
      String reqData = new Gson().toJson(request);
      try (OutputStream reqBody = http.getOutputStream()) {
        reqBody.write(reqData.getBytes());
      }
    }
  }

  private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
    var status = http.getResponseCode();
    if (!isSuccessful(status)) {
      throw new ResponseException(status, "failure: " + status);
    }
  }

  private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
    T response = null;
    if (http.getContentLength() < 0) {
      try (InputStream respBody = http.getInputStream()) {
        InputStreamReader reader = new InputStreamReader(respBody);
        if (responseClass != null) {
          response = new Gson().fromJson(reader, responseClass);
        }
      }
    }
    return response;
  }


  private boolean isSuccessful(int status) {
    return status / 100 == 2;
  }
}