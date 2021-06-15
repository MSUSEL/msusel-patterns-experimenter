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

/* $Id:  $
 *
 * Copyright (C) 2007 Olaf Freyer
 *
 * This file is part of the Heritrix web crawler (crawler.archive.org).
 *
 * Heritrix is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * any later version.
 *
 * Heritrix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with Heritrix; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.archive.crawler.deciderules;

import org.archive.crawler.datamodel.CrawlURI;

/**
 * DecideRule whose decision is applied if the URI's content-type 
 * is present and does not match the supplied regular expression. 
 * 
 * @author Olaf Freyer
 */
public class ContentTypeNotMatchesRegExpDecideRule extends
        ContentTypeMatchesRegExpDecideRule {
    private static final long serialVersionUID = 4729800377757426137L;

    public ContentTypeNotMatchesRegExpDecideRule(String name) {
        super(name);
        setDescription("ContentTypeNotMatchesRegExpDecideRule. Applies the " +
            "configured decision to URIs not matching the supplied regular " +
            "expression. Cannot be used until after fetcher processors. " +
            "Only then is the Content-Type known. A good place for this " +
            "rule is at the writer step processing.  If the content-type " +
            "is null, 301s usually have no content-type, this deciderule " +
            "will PASS.");
    }
    
    /**
     * Evaluate whether given object's string version does not match 
     * configured regexp (by reversing the superclass's answer).
     * 
     * @param object Object to make decision about.
     * @return true if the regexp is not matched
     */
    protected boolean evaluate(Object o) {
        if (!(o instanceof CrawlURI)) {
            return false;
        }
        String content_type = ((CrawlURI)o).getContentType();
        String regexp = getRegexp(o);
        return (regexp == null || content_type == null)? false:
             ! super.evaluate(o);
    }
    
}
