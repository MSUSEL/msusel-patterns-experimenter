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
package org.archive.crawler.deciderules;

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.util.TextUtils;

/**
 * DecideRule whose decision is applied if the URI's content-type 
 * is present and matches the supplied regular expression. 
 * 
 * @author Olaf Freyer
 */
public class ContentTypeMatchesRegExpDecideRule extends MatchesRegExpDecideRule{
    private static final long serialVersionUID = -2066930281015155843L;

    public ContentTypeMatchesRegExpDecideRule(String name) {
        super(name);
        setDescription("ContentTypeMatchesRegExpDecideRule. Applies the " +
            "configured decision to URIs matching the supplied regular " +
            "expression. Cannot be used until after fetcher processors. " +
            "Only then is the Content-Type known. A good place for this " +
            "rule is at the writer step processing.  If the content-type " +
            "is null, 301s usually have no content-type, this deciderule " +
            "will PASS.");
    }
    
    @Override
    protected boolean evaluate(Object o) {
            if (!(o instanceof CrawlURI)) {
                return false;
            }
            String content_type = ((CrawlURI)o).getContentType();
            String regexp = getRegexp(o);
            return (regexp == null || content_type == null)? false:
                    TextUtils.matches(getRegexp(o), content_type);
        }
}
