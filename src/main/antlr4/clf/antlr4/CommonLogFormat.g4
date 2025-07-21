grammar CommonLogFormat;

// Parser rules
log : entry+ ;

entry : ip SP ident SP user SP dateTime SP request SP status SP bytes (SP responseTime SP referer SP userAgent SP prio)? EOL ;

ip : IPV4 ;

ident : WORD ;

user : WORD ;

dateTime : '[' DATE ':' TIME SP TZ ']' ;

request : '"' method SP url SP protocol '"' ;

method : WORD ;

url : ('/' (WORD | DIGITS | SIGNS)*)+ ;

status : DIGITS ;

protocol : 'HTTP/' DIGITS '.' DIGITS;

bytes : DIGITS | DASH ;

responseTime : DIGITS ('.' DIGITS)? ;

referer : '"' ( ~ '"' )* '"' ;

userAgent : '"' ( ~ '"' )* '"' ;

prio : 'prio:' DIGITS ;

// Lexer rules
IPV4 : DIGITS '.' DIGITS '.' DIGITS '.' DIGITS ;

DATE : DIGITS '/' (MONTH | DIGITS) '/' DIGITS ;

TIME : DIGITS ':' DIGITS ':' DIGITS ;

TZ : [+-] DIGITS ;

WORD : START_LETTER REST_LETTERS* ;

SIGNS : SIGN+ ;

DIGITS : DIGIT+ ;

DASH : '-' ;

SP : ' ' ;

EOL : '\r'? '\n' ;

// Fragments
fragment START_LETTER : [a-zA-Z_-] ;
fragment REST_LETTERS : [a-zA-Z0-9._-] ;
fragment SIGN : [/%?=&@!#] ;
fragment DIGIT : [0-9] ;
fragment MONTH : 'Jan' | 'Feb' | 'Mar' | 'Apr' | 'May' | 'Jun' | 'Jul' | 'Aug' | 'Sep' | 'Oct' | 'Nov' | 'Dec' ;