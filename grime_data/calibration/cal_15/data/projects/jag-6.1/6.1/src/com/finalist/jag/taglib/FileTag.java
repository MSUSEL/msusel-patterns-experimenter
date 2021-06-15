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
package com.finalist.jag.taglib;



import java.util.*;

import com.finalist.jag.*;
import com.finalist.jag.taglib.util.RequestUtil;


/**
 * Class FileTag
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class FileTag extends TagBodySupport {

    /** Field path */
    private String path = null;

    /** Field title */
    private String title = null;

    /** Field ext */
    private String ext = null;

    /** Field name           */
    private String name = null;

    /** Field property           */
    private String property = null;

    /** Field counter           */
    protected int counter = 0;


    /**
     * Method setName
     *
     *
     * @param name
     *
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method setValue
     *
     *
     * @param property
     *
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * Method getName
     *
     *
     * @return
     *
     */
    public String getName() {
        return (this.name);
    }

    /**
     * Method getValue
     *
     *
     * @return
     *
     */
    public String getProperty() {
        return (this.property);
    }

    /**
     * Method getPath
     *
     *
     * @return
     *
     */
    public String getPath() {
        return (this.path);
    }

    /**
     * Method setPath
     *
     *
     * @param path
     *
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Method getTitle
     *
     *
     * @return
     *
     */
    public String getTitle() {
        return (this.title);
    }

    /**
     * Method setTitle
     *
     *
     * @param title
     *
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Method getExt
     *
     *
     * @return
     *
     */
    public String getExt() {
        return (this.ext);
    }

    /**
     * Method setExt
     *
     *
     * @param ext
     *
     */
    public void setExt(String ext) {
        this.ext = ext;
    }

    /**
     * Method doStartTag
     *
     *
     * @return
     *
     * @throws JagException
     *
     */
    public int doStartTag() throws JagException {
        return (EVAL_PAGE);
    }

    /**
     * Method doAfterBodyTag
     *
     *
     * @return
     *
     * @throws JagException
     *
     */
    public int doAfterBodyTag() throws JagException {

        counter++;
		// Retrieve the file title from the tag body.
        if ((title == null) || (title.length() < 1)) {
            if (counter == 1) {
                return (EVAL_BODY_TAG);
            }
            else {
                title = getBodyText();
                title = title.replace('\n',' ');
                title = title.replace('\r',' ');
                title = title.trim();
            }
        }
		// Construct the filepath
        StringBuffer filePath = new StringBuffer();

        if ((path != null) && (path.length() > 0)) {
            filePath.append(path);
        }
        else if(name != null && property != null)
        {
            String value = RequestUtil.lookupString(getPageContext(), name,
                                                    property);
            if (value == null) {
                throw new JagException("WriteTag: Invalid name field >"
                                       + name + "<");
            }

            filePath.append(value);
        }

        filePath.append(title);

        if ((ext != null) && (ext.length() > 0)) {
            filePath.append(".");
            filePath.append(ext);
        }
		// Create a new file entry.
        getWriter().createNewFile(filePath);
        return (SKIP_CLEAR_BODY);
    }
}
;