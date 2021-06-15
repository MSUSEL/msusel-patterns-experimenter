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

@echo off
rem
rem Phoenix start script.
rem
rem Author: Peter Donald [donaldp@apache.org]
rem
rem The user may choose to supply parameters to the JVM (such as memory settings)
rem via setting the environment variable PHOENIX_JVM_OPTS
rem

rem
rem Determine if JAVA_HOME is set and if so then use it
rem
if not "%JAVA_HOME%"=="" goto found_java

set PHOENIX_JAVACMD=java
goto file_locate

:found_java
set PHOENIX_JAVACMD=%JAVA_HOME%\bin\java

:file_locate

rem
rem Locate where phoenix is in filesystem
rem
if not "%OS%"=="Windows_NT" goto start

rem %~dp0 is name of current script under NT
set PHOENIX_HOME=%~dp0

rem : operator works similar to make : operator
set PHOENIX_HOME=%PHOENIX_HOME:\bin\=%

:start

if not "%PHOENIX_HOME%" == "" goto phoenix_home

echo.
echo Warning: PHOENIX_HOME environment variable is not set.
echo   This needs to be set for Win9x as it's command prompt 
echo   scripting bites
echo.
goto end

:phoenix_home

rem echo "Home directory: %PHOENIX_HOME%"
rem echo "Home ext directory: %PHOENIX_HOME%\lib"

rem
rem This is needed as some JVM vendors do foolish things
rem like placing jaxp/jaas/xml-parser jars in ext dir
rem thus breaking Phoenix
rem

rem Kicking the tires and lighting the fires!!!
%PHOENIX_JAVACMD% -Djava.ext.dirs="%PHOENIX_HOME%\lib" %PHOENIX_JVM_OPTS% -jar "%PHOENIX_HOME%\bin\phoenix-loader.jar" %1 %2 %3 %4 %5 %6 %7 %8 %9

:end
