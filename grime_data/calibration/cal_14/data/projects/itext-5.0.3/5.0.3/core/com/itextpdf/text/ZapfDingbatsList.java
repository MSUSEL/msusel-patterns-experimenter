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
 *
 * A special-version of <CODE>LIST</CODE> which use zapfdingbats-letters.
 *
 * @see com.itextpdf.text.List
 * @author Michael Niedermair and Bruno Lowagie
 */

public class ZapfDingbatsList extends List {

	/**
	 * char-number in zapfdingbats
	 */
	protected int zn;

	/**
	 * Creates a ZapfDingbatsList
	 *
	 * @param zn a char-number
	 */
	public ZapfDingbatsList(int zn) {
		super(true);
		this.zn = zn;
		float fontsize = symbol.getFont().getSize();
		symbol.setFont(FontFactory.getFont(FontFactory.ZAPFDINGBATS, fontsize, Font.NORMAL));
		postSymbol = " ";
	}

	/**
	 * Creates a ZapfDingbatsList
	 *
	 * @param zn a char-number
	 * @param symbolIndent	indent
	 */
	public ZapfDingbatsList(int zn, int symbolIndent) {
		super(true, symbolIndent);
		this.zn = zn;
		float fontsize = symbol.getFont().getSize();
		symbol.setFont(FontFactory.getFont(FontFactory.ZAPFDINGBATS, fontsize, Font.NORMAL));
		postSymbol = " ";
	}

	/**
	 * set the char-number
	 * @param zn a char-number
	 */
	public void setCharNumber(int zn) {
		this.zn = zn;
	}

	/**
	 * get the char-number
	 *
	 * @return	char-number
	 */
	public int getCharNumber() {
		return zn;
	}

	/**
	 * Adds an <CODE>Element</CODE> to the <CODE>List</CODE>.
	 *
	 * @param	o	the object to add.
	 * @return true if adding the object succeeded
	 */
	@Override
	public boolean add(Element o) {
		if (o instanceof ListItem) {
			ListItem item = (ListItem) o;
			Chunk chunk = new Chunk(preSymbol, symbol.getFont());
			chunk.append(String.valueOf((char)zn));
			chunk.append(postSymbol);
			item.setListSymbol(chunk);
			item.setIndentationLeft(symbolIndent, autoindent);
			item.setIndentationRight(0);
			list.add(item);
		} else if (o instanceof List) {
			List nested = (List) o;
			nested.setIndentationLeft(nested.getIndentationLeft() + symbolIndent);
			first--;
			return list.add(nested);
		}
		return false;
	}
}
