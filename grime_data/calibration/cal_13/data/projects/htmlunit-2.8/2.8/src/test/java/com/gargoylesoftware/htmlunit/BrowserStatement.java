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

import java.lang.reflect.Method;

import org.junit.runners.model.Statement;

/**
 * The Browser Statement.
 *
 * @version $Revision: 5635 $
 * @author Ahmed Ashour
 */
class BrowserStatement extends Statement {

    private Statement next_;
    private final boolean shouldFail_;
    private final boolean notYetImplemented_;
    private final Method method_;
    private final String browserVersionString_;
    private final int tries_;

    BrowserStatement(final Statement next, final Method method, final boolean shouldFail,
            final boolean notYetImplemented, final int tries, final String browserVersionString) {
        next_ = next;
        method_ = method;
        shouldFail_ = shouldFail;
        notYetImplemented_ = notYetImplemented;
        tries_ = tries;
        browserVersionString_ = browserVersionString;
    }

    @Override
    public void evaluate() throws Throwable {
        for (int i = 0; i < tries_; i++) {
            try {
                evaluateSolo();
                break;
            }
            catch (final Throwable t) {
                if (shouldFail_ || notYetImplemented_) {
                    throw t;
                }
                System.out.println("Failed test "
                        + method_.getDeclaringClass().getName() + '.' + method_.getName() + " #" + (i + 1));
                if (i == tries_ - 1) {
                    throw t;
                }
            }
        }
    }

    public void evaluateSolo() throws Throwable {
        Exception toBeThrown = null;
        try {
            next_.evaluate();
            if (shouldFail_) {
                final String errorMessage;
                if (browserVersionString_ == null) {
                    errorMessage = method_.getName() + " is marked to fail with "
                        + browserVersionString_ + ", but succeeds";
                }
                else {
                    errorMessage = method_.getName() + " is marked to fail, but succeeds";
                }
                toBeThrown = new Exception(errorMessage);
            }
            else if (notYetImplemented_) {
                final String errorMessage;
                if (browserVersionString_ == null) {
                    errorMessage = method_.getName() + " is marked as not implemented but already works";
                }
                else {
                    errorMessage = method_.getName() + " is marked as not implemented with "
                        + browserVersionString_ + " but already works";
                }
                toBeThrown = new Exception(errorMessage);
            }
        }
        catch (final Throwable e) {
            if (!shouldFail_ && !notYetImplemented_) {
                throw e;
            }
        }
        if (toBeThrown != null) {
            throw toBeThrown;
        }
    }
}
