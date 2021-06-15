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

import org.archive.crawler.settings.SimpleType;

public class ExceedsDocumentLengthTresholdDecideRule extends
NotExceedsDocumentLengthTresholdDecideRule {

    private static final long serialVersionUID = -3008503096295212224L;

    /**
     * Usual constructor. 
     * @param name Name of this rule.
     */
    public ExceedsDocumentLengthTresholdDecideRule(String name) {
    	super(name);
    	setDescription("ExceedsDocumentLengthTresholdDecideRule. ACCEPTs URIs "+
             "with content length exceeding a given treshold. "+
             "Either examines HTTP header content length or " +
             "actual downloaded content length and returns false " +
             "for documents exceeding a given length treshold.");

        addElementToDefinition(new SimpleType(ATTR_CONTENT_LENGTH_TRESHOLD,
        	"Min " +
    	    "content-length this filter will allow to pass through. If -1, " +
    	    "then no limit.", DEFAULT_CONTENT_LENGTH_TRESHOLD));    }
    
    /**
     * @param contentLength content length to check against treshold
     * @param obj Context object.
     * @return contentLength exceeding treshold?
     */
    protected Boolean makeDecision(int contentLength, Object obj) {
    	return contentLength > getContentLengthTreshold(obj);
    }
    
    /**
     * @param obj Context object.
     * @return content length threshold
     */
    protected int getContentLengthTreshold(Object obj) {
        int len = ((Integer)getUncheckedAttribute(obj,
        		ATTR_CONTENT_LENGTH_TRESHOLD)).intValue();
        return len == -1? 0: len;
    }
}