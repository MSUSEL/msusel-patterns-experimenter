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
import java.util.Properties;
import java.util.StringTokenizer;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementTags;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.html.Markup;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.HyphenationAuto;
import com.itextpdf.text.pdf.HyphenationEvent;
/**
 *
 * @author  psoares
 */
public class FactoryProperties {

	/**
	 * @since	iText 5.0	This used to be a FontFactoryImp
	 */
	private FontProvider fontImp = FontFactory.getFontImp();

	/** Creates a new instance of FactoryProperties */
	public FactoryProperties() {
	}

	public Chunk createChunk(String text, ChainedProperties props) {
		Font font = getFont(props);
		float size = font.getSize();
		size /= 2;
		Chunk ck = new Chunk(text, font);
		if (props.hasProperty("sub"))
			ck.setTextRise(-size);
		else if (props.hasProperty("sup"))
			ck.setTextRise(size);
		ck.setHyphenation(getHyphenation(props));
		return ck;
	}

	private static void setParagraphLeading(Paragraph p, String leading) {
		if (leading == null) {
			p.setLeading(0, 1.5f);
			return;
		}
		try {
			StringTokenizer tk = new StringTokenizer(leading, " ,");
			String v = tk.nextToken();
			float v1 = Float.parseFloat(v);
			if (!tk.hasMoreTokens()) {
				p.setLeading(v1, 0);
				return;
			}
			v = tk.nextToken();
			float v2 = Float.parseFloat(v);
			p.setLeading(v1, v2);
		} catch (Exception e) {
			p.setLeading(0, 1.5f);
		}
	}

	public static void createParagraph(Paragraph p, ChainedProperties props) {
		String value = props.getProperty("align");
		if (value != null) {
			if (value.equalsIgnoreCase("center"))
				p.setAlignment(Element.ALIGN_CENTER);
			else if (value.equalsIgnoreCase("right"))
				p.setAlignment(Element.ALIGN_RIGHT);
			else if (value.equalsIgnoreCase("justify"))
				p.setAlignment(Element.ALIGN_JUSTIFIED);
		}
		p.setHyphenation(getHyphenation(props));
		setParagraphLeading(p, props.getProperty("leading"));
		value = props.getProperty("before");
		if (value != null) {
			try {
				p.setSpacingBefore(Float.parseFloat(value));
			} catch (Exception e) {
			}
		}
		value = props.getProperty("after");
		if (value != null) {
			try {
				p.setSpacingAfter(Float.parseFloat(value));
			} catch (Exception e) {
			}
		}
		value = props.getProperty("extraparaspace");
		if (value != null) {
			try {
				p.setExtraParagraphSpace(Float.parseFloat(value));
			} catch (Exception e) {
			}
		}
	}

	public static Paragraph createParagraph(ChainedProperties props) {
		Paragraph p = new Paragraph();
		createParagraph(p, props);
		return p;
	}

	public static ListItem createListItem(ChainedProperties props) {
		ListItem p = new ListItem();
		createParagraph(p, props);
		return p;
	}

	public Font getFont(ChainedProperties props) {
		String face = props.getProperty(ElementTags.FACE);
		if (face != null) {
			StringTokenizer tok = new StringTokenizer(face, ",");
			while (tok.hasMoreTokens()) {
				face = tok.nextToken().trim();
				if (face.startsWith("\""))
					face = face.substring(1);
				if (face.endsWith("\""))
					face = face.substring(0, face.length() - 1);
				if (fontImp.isRegistered(face))
					break;
			}
		}
		int style = 0;
		if (props.hasProperty(HtmlTags.I))
			style |= Font.ITALIC;
		if (props.hasProperty(HtmlTags.B))
			style |= Font.BOLD;
		if (props.hasProperty(HtmlTags.U))
			style |= Font.UNDERLINE;
		if (props.hasProperty(HtmlTags.S))
			style |= Font.STRIKETHRU;
		String value = props.getProperty(ElementTags.SIZE);
		float size = 12;
		if (value != null)
			size = Float.parseFloat(value);
		BaseColor color = Markup.decodeColor(props.getProperty("color"));
		String encoding = props.getProperty("encoding");
		if (encoding == null)
			encoding = BaseFont.WINANSI;
		return fontImp.getFont(face, encoding, true, size, style, color);
	}

	/**
	 * Gets a HyphenationEvent based on the hyphenation entry in ChainedProperties.
	 * @param	props	ChainedProperties
	 * @return	a HyphenationEvent
	 * @since	2.1.2
	 */
	public static HyphenationEvent getHyphenation(ChainedProperties props) {
		return getHyphenation(props.getProperty("hyphenation"));
	}

	/**
	 * Gets a HyphenationEvent based on the hyphenation entry in a HashMap.
	 * @param	props	a HashMap with properties
	 * @return	a HyphenationEvent
	 * @since	2.1.2
	 */
	public static HyphenationEvent getHyphenation(HashMap<String, String> props) {
		return getHyphenation(props.get("hyphenation"));
	}

	/**
	 * Gets a HyphenationEvent based on a String.
	 * For instance "en_UK,3,2" returns new HyphenationAuto("en", "UK", 3, 2);
	 * @param	s a String, for instance "en_UK,2,2"
	 * @return	a HyphenationEvent
	 * @since	2.1.2
	 */
	public static HyphenationEvent getHyphenation(String s) {
		if (s == null || s.length() == 0) {
			return null;
		}
		String lang = s;
		String country = null;
		int leftMin = 2;
		int rightMin = 2;

		int pos = s.indexOf('_');
		if (pos == -1) {
			return new HyphenationAuto(lang, country, leftMin, rightMin);
		}
		lang = s.substring(0, pos);
		country = s.substring(pos + 1);
		pos = country.indexOf(',');
		if (pos == -1) {
			return new HyphenationAuto(lang, country, leftMin, rightMin);
		}
		s = country.substring(pos + 1);
		country = country.substring(0, pos);
		pos = s.indexOf(',');
		if (pos == -1) {
			leftMin = Integer.parseInt(s);
		} else {
			leftMin = Integer.parseInt(s.substring(0, pos));
			rightMin = Integer.parseInt(s.substring(pos + 1));
		}
		return new HyphenationAuto(lang, country, leftMin, rightMin);
	}

	/**
	 * This method isn't used by iText, but you can use it to analyze
	 * the value of a style attribute inside a HashMap.
	 * The different elements of the style attribute are added to the
	 * HashMap as key-value pairs.
	 * @param	h	a HashMap that should have at least a key named
	 * style. After this method is invoked, more keys could be added.
	 * @since 5.0.1 (generic type in signature)
	 */
	public static void insertStyle(HashMap<String, String> h) {
		String style = h.get("style");
		if (style == null)
			return;
		Properties prop = Markup.parseAttributes(style);
		for (Object o: prop.keySet()) {
		    String key = (String) o;
			if (key.equals(Markup.CSS_KEY_FONTFAMILY)) {
				h.put("face", prop.getProperty(key));
			} else if (key.equals(Markup.CSS_KEY_FONTSIZE)) {
				h.put("size", Float.toString(Markup.parseLength(prop
						.getProperty(key)))
						+ "pt");
			} else if (key.equals(Markup.CSS_KEY_FONTSTYLE)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				if (ss.equals("italic") || ss.equals("oblique"))
					h.put("i", null);
			} else if (key.equals(Markup.CSS_KEY_FONTWEIGHT)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				if (ss.equals("bold") || ss.equals("700") || ss.equals("800")
						|| ss.equals("900"))
					h.put("b", null);
			} else if (key.equals(Markup.CSS_KEY_TEXTDECORATION)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				if (ss.equals(Markup.CSS_VALUE_UNDERLINE))
					h.put("u", null);
			} else if (key.equals(Markup.CSS_KEY_COLOR)) {
				BaseColor c = Markup.decodeColor(prop.getProperty(key));
				if (c != null) {
					int hh = c.getRGB();
					String hs = Integer.toHexString(hh);
					hs = "000000" + hs;
					hs = "#" + hs.substring(hs.length() - 6);
					h.put("color", hs);
				}
			} else if (key.equals(Markup.CSS_KEY_LINEHEIGHT)) {
				String ss = prop.getProperty(key).trim();
				float v = Markup.parseLength(prop.getProperty(key));
				if (ss.endsWith("%")) {
					h.put("leading", "0," + v / 100);
				} else if ("normal".equalsIgnoreCase(ss)) {
					h.put("leading", "0,1.5");
				}
				else {
					h.put("leading", v + ",0");
				}
			} else if (key.equals(Markup.CSS_KEY_TEXTALIGN)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				h.put("align", ss);
			}
		}
	}

	/**
	 * New method contributed by Lubos Strapko
	 * @param h
	 * @param cprops
	 * @since 2.1.3
	 */
	public static void insertStyle(HashMap<String, String> h, ChainedProperties cprops) {
		String style = h.get("style");
		if (style == null)
			return;
		Properties prop = Markup.parseAttributes(style);
		for (Object element : prop.keySet()) {
			String key = (String) element;
			if (key.equals(Markup.CSS_KEY_FONTFAMILY)) {
				h.put(ElementTags.FACE, prop.getProperty(key));
			} else if (key.equals(Markup.CSS_KEY_FONTSIZE)) {
				float actualFontSize = Markup.parseLength(cprops
						.getProperty(ElementTags.SIZE),
						Markup.DEFAULT_FONT_SIZE);
				if (actualFontSize <= 0f)
					actualFontSize = Markup.DEFAULT_FONT_SIZE;
				h.put(ElementTags.SIZE, Float.toString(Markup.parseLength(prop
						.getProperty(key), actualFontSize))
						+ "pt");
			} else if (key.equals(Markup.CSS_KEY_FONTSTYLE)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				if (ss.equals("italic") || ss.equals("oblique"))
					h.put("i", null);
			} else if (key.equals(Markup.CSS_KEY_FONTWEIGHT)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				if (ss.equals("bold") || ss.equals("700") || ss.equals("800")
						|| ss.equals("900"))
					h.put("b", null);
			} else if (key.equals(Markup.CSS_KEY_TEXTDECORATION)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				if (ss.equals(Markup.CSS_VALUE_UNDERLINE))
					h.put("u", null);
			} else if (key.equals(Markup.CSS_KEY_COLOR)) {
				BaseColor c = Markup.decodeColor(prop.getProperty(key));
				if (c != null) {
					int hh = c.getRGB();
					String hs = Integer.toHexString(hh);
					hs = "000000" + hs;
					hs = "#" + hs.substring(hs.length() - 6);
					h.put("color", hs);
				}
			} else if (key.equals(Markup.CSS_KEY_LINEHEIGHT)) {
				String ss = prop.getProperty(key).trim();
				float actualFontSize = Markup.parseLength(cprops
						.getProperty(ElementTags.SIZE),
						Markup.DEFAULT_FONT_SIZE);
				if (actualFontSize <= 0f)
					actualFontSize = Markup.DEFAULT_FONT_SIZE;
				float v = Markup.parseLength(prop.getProperty(key),
						actualFontSize);
				if (ss.endsWith("%")) {
					h.put("leading", "0," + v / 100);
					return;
				}
				if ("normal".equalsIgnoreCase(ss)) {
					h.put("leading", "0,1.5");
					return;
				}
				h.put("leading", v + ",0");
			} else if (key.equals(Markup.CSS_KEY_TEXTALIGN)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				h.put("align", ss);
			} else if (key.equals(Markup.CSS_KEY_PADDINGLEFT)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				h.put("indent", Float.toString(Markup.parseLength(ss)));
			}
		}
	}

	public FontProvider getFontImp() {
		return fontImp;
	}

	public void setFontImp(FontProvider fontImp) {
		this.fontImp = fontImp;
	}

	public static HashMap<String, String> followTags = new HashMap<String, String>();
	static {
		followTags.put("i", "i");
		followTags.put("b", "b");
		followTags.put("u", "u");
		followTags.put("sub", "sub");
		followTags.put("sup", "sup");
		followTags.put("em", "i");
		followTags.put("strong", "b");
		followTags.put("s", "s");
		followTags.put("strike", "s");
	}
}
