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
package com.itextpdf.text.factories;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;
import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Annotation;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.ChapterAutoNumber;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.ElementTags;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.html.Markup;

/**
 * This class is able to create Element objects based on a list of properties.
 */
public class ElementFactory {

	/**
	 * Creates a Chunk object based on a list of properties.
	 * @param attributes
	 * @return a Chunk
	 */
	public static Chunk getChunk(Properties attributes) {
		Chunk chunk = new Chunk();

		chunk.setFont(FontFactory.getFont(attributes));
		String value;

		value = attributes.getProperty(ElementTags.ITEXT);
		if (value != null) {
			chunk.append(value);
		}
		value = attributes.getProperty(ElementTags.LOCALGOTO);
		if (value != null) {
			chunk.setLocalGoto(value);
		}
		value = attributes.getProperty(ElementTags.REMOTEGOTO);
		if (value != null) {
			String page = attributes.getProperty(ElementTags.PAGE);
			if (page != null) {
				chunk.setRemoteGoto(value, Integer.parseInt(page));
			} else {
				String destination = attributes
						.getProperty(ElementTags.DESTINATION);
				if (destination != null) {
					chunk.setRemoteGoto(value, destination);
				}
			}
		}
		value = attributes.getProperty(ElementTags.LOCALDESTINATION);
		if (value != null) {
			chunk.setLocalDestination(value);
		}
		value = attributes.getProperty(ElementTags.SUBSUPSCRIPT);
		if (value != null) {
			chunk.setTextRise(Float.parseFloat(value + "f"));
		}
		value = attributes.getProperty(Markup.CSS_KEY_VERTICALALIGN);
		if (value != null && value.endsWith("%")) {
			float p = Float.parseFloat(value.substring(0, value.length() - 1)
					+ "f") / 100f;
			chunk.setTextRise(p * chunk.getFont().getSize());
		}
		value = attributes.getProperty(ElementTags.GENERICTAG);
		if (value != null) {
			chunk.setGenericTag(value);
		}
		value = attributes.getProperty(ElementTags.BACKGROUNDCOLOR);
		if (value != null) {
			chunk.setBackground(Markup.decodeColor(value));
		}
		return chunk;
	}

	/**
	 * Creates a Phrase object based on a list of properties.
	 * @param attributes
	 * @return a Phrase
	 */
	public static Phrase getPhrase(Properties attributes) {
		Phrase phrase = new Phrase();
		phrase.setFont(FontFactory.getFont(attributes));
		String value;
		value = attributes.getProperty(ElementTags.LEADING);
		if (value != null) {
			phrase.setLeading(Float.parseFloat(value + "f"));
		}
		value = attributes.getProperty(Markup.CSS_KEY_LINEHEIGHT);
		if (value != null) {
			phrase.setLeading(Markup.parseLength(value,
					Markup.DEFAULT_FONT_SIZE));
		}
		value = attributes.getProperty(ElementTags.ITEXT);
		if (value != null) {
			Chunk chunk = new Chunk(value);
			if ((value = attributes.getProperty(ElementTags.GENERICTAG)) != null) {
				chunk.setGenericTag(value);
			}
			phrase.add(chunk);
		}
		return phrase;
	}

	/**
	 * Creates an Anchor object based on a list of properties.
	 * @param attributes
	 * @return an Anchor
	 */
	public static Anchor getAnchor(Properties attributes) {
		Anchor anchor = new Anchor(getPhrase(attributes));
		String value;
		value = attributes.getProperty(ElementTags.NAME);
		if (value != null) {
			anchor.setName(value);
		}
		value = (String) attributes.remove(ElementTags.REFERENCE);
		if (value != null) {
			anchor.setReference(value);
		}
		return anchor;
	}

	/**
	 * Creates a Paragraph object based on a list of properties.
	 * @param attributes
	 * @return a Paragraph
	 */
	public static Paragraph getParagraph(Properties attributes) {
		Paragraph paragraph = new Paragraph(getPhrase(attributes));
		String value;
		value = attributes.getProperty(ElementTags.ALIGN);
		if (value != null) {
			paragraph.setAlignment(value);
		}
		value = attributes.getProperty(ElementTags.INDENTATIONLEFT);
		if (value != null) {
			paragraph.setIndentationLeft(Float.parseFloat(value + "f"));
		}
		value = attributes.getProperty(ElementTags.INDENTATIONRIGHT);
		if (value != null) {
			paragraph.setIndentationRight(Float.parseFloat(value + "f"));
		}
		return paragraph;
	}

	/**
	 * Creates a ListItem object based on a list of properties.
	 * @param attributes
	 * @return a ListItem
	 */
	public static ListItem getListItem(Properties attributes) {
		ListItem item = new ListItem(getParagraph(attributes));
		return item;
	}

	/**
	 * Creates a List object based on a list of properties.
	 * @param attributes
	 * @return the List
	 */
	public static List getList(Properties attributes) {
		List list = new List();

		list.setNumbered(Utilities.checkTrueOrFalse(attributes,
				ElementTags.NUMBERED));
		list.setLettered(Utilities.checkTrueOrFalse(attributes,
				ElementTags.LETTERED));
		list.setLowercase(Utilities.checkTrueOrFalse(attributes,
				ElementTags.LOWERCASE));
		list.setAutoindent(Utilities.checkTrueOrFalse(attributes,
				ElementTags.AUTO_INDENT_ITEMS));
		list.setAlignindent(Utilities.checkTrueOrFalse(attributes,
				ElementTags.ALIGN_INDENTATION_ITEMS));

		String value;

		value = attributes.getProperty(ElementTags.FIRST);
		if (value != null) {
			char character = value.charAt(0);
			if (Character.isLetter(character)) {
				list.setFirst(character);
			} else {
				list.setFirst(Integer.parseInt(value));
			}
		}

		value = attributes.getProperty(ElementTags.LISTSYMBOL);
		if (value != null) {
			list
					.setListSymbol(new Chunk(value, FontFactory
							.getFont(attributes)));
		}

		value = attributes.getProperty(ElementTags.INDENTATIONLEFT);
		if (value != null) {
			list.setIndentationLeft(Float.parseFloat(value + "f"));
		}

		value = attributes.getProperty(ElementTags.INDENTATIONRIGHT);
		if (value != null) {
			list.setIndentationRight(Float.parseFloat(value + "f"));
		}

		value = attributes.getProperty(ElementTags.SYMBOLINDENT);
		if (value != null) {
			list.setSymbolIndent(Float.parseFloat(value));
		}

		return list;
	}

	/**
	 * Creates a ChapterAutoNumber object based on a list of properties.
	 * @param attributes
	 * @return a Chapter
	 */
	public static ChapterAutoNumber getChapter(Properties attributes) {
		ChapterAutoNumber chapter = new ChapterAutoNumber("");
		setSectionParameters(chapter, attributes);
		return chapter;
	}

	/**
	 * Creates a Section object based on a list of properties.
	 * @param attributes
	 * @return a Section
	 */
	public static Section getSection(Section parent, Properties attributes) {
		Section section = parent.addSection("");
		setSectionParameters(section, attributes);
		return section;
	}

	/**
	 * Helper method to create a Chapter/Section object.
	 * @param attributes
	 */
	private static void setSectionParameters(Section section,
			Properties attributes) {
		String value;
		value = attributes.getProperty(ElementTags.NUMBERDEPTH);
		if (value != null) {
			section.setNumberDepth(Integer.parseInt(value));
		}
		value = attributes.getProperty(ElementTags.INDENT);
		if (value != null) {
			section.setIndentation(Float.parseFloat(value + "f"));
		}
		value = attributes.getProperty(ElementTags.INDENTATIONLEFT);
		if (value != null) {
			section.setIndentationLeft(Float.parseFloat(value + "f"));
		}
		value = attributes.getProperty(ElementTags.INDENTATIONRIGHT);
		if (value != null) {
			section.setIndentationRight(Float.parseFloat(value + "f"));
		}
	}

	/**
	 * Creates an Image object based on a list of properties.
	 * @param attributes
	 * @return an Image
	 */
	public static Image getImage(Properties attributes)
			throws BadElementException, MalformedURLException, IOException {
		String value;

		value = attributes.getProperty(ElementTags.URL);
		if (value == null)
			throw new MalformedURLException(MessageLocalization.getComposedMessage("the.url.of.the.image.is.missing"));
		Image image = Image.getInstance(value);

		value = attributes.getProperty(ElementTags.ALIGN);
		int align = 0;
		if (value != null) {
			if (ElementTags.ALIGN_LEFT.equalsIgnoreCase(value))
				align |= Image.LEFT;
			else if (ElementTags.ALIGN_RIGHT.equalsIgnoreCase(value))
				align |= Image.RIGHT;
			else if (ElementTags.ALIGN_MIDDLE.equalsIgnoreCase(value))
				align |= Image.MIDDLE;
		}
		if ("true".equalsIgnoreCase(attributes
				.getProperty(ElementTags.UNDERLYING)))
			align |= Image.UNDERLYING;
		if ("true".equalsIgnoreCase(attributes
				.getProperty(ElementTags.TEXTWRAP)))
			align |= Image.TEXTWRAP;
		image.setAlignment(align);

		value = attributes.getProperty(ElementTags.ALT);
		if (value != null) {
			image.setAlt(value);
		}

		String x = attributes.getProperty(ElementTags.ABSOLUTEX);
		String y = attributes.getProperty(ElementTags.ABSOLUTEY);
		if ((x != null) && (y != null)) {
			image.setAbsolutePosition(Float.parseFloat(x + "f"), Float
					.parseFloat(y + "f"));
		}
		value = attributes.getProperty(ElementTags.PLAINWIDTH);
		if (value != null) {
			image.scaleAbsoluteWidth(Float.parseFloat(value + "f"));
		}
		value = attributes.getProperty(ElementTags.PLAINHEIGHT);
		if (value != null) {
			image.scaleAbsoluteHeight(Float.parseFloat(value + "f"));
		}
		value = attributes.getProperty(ElementTags.ROTATION);
		if (value != null) {
			image.setRotation(Float.parseFloat(value + "f"));
		}
		return image;
	}

	/**
	 * Creates an Annotation object based on a list of properties.
	 * @param attributes
	 * @return an Annotation
	 */
	public static Annotation getAnnotation(Properties attributes) {
		float llx = 0, lly = 0, urx = 0, ury = 0;
		String value;

		value = attributes.getProperty(ElementTags.LLX);
		if (value != null) {
			llx = Float.parseFloat(value + "f");
		}
		value = attributes.getProperty(ElementTags.LLY);
		if (value != null) {
			lly = Float.parseFloat(value + "f");
		}
		value = attributes.getProperty(ElementTags.URX);
		if (value != null) {
			urx = Float.parseFloat(value + "f");
		}
		value = attributes.getProperty(ElementTags.URY);
		if (value != null) {
			ury = Float.parseFloat(value + "f");
		}

		String title = attributes.getProperty(ElementTags.TITLE);
		String text = attributes.getProperty(ElementTags.CONTENT);
		if (title != null || text != null) {
			return new Annotation(title, text, llx, lly, urx, ury);
		}
		value = attributes.getProperty(ElementTags.URL);
		if (value != null) {
			return new Annotation(llx, lly, urx, ury, value);
		}
		value = attributes.getProperty(ElementTags.NAMED);
		if (value != null) {
			return new Annotation(llx, lly, urx, ury, Integer.parseInt(value));
		}
		String file = attributes.getProperty(ElementTags.FILE);
		String destination = attributes.getProperty(ElementTags.DESTINATION);
		String page = (String) attributes.remove(ElementTags.PAGE);
		if (file != null) {
			if (destination != null) {
				return new Annotation(llx, lly, urx, ury, file, destination);
			}
			if (page != null) {
				return new Annotation(llx, lly, urx, ury, file, Integer
						.parseInt(page));
			}
		}
		return new Annotation("", "", llx, lly, urx, ury);
	}
}
