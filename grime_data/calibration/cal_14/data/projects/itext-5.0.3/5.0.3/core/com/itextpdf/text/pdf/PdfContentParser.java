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

import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PRTokeniser.TokenType;
/**
 * Parses the page or template content.
 * @author Paulo Soares
 */
public class PdfContentParser {

    /**
     * Commands have this type.
     */
    public static final int COMMAND_TYPE = 200;
    /**
     * Holds value of property tokeniser.
     */
    private PRTokeniser tokeniser;

    /**
     * Creates a new instance of PdfContentParser
     * @param tokeniser the tokeniser with the content
     */
    public PdfContentParser(PRTokeniser tokeniser) {
        this.tokeniser = tokeniser;
    }

    /**
     * Parses a single command from the content. Each command is output as an array of arguments
     * having the command itself as the last element. The returned array will be empty if the
     * end of content was reached.
     * @param ls an <CODE>ArrayList</CODE> to use. It will be cleared before using. If it's
     * <CODE>null</CODE> will create a new <CODE>ArrayList</CODE>
     * @return the same <CODE>ArrayList</CODE> given as argument or a new one
     * @throws IOException on error
     */
    public ArrayList<PdfObject> parse(ArrayList<PdfObject> ls) throws IOException {
        if (ls == null)
            ls = new ArrayList<PdfObject>();
        else
            ls.clear();
        PdfObject ob = null;
        while ((ob = readPRObject()) != null) {
            ls.add(ob);
            if (ob.type() == COMMAND_TYPE)
                break;
        }
        return ls;
    }

    /**
     * Gets the tokeniser.
     * @return the tokeniser.
     */
    public PRTokeniser getTokeniser() {
        return this.tokeniser;
    }

    /**
     * Sets the tokeniser.
     * @param tokeniser the tokeniser
     */
    public void setTokeniser(PRTokeniser tokeniser) {
        this.tokeniser = tokeniser;
    }

    /**
     * Reads a dictionary. The tokeniser must be positioned past the "&lt;&lt;" token.
     * @return the dictionary
     * @throws IOException on error
     */
    public PdfDictionary readDictionary() throws IOException {
        PdfDictionary dic = new PdfDictionary();
        while (true) {
            if (!nextValidToken())
                throw new IOException(MessageLocalization.getComposedMessage("unexpected.end.of.file"));
                if (tokeniser.getTokenType() == TokenType.END_DIC)
                    break;
                if (tokeniser.getTokenType() != TokenType.NAME)
                    throw new IOException(MessageLocalization.getComposedMessage("dictionary.key.is.not.a.name"));
                PdfName name = new PdfName(tokeniser.getStringValue(), false);
                PdfObject obj = readPRObject();
                int type = obj.type();
                if (-type == TokenType.END_DIC.ordinal())
                    throw new IOException(MessageLocalization.getComposedMessage("unexpected.gt.gt"));
                if (-type == TokenType.END_ARRAY.ordinal())
                    throw new IOException(MessageLocalization.getComposedMessage("unexpected.close.bracket"));
                dic.put(name, obj);
        }
        return dic;
    }

    /**
     * Reads an array. The tokeniser must be positioned past the "[" token.
     * @return an array
     * @throws IOException on error
     */
    public PdfArray readArray() throws IOException {
        PdfArray array = new PdfArray();
        while (true) {
            PdfObject obj = readPRObject();
            int type = obj.type();
            if (-type == TokenType.END_ARRAY.ordinal())
                break;
            if (-type == TokenType.END_DIC.ordinal())
                throw new IOException(MessageLocalization.getComposedMessage("unexpected.gt.gt"));
            array.add(obj);
        }
        return array;
    }

    /**
     * Reads a pdf object.
     * @return the pdf object
     * @throws IOException on error
     */
    public PdfObject readPRObject() throws IOException {
        if (!nextValidToken())
            return null;
        TokenType type = tokeniser.getTokenType();
        switch (type) {
            case START_DIC: {
                PdfDictionary dic = readDictionary();
                return dic;
            }
            case START_ARRAY:
                return readArray();
            case STRING:
                PdfString str = new PdfString(tokeniser.getStringValue(), null).setHexWriting(tokeniser.isHexString());
                return str;
            case NAME:
                return new PdfName(tokeniser.getStringValue(), false);
            case NUMBER:
                return new PdfNumber(tokeniser.getStringValue());
            case OTHER:
                return new PdfLiteral(COMMAND_TYPE, tokeniser.getStringValue());
            default:
                return new PdfLiteral(-type.ordinal(), tokeniser.getStringValue());
        }
    }

    /**
     * Reads the next token skipping over the comments.
     * @return <CODE>true</CODE> if a token was read, <CODE>false</CODE> if the end of content was reached
     * @throws IOException on error
     */
    public boolean nextValidToken() throws IOException {
        while (tokeniser.nextToken()) {
            if (tokeniser.getTokenType() == TokenType.COMMENT)
                continue;
            return true;
        }
        return false;
    }
}
