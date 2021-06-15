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
/* HopsPathMatchesRegExpDecideRule
*
* $Id: HopsPathMatchesRegExpDecideRule.java 4649 2006-09-25 17:16:55Z paul_jack $
*
* Created on June 23, 2005
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

import javax.management.AttributeNotFoundException;

import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.settings.SimpleType;
import org.archive.util.TextUtils;

/**
 * Rule applies configured decision to any CrawlURIs whose 'hops-path'
 * (string like "LLXE" etc.) matches the supplied regexp.
 *
 * @author gojomo
 */
public class HopsPathMatchesRegExpDecideRule extends PredicatedDecideRule {

    private static final long serialVersionUID = -8881013912393934053L;

    private static final Logger logger =
        Logger.getLogger(HopsPathMatchesRegExpDecideRule.class.getName());
    
    public static final String ATTR_REGEXP = "regexp";

    /**
     * Usual constructor. 
     * @param name
     */
    public HopsPathMatchesRegExpDecideRule(String name) {
        super(name);
        setDescription("HopsPathMatchesRegExpDecideRule. Applies the " +
                "configured decision to URIs whose hops-path (string with " +
                "L E R X P etc) matches the supplied regular expression.");
        addElementToDefinition(new SimpleType(ATTR_REGEXP, "Java regular" +
            "expression to match.", ""));
    }

    /**
     * Evaluate whether given object (if CandidateURI) has hops-path
     * matching configured regexp
     * 
     * @param object
     * @return true if regexp is matched
     */
    protected boolean evaluate(Object object) {
        try {
            String regexp = getRegexp(object);
            String str = ((CandidateURI)object).getPathFromSeed();
            boolean result = (regexp == null)?
                    false: TextUtils.matches(regexp, str);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Tested '" + str + "' match with regex '" +
                        regexp + " and result was " + result);
            }
            return result;
        } catch (ClassCastException e) {
            // if not CrawlURI, always disregard
            return false; 
        }
    }
    
    /** 
     * Get the regular expression string to match the URI against.
     *
     * @param o the object for which the regular expression should be
     *          matched against.
     * @return the regular expression to match against.
     */
    protected String getRegexp(Object o) {
        try {
            return (String) getAttribute(o, ATTR_REGEXP);
        } catch (AttributeNotFoundException e) {
            logger.severe(e.getMessage());
            return null;  // Basically the filter is inactive if this occurs.
        }
    }
}
