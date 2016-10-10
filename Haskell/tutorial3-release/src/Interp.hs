module Interp where

import Parser
import Declare

type Binding = (String, Int)
type Env = [Binding]

-- var a = 3; var b = 8; var a = b, b = a; a + b
e5 :: Exp
e5 = Decl "a" (Num 3) (Decl "b" (Num 8) (Add (Var "a") (Var "b")))

-- var a = 3; var b = 8; var a = b; var b = a; a + b
e6 :: Exp
e6 = Decl "a" (Num 3) (Decl "b" (Num 8) (Decl "a" (Var "b") (Decl "b" (Var "a") (Add (Var "a") (Var "b")))))


-- | Evaluation function
--
-- Examples:
--
-- >>> evaluate e5
-- 11
--
-- >>> evaluate e6
-- 16
evaluate :: Exp -> Int
evaluate e = eval e [] -- starts with an empty environment
  where
    eval :: Exp -> Env -> Int
    eval = error "TODO: Question 4 and 6"


calc :: String -> Int
calc  = evaluate . parseExpr



{-

test cases for multiple declare

-- var a = 3; var b = 8; var a = b; var b = a; a + b
e7 :: Exp
e7 = Decl "a" (Num 3) (Decl "b" (Num 8) (Decl "a" (Var "b") (Decl "b" (Var "a") (Add (Var "a") (Var "b")))))


-- var x = 3, x = x + 2; x * 2   duplicated declaration, signal an error
e8 :: Exp
e8 = DeclareMulti [("x", (Num 3)), ("x", (Add (Var "x") (Num 2)))] (Mult (Var "x") (Num 2))


-- var a = 2, b = 7; var m = 5 * a, n = b - 1; a * n + b / m + a   evaluate to 14
e9 :: Exp
e9 = DeclareMulti [("a", (Num 2)), ("b", (Num 7))] (Add (DeclareMulti [("m", (Mult (Num 5) (Var "a"))), ("n", (Sub (Var "b") (Num 1)))] (Add (Mult (Var "a") (Var "n")) (Div (Var "b") (Var "m")))) (Var "a"))


-- var a = 3; var b = 8; var a = b, b = a; a + b
e10 :: Exp
e10 = Decl "a" (Num 3) (Decl "b" (Num 8) (DeclareMulti [("a",(Var "b")), ("b",(Var "a"))] (Add (Var "a") (Var "b"))))

-}
