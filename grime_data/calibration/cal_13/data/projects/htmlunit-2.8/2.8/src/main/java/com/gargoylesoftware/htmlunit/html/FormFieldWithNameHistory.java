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

import java.util.Collection;

/**
 * Interface for form fields where the original field name still matters even once it
 * has been changed.
 * <p>
 * Example:<br/>
 * With<br/>
 * <code>&lt;input name="foo"/&gt;</code><br/>
 * following script will work<br/>
 * <code>
 * theForm.foo.name = 'newName';<br/>
 * theForm.foo.value = 'some value';
 * </code><br/>
 * Depending on the simulated browser the form field is reachable only through its original name
 * or through all the names it has had.
 * @author Marc Guillemot
 * @version $Revision: 5796 $
 */
public interface FormFieldWithNameHistory {
    /**
     * Gets the first value of the <code>name</code> attribute of this field before any change.
     * @return the original name (which is the same as the current one when no change has been made)
     */
    String getOriginalName();

    /**
     * Get all the names this field had before the current one.
     * @return an empty collection if the name attribute has never been changed.
     */
    Collection<String> getPreviousNames();
}
