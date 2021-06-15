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
package com.itextpdf.text.pdf.events;

import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * If you want to add more than one page event to a PdfWriter,
 * you have to construct a PdfPageEventForwarder, add the
 * different events to this object and add the forwarder to
 * the PdfWriter.
 */

public class PdfPageEventForwarder implements PdfPageEvent {

	/** ArrayList containing all the PageEvents that have to be executed. */
	protected ArrayList<PdfPageEvent> events = new ArrayList<PdfPageEvent>();

	/**
	 * Add a page event to the forwarder.
	 * @param event an event that has to be added to the forwarder.
	 */
	public void addPageEvent(PdfPageEvent event) {
		events.add(event);
	}

	/**
	 * Called when the document is opened.
	 *
	 * @param writer
	 *            the <CODE>PdfWriter</CODE> for this document
	 * @param document
	 *            the document
	 */
	public void onOpenDocument(PdfWriter writer, Document document) {
		for (PdfPageEvent event: events) {
			event.onOpenDocument(writer, document);
		}
	}

	/**
	 * Called when a page is initialized.
	 * <P>
	 * Note that if even if a page is not written this method is still called.
	 * It is preferable to use <CODE>onEndPage</CODE> to avoid infinite loops.
	 *
	 * @param writer
	 *            the <CODE>PdfWriter</CODE> for this document
	 * @param document
	 *            the document
	 */
	public void onStartPage(PdfWriter writer, Document document) {
		for (PdfPageEvent event: events) {
			event.onStartPage(writer, document);
		}
	}

	/**
	 * Called when a page is finished, just before being written to the
	 * document.
	 *
	 * @param writer
	 *            the <CODE>PdfWriter</CODE> for this document
	 * @param document
	 *            the document
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		for (PdfPageEvent event: events) {
			event.onEndPage(writer, document);
		}
	}

	/**
	 * Called when the document is closed.
	 * <P>
	 * Note that this method is called with the page number equal to the last
	 * page plus one.
	 *
	 * @param writer
	 *            the <CODE>PdfWriter</CODE> for this document
	 * @param document
	 *            the document
	 */
	public void onCloseDocument(PdfWriter writer, Document document) {
		for (PdfPageEvent event: events) {
			event.onCloseDocument(writer, document);
		}
	}

	/**
	 * Called when a Paragraph is written.
	 * <P>
	 * <CODE>paragraphPosition</CODE> will hold the height at which the
	 * paragraph will be written to. This is useful to insert bookmarks with
	 * more control.
	 *
	 * @param writer
	 *            the <CODE>PdfWriter</CODE> for this document
	 * @param document
	 *            the document
	 * @param paragraphPosition
	 *            the position the paragraph will be written to
	 */
	public void onParagraph(PdfWriter writer, Document document,
			float paragraphPosition) {
		for (PdfPageEvent event: events) {
			event.onParagraph(writer, document, paragraphPosition);
		}
	}

	/**
	 * Called when a Paragraph is written.
	 * <P>
	 * <CODE>paragraphPosition</CODE> will hold the height of the end of the
	 * paragraph.
	 *
	 * @param writer
	 *            the <CODE>PdfWriter</CODE> for this document
	 * @param document
	 *            the document
	 * @param paragraphPosition
	 *            the position of the end of the paragraph
	 */
	public void onParagraphEnd(PdfWriter writer, Document document,
			float paragraphPosition) {
		for (PdfPageEvent event: events) {
			event.onParagraphEnd(writer, document, paragraphPosition);
		}
	}

	/**
	 * Called when a Chapter is written.
	 * <P>
	 * <CODE>position</CODE> will hold the height at which the chapter will be
	 * written to.
	 *
	 * @param writer
	 *            the <CODE>PdfWriter</CODE> for this document
	 * @param document
	 *            the document
	 * @param paragraphPosition
	 *            the position the chapter will be written to
	 * @param title
	 *            the title of the Chapter
	 */
	public void onChapter(PdfWriter writer, Document document,
			float paragraphPosition, Paragraph title) {
		for (PdfPageEvent event: events) {
			event.onChapter(writer, document, paragraphPosition, title);
		}
	}

	/**
	 * Called when the end of a Chapter is reached.
	 * <P>
	 * <CODE>position</CODE> will hold the height of the end of the chapter.
	 *
	 * @param writer
	 *            the <CODE>PdfWriter</CODE> for this document
	 * @param document
	 *            the document
	 * @param position
	 *            the position of the end of the chapter.
	 */
	public void onChapterEnd(PdfWriter writer, Document document, float position) {
		for (PdfPageEvent event: events) {
			event.onChapterEnd(writer, document, position);
		}
	}

	/**
	 * Called when a Section is written.
	 * <P>
	 * <CODE>position</CODE> will hold the height at which the section will be
	 * written to.
	 *
	 * @param writer
	 *            the <CODE>PdfWriter</CODE> for this document
	 * @param document
	 *            the document
	 * @param paragraphPosition
	 *            the position the section will be written to
	 * @param depth
	 *            the number depth of the Section
	 * @param title
	 *            the title of the section
	 */
	public void onSection(PdfWriter writer, Document document,
			float paragraphPosition, int depth, Paragraph title) {
		for (PdfPageEvent event: events) {
			event.onSection(writer, document, paragraphPosition, depth, title);
		}
	}

	/**
	 * Called when the end of a Section is reached.
	 * <P>
	 * <CODE>position</CODE> will hold the height of the section end.
	 *
	 * @param writer
	 *            the <CODE>PdfWriter</CODE> for this document
	 * @param document
	 *            the document
	 * @param position
	 *            the position of the end of the section
	 */
	public void onSectionEnd(PdfWriter writer, Document document, float position) {
		for (PdfPageEvent event: events) {
			event.onSectionEnd(writer, document, position);
		}
	}

	/**
	 * Called when a <CODE>Chunk</CODE> with a generic tag is written.
	 * <P>
	 * It is useful to pinpoint the <CODE>Chunk</CODE> location to generate
	 * bookmarks, for example.
	 *
	 * @param writer
	 *            the <CODE>PdfWriter</CODE> for this document
	 * @param document
	 *            the document
	 * @param rect
	 *            the <CODE>Rectangle</CODE> containing the <CODE>Chunk
	 *            </CODE>
	 * @param text
	 *            the text of the tag
	 */
	public void onGenericTag(PdfWriter writer, Document document,
			Rectangle rect, String text) {
		PdfPageEvent event;
		for (Object element : events) {
			event = (PdfPageEvent)element;
			event.onGenericTag(writer, document, rect, text);
		}
	}
}