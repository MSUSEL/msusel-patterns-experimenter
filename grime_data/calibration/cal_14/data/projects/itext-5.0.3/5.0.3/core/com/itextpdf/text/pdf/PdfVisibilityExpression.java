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

import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * An array specifying a visibility expression, used to compute visibility
 * of content based on a set of optional content groups.
 * @since 5.0.2
 */
public class PdfVisibilityExpression extends PdfArray {

	/** A boolean operator. */
	public static final int OR = 0;
	/** A boolean operator. */
	public static final int AND = 1;
	/** A boolean operator. */
	public static final int NOT = -1;
	
	/**
	 * Creates a visibility expression.
	 * @param type should be AND, OR, or NOT
	 */
	public PdfVisibilityExpression(int type) {
		super();
		switch(type) {
		case OR:
			super.add(PdfName.OR);
			break;
		case AND:
			super.add(PdfName.AND);
			break;
		case NOT:
			super.add(PdfName.NOT);
			break;
		default:
			throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.ve.value"));	
		} 
	}

	/**
	 * @see com.itextpdf.text.pdf.PdfArray#add(int, com.itextpdf.text.pdf.PdfObject)
	 */
	@Override
	public void add(int index, PdfObject element) {
		throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.ve.value"));
	}

	/**
	 * @see com.itextpdf.text.pdf.PdfArray#add(com.itextpdf.text.pdf.PdfObject)
	 */
	@Override
	public boolean add(PdfObject object) {
		if (object instanceof PdfLayer)
			return super.add(((PdfLayer)object).getRef());
		if (object instanceof PdfVisibilityExpression)
			return super.add(object);
		throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.ve.value"));
	}

	/**
	 * @see com.itextpdf.text.pdf.PdfArray#addFirst(com.itextpdf.text.pdf.PdfObject)
	 */
	@Override
	public void addFirst(PdfObject object) {
		throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.ve.value"));
	}

	/**
	 * @see com.itextpdf.text.pdf.PdfArray#add(float[])
	 */
	@Override
	public boolean add(float[] values) {
		throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.ve.value"));
	}

	/**
	 * @see com.itextpdf.text.pdf.PdfArray#add(int[])
	 */
	@Override
	public boolean add(int[] values) {
		throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.ve.value"));
	}
	
}
