package ui;

import ui.websocket.messageHandler.ServerMessageHandler;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements ServerMessageHandler {
  private final ChessClient client;

  public Repl(String serverUrl) {
    client = new ChessClient(serverUrl, this);
  }

  public void run() {
    System.out.println("♕ Welcome to 240 Chess! Type help to get started. >>>");
    System.out.print(client.help());

    Scanner scanner = new Scanner(System.in);
    String result = "";
    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = client.eval(line);

        if (result.contains("\n")) {
          String[] resultLines = result.split("\n");
          for (String resultLine : resultLines) {
            if(resultLine.contains("-")) {
              String[] parts = resultLine.split(" - ", 2);
              System.out.print(SET_TEXT_COLOR_BLUE + parts[0]);
              System.out.print(SET_TEXT_COLOR_MAGENTA + " - " + parts[1] + "\n");
            } else {
              System.out.print(SET_TEXT_COLOR_BLUE + resultLine + "\n");
            }
          }
        }
        else {
          System.out.print(SET_TEXT_COLOR_BLUE + result);
        }
      } catch (Throwable e) {
          var msg = e.toString();
          System.out.print(msg);
      }
    }

    System.out.println("\n" + SET_TEXT_COLOR_BLUE + client.eval("leave"));
    System.out.println();
  }

  private void printPrompt() {
    System.out.print("\n" + "\u001B[0m" + "[" + client.printState() + "] >>> " + SET_TEXT_COLOR_GREEN);
  }

  @Override
  public void handleNotification(Notification notification) {
    System.out.println(SET_TEXT_COLOR_MAGENTA + "\n" + notification.getMessage());
    printPrompt();
  }

  @Override
  public void handleError(Error error) {
    System.out.println(SET_TEXT_COLOR_RED + "\n" + error.getMessage());
    printPrompt();
  }

  @Override
  public void handleLoadGame(LoadGame loadGame) {
    System.out.println(client.printChessBoard(loadGame.getGame()));
    printPrompt();
  }
}
