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

import java.io.IOException;

import com.itextpdf.text.pdf.PdfReader;

/**
 * Extracts text from a PDF file.
 * @since	2.1.4
 */
public final class PdfTextExtractor {

	/**
	 * This class only contains static methods.
	 */
	private PdfTextExtractor()  {
	}
	
    /**
     * Extract text from a specified page using an extraction strategy.
     * @param reader the reader to extract text from
     * @param pageNumber the page to extract text from
     * @param strategy the strategy to use for extracting text
     * @return the extracted text
     * @throws IOException if any operation fails while reading from the provided PdfReader
     * @since 5.0.2
     */
    public static String getTextFromPage(PdfReader reader, int pageNumber, TextExtractionStrategy strategy) throws IOException{
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        return parser.processContent(pageNumber, strategy).getResultantText();
        
    }
    
    /**
     * Extract text from a specified page using the default strategy.
     * <p><strong>Note:</strong> the default strategy is subject to change.  If using a specific strategy
     * is important, use {@link PdfTextExtractor#getTextFromPage(PdfReader, int, TextExtractionStrategy)}
     * @param reader the reader to extract text from
     * @param pageNumber the page to extract text from
     * @return the extracted text
     * @throws IOException if any operation fails while reading from the provided PdfReader
     * @since 5.0.2
     */
    public static String getTextFromPage(PdfReader reader, int pageNumber) throws IOException{
        return getTextFromPage(reader, pageNumber, new LocationTextExtractionStrategy());
    }

}
