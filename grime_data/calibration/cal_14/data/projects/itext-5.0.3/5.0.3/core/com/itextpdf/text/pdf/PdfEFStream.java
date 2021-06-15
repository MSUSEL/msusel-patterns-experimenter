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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * Extends PdfStream and should be used to create Streams for Embedded Files
 * (file attachments).
 * @since	2.1.3
 */

public class PdfEFStream extends PdfStream {

	/**
	 * Creates a Stream object using an InputStream and a PdfWriter object
	 * @param	in	the InputStream that will be read to get the Stream object
	 * @param	writer	the writer to which the stream will be added
	 */
	public PdfEFStream(InputStream in, PdfWriter writer) {
		super(in, writer);
	}

	/**
	 * Creates a Stream object using a byte array
	 * @param	fileStore	the bytes for the stream
	 */
	public PdfEFStream(byte[] fileStore) {
		super(fileStore);
	}

    /**
     * @see com.itextpdf.text.pdf.PdfDictionary#toPdf(com.itextpdf.text.pdf.PdfWriter, java.io.OutputStream)
     */
    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        if (inputStream != null && compressed)
            put(PdfName.FILTER, PdfName.FLATEDECODE);
        PdfEncryption crypto = null;
        if (writer != null)
            crypto = writer.getEncryption();
        if (crypto != null) {
            PdfObject filter = get(PdfName.FILTER);
            if (filter != null) {
                if (PdfName.CRYPT.equals(filter))
                    crypto = null;
                else if (filter.isArray()) {
                    PdfArray a = (PdfArray)filter;
                    if (!a.isEmpty() && PdfName.CRYPT.equals(a.getPdfObject(0)))
                        crypto = null;
                }
            }
        }
    	if (crypto != null && crypto.isEmbeddedFilesOnly()) {
    		PdfArray filter = new PdfArray();
    		PdfArray decodeparms = new PdfArray();
    		PdfDictionary crypt = new PdfDictionary();
    		crypt.put(PdfName.NAME, PdfName.STDCF);
    		filter.add(PdfName.CRYPT);
    		decodeparms.add(crypt);
    		if (compressed) {
    			filter.add(PdfName.FLATEDECODE);
    			decodeparms.add(new PdfNull());
    		}
    		put(PdfName.FILTER, filter);
    		put(PdfName.DECODEPARMS, decodeparms);
    	}
        PdfObject nn = get(PdfName.LENGTH);
        if (crypto != null && nn != null && nn.isNumber()) {
            int sz = ((PdfNumber)nn).intValue();
            put(PdfName.LENGTH, new PdfNumber(crypto.calculateStreamSize(sz)));
            superToPdf(writer, os);
            put(PdfName.LENGTH, nn);
        }
        else
            superToPdf(writer, os);

        os.write(STARTSTREAM);
        if (inputStream != null) {
            rawLength = 0;
            DeflaterOutputStream def = null;
            OutputStreamCounter osc = new OutputStreamCounter(os);
            OutputStreamEncryption ose = null;
            OutputStream fout = osc;
            if (crypto != null)
                fout = ose = crypto.getEncryptionStream(fout);
            Deflater deflater = null;
            if (compressed) {
                deflater = new Deflater(compressionLevel);
                fout = def = new DeflaterOutputStream(fout, deflater, 0x8000);
            }
            
            byte buf[] = new byte[4192];
            while (true) {
                int n = inputStream.read(buf);
                if (n <= 0)
                    break;
                fout.write(buf, 0, n);
                rawLength += n;
            }
            if (def != null) {
                def.finish();
                deflater.end();
            }
            if (ose != null)
                ose.finish();
            inputStreamLength = osc.getCounter();
        }
        else {
            if (crypto == null) {
                if (streamBytes != null)
                    streamBytes.writeTo(os);
                else
                    os.write(bytes);
            }
            else {
                byte b[];
                if (streamBytes != null) {
                    b = crypto.encryptByteArray(streamBytes.toByteArray());
                }
                else {
                    b = crypto.encryptByteArray(bytes);
                }
                os.write(b);
            }
        }
        os.write(ENDSTREAM);
    }
}
