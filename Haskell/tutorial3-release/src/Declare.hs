module Declare where

data Exp = Num Int
         | Add Exp Exp
         | Sub Exp Exp
         | Mult Exp Exp
         | Div Exp Exp
         | Var String            -- new
         | Decl String Exp Exp   -- new
         -- TODO: Question 5


e1 :: Exp
e1 = Decl "x" (Num 3) (Mult (Var "x") (Num 3))

e2 :: Exp
e2 = Decl "x" (Add (Num 3) (Num 4)) (Var "x")

e3 :: Exp
e3 = Add (Var "x") (Mult (Num 4) (Num 5))

e4 :: Exp
e4 = Decl "y" e3 (Div (Var "x") (Var "y"))


instance Show Exp where
  show = showExpr


-- | Pretty printer
--
-- Examples:
--
-- >>> e1
-- var x = 3; (x * 3)
--
-- >>> e2
-- var x = (3 + 4); x
--
-- >>> e3
-- (x + (4 * 5))
--
-- >>> e4
-- var y = (x + (4 * 5)); (x / y)
showExpr :: Exp -> String
showExpr (Num a) = show a
showExpr (Add a b) = "(" ++ showExpr a ++ " + "++ showExpr b ++ ")"
showExpr (Sub a b) = "(" ++ showExpr a ++ " - "++ showExpr b ++ ")"
showExpr (Mult a b) = "(" ++ showExpr a ++ " * "++ showExpr b ++ ")"
showExpr (Div a b) = "(" ++ showExpr a ++ " / "++ showExpr b ++ ")"
showExpr (Decl x expA expB) = "var " ++ x ++ " = " ++ showExpr expA ++ "; " ++showExpr expB
showExpr (Var x) = x


-- | Renaming function
--
-- Examples:
--
-- >>> rename "x" "i" e3
-- (i + (4 * 5))
--
-- >>> rename "x" "i" e1
-- var x = 3; (x * 3)
--
-- >>> rename "x" "i" e4
-- var y = (i + (4 * 5)); (i / y)
rename :: String -> String -> Exp -> Exp
rename x y exp = exp
