====
    The MIT License (MIT)

    MSUSEL Arc Framework
    Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
    Software Engineering Laboratory and Idaho State University, Informatics and
    Computer Science, Empirical Software Engineering Laboratory

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
====

========================
ecmaunit unit test suite
========================

This is an object oriented unit test suite library for JavaScript. It is
tested on Internet Explorer and Mozilla and should run on all browers with
decent JavaScript support.

This package can just be placed somewhere on a filesystem or webserver and
used directly by other JavaScripts. For an example (this should be enough to 
get you started) see 'testecmaunit.js' and 'testecmaunit.html' (which happen 
to be tests for the system as well, although they're a bit awkward for some 
of them are supposed to fail for the demo).

EcmaUnit also supports running test from the command-line with a
proper JavaScript interpreter. Spidermonkey of the Mozilla project has
proven to work. To run the ecmaunit tests from the command line using
the spidermonkey interpreter js, simply issue the following command:

  $ js -f ecmaunit.js -f testecmaunit.js runtests.js

If you have any questions, want to report bugs or have suggestions about how
to improve the product, please send an email to Guido Wesdorp 
(guido@infrae.com).

Thanks for using EcmaUnit!
