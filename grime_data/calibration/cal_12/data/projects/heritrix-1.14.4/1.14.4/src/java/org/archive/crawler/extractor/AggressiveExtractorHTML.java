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

import org.archive.crawler.datamodel.CrawlURI;

/**
 * Extended version of ExtractorHTML with more aggressive javascript link
 * extraction where javascript code is parsed first with general HTML tags
 * regexp, and than by javascript speculative link regexp.
 *
 * @author Igor Ranitovic
 *
 */
public class AggressiveExtractorHTML
extends ExtractorHTML {

    private static final long serialVersionUID = 3586060081186247087L;

    static Logger logger =
        Logger.getLogger(AggressiveExtractorHTML.class.getName());
    
    public AggressiveExtractorHTML(String name) {
        super(name, "Aggressive HTML extractor. Subclasses ExtractorHTML " +
                " so does all that it does, except in regard to javascript " +
                " blocks.  Here " +
                " it first processes as JS as its parent does, but then it " +
                " reruns through the JS treating it as HTML (May cause many " +
                " false positives). It finishes by applying heuristics " +
                " against script code looking for possible URIs. ");
    }

    protected void processScript(CrawlURI curi, CharSequence sequence,
            int endOfOpenTag) {
        super.processScript(curi,sequence,endOfOpenTag);
        // then, process entire javascript code as html code
        // this may cause a lot of false positves
        processGeneralTag(curi, sequence.subSequence(0,6),
            sequence.subSequence(endOfOpenTag, sequence.length()));
    }

    /* (non-Javadoc)
     * @see org.archive.crawler.framework.Processor#report()
     */
    public String report() {
        StringBuffer ret = new StringBuffer(256);
        ret.append("Processor: org.archive.crawler.extractor.AggressiveExtractorHTML\n");
        ret.append("  Function:          Link extraction on HTML documents " +
            "(including embedded CSS)\n");
        ret.append("  CrawlURIs handled: " + numberOfCURIsHandled + "\n");
        ret.append("  Links extracted:   " + numberOfLinksExtracted + "\n\n");
        return ret.toString();
    }
}
