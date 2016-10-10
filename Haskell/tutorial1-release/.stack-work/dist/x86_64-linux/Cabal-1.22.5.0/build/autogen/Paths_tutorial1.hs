module Paths_tutorial1 (
    version,
    getBinDir, getLibDir, getDataDir, getLibexecDir,
    getDataFileName, getSysconfDir
  ) where

import qualified Control.Exception as Exception
import Data.Version (Version(..))
import System.Environment (getEnv)
import Prelude

catchIO :: IO a -> (Exception.IOException -> IO a) -> IO a
catchIO = Exception.catch

version :: Version
version = Version [0,1,0,0] []
bindir, libdir, datadir, libexecdir, sysconfdir :: FilePath

bindir     = "/home/aman/Desktop/Haskell/tutorial1-release/.stack-work/install/x86_64-linux/lts-6.15/7.10.3/bin"
libdir     = "/home/aman/Desktop/Haskell/tutorial1-release/.stack-work/install/x86_64-linux/lts-6.15/7.10.3/lib/x86_64-linux-ghc-7.10.3/tutorial1-0.1.0.0-LBoYk3wLffr056PhhzJCTy"
datadir    = "/home/aman/Desktop/Haskell/tutorial1-release/.stack-work/install/x86_64-linux/lts-6.15/7.10.3/share/x86_64-linux-ghc-7.10.3/tutorial1-0.1.0.0"
libexecdir = "/home/aman/Desktop/Haskell/tutorial1-release/.stack-work/install/x86_64-linux/lts-6.15/7.10.3/libexec"
sysconfdir = "/home/aman/Desktop/Haskell/tutorial1-release/.stack-work/install/x86_64-linux/lts-6.15/7.10.3/etc"

getBinDir, getLibDir, getDataDir, getLibexecDir, getSysconfDir :: IO FilePath
getBinDir = catchIO (getEnv "tutorial1_bindir") (\_ -> return bindir)
getLibDir = catchIO (getEnv "tutorial1_libdir") (\_ -> return libdir)
getDataDir = catchIO (getEnv "tutorial1_datadir") (\_ -> return datadir)
getLibexecDir = catchIO (getEnv "tutorial1_libexecdir") (\_ -> return libexecdir)
getSysconfDir = catchIO (getEnv "tutorial1_sysconfdir") (\_ -> return sysconfdir)

getDataFileName :: FilePath -> IO FilePath
getDataFileName name = do
  dir <- getDataDir
  return (dir ++ "/" ++ name)
