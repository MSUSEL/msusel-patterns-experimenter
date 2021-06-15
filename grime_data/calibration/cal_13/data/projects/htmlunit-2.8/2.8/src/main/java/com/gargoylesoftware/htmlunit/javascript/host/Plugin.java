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
package com.gargoylesoftware.htmlunit.javascript.host;

/**
 * A JavaScript object for a document.navigator.plugins.
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 *
 * @see <a href="http://www.xulplanet.com/references/objref/MimeTypeArray.html">XUL Planet</a>
 */
public class Plugin extends SimpleArray {
    private static final long serialVersionUID = -6829501824595761156L;
    private String description_;
    private String filename_;
    private String name_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public Plugin() {
        // nothing
    }

    /**
     * C'tor initializing fields.
     * @param name the plugin name
     * @param description the plugin description
     * @param filename the plugin filename
     */
    public Plugin(final String name, final String description, final String filename) {
        name_ = name;
        description_ = description;
        filename_ = filename;
    }

    /**
     * Gets the name of the mime type.
     * @param element a {@link MimeType}
     * @return the name
     */
    @Override
    protected String getItemName(final Object element) {
        return ((MimeType) element).jsxGet_type();
    }

    /**
     * Gets the plugin's description.
     * @return the description
     */
    public String jsxGet_description() {
        return description_;
    }

    /**
     * Gets the plugin's file name.
     * @return the file name
     */
    public String jsxGet_filename() {
        return filename_;
    }

    /**
     * Gets the plugin's name.
     * @return the name
     */
    public String jsxGet_name() {
        return name_;
    }
}
