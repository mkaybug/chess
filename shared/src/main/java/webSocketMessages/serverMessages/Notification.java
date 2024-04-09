package webSocketMessages.serverMessages;

public class Notification extends ServerMessage {
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  private String message;
  public Notification(ServerMessageType type, String message) {
    super(type);
    this.message = message;
  }
}
