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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

import com.ivata.mask.Mask;
import com.ivata.mask.validation.ValidationErrors;
import com.ivata.mask.web.struts.DialogForm;


/**
 * <p>This form is used to upload a HTML file straight to pages of an editer library documentr.</p>
 *
 * @since 2003-07-02
 * @author Peter Illes
 * @version $Revision: 1.3 $
 *
 */
public class UploadHTMLForm extends DialogForm {
    /**
     * <p>the uploaded file</p>
     */
    private FormFile file;
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
     * @see com.ivata.mask.web.struts.MaskForm#clear()
     */
    protected void clear() {
    }

    /**
     * <p>getter for the file field</p>
     * @return the uploaded file as <code>FormFile</code>
     */
    public FormFile getFile() {
        return this.file;
    }

    /**
     * <p>setter for the file field</p>
     * @param file  the uploaded file as <code>FormFile</code>
     */
    public void setFile(FormFile file) {
        this.file = file;
    }

    /**
     * Validate the properties that have been set for this HTTP request,
     * and return an <code>ActionMessages</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found,
     * return <code>null</code> or an <code>ActionMessages</code> object with
     * no recorded error messages.
     * <p>
     * The default implementation performs no validation and returns
     * <code>null</code>.  Subclasses must override this method to provide
     * any validation they wish to perform.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if ((file == null) || (file.getFileSize() == 0)) {
            errors.add("file", new ActionMessage("errors.nullUpload"));
        } else if (!file.getContentType().equals("text/HTML")) {
            errors.add("file", new ActionMessage("errors.fileContentType", "HTML"));
        }

        return errors;
    }

    /**
     * @see com.ivata.mask.web.struts.MaskForm#validate
     */
    public ValidationErrors validate(HttpServletRequest request, HttpSession session) {
        // TODO Auto-generated method stub
        return null;
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
