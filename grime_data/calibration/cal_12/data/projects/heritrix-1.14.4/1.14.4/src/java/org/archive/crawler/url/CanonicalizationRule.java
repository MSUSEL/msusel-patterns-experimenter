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
package org.archive.crawler.url;


/**
 * A rule to apply canonicalizing a url.
 * @author stack
 * @version $Date: 2004-10-08 17:39:42 +0000 (Fri, 08 Oct 2004) $, $Revision: 2627 $
 */
public interface CanonicalizationRule {
    /**
     * Apply this canonicalization rule.
     * 
     * @param url Url string we apply this rule to.
     * @param context An object that will provide context for the settings
     * system.  The UURI of the URL we're canonicalizing is an example of
     * an object that provides context.
     * @return Result of applying this rule to passed <code>url</code>.
     */
    public String canonicalize(String url, Object context);

    /**
     * @return Name of this rule.
     */
    public String getName();
    
    /**
     * @param context An object that will provide context for the settings
     * system.  The UURI of the URL we're canonicalizing is an example of
     * an object that provides context.
     * @return True if this rule is enabled and to be run.
     */
    public boolean isEnabled(Object context);
}
