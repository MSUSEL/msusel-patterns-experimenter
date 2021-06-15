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

import java.util.Iterator;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.management.AttributeNotFoundException;

import org.archive.crawler.datamodel.CrawlOrder;
import org.archive.crawler.settings.MapType;
import org.archive.net.UURI;

/**
 * URL canonicalizer.
 * @author stack
 * @version $Date: 2006-09-26 20:38:48 +0000 (Tue, 26 Sep 2006) $, $Revision: 4667 $
 */
public class Canonicalizer {
    private static Logger logger =
        Logger.getLogger(Canonicalizer.class.getName());
    
    /**
     * Constructor.
     * This class can't be constructed.
     * Shutdown.
     */
    private Canonicalizer() {
        super();
    }
    
    /**
     * Convenience method that is passed a settings object instance pulling
     * from it what it needs to canonicalize.
     * @param uuri UURI to canonicalize.
     * @param order A crawlorder instance.
     * @return Canonicalized string of uuri else uuri if an error.
     */
    public static String canonicalize(UURI uuri, CrawlOrder order) {
        MapType rules = null;
        String canonical = uuri.toString();
        try {
            rules = (MapType)order.getAttribute(uuri, CrawlOrder.ATTR_RULES);
            canonical = Canonicalizer.canonicalize(uuri, rules.iterator(uuri));
        } catch (AttributeNotFoundException e) {
            logger.warning("Failed canonicalization of " + canonical +
                ": " + e);
        }
        return canonical;
    }

    /**
     * Run the passed uuri through the list of rules.
     * @param uuri Url to canonicalize.
     * @param rules Iterator of canonicalization rules to apply (Get one
     * of these on the url-canonicalizer-rules element in order files or
     * create a list externally).  Rules must implement the Rule interface.
     * @return Canonicalized URL.
     */
    public static String canonicalize(UURI uuri, Iterator rules) {
        String before = uuri.toString();
        //String beforeRule = null;
        String canonical = before;
        for (; rules.hasNext();) {
            CanonicalizationRule r = (CanonicalizationRule)rules.next();
            //if (logger.isLoggable(Level.FINER)) {
            //    beforeRule = canonical;
            //}
            if (!r.isEnabled(uuri)) {
                if (logger.isLoggable(Level.FINER)) {
                    logger.finer("Rule " + r.getName() + " is disabled.");
                }
                continue;
            }
            canonical = r.canonicalize(canonical, uuri);
            if (logger.isLoggable(Level.FINER)) {
                logger.finer("Rule " + r.getName() + " " + before + " => " +
                        canonical);
            }
        }
        if (logger.isLoggable(Level.INFO)) {
            logger.fine(before + " => " + canonical);
        }
        return canonical;
    }
}
