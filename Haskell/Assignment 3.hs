import Language.Haskell.Liquid.ProofCombinators

import Prelude hiding (length, (++), reverse, foldr, filter, Maybe(..), return, (>>=))

----------------------------------------
-- Define List
----------------------------------------

{-@ LIQUID "--exact-data-cons" @-}
{-@ LIQUID "--higherorder" @-}

data List a = Nil | Cons a (List a)

{-@ measure length               @-}
{-@ length      :: List a -> Nat @-}
length :: List a -> Int
length Nil        = 0
length (Cons x xs) = 1 + length xs

{-@ data List [length] a = Nil | Cons {hd :: a, tl :: List a} @-}

{-@ infix   ++ @-}
{-@ ++      :: List a -> List a -> List a @-}
{-@ reflect ++ @-}
(++) :: List a -> List a -> List a
(++) Nil ys        = ys
(++) (Cons x xs) ys = Cons x (xs ++ ys)

{-@ reverse :: List a -> List a @-}
{-@ reflect reverse @-}
reverse :: List a -> List a
reverse Nil        = Nil
reverse (Cons x xs) = reverse xs ++ (Cons x Nil)

{-@ reflect foldr @-}
foldr :: (a -> b -> b) -> b -> List a -> b
foldr _ v Nil = v
foldr f v (Cons x xs) = f x (foldr f v xs)

----------------------------------------
-- Example
----------------------------------------

{-@ lengthConcat :: xs:List a -> ys:List a -> {length (xs ++ ys) == length xs + length ys} @-}
lengthConcat :: List a -> List a -> Proof
{- Induction on xs -}
lengthConcat Nil ys
  {- Base case: xs = [] -}
  = length (Nil ++ ys)
  {- Definition of ++ -}
  ==. length ys
  {- plus left identity -}
  ==. 0 + length ys
  {- Definition of length -}
  ==. length Nil + length ys
  {- xs = [] -}
  *** QED
lengthConcat (Cons x xs) ys
  {- Inductive case: xs = x:xs -}
  = length (Cons x xs ++ ys)
  {- Definition of ++ -}
  ==. length (Cons x (xs ++ ys))
  {- Definition of length -}
  ==. 1 + length (xs ++ ys)
  {- Induction Hypothesis -}
  ==. 1 + length xs + length ys                       ? lengthConcat xs ys
  {- Definition of length -}
  ==. length (Cons x xs) + length ys
  {- xs = x : xs -}
  *** QED

----------------------------------------
-- Question 1
----------------------------------------
{-@ lengthReverse :: xs:List a -> {length (reverse xs) == length xs} @-}
lengthReverse :: List a -> Proof
{-Induction on l-}
lengthReverse Nil
  {-Base case l = []-}
  = length (reverse Nil)
  {-Definition of reverse-}
  ==. length (Nil)
  {-Definition of length-}
  ==. 0
  {- Definition of length -}
  ==. length Nil 
  {- l = []-}
  *** QED
lengthReverse (Cons x xs)
  {- Inductive case: l = x:xs -}
  = length (reverse (Cons x xs))
  {- Definition of reverse -}
  ==. length (reverse xs ++ (Cons x Nil))
  {- Definition of lengthContact -}
  ==. length (reverse xs) + length (Cons x Nil) ?  lengthConcat (reverse xs) (Cons x Nil)
  {- Definition of length -}
  ==. length (reverse xs) + 1 + length Nil
  {- Definition of length -}
  ==. length (reverse xs) + 1 + 0
  {- plus identity -}
  ==. length (reverse xs) + 1
  {- Induction Hypothesis -}
  ==. 1 + length xs   ? lengthReverse xs
  {- Definition of length -}
  ==. length (Cons x xs)
  {- l = x : xs -}
  *** QED


----------------------------------------
-- Question 2
-- you may or may not need to prove following utility lemmas
----------------------------------------
{-@ concatNil :: xs:List a -> {xs == xs ++ Nil}@-}
concatNil :: List a -> Proof
{-Induction on xs -}
concatNil Nil
  {- Base case: xs = [] -} 
  = Nil ++ Nil
  {- Definition of ++ -}
  ==. Nil
  {- xs = [] -}
  *** QED
concatNil (Cons x xs)
  {- Inductive case: xs = x:xs -}
  = (Cons x xs) ++ Nil
  {- Definition of ++ -}
  ==. Cons x (xs ++ Nil)
  {-Induction Hypothesis-}
  ==. Cons x (xs) ? concatNil xs
  {- Definition of List-}
  ==. (Cons x xs)
  {- xs = x : xs -}
  *** QED

{-@ concatAssoc :: xs:List a -> ys:List a -> zs:List a -> {xs ++ ys ++ zs == xs ++ (ys ++ zs)} @-}
concatAssoc :: List a -> List a -> List a -> Proof
{-Induction on xs-}
concatAssoc Nil ys zs
  {- Base case: xs = [] -} 
  = (Nil ++ ys) ++ zs
  {- Definition of ++ -}
  ==. ys ++ zs
  {- Definition of ++ -}
  ==. Nil ++ (ys ++ zs)
  {- xs = [] -}
  *** QED
concatAssoc (Cons x xs) ys zs
  {- Inductive case: xs = x:xs -}
  = (Cons x xs) ++ ys ++ zs
  {- Definition of ++ -}
  ==. Cons x (xs ++ ys) ++ zs
  {- Definition of ++ -}
  ==. Cons x ((xs ++ ys) ++ zs)
  {-Induction Hypothesis-}
  ==. Cons x (xs ++ (ys ++ zs)) ? concatAssoc xs ys zs
  {- Definition of List-}
  ==. (Cons x xs) ++ (ys ++ zs)
  {- xs  = x : xs -}
  *** QED

{-@ reverseConcat :: xs:List a -> ys:List a -> {reverse (xs ++ ys) == reverse ys ++ reverse xs} @-}
reverseConcat :: List a -> List a -> Proof
{-Induction on xs -}
reverseConcat Nil ys
  {- Base case: xs = [] -} 
  = reverse (Nil ++ ys)
  {- Definition of ++ -}
  ==. reverse ys
  {- definition of concatNil -}
  ==. reverse ys ++ Nil ? concatNil (reverse ys)
  {- definition of reverse -}
  ==. reverse ys ++ reverse Nil
  {- xs = [] -}
  *** QED
reverseConcat (Cons x xs) ys
  {- Inductive case where xs = x:xs -}
  = reverse ((Cons x xs) ++ ys)
  {- definition of ++ -}
  ==. reverse (Cons x (xs ++ ys))
  {- definition of reverse -}
  ==. reverse (xs ++ ys) ++ (Cons x Nil)
  {- Induction Hypothesis -}
  ==. reverse ys ++ reverse xs ++ (Cons x Nil) ? reverseConcat xs ys 
  {- definiiton of contactAssoc -}
  ==. reverse ys ++ ((reverse xs) ++ (Cons x Nil)) ? concatAssoc (reverse ys) (reverse xs) (Cons x Nil)
  {- definition of reverse -}
  ==. reverse ys ++ reverse (Cons x xs)
  {- xs = x : xs -}
  *** QED
  
----------------------------------------
-- Question 3
----------------------------------------

{-@ reflect v @-}
v = v

{-@ reflect w @-}
w = w

{-@ reflect h @-}
h :: a -> a
h x = h x

{-@ reflect g @-}
g :: b -> a -> a
g b a = g b a

{-@ reflect f @-}
f :: b -> a -> a
f b a = f b a

{-@ assume p1    :: {h w == v } @-}
p1 :: Proof
p1 = p1

{-@ assume p2    :: x:b -> y:a -> {f x (h y) == h (g x y) } @-}
p2 :: b -> a -> Proof
p2 x y = p2 x y

{-@ fusion    :: xs:List a -> {h (foldr g w xs) == foldr f v xs} @-}
fusion :: List a -> Proof
fusion Nil
  {- Base case xs = [] -}
  = h (foldr g w Nil)
  {- Definition of foldr -}
  ==. h w
  {- definitio of p1 -}
  ==. v
  {- Definition of foldr -}
  ==. foldr f v Nil
  {- xs = [] -}
  *** QED
fusion (Cons x xs)
  {- Inductive case: x = x:xs -}
  = h (foldr g w (Cons x xs))
  {- Definition of foldr -}
  ==. h (g x (foldr g w xs ))
  {- Definiton of p2 -}
  ==. f x (h (foldr g w xs))
  {- Inductive Hypothesis -}
  ==. f x (foldr f v xs) ? fusion xs
  {- Definition of foldr -}
  ==. foldr f v (Cons x xs)
  {- xs = x:xs -}
  *** QED
----------------------------------------
-- Question 4
-- you may or may not need to use beta_application,
-- which essentially states that f x == (\a -> f a) x
----------------------------------------

{-@ beta_application :: bd:b -> f:(a -> {bd':b | bd' == bd}) -> x:a -> {f x == bd } @-}
beta_application :: b -> (a -> b) -> a -> Proof
beta_application bd f x
  = f x ==. bd *** QED

data Maybe a = Nothing | Just a

{-@ reflect return  @-}
return :: a -> Maybe a
return a = Just a

{-@ infix   >>= @-}
{-@ reflect >>= @-}
(>>=) :: Maybe a -> (a -> Maybe b) -> Maybe b
(>>=) Nothing _ = Nothing
(>>=) (Just a) f = f a

{-@ left_identity :: v:a -> f:(a -> Maybe b) -> {(return v) >>= f == f v} @-}
left_identity :: a -> (a -> Maybe b) -> Proof
left_identity a f
  =  (>>=) (return a) f
  {- definition of return -}
  ==. (>>=) (Just a) f 
  {- definition of >>= -}
  ==. f a
  *** QED
  
{-@ right_identity :: m:(Maybe a) -> {m >>= return == m} @-}
right_identity :: (Maybe a) -> Proof
{-induction on m-}
right_identity Nothing 
  {- Base case: m = Nothing -} 
  = (>>=) Nothing  return
  {- definition of >>= -}
  ==. Nothing
  {- m = Nothing -}
  *** QED
right_identity (Just a)
  {- Inductive case m is Just a -}
  = Just a >>= return
  {- Definition of >>= -}
  ==. return a
  {- definition of return -}
  ==. Just a
  {- m = Just a-}
  *** QED

{-@ associativity :: m:(Maybe a) -> k:(a -> Maybe b) -> h:(b -> Maybe c) -> {m >>= (\x:a -> k x >>= h) == (m >>= k) >>= h} @-}
associativity :: (Maybe a) -> (a -> Maybe b) -> (b -> Maybe c) -> Proof
{- induction on m -}
associativity Nothing k h 
  {- Base case m = Nothing -}
  = Nothing >>= (\x -> k x >>= h)
  {- definition of >>= -}
  ==. Nothing 
  {- definition of >>= -}
  ==. Nothing >>= h 
  {- definition of >>= -}
  ==. (Nothing >>= k) >>= h 
  {- m = Nothing -}
  *** QED
associativity (Just a) k h 
  {- Inductive case m = Just a -}
  = Just a >>= (\x -> k x >>= h)
  {- Definition of >>= -}
  ==. (\x -> k x >>= h) a
  {- Definiiton of beta_application -}
  ==. k a >>= h ? beta_application (k a >>= h) (\x -> k x >>= h) a 
  {- Definition of >>= -}
  ==. (Just a >>= k) >>= h
  {- m = Just a -}
  *** QED  