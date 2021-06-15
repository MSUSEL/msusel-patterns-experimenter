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

import java.net.URL;
import java.security.MessageDigest;

/**
 * Support for JBIG2 images.
 * @since 2.1.5
 */
public class ImgJBIG2 extends Image {
	
	/** JBIG2 globals */
	private  byte[] global;
	/** A unique hash */
	private  byte[] globalHash;
	
	/**
	 * Copy contstructor.
	 * @param	image another Image
	 */
	ImgJBIG2(Image image) {
		super(image);
	}

	/**
	 * Empty constructor.
	 */
	public ImgJBIG2() {
		super((Image) null);
	}

	/**
	 * Actual constructor for ImgJBIG2 images.
	 * @param	width	the width of the image
	 * @param	height	the height of the image
	 * @param	data	the raw image data
	 * @param	globals	JBIG2 globals
	 */
	public ImgJBIG2(int width, int height, byte[] data, byte[] globals) {
		super((URL) null);
        type = JBIG2;
        originalType = ORIGINAL_JBIG2;
		scaledHeight = height;
		setTop(scaledHeight);
		scaledWidth = width;
		setRight(scaledWidth);
		bpc = 1;
		colorspace = 1;
		rawData = data;
		plainWidth = getWidth();
		plainHeight = getHeight();
		if ( globals != null ) {
			this.global = globals;
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("MD5");
				md.update(this.global);
				this.globalHash = md.digest();
			} catch (Exception e) {
				//ignore
			}
			
		}
	}
	
	/**
	 * Getter for the JBIG2 global data.
	 * @return 	an array of bytes
	 */
	public byte[] getGlobalBytes() {
		return this.global;
	}
	
	/**
	 * Getter for the unique hash.
	 * @return	an array of bytes
	 */
	public byte[] getGlobalHash() {
		return this.globalHash;
	}

}
