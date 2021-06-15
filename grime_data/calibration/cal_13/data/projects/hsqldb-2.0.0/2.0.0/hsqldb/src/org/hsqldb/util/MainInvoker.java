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

package org.hsqldb.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Invokes the static main(String[]) method from each class specified.
 *
 * This class <b>will System.exit()</b> if any invocation fails.
 *
 * @author Blaine Simpson (blaine dot simpson at admc dot com)
 * @since    HSQLDB 1.8.0
 * @version  $Revision: 3481 $, $Date: 2010-02-26 13:05:06 -0500 (Fri, 26 Feb 2010) $
 */
public class MainInvoker {

    /*
     * This class currently consists of just a static utility.
     * It may or may not make sense to make this into a class with real
     * instances that can keep track of status of stuff invoked by it.
     */
    private static String[] emptyStringArray = new String[0];

    private static void syntaxFailure() {
        System.err.println(SYNTAX_MSG);
        System.exit(2);
    }

    /**
     * Invokes the static main(String[]) method from each specified class.
     * This method <b>will System.exit()</b> if any invocation fails.
     *
     * Note that multiple class invocations are delimited by empty-string
     * parameters.  How the user supplies these empty strings is determined
     * entirely by the caller's environment.  From Windows this can
     * generally be accomplished with double-quotes like "".  From all
     * popular UNIX shells, this can be accomplished with single or
     * double-quotes:  '' or "".
     *
     * @param sa Run java org.hsqldb.util.MainInvoker --help for syntax help
     */
    public static void main(String[] sa) {

        if (sa.length > 0 && sa[0].equals("--help")) {
            System.err.println(SYNTAX_MSG);
            System.exit(0);
        }

        ArrayList outList  = new ArrayList();
        int       curInArg = -1;

        try {
            while (++curInArg < sa.length) {
                if (sa[curInArg].length() < 1) {
                    if (outList.size() < 1) {
                        syntaxFailure();
                    }

                    invoke((String) outList.remove(0),
                           (String[]) outList.toArray(emptyStringArray));
                    outList.clear();
                } else {
                    outList.add(sa[curInArg]);
                }
            }

            if (outList.size() < 1) {
                syntaxFailure();
            }

            invoke((String) outList.remove(0),
                   (String[]) outList.toArray(emptyStringArray));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static String LS = System.getProperty("line.separator");
    private static String SYNTAX_MSG =
        "    java org.hsqldb.util.MainInvoker "
        + "[package1.Class1 [arg1a arg1b...] \"\"]... \\\n"
        + "    packageX.ClassX [argXa argXb...]\n" + "OR\n"
        + "    java org.hsqldb.util.MainInvoker --help\n\n"
        + "Note that you can only invoke classes in 'named' (non-default) "
        + "packages.  Delimit multiple classes with empty strings.";
    static {
        if (!LS.equals("\n")) {
            SYNTAX_MSG = SYNTAX_MSG.replaceAll("\n", LS);
        }
    }

    /**
     * Invokes the static main(String[]) method from each specified class.
     */
    public static void invoke(String className,
                              String[] args)
                              throws ClassNotFoundException,
                                     NoSuchMethodException,
                                     IllegalAccessException,
                                     InvocationTargetException {

        Class    c;
        Method   method;
        Class[]  stringArrayCA = { emptyStringArray.getClass() };
        Object[] objectArray   = { (args == null) ? emptyStringArray
                                                  : args };

        c      = Class.forName(className);
        method = c.getMethod("main", stringArrayCA);

        method.invoke(null, objectArray);

        //System.err.println(c.getName() + ".main() invoked");
    }
}
