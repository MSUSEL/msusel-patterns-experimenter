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
package com.itextpdf.text;

import java.util.ArrayList;

/**
 * Interface for a text element.
 * <P>
 * Remark: I looked at the interface javax.swing.text.Element, but I decided to
 * write my own text-classes for two reasons:
 * <OL>
 * <LI>The javax.swing.text-classes may be very generic, I think they are
 * overkill: they are to heavy for what they have to do.
 * <LI>A lot of people using iText (formerly known as rugPdf), still use
 * JDK1.1.x. I try to keep the Java2 requirements limited to the Collection
 * classes (I think they're really great). However, if I use the
 * javax.swing.text classes, it will become very difficult to downgrade rugPdf.
 * </OL>
 *
 * @see Anchor
 * @see Chapter
 * @see Chunk
 * @see Header
 * @see Image
 * @see Jpeg
 * @see List
 * @see ListItem
 * @see Meta
 * @see Paragraph
 * @see Phrase
 * @see Rectangle
 * @see Section
 */

public interface Element {

	// static membervariables (meta information)

	/** This is a possible type of <CODE>Element</CODE>. */
	public static final int HEADER = 0;

	/** This is a possible type of <CODE>Element</CODE>. */
	public static final int TITLE = 1;

	/** This is a possible type of <CODE>Element</CODE>. */
	public static final int SUBJECT = 2;

	/** This is a possible type of <CODE>Element</CODE>. */
	public static final int KEYWORDS = 3;

	/** This is a possible type of <CODE>Element </CIDE>. */
	public static final int AUTHOR = 4;

	/** This is a possible type of <CODE>Element </CIDE>. */
	public static final int PRODUCER = 5;

	/** This is a possible type of <CODE>Element </CIDE>. */
	public static final int CREATIONDATE = 6;

	/** This is a possible type of <CODE>Element </CIDE>. */
	public static final int CREATOR = 7;

	// static membervariables (content)

	/** This is a possible type of <CODE>Element</CODE>. */
	public static final int CHUNK = 10;

	/** This is a possible type of <CODE>Element</CODE>. */
	public static final int PHRASE = 11;

	/** This is a possible type of <CODE>Element</CODE>. */
	public static final int PARAGRAPH = 12;

	/** This is a possible type of <CODE>Element</CODE> */
	public static final int SECTION = 13;

	/** This is a possible type of <CODE>Element</CODE> */
	public static final int LIST = 14;

	/** This is a possible type of <CODE>Element</CODE> */
	public static final int LISTITEM = 15;

	/** This is a possible type of <CODE>Element</CODE> */
	public static final int CHAPTER = 16;

	/** This is a possible type of <CODE>Element</CODE> */
	public static final int ANCHOR = 17;

	// static membervariables (tables)

	/** This is a possible type of <CODE>Element</CODE>. */
	public static final int PTABLE = 23;

	// static membervariables (annotations)

	/** This is a possible type of <CODE>Element</CODE>. */
	public static final int ANNOTATION = 29;

	// static membervariables (geometric figures)

	/** This is a possible type of <CODE>Element</CODE>. */
	public static final int RECTANGLE = 30;

	/** This is a possible type of <CODE>Element</CODE>. */
	public static final int JPEG = 32;

	/** This is a possible type of <CODE>Element</CODE>. */
	public static final int JPEG2000 = 33;

	/** This is a possible type of <CODE>Element</CODE>. */
	public static final int IMGRAW = 34;

	/** This is a possible type of <CODE>Element</CODE>. */
	public static final int IMGTEMPLATE = 35;

	/**
	 * This is a possible type of <CODE>Element</CODE>.
	 * @since	2.1.5
	 */
	public static final int JBIG2 = 36;

	/** This is a possible type of <CODE>Element</CODE>. */
	public static final int MULTI_COLUMN_TEXT = 40;

	/** This is a possible type of <CODE>Element</CODE>. */
	public static final int MARKED = 50;

	/** This is a possible type of <CODE>Element</CODE>.
	 * @since 2.1.2
	 */
	public static final int YMARK = 55;

	// static membervariables (alignment)

	/**
	 * A possible value for paragraph alignment. This specifies that the text is
	 * aligned to the left indent and extra whitespace should be placed on the
	 * right.
	 */
	public static final int ALIGN_UNDEFINED = -1;

	/**
	 * A possible value for paragraph alignment. This specifies that the text is
	 * aligned to the left indent and extra whitespace should be placed on the
	 * right.
	 */
	public static final int ALIGN_LEFT = 0;

	/**
	 * A possible value for paragraph alignment. This specifies that the text is
	 * aligned to the center and extra whitespace should be placed equally on
	 * the left and right.
	 */
	public static final int ALIGN_CENTER = 1;

	/**
	 * A possible value for paragraph alignment. This specifies that the text is
	 * aligned to the right indent and extra whitespace should be placed on the
	 * left.
	 */
	public static final int ALIGN_RIGHT = 2;

	/**
	 * A possible value for paragraph alignment. This specifies that extra
	 * whitespace should be spread out through the rows of the paragraph with
	 * the text lined up with the left and right indent except on the last line
	 * which should be aligned to the left.
	 */
	public static final int ALIGN_JUSTIFIED = 3;

	/**
	 * A possible value for vertical alignment.
	 */

	public static final int ALIGN_TOP = 4;

	/**
	 * A possible value for vertical alignment.
	 */

	public static final int ALIGN_MIDDLE = 5;

	/**
	 * A possible value for vertical alignment.
	 */

	public static final int ALIGN_BOTTOM = 6;

	/**
	 * A possible value for vertical alignment.
	 */
	public static final int ALIGN_BASELINE = 7;

	/**
	 * Does the same as ALIGN_JUSTIFIED but the last line is also spread out.
	 */
	public static final int ALIGN_JUSTIFIED_ALL = 8;

	// static member variables for CCITT compression

	/**
	 * Pure two-dimensional encoding (Group 4)
	 */
	public static final int CCITTG4 = 0x100;

	/**
	 * Pure one-dimensional encoding (Group 3, 1-D)
	 */
	public static final int CCITTG3_1D = 0x101;

	/**
	 * Mixed one- and two-dimensional encoding (Group 3, 2-D)
	 */
	public static final int CCITTG3_2D = 0x102;

	/**
	 * A flag indicating whether 1-bits are to be interpreted as black pixels
	 * and 0-bits as white pixels,
	 */
	public static final int CCITT_BLACKIS1 = 1;

	/**
	 * A flag indicating whether the filter expects extra 0-bits before each
	 * encoded line so that the line begins on a byte boundary.
	 */
	public static final int CCITT_ENCODEDBYTEALIGN = 2;

	/**
	 * A flag indicating whether end-of-line bit patterns are required to be
	 * present in the encoding.
	 */
	public static final int CCITT_ENDOFLINE = 4;

	/**
	 * A flag indicating whether the filter expects the encoded data to be
	 * terminated by an end-of-block pattern, overriding the Rows parameter. The
	 * use of this flag will set the key /EndOfBlock to false.
	 */
	public static final int CCITT_ENDOFBLOCK = 8;

	// methods

	/**
	 * Processes the element by adding it (or the different parts) to an <CODE>
	 * ElementListener</CODE>.
	 *
	 * @param listener
	 *            an <CODE>ElementListener</CODE>
	 * @return <CODE>true</CODE> if the element was processed successfully
	 */

	public boolean process(ElementListener listener);

	/**
	 * Gets the type of the text element.
	 *
	 * @return a type
	 */

	public int type();

	/**
	 * Checks if this element is a content object.
	 * If not, it's a metadata object.
	 * @since	iText 2.0.8
	 * @return	true if this is a 'content' element; false if this is a 'metadata' element
	 */

	public boolean isContent();

	/**
	 * Checks if this element is nestable.
	 * @since	iText 2.0.8
	 * @return	true if this element can be nested inside other elements.
	 */

	public boolean isNestable();

	/**
	 * Gets all the chunks in this element.
	 *
	 * @return an <CODE>ArrayList</CODE>
	 */

	public ArrayList<Chunk> getChunks();

	/**
	 * Gets the content of the text element.
	 *
	 * @return a type
	 */

	public String toString();
}