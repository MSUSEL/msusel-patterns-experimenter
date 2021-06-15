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
package org.archive.crawler.filter;

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.util.TextUtils;

/**
 * Compares the content-type of the passed CrawlURI to a regular expression.
 *
 * @author Tom Emerson
 * @version $Date: 2006-09-25 18:41:10 +0000 (Mon, 25 Sep 2006) $, $Revision: 4652 $
 * @deprecated As of release 1.10.0.  To be replaced by an equivalent
 * {@link DecideRule}.
 */
public class ContentTypeRegExpFilter extends URIRegExpFilter {

    private static final long serialVersionUID = 206378978342655106L;

    private static final String DESCRIPTION = "ContentType regexp filter" +
    		"*Deprecated* To be replaced by an equivalent DecideRule. " +
        "Cannot be used until after fetcher processors. Only then is the" +
        " Content-Type known. A good place for this filter is at" +
        " the writer step processing.  If the content-type is null," +
        " 301s usually have no content-type, the filter returns true.";

    /**
     * @param name Filter name.
     */
    public ContentTypeRegExpFilter(String name) {
        super
        (name, DESCRIPTION, "");
    }

    public ContentTypeRegExpFilter(String name, String regexp) {
        super(name, DESCRIPTION, regexp);
    }
    
    protected boolean innerAccepts(Object o) {
        // FIXME: can o ever be anything but a CrawlURI?
        if (!(o instanceof CrawlURI)) {
            return false;
        }
        String content_type = ((CrawlURI)o).getContentType();
        String regexp = getRegexp(o);
        return (regexp == null)? false:
            (content_type == null)? true:
                TextUtils.matches(getRegexp(o), content_type);
    }
}
