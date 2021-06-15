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
package com.ivata.groupware.business.library.struts;

import com.ivata.mask.Mask;
import com.ivata.mask.web.struts.DialogForm;


/**
 * <p>Provides a base class for library form
 * classes, to retrieve the library objects.</p>
 *
 *
 * @since 2003-02-18
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public abstract class LibraryForm extends DialogForm {

    /**
     * <p>Indicates the current item/comment should be edited.</p>
     */
    private String edit = null;
    /**
     * <p>When submitting library items and comments, they must be
     * previewed once before submitting.</p>
     */
    private String preview = null;
    /**
     * <p>
     * Defines the base class of all objects in the value object list.
     * </p>
     */
    private Class baseClass;
    /**
     * <p>
     * Mask containing all the field definitions for this list.
     * </p>
     */
    private Mask mask;

    /**
     * <p>Default constructor. Overridden to set <code>bundle</code>.</p>
     *
     * @param maskParam
     *            Refer to {@link #getMask}.
     * @param baseClassParam
     *            Refer to {@link #getBaseClass}.
     */
    public LibraryForm() {
        // we want messages from the library
        setBundle("library");
    }

    /**
     * <p>Indicates the current item/comment should be edited.</p>
     *
     * @return the current value of edit.
     */
    public final String getEdit() {
        return edit;
    }

    /**
     * <p>When submitting library items and comments, they must be
     * previewed once before submitting.</p>
     *
     * @return the current value of preview.
     */
    public final String getPreview() {
        return preview;
    }

    /**
     * <p>Indicates the current item/comment should be edited.</p>
     *
     * @param edit the new value of edit.
     */
    public final void setEdit(final String edit) {
        this.edit = edit;
    }

    /**
     * <p>When submitting library items and comments, they must be
     * previewed once before submitting.</p>
     *
     * @param preview the new value of preview.
     */
    public final void setPreview(final String preview) {
        this.preview = preview;
    }

    /**
     * <p>
     * Defines the base class of all objects in the value object list.
     * </p>
     *
     * @return base class of all objects in the value object list.
     */
    public final Class getBaseClass() {
        return baseClass;
    }

    /**
     * <p>
     * Mask containing all the field definitions for this list.
     * </p>
     *
     * @return mask containing all the field definitions for this list.
     */
    public final Mask getMask() {
        return mask;
    }
}
