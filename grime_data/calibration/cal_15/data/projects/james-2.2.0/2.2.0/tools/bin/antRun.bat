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

REM   Copyright (c) 2001 The Apache Software Foundation.  All rights
REM   reserved.

rem Change drive and directory to %1 (Win9X only for NT/2K use "cd /d")
cd %1
%1\
set ANT_RUN_CMD=%2
shift
shift

set PARAMS=
:loop
if ""%1 == "" goto runCommand
set PARAMS=%PARAMS% %1
shift
goto loop

:runCommand
rem echo %ANT_RUN_CMD% %PARAMS%
%ANT_RUN_CMD% %PARAMS%

