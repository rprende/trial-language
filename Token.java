package language.trial;

class Token {                                                     
    final TokenType type;                                           
    final String word;                                            
    final Object literal;                                           
    final int line;
  
    Token(TokenType type, String word, Object literal, int line) {
      this.type = type;                                             
      this.word = word;                                         
      this.literal = literal;                                       
      this.line = line;                                             
    }                                                               
  
    public String toString() {                                      
      return type + " " + word + " " + literal;                   
    }                                                               
  }                                