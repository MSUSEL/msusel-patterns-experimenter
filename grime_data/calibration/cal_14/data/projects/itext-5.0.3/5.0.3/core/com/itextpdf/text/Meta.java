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
 * This is an <CODE>Element</CODE> that contains
 * some meta information about the document.
 * <P>
 * An object of type <CODE>Meta</CODE> can not be constructed by the user.
 * User defined meta information should be placed in a <CODE>Header</CODE>-object.
 * <CODE>Meta</CODE> is reserved for: Subject, Keywords, Author, Title, Producer
 * and Creationdate information.
 *
 * @see		Element
 * @see		Header
 */

public class Meta implements Element {

    // membervariables

	/** This is the type of Meta-information this object contains. */
    private int type;

    /** This is the content of the Meta-information. */
    private StringBuffer content;

    // constructors

    /**
     * Constructs a <CODE>Meta</CODE>.
     *
     * @param	type		the type of meta-information
     * @param	content		the content
     */
    Meta(int type, String content) {
        this.type = type;
        this.content = new StringBuffer(content);
    }

    /**
     * Constructs a <CODE>Meta</CODE>.
     *
     * @param	tag		    the tagname of the meta-information
     * @param	content		the content
     */
    public Meta(String tag, String content) {
        this.type = Meta.getType(tag);
        this.content = new StringBuffer(content);
    }

    // implementation of the Element-methods

    /**
     * Processes the element by adding it (or the different parts) to a
     * <CODE>ElementListener</CODE>.
     *
     * @param	listener		the <CODE>ElementListener</CODE>
     * @return	<CODE>true</CODE> if the element was processed successfully
     */
    public boolean process(ElementListener listener) {
        try {
            return listener.add(this);
        }
        catch(DocumentException de) {
            return false;
        }
    }

    /**
     * Gets the type of the text element.
     *
     * @return	a type
     */
    public int type() {
        return type;
    }

    /**
     * Gets all the chunks in this element.
     *
     * @return	an <CODE>ArrayList</CODE>
     */
    public ArrayList<Chunk> getChunks() {
        return new ArrayList<Chunk>();
    }

	/**
	 * @see com.itextpdf.text.Element#isContent()
	 * @since	iText 2.0.8
	 */
	public boolean isContent() {
		return false;
	}

	/**
	 * @see com.itextpdf.text.Element#isNestable()
	 * @since	iText 2.0.8
	 */
	public boolean isNestable() {
		return false;
	}

    // methods

    /**
     * appends some text to this <CODE>Meta</CODE>.
     *
     * @param	string      a <CODE>String</CODE>
     * @return	a <CODE>StringBuffer</CODE>
     */
    public StringBuffer append(String string) {
        return content.append(string);
    }

    // methods to retrieve information

	/**
     * Returns the content of the meta information.
     *
     * @return	a <CODE>String</CODE>
     */
    public String getContent() {
        return content.toString();
    }

	/**
     * Returns the name of the meta information.
     *
     * @return	a <CODE>String</CODE>
     */

    public String getName() {
        switch (type) {
            case Element.SUBJECT:
                return ElementTags.SUBJECT;
            case Element.KEYWORDS:
                return ElementTags.KEYWORDS;
            case Element.AUTHOR:
                return ElementTags.AUTHOR;
            case Element.TITLE:
                return ElementTags.TITLE;
            case Element.PRODUCER:
                return ElementTags.PRODUCER;
            case Element.CREATIONDATE:
                return ElementTags.CREATIONDATE;
                default:
                    return ElementTags.UNKNOWN;
        }
    }

    /**
     * Returns the name of the meta information.
     *
     * @param tag iText tag for meta information
     * @return	the Element value corresponding with the given tag
     */
    public static int getType(String tag) {
        if (ElementTags.SUBJECT.equals(tag)) {
            return Element.SUBJECT;
        }
        if (ElementTags.KEYWORDS.equals(tag)) {
            return Element.KEYWORDS;
        }
        if (ElementTags.AUTHOR.equals(tag)) {
            return Element.AUTHOR;
        }
        if (ElementTags.TITLE.equals(tag)) {
            return Element.TITLE;
        }
        if (ElementTags.PRODUCER.equals(tag)) {
            return Element.PRODUCER;
        }
        if (ElementTags.CREATIONDATE.equals(tag)) {
            return Element.CREATIONDATE;
        }
        return Element.HEADER;
    }

}