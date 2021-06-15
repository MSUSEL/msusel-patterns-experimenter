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
package com.itextpdf.text.pdf.richmedia;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;

/**
 * A RichMediaAnimation dictionary specifies the preferred method that
 * conforming readers should use to apply timeline scaling to keyframe
 * animations. It can also specify that keyframe animations be played
 * repeatedly.
 * See ExtensionLevel 3 p80
 * @see		RichMediaActivation
 * @since	5.0.0
 */
public class RichMediaAnimation extends PdfDictionary {
	
	/**
	 * Constructs a RichMediaAnimation. Also sets the animation style
	 * described by this dictionary. Valid values are None, Linear, and
	 * Oscillating.
	 * @param	subtype	possible values are
	 * 		PdfName.NONE, PdfName.LINEAR, PdfName.OSCILLATING
	 */
	public RichMediaAnimation(PdfName subtype) {
		super(PdfName.RICHMEDIAANIMATION);
		put(PdfName.SUBTYPE, subtype);
	}
	
	/**
	 * Sets the number of times the animation is played.
	 * @param	playCount	the play count
	 */
	public void setPlayCount(int playCount) {
		put(PdfName.PLAYCOUNT, new PdfNumber(playCount));
	}
	
	/**
	 * Sets the speed to be used when running the animation.
	 * A value greater than one shortens the time it takes to play
	 * the animation, or effectively speeds up the animation.
	 * @param	speed	a speed value
	 */
	public void setSpeed(int speed) {
		put(PdfName.SPEED, new PdfNumber(speed));
	}
	
	/**
	 * Sets the speed to be used when running the animation.
	 * A value greater than one shortens the time it takes to play
	 * the animation, or effectively speeds up the animation.
	 * @param	speed	a speed value
	 */
	public void setSpeed(float speed) {
		put(PdfName.SPEED, new PdfNumber(speed));
	}
}
