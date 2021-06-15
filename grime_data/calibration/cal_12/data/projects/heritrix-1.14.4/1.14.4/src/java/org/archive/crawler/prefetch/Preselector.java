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
package org.archive.crawler.prefetch;

import javax.management.AttributeNotFoundException;

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.datamodel.FetchStatusCodes;
import org.archive.crawler.framework.Scoper;
import org.archive.crawler.settings.SimpleType;
import org.archive.crawler.settings.Type;
import org.archive.util.TextUtils;

/**
 * If set to recheck the crawl's scope, gives a yes/no on whether
 * a CrawlURI should be processed at all. If not, its status
 * will be marked OUT_OF_SCOPE and the URI will skip directly
 * to the first "postprocessor".
 *
 *
 * @author gojomo
 *
 */
public class Preselector extends Scoper
implements FetchStatusCodes {

    private static final long serialVersionUID = 3738560264369561017L;

    /** whether to reapply crawl scope at this step */
    public static final String ATTR_RECHECK_SCOPE = "recheck-scope";
    /** indicator allowing all URIs (of a given host, typically) to
     * be blocked at this step*/
    public static final String ATTR_BLOCK_ALL = "block-all";
    /** indicator allowing all matching URIs to be blocked at this step */
    public static final String ATTR_BLOCK_BY_REGEXP = "block-by-regexp";
    /** indicator allowing all matching URIs */
    public static final String ATTR_ALLOW_BY_REGEXP = "allow-by-regexp";

    /**
     * Constructor.
     * @param name Name of this processor.
     */
    public Preselector(String name) {
        super(name, "Preselector. Does one last bit of checking to make " +
            "sure that the current URI should be fetched.");
        Type e;
        e = addElementToDefinition(new SimpleType(ATTR_RECHECK_SCOPE,
                "Recheck if uri is in scope. This is meaningful if the scope" +
                " is altered during a crawl. URIs are checked against the" +
                " scope when they are added to queues. Setting this value to" +
                " true forces the URI to be checked against the scope when it" +
                " is comming out of the queue, possibly after the scope is" +
                " altered.", new Boolean(false)));
        e.setExpertSetting(true);

        e = addElementToDefinition(new SimpleType(ATTR_BLOCK_ALL,
                "Block all URIs from being processed. This is most likely to" +
                " be used in overrides to easily reject certain hosts from" +
                " being processed.", new Boolean(false)));
        e.setExpertSetting(true);

        e = addElementToDefinition(new SimpleType(ATTR_BLOCK_BY_REGEXP,
                "Block all URIs matching the regular expression from being" +
                " processed.", ""));
        e.setExpertSetting(true);

        e = addElementToDefinition(new SimpleType(ATTR_ALLOW_BY_REGEXP,
                "Allow only URIs matching the regular expression to be" +
                " processed.", ""));
        e.setExpertSetting(true);
    }

    protected void innerProcess(CrawlURI curi) {
        // Check if uris should be blocked
        try {
            if (((Boolean) getAttribute(ATTR_BLOCK_ALL, curi)).booleanValue()) {
                curi.setFetchStatus(S_BLOCKED_BY_USER);
                curi.skipToProcessorChain(getController().
                    getPostprocessorChain());
            }
        } catch (AttributeNotFoundException e) {
            // Act as attribute was false, that is: do nothing.
        }

        // Check if allowed by regular expression
        try {
            String regexp = (String) getAttribute(ATTR_ALLOW_BY_REGEXP, curi);
            if (regexp != null && !regexp.equals("")) {
                if (!TextUtils.matches(regexp, curi.toString())) {
                    curi.setFetchStatus(S_BLOCKED_BY_USER);
                    curi.skipToProcessorChain(getController().
                        getPostprocessorChain());
                }
            }
        } catch (AttributeNotFoundException e) {
            // Act as regexp was null, that is: do nothing.
        }


        // Check if blocked by regular expression
        try {
            String regexp = (String) getAttribute(ATTR_BLOCK_BY_REGEXP, curi);
            if (regexp != null && !regexp.equals("")) {
                if (TextUtils.matches(regexp, curi.toString())) {
                    curi.setFetchStatus(S_BLOCKED_BY_USER);
                    curi.skipToProcessorChain(getController().
                        getPostprocessorChain());
                }
            }
        } catch (AttributeNotFoundException e) {
            // Act as regexp was null, that is: do nothing.
        }

        // Possibly recheck scope
        try {
            if (((Boolean) getAttribute(ATTR_RECHECK_SCOPE, curi)).
                    booleanValue()) {
                if (!isInScope(curi)) {
                    // Scope rejected
                    curi.setFetchStatus(S_OUT_OF_SCOPE);
                    curi.skipToProcessorChain(getController().
                        getPostprocessorChain());
                }
            }
        } catch (AttributeNotFoundException e) {
            // Act as attribute was false, that is: do nothing.
        }
    }
}
