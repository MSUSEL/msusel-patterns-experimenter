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
package com.itextpdf.text.pdf.codec;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgJBIG2;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * Support for JBIG2 Images.
 * This class assumes that we are always embedding into a pdf.
 * 
 * @since 2.1.5
 */
public class JBIG2Image {

	/**
	 * Gets a byte array that can be used as a /JBIG2Globals,
	 * or null if not applicable to the given jbig2.
	 * @param	ra	an random access file or array
	 * @return	a byte array
	 */
	public static byte[] getGlobalSegment(RandomAccessFileOrArray ra ) {
		try {
			JBIG2SegmentReader sr = new JBIG2SegmentReader(ra);
			sr.read();
			return sr.getGlobal(true);
		} catch (Exception e) {
	        return null;
	    }
	}
	
	/**
	 * returns an Image representing the given page.
	 * @param ra	the file or array containing the image
	 * @param page	the page number of the image
	 * @return	an Image object
	 */
	public static Image getJbig2Image(RandomAccessFileOrArray ra, int page) {
		if (page < 1)
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.page.number.must.be.gt.eq.1"));
		
		try {
			JBIG2SegmentReader sr = new JBIG2SegmentReader(ra);
			sr.read();
			JBIG2SegmentReader.JBIG2Page p = sr.getPage(page);
			Image img = new ImgJBIG2(p.pageBitmapWidth, p.pageBitmapHeight, p.getData(true), sr.getGlobal(true));
			return img;
		} catch (Exception e) {
	        throw new ExceptionConverter(e);
	    }
	}

	/***
	 * Gets the number of pages in a JBIG2 image.
	 * @param ra	a random acces file array containing a JBIG2 image
	 * @return	the number of pages
	 */
	public static int getNumberOfPages(RandomAccessFileOrArray ra) {
		try {
			JBIG2SegmentReader sr = new JBIG2SegmentReader(ra);
			sr.read();
			return sr.numberOfPages();
		} catch (Exception e) {
	        throw new ExceptionConverter(e);
	    }
    }
	
	
}
