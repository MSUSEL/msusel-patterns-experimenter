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
package org.apache.james.util;

import java.util.Date;
import javax.mail.internet.MailDateFormat;

/**
 * A thread safe wrapper for the <code>javax.mail.internet.MailDateFormat</code> class.
 *
 */
public class RFC822DateFormat extends SynchronizedDateFormat {
    /**
     * A static instance of the RFC822DateFormat, used by toString
     */
    private static RFC822DateFormat instance; 

    static {
        instance = new RFC822DateFormat();
    }

    /**
     * This static method allows us to format RFC822 dates without
     * explicitly instantiating an RFC822DateFormat object.
     *
     * @return java.lang.String
     * @param d Date
     *
     * @deprecated This method is not necessary and is preserved for API
     *             backwards compatibility.  Users of this class should
     *             instantiate an instance and use it as they would any
     *             other DateFormat object.
     */
    public static String toString(Date d) {
        return instance.format(d);
    }

    /**
     * Constructor for RFC822DateFormat
     */
    public RFC822DateFormat() {
        super(new MailDateFormat());
    }
}
