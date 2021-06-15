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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * An <CODE>Anchor</CODE> can be a reference or a destination of a reference.
 * <P>
 * An <CODE>Anchor</CODE> is a special kind of <CODE>Phrase</CODE>.
 * It is constructed in the same way.
 * <P>
 * Example:
 * <BLOCKQUOTE><PRE>
 * <STRONG>Anchor anchor = new Anchor("this is a link");</STRONG>
 * <STRONG>anchor.setName("LINK");</STRONG>
 * <STRONG>anchor.setReference("http://www.lowagie.com");</STRONG>
 * </PRE></BLOCKQUOTE>
 *
 * @see		Element
 * @see		Phrase
 */

public class Anchor extends Phrase {

	// constant
	private static final long serialVersionUID = -852278536049236911L;

    // membervariables

	/** This is the name of the <CODE>Anchor</CODE>. */
    protected String name = null;

    /** This is the reference of the <CODE>Anchor</CODE>. */
    protected String reference = null;

    // constructors

    /**
     * Constructs an <CODE>Anchor</CODE> without specifying a leading.
     */
    public Anchor() {
        super(16);
    }

    /**
     * Constructs an <CODE>Anchor</CODE> with a certain leading.
     *
     * @param	leading		the leading
     */

    public Anchor(float leading) {
        super(leading);
    }

    /**
     * Constructs an <CODE>Anchor</CODE> with a certain <CODE>Chunk</CODE>.
     *
     * @param	chunk		a <CODE>Chunk</CODE>
     */
    public Anchor(Chunk chunk) {
        super(chunk);
    }

    /**
     * Constructs an <CODE>Anchor</CODE> with a certain <CODE>String</CODE>.
     *
     * @param	string		a <CODE>String</CODE>
     */
    public Anchor(String string) {
        super(string);
    }

    /**
     * Constructs an <CODE>Anchor</CODE> with a certain <CODE>String</CODE>
     * and a certain <CODE>Font</CODE>.
     *
     * @param	string		a <CODE>String</CODE>
     * @param	font		a <CODE>Font</CODE>
     */
    public Anchor(String string, Font font) {
        super(string, font);
    }

    /**
     * Constructs an <CODE>Anchor</CODE> with a certain <CODE>Chunk</CODE>
     * and a certain leading.
     *
     * @param	leading		the leading
     * @param	chunk		a <CODE>Chunk</CODE>
     */
    public Anchor(float leading, Chunk chunk) {
        super(leading, chunk);
    }

    /**
     * Constructs an <CODE>Anchor</CODE> with a certain leading
     * and a certain <CODE>String</CODE>.
     *
     * @param	leading		the leading
     * @param	string		a <CODE>String</CODE>
     */
    public Anchor(float leading, String string) {
        super(leading, string);
    }

    /**
     * Constructs an <CODE>Anchor</CODE> with a certain leading,
     * a certain <CODE>String</CODE> and a certain <CODE>Font</CODE>.
     *
     * @param	leading		the leading
     * @param	string		a <CODE>String</CODE>
     * @param	font		a <CODE>Font</CODE>
     */
    public Anchor(float leading, String string, Font font) {
        super(leading, string, font);
    }

    /**
     * Constructs an <CODE>Anchor</CODE> with a certain <CODE>Phrase</CODE>.
     *
     * @param	phrase		a <CODE>Phrase</CODE>
     */
    public Anchor(Phrase phrase) {
    	super(phrase);
    	if (phrase instanceof Anchor) {
    		Anchor a = (Anchor) phrase;
    		setName(a.name);
    		setReference(a.reference);
    	}
    }

    // implementation of the Element-methods

    /**
     * Processes the element by adding it (or the different parts) to an
     * <CODE>ElementListener</CODE>.
     *
     * @param	listener	an <CODE>ElementListener</CODE>
     * @return	<CODE>true</CODE> if the element was processed successfully
     */
    @Override
    public boolean process(ElementListener listener) {
        try {
            Chunk chunk;
            Iterator<Chunk> i = getChunks().iterator();
            boolean localDestination = reference != null && reference.startsWith("#");
            boolean notGotoOK = true;
            while (i.hasNext()) {
                chunk = i.next();
                if (name != null && notGotoOK && !chunk.isEmpty()) {
                    chunk.setLocalDestination(name);
                    notGotoOK = false;
                }
                if (localDestination) {
                    chunk.setLocalGoto(reference.substring(1));
                }
                listener.add(chunk);
            }
            return true;
        }
        catch(DocumentException de) {
            return false;
        }
    }

    /**
     * Gets all the chunks in this element.
     *
     * @return	an <CODE>ArrayList</CODE>
     */
    @Override
    public ArrayList<Chunk> getChunks() {
        ArrayList<Chunk> tmp = new ArrayList<Chunk>();
        Chunk chunk;
        Iterator<Element> i = iterator();
        boolean localDestination = reference != null && reference.startsWith("#");
        boolean notGotoOK = true;
        while (i.hasNext()) {
            chunk = (Chunk) i.next();
            if (name != null && notGotoOK && !chunk.isEmpty()) {
                chunk.setLocalDestination(name);
                notGotoOK = false;
            }
            if (localDestination) {
                chunk.setLocalGoto(reference.substring(1));
            }
            else if (reference != null)
                chunk.setAnchor(reference);
            tmp.add(chunk);
        }
        return tmp;
    }

    /**
     * Gets the type of the text element.
     *
     * @return	a type
     */
    @Override
    public int type() {
        return Element.ANCHOR;
    }

    // methods

    /**
     * Sets the name of this <CODE>Anchor</CODE>.
     *
     * @param	name		a new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the reference of this <CODE>Anchor</CODE>.
     *
     * @param	reference		a new reference
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    // methods to retrieve information

	/**
     * Returns the name of this <CODE>Anchor</CODE>.
     *
     * @return	a name
     */
    public String getName() {
        return name;
    }

	/**
     * Gets the reference of this <CODE>Anchor</CODE>.
     *
     * @return	a reference
     */
    public String getReference() {
        return reference;
    }

	/**
     * Gets the reference of this <CODE>Anchor</CODE>.
     *
     * @return	an <CODE>URL</CODE>
     */
    public URL getUrl() {
        try {
            return new URL(reference);
        }
        catch(MalformedURLException mue) {
            return null;
        }
    }

}
