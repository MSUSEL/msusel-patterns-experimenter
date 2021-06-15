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
package com.itextpdf.rups.view.icons;

import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Class that fetches the icons in com.itextpdf.trapeze.icons.
 */
public class IconFetcher {
	
	/** Cache with icons. */
	protected static HashMap<String, Icon> cache = new HashMap<String, Icon>();
	
	/**
	 * Gets an Icon with a specific name.
	 * @param	filename	the filename of the Icon.
	 * @return	an Icon
	 */
	public static Icon getIcon(String filename) {
		if (filename == null) {
			return null;
		}
		Icon icon = cache.get(filename);
		if (icon == null) {
			try {
				icon = new ImageIcon(IconFetcher.class.getResource(filename));
				cache.put(filename, icon);
			}
			catch(Exception e) {
				System.err.println("Can't find file: " + filename);
				return null;
			}
		}
		return icon;
	}
}
