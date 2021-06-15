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

import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.framework.Frontier;
import org.archive.crawler.frontier.WorkQueue;

/**
 * Applies configured decision to every candidate URI that would 
 * overbudget its queue. (total expended + pending > total budget).
 * This rule has no impact on allready enqueued URIs, thus
 * the right place to use it is the DecidingScope (triggered via LinksScoper)
 * 
 * (Originally named QueueSizeLimiterDecideRule).
 * @author Olaf Freyer
 */
public class QueueOverbudgetDecideRule extends PredicatedDecideRule {

    private static final long serialVersionUID = 5165201864629344642L;

    public QueueOverbudgetDecideRule(String name) {
        super(name);
        setDescription("QueueOverbudgetDecideRule. "
                + "Applies configured decision to every candidate URI that would "
                + "overbudget its queue. (total expended + pending > total budget)."
                + "This rule has no impact on already enqueued URIs, thus "
                + "the right place to use it is the DecidingScope (triggered via LinksScoper) ");
    }

    @Override
    protected boolean evaluate(Object object) {
        if(! (object instanceof CandidateURI)) {
            return false; 
        }
        
        CandidateURI caUri = (CandidateURI) object;
        Frontier frontier = getController().getFrontier();

        CrawlURI curi;
        if (caUri instanceof CrawlURI) {
            // this URI already has been enqueued - don't change previous
            // decision
            return false;
        } else {
            curi = new CrawlURI(caUri.getUURI());
            curi.setClassKey(frontier.getClassKey(curi));
        }
        WorkQueue wq = (WorkQueue) frontier.getGroup(curi);
        return (wq.getPendingExpenditure() + wq.getTotalExpenditure()) 
                    > wq.getTotalBudget();
    }
}
