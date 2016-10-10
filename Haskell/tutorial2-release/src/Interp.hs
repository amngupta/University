module Interp where

import Parser
import Declare
import Prelude hiding (Either(..))


-- | Evaluation function
--
-- Examples:
--
-- >>> evaluate (Sqrt (Num 4.0))
-- 2.0
--
-- >>> evaluate (Log (Num (exp 6.4)))
-- 6.4
evaluate :: Exp -> Double 
evaluate (Num n) = n
evaluate (Add a b) = evaluate a + evaluate b
evaluate (Sub a b) = evaluate a - evaluate b
evaluate (Mult a b) = evaluate a * evaluate b
evaluate (Div a b) = evaluate a / evaluate b
evaluate (Sqrt a) = sqrt (evaluate a)
evaluate (Log a) = log (evaluate a)
-- TODO: Question 2


-- | Simple calculator
--
-- Examples:
--
-- >>> calc "1.0 + 8.0 * 2.0"
-- 17.0 
--
-- >>> calc "2.0 * (8.0 + sqrt 16.0) + log 1.0"
-- 24.0
calc :: String -> Double
calc a = evaluate ( parseExpr a )


-- Error Handling

data Either a b = Left a | Right b deriving Show

safeHead :: [a] -> Either String a
safeHead []     = Left "can't access the head of an empty list"
safeHead (x:_) = Right x


-- | Evaluation function, revisited
--
-- Examples:
--
-- >>> evaluate2 (Add (Sub (Num 3.2) (Num 2.5)) (Mult (Num 2.7) (Num 3.9)))
-- Right 11.23
--
-- >>> evaluate2 (Div (Num 2.0) (Num 0.0))
-- Left "Division by zero"
--
-- >>> evaluate2 (Log (Sub (Num 2) (Num 3)))
-- Left "Logarithm of negative number"

evaluate2 :: Exp -> Either String Double 
evaluate2 (Num n) = Right n
evaluate2 (Add a b) =
  case evaluate2 a of
    Left msg -> Left msg
    Right aR ->
      case evaluate2 b of
        Left msg -> Left msg
        Right bR -> Right (aR + bR)

evaluate2 (Div a b) =
	case evaluate2 a of
	    Left msg -> Left msg
	    Right aR ->
	      case evaluate2 b of
	        Left msg -> Left msg
	        Right bR -> 
	        	if bR == 0.0
	        		then Left ("Division by zero")
	        		else Right (aR / bR)

evaluate2 (Sub a b) =
	case evaluate2 a of
	    Left msg -> Left msg
	    Right aR ->
	      case evaluate2 b of
	        Left msg -> Left msg
	        Right bR -> Right (aR - bR)

evaluate2 (Mult a b) =
	case evaluate2 a of
	    Left msg -> Left msg
	    Right aR ->
	      case evaluate2 b of
	        Left msg -> Left msg
	        Right bR -> Right (aR * bR)

evaluate2 (Sqrt a) =
	case evaluate2 a of
	    Left msg -> Left msg
	    Right aR ->
	      if aR < 0.0
	      	then Left ("Square of negative number")
	      	else Right (sqrt aR)

evaluate2 (Log a) =
	case evaluate2 a of
	    Left msg -> Left msg
	    Right aR ->
	      if aR < 0.0
	      	then Left ("Logarithm of negative number")
	      	else Right (log aR)
-- TODO: Question 6


calc2 :: String -> Either String Double 
calc2 a = evaluate2 ( parseExpr a )

-- | Monadic binding operation
--
-- Examples:
-- >>> bindE (Right 3.0) (\n -> Right (n + 1.0))
-- Right 4.0
--
-- >>> bindE (Left "bad things") (\n -> Right (n + 1.0))
-- Left "bad things"
bindE :: Either a b -> (b -> Either a b) -> Either a b
bindE a b =
    case a of
      	Left msg -> Left msg
      	Right aR -> b (aR)

evaluate3 :: Exp -> Either String Double 
evaluate3 (Num n) = Right n

evaluate3 (Add a b) = 
	bindE (evaluate3 a) (\m -> (bindE (evaluate3 b) (\n -> Right (m + n))))

evaluate3 (Sub a b) = 
		bindE (evaluate3 a) (\m -> (bindE (evaluate3 b) (\n -> Right (m - n))))


evaluate3 (Mult a b) = 
		bindE (evaluate3 a) (\m -> (bindE (evaluate3 b) (\n -> Right (m * n))))


evaluate3 (Div a b) = 
	bindE (evaluate3 a) (\m -> (bindE (evaluate3 b) (\n ->
		if (n == 0.0)
			then Left "Division by zero"
			else Right (m / n)
			)
	))

evaluate3 (Sqrt a) = 
	bindE (evaluate3 a) (\m -> 
		if (m < 0.0)
			then Left "Square of negative"
			else Right (sqrt m)
		)

evaluate3 (Log a) = 
	bindE (evaluate3 a) (\m -> 
		if (m < 0.0)
			then Left "Logarithm of negative"
			else Right (log m)
		)


--	case evaluate3 a of 
--		True -> evaluate3
-- 	   	otherwise -> evaluate3

-- | Our glorious calculator
--
-- Examples:
--
-- >>> calc3 "1.0 + 8.0 * 2.0"
-- Right 17.0
--
-- >>> calc3 "2.0 * (8.0 + sqrt 16.0) + log 1.0"
-- Right 24.0
--
-- >>> calc3 "2.0 * (4.0 - 2.0) / (8.0 - 8.0)"
-- Left "Division by zero"
--
-- >>> calc3 "2 * (4 - 2) * sqrt (6 - 8)"
-- Left "Square of negative" 
calc3 :: String -> Either String Double 
calc3  a = evaluate3 (parseExpr a)

