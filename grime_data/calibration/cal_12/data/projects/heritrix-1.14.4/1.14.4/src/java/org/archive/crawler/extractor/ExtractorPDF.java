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
package org.archive.crawler.extractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.commons.httpclient.URIException;
import org.archive.crawler.datamodel.CoreAttributeConstants;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.framework.ToeThread;


/** Allows the caller to process a CrawlURI representing a PDF
 *  for the purpose of extracting URIs
 *
 * @author Parker Thompson
 *
 */
public class ExtractorPDF extends Extractor implements CoreAttributeConstants {

    private static final long serialVersionUID = -6040669467531928494L;

    private static final Logger LOGGER =
        Logger.getLogger(ExtractorPDF.class.getName());
    private static int DEFAULT_MAX_SIZE_TO_PARSE = 5*1024*1024; // 5MB

    // TODO: make configurable
    private long maxSizeToParse = DEFAULT_MAX_SIZE_TO_PARSE;

    protected long numberOfCURIsHandled = 0;
    protected long numberOfLinksExtracted = 0;

    /**
     * @param name
     */
    public ExtractorPDF(String name) {
        super(name, "PDF extractor. Link extraction on PDF documents.");
    }

    protected void extract(CrawlURI curi){
        if (!isHttpTransactionContentToProcess(curi) ||
                !isExpectedMimeType(curi.getContentType(),
                    "application/pdf")) {
            return;
        }

        numberOfCURIsHandled++;

        File tempFile;

        if(curi.getHttpRecorder().getRecordedInput().getSize()>maxSizeToParse)
        {
            return;
        }

        int sn = ((ToeThread)Thread.currentThread()).getSerialNumber();
        tempFile = new File(getController().getScratchDisk(),"tt"+sn+"tmp.pdf");

        PDFParser parser;
        ArrayList uris;
        try {
            curi.getHttpRecorder().getRecordedInput().
                copyContentBodyTo(tempFile);
            parser = new PDFParser(tempFile.getAbsolutePath());
            uris = parser.extractURIs();
        } catch (IOException e) {
            curi.addLocalizedError(getName(), e, "ExtractorPDF IOException");
            return;
        } catch (RuntimeException e) {
            // Truncated/corrupt  PDFs may generate ClassCast exceptions, or
            // other problems
            curi.addLocalizedError(getName(), e,
                "ExtractorPDF RuntimeException");
            return;
        } finally {
            tempFile.delete();
        }

        if(uris!=null && uris.size()>0) {
            Iterator iter = uris.iterator();
            while(iter.hasNext()) {
                String uri = (String)iter.next();
                try {
                    curi.createAndAddLink(uri,Link.NAVLINK_MISC,Link.NAVLINK_HOP);
                } catch (URIException e1) {
                    // There may not be a controller (e.g. If we're being run
                    // by the extractor tool).
                    if (getController() != null) {
                        getController().logUriError(e1, curi.getUURI(), uri);
                    } else {
                        LOGGER.info(curi + ", " + uri + ": " +
                            e1.getMessage());
                    }
                }
            }
            numberOfLinksExtracted += uris.size();
        }

        LOGGER.fine(curi+" has "+uris.size()+" links.");
        // Set flag to indicate that link extraction is completed.
        curi.linkExtractorFinished();
    }

    /**
     * Provide a human-readable textual summary of this Processor's state.
     *
     * @see org.archive.crawler.framework.Processor#report()
     */
    public String report() {
        StringBuffer ret = new StringBuffer();
        ret.append("Processor: org.archive.crawler.extractor.ExtractorPDF\n");
        ret.append("  Function:          Link extraction on PDF documents\n");
        ret.append("  CrawlURIs handled: " + numberOfCURIsHandled + "\n");
        ret.append("  Links extracted:   " + numberOfLinksExtracted + "\n\n");

        return ret.toString();
    }
}
