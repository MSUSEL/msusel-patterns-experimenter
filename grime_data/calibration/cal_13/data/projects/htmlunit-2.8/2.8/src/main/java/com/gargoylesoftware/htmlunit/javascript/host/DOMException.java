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

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * Exception for DOM manipulations.
 *
 * @see <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-17189187">
 * DOM-Level-2-Core</a>
 * @version $Revision: 5527 $
 * @author Marc Guillemot
 */
public class DOMException extends SimpleScriptable {
    /** Serial version UID. */
    private static final long serialVersionUID = 5758391763325220491L;
    /** If the specified range of text does not fit into a DOMString. */
    public static final short DOMSTRING_SIZE_ERR = org.w3c.dom.DOMException.DOMSTRING_SIZE_ERR;
    /** If any node is inserted somewhere it doesn't belong. */
    public static final short HIERARCHY_REQUEST_ERR = org.w3c.dom.DOMException.HIERARCHY_REQUEST_ERR;
    /** If index or size is negative, or greater than the allowed value. */
    public static final short INDEX_SIZE_ERR = org.w3c.dom.DOMException.INDEX_SIZE_ERR;
    /** If an attempt is made to add an attribute that is already in use elsewhere. */
    public static final short INUSE_ATTRIBUTE_ERR = org.w3c.dom.DOMException.INUSE_ATTRIBUTE_ERR;
    /** If a parameter or an operation is not supported by the underlying object. */
    public static final short INVALID_ACCESS_ERR = org.w3c.dom.DOMException.INVALID_ACCESS_ERR;
    /** If an invalid or illegal character is specified, such as in a name. */
    public static final short INVALID_CHARACTER_ERR = org.w3c.dom.DOMException.INVALID_CHARACTER_ERR;
    /** If an attempt is made to modify the type of the underlying object. */
    public static final short INVALID_MODIFICATION_ERR = org.w3c.dom.DOMException.INVALID_MODIFICATION_ERR;
    /** If an attempt is made to use an object that is not, or is no longer, usable. */
    public static final short INVALID_STATE_ERR = org.w3c.dom.DOMException.INVALID_STATE_ERR;
    /** If an attempt is made to create or change an object in a way which is incorrect with regard to namespaces. */
    public static final short NAMESPACE_ERR = org.w3c.dom.DOMException.NAMESPACE_ERR;
    /** If data is specified for a node which does not support data. */
    public static final short NO_DATA_ALLOWED_ERR = org.w3c.dom.DOMException.NO_DATA_ALLOWED_ERR;
    /** If an attempt is made to modify an object where modifications are not allowed. */
    public static final short NO_MODIFICATION_ALLOWED_ERR = org.w3c.dom.DOMException.NO_MODIFICATION_ALLOWED_ERR;
    /** If an attempt is made to reference a node in a context where it does not exist. */
    public static final short NOT_FOUND_ERR = org.w3c.dom.DOMException.NOT_FOUND_ERR;
    /** If the implementation does not support the requested type of object or operation. */
    public static final short NOT_SUPPORTED_ERR = org.w3c.dom.DOMException.NOT_SUPPORTED_ERR;
    /** If an invalid or illegal string is specified. */
    public static final short SYNTAX_ERR = org.w3c.dom.DOMException.SYNTAX_ERR;
    /** If a node is used in a different document than the one that created it (that doesn't support it). */
    public static final short WRONG_DOCUMENT_ERR = org.w3c.dom.DOMException.WRONG_DOCUMENT_ERR;

    private final short code_;
    private final String message_;
    private int lineNumber_;
    private String fileName_;

    /**
     * Default constructor used to build the prototype.
     */
    public DOMException() {
        code_ = -1;
        message_ = null;
    }

    /**
     * Constructor.
     * @param message the exception message
     * @param errorCode the error code
     */
    public DOMException(final String message, final short errorCode) {
        code_ = errorCode;
        message_ = message;
    }

    /**
     * Gets the exception code.
     * @return the exception code
     */
    public Object jsxGet_code() {
        if (code_ == -1) {
            return Context.getUndefinedValue();
        }
        return code_;
    }

    /**
     * Gets the exception message.
     * @return the exception message
     */
    public Object jsxGet_message() {
        if (message_ == null) {
            return Context.getUndefinedValue();
        }
        return message_;
    }

    /**
     * Gets the line at which the exception occurred.
     * @return the line of the exception
     */
    public Object jsxGet_lineNumber() {
        if (lineNumber_ == -1) {
            return Context.getUndefinedValue();
        }
        return lineNumber_;
    }

    /**
     * Gets the name of the in  which the exception occurred.
     * @return the name of the source file
     */
    public Object jsxGet_filename() {
        if (fileName_ == null) {
            return Context.getUndefinedValue();
        }
        return fileName_;
    }

    /**
     * Sets the location in JavaScript source where this exception occurred.
     * @param fileName the name of the source file
     * @param lineNumber the line number
     */
    public void setLocation(final String fileName, final int lineNumber) {
        fileName_ = fileName;
        lineNumber_ = lineNumber;
    }
}
