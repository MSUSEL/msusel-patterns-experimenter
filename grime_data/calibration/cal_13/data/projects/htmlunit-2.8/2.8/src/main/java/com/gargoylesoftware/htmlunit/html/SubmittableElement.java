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
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * An element that can have it's values sent to the server during a form submit.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 */
public interface SubmittableElement {

    /**
     * <p>Returns an array of {@link NameValuePair}s that are the values that will be sent
     * back to the server whenever this element's containing form is submitted.</p>
     *
     * <p>THIS METHOD IS INTENDED FOR THE USE OF THE FRAMEWORK ONLY AND SHOULD NOT
     * BE USED BY CONSUMERS OF HTMLUNIT. USE AT YOUR OWN RISK.</p>
     *
     * @return the values that will be sent back to the server whenever this element's
     *         containing form is submitted
     */
    NameValuePair[] getSubmitKeyValuePairs();

    /**
     * Returns the value of this element to the default value or checked state (usually what it was at
     * the time the page was loaded, unless it has been modified via JavaScript).
     */
    void reset();

    /**
     * Sets the default value to use when this element gets reset, if applicable.
     * @param defaultValue the default value to use when this element gets reset, if applicable
     */
    void setDefaultValue(final String defaultValue);

    /**
     * Returns the default value to use when this element gets reset, if applicable.
     * @return the default value to use when this element gets reset, if applicable
     */
    String getDefaultValue();

    /**
     * Sets the default checked state to use when this element gets reset, if applicable.
     * @param defaultChecked the default checked state to use when this element gets reset, if applicable
     */
    void setDefaultChecked(final boolean defaultChecked);

    /**
     * Returns the default checked state to use when this element gets reset, if applicable.
     * @return the default checked state to use when this element gets reset, if applicable
     */
    boolean isDefaultChecked();

}
