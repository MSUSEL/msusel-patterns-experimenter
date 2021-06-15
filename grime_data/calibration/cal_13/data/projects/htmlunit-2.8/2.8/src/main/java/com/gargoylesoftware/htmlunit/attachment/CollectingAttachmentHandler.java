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

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebAssert;

/**
 * An {@link AttachmentHandler} implementation which creates an {@link Attachment} for
 * each attached page, collecting all created attachments into a list.
 *
 * @version $Revision: 5301 $
 * @author Bruce Chapman
 * @author Sudhan Moghe
 * @author Daniel Gredler
 */
public class CollectingAttachmentHandler implements AttachmentHandler {

    private final List<Attachment> collectedAttachments_;

    /**
     * Creates a new instance.
     */
    public CollectingAttachmentHandler() {
        this(new ArrayList<Attachment>());
    }

    /**
     * Creates a new instance which collects attachments into the specified list.
     * @param list the list to store attachments in
     */
    public CollectingAttachmentHandler(final List<Attachment> list) {
        WebAssert.notNull("list", list);
        collectedAttachments_ = list;
    }

    /**
     * {@inheritDoc}
     */
    public void handleAttachment(final Page page) {
        collectedAttachments_.add(new Attachment(page));
    }

    /**
     * Returns the list of attachments collected by this attachment handler. The returned
     * list is modifiable, so that attachments can be removed after being processed.
     * @return the list of attachments collected by this attachment handler
     */
    public List<Attachment> getCollectedAttachments() {
        return collectedAttachments_;
    }

}
