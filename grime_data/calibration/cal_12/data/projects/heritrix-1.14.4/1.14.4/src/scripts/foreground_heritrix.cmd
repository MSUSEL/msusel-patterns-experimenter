@REM
@REM The MIT License (MIT)
@REM
@REM MSUSEL Arc Framework
@REM Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
@REM Software Engineering Laboratory and Idaho State University, Informatics and
@REM Computer Science, Empirical Software Engineering Laboratory
@REM
@REM Permission is hereby granted, free of charge, to any person obtaining a copy
@REM of this software and associated documentation files (the "Software"), to deal
@REM in the Software without restriction, including without limitation the rights
@REM to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
@REM copies of the Software, and to permit persons to whom the Software is
@REM furnished to do so, subject to the following conditions:
@REM
@REM The above copyright notice and this permission notice shall be included in all
@REM copies or substantial portions of the Software.
@REM
@REM THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
@REM IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
@REM FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
@REM AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
@REM LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
@REM OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
@REM SOFTWARE.
@REM

:: This is the windows version of the foreground_heritrix shell script
:: The only difference to an invokation with "heritrix.cmd" is that no extra
:: (minimized) console window is created...
:: Caveats and version history, see heritrix.cmd
::
:: This script launches the heritrix crawler and keeps the window in foreground
::
:: Optional environment variables
::
:: JAVA_HOME        Point at a JDK install to use.
:: 
:: HERITRIX_HOME    Pointer to your heritrix install.  If not present, we 
::                  make an educated guess based of position relative to this
::                  script.
::
:: JAVA_OPTS        Java runtime options.
::
:: FOREGROUND       Set to any value -- e.g. 'true' -- if you want to run 
::                  heritrix in foreground (Used by build system when it runs
::                  selftest to see if completed successfully or not)..
::
@echo off
setlocal ENABLEEXTENSIONS ENABLEDELAYEDEXPANSION
set PRGDIR=%~p0
set FOREGROUND=true
call "%PRGDIR%\heritrix.cmd" %*
