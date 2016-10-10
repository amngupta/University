module Paths_tutorial2 (
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

bindir     = "/home/aman/Desktop/Haskell/tutorial2-release/.stack-work/install/x86_64-linux/lts-6.16/7.10.3/bin"
libdir     = "/home/aman/Desktop/Haskell/tutorial2-release/.stack-work/install/x86_64-linux/lts-6.16/7.10.3/lib/x86_64-linux-ghc-7.10.3/tutorial2-0.1.0.0-I0JxLUPgaQCBFIX5GfZik4"
datadir    = "/home/aman/Desktop/Haskell/tutorial2-release/.stack-work/install/x86_64-linux/lts-6.16/7.10.3/share/x86_64-linux-ghc-7.10.3/tutorial2-0.1.0.0"
libexecdir = "/home/aman/Desktop/Haskell/tutorial2-release/.stack-work/install/x86_64-linux/lts-6.16/7.10.3/libexec"
sysconfdir = "/home/aman/Desktop/Haskell/tutorial2-release/.stack-work/install/x86_64-linux/lts-6.16/7.10.3/etc"

getBinDir, getLibDir, getDataDir, getLibexecDir, getSysconfDir :: IO FilePath
getBinDir = catchIO (getEnv "tutorial2_bindir") (\_ -> return bindir)
getLibDir = catchIO (getEnv "tutorial2_libdir") (\_ -> return libdir)
getDataDir = catchIO (getEnv "tutorial2_datadir") (\_ -> return datadir)
getLibexecDir = catchIO (getEnv "tutorial2_libexecdir") (\_ -> return libexecdir)
getSysconfDir = catchIO (getEnv "tutorial2_sysconfdir") (\_ -> return sysconfdir)

getDataFileName :: FilePath -> IO FilePath
getDataFileName name = do
  dir <- getDataDir
  return (dir ++ "/" ++ name)
