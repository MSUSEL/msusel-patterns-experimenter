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
import java.io.OutputStream;

/**
 * A <CODE>PdfString</CODE>-class is the PDF-equivalent of a
 * JAVA-<CODE>String</CODE>-object.
 * <P>
 * A string is a sequence of characters delimited by parenthesis.
 * If a string is too long to be conveniently placed on a single line, it may
 * be split across multiple lines by using the backslash character (\) at the
 * end of a line to indicate that the string continues on the following line.
 * Within a string, the backslash character is used as an escape to specify
 * unbalanced parenthesis, non-printing ASCII characters, and the backslash
 * character itself. Use of the \<I>ddd</I> escape sequence is the preferred
 * way to represent characters outside the printable ASCII character set.<BR>
 * This object is described in the 'Portable Document Format Reference Manual
 * version 1.7' section 3.2.3 (page 53-56).
 *
 * @see PdfObject
 * @see BadPdfFormatException
 */
public class PdfString extends PdfObject {
    
    // CLASS VARIABLES
    
    /** The value of this object. */
    protected String value = NOTHING;
    
    protected String originalValue = null;
    
    /** The encoding. */
    protected String encoding = TEXT_PDFDOCENCODING;
    
    protected int objNum = 0;
    
    protected int objGen = 0;
    
    protected boolean hexWriting = false;

    // CONSTRUCTORS
    
    /**
     * Constructs an empty <CODE>PdfString</CODE>-object.
     */
    public PdfString() {
        super(STRING);
    }
    
    /**
     * Constructs a <CODE>PdfString</CODE>-object containing a string in the
     * standard encoding <CODE>TEXT_PDFDOCENCODING</CODE>.
     *
     * @param value    the content of the string
     */
    public PdfString(String value) {
        super(STRING);
        this.value = value;
    }
    
    /**
     * Constructs a <CODE>PdfString</CODE>-object containing a string in the
     * specified encoding.
     *
     * @param value    the content of the string
     * @param encoding an encoding
     */
    public PdfString(String value, String encoding) {
        super(STRING);
        this.value = value;
        this.encoding = encoding;
    }
    
    /**
     * Constructs a <CODE>PdfString</CODE>-object.
     *
     * @param bytes    an array of <CODE>byte</CODE>
     */
    public PdfString(byte[] bytes) {
        super(STRING);
        value = PdfEncodings.convertToString(bytes, null);
        encoding = NOTHING;
    }
    
    // methods overriding some methods in PdfObject
    
    /**
     * Writes the PDF representation of this <CODE>PdfString</CODE> as an array
     * of <CODE>byte</CODE> to the specified <CODE>OutputStream</CODE>.
     * 
     * @param writer for backwards compatibility
     * @param os The <CODE>OutputStream</CODE> to write the bytes to.
     */
    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        byte b[] = getBytes();
        PdfEncryption crypto = null;
        if (writer != null)
            crypto = writer.getEncryption();
        if (crypto != null && !crypto.isEmbeddedFilesOnly())
            b = crypto.encryptByteArray(b);
        if (hexWriting) {
            ByteBuffer buf = new ByteBuffer();
            buf.append('<');
            int len = b.length;
            for (int k = 0; k < len; ++k)
                buf.appendHex(b[k]);
            buf.append('>');
            os.write(buf.toByteArray());
        }
        else
            os.write(PdfContentByte.escapeString(b));
    }
    
    /**
     * Returns the <CODE>String</CODE> value of this <CODE>PdfString</CODE>-object.
     *
     * @return A <CODE>String</CODE>
     */
    public String toString() {
        return value;
    }
    
    public byte[] getBytes() {
        if (bytes == null) {
            if (encoding != null && encoding.equals(TEXT_UNICODE) && PdfEncodings.isPdfDocEncoding(value))
                bytes = PdfEncodings.convertToBytes(value, TEXT_PDFDOCENCODING);
            else
                bytes = PdfEncodings.convertToBytes(value, encoding);
        }
        return bytes;
    }
    
    // other methods
    
    /**
     * Returns the Unicode <CODE>String</CODE> value of this
     * <CODE>PdfString</CODE>-object.
     *
     * @return A <CODE>String</CODE>
     */
    public String toUnicodeString() {
        if (encoding != null && encoding.length() != 0)
            return value;
        getBytes();
        if (bytes.length >= 2 && bytes[0] == (byte)254 && bytes[1] == (byte)255)
            return PdfEncodings.convertToString(bytes, PdfObject.TEXT_UNICODE);
        else
            return PdfEncodings.convertToString(bytes, PdfObject.TEXT_PDFDOCENCODING);
    }
    
    /**
     * Gets the encoding of this string.
     *
     * @return a <CODE>String</CODE>
     */
    public String getEncoding() {
        return encoding;
    }
    
    void setObjNum(int objNum, int objGen) {
        this.objNum = objNum;
        this.objGen = objGen;
    }
    
    /**
     * Decrypt an encrypted <CODE>PdfString</CODE>
     */
    void decrypt(PdfReader reader) {
        PdfEncryption decrypt = reader.getDecrypt();
        if (decrypt != null) {
            originalValue = value;
            decrypt.setHashKey(objNum, objGen);
            bytes = PdfEncodings.convertToBytes(value, null);
            bytes = decrypt.decryptByteArray(bytes);
            value = PdfEncodings.convertToString(bytes, null);
        }
    }
   
    public byte[] getOriginalBytes() {
        if (originalValue == null)
            return getBytes();
        return PdfEncodings.convertToBytes(originalValue, null);
    }
    
    public PdfString setHexWriting(boolean hexWriting) {
        this.hexWriting = hexWriting;
        return this;
    }
    
    public boolean isHexWriting() {
        return hexWriting;
    }
}