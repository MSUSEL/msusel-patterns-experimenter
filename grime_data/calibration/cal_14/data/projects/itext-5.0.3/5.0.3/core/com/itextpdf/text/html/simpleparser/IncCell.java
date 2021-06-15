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
package com.itextpdf.text.html.simpleparser;

import java.util.ArrayList;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.TextElementArray;
import com.itextpdf.text.html.Markup;
import com.itextpdf.text.pdf.PdfPCell;
/**
 *
 * @author  psoares
 */
public class IncCell implements TextElementArray {

    private ArrayList<Chunk> chunks = new ArrayList<Chunk>();
    private PdfPCell cell;

    /** Creates a new instance of IncCell */
    public IncCell(String tag, ChainedProperties props) {
        cell = new PdfPCell((Phrase)null);
        String value = props.getProperty("colspan");
        if (value != null)
            cell.setColspan(Integer.parseInt(value));
        value = props.getProperty("rowspan");
        if (value != null)
            cell.setRowspan(Integer.parseInt(value));
        value = props.getProperty("align");
        if (tag.equals("th"))
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        if (value != null) {
            if ("center".equalsIgnoreCase(value))
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            else if ("right".equalsIgnoreCase(value))
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            else if ("left".equalsIgnoreCase(value))
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            else if ("justify".equalsIgnoreCase(value))
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        }
        value = props.getProperty("valign");
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        if (value != null) {
            if ("top".equalsIgnoreCase(value))
                cell.setVerticalAlignment(Element.ALIGN_TOP);
            else if ("bottom".equalsIgnoreCase(value))
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        }
        value = props.getProperty("border");
        float border = 0;
        if (value != null)
            border = Float.parseFloat(value);
        cell.setBorderWidth(border);
        value = props.getProperty("cellpadding");
        if (value != null)
            cell.setPadding(Float.parseFloat(value));
        cell.setUseDescender(true);
        value = props.getProperty("bgcolor");
        cell.setBackgroundColor(Markup.decodeColor(value));
    }

    public boolean add(Element o) {
        cell.addElement(o);
        return true;
    }

    public ArrayList<Chunk> getChunks() {
        return chunks;
    }

    public boolean process(ElementListener listener) {
        return true;
    }

    public int type() {
        return Element.RECTANGLE;
    }

    public PdfPCell getCell() {
        return cell;
    }

	/**
	 * @see com.itextpdf.text.Element#isContent()
	 * @since	iText 2.0.8
	 */
	public boolean isContent() {
		return true;
	}

	/**
	 * @see com.itextpdf.text.Element#isNestable()
	 * @since	iText 2.0.8
	 */
	public boolean isNestable() {
		return true;
	}
}