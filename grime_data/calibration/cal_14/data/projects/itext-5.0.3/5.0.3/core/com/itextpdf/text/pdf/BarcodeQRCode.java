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

import com.itextpdf.text.pdf.qrcode.EncodeHintType;
import com.itextpdf.text.pdf.qrcode.WriterException;
import com.itextpdf.text.pdf.qrcode.ByteMatrix;
import com.itextpdf.text.pdf.qrcode.QRCodeWriter;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.codec.CCITTG4Encoder;
import java.awt.Canvas;
import java.awt.image.MemoryImageSource;
import java.util.Map;

/**
 * A QRCode implementation based on the zxing code.
 * @author Paulo Soares
 * @since 5.0.2
 */
public class BarcodeQRCode {
    ByteMatrix bm;

    /**
     * Creates the QR barcode. The barcode is always created with the smallest possible size and is then stretched
     * to the width and height given. Set the width and height to 1 to get an unscaled barcode.
     * @param content the text to be encoded
     * @param width the barcode width
     * @param height the barcode height
     * @param hints modifiers to change the way the barcode is create. They can be EncodeHintType.ERROR_CORRECTION
     * and EncodeHintType.CHARACTER_SET. For EncodeHintType.ERROR_CORRECTION the values can be ErrorCorrectionLevel.L, M, Q, H.
     * For EncodeHintType.CHARACTER_SET the values are strings and can be Cp437, Shift_JIS and ISO-8859-1 to ISO-8859-16. The default value is
     * ISO-8859-1.
     * @throws WriterException
     */
    public BarcodeQRCode(String content, int width, int height, Map<EncodeHintType,Object> hints) {
        try {
            QRCodeWriter qc = new QRCodeWriter();
            bm = qc.encode(content, width, height, hints);
        }
        catch (WriterException ex) {
            throw new ExceptionConverter(ex);
        }
    }

    private byte[] getBitMatrix() {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int stride = (width + 7) / 8;
        byte[] b = new byte[stride * height];
        byte[][] mt = bm.getArray();
        for (int y = 0; y < height; ++y) {
            byte[] line = mt[y];
            for (int x = 0; x < width; ++x) {
                if (line[x] != 0) {
                    int offset = stride * y + x / 8;
                    b[offset] |= (byte)(0x80 >> (x % 8));
                }
            }
        }
        return b;
    }

    /** Gets an <CODE>Image</CODE> with the barcode.
     * @return the barcode <CODE>Image</CODE>
     * @throws BadElementException on error
     */
    public Image getImage() throws BadElementException {
        byte[] b = getBitMatrix();
        byte g4[] = CCITTG4Encoder.compress(b, bm.getWidth(), bm.getHeight());
        return Image.getInstance(bm.getWidth(), bm.getHeight(), false, Image.CCITTG4, Image.CCITT_BLACKIS1, g4, null);
    }

    /** Creates a <CODE>java.awt.Image</CODE>.
     * @param foreground the color of the bars
     * @param background the color of the background
     * @return the image
     */
    public java.awt.Image createAwtImage(java.awt.Color foreground, java.awt.Color background) {
        int f = foreground.getRGB();
        int g = background.getRGB();
        Canvas canvas = new Canvas();

        int width = bm.getWidth();
        int height = bm.getHeight();
        int pix[] = new int[width * height];
        byte[][] mt = bm.getArray();
        for (int y = 0; y < height; ++y) {
            byte[] line = mt[y];
            for (int x = 0; x < width; ++x) {
                pix[y * width + x] = line[x] == 0 ? f : g;
            }
        }

        java.awt.Image img = canvas.createImage(new MemoryImageSource(width, height, pix, 0, width));
        return img;
    }
}
