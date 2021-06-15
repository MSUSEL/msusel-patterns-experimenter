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
/* DecideRule
*
* $Id: DecideRule.java 5010 2007-03-16 15:50:55Z Gojomo $
*
* Created on Mar 3, 2005
*
* Copyright (C) 2005 Internet Archive.
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

import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.settings.ModuleType;


/**
 * Interface for rules which, given an object to evaluate,
 * respond with a decision: {@link DecideRule#ACCEPT}, 
 * {@link DecideRule#REJECT}, or 
 * {@link DecideRule#PASS}.
 * 
 * Rules return {@link #PASS} by default.
 *
 * @author gojomo
 * @see org.archive.crawler.deciderules.DecideRuleSequence
 */
public class DecideRule extends ModuleType {

    private static final long serialVersionUID = 3437522810581532520L;
    // enumeration of 'actions'
    public static final String ACCEPT = "ACCEPT".intern();
    public static final String REJECT = "REJECT".intern();
    public static final String PASS = "PASS".intern();

    /**
     * Constructor.
     * @param name Name of this rule.
     */
    public DecideRule(String name) {
        super(name);
    }

    /**
     * Make decision on passed <code>object</code>.
     * @param object Object to rule on.
     * @return {@link #ACCEPT}, {@link #REJECT}, or {@link #PASS}.
     */
    public Object decisionFor(Object object) {
        return PASS;
    }

    /**
     * If this rule is "one-way" -- can only return a single
     * possible decision other than PASS -- return that 
     * decision. Otherwise return null. Most rules will be
     * one-way. 
     * @param object 
     * 
     * @return the one decision other than PASS this rule might
     * return, if there is only one
     */
    public Object singlePossibleNonPassDecision(Object object) {
        // by default, don't assume one-way
        return null;
    }
    
    /**
     * Respond to a settings update, refreshing any internal settings-derived
     * state.
     * 
     * This method gives implementors a chance to refresh internal state
     * after a settings change. Normally new settings are picked up w/o
     * the need of work on the part of settings' clients but some facilities
     * -- for example, Surt classes need to sort submissions into
     * common-prefix-coalesced collection of Surt prefixes, or,
     * settings changes that alter external file or seeds/directives
     * references -- need to be flagged so they can take
     * compensatory action. 
     */
    public void kickUpdate() {
        // by default do nothing
    }
    
    /** 
     * Get the controller object.
     *
     * @return the controller object.
     */
    public CrawlController getController() {
        return getSettingsHandler().getOrder().getController();
    }
}
