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
package com.gargoylesoftware.htmlunit.attachment;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * An attachment represents a page received from the server which contains a
 * {@code Content-Disposition=attachment} header.
 *
 * @version $Revision: 5301 $
 * @author Bruce Chapman
 * @author Sudhan Moghe
 * @author Daniel Gredler
 */
public class Attachment {

    /** The attached page. */
    private final Page page_;

    /**
     * Creates a new attachment for the specified page.
     * @param page the attached page
     */
    public Attachment(final Page page) {
        page_ = page;
    }

    /**
     * Returns the attached page.
     * @return the attached page
     */
    public Page getPage() {
        return page_;
    }

    /**
     * Returns the attachment's filename, as suggested by the <tt>Content-Disposition</tt>
     * header, or <tt>null</tt> if no filename was suggested.
     * @return the attachment's suggested filename, or <tt>null</tt> if none was suggested
     */
    public String getSuggestedFilename() {
        final WebResponse response = page_.getWebResponse();
        final String disp = response.getResponseHeaderValue("Content-Disposition");
        int start = disp.indexOf("filename=");
        if (start == -1) {
            return null;
        }
        start += "filename=".length();
        int end = disp.indexOf(";", start);
        if (end == -1) {
            end = disp.length();
        }
        if (disp.charAt(start) == '"' && disp.charAt(end - 1) == '"') {
            start++;
            end--;
        }
        return disp.substring(start, end);
    }

    /**
     * Returns <tt>true</tt> if the specified response represents an attachment.
     * @param response the response to check
     * @return <tt>true</tt> if the specified response represents an attachment, <tt>false</tt> otherwise
     * @see <a href="http://www.ietf.org/rfc/rfc2183.txt">RFC 2183</a>
     */
    public static boolean isAttachment(final WebResponse response) {
        final String disp = response.getResponseHeaderValue("Content-Disposition");
        if (disp == null) {
            return false;
        }
        return disp.startsWith("attachment");
    }

}
