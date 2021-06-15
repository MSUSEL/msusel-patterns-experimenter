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

/**
 * <p>A handler for attachments, which represent pages received from the server which contain
 * {@code Content-Disposition=attachment} headers. Normally pages are loaded inline: clicking on
 * a link, for example, loads the linked page in the current window. Attached pages are different
 * in that they are intended to be loaded outside of this flow: clicking on a link prompts the
 * user to either save the linked page, or open it outside of the current window, but does not
 * load the page in the current window.</p>
 *
 * <p>HtmlUnit complies with the semantics described above when an <tt>AttachmentHandler</tt> has
 * been registered with the {@link com.gargoylesoftware.htmlunit.WebClient} via
 * {@link com.gargoylesoftware.htmlunit.WebClient#setAttachmentHandler(AttachmentHandler)}. When
 * no attachment handler has been registered with the <tt>WebClient</tt>, the semantics described
 * above to not apply, and attachments are loaded inline. By default, <tt>AttachmentHandler</tt>s
 * are not registered with new <tt>WebClient</tt> instances, in order to maintain backwards
 * compatibility with HtmlUnit 2.1 and earlier. This will likely change in the future.</p>
 *
 * @version $Revision: 5301 $
 * @author Bruce Chapman
 * @author Sudhan Moghe
 * @author Daniel Gredler
 * @see com.gargoylesoftware.htmlunit.WebClient#setAttachmentHandler(AttachmentHandler)
 * @see com.gargoylesoftware.htmlunit.WebClient#getAttachmentHandler()
 * @see <a href="http://www.ietf.org/rfc/rfc2183.txt">RFC 2183</a>
 */
public interface AttachmentHandler {

    /**
     * Handles the specified attached page.
     * @param page an attached page, which doesn't get loaded inline
     */
    void handleAttachment(final Page page);

}
