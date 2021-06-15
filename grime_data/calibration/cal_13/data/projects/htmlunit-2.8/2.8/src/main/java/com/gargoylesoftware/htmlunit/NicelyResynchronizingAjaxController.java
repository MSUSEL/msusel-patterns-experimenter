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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.ref.WeakReference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * This {@link AjaxController} resynchronizes calls calling from the main thread.
 * The idea is that asynchronous AJAX calls performed directly in response to a user
 * action (therefore in the "main" thread and not in the thread of a background task)
 * are directly useful for the user. To easily have a testable state, these calls
 * are performed synchronously.
 *
 * @version $Revision: 5864 $
 * @author Marc Guillemot
 */
public class NicelyResynchronizingAjaxController extends AjaxController {

    private static final long serialVersionUID = -5406000795046341395L;
    private static final Log LOG = LogFactory.getLog(NicelyResynchronizingAjaxController.class);

    private transient WeakReference<Thread> originatedThread_;

    /**
     * Creates an instance.
     */
    public NicelyResynchronizingAjaxController() {
        init();
    }

    /**
     * Initializes this instance.
     */
    private void init() {
        originatedThread_ = new WeakReference<Thread>(Thread.currentThread());
    }

    /**
     * Resynchronizes calls performed from the thread where this instance has been created.
     *
     * {@inheritDoc}
     */
    @Override
    public boolean processSynchron(final HtmlPage page, final WebRequest settings, final boolean async) {
        if (async && isInOriginalThread()) {
            LOG.info("Re-synchronized call to " + settings.getUrl());
            return true;
        }
        return !async;
    }

    /**
     * Indicates if the currently executing thread is the one in which this instance has been created.
     * @return <code>true</code> if it's the same thread
     */
    boolean isInOriginalThread() {
        return Thread.currentThread() == originatedThread_.get();
    }

    /**
     * Custom deserialization logic.
     * @param stream the stream from which to read the object
     * @throws IOException if an IO error occurs
     * @throws ClassNotFoundException if a class cannot be found
     */
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        init();
    }

}
