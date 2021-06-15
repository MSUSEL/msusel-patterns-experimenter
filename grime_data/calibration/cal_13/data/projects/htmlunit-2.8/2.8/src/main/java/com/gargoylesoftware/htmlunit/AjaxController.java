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
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * This class is notified when AJAX calls are made, and has the ability to influence these calls.
 * For instance, it can turn asynchronous AJAX calls into synchronous AJAX calls, making test code
 * deterministic and avoiding calls to <tt>Thread.sleep()</tt>.
 *
 * @version $Revision: 5660 $
 * @author Marc Guillemot
 */
public class AjaxController implements Serializable {

    private static final long serialVersionUID = 2170842485774504546L;

    /**
     * Gets notified of an AJAX call to determine how it should be processed.
     * @param page the page the request comes from
     * @param request the request that should be performed
     * @param async indicates if the request should originally be asynchron
     * @return if the call should be synchron or not; here just like the original call
     */
    public boolean processSynchron(final HtmlPage page, final WebRequest request, final boolean async) {
        return !async;
    }

}
