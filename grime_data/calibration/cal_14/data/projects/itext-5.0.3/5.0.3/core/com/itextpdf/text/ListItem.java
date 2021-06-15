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

/**
 * A <CODE>ListItem</CODE> is a <CODE>Paragraph</CODE>
 * that can be added to a <CODE>List</CODE>.
 * <P>
 * <B>Example 1:</B>
 * <BLOCKQUOTE><PRE>
 * List list = new List(true, 20);
 * list.add(<STRONG>new ListItem("First line")</STRONG>);
 * list.add(<STRONG>new ListItem("The second line is longer to see what happens once the end of the line is reached. Will it start on a new line?")</STRONG>);
 * list.add(<STRONG>new ListItem("Third line")</STRONG>);
 * </PRE></BLOCKQUOTE>
 *
 * The result of this code looks like this:
 *	<OL>
 *		<LI>
 *			First line
 *		</LI>
 *		<LI>
 *			The second line is longer to see what happens once the end of the line is reached. Will it start on a new line?
 *		</LI>
 *		<LI>
 *			Third line
 *		</LI>
 *	</OL>
 *
 * <B>Example 2:</B>
 * <BLOCKQUOTE><PRE>
 * List overview = new List(false, 10);
 * overview.add(<STRONG>new ListItem("This is an item")</STRONG>);
 * overview.add("This is another item");
 * </PRE></BLOCKQUOTE>
 *
 * The result of this code looks like this:
 *	<UL>
 *		<LI>
 *			This is an item
 *		</LI>
 *		<LI>
 *			This is another item
 *		</LI>
 *	</UL>
 *
 * @see	Element
 * @see List
 * @see	Paragraph
 */

public class ListItem extends Paragraph {
    
    // constants
	private static final long serialVersionUID = 1970670787169329006L;
	
	// member variables
	
	/**
	 * this is the symbol that will precede the listitem.
	 * @since	5.0	used to be private
	 */
    protected Chunk symbol;
    
    // constructors
    
    /**
     * Constructs a <CODE>ListItem</CODE>.
     */
    public ListItem() {
        super();
    }
    
    /**
     * Constructs a <CODE>ListItem</CODE> with a certain leading.
     *
     * @param	leading		the leading
     */    
    public ListItem(float leading) {
        super(leading);
    }
    
    /**
     * Constructs a <CODE>ListItem</CODE> with a certain <CODE>Chunk</CODE>.
     *
     * @param	chunk		a <CODE>Chunk</CODE>
     */
    public ListItem(Chunk chunk) {
        super(chunk);
    }
    
    /**
     * Constructs a <CODE>ListItem</CODE> with a certain <CODE>String</CODE>.
     *
     * @param	string		a <CODE>String</CODE>
     */
    public ListItem(String string) {
        super(string);
    }
    
    /**
     * Constructs a <CODE>ListItem</CODE> with a certain <CODE>String</CODE>
     * and a certain <CODE>Font</CODE>.
     *
     * @param	string		a <CODE>String</CODE>
     * @param	font		a <CODE>String</CODE>
     */
    public ListItem(String string, Font font) {
        super(string, font);
    }
    
    /**
     * Constructs a <CODE>ListItem</CODE> with a certain <CODE>Chunk</CODE>
     * and a certain leading.
     *
     * @param	leading		the leading
     * @param	chunk		a <CODE>Chunk</CODE>
     */
    public ListItem(float leading, Chunk chunk) {
        super(leading, chunk);
    }
    
    /**
     * Constructs a <CODE>ListItem</CODE> with a certain <CODE>String</CODE>
     * and a certain leading.
     *
     * @param	leading		the leading
     * @param	string		a <CODE>String</CODE>
     */
    public ListItem(float leading, String string) {
        super(leading, string);
    }
    
    /**
     * Constructs a <CODE>ListItem</CODE> with a certain leading, <CODE>String</CODE>
     * and <CODE>Font</CODE>.
     *
     * @param	leading		the leading
     * @param	string		a <CODE>String</CODE>
     * @param	font		a <CODE>Font</CODE>
     */
    public ListItem(float leading, String string, Font font) {
        super(leading, string, font);
    }
    
    /**
     * Constructs a <CODE>ListItem</CODE> with a certain <CODE>Phrase</CODE>.
     *
     * @param	phrase		a <CODE>Phrase</CODE>
     */
    public ListItem(Phrase phrase) {
        super(phrase);
    }
    
    // implementation of the Element-methods
    
    /**
     * Gets the type of the text element.
     *
     * @return	a type
     */
    public int type() {
        return Element.LISTITEM;
    }
    
    // methods
    
    /**
     * Sets the listsymbol.
     *
     * @param	symbol	a <CODE>Chunk</CODE>
     */
    public void setListSymbol(Chunk symbol) {
    	if (this.symbol == null) {
    		this.symbol = symbol;
    		if (this.symbol.getFont().isStandardFont()) {
    			this.symbol.setFont(font);
    		}
    	}
    }
    
    /**
     * Sets the indentation of this paragraph on the left side.
     *
     * @param	indentation		the new indentation
     */
    public void setIndentationLeft(float indentation, boolean autoindent) {
    	if (autoindent) {
    		setIndentationLeft(getListSymbol().getWidthPoint());
    	}
    	else {
    		setIndentationLeft(indentation);
    	}
    }
    
    // methods to retrieve information

	/**
     * Returns the listsymbol.
     *
     * @return	a <CODE>Chunk</CODE>
     */
    public Chunk getListSymbol() {
        return symbol;
    }

}
