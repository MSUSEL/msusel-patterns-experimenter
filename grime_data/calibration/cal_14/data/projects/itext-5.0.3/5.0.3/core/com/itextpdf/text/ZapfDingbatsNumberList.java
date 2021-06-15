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
 * A special-version of <CODE>LIST</CODE> which use zapfdingbats-numbers (1..10).
 *
 * @see com.itextpdf.text.List
 * @author Michael Niedermair and Bruno Lowagie
 */

public class ZapfDingbatsNumberList extends List {

	/**
	 * which type
	 */
	protected int type;

	/**
	 * Creates a ZapdDingbatsNumberList
	 * @param type the type of list
	 */
	public ZapfDingbatsNumberList(int type) {
		super(true);
		this.type = type;
		float fontsize = symbol.getFont().getSize();
		symbol.setFont(FontFactory.getFont(FontFactory.ZAPFDINGBATS, fontsize, Font.NORMAL));
		postSymbol = " ";
	}

	/**
	 * Creates a ZapdDingbatsNumberList
	 * @param type the type of list
	 * @param symbolIndent	indent
	 */
	public ZapfDingbatsNumberList(int type, int symbolIndent) {
		super(true, symbolIndent);
		this.type = type;
		float fontsize = symbol.getFont().getSize();
		symbol.setFont(FontFactory.getFont(FontFactory.ZAPFDINGBATS, fontsize, Font.NORMAL));
		postSymbol = " ";
	}

	/**
	 * set the type
	 *
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * get the type
	 *
	 * @return	char-number
	 */
	public int getType() {
		return type;
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
			switch (type ) {
				case 0:
					chunk.append(String.valueOf((char)(first + list.size() + 171)));
					break;
				case 1:
					chunk.append(String.valueOf((char)(first + list.size() + 181)));
					break;
				case 2:
					chunk.append(String.valueOf((char)(first + list.size() + 191)));
					break;
				default:
					chunk.append(String.valueOf((char)(first + list.size() + 201)));
			}
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
