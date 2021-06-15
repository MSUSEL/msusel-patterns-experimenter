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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An <CODE>Annotation</CODE> is a little note that can be added to a page on
 * a document.
 *
 * @see Element
 * @see Anchor
 */

public class Annotation implements Element {

	// membervariables

	/** This is a possible annotation type. */
	public static final int TEXT = 0;

	/** This is a possible annotation type. */
	public static final int URL_NET = 1;

	/** This is a possible annotation type. */
	public static final int URL_AS_STRING = 2;

	/** This is a possible annotation type. */
	public static final int FILE_DEST = 3;

	/** This is a possible annotation type. */
	public static final int FILE_PAGE = 4;

	/** This is a possible annotation type. */
	public static final int NAMED_DEST = 5;

	/** This is a possible annotation type. */
	public static final int LAUNCH = 6;

	/** This is a possible annotation type. */
	public static final int SCREEN = 7;

	/** This is a possible attribute. */
	public static final String TITLE = "title";

	/** This is a possible attribute. */
	public static final String CONTENT = "content";

	/** This is a possible attribute. */
	public static final String URL = "url";

	/** This is a possible attribute. */
	public static final String FILE = "file";

	/** This is a possible attribute. */
	public static final String DESTINATION = "destination";

	/** This is a possible attribute. */
	public static final String PAGE = "page";

	/** This is a possible attribute. */
	public static final String NAMED = "named";

	/** This is a possible attribute. */
	public static final String APPLICATION = "application";

	/** This is a possible attribute. */
	public static final String PARAMETERS = "parameters";

	/** This is a possible attribute. */
	public static final String OPERATION = "operation";

	/** This is a possible attribute. */
	public static final String DEFAULTDIR = "defaultdir";

	/** This is a possible attribute. */
	public static final String LLX = "llx";

	/** This is a possible attribute. */
	public static final String LLY = "lly";

	/** This is a possible attribute. */
	public static final String URX = "urx";

	/** This is a possible attribute. */
	public static final String URY = "ury";

	/** This is a possible attribute. */
	public static final String MIMETYPE = "mime";

	/** This is the type of annotation. */
	protected int annotationtype;

	/** This is the title of the <CODE>Annotation</CODE>. */
	protected HashMap<String, Object> annotationAttributes = new HashMap<String, Object>();

	/** This is the lower left x-value */
	protected float llx = Float.NaN;

	/** This is the lower left y-value */
	protected float lly = Float.NaN;

	/** This is the upper right x-value */
	protected float urx = Float.NaN;

	/** This is the upper right y-value */
	protected float ury = Float.NaN;

	// constructors

	/**
	 * Constructs an <CODE>Annotation</CODE> with a certain title and some
	 * text.
	 *
	 * @param llx
	 *            lower left x coordinate
	 * @param lly
	 *            lower left y coordinate
	 * @param urx
	 *            upper right x coordinate
	 * @param ury
	 *            upper right y coordinate
	 */
	private Annotation(float llx, float lly, float urx, float ury) {
		this.llx = llx;
		this.lly = lly;
		this.urx = urx;
		this.ury = ury;
	}

	/**
	 * Copy constructor.
	 */
    public Annotation(Annotation an) {
        annotationtype = an.annotationtype;
        annotationAttributes = an.annotationAttributes;
        llx = an.llx;
        lly = an.lly;
        urx = an.urx;
        ury = an.ury;
    }

	/**
	 * Constructs an <CODE>Annotation</CODE> with a certain title and some
	 * text.
	 *
	 * @param title
	 *            the title of the annotation
	 * @param text
	 *            the content of the annotation
	 */
	public Annotation(String title, String text) {
		annotationtype = TEXT;
		annotationAttributes.put(TITLE, title);
		annotationAttributes.put(CONTENT, text);
	}

	/**
	 * Constructs an <CODE>Annotation</CODE> with a certain title and some
	 * text.
	 *
	 * @param title
	 *            the title of the annotation
	 * @param text
	 *            the content of the annotation
	 * @param llx
	 *            the lower left x-value
	 * @param lly
	 *            the lower left y-value
	 * @param urx
	 *            the upper right x-value
	 * @param ury
	 *            the upper right y-value
	 */
	public Annotation(String title, String text, float llx, float lly,
			float urx, float ury) {
		this(llx, lly, urx, ury);
		annotationtype = TEXT;
		annotationAttributes.put(TITLE, title);
		annotationAttributes.put(CONTENT, text);
	}

	/**
	 * Constructs an <CODE>Annotation</CODE>.
	 *
	 * @param llx
	 *            the lower left x-value
	 * @param lly
	 *            the lower left y-value
	 * @param urx
	 *            the upper right x-value
	 * @param ury
	 *            the upper right y-value
	 * @param url
	 *            the external reference
	 */
	public Annotation(float llx, float lly, float urx, float ury, URL url) {
		this(llx, lly, urx, ury);
		annotationtype = URL_NET;
		annotationAttributes.put(URL, url);
	}

	/**
	 * Constructs an <CODE>Annotation</CODE>.
	 *
	 * @param llx
	 *            the lower left x-value
	 * @param lly
	 *            the lower left y-value
	 * @param urx
	 *            the upper right x-value
	 * @param ury
	 *            the upper right y-value
	 * @param url
	 *            the external reference
	 */
	public Annotation(float llx, float lly, float urx, float ury, String url) {
		this(llx, lly, urx, ury);
		annotationtype = URL_AS_STRING;
		annotationAttributes.put(FILE, url);
	}

	/**
	 * Constructs an <CODE>Annotation</CODE>.
	 *
	 * @param llx
	 *            the lower left x-value
	 * @param lly
	 *            the lower left y-value
	 * @param urx
	 *            the upper right x-value
	 * @param ury
	 *            the upper right y-value
	 * @param file
	 *            an external PDF file
	 * @param dest
	 *            the destination in this file
	 */
	public Annotation(float llx, float lly, float urx, float ury, String file,
			String dest) {
		this(llx, lly, urx, ury);
		annotationtype = FILE_DEST;
		annotationAttributes.put(FILE, file);
		annotationAttributes.put(DESTINATION, dest);
	}

	/**
	 * Creates a Screen annotation to embed media clips
	 *
	 * @param llx
	 * @param lly
	 * @param urx
	 * @param ury
	 * @param moviePath
	 *            path to the media clip file
	 * @param mimeType
	 *            mime type of the media
	 * @param showOnDisplay
	 *            if true play on display of the page
	 */
	public Annotation(float llx, float lly, float urx, float ury,
			String moviePath, String mimeType, boolean showOnDisplay) {
		this(llx, lly, urx, ury);
		annotationtype = SCREEN;
		annotationAttributes.put(FILE, moviePath);
		annotationAttributes.put(MIMETYPE, mimeType);
		annotationAttributes.put(PARAMETERS, new boolean[] {
				false /* embedded */, showOnDisplay });
	}

	/**
	 * Constructs an <CODE>Annotation</CODE>.
	 *
	 * @param llx
	 *            the lower left x-value
	 * @param lly
	 *            the lower left y-value
	 * @param urx
	 *            the upper right x-value
	 * @param ury
	 *            the upper right y-value
	 * @param file
	 *            an external PDF file
	 * @param page
	 *            a page number in this file
	 */
	public Annotation(float llx, float lly, float urx, float ury, String file,
			int page) {
		this(llx, lly, urx, ury);
		annotationtype = FILE_PAGE;
		annotationAttributes.put(FILE, file);
		annotationAttributes.put(PAGE, new Integer(page));
	}

	/**
	 * Constructs an <CODE>Annotation</CODE>.
	 *
	 * @param llx
	 *            the lower left x-value
	 * @param lly
	 *            the lower left y-value
	 * @param urx
	 *            the upper right x-value
	 * @param ury
	 *            the upper right y-value
	 * @param named
	 *            a named destination in this file
	 */
	public Annotation(float llx, float lly, float urx, float ury, int named) {
		this(llx, lly, urx, ury);
		annotationtype = NAMED_DEST;
		annotationAttributes.put(NAMED, new Integer(named));
	}

	/**
	 * Constructs an <CODE>Annotation</CODE>.
	 *
	 * @param llx
	 *            the lower left x-value
	 * @param lly
	 *            the lower left y-value
	 * @param urx
	 *            the upper right x-value
	 * @param ury
	 *            the upper right y-value
	 * @param application
	 *            an external application
	 * @param parameters
	 *            parameters to pass to this application
	 * @param operation
	 *            the operation to pass to this application
	 * @param defaultdir
	 *            the default directory to run this application in
	 */
	public Annotation(float llx, float lly, float urx, float ury,
			String application, String parameters, String operation,
			String defaultdir) {
		this(llx, lly, urx, ury);
		annotationtype = LAUNCH;
		annotationAttributes.put(APPLICATION, application);
		annotationAttributes.put(PARAMETERS, parameters);
		annotationAttributes.put(OPERATION, operation);
		annotationAttributes.put(DEFAULTDIR, defaultdir);
	}

	// implementation of the Element-methods

	/**
	 * Gets the type of the text element.
	 *
	 * @return a type
	 */
	public int type() {
		return Element.ANNOTATION;
	}

	/**
	 * Processes the element by adding it (or the different parts) to an <CODE>
	 * ElementListener</CODE>.
	 *
	 * @param listener
	 *            an <CODE>ElementListener</CODE>
	 * @return <CODE>true</CODE> if the element was processed successfully
	 */
	public boolean process(ElementListener listener) {
		try {
			return listener.add(this);
		} catch (DocumentException de) {
			return false;
		}
	}

	/**
	 * Gets all the chunks in this element.
	 *
	 * @return an <CODE>ArrayList</CODE>
	 */

	public ArrayList<Chunk> getChunks() {
		return new ArrayList<Chunk>();
	}

	// methods

	/**
	 * Sets the dimensions of this annotation.
	 *
	 * @param llx
	 *            the lower left x-value
	 * @param lly
	 *            the lower left y-value
	 * @param urx
	 *            the upper right x-value
	 * @param ury
	 *            the upper right y-value
	 */
	public void setDimensions(float llx, float lly, float urx, float ury) {
		this.llx = llx;
		this.lly = lly;
		this.urx = urx;
		this.ury = ury;
	}

	// methods to retrieve information

	/**
	 * Returns the lower left x-value.
	 *
	 * @return a value
	 */
	public float llx() {
		return llx;
	}

	/**
	 * Returns the lower left y-value.
	 *
	 * @return a value
	 */
	public float lly() {
		return lly;
	}

	/**
	 * Returns the upper right x-value.
	 *
	 * @return a value
	 */
	public float urx() {
		return urx;
	}

	/**
	 * Returns the upper right y-value.
	 *
	 * @return a value
	 */
	public float ury() {
		return ury;
	}

	/**
	 * Returns the lower left x-value.
	 *
	 * @param def
	 *            the default value
	 * @return a value
	 */
	public float llx(float def) {
		if (Float.isNaN(llx))
			return def;
		return llx;
	}

	/**
	 * Returns the lower left y-value.
	 *
	 * @param def
	 *            the default value
	 * @return a value
	 */
	public float lly(float def) {
		if (Float.isNaN(lly))
			return def;
		return lly;
	}

	/**
	 * Returns the upper right x-value.
	 *
	 * @param def
	 *            the default value
	 * @return a value
	 */
	public float urx(float def) {
		if (Float.isNaN(urx))
			return def;
		return urx;
	}

	/**
	 * Returns the upper right y-value.
	 *
	 * @param def
	 *            the default value
	 * @return a value
	 */
	public float ury(float def) {
		if (Float.isNaN(ury))
			return def;
		return ury;
	}

	/**
	 * Returns the type of this <CODE>Annotation</CODE>.
	 *
	 * @return a type
	 */
	public int annotationType() {
		return annotationtype;
	}

	/**
	 * Returns the title of this <CODE>Annotation</CODE>.
	 *
	 * @return a name
	 */
	public String title() {
		String s = (String) annotationAttributes.get(TITLE);
		if (s == null)
			s = "";
		return s;
	}

	/**
	 * Gets the content of this <CODE>Annotation</CODE>.
	 *
	 * @return a reference
	 */
	public String content() {
		String s = (String) annotationAttributes.get(CONTENT);
		if (s == null)
			s = "";
		return s;
	}

	/**
	 * Gets the content of this <CODE>Annotation</CODE>.
	 *
	 * @return a reference
	 */
	public HashMap<String, Object> attributes() {
		return annotationAttributes;
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