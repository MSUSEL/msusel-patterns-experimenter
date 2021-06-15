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

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.upload.FormFile;

import com.ivata.mask.Mask;
import com.ivata.mask.validation.ValidationErrors;
import com.ivata.mask.web.struts.DialogForm;


/**
 * <p>Library image upload form. It's used when there are some local
 * images
 * in the uploaded HTML for library document.</p>
 *
 * @since 2003-07-04
 * @author Peter Illes
 * @version $Revision: 1.3 $
 */
public class UploadImagesForm extends DialogForm {

    /**
     * <p><code>Map</code> storing the version comments of images</p>
     */
    private Map comment = new HashMap();
    /**
     * <p><code>Map</code> of <code>FormFile</code>s, the uploaded
     * images</p>
     */
    private Map image = new HashMap();

    /**
     * <p><code>Vector</code> holding the names of images to upload</p>
     */
    private Vector imageFileName = null;
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
     * TODO
     *
     * @see com.ivata.mask.web.struts.MaskForm#clear()
     */
    protected void clear() {
        // TODO Auto-generated method stub

    }

    /**
     * <p>comments for uploaded images</p>
     * @param index the index in the vector
     * @return the comment with the given index
     */
    public final String getComment(final String index) {
        return (String) comment.get(index);
    }

    /**
     * <p><code>Vector</code> of <code>FormFile</code>s, the uploaded
     * images</p>
     * @param index
     * @return null , file input type can't be get to jsp
     */
    public final FormFile getImage(final String index) {
        return null;
    }

    /**
     * <p><code>Vector</code> holding the names of images to upload</p>
     * @return <code> <code>Vector</code> holding the names of images to
     * upload
     */
    public final Vector getImageFileName() {
        return imageFileName;
    }

    /**
     * <p>returns all uploaded images</p>
     * @return all uploaded images
     */
    public final Map getImages() {
        return image;
    }

    /**
     * <p>comment for uploaded image</p>
     * @param index the index in the vector
     * @comment the version comment
     */
    public final void setComment(final String index,
            final String comment) {
        this.comment.put(index, comment);
    }

    /**
     * <p><code>Vector</code> of <code>FormFile</code>s, the uploaded
     * images</p>
     * @param index
     * @param image the uploaded file
     */
    public final void setImage(final String index,
            final FormFile image) {
        this.image.put(index, image);
    }

    /**
     * <p><code>Vector</code> holding the names of images to upload</p>
     * @param imageFileName <code> <code>Vector</code> holding the names
     * of images to upload
     */
    public final void setImageFileName(final Vector imageFileName) {
        this.imageFileName = imageFileName;
    }

    /**
     * TODO
     *
     * @see com.ivata.mask.web.struts.MaskForm#validate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpSession)
     */
    public ValidationErrors validate(final HttpServletRequest request,
            final HttpSession session) {
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
