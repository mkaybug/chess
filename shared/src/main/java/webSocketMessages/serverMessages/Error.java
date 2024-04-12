package webSocketMessages.serverMessages;

import com.google.gson.Gson;

public class Error extends ServerMessage {
  private String errorMessage;
  public Error(String errorMessage) {
    super(ServerMessageType.ERROR);
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public String toString() {
    return new Gson().toJson(this);
  }
}
