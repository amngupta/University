import Data.Char
import Data.List
import Parsing
import System.IO

data Binop = Add | Sub | Mul | Div | Mod
    deriving (Eq, Show)
data Expr= Bin Binop Expr Expr
    | Val Int
    | Var String
    deriving (Eq, Show)

type Env = [(String, Int)]

-- Question 1
eval :: Env->Expr->Maybe Int
eval x (Val a) =  Just a
eval x (Var a) = lookup a x 
eval x (Bin op a b)  
    | eval x a == Nothing = Nothing 
    | eval x b == Nothing = Nothing 
    | op == Add =  Just (a' + b') 
    | op == Sub = Just (a' - b')  
    | op == Mul = Just (a' * b')
    | op == Div = if b' == 0 then Nothing else Just (a' `div` b') 
    | op == Mod = if b' == 0 then Nothing else Just (a' `mod` b') 
    where 
    Just a' = eval x a
    Just b' = eval x b

-- Question 2
pExpr :: Parser Expr
pExpr = do 
    e <- pTerm 
    f <- pOpTerm e
    return f

pOpTerm :: Expr -> Parser Expr
pOpTerm exp = pAdd +++ pSub +++ return exp
    where 
        pAdd = do
            symbol "+"
            y <- pTerm 
            (do z <- pOpTerm (Bin Add exp y)
                return z) +++ return (Bin Add exp y)
        pSub = do
            symbol "-"
            y <- pTerm 
            (do z <- pOpTerm (Bin Sub exp y)
                return z) +++ return (Bin Sub exp y)

pTerm :: Parser Expr
pTerm = do 
    e <- pFactor 
    m <- pOpFactor e
    return m

pOpFactor :: Expr -> Parser Expr 
pOpFactor exp = pMul +++ pDiv +++ pMod +++ return exp
    where 
        pMul = do
            symbol "*"
            y <- pFactor
            (do z <- pOpFactor (Bin Mul exp y)
                return z) +++ return (Bin Mul exp y) 
        pDiv = do
            symbol "/"
            y <- pFactor 
            (do z <- pOpFactor (Bin Div exp y) 
                return z) +++ return (Bin Div exp y) 
        pMod = do
            symbol "%"
            y <- pFactor
            (do z <- pOpFactor (Bin Mod exp y)
                return z) +++ return (Bin Mod exp y)

pFactor :: Parser Expr
pFactor = pPara  +++ pIdent +++ pInt 
    where 
        pPara = do
            symbol "("
            e <- pExpr
            symbol ")"
            return e             
        pInt = do
            x <- integer
            return (Val x)
        pIdent =  do 
            x <- identifier
            return (Var x)

-- Question 3
runParser :: Parser a -> String -> Maybe a
runParser p s
    | length (parse p s) == 0 = Nothing
    | length y == 0 = Just x 
    | otherwise = Nothing
    where [(x,y)] = parse p s

-- Question 4
data Instr = IVal Int | IBin Binop | IVar String
    deriving (Eq, Show)

type Stack = [Int]
type Prog = [Instr]
type OpStack = [Binop]

runProg :: Prog -> Env -> Maybe Int
runProg [] _ = Nothing
runProg [(IVal a)] _ = Just a
runProg x y = do 
    let stack = getStack [] x y
    let ops = getOpsStack x
    runProgImpl stack ops

runProgImpl :: Stack -> OpStack -> Maybe Int
runProgImpl [] _ = Nothing
runProgImpl [a] [] = Just a 
runProgImpl [a] x = Nothing
runProgImpl x [] = Nothing
runProgImpl x (y:ys) 
    | performOp y x == [] = Nothing
    | otherwise =  runProgImpl (performOp y x) ys 

performOp :: Binop -> Stack -> Stack
performOp b [] = []
performOp b [x] = []
performOp b (x:y:yz) 
    | b == Add = (x + y):yz
    | b == Sub = (x - y):yz
    | b == Mul = (x * y):yz
    | b == Div = if y == 0 then [] else (x `div` y):yz
    | b == Mod = if y == 0 then [] else (x `mod` y):yz

getStack :: Stack -> Prog -> Env -> Stack
getStack s [] _ = s
getStack s (x:xs) y 
    | getVal x y == Nothing = getStack s xs y
    | otherwise =  getStack (a:s) xs y
        where Just a = getVal x y

getVal :: Instr -> Env -> Maybe Int
getVal (IVal a) y = Just a
getVal (IVar x) y = lookup x y 
getVal (IBin a) y =  Nothing

getOpsStack :: Prog -> OpStack
getOpsStack s = reverse [ m | (IBin m) <-s ]

-- Question 5
compile :: Expr -> Prog
compile (Val a) = [IVal a]
compile (Var a) = [IVar a]
compile (Bin op a b) = compile b ++ compile a ++ [IBin op]

-- Question 6
optimize :: Expr -> Maybe Expr
optimize (Val a) = Just (Val a)
optimize (Var a) = Just (Var a)
optimize (Bin Mul _ (Val 0)) = Just (Val 0) 
optimize (Bin Mul (Val 0) _) = Just (Val 0)
optimize (Bin Add x (Val 0)) = Just x
optimize (Bin Add (Val 0) x) = Just x
optimize (Bin Sub x (Val 0)) = Just x
optimize (Bin Div _ (Val 0)) = Nothing
optimize (Bin Mod _ (Val 0)) = Nothing
optimize (Bin op (Val a) (Val b)) 
    | eval [] (Bin op (Val a) (Val b)) == Nothing = Nothing
    | otherwise = Just (Val m) where Just m = (eval [] (Bin op (Val a) (Val b))) 
optimize (Bin op (Var a) x) = Just (Bin op (Var a) m) where Just m = optimize x 
optimize (Bin op x (Var a)) = Just (Bin op m (Var a)) where Just m = optimize x  
optimize (Bin op a b)
    | optimize a == Nothing = Nothing
    | optimize b == Nothing = Nothing
    | otherwise =  optimize (Bin op a' b')
        where 
            Just a' = optimize a
            Just b' = optimize b

-- Question 7 
data Opr = Quit | Let | Env | FindVar | DelVar | Ans
    deriving (Eq, Show)

type LetExpr = (Opr, String, Expr)

main :: IO () 
main = do 
    hSetBuffering stdin  LineBuffering 
    hSetBuffering stdout NoBuffering 
    repl []

repl :: Env -> IO () 
repl env = do 
    putStr "\n> " 
    line <- getLine
    dispatch env line

dispatch :: Env -> String -> IO ()
dispatch env string 
    | pObj == Nothing = do 
        loop "Error \n" env
    | val == Nothing = do 
        loop "Error \n" env
    | opr == Quit = quit
    | opr == Env = do 
        mapM_ putStrLn (sort (printEnv env))
        repl env
    | opr == FindVar = do 
        if lookup s env == Nothing then loop "Error\n" env else 
            loop (s ++ " = " ++ (show num)) env
    | opr == DelVar = do
        if lookup s env == Nothing then loop "Error\n" env else 
            loop ("Deleted " ++ s) (deleteVar env s)
    | opr == Ans = do
            loop ("ans = " ++ show m) env
    | opr == Let = do 
        loop (s ++ " = " ++ (show m)) (addVar env (s,m))
    | otherwise =  do 
        loop "Error\n" env
    where
        pObj =  (runParser parseStat string)
        Just (opr, s, y) = pObj        
        (str, val) = (s, (eval env y))  
        Just m = val 
        Just num = lookup s env    

parseStat :: Parser LetExpr
parseStat = parseQuitExpr +++ parseEnvExpr +++ parseVarExpr +++ parseDelExpr +++ parseLetExpr +++ parseAnsExpr

parseQuitExpr :: Parser LetExpr
parseQuitExpr = do 
    string "quit"
    return (Quit, "", (Val 0))

parseDelExpr :: Parser LetExpr
parseDelExpr = do
    string "del"
    x <- identifier
    return (DelVar, x, (Val 0))
    
parseEnvExpr :: Parser LetExpr
parseEnvExpr = do 
    string "env"
    return (Env, "", (Val 0))

parseLetExpr :: Parser LetExpr
parseLetExpr = do 
    x <- parseVar
    y <- pExpr
    return (Let, x, y)

parseVarExpr :: Parser LetExpr
parseVarExpr = do
    string "var"
    x <- identifier
    return (FindVar, x, (Val 0))

parseAnsExpr :: Parser LetExpr
parseAnsExpr = do 
    x <- pExpr
    return(Ans, "", x)

parseVar :: Parser String 
parseVar = do
    string "let"
    x <- identifier
    char '='
    return x 

printEnv :: Env -> [String]
printEnv [] = []
printEnv ((a, b):xs) = (a ++ " = " ++ (show b)): printEnv xs

deleteVar :: Env -> String -> Env
deleteVar [] _ = []
deleteVar ((x,y):xs) s
    | x == s = deleteVar xs s
    | otherwise = (x,y): (deleteVar xs s)

addVar :: Env -> (String, Int) -> Env
addVar env (x,num) = if (lookup x env) == Nothing then (x,num) : env 
    else (x,num) : (deleteVar env x)

quit :: IO () 
quit = return ()

loop :: String -> Env -> IO () 
loop str next = do 
    putStrLn str 
    repl next 