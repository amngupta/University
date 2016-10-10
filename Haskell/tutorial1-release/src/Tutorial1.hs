module Tutorial1 where

import Data.Char (ord)
import Prelude hiding (Maybe(..))



absolute :: Int -> Int
absolute x = if x < 0 then -x else x

nested_if1 x = 	if ( (absolute x) <= 10)
	then x
	else error "Only numbers between [-10,10] allowed"


nested_if2 x = 	if ( (if x < 0 then -x else x) <= 10)
	then x
	else error "Only numbers between [-10,10] allowed"

{-

Write your answers of Q1 to Q4 below
	Q1: 
	Q2:
	Q3: JavaScript or ES6 let us use var / let to define a variable for multiple types

-}

-- | The tail of a list
--
-- Examples:
--
-- >>> tl [1,2,3]
-- [2,3]
--
-- >>> tl [1]
-- []
tl :: [a] -> [a]
tl [] = error "TODO: question 4"
tl (xs:x) = x

-- | Factorial function
--
-- Examples:
--
-- >>> factorial 3
-- 6
--
-- >>> factorial 0
-- 1
factorial :: Int -> Int
factorial x = if ( x == 0 )
	then 1
	else factorial (x-1) * x
--factorial = error "TODO: question 5" 



-- | Compute Fibonacci numbers
--
-- Examples:
--
-- >>> fibonacci 10
-- 55
--
-- >>> fibonacci 5
-- 5
fibonacci :: Int -> Int
fibonacci x = if (x == 0)
	then 0
	else if (x == 1)
		then 1
		else fibonacci (x-1) + fibonacci(x-2)
--fibonacci = error "TODO: question 6"


-- |
-- >>> mapList absolute [4,-5,9,-7]
-- [4,5,9,7]
mapList :: (a -> b) -> [a] -> [b]
mapList = error "TODO: question 7"



-- |
-- >>> ascii "abcds"
-- [97,98,99,100,115]
ascii :: [Char] -> [Int]
ascii = error "TODO: question 8"



-- |
-- >>> filterList even [1,2,3,4,5]
-- [2,4]
filterList :: (a -> Bool) -> [a] -> [a]
filterList = error "TODO: question 9"



-- |
-- >>> zipList [1,2,3] ['a', 'b', 'c']
-- [(1,'a'),(2,'b'),(3,'c')]
zipList :: [a] -> [b] -> [(a,b)]
zipList = error "TODO: question 10"



-- |
-- >>> zipSum [1,2,3] [4,5,6]
-- [5,7,9]
zipSum :: [Int] -> [Int] -> [Int]
zipSum = error "TODO: question 11"



data Maybe a = Nothing | Just a deriving Show

-- |
-- >>> safeHead []
-- Nothing
--
-- >>> safeHead [1,2,3]
-- Just 1
safeHead :: [a] -> Maybe a
safeHead = error "TODO: question 12"


-- |
-- >>> catMaybes [Just 1, Nothing, Just 2]
-- [1,2]
catMaybes :: [Maybe a] -> [a]
catMaybes = error "TODO: question 13"
