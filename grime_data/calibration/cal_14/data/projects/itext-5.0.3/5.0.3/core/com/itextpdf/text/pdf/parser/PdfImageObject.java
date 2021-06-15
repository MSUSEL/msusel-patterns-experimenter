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

import com.itextpdf.text.Document;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.codec.PngWriter;
import com.itextpdf.text.pdf.codec.TIFFConstants;
import com.itextpdf.text.pdf.codec.TiffWriter;
import java.io.ByteArrayOutputStream;

/**
 * An object that contains an image dictionary and image bytes.
 * @since 5.0.2
 */
public class PdfImageObject {

	/** The image dictionary. */
	protected PdfDictionary dictionary;
	/** The image bytes. */
	protected byte[] streamBytes;
	
    private int pngColorType = -1;
    private int pngBitDepth;
    private int width;
    private int height;
    private int bpc;
    private byte[] palette;
    private byte[] icc;
    private int stride;
    private boolean  decoded;
    public static final String TYPE_PNG = "png";
    public static final String TYPE_JPG = "jpg";
    public static final String TYPE_JP2 = "jp2";
    public static final String TYPE_TIF = "tif";

    protected String fileType;

    public String getFileType() {
        return fileType;
    }
	/**
	 * Creates a PdfImage object.
	 * @param stream a PRStream
	 * @throws IOException
	 */
	public PdfImageObject(PRStream stream) {
		this.dictionary = stream;
        try {
            streamBytes = PdfReader.getStreamBytes(stream);
            decoded = true;
        }
        catch (Exception e) {
            try {
                streamBytes = PdfReader.getStreamBytesRaw(stream);
            }
            catch (Exception e2) {}
        }
	}
	
	/**
	 * Returns an entry from the image dictionary.
	 * @param key a key
	 * @return the value
	 */
	public PdfObject get(PdfName key) {
		return dictionary.get(key);
	}
	
	/**
	 * Returns the image dictionary.
	 * @return the dictionary
	 */
	public PdfDictionary getDictionary() {
		return dictionary;
	}

	/**
	 * Returns the image bytes.
	 * @return the streamBytes
	 */
	public byte[] getStreamBytes() {
		return streamBytes;
	}
	
    private void findColorspace(PdfObject colorspace, boolean allowIndexed) throws IOException {
        if (PdfName.DEVICEGRAY.equals(colorspace)) {
            stride = (width * bpc + 7) / 8;
            pngColorType = 0;
        }
        else if (PdfName.DEVICERGB.equals(colorspace)) {
            if (bpc == 8 || bpc == 16) {
                stride = (width * bpc * 3 + 7) / 8;
                pngColorType = 2;
            }
        }
        else if (colorspace instanceof PdfArray) {
            PdfArray ca = (PdfArray)colorspace;
            PdfObject tyca = ca.getDirectObject(0);
            if (PdfName.CALGRAY.equals(tyca)) {
                stride = (width * bpc + 7) / 8;
                pngColorType = 0;
            }
            else if (PdfName.CALRGB.equals(tyca)) {
                if (bpc == 8 || bpc == 16) {
                    stride = (width * bpc * 3 + 7) / 8;
                    pngColorType = 2;
                }
            }
            else if (PdfName.ICCBASED.equals(tyca)) {
                PRStream pr = (PRStream)ca.getDirectObject(1);
                int n = pr.getAsNumber(PdfName.N).intValue();
                if (n == 1) {
                    stride = (width * bpc + 7) / 8;
                    pngColorType = 0;
                    icc = PdfReader.getStreamBytes(pr);
                }
                else if (n == 3) {
                    stride = (width * bpc * 3 + 7) / 8;
                    pngColorType = 2;
                    icc = PdfReader.getStreamBytes(pr);
                }
            }
            else if (allowIndexed && PdfName.INDEXED.equals(tyca)) {
                findColorspace(ca.getDirectObject(1), false);
                if (pngColorType == 2) {
                    PdfObject id2 = ca.getDirectObject(3);
                    if (id2 instanceof PdfString) {
                        palette = ((PdfString)id2).getBytes();
                    }
                    else if (id2 instanceof PRStream) {
                        palette = PdfReader.getStreamBytes(((PRStream)id2));
                    }
                    stride = (width * bpc + 7) / 8;
                    pngColorType = 3;
                }
            }
        }
    }

    public byte[] getImageAsBytes() throws IOException {
        if (streamBytes == null)
            return null;
        if (!decoded) {
            PdfName filter = dictionary.getAsName(PdfName.FILTER);
            if (PdfName.DCTDECODE.equals(filter)) {
                fileType = TYPE_JPG;
                return streamBytes;
            }
            else if (PdfName.JPXDECODE.equals(filter)) {
                fileType = TYPE_JP2;
                return streamBytes;
            }
            return null;
        }
        pngColorType = -1;
        width = dictionary.getAsNumber(PdfName.WIDTH).intValue();
        height = dictionary.getAsNumber(PdfName.HEIGHT).intValue();
        bpc = dictionary.getAsNumber(PdfName.BITSPERCOMPONENT).intValue();
        pngBitDepth = bpc;
        PdfObject colorspace = dictionary.getDirectObject(PdfName.COLORSPACE);
        palette = null;
        icc = null;
        stride = 0;
        findColorspace(colorspace, true);
        ByteArrayOutputStream ms = new ByteArrayOutputStream();
        if (pngColorType < 0) {
            if (bpc != 8)
                return null;
            if (PdfName.DEVICECMYK.equals(colorspace)) {
            }
            else if (colorspace instanceof PdfArray) {
                PdfArray ca = (PdfArray)colorspace;
                PdfObject tyca = ca.getDirectObject(0);
                if (!PdfName.ICCBASED.equals(tyca))
                    return null;
                PRStream pr = (PRStream)ca.getDirectObject(1);
                int n = pr.getAsNumber(PdfName.N).intValue();
                if (n != 4) {
                    return null;
                }
                icc = PdfReader.getStreamBytes(pr);
            }
            else
                return null;
            stride = 4 * width;
            TiffWriter wr = new TiffWriter();
            wr.addField(new TiffWriter.FieldShort(TIFFConstants.TIFFTAG_SAMPLESPERPIXEL, 4));
            wr.addField(new TiffWriter.FieldShort(TIFFConstants.TIFFTAG_BITSPERSAMPLE, new int[]{8,8,8,8}));
            wr.addField(new TiffWriter.FieldShort(TIFFConstants.TIFFTAG_PHOTOMETRIC, TIFFConstants.PHOTOMETRIC_SEPARATED));
            wr.addField(new TiffWriter.FieldLong(TIFFConstants.TIFFTAG_IMAGEWIDTH, width));
            wr.addField(new TiffWriter.FieldLong(TIFFConstants.TIFFTAG_IMAGELENGTH, height));
            wr.addField(new TiffWriter.FieldShort(TIFFConstants.TIFFTAG_COMPRESSION, TIFFConstants.COMPRESSION_LZW));
            wr.addField(new TiffWriter.FieldShort(TIFFConstants.TIFFTAG_PREDICTOR, TIFFConstants.PREDICTOR_HORIZONTAL_DIFFERENCING));
            wr.addField(new TiffWriter.FieldLong(TIFFConstants.TIFFTAG_ROWSPERSTRIP, height));
            wr.addField(new TiffWriter.FieldRational(TIFFConstants.TIFFTAG_XRESOLUTION, new int[]{300,1}));
            wr.addField(new TiffWriter.FieldRational(TIFFConstants.TIFFTAG_YRESOLUTION, new int[]{300,1}));
            wr.addField(new TiffWriter.FieldShort(TIFFConstants.TIFFTAG_RESOLUTIONUNIT, TIFFConstants.RESUNIT_INCH));
            wr.addField(new TiffWriter.FieldAscii(TIFFConstants.TIFFTAG_SOFTWARE, Document.getVersion()));
            ByteArrayOutputStream comp = new ByteArrayOutputStream();
            TiffWriter.compressLZW(comp, 2, streamBytes, height, 4, stride);
            byte[] buf = comp.toByteArray();
            wr.addField(new TiffWriter.FieldImage(buf));
            wr.addField(new TiffWriter.FieldLong(TIFFConstants.TIFFTAG_STRIPBYTECOUNTS, buf.length));
            if (icc != null)
                wr.addField(new TiffWriter.FieldUndefined(TIFFConstants.TIFFTAG_ICCPROFILE, icc));
            wr.writeFile(ms);
            fileType = TYPE_TIF;
            return ms.toByteArray();
        }
        PngWriter png = new PngWriter(ms);
        png.writeHeader(width, height, pngBitDepth, pngColorType);
        if (icc != null)
            png.writeIccProfile(icc);
        if (palette != null)
            png.writePalette(palette);
        png.writeData(streamBytes, stride);
        png.writeEnd();
        fileType = TYPE_PNG;
        return ms.toByteArray();
    }

    /**
     * @since 5.0.3 renamed from getAwtImage()
     */
    public BufferedImage getBufferedImage() throws IOException {
        byte[] img = getImageAsBytes();
        if (img == null)
            return null;
        return ImageIO.read(new ByteArrayInputStream(img));
    }
}
