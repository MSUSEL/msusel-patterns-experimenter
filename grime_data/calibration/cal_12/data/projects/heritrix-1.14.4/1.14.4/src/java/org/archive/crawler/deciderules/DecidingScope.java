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


import javax.management.AttributeNotFoundException;

import org.archive.crawler.framework.CrawlScope;

/**
 * DecidingScope: a Scope which makes its accept/reject decision based on 
 * whatever DecideRules have been set up inside it.
 * @author gojomo
 */
public class DecidingScope extends CrawlScope {

    private static final long serialVersionUID = -2942787757512964906L;

    //private static Logger logger =
    //    Logger.getLogger(DecidingScope.class.getName());
    public static final String ATTR_DECIDE_RULES = "decide-rules";

    public DecidingScope(String name) {
        super(name);
        setDescription(
            "DecidingScope. A Scope that applies one or " +
            "more DecideRules to determine whether a URI is accepted " +
            "or rejected (returns false).");
        addElementToDefinition(new DecideRuleSequence(
            ATTR_DECIDE_RULES));
    }
    
    protected DecideRule getDecideRule(Object o) {
        try {
            return (DecideRule)getAttribute(o, ATTR_DECIDE_RULES);
        } catch (AttributeNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean innerAccepts(Object o) {
        // would like to use identity test '==' here, but at some
        // step string is being copied, and 'legal-type' mechanism
        // doesn't enforce/maintain identity
        return getDecideRule(o).decisionFor(o).equals(DecideRule.ACCEPT);
    }
    
    /**
     * Note that configuration updates may be necessary. Pass to
     * constituent rules.
     */
    public void kickUpdate() {
        super.kickUpdate();
        // TODO: figure out if there's any way to reconcile this with
        // overrides/refinement rules
        getDecideRule(null).kickUpdate();
    }
}
