package language.trial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static language.trial.TokenType.*;

class Scanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
  private int firstChar = 0;
  private int current = 0;
  private int line = 1;
  private static final Map<String, TokenType> keywords;

  static {
    keywords = new HashMap<>();
    keywords.put("and", AND);
    keywords.put("class", CLASS);
    keywords.put("else", ELSE);
    keywords.put("false", FALSE);
    keywords.put("for", FOR);
    keywords.put("if", IF);
    keywords.put("null", NULL);
    keywords.put("or", OR);
    keywords.put("print", PRINT);
    keywords.put("ret", RET);
    keywords.put("super", SUPER);
    keywords.put("this", THIS);
    keywords.put("true", TRUE);
    keywords.put("var", VAR);
    keywords.put("while", WHILE);
    keywords.put("each", EACH);
  }

  Scanner(String source) {
    this.source = source;
  }

  // iterate through all charaxters
  List<Token> scan() {
    while (current < source.length()) {
      firstChar = current;
      scanToken();
    }

    tokens.add(new Token(EOF, "", null, line));
    return tokens;
  }

  // read current token
  private void scanToken() {
    char c = source.charAt(firstChar);
    current++;
    switch (c) {
    case '(':
      addToken(LEFT_PAREN, "(");
      break;
    case ')':
      addToken(RIGHT_PAREN, ")");
      break;
    case '{':
      addToken(LEFT_BRACE, "{");
      break;
    case '}':
      addToken(RIGHT_BRACE, "}");
      break;
    case ',':
      addToken(COMMA, ",");
      break;
    case '.':
      addToken(DOT, ".");
      break;
    case '-':
      addToken(MINUS, "-");
      break;
    case '+':
      addToken(PLUS, "+");
      break;
    case ';':
      addToken(SEMICOLON, ";");
      break;
    case '*':
      addToken(STAR, "*");
      break;
    case '!':
      if (lastCharInToken('=')) {
        addToken(BANG_EQUAL, "!=");
      } else {
        addToken(BANG, "!");
      }
      break;
    case '=':
      if (lastCharInToken('=')) {
        addToken(EQUAL_EQUAL, "==");
      } else {
        addToken(EQUAL, "=");
      }
      break;
    case '<':
      if (lastCharInToken('=')) {
        addToken(LESS_EQUAL, "<=");
      } else {
        addToken(LESS, "<");
      }
      break;
    case '>':
      if (lastCharInToken('=')) {
        addToken(GREATER_EQUAL, ">=");
      } else {
        addToken(GREATER, ">");
      }

      break;
    case '#':
      if (lastCharInToken('/')) {
        while (current < source.length() && peek() != '\n') {
          current++;
        }
      } else {
        addToken(HASHTAG, "#");
      }
      break;
    case ' ':
      break;
    case '\n':
      line++;
      break;
    case '"':
      readString();
      break;
    default:
      if (isNumber(c)) {
        readNumber();
      } else if (isIdentifier(c)) { // check for keyword
        readIdentifier();
      } else {
        Trial.error(line, "Unexpected character.");
      }
      break;
    }
  }

  private void addToken(TokenType type, Object literal) {
    String text = source.substring(firstChar, current);
    tokens.add(new Token(type, text, literal, line));
  }

  // check if the next character is a certain character
  private boolean lastCharInToken(char expected) {
    if (current >= source.length()) {
      return false;
    }
    if (source.charAt(current) != expected) {
      return false;
    }
    current++;
    return true;
  }

  private void readString() {
    while (current < source.length() && peek() != '"') {
      current++;
    }
    if (current >= source.length()) {
      Trial.error(line, "Unterminated string.");
      return;
    }
    current++; // get last quotation mark
    String currToken = source.substring(firstChar, current);
    addToken(STRING, currToken);
  }

  private char peek() {
    if (current >= source.length()) {
      return '\0';
    }
    return source.charAt(current);
  }

  private boolean isNumber(char c) {
    return c >= '0' && c <= '9';
  }

  private void readNumber() {
    while (current < source.length() && isNumber(peek())) {
      current++;
    }
    if (peek() == '.') {
      current++;
      while (current < source.length() && isNumber(peek())) {
        current++;
      }
    }
    String currToken = source.substring(firstChar, current);
    addToken(NUMBER, currToken);
  }

  private boolean isIdentifier(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
  }

  private void readIdentifier() {
    while (current < source.length() && peek() != ' ') {
      current++;
    }
    String currToken = source.substring(firstChar, current);
    TokenType identifierType = keywords.get(currToken);
    if (identifierType == null) {
      identifierType = IDENTIFIER;
    }
    addToken(identifierType, currToken);
    current++;
  }
}