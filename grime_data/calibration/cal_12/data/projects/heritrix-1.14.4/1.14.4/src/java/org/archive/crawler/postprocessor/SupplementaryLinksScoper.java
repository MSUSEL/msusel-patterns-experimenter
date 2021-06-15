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
package org.archive.crawler.postprocessor;

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.AttributeNotFoundException;

import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.deciderules.DecideRule;
import org.archive.crawler.deciderules.DecideRuleSequence;
import org.archive.crawler.framework.Filter;
import org.archive.crawler.framework.Scoper;
import org.archive.crawler.settings.MapType;

/**
 * Run CandidateURI links carried in the passed CrawlURI through a filter
 * and 'handle' rejections.
 * Used to do supplementary processing of links after they've been scope
 * processed and ruled 'in-scope' by LinkScoper.  An example of
 * 'supplementary processing' would check that a Link is intended for
 * this host to crawl in a multimachine crawl setting. Configure filters to
 * rule on links.  Default handler writes rejected URLs to disk.  Subclass
 * to handle rejected URLs otherwise.
 * @author stack
 */
public class SupplementaryLinksScoper extends Scoper {

    private static final long serialVersionUID = -775819977752790418L;

    private static Logger LOGGER =
        Logger.getLogger(SupplementaryLinksScoper.class.getName());
    
    public static final String ATTR_LINKS_DECIDE_RULES = "link-rules";

    /**
     * @param name Name of this filter.
     */
    public SupplementaryLinksScoper(String name) {
        super(name, "SupplementaryLinksScoper. Use to do supplementary " +
            "processing of in-scope links.  Will run each link through " +
            "configured filters.  Must be run after LinkScoper and " +
            "before FrontierScheduler. " +
            "Optionally logs rejected links (Enable " +
            ATTR_OVERRIDE_LOGGER_ENABLED + " and set logger level " +
            "at INFO or above).");
        
        addElementToDefinition(
                new DecideRuleSequence(ATTR_LINKS_DECIDE_RULES,
                    "DecideRules which if their final decision on a link is " +
                    "REJECT, cause the link to be ruled out-of-scope, even " +
                    "if it had previously been accepted by the main scope."));
    }

    protected void innerProcess(final CrawlURI curi) {
        // If prerequisites or no links, nothing to be done in here.
        if (curi.hasPrerequisiteUri() || curi.outlinksSize() <= 0) {
            return;
        }
        
        Collection<CandidateURI> inScopeLinks = new HashSet<CandidateURI>();
        for (CandidateURI cauri: curi.getOutCandidates()) {
            if (isInScope(cauri)) {
                inScopeLinks.add(cauri);
            }
        }
        // Replace current links collection w/ inscopeLinks.  May be
        // an empty collection.
        curi.replaceOutlinks(inScopeLinks);
    }
    
    protected boolean isInScope(CandidateURI caUri) {
        // TODO: Fix filters so work on CandidateURI.
        CrawlURI curi = (caUri instanceof CrawlURI)?
            (CrawlURI)caUri:
            new CrawlURI(caUri.getUURI());
        boolean result = false;
        if (rulesAccept(getLinkRules(curi), curi)) {
            result = true;
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("Accepted: " + caUri);
            }
        } else {
            outOfScope(caUri);
        }
        return result;
    }
    
    protected DecideRule getLinkRules(Object o) {
        try {
            return (DecideRule)getAttribute(o, ATTR_LINKS_DECIDE_RULES);
        } catch (AttributeNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Called when a CandidateUri is ruled out of scope.
     * @param caUri CandidateURI that is out of scope.
     */
    protected void outOfScope(CandidateURI caUri) {
        if (!LOGGER.isLoggable(Level.INFO)) {
            return;
        }
        LOGGER.info(caUri.getUURI().toString());
    }
}