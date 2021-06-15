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

JASML is a tool providing alternative ways, other than the commonly used java programming language, to view and edit the java class file - through asm like java macro instructions specified in The Java Language Specification.

As we all known, the java is a platform independent, and this is achieved through the JVM. The code is written once, and compiled into .class files(with the javac.exe or other compilers), which is then executed by JVM on different platforms. However, there is another way to construct the .class files, by directly writting the macro insctructions which can be recognized by JVM. Also, the .class files can be decompiled into macro instructions. The java macro instructions to java is what asm to C.

JASML provides ways to decompile java .class file into .jasm file. So, even without the source code, developers can now investigate the inner implementation of java classes.

JASML also provides means to compile from .jasm file to .class file, providing another way to implement a java class, and more interesting, allowing users to modify the java classes without the source code at hand(this is actually the reason why JASML is created).

JAML supports all the instructions and attributes defined in The Java Language Specification(except the LineNumber attribute which is missed when compiling .jasm files).

Any Problems or Suggestions please sent to Jiang Yang at yang.jiang.z@gmail.com

