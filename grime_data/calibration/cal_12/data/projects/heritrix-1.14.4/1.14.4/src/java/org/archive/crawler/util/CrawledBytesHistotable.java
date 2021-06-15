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
package org.archive.crawler.util;

import org.apache.commons.httpclient.HttpStatus;
import org.archive.crawler.datamodel.CoreAttributeConstants;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.deciderules.recrawl.IdenticalDigestDecideRule;
import org.archive.util.Accumulator;
import org.archive.util.ArchiveUtils;
import org.archive.util.Histotable;

public class CrawledBytesHistotable extends Histotable<String> 
implements Accumulator<CrawlURI>, CoreAttributeConstants {
    private static final long serialVersionUID = 7923431123239026213L;
    
    public static final String NOTMODIFIED = "not-modified";
    public static final String DUPLICATE = "dup-by-hash";
    public static final String NOVEL = "novel";

    
    public CrawledBytesHistotable() {
        super();
        tally(NOVEL,0);
    }

    public void accumulate(CrawlURI curi) {
        if(curi.getFetchStatus()==HttpStatus.SC_NOT_MODIFIED) {
            tally(NOTMODIFIED, curi.getContentSize());
        } else if (IdenticalDigestDecideRule.hasIdenticalDigest(curi)) {
            tally(DUPLICATE,curi.getContentSize());
        } else {
            tally(NOVEL,curi.getContentSize());
        }
    }
    
    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append(ArchiveUtils.formatBytesForDisplay(getTotal()));
        sb.append(" crawled (");
        sb.append(ArchiveUtils.formatBytesForDisplay(get(NOVEL)));
        sb.append(" novel");
        if(get(DUPLICATE)!=null) {
            sb.append(", ");
            sb.append(ArchiveUtils.formatBytesForDisplay(get(DUPLICATE)));
            sb.append(" ");
            sb.append(DUPLICATE);
        }
        if(get(NOTMODIFIED)!=null) {
            sb.append(", ");
            sb.append(ArchiveUtils.formatBytesForDisplay(get(NOTMODIFIED)));
            sb.append(" ");
            sb.append(NOTMODIFIED);
        }
        sb.append(")");
        return sb.toString();
    }
}
