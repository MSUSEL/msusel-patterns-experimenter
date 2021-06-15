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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.pdf.collection.PdfCollectionItem;
/** Specifies a file or an URL. The file can be extern or embedded.
 *
 * @author Paulo Soares
 */
public class PdfFileSpecification extends PdfDictionary {
    protected PdfWriter writer;
    protected PdfIndirectReference ref;
    
    /** Creates a new instance of PdfFileSpecification. The static methods are preferred. */
    public PdfFileSpecification() {
        super(PdfName.FILESPEC);
    }
    
    /**
     * Creates a file specification of type URL.
     * @param writer the <CODE>PdfWriter</CODE>
     * @param url the URL
     * @return the file specification
     */    
    public static PdfFileSpecification url(PdfWriter writer, String url) {
        PdfFileSpecification fs = new PdfFileSpecification();
        fs.writer = writer;
        fs.put(PdfName.FS, PdfName.URL);
        fs.put(PdfName.F, new PdfString(url));
        return fs;
    }

    /**
     * Creates a file specification with the file embedded. The file may
     * come from the file system or from a byte array. The data is flate compressed.
     * @param writer the <CODE>PdfWriter</CODE>
     * @param filePath the file path
     * @param fileDisplay the file information that is presented to the user
     * @param fileStore the byte array with the file. If it is not <CODE>null</CODE>
     * it takes precedence over <CODE>filePath</CODE>
     * @throws IOException on error
     * @return the file specification
     */    
    public static PdfFileSpecification fileEmbedded(PdfWriter writer, String filePath, String fileDisplay, byte fileStore[]) throws IOException {
        return fileEmbedded(writer, filePath, fileDisplay, fileStore, PdfStream.BEST_COMPRESSION);
    }

    /**
     * Creates a file specification with the file embedded. The file may
     * come from the file system or from a byte array. The data is flate compressed.
     * @param writer the <CODE>PdfWriter</CODE>
     * @param filePath the file path
     * @param fileDisplay the file information that is presented to the user
     * @param fileStore the byte array with the file. If it is not <CODE>null</CODE>
     * it takes precedence over <CODE>filePath</CODE>
     * @param compressionLevel	the compression level to be used for compressing the file
     * it takes precedence over <CODE>filePath</CODE>
     * @throws IOException on error
     * @return the file specification
     * @since	2.1.3
     */    
    public static PdfFileSpecification fileEmbedded(PdfWriter writer, String filePath, String fileDisplay, byte fileStore[], int compressionLevel) throws IOException {
        return fileEmbedded(writer, filePath, fileDisplay, fileStore, null, null, compressionLevel);
    }
    
    
    /**
     * Creates a file specification with the file embedded. The file may
     * come from the file system or from a byte array.
     * @param writer the <CODE>PdfWriter</CODE>
     * @param filePath the file path
     * @param fileDisplay the file information that is presented to the user
     * @param fileStore the byte array with the file. If it is not <CODE>null</CODE>
     * it takes precedence over <CODE>filePath</CODE>
     * @param compress sets the compression on the data. Multimedia content will benefit little
     * from compression
     * @throws IOException on error
     * @return the file specification
     */    
    public static PdfFileSpecification fileEmbedded(PdfWriter writer, String filePath, String fileDisplay, byte fileStore[], boolean compress) throws IOException {
        return fileEmbedded(writer, filePath, fileDisplay, fileStore, null, null, compress ? PdfStream.BEST_COMPRESSION : PdfStream.NO_COMPRESSION);
    }
    
    /**
     * Creates a file specification with the file embedded. The file may
     * come from the file system or from a byte array.
     * @param writer the <CODE>PdfWriter</CODE>
     * @param filePath the file path
     * @param fileDisplay the file information that is presented to the user
     * @param fileStore the byte array with the file. If it is not <CODE>null</CODE>
     * it takes precedence over <CODE>filePath</CODE>
     * @param compress sets the compression on the data. Multimedia content will benefit little
     * from compression
     * @param mimeType the optional mimeType
     * @param fileParameter the optional extra file parameters such as the creation or modification date
     * @throws IOException on error
     * @return the file specification
     */    
    public static PdfFileSpecification fileEmbedded(PdfWriter writer, String filePath, String fileDisplay, byte fileStore[], boolean compress, String mimeType, PdfDictionary fileParameter) throws IOException {
    	return fileEmbedded(writer, filePath, fileDisplay, fileStore, mimeType, fileParameter, compress ? PdfStream.BEST_COMPRESSION : PdfStream.NO_COMPRESSION);
    }
    
    /**
     * Creates a file specification with the file embedded. The file may
     * come from the file system or from a byte array.
     * @param writer the <CODE>PdfWriter</CODE>
     * @param filePath the file path
     * @param fileDisplay the file information that is presented to the user
     * @param fileStore the byte array with the file. If it is not <CODE>null</CODE>
     * it takes precedence over <CODE>filePath</CODE>
     * @param mimeType the optional mimeType
     * @param fileParameter the optional extra file parameters such as the creation or modification date
     * @param compressionLevel the level of compression
     * @throws IOException on error
     * @return the file specification
     * @since	2.1.3
     */    
    public static PdfFileSpecification fileEmbedded(PdfWriter writer, String filePath, String fileDisplay, byte fileStore[], String mimeType, PdfDictionary fileParameter, int compressionLevel) throws IOException {
        PdfFileSpecification fs = new PdfFileSpecification();
        fs.writer = writer;
        fs.put(PdfName.F, new PdfString(fileDisplay));
        fs.setUnicodeFileName(fileDisplay, false);
        PdfEFStream stream;
        InputStream in = null;
        PdfIndirectReference ref;
        PdfIndirectReference refFileLength = null;
        try {
            if (fileStore == null) {
                refFileLength = writer.getPdfIndirectReference();
                File file = new File(filePath);
                if (file.canRead()) {
                    in = new FileInputStream(filePath);
                }
                else {
                    if (filePath.startsWith("file:/") || filePath.startsWith("http://") || filePath.startsWith("https://") || filePath.startsWith("jar:")) {
                        in = new URL(filePath).openStream();
                    }
                    else {
                        in = BaseFont.getResourceStream(filePath);
                        if (in == null)
                            throw new IOException(MessageLocalization.getComposedMessage("1.not.found.as.file.or.resource", filePath));
                    }
                }
                stream = new PdfEFStream(in, writer);
            }
            else {
                stream = new PdfEFStream(fileStore);
            }
            stream.put(PdfName.TYPE, PdfName.EMBEDDEDFILE);
            stream.flateCompress(compressionLevel);
            PdfDictionary param = new PdfDictionary();
            if (fileParameter != null) {
                param.merge(fileParameter);
            }

            if (fileStore != null) {
                param.put(PdfName.SIZE, new PdfNumber(stream.getRawLength()));
                stream.put(PdfName.PARAMS, param);
            }
            else
                stream.put(PdfName.PARAMS, refFileLength);

            if (mimeType != null)
                stream.put(PdfName.SUBTYPE, new PdfName(mimeType));

            ref = writer.addToBody(stream).getIndirectReference();
            if (fileStore == null) {
                stream.writeLength();
                param.put(PdfName.SIZE, new PdfNumber(stream.getRawLength()));
                writer.addToBody(param, refFileLength);
            }
        }
        finally {
            if (in != null)
                try{in.close();}catch(Exception e){}
        }
        PdfDictionary f = new PdfDictionary();
        f.put(PdfName.F, ref);
        f.put(PdfName.UF, ref);
        fs.put(PdfName.EF, f);
        return fs;
    }
    
    /**
     * Creates a file specification for an external file.
     * @param writer the <CODE>PdfWriter</CODE>
     * @param filePath the file path
     * @return the file specification
     */
    public static PdfFileSpecification fileExtern(PdfWriter writer, String filePath) {
        PdfFileSpecification fs = new PdfFileSpecification();
        fs.writer = writer;
        fs.put(PdfName.F, new PdfString(filePath));
        fs.setUnicodeFileName(filePath, false);
        return fs;
    }
    
    /**
     * Gets the indirect reference to this file specification.
     * Multiple invocations will retrieve the same value.
     * @throws IOException on error
     * @return the indirect reference
     */    
    public PdfIndirectReference getReference() throws IOException {
        if (ref != null)
            return ref;
        ref = writer.addToBody(this).getIndirectReference();
        return ref;
    }
    
    /**
     * Sets the file name (the key /F) string as an hex representation
     * to support multi byte file names. The name must have the slash and
     * backslash escaped according to the file specification rules
     * @param fileName the file name as a byte array
     */    
    public void setMultiByteFileName(byte fileName[]) {
        put(PdfName.F, new PdfString(fileName).setHexWriting(true));
    }
    
    /**
     * Adds the unicode file name (the key /UF). This entry was introduced
     * in PDF 1.7. The filename must have the slash and backslash escaped
     * according to the file specification rules.
     * @param filename	the filename
     * @param unicode	if true, the filename is UTF-16BE encoded; otherwise PDFDocEncoding is used;
     */    
    public void setUnicodeFileName(String filename, boolean unicode) {
        put(PdfName.UF, new PdfString(filename, unicode ? PdfObject.TEXT_UNICODE : PdfObject.TEXT_PDFDOCENCODING));
    }
    
    /**
     * Sets a flag that indicates whether an external file referenced by the file
     * specification is volatile. If the value is true, applications should never
     * cache a copy of the file.
     * @param volatile_file	if true, the external file should not be cached
     */
    public void setVolatile(boolean volatile_file) {
    	put(PdfName.V, new PdfBoolean(volatile_file));
    }
    
    /**
     * Adds a description for the file that is specified here.
     * @param description	some text
     * @param unicode		if true, the text is added as a unicode string
     */
    public void addDescription(String description, boolean unicode) {
        put(PdfName.DESC, new PdfString(description, unicode ? PdfObject.TEXT_UNICODE : PdfObject.TEXT_PDFDOCENCODING));
    }
    
    /**
     * Adds the Collection item dictionary.
     */
    public void addCollectionItem(PdfCollectionItem ci) {
    	put(PdfName.CI, ci);
    }
}
