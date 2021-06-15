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
package com.itextpdf.text.pdf;

import java.io.OutputStream;
import java.util.HashMap;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.AcroFields.Item;

/**
 * Allows you to add one (or more) existing PDF document(s)
 * and add the form(s) of (an)other PDF document(s).
 * @since 2.1.5
 */
class PdfCopyFormsImp extends PdfCopyFieldsImp {

    /**
   * This sets up the output document
   * @param os The Outputstream pointing to the output document
   * @throws DocumentException
   */
    PdfCopyFormsImp(OutputStream os) throws DocumentException {
        super(os);
    }

    /**
     * This method feeds in the source document
     * @param reader The PDF reader containing the source document
     * @throws DocumentException
     */
    public void copyDocumentFields(PdfReader reader) throws DocumentException {
    	if (!reader.isOpenedWithFullPermissions())
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("pdfreader.not.opened.with.owner.password"));
        if (readers2intrefs.containsKey(reader)) {
            reader = new PdfReader(reader);
        }
        else {
            if (reader.isTampered())
                throw new DocumentException(MessageLocalization.getComposedMessage("the.document.was.reused"));
            reader.consolidateNamedDestinations();
            reader.setTampered(true);
        }
        reader.shuffleSubsetNames();
        readers2intrefs.put(reader, new IntHashtable());
        fields.add(reader.getAcroFields());
        updateCalculationOrder(reader);
    }

    /**
     * This merge fields is slightly different from the mergeFields method
     * of PdfCopyFields.
     */
    @Override
    void mergeFields() {
        for (int k = 0; k < fields.size(); ++k) {
            HashMap<String, Item> fd = (fields.get(k)).getFields();
            mergeWithMaster(fd);
        }
    }

}
