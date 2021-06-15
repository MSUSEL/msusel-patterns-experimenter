/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.apache.james.mailrepository.filepair;

import java.io.File;
import java.io.FilenameFilter;

/**
 * This filters files based on the extension and is tailored to provide
 * backwards compatibility of the numbered repositories that Avalon does.
 *
 */
public class NumberedRepositoryFileFilter implements FilenameFilter {
    private String postfix;
    private String prefix;

    public NumberedRepositoryFileFilter(final String extension) {
        postfix = extension;
        prefix = "." + RepositoryManager.getName();
    }

    public boolean accept(final File file, final String name) {
        //System.out.println("check: " + name);
        //System.out.println("post: " + postfix);
        if (!name.endsWith(postfix)) {
            return false;
        }
        //Look for a couple of digits next
        int pos = name.length() - postfix.length();
        //We have to find at least one digit... if not then this isn't what we want
        if (!Character.isDigit(name.charAt(pos - 1))) {
            return false;
        }
        pos--;
        while (pos >= 1 && Character.isDigit(name.charAt(pos - 1))) {
            //System.out.println("back one");
            pos--;
        }
        //System.out.println("sub: " + name.substring(0, pos));
        //Now want to check that we match the rest
        return name.substring(0, pos).endsWith(prefix);
    }
}


