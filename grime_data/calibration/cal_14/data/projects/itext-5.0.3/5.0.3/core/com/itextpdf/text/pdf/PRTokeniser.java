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
import com.itextpdf.text.exceptions.InvalidPdfException;
import com.itextpdf.text.error_messages.MessageLocalization;
/**
 *
 * @author  Paulo Soares
 */
public class PRTokeniser {

    /**
     * Enum representing the possible token types
     * @since 5.0.1
     */ 
    public enum TokenType {
        NUMBER,
        STRING,
        NAME,
        COMMENT,
        START_ARRAY,
        END_ARRAY,
        START_DIC,
        END_DIC,
        REF,
        OTHER,
        ENDOFFILE
    }
    
    public static final boolean delims[] = {
        true,  true,  false, false, false, false, false, false, false, false,
        true,  true,  false, true,  true,  false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, true,  false, false, false, false, true,  false,
        false, true,  true,  false, false, false, false, false, true,  false,
        false, false, false, false, false, false, false, false, false, false,
        false, true,  false, true,  false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, true,  false, true,  false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false};
    
    static final String EMPTY = "";

    
    protected RandomAccessFileOrArray file;
    protected TokenType type;
    protected String stringValue;
    protected int reference;
    protected int generation;
    protected boolean hexString;
       
    public PRTokeniser(String filename) throws IOException {
        file = new RandomAccessFileOrArray(filename);
    }

    public PRTokeniser(byte pdfIn[]) {
        file = new RandomAccessFileOrArray(pdfIn);
    }
    
    public PRTokeniser(RandomAccessFileOrArray file) {
        this.file = file;
    }
    
    public void seek(int pos) throws IOException {
        file.seek(pos);
    }
    
    public int getFilePointer() throws IOException {
        return file.getFilePointer();
    }

    public void close() throws IOException {
        file.close();
    }
    
    public int length() throws IOException {
        return file.length();
    }

    public int read() throws IOException {
        return file.read();
    }
    
    public RandomAccessFileOrArray getSafeFile() {
        return new RandomAccessFileOrArray(file);
    }
    
    public RandomAccessFileOrArray getFile() {
        return file;
    }
    
    public String readString(int size) throws IOException {
        StringBuffer buf = new StringBuffer();
        int ch;
        while ((size--) > 0) {
            ch = file.read();
            if (ch == -1)
                break;
            buf.append((char)ch);
        }
        return buf.toString();
    }

    public static final boolean isWhitespace(int ch) {
        return (ch == 0 || ch == 9 || ch == 10 || ch == 12 || ch == 13 || ch == 32);
    }
    
    public static final boolean isDelimiter(int ch) {
        return (ch == '(' || ch == ')' || ch == '<' || ch == '>' || ch == '[' || ch == ']' || ch == '/' || ch == '%');
    }

    public static final boolean isDelimiterWhitespace(int ch) {
        return delims[ch + 1];
    }

    public TokenType getTokenType() {
        return type;
    }
    
    public String getStringValue() {
        return stringValue;
    }
    
    public int getReference() {
        return reference;
    }
    
    public int getGeneration() {
        return generation;
    }
    
    public void backOnePosition(int ch) {
        if (ch != -1)
            file.pushBack((byte)ch);
    }
    
    public void throwError(String error) throws IOException {
        throw new InvalidPdfException(MessageLocalization.getComposedMessage("1.at.file.pointer.2", error, String.valueOf(file.getFilePointer())));
    }
    
    public char checkPdfHeader() throws IOException {
        file.setStartOffset(0);
        String str = readString(1024);
        int idx = str.indexOf("%PDF-");
        if (idx < 0)
            throw new InvalidPdfException(MessageLocalization.getComposedMessage("pdf.header.not.found"));
        file.setStartOffset(idx);
        return str.charAt(idx + 7);
    }
    
    public void checkFdfHeader() throws IOException {
        file.setStartOffset(0);
        String str = readString(1024);
        int idx = str.indexOf("%FDF-");
        if (idx < 0)
            throw new InvalidPdfException(MessageLocalization.getComposedMessage("fdf.header.not.found"));
        file.setStartOffset(idx);
    }

    public int getStartxref() throws IOException {
        int size = Math.min(1024, file.length());
        int pos = file.length() - size;
        file.seek(pos);
        String str = readString(1024);
        int idx = str.lastIndexOf("startxref");
        if (idx < 0)
            throw new InvalidPdfException(MessageLocalization.getComposedMessage("pdf.startxref.not.found"));
        return pos + idx;
    }

    public static int getHex(int v) {
        if (v >= '0' && v <= '9')
            return v - '0';
        if (v >= 'A' && v <= 'F')
            return v - 'A' + 10;
        if (v >= 'a' && v <= 'f')
            return v - 'a' + 10;
        return -1;
    }
    
    public void nextValidToken() throws IOException {
        int level = 0;
        String n1 = null;
        String n2 = null;
        int ptr = 0;
        while (nextToken()) {
            if (type == TokenType.COMMENT)
                continue;
            switch (level) {
                case 0:
                {
                    if (type != TokenType.NUMBER)
                        return;
                    ptr = file.getFilePointer();
                    n1 = stringValue;
                    ++level;
                    break;
                }
                case 1:
                {
                    if (type != TokenType.NUMBER) {
                        file.seek(ptr);
                        type = TokenType.NUMBER;
                        stringValue = n1;
                        return;
                    }
                    n2 = stringValue;
                    ++level;
                    break;
                }
                default:
                {
                    if (type != TokenType.OTHER || !stringValue.equals("R")) {
                        file.seek(ptr);
                        type = TokenType.NUMBER;
                        stringValue = n1;
                        return;
                    }
                    type = TokenType.REF;
                    reference = Integer.parseInt(n1);
                    generation = Integer.parseInt(n2);
                    return;
                }
            }
        }
        // if we hit here, the file is either corrupt (stream ended unexpectedly),
        // or the last token ended exactly at the end of a stream.  This last
        // case can occur inside an Object Stream.
    }
    
    public boolean nextToken() throws IOException {
        int ch = 0;
        do {
            ch = file.read();
        } while (ch != -1 && isWhitespace(ch));
        if (ch == -1){
            type = TokenType.ENDOFFILE;
            return false;
        }

        // Note:  We have to initialize stringValue here, after we've looked for the end of the stream,
        // to ensure that we don't lose the value of a token that might end exactly at the end
        // of the stream
        StringBuffer outBuf = null;
        stringValue = EMPTY;

        switch (ch) {
            case '[':
                type = TokenType.START_ARRAY;
                break;
            case ']':
                type = TokenType.END_ARRAY;
                break;
            case '/':
            {
                outBuf = new StringBuffer();
                type = TokenType.NAME;
                while (true) {
                    ch = file.read();
                    if (delims[ch + 1])
                        break;
                    if (ch == '#') {
                        ch = (getHex(file.read()) << 4) + getHex(file.read());
                    }
                    outBuf.append((char)ch);
                }
                backOnePosition(ch);
                break;
            }
            case '>':
                ch = file.read();
                if (ch != '>')
                    throwError(MessageLocalization.getComposedMessage("greaterthan.not.expected"));
                type = TokenType.END_DIC;
                break;
            case '<':
            {
                int v1 = file.read();
                if (v1 == '<') {
                    type = TokenType.START_DIC;
                    break;
                }
                outBuf = new StringBuffer();
                type = TokenType.STRING;
                hexString = true;
                int v2 = 0;
                while (true) {
                    while (isWhitespace(v1))
                        v1 = file.read();
                    if (v1 == '>')
                        break;
                    v1 = getHex(v1);
                    if (v1 < 0)
                        break;
                    v2 = file.read();
                    while (isWhitespace(v2))
                        v2 = file.read();
                    if (v2 == '>') {
                        ch = v1 << 4;
                        outBuf.append((char)ch);
                        break;
                    }
                    v2 = getHex(v2);
                    if (v2 < 0)
                        break;
                    ch = (v1 << 4) + v2;
                    outBuf.append((char)ch);
                    v1 = file.read();
                }
                if (v1 < 0 || v2 < 0)
                    throwError(MessageLocalization.getComposedMessage("error.reading.string"));
                break;
            }
            case '%':
                type = TokenType.COMMENT;
                do {
                    ch = file.read();
                } while (ch != -1 && ch != '\r' && ch != '\n');
                break;
            case '(':
            {
                outBuf = new StringBuffer();
                type = TokenType.STRING;
                hexString = false;
                int nesting = 0;
                while (true) {
                    ch = file.read();
                    if (ch == -1)
                        break;
                    if (ch == '(') {
                        ++nesting;
                    }
                    else if (ch == ')') {
                        --nesting;
                    }
                    else if (ch == '\\') {
                        boolean lineBreak = false;
                        ch = file.read();
                        switch (ch) {
                            case 'n':
                                ch = '\n';
                                break;
                            case 'r':
                                ch = '\r';
                                break;
                            case 't':
                                ch = '\t';
                                break;
                            case 'b':
                                ch = '\b';
                                break;
                            case 'f':
                                ch = '\f';
                                break;
                            case '(':
                            case ')':
                            case '\\':
                                break;
                            case '\r':
                                lineBreak = true;
                                ch = file.read();
                                if (ch != '\n')
                                    backOnePosition(ch);
                                break;
                            case '\n':
                                lineBreak = true;
                                break;
                            default:
                            {
                                if (ch < '0' || ch > '7') {
                                    break;
                                }
                                int octal = ch - '0';
                                ch = file.read();
                                if (ch < '0' || ch > '7') {
                                    backOnePosition(ch);
                                    ch = octal;
                                    break;
                                }
                                octal = (octal << 3) + ch - '0';
                                ch = file.read();
                                if (ch < '0' || ch > '7') {
                                    backOnePosition(ch);
                                    ch = octal;
                                    break;
                                }
                                octal = (octal << 3) + ch - '0';
                                ch = octal & 0xff;
                                break;
                            }
                        }
                        if (lineBreak)
                            continue;
                        if (ch < 0)
                            break;
                    }
                    else if (ch == '\r') {
                        ch = file.read();
                        if (ch < 0)
                            break;
                        if (ch != '\n') {
                            backOnePosition(ch);
                            ch = '\n';
                        }
                    }
                    if (nesting == -1)
                        break;
                    outBuf.append((char)ch);
                }
                if (ch == -1)
                    throwError(MessageLocalization.getComposedMessage("error.reading.string"));
                break;
            }
            default:
            {
                outBuf = new StringBuffer();
                if (ch == '-' || ch == '+' || ch == '.' || (ch >= '0' && ch <= '9')) {
                    type = TokenType.NUMBER;
                    do {
                        outBuf.append((char)ch);
                        ch = file.read();
                    } while (ch != -1 && ((ch >= '0' && ch <= '9') || ch == '.'));
                }
                else {
                    type = TokenType.OTHER;
                    do {
                        outBuf.append((char)ch);
                        ch = file.read();
                    } while (!delims[ch + 1]);
                }
                backOnePosition(ch);
                break;
            }
        }
        if (outBuf != null)
            stringValue = outBuf.toString();
        return true;
    }
    
    public int intValue() {
        return Integer.parseInt(stringValue);
    }
    
    public boolean readLineSegment(byte input[]) throws IOException {
        int c = -1;
        boolean eol = false;
        int ptr = 0;
        int len = input.length;
	// ssteward, pdftk-1.10, 040922: 
	// skip initial whitespace; added this because PdfReader.rebuildXref()
	// assumes that line provided by readLineSegment does not have init. whitespace;
	if ( ptr < len ) {
	    while ( isWhitespace( (c = read()) ) );
	}
	while ( !eol && ptr < len ) {
	    switch (c) {
                case -1:
                case '\n':
                    eol = true;
                    break;
                case '\r':
                    eol = true;
                    int cur = getFilePointer();
                    if ((read()) != '\n') {
                        seek(cur);
                    }
                    break;
                default:
                    input[ptr++] = (byte)c;
                    break;
            }

	    // break loop? do it before we read() again
	    if( eol || len <= ptr ) {
		break;
	    }
	    else {
		c = read();
	    }
        }
        if (ptr >= len) {
            eol = false;
            while (!eol) {
                switch (c = read()) {
                    case -1:
                    case '\n':
                        eol = true;
                        break;
                    case '\r':
                        eol = true;
                        int cur = getFilePointer();
                        if ((read()) != '\n') {
                            seek(cur);
                        }
                        break;
                }
            }
        }
        
        if ((c == -1) && (ptr == 0)) {
            return false;
        }
        if (ptr + 2 <= len) {
            input[ptr++] = (byte)' ';
            input[ptr] = (byte)'X';
        }
        return true;
    }
    
    public static int[] checkObjectStart(byte line[]) {
        try {
            PRTokeniser tk = new PRTokeniser(line);
            int num = 0;
            int gen = 0;
            if (!tk.nextToken() || tk.getTokenType() != TokenType.NUMBER)
                return null;
            num = tk.intValue();
            if (!tk.nextToken() || tk.getTokenType() != TokenType.NUMBER)
                return null;
            gen = tk.intValue();
            if (!tk.nextToken())
                return null;
            if (!tk.getStringValue().equals("obj"))
                return null;
            return new int[]{num, gen};
        }
        catch (Exception ioe) {
            // empty on purpose
        }
        return null;
    }
    
    public boolean isHexString() {
        return this.hexString;
    }
    
}
