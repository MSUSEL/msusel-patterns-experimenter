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
package com.itextpdf.text.html.simpleparser;

import java.util.ArrayList;
import java.util.HashMap;

import com.itextpdf.text.ElementTags;

public class ChainedProperties {

	public final static int fontSizes[] = { 8, 10, 12, 14, 18, 24, 36 };

	private static final class ChainedProperty {
	    final String key;
	    final HashMap<String, String> property;
	    ChainedProperty(String key, HashMap<String, String> property) {
	    this.key = key;
	    this.property = property;
	    }
	}

	public ArrayList<ChainedProperty> chain = new ArrayList<ChainedProperty>();

	/** Creates a new instance of ChainedProperties */
	public ChainedProperties() {
	}

	public String getProperty(String key) {
    		for (int k = chain.size() - 1; k >= 0; --k) {
                        ChainedProperty p = chain.get(k);
                        HashMap<String, String> prop = p.property;
                        String ret = prop.get(key);
			if (ret != null)
				return ret;
		}
		return null;
	}

	public boolean hasProperty(String key) {
		for (int k = chain.size() - 1; k >= 0; --k) {
		        ChainedProperty p = chain.get(k);
                        HashMap<String, String> prop = p.property;
			if (prop.containsKey(key))
				return true;
		}
		return false;
	}

	public void addToChain(String key, HashMap<String, String> prop) {
		// adjust the font size
		String value = prop.get(ElementTags.SIZE);
		if (value != null) {
			if (value.endsWith("pt")) {
				prop.put(ElementTags.SIZE, value.substring(0,
						value.length() - 2));
			} else {
				int s = 0;
				if (value.startsWith("+") || value.startsWith("-")) {
					String old = getProperty("basefontsize");
					if (old == null)
						old = "12";
					float f = Float.parseFloat(old);
					int c = (int) f;
					for (int k = fontSizes.length - 1; k >= 0; --k) {
						if (c >= fontSizes[k]) {
							s = k;
							break;
						}
					}
					int inc = Integer.parseInt(value.startsWith("+") ? value
							.substring(1) : value);
					s += inc;
				} else {
					try {
						s = Integer.parseInt(value) - 1;
					} catch (NumberFormatException nfe) {
						s = 0;
					}
				}
				if (s < 0)
					s = 0;
				else if (s >= fontSizes.length)
					s = fontSizes.length - 1;
				prop.put(ElementTags.SIZE, Integer.toString(fontSizes[s]));
			}
		}
		chain.add(new ChainedProperty(key, prop));
	}

	public void removeChain(String key) {
		for (int k = chain.size() - 1; k >= 0; --k) {
			if (key.equals(chain.get(k).key)) {
				chain.remove(k);
				return;
			}
		}
	}
}
