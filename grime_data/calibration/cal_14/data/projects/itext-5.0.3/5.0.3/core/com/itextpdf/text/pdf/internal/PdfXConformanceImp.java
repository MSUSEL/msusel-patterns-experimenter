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
package com.itextpdf.text.pdf.internal;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ExtendedColor;
import com.itextpdf.text.pdf.PatternColor;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfXConformanceException;
import com.itextpdf.text.pdf.ShadingColor;
import com.itextpdf.text.pdf.SpotColor;
import com.itextpdf.text.pdf.interfaces.PdfXConformance;

public class PdfXConformanceImp implements PdfXConformance {

    /** A key for an aspect that can be checked for PDF/X Conformance. */
    public static final int PDFXKEY_COLOR = 1;
    /** A key for an aspect that can be checked for PDF/X Conformance. */
    public static final int PDFXKEY_CMYK = 2;
    /** A key for an aspect that can be checked for PDF/X Conformance. */
    public static final int PDFXKEY_RGB = 3;
    /** A key for an aspect that can be checked for PDF/X Conformance. */
    public static final int PDFXKEY_FONT = 4;
    /** A key for an aspect that can be checked for PDF/X Conformance. */
    public static final int PDFXKEY_IMAGE = 5;
    /** A key for an aspect that can be checked for PDF/X Conformance. */
    public static final int PDFXKEY_GSTATE = 6;
    /** A key for an aspect that can be checked for PDF/X Conformance. */
    public static final int PDFXKEY_LAYER = 7;
    
    /**
     * The value indicating if the PDF has to be in conformance with PDF/X.
     */
    protected int pdfxConformance = PdfWriter.PDFXNONE;
    
    /**
     * @see com.itextpdf.text.pdf.interfaces.PdfXConformance#setPDFXConformance(int)
     */
    public void setPDFXConformance(int pdfxConformance) {
        this.pdfxConformance = pdfxConformance;
    }

	/**
	 * @see com.itextpdf.text.pdf.interfaces.PdfXConformance#getPDFXConformance()
	 */
	public int getPDFXConformance() {
		return pdfxConformance;
	}
    
    /**
     * Checks if the PDF/X Conformance is necessary.
     * @return true if the PDF has to be in conformance with any of the PDF/X specifications
     */
    public boolean isPdfX() {
    	return pdfxConformance != PdfWriter.PDFXNONE;
    }
    /**
     * Checks if the PDF has to be in conformance with PDF/X-1a:2001
     * @return true of the PDF has to be in conformance with PDF/X-1a:2001
     */
    public boolean isPdfX1A2001() {
    	return pdfxConformance == PdfWriter.PDFX1A2001;
    }
    /**
     * Checks if the PDF has to be in conformance with PDF/X-3:2002
     * @return true of the PDF has to be in conformance with PDF/X-3:2002
     */
    public boolean isPdfX32002() {
    	return pdfxConformance == PdfWriter.PDFX32002;
    }
    
    /**
     * Checks if the PDF has to be in conformance with PDFA1
     * @return true of the PDF has to be in conformance with PDFA1
     */
    public boolean isPdfA1() {
    	return pdfxConformance == PdfWriter.PDFA1A || pdfxConformance == PdfWriter.PDFA1B;
    }
    
    /**
     * Checks if the PDF has to be in conformance with PDFA1A
     * @return true of the PDF has to be in conformance with PDFA1A
     */
    public boolean isPdfA1A() {
    	return pdfxConformance == PdfWriter.PDFA1A;
    }
    
    public void completeInfoDictionary(PdfDictionary info) {
        if (isPdfX() && !isPdfA1()) {
            if (info.get(PdfName.GTS_PDFXVERSION) == null) {
                if (isPdfX1A2001()) {
                    info.put(PdfName.GTS_PDFXVERSION, new PdfString("PDF/X-1:2001"));
                    info.put(new PdfName("GTS_PDFXConformance"), new PdfString("PDF/X-1a:2001"));
                }
                else if (isPdfX32002())
                    info.put(PdfName.GTS_PDFXVERSION, new PdfString("PDF/X-3:2002"));
            }
            if (info.get(PdfName.TITLE) == null) {
                info.put(PdfName.TITLE, new PdfString("Pdf document"));
            }
            if (info.get(PdfName.CREATOR) == null) {
                info.put(PdfName.CREATOR, new PdfString("Unknown"));
            }
            if (info.get(PdfName.TRAPPED) == null) {
                info.put(PdfName.TRAPPED, new PdfName("False"));
            }
        }
    }
    
    public void completeExtraCatalog(PdfDictionary extraCatalog) {
        if (isPdfX() && !isPdfA1()) {
            if (extraCatalog.get(PdfName.OUTPUTINTENTS) == null) {
                PdfDictionary out = new PdfDictionary(PdfName.OUTPUTINTENT);
                out.put(PdfName.OUTPUTCONDITION, new PdfString("SWOP CGATS TR 001-1995"));
                out.put(PdfName.OUTPUTCONDITIONIDENTIFIER, new PdfString("CGATS TR 001"));
                out.put(PdfName.REGISTRYNAME, new PdfString("http://www.color.org"));
                out.put(PdfName.INFO, new PdfString(""));
                out.put(PdfName.S, PdfName.GTS_PDFX);
                extraCatalog.put(PdfName.OUTPUTINTENTS, new PdfArray(out));
            }
        }
    }
    
    /**
	 * Business logic that checks if a certain object is in conformance with PDF/X.
     * @param writer	the writer that is supposed to write the PDF/X file
     * @param key		the type of PDF/X conformance that has to be checked
     * @param obj1		the object that is checked for conformance
     */
    public static void checkPDFXConformance(PdfWriter writer, int key, Object obj1) {
        if (writer == null || !writer.isPdfX())
            return;
        int conf = writer.getPDFXConformance();
        switch (key) {
            case PDFXKEY_COLOR:
                switch (conf) {
                    case PdfWriter.PDFX1A2001:
                        if (obj1 instanceof ExtendedColor) {
                            ExtendedColor ec = (ExtendedColor)obj1;
                            switch (ec.getType()) {
                                case ExtendedColor.TYPE_CMYK:
                                case ExtendedColor.TYPE_GRAY:
                                    return;
                                case ExtendedColor.TYPE_RGB:
                                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed"));
                                case ExtendedColor.TYPE_SEPARATION:
                                    SpotColor sc = (SpotColor)ec;
                                    checkPDFXConformance(writer, PDFXKEY_COLOR, sc.getPdfSpotColor().getAlternativeCS());
                                    break;
                                case ExtendedColor.TYPE_SHADING:
                                    ShadingColor xc = (ShadingColor)ec;
                                    checkPDFXConformance(writer, PDFXKEY_COLOR, xc.getPdfShadingPattern().getShading().getColorSpace());
                                    break;
                                case ExtendedColor.TYPE_PATTERN:
                                    PatternColor pc = (PatternColor)ec;
                                    checkPDFXConformance(writer, PDFXKEY_COLOR, pc.getPainter().getDefaultColor());
                                    break;
                            }
                        }
                        else if (obj1 instanceof BaseColor)
                            throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed"));
                        break;
                }
                break;
            case PDFXKEY_CMYK:
                break;
            case PDFXKEY_RGB:
                if (conf == PdfWriter.PDFX1A2001)
                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed"));
                break;
            case PDFXKEY_FONT:
                if (!((BaseFont)obj1).isEmbedded())
                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("all.the.fonts.must.be.embedded.this.one.isn.t.1", ((BaseFont)obj1).getPostscriptFontName()));
                break;
            case PDFXKEY_IMAGE:
                PdfImage image = (PdfImage)obj1;
                if (image.get(PdfName.SMASK) != null)
                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("the.smask.key.is.not.allowed.in.images"));
                switch (conf) {
                    case PdfWriter.PDFX1A2001:
                        PdfObject cs = image.get(PdfName.COLORSPACE);
                        if (cs == null)
                            return;
                        if (cs.isName()) {
                            if (PdfName.DEVICERGB.equals(cs))
                                throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed"));
                        }
                        else if (cs.isArray()) {
                            if (PdfName.CALRGB.equals(((PdfArray)cs).getPdfObject(0)))
                                throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.calrgb.is.not.allowed"));
                        }
                        break;
                }
                break;
            case PDFXKEY_GSTATE:
                PdfDictionary gs = (PdfDictionary)obj1;
                PdfObject obj = gs.get(PdfName.BM);
                if (obj != null && !PdfGState.BM_NORMAL.equals(obj) && !PdfGState.BM_COMPATIBLE.equals(obj))
                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("blend.mode.1.not.allowed", obj.toString()));
                obj = gs.get(PdfName.CA);
                double v = 0.0;
                if (obj != null && (v = ((PdfNumber)obj).doubleValue()) != 1.0)
                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("transparency.is.not.allowed.ca.eq.1", String.valueOf(v)));
                obj = gs.get(PdfName.ca);
                v = 0.0;
                if (obj != null && (v = ((PdfNumber)obj).doubleValue()) != 1.0)
                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("transparency.is.not.allowed.ca.eq.1", String.valueOf(v)));
                break;
            case PDFXKEY_LAYER:
                throw new PdfXConformanceException(MessageLocalization.getComposedMessage("layers.are.not.allowed"));
        }
    }
}
