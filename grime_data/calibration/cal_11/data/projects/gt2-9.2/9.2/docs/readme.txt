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

To build the docs for the first time....

.. this is covered in the developers guide setup instructions in greater details.

Windows Sphinx Install
^^^^^^^^^^^^^^^^^^^^^^

Install Python:

1. Python version 2.7 has been verified to work: http://www.python.org/download/releases/2.7/
2. You will need to add it to your path.::
     
     set 'PYTHON=C:\Python27\'
     set 'PATH=%PATH%;%PYTHON%'

3. You will need Setup Tools for Python 2.7
   
   http://pypi.python.org/pypi/setuptools#downloads
   
4. Install and add Setup Tools to your path::
        
        run 'set SETUPTOOLS=C:\Python27\Scripts'
        run 'set PATH=%PATH%;%SETUPTOOLS%'

5. Install Sphinx::
   
        easy_install sphinx
   
   Optionally you could install a specific version of sphinx (although we try and use the latest)::
    
        easy_install sphinx==1.0.7

rst2pdf Optional Install
^^^^^^^^^^^^^^^^^^^^^^^^

You can optionally install rst2pdf to build pdf documentation:

1. Install Visual Studio 2008 Express Edition (this provides windows with a C compiler). It is a free download on the Microsoft site.
   You need to be sure to use the 2008 edition so that easy_install will compile something that can actually be linked to the Python executable.

2. Use easy install to produce rst2pdf::
      
      easy_install rst2pdf
      
3. This depends on the Python Image Library (which it can probably build now that you have a compiler).
4. If you cannot manage to build you can download a precompiled Python Image Library (PIL) from here:
   
   * http://effbot.org/downloads/#pil (download the one for python 2.7)

Mac Sphinx Install
^^^^^^^^^^^^^^^^^^

You can use the distribution manager of your choice (example bru, macports, etc...). The following example
is for macports.

1. On OSX Use macports to install Python 2.7::
     
     sudo port install python27
     sudo port install python_select
     sudo python_select python27
     
2. You can use macports to install Python Image Library::
     
     sudo port install py27-pil
     
3. You can now use python easy_install to install sphinx::
     
     sudo easy_install sphinx
   
   Optionally you could ask for a specific version (we try and use the latest)::
     
      sudo easy_install sphinx==1.0.7
 
4. To build the PDF targets you will also need rst2pdf.::
     
     sudo easy_install rst2pdf

5. If you uses easy_install to grab the python image library it easy to get compile errors.
      

Linux Sphinx Install
^^^^^^^^^^^^^^^^^^^^

Use apt-get and easy install.

1. Python is usually available by default, if not::
     
      apt-get install Python
  
   You may need to use sudo (if for example you are on unbuntu)
      
2. Use easy_install to graph sphinx (using sudo if required)::
     
     easy_install sphinx
  
  Optionally you can install a specifc version::
  
     easy_install sphinx==1.0.7
 