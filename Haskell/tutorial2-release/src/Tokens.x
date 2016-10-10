{
module Tokens where
}

%wrapper "basic"

$digit = 0-9
$alpha = [a-zA-Z]

tokens :-

  $white+                       ;
  "--".*                        ;
  $digit+(\.$digit*)?           { \s -> TokenDouble (read s) }
  \+                            { \s -> TokenPlus }
  \-                            { \s -> TokenMinus }
  \*                            { \s -> TokenTimes }
  \/                            { \s -> TokenDiv }
  \(                            { \s -> TokenLParen }
  \)                            { \s -> TokenRParen }
  sqrt                          { \s -> TokenSqrt }
  log                           { \s -> TokenLog }
  $alpha [$alpha $digit \_ \']* { \s -> TokenSym s }



{
-- The token type:
data Token = TokenDouble Double
           | TokenSym String
           | TokenPlus
           | TokenMinus
           | TokenTimes
           | TokenDiv
           | TokenLParen
           | TokenRParen
           | TokenLog
           | TokenSqrt
           deriving (Eq,Show)

scanTokens = alexScanTokens
}
