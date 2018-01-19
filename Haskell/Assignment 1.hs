import Prelude hiding (words)
import Data.List hiding (words)

isDigit :: Char -> Bool
isDigit s = any (\x-> (show x) == [s]) [0..9]

removeSpaces ::  String -> String 
removeSpaces x = filter (/=' ') x

isPrefix :: Eq a => [a] -> [a] -> Bool
isPrefix [] _ = True
isPrefix _ [] = False
isPrefix (x:xs) (y:ys) = x == y && isPrefix xs ys

-- Problem 1 - Completed
combinations :: Int -> Int -> Int
combinations 0 _ = 1
combinations k n = (combinations k (n - 1)) + (combinations (k-1) (n-1))


-- -- Problem 2 - Completed
parser = [(1,'A'), (2,'B'), (3,'C'), (4,'D'), (5,'E'), (6,'F'), (7,'G'), (8,'H'), (9,'I'), (10,'J'), (11,'K'), (12,'L'), (13,'M'), (14,'N'), (15,'O'), (16,'P'), (17,'Q'), (18,'R'),(19,'S'), (20,'T'), (21,'U'), (22,'V'), (23,'W'), (24,'X'),(25, 'Y'), (26, 'Z')]

intCon :: Int -> [Int]
intCon 0 = []
intCon 26 = [26]
intCon n = intCon(div n 26) ++ if rem n 26 == 0 then [26] else [rem n 26]

intToColumn :: Int -> String
intToColumn 0 = []
intToColumn n = [a | (x, a) <- parser, d<-intCon n, x == d]

-- Problem 3 - Completed
escapeRe :: String -> String
escapeRe [] = []
escapeRe (x:xs)
    | isDigit x = x : escapeRe xs
    | otherwise = escapeRe xs

lookupPhoneNumber :: [String] -> String -> [String]
lookupPhoneNumber x p = [escapeRe a | a <- x, isPrefix p (escapeRe a)]

-- Problem 4 - Completed
-- Part A - Completed: Used online resources to understand the foldr behaviour
-- https://wiki.haskell.org/Foldr_Foldl_Foldl%27
myFilter :: (Int-> Bool) -> [Int] -> [Int]
myFilter f x = foldr (\x xs -> if f x then x : xs else xs) [] x

-- Part B - Completed
takeFromUntil :: (Int -> Bool) -> (Int -> Bool) -> [Int] -> [Int]
takeFromUntil f h x = takeWhile (not . h ) (dropWhile (not . f) x)

-- Problem 5 -- Completed
hasAny :: String -> Char -> Bool
hasAny [] cha = False
hasAny (x:xs) cha = cha == x || hasAny xs cha

wordImpl :: String -> String -> String -> String -> Bool
wordImpl _ _ [] _ = True
wordImpl v s (x:xs) "stop" = hasAny s x && wordImpl v s xs "vowel"
wordImpl v s (x:xs) "vowel" = hasAny v x && wordImpl v s xs "stop"

words :: String -> String -> [String] -> [String]
words v s dict = [a | a <-dict, wordImpl v s a "stop"]

-- Problem 6 - Completed
wave :: [Int] -> [Int]
wave [] = []
wave [x] = [x]
wave x = last y : head y : wave (tail (init y)) where
        y = sort x

-- Problem 7
-- Since the problem statement notes that all integers are positive
-- I am considering negative solutions to subproblems i.e. "1-4" in "1-4+5" as invalid 
performFunction :: Int -> String -> Int -> Int
performFunction a sign b 
    | sign == "+" = a + b
    | sign == "-" = a - b
    | sign == "*" = a * b
    | sign == "/" = if b == 0 then -1 else div a b
    | otherwise = -1

readNumber :: String -> Int
readNumber [] = (-1)
readNumber s = if isPrefix "-" s then (-1) else read (takeWhile isDigit s) :: Int

calculator :: String -> Int 
calculator [] = 0
calculator x 
    | length (takeWhile isDigit x) == length x = readNumber x
    | a == (-1) = -1 
    | b == (-1) = -1
    | otherwise = calculator (show (performFunction a sign b) ++ trailingString) where
        i = removeSpaces x
        a = readNumber i
        sign = take 1 (drop (length (show a)) i)  
        b = readNumber (drop (length (show a) + 1) i)
        trailingString = drop (length (show a) +  length (show b) + 1) i