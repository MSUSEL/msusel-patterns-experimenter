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

package org.hsqldb.util.preprocessor;

/* $Id: Option.java 610 2008-12-22 15:54:18Z unsaved $ */

/**
 * Static methods and constants to decode preprocessor options.
 *
 * @author boucherb@users
 * @version 1.8.1
 * @since 1.8.1
 */
public class Option {

    public static final int DEFAULT   = 0;    // No options set
    public static final int BACKUP    = 1<<0; // Backup source files?
    public static final int FILTER    = 1<<1; // Remove directive lines?
    public static final int INDENT    = 1<<2; // indent directive lines?
    public static final int TEST_ONLY = 1<<3; // process only; don't save
    public static final int VERBOSE   = 1<<4; // log detailed info

    private Option(){/*Construction Disabled*/}

    public static boolean isDefault(int options) {
        return options == DEFAULT;
    }

    public static int setDefault(int options, boolean _default) {
        return (_default) ? DEFAULT : options;
    }

    public static boolean isBackup(int options) {
        return ((options & BACKUP) != 0);
    }

    public static int setBackup(int options, boolean backup) {
        return (backup) ? (options | BACKUP) : (options & ~BACKUP);
    }

    public static boolean isFilter(int options) {
        return ((options & FILTER) != 0);
    }

    public static int setFilter(int options, boolean filter) {
        return (filter) ? (options | FILTER) : (options & ~FILTER);
    }

    public static boolean isIndent(int options) {
        return ((options & INDENT) != 0);
    }

    public static int setIndent(int options, boolean indent) {
        return (indent) ? (options | INDENT) : (options & ~INDENT);
    }

    public static boolean isTestOnly(int options) {
        return ((options & TEST_ONLY) != 0);
    }

    public static int setTestOnly(int options, boolean testOnly) {
        return (testOnly) ? (options | TEST_ONLY) : (options & ~TEST_ONLY);
    }

    public static boolean isVerbose(int options) {
        return ((options & VERBOSE) != 0);
    }

    public static int setVerbose(int options, boolean verbose) {
        return (verbose) ? (options | VERBOSE) : (options & ~VERBOSE);
    }
}
