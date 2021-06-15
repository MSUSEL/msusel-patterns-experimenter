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
package com.gargoylesoftware.htmlunit.javascript;

import java.lang.ref.WeakReference;

import com.gargoylesoftware.htmlunit.Page;

/**
 * An action triggered by a script execution but that should be executed first when the script is finished.
 * Example: when a script sets the source of an (i)frame, the request to the specified page will be first
 * triggered after the script execution.
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 */
public abstract class PostponedAction {

    private final WeakReference<Page> owningPageRef_; // as weak ref in case it may allow page to be GCed

    /**
     * C'tor.
     * @param owningPage the page that initiates this action
     */
    public PostponedAction(final Page owningPage) {
        owningPageRef_ = new WeakReference<Page>(owningPage);
    }

    /**
     * Gets the owning page.
     * @return the page that initiated this action or <code>null</code> if it has already been GCed
     */
    Page getOwningPage() {
        return owningPageRef_.get();
    }

    /**
     * Execute the action.
     * @throws Exception if it fails
     */
    public abstract void execute() throws Exception;
}
