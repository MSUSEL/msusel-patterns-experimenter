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
/* ClassKeyMatchesRegExpDecideRule
*
* $Id: ClassKeyMatchesRegExpDecideRule.java 4649 2006-09-25 17:16:55Z paul_jack $
*
* Created on Apr 4, 2005
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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.archive.crawler.datamodel.CandidateURI;
import org.archive.util.TextUtils;



/**
 * Rule applies configured decision to any CrawlURI class key -- i.e.
 * {@link CandidateURI#getClassKey()} -- matches matches supplied regexp.
 *
 * @author gojomo
 */
public class ClassKeyMatchesRegExpDecideRule extends MatchesRegExpDecideRule {

    private static final long serialVersionUID = 1178873944436973294L;

    private static final Logger logger =
        Logger.getLogger(ClassKeyMatchesRegExpDecideRule.class.getName());

    /**
     * Usual constructor. 
     * @param name
     */
    public ClassKeyMatchesRegExpDecideRule(String name) {
        super(name);
        setDescription("ClassKeyMatchesRegExpDecideRule. " +
            "Applies the configured " +
            "decision to class keys matching the supplied " +
            "regular expression. Class keys are values set into " +
            "an URL by the Frontier. They are usually the names " +
            "of queues used by the Frontier. Class keys can " +
            "look like hostname + port or be plain IPs (It will " +
            "depend on the Frontier implementation/configuration).");
    }

    /**
     * Evaluate passed object.
     * Test first that its CandidateURI.  If so, does it have a class key.
     * If not, ask frontier for its classkey.  Then test against regex.
     * 
     * @param object
     * @return true if regexp is matched
     */
    protected boolean evaluate(Object object) {
        try {
            CandidateURI cauri = (CandidateURI)object;
            String classKey = cauri.getClassKey();
            if (classKey == null || classKey.length() <= 0) {
                classKey = getSettingsHandler().getOrder().getController().
                    getFrontier().getClassKey(cauri);
                cauri.setClassKey(classKey);
            }
            String regexp = getRegexp(cauri);
            boolean result = (regexp == null)?
                false: TextUtils.matches(regexp, cauri.getClassKey());
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Tested '" + cauri.getClassKey() +
                    "' match with regex '" + regexp + " and result was " +
                    result);
            }
            return result;
        } catch (ClassCastException e) {
            // if not CrawlURI, always disregard
            return false; 
        }
    }
}