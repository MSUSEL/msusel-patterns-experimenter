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

import java.util.logging.Logger;

import org.archive.crawler.datamodel.CoreAttributeConstants;
import org.archive.crawler.datamodel.CrawlURI;


/** 
 * Pseudo-extractor that suppresses link-extraction of likely trap pages,
 * by noticing when content's digest is identical to that of its 'via'. 
 *
 * @author gojomo
 *
 */
public class TrapSuppressExtractor extends Extractor implements CoreAttributeConstants {
    private static final long serialVersionUID = -1028783453022579530L;

    private static final Logger LOGGER =
        Logger.getLogger(TrapSuppressExtractor.class.getName());

    /** ALIst attribute key for carrying-forward content-digest from 'via'*/
    public static String A_VIA_DIGEST = "via-digest";
    
    protected long numberOfCURIsHandled = 0;
    protected long numberOfCURIsSuppressed = 0;

    /**
     * Usual constructor. 
     * @param name
     */
    public TrapSuppressExtractor(String name) {
        super(name, "TrapSuppressExtractor. Prevent extraction of likely " +
                "trap content.");
    }

    @Override
    protected void initialTasks() {
        super.initialTasks();
    }

    protected void extract(CrawlURI curi){
        numberOfCURIsHandled++;

        String currentDigest = curi.getContentDigestSchemeString();
        String viaDigest = null;
        if(curi.containsKey(A_VIA_DIGEST)) {
            viaDigest = curi.getString(A_VIA_DIGEST);
        }
        
        if(currentDigest!=null) {
            if(currentDigest.equals(viaDigest)) {
                // mark as already-extracted -- suppressing further extraction
                curi.linkExtractorFinished();
                curi.addAnnotation("trapSuppressExtractor");
                numberOfCURIsSuppressed++;
            }
            // already consulted; so clobber with current value to be 
            // inherited
            curi.putString(A_VIA_DIGEST, currentDigest);
            curi.makeHeritable(A_VIA_DIGEST);
        }
    }

    /**
     * Provide a human-readable textual summary of this Processor's state.
     *
     * @see org.archive.crawler.framework.Processor#report()
     */
    public String report() {
        StringBuffer ret = new StringBuffer();
        ret.append("Processor: org.archive.crawler.extractor.TrapSuppressExtractor\n");
        ret.append("  Function:             Suppress extraction on likely traps\n");
        ret.append("  CrawlURIs handled:    " + numberOfCURIsHandled + "\n");
        ret.append("  CrawlURIs suppressed: " + numberOfCURIsSuppressed + "\n\n");

        return ret.toString();
    }
}
