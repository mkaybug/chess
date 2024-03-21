package ui.server;

import com.google.gson.Gson;
import model.AuthData;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import ui.exception.ResponseException;
import model.GameData;
import model.UserData;

import java.io.*;
import java.net.*;

public class ServerFacade {

  private final String serverUrl;

  public ServerFacade(String url) {
    serverUrl = url;
  }

  public AuthData register(RegisterRequest registerRequest) throws ResponseException {
    var path = "/user";
    return this.makeRequest("POST", path, registerRequest, AuthData.class);
  }

  public AuthData login(LoginRequest loginRequest) throws ResponseException {
    String path = "/session";
    return this.makeRequest("POST", path, loginRequest, AuthData.class);
  }

  public void logout(LogoutRequest logoutRequest) throws ResponseException {
    var path = "/session";
    this.makeRequest("DELETE", path, logoutRequest, null);
  }

  public GameData[] listGames(String authToken) throws ResponseException {
    var path = "/game";
    record listGamesResponse(GameData[] game) {
    }
    var response = this.makeRequest("GET", path, authToken, listGamesResponse.class);
    return response.game();
  }

  public GameData createGame(String authToken) throws ResponseException {
    var path = "/game";
    return this.makeRequest("GET", path, authToken, GameData.class);
  }

  public void joinGame(GameData game) throws ResponseException {
    var path = "/game";
    this.makeRequest("PUT", path, game, null);
  }

  private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
    try {
      URL url = (new URI(serverUrl + path)).toURL();
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      http.setRequestMethod(method);
      http.setDoOutput(true);

      writeBody(request, http);
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