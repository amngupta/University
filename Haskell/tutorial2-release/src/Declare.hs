module Declare where


data Exp = Num Double 
         | Add Exp Exp
         | Sub Exp Exp
         | Mult Exp Exp
         | Div Exp Exp
         | Sqrt Exp
         | Log Exp
         --deriving Show

-- TODO: Please write your answer to Question 1 below
-- {- Question 1: - Option 1}

instance Show Exp where
	show = showExpr

e1 :: Exp
e1 = Add (Num 3.1) (Num 4.5)

e2 :: Exp
e2 = Sub (Mult (Add (Num 8.8) (Num 2.0)) (Num 9.2)) (Num 3.4)

e3 :: Exp
e3 = Sub (Div (Add (Num 1.7) (Num 2.6)) (Num 3.5)) 
         (Mult (Sub (Num 5.3) (Num 6.2)) (Num 7.1))

e4 :: Exp
e4 = Sqrt (Log(Add (Num 4.3) (Num 2.3)))

-- | Printer printer
--
-- Examples:
--
-- >>> e1
-- (3.1 + 4.5)
--
-- >>> e2
-- (((8.8 + 2.0) * 9.2) - 3.4)
--
-- >>> e3
--(((1.7 + 2.6) / 3.5) - ((5.3 - 6.2) * 7.1))
showExpr :: Exp -> String
showExpr (Num a) = show a
showExpr (Add a b) = "(" ++ showExpr a ++ " + "++ showExpr b ++ ")"
showExpr (Sub a b) = "(" ++ showExpr a ++ " - "++ showExpr b ++ ")"
showExpr (Mult a b) = "(" ++ showExpr a ++ " * "++ showExpr b ++ ")"
showExpr (Div a b) = "(" ++ showExpr a ++ " / "++ showExpr b ++ ")"
showExpr (Sqrt a) = "sqrt(" ++ showExpr a ++ ")"
showExpr (Log a) = "log(" ++ showExpr a ++ ")"