package language.trial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Trial {
  static boolean hasError = false;

  public static void main(String[] args) throws IOException {
    if (args.length > 1) {
      System.out.println("Error");
      System.exit(64);
    } else if (args.length == 1) {
      runFile(args[0]);
    } else {
      runPrompt();
    }
  }

  private static void runFile(String path) throws IOException {
    byte[] bytes = Files.readAllBytes(Paths.get(path));
    run(new String(bytes, Charset.defaultCharset()));
    if (hasError)
      System.exit(65);
  }

  private static void runPrompt() throws IOException {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);

    for (;;) {
      System.out.print("> ");
      run(reader.readLine());
      hasError = false;
    }
  }

  private static void run(String source) {
    Scanner scanner = new Scanner(source);
    List<Token> tokens = scanner.scan();

    // For now, just print the tokens.
    for (Token token : tokens) {
      System.out.println("current token " + token);
    }
  }

  static void error(int line, String message) {
    errorReport(line, "", message);
  }

  private static void errorReport(int line, String where, String message) {
    System.err.println("[line " + line + "] Error" + where + ": " + message);
    hasError = true;
  }
}