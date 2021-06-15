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

import java.util.ArrayList;
import java.util.Collection;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.DocumentFont;

/**
 * Provides information and calculations needed by render listeners
 * to display/evaluate text render operations.
 * <br><br>
 * This is passed between the {@link PdfContentStreamProcessor} and 
 * {@link RenderListener} objects as text rendering operations are
 * discovered
 */
public class TextRenderInfo {
	
    private final String text;
    private final Matrix textToUserSpaceTransformMatrix;
    private final GraphicsState gs;
    /**
     * Array containing marked content info for the text.
     * @since 5.0.2
     */
    private final Collection<MarkedContentInfo> markedContentInfos;
    
    /**
     * Creates a new TextRenderInfo object
     * @param text the text that should be displayed
     * @param gs the graphics state (note: at this time, this is not immutable, so don't cache it)
     * @param textMatrix the text matrix at the time of the render operation
     * @param markedContentInfo the marked content sequence, if available
     */
    TextRenderInfo(String text, GraphicsState gs, Matrix textMatrix, Collection<MarkedContentInfo> markedContentInfo) {
        this.text = text;
        this.textToUserSpaceTransformMatrix = textMatrix.multiply(gs.ctm);
        this.gs = gs;
        this.markedContentInfos = new ArrayList<MarkedContentInfo>(markedContentInfo);
    }
    
    /**
     * @return the text to render
     */
    public String getText(){ 
        return text; 
    }

	/**
	 * Checks if the text belongs to a marked content sequence
	 * with a given mcid.
	 * @param mcid a marked content id
	 * @return true if the text is marked with this id
	 * @since 5.0.2
	 */
	public boolean hasMcid(int mcid) {
		for (MarkedContentInfo info : markedContentInfos) {
            if (info.hasMcid())
                if(info.getMcid() == mcid)
                    return true;
        }

		return false;
	}

	/**
     * @return the unscaled (i.e. in Text space) width of the text
     */
    float getUnscaledWidth(){ 
        return getStringWidth(text); 
    }
    
    /**
     * Gets the baseline for the text (i.e. the line that the text 'sits' on)
     * @return the baseline line segment
     * @since 5.0.2
     */
    public LineSegment getBaseline(){
        return getUnscaledBaselineWithOffset(0).transformBy(textToUserSpaceTransformMatrix);
    }
    
    /**
     * Gets the ascentline for the text (i.e. the line that represents the topmost extent that a string of the current font could have)
     * @return the ascentline line segment
     * @since 5.0.2
     */
    public LineSegment getAscentLine(){
        float ascent = gs.getFont().getFontDescriptor(BaseFont.ASCENT, gs.getFontSize());
        return getUnscaledBaselineWithOffset(ascent).transformBy(textToUserSpaceTransformMatrix);
    }
    
    /**
     * Gets the descentline for the text (i.e. the line that represents the bottom most extent that a string of the current font could have)
     * @return the descentline line segment
     * @since 5.0.2
     */
    public LineSegment getDescentLine(){
        // per getFontDescription() API, descent is returned as a negative number, so we apply that as a normal vertical offset
        float descent = gs.getFont().getFontDescriptor(BaseFont.DESCENT, gs.getFontSize());
        return getUnscaledBaselineWithOffset(descent).transformBy(textToUserSpaceTransformMatrix);
    }
    
    private LineSegment getUnscaledBaselineWithOffset(float yOffset){
        return new LineSegment(new Vector(0, yOffset, 1), new Vector(getUnscaledWidth(), yOffset, 1));
    }

	/**
	 * Getter for the font
	 * @return the font
	 * @since iText 5.0.2
	 */
	public DocumentFont getFont() {
		return gs.getFont();
	}
    
    /**
     * @return The width, in user space units, of a single space character in the current font
     */
    public float getSingleSpaceWidth(){
        LineSegment textSpace = new LineSegment(new Vector(0, 0, 1), new Vector(getUnscaledFontSpaceWidth(), 0, 1));
        LineSegment userSpace = textSpace.transformBy(textToUserSpaceTransformMatrix);
        return userSpace.getLength();
    }
    
    /**
     * @return the text render mode that should be used for the text.  From the
     * PDF specification, this means:
     * <ul>
     *   <li>0 = Fill text</li>
     *   <li>1 = Stroke text</li>
     *   <li>2 = Fill, then stroke text</li>
     *   <li>3 = Invisible</li>
     *   <li>4 = Fill text and add to path for clipping</li>
     *   <li>5 = Stroke text and add to path for clipping</li>
     *   <li>6 = Fill, then stroke text and add to path for clipping</li>
     *   <li>7 = Add text to padd for clipping</li>
     * </ul>
     * @since iText 5.0.1
     */
    public int getTextRenderMode(){
        return gs.renderMode;
    }
    
    /**
     * Calculates the width of a space character.  If the font does not define
     * a width for a standard space character \u0020, we also attempt to use
     * the width of \u00A0 (a non-breaking space in many fonts)
     * @return the width of a single space character in text space units
     */
    private float getUnscaledFontSpaceWidth(){
        char charToUse = ' ';
        if (gs.font.getWidth(charToUse) == 0)
            charToUse = '\u00A0';
        return getStringWidth(String.valueOf(charToUse));
    }
    
    /**
     * Gets the width of a String in text space units
     * @param string    the string that needs measuring
     * @return  the width of a String in text space units
     */
    private float getStringWidth(String string){
        DocumentFont font = gs.font;
        char[] chars = string.toCharArray();
        float totalWidth = 0;
        for (int i = 0; i < chars.length; i++) {
            float w = font.getWidth(chars[i]) / 1000.0f;
            float wordSpacing = chars[i] == 32 ? gs.wordSpacing : 0f;
            totalWidth += (w * gs.fontSize + gs.characterSpacing + wordSpacing) * gs.horizontalScaling;
        }
        
        return totalWidth;
    }
    
}
