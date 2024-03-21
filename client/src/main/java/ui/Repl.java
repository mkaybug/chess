package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
  private final ChessClient client;

  public Repl(String serverUrl) {
    client = new ChessClient(serverUrl);
  }

  public void run() {
    System.out.println("♕ Welcome to 240 Chess! Type help to get started. >>>");

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
    System.out.println();
  }

  private void printPrompt() {
    System.out.print("\n" + "\u001B[0m" + "[" + client.printState() + "] >>> " + SET_TEXT_COLOR_GREEN);
  }
}
