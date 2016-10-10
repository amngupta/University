{
module Parser (parseExpr) where
import Data.Char (isDigit, isSpace, isAlpha)
import Data.List (stripPrefix)
import Declare (Exp(..))
import Tokens
}


%name parser
%tokentype { Token }
%error { parseError }

%token
    var     { TokenVar }
    id      { TokenSym $$ }
    int     { TokenInt $$ }
    '+'     { TokenPlus }
    '-'     { TokenMinus }
    '*'     { TokenTimes }
    '/'     { TokenDiv }
    '('     { TokenLParen }
    ')'     { TokenRParen }
    ';'     { TokenSemiColon }
    '='     { TokenEq }

%right ';'
%left '+' '-'
%left '*' '/'

%%

Exp : var id '=' Exp ';' Exp { Decl $2 $4 $6 }
    | Exp '+' Exp            { Add $1 $3 }
    | Exp '-' Exp            { Sub $1 $3 }
    | Exp '*' Exp            { Mult $1 $3 }
    | Exp '/' Exp            { Div $1 $3 }
    | int                    { Num $1 }
    | id                     { Var $1 }
    | '(' Exp ')'            { $2 }


{

parseError :: [Token] -> a
parseError _ = error "Parse error"

parseExpr = parser . scanTokens

}
