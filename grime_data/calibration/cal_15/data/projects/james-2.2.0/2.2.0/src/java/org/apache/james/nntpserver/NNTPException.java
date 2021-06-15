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
package org.apache.james.nntpserver;

/**
 * Exception Wrapper, like javax.servlet.ServletException.
 * Purpose is to catch and wrap exceptions into unchecked NNTP specific. 
 * Protocol handler catches the exception and returns error info to client.
 * Error Information is obtained by calling 'getMessage'
 *
 */
public class NNTPException extends RuntimeException {

    /**
     * The encapsulated Throwable
     */
    private final Throwable t;

    /**
     * Create an NNTPException with an error message and no
     * encapsulated <code>Throwable</code>
     *
     * @param msg the error message for this exception
     */
    public NNTPException(String msg) {
        super(msg);
        this.t = null;
    }

    /**
     * Create an NNTPException with an error message and an
     * encapsulated <code>Throwable</code>
     *
     * @param msg the error message for this exception
     * @param t the encapsulated <code>Throwable</code>
     */
    public NNTPException(String msg,Throwable t) {
        super(msg+((t!=null)?": "+t.toString():""));
        this.t = t;
    }

    /**
     * Create an NNTPException with an
     * encapsulated <code>Throwable</code>
     *
     * @param t the encapsulated <code>Throwable</code>
     */
    public NNTPException(Throwable t) {
        super(t.toString());
        this.t = t;
    }
}
