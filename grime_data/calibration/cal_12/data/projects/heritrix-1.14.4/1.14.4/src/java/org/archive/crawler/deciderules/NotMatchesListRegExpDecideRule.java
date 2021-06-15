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


/**
 * Rule applies configured decision to any URIs which do *not*
 * match the supplied regexp.
 *
 * @author Kristinn Sigurdsson
 */
public class NotMatchesListRegExpDecideRule extends MatchesListRegExpDecideRule {

    private static final long serialVersionUID = 8691360087063555583L;

    //private static final Logger logger =
    //    Logger.getLogger(NotMatchesListRegExpDecideRule.class.getName());


    /**
     * Usual constructor. 
     * @param name
     */
    public NotMatchesListRegExpDecideRule(String name) {
        super(name);
        setDescription("NotMatchesListRegExpDecideRule. Applies the configured " +
            "decision to URIs *not* matching the supplied regular " +
            "expressions. The list of regular expressions can be " +
            "considered logically AND or OR. " +
            "NOTE: This means that if there are no regular expressions in " +
            "the list, this rule will apply to *all* URIs!");
    }

    /**
     * Evaluate whether given object's string version does not match 
     * configured regexps (by reversing the superclass's answer).
     * 
     * @param object Object to make decision about.
     * @return true if the regexps are not matched
     */
    protected boolean evaluate(Object object) {
        return ! super.evaluate(object);
    }
}
