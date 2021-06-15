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

import java.util.HashMap;

import com.itextpdf.text.html.Markup;

public class StyleSheet {

	public HashMap<String, HashMap<String, String>> classMap = new HashMap<String, HashMap<String, String>>();

	public HashMap<String, HashMap<String, String>> tagMap = new HashMap<String, HashMap<String, String>>();

	/** Creates a new instance of StyleSheet */
	public StyleSheet() {
	}

	public void applyStyle(String tag, HashMap<String, String> props) {
		HashMap<String, String> map = tagMap.get(tag.toLowerCase());
		if (map != null) {
			HashMap<String, String> temp = new HashMap<String, String>(map);
			temp.putAll(props);
			props.putAll(temp);
		}
		String cm = props.get(Markup.HTML_ATTR_CSS_CLASS);
		if (cm == null)
			return;
		map = classMap.get(cm.toLowerCase());
		if (map == null)
			return;
		props.remove(Markup.HTML_ATTR_CSS_CLASS);
		HashMap<String, String> temp = new HashMap<String, String>(map);
		temp.putAll(props);
		props.putAll(temp);
	}

	public void loadStyle(String style, HashMap<String, String> props) {
		classMap.put(style.toLowerCase(), props);
	}

	public void loadStyle(String style, String key, String value) {
		style = style.toLowerCase();
		HashMap<String, String> props = classMap.get(style);
		if (props == null) {
			props = new HashMap<String, String>();
			classMap.put(style, props);
		}
		props.put(key, value);
	}

	public void loadTagStyle(String tag, HashMap<String, String> props) {
		tagMap.put(tag.toLowerCase(), props);
	}

	public void loadTagStyle(String tag, String key, String value) {
		tag = tag.toLowerCase();
		HashMap<String, String> props = tagMap.get(tag);
		if (props == null) {
			props = new HashMap<String, String>();
			tagMap.put(tag, props);
		}
		props.put(key, value);
	}

}