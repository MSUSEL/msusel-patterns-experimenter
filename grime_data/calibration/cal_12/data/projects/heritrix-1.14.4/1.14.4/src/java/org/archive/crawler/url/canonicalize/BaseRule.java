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
package org.archive.crawler.url.canonicalize;

import java.util.logging.Logger;
import java.util.regex.Matcher;

import javax.management.AttributeNotFoundException;

import org.archive.crawler.settings.ModuleType;
import org.archive.crawler.settings.SimpleType;
import org.archive.crawler.url.CanonicalizationRule;

/**
 * Base of all rules applied canonicalizing a URL that are configurable
 * via the Heritrix settings system.
 * 
 * This base class is abstact.  Subclasses must implement the
 * {@link CanonicalizationRule#canonicalize(String, Object)} method.
 * 
 * @author stack
 * @version $Date: 2005-11-04 23:00:23 +0000 (Fri, 04 Nov 2005) $, $Revision: 3932 $
 */
public abstract class BaseRule
extends ModuleType
implements CanonicalizationRule {
    private static Logger logger =
        Logger.getLogger(BaseRule.class.getName());
    public static final String ATTR_ENABLED = "enabled";
    
    /**
     * Constructor.
     * @param name Name of this canonicalization rule.
     * @param description Description of what this rule does.
     */
    public BaseRule(String name, String description) {
        super(name, description);
        setExpertSetting(true);
        setOverrideable(true);
        Object [] possibleValues = {Boolean.TRUE, Boolean.FALSE};
        addElementToDefinition(new SimpleType(ATTR_ENABLED,
            "Rule is enabled.", new Boolean(true), possibleValues));
    }
    
    public boolean isEnabled(Object context) {
        boolean result = true;
        try {
            Boolean b = (Boolean)getAttribute(context, ATTR_ENABLED);
            if (b != null) {
                result = b.booleanValue();
            }
        } catch (AttributeNotFoundException e) {
            logger.warning("Failed get of 'enabled' attribute.");
        }

        return result;
    }
    
    /**
     * Run a regex that strips elements of a string.
     * 
     * Assumes the regex has a form that wants to strip elements of the passed
     * string.  Assumes that if a match, appending group 1
     * and group 2 yields desired result.
     * @param url Url to search in.
     * @param matcher Matcher whose form yields a group 1 and group 2 if a
     * match (non-null.
     * @return Original <code>url</code> else concatenization of group 1
     * and group 2.
     */
    protected String doStripRegexMatch(String url, Matcher matcher) {
        return (matcher != null && matcher.matches())?
            checkForNull(matcher.group(1)) + checkForNull(matcher.group(2)):
            url;
    }

    /**
     * @param string String to check.
     * @return <code>string</code> if non-null, else empty string ("").
     */
    private String checkForNull(String string) {
        return (string != null)? string: "";
    }
}
