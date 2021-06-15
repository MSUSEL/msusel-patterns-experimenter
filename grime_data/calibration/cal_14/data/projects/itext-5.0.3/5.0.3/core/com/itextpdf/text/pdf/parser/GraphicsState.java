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
package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;

/**
 * Keeps all the parameters of the graphics state.
 * @since	2.1.4
 */
public class GraphicsState {
    /** The current transformation matrix. */
    Matrix ctm;
    /** The current character spacing. */
    float characterSpacing;
    /** The current word spacing. */
    float wordSpacing;
    /** The current horizontal scaling */
    float horizontalScaling;
    /** The current leading. */
    float leading;
    /** The active font. */
    CMapAwareDocumentFont font;
    /** The current font size. */
    float fontSize;
    /** The current render mode. */
    int renderMode;
    /** The current text rise */
    float rise;
    /** The current knockout value. */
    boolean knockout;
    
    /**
     * Constructs a new Graphics State object with the default values.
     */
    public GraphicsState(){
        ctm = new Matrix();
        characterSpacing = 0;
        wordSpacing = 0;
        horizontalScaling = 1.0f;
        leading = 0;
        font = null;
        fontSize = 0;
        renderMode = 0;
        rise = 0;
        knockout = true;
    }
    
    /**
     * Copy constructor.
     * @param source	another GraphicsState object
     */
    public GraphicsState(GraphicsState source){
        // note: all of the following are immutable, with the possible exception of font
        // so it is safe to copy them as-is
        ctm = source.ctm;
        characterSpacing = source.characterSpacing;
        wordSpacing = source.wordSpacing;
        horizontalScaling = source.horizontalScaling;
        leading = source.leading;
        font = source.font;
        fontSize = source.fontSize;
        renderMode = source.renderMode;
        rise = source.rise;
        knockout = source.knockout;
    }

	/**
	 * Getter for the current transformation matrix
	 * @return the ctm
	 * @since iText 5.0.1
	 */
	public Matrix getCtm() {
		return ctm;
	}

	/**
	 * Getter for the character spacing.
	 * @return the character spacing
	 * @since iText 5.0.1
	 */
	public float getCharacterSpacing() {
		return characterSpacing;
	}

	/**
	 * Getter for the word spacing
	 * @return the word spacing
	 * @since iText 5.0.1
	 */
	public float getWordSpacing() {
		return wordSpacing;
	}

	/**
	 * Getter for the horizontal scaling
	 * @return the horizontal scaling
	 * @since iText 5.0.1
	 */
	public float getHorizontalScaling() {
		return horizontalScaling;
	}

	/**
	 * Getter for the leading
	 * @return the leading
	 * @since iText 5.0.1
	 */
	public float getLeading() {
		return leading;
	}

	/**
	 * Getter for the font
	 * @return the font
	 * @since iText 5.0.1
	 */
	public CMapAwareDocumentFont getFont() {
		return font;
	}

	/**
	 * Getter for the font size
	 * @return the font size
	 * @since iText 5.0.1
	 */
	public float getFontSize() {
		return fontSize;
	}

	/**
	 * Getter for the render mode
	 * @return the renderMode
	 * @since iText 5.0.1
	 */
	public int getRenderMode() {
		return renderMode;
	}

	/**
	 * Getter for text rise
	 * @return the text rise
	 * @since iText 5.0.1
	 */
	public float getRise() {
		return rise;
	}

	/**
	 * Getter for knockout
	 * @return the knockout
	 * @since iText 5.0.1
	 */
	public boolean isKnockout() {
		return knockout;
	}
}
