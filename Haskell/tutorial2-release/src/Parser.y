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
    digits  { TokenDouble $$ }
    '+'     { TokenPlus }
    '-'     { TokenMinus }
    '*'     { TokenTimes }
    '/'     { TokenDiv }
    '('     { TokenLParen }
    ')'     { TokenRParen }
    sqrt    { TokenSqrt }
    log     { TokenLog }


%%

AExpr : AExpr '+' Term        { Add $1 $3 }
      | AExpr '-' Term        { Sub $1 $3 }
      | Term                  { $1 }

Term : Term '*' Factor        { Mult $1 $3 }
     | Term '/' Factor        { Div $1 $3 }
     | Factor                 { $1 }

Factor : log Primary          { Log $2 }
       | sqrt Primary         { Sqrt $2 }
       | Primary              { $1 }

Primary : digits              { Num $1 }
        | '(' AExpr ')'       { $2 }

{

parseError _ = error "Parse error"

parseExpr = parser . scanTokens

}
