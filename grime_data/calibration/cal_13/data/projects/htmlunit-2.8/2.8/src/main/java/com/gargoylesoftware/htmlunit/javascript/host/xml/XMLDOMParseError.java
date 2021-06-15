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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for XMLDOMParseError.
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms757019.aspx">MSDN documentation</a>
 *
 * @version $Revision: 5893 $
 * @author Ahmed Ashour
 */
public class XMLDOMParseError extends SimpleScriptable {

    private static final long serialVersionUID = 8435555857574741660L;

    private int errorCode_;
    private int filepos_;
    private int line_;
    private int linepos_;
    private String reason_ = "";
    private String srcText_ = "";
    private String url_ = "";

    /**
     * Returns the error code of the last parse error.
     * @return the error code of the last parse error
     */
    public int jsxGet_errorCode() {
        return errorCode_;
    }

    /**
     * Returns the absolute file position where the error occurred.
     * @return the absolute file position where the error occurred
     */
    public int jsxGet_filepos() {
        return filepos_;
    }

    /**
     * Returns the line number that contains the error.
     * @return the line number that contains the error
     */
    public int jsxGet_line() {
        return line_;
    }

    /**
     * Returns the character position within the line where the error occurred.
     * @return the character position within the line where the error occurred
     */
    public int jsxGet_linepos() {
        return linepos_;
    }

    /**
     * Returns the reason for the error.
     * @return the reason for the error
     */
    public String jsxGet_reason() {
        return reason_;
    }

    /**
     * Returns the full text of the line containing the error.
     * @return the full text of the line containing the error
     */
    public String jsxGet_srcText() {
        return srcText_;
    }

    /**
     * Returns the URL of the XML document containing the last error.
     * @return the URL of the XML document containing the last error
     */
    public String jsxGet_url() {
        return url_;
    }

    void setErrorCode(final int errorCode) {
        errorCode_ = errorCode;
    }

    void setFilepos(final int filepos) {
        filepos_ = filepos;
    }

    void setLine(final int line) {
        line_ = line;
    }

    void setLinepos(final int linepos) {
        linepos_ = linepos;
    }

    void setReason(final String reason) {
        reason_ = reason;
    }

    void setSrcText(final String srcText) {
        srcText_ = srcText;
    }

    void setUrl(final String url) {
        url_ = url;
    }
}
