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
package com.itextpdf.text.pdf;

import java.awt.Font;

public class AsianFontMapper extends DefaultFontMapper {
	
	public static final String ChineseSimplifiedFont = "STSong-Light";
	public static final String ChineseSimplifiedEncoding_H = "UniGB-UCS2-H";
	public static final String ChineseSimplifiedEncoding_V = "UniGB-UCS2-V";
	
	public static final String ChineseTraditionalFont_MHei = "MHei-Medium";
	public static final String ChineseTraditionalFont_MSung = "MSung-Light";
	public static final String ChineseTraditionalEncoding_H = "UniCNS-UCS2-H";
	public static final String ChineseTraditionalEncoding_V = "UniCNS-UCS2-V";
	
	public static final String JapaneseFont_Go = "HeiseiKakuGo-W5";
	public static final String JapaneseFont_Min = "HeiseiMin-W3";
	public static final String JapaneseEncoding_H = "UniJIS-UCS2-H";
	public static final String JapaneseEncoding_V = "UniJIS-UCS2-V";
	public static final String JapaneseEncoding_HW_H = "UniJIS-UCS2-HW-H";
	public static final String JapaneseEncoding_HW_V = "UniJIS-UCS2-HW-V";
	
	public static final String KoreanFont_GoThic = "HYGoThic-Medium";
	public static final String KoreanFont_SMyeongJo = "HYSMyeongJo-Medium";
	public static final String KoreanEncoding_H = "UniKS-UCS2-H";
	public static final String KoreanEncoding_V = "UniKS-UCS2-V";
	
	private final String defaultFont;
	private final String encoding;

	public AsianFontMapper(String font, String encoding) {
		super();
		
		this.defaultFont = font;
		this.encoding = encoding;
	}

	public BaseFont awtToPdf(Font font) {
		try {
			BaseFontParameters p = getBaseFontParameters(font.getFontName());
			if (p != null){
				return BaseFont.createFont(p.fontName, p.encoding, p.embedded, p.cached, p.ttfAfm, p.pfb);
			}else{
				return BaseFont.createFont(defaultFont, encoding, true);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
