#!/bin/bash
#
# The MIT License (MIT)
#
# MSUSEL Arc Framework
# Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
# Software Engineering Laboratory and Idaho State University, Informatics and
# Computer Science, Empirical Software Engineering Laboratory
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
#

cd `dirname $0`

if test -d exim; then
    echo "This program makes links to the scripts in the exim directory."
else
    echo "No directory called exim! Cannot link scripts.";
    exit -1;
fi

for script in exim/*;
do
    script=$(echo $script | sed s/^exim.//)
    if [ "$script" != CVS ]; then
        chmod +x "exim/$script"
        for directory in *;
        do
            if [[ "$directory" != CVS && "$directory" != exim && -e "$directory/exec.sh" && "$directory" != makeLinks.sh && "$directory" != clearLinks.sh ]]; then
                chmod a+x "$directory/exec.sh"
                if [[ -e "$directory/$script" && ! -h "$directory/$script" ]]; then
                    echo "Skipping $directory/$script"
                    chmod a+x "$directory/$script"
                else
                    echo "Linking $directory/$script"
                    ln -sf "exec.sh" "$directory/$script"
                    chmod a+x "$directory/$script"
                fi
            fi
        done
    fi
done
