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
package com.itextpdf.text.pdf.richmedia;

import com.itextpdf.text.exceptions.IllegalPdfSyntaxException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;

/**
 * A video file can contain cue points that are encoded in a video stream
 * or may be created by an associated ActionScript within the Flash content.
 * The CuePoint dictionary contains a state that relates the cue points to
 * an action that may be passed to the conforming application or may be used
 * to change the appearance. Cue points in the Flash content are matched to
 * the cue points declared in the PDF file by the values specified by the
 * Name or Time keys. (See ExtensionLevel 3 p91)
 * @since	5.0.0
 */
public class CuePoint extends PdfDictionary {

	/**
	 * Constructs a CuePoint object.
	 * A <code>Navigation</code> cue point is an event encoded in a Flash movie (FLV).
	 * A chapter stop may be encoded so that when the user requests to go to or skip
	 * a chapter, a navigation cue point is used to indicate the location of the chapter.
	 * An <code>Event</code> is a generic cue point of no specific significance other
	 * than a corresponding action is triggered.
	 * @param	subtype	possible values: PdfName.NAVIGATION or PdfName.EVENT
	 */
	public CuePoint(PdfName subtype) {
		super(PdfName.CUEPOINT);
		put(PdfName.SUBTYPE, subtype);
	}
	
	/**
	 * Set the name of the cue point to match against the cue point within
	 * Flash content and for display purposes.
	 * @param	name	the name of the cue point
	 */
	public void setName(PdfString name) {
		put(PdfName.NAME, name);
	}
	
	/**
	 * Sets the time value of the cue point in milliseconds to match against
	 * the cue point within Flash content and for display purposes.
	 * @param	time	the time value of the cue point
	 */
	public void setTime(int time) {
		put(PdfName.TIME, new PdfNumber(time));
	}

	/**
	 * Sets an action dictionary defining the action that is executed
	 * if this cue point is triggered, meaning that the Flash content
	 * reached the matching cue point during its playback.
	 * @param	action	an action
	 */
	public void setAction(PdfObject action) {
		if (action instanceof PdfDictionary || action instanceof PdfIndirectReference)
			put(PdfName.A, action);
		else
			throw new IllegalPdfSyntaxException(
				"An action should be defined as a dictionary");
	}
}
