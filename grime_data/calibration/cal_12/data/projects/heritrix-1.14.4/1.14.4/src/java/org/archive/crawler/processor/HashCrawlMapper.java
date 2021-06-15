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
package org.archive.crawler.processor;

import java.util.regex.Matcher;

import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.settings.SimpleType;
import org.archive.net.PublicSuffixes;
import org.archive.util.TextUtils;

import st.ata.util.FPGenerator;

/**
 * Maps URIs to one of N crawler names by applying a hash to the
 * URI's (possibly-transformed) classKey. 
 * 
 * @author gojomo
 * @version $Date: 2007-06-19 02:00:24 +0000 (Tue, 19 Jun 2007) $, $Revision: 5215 $
 */
public class HashCrawlMapper extends CrawlMapper {
    private static final long serialVersionUID = 1L;
    
    /** count of crawlers */
    public static final String ATTR_CRAWLER_COUNT = "crawler-count";
    public static final Long DEFAULT_CRAWLER_COUNT = new Long(1);

    /** ruse publicsuffixes pattern for reducing classKey? */
    public static final String ATTR_USE_PUBLICSUFFIX_REDUCE = 
        "use_publicsuffix_reduction";
    public static final Boolean DEFAULT_USE_PUBLICSUFFIX_REDUCE = true;
    
    /** regex pattern for reducing classKey */
    public static final String ATTR_REDUCE_PATTERN = "reduce-prefix-pattern";
    public static final String DEFAULT_REDUCE_PATTERN = "";
 
    long bucketCount = 1;
    String reducePattern = null;
 
    /**
     * Constructor.
     * @param name Name of this processor.
     */
    public HashCrawlMapper(String name) {
        super(name, "HashCrawlMapper. Maps URIs to a numerically named " +
                "crawler by hashing the URI's (possibly transfored) " +
                "classKey to one of the specified number of buckets.");
        addElementToDefinition(new SimpleType(ATTR_CRAWLER_COUNT,
            "Number of crawlers among which to split up the URIs. " +
            "Their names are assumed to be 0..N-1.",
            DEFAULT_CRAWLER_COUNT));
        addElementToDefinition(new SimpleType(ATTR_USE_PUBLICSUFFIX_REDUCE,
                "Whether to use a built-in regular expression, built from " +
                "the 'public suffix' list at publicsuffix.org, for " +
                "reducing classKeys to mapping keys. If true, the default, " +
                "then the '"+ATTR_REDUCE_PATTERN+"' setting is ignored.",
                DEFAULT_USE_PUBLICSUFFIX_REDUCE));
        addElementToDefinition(new SimpleType(ATTR_REDUCE_PATTERN,
                "A regex pattern to apply to the classKey, using " +
                "the first match as the mapping key. Ignored if '"+
                ATTR_USE_PUBLICSUFFIX_REDUCE+"' is set true. If empty " +
                "(the default), use the full classKey.",
                DEFAULT_REDUCE_PATTERN));
    }

    /**
     * Look up the crawler node name to which the given CandidateURI 
     * should be mapped. 
     * 
     * @param cauri CandidateURI to consider
     * @return String node name which should handle URI
     */
    protected String map(CandidateURI cauri) {
        // get classKey, via frontier to generate if necessary
        String key = getController().getFrontier().getClassKey(cauri);
        return mapString(key, reducePattern, bucketCount); 
    }

    protected void initialTasks() {
        super.initialTasks();
        bucketCount = (Long) getUncheckedAttribute(null,ATTR_CRAWLER_COUNT);
        kickUpdate();
    }

    @Override
    public void kickUpdate() {
        super.kickUpdate();
        if ((Boolean) getUncheckedAttribute(null, ATTR_USE_PUBLICSUFFIX_REDUCE)) {
            reducePattern = PublicSuffixes.getTopmostAssignedSurtPrefixRegex();
        } else {
            reducePattern = (String) getUncheckedAttribute(null,
                    ATTR_REDUCE_PATTERN);
        }
    }
    
    public static String mapString(String key, String reducePattern, long bucketCount) {
        if(reducePattern!=null && reducePattern.length()>0) {
           Matcher matcher = TextUtils.getMatcher(reducePattern,key);
           if(matcher.find()) {
               key = matcher.group();
           }
           TextUtils.recycleMatcher(matcher);
        }
        long fp = FPGenerator.std64.fp(key);
        long bucket = fp % bucketCount;
        return Long.toString(bucket >= 0 ? bucket : -bucket);
    }
}