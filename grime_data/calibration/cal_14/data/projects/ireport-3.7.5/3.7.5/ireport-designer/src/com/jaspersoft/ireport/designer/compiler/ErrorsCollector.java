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
package com.jaspersoft.ireport.designer.compiler;

import com.jaspersoft.ireport.designer.compiler.xml.SourceLocation;
import com.jaspersoft.ireport.designer.errorhandler.ProblemItem;
import net.sf.jasperreports.engine.JRExpression;
import org.eclipse.jdt.core.compiler.IProblem;

/**
 *
 * @author gtoffoli
 */
public class ErrorsCollector implements JasperReportErrorHandler {
    
    private java.util.List problemItems = null;
    /** Creates a new instance of ErrorsCollector */
    public ErrorsCollector() {
        setProblemItems(new java.util.ArrayList());
    }

    public void addMarker(Throwable e) {
        e.printStackTrace();
        addMarker( e.getMessage(), null);
    }

    @SuppressWarnings("unchecked")
    public void addMarker(String message, SourceLocation location) {
        if (location == null)
        {
            getProblemItems().add( new ProblemItem(ProblemItem.ERROR, message, location, null) );
        }
        else
        {
            getProblemItems().add( new ProblemItem(ProblemItem.ERROR, message, location, location.getXPath()) );
        }
    }

    public void addMarker(IProblem problem, SourceLocation location) {
        
        addMarker( problem.getMessage(), location);
    }
    
    public void addMarker(JRExpression expression, IProblem problem, SourceLocation location) {
        
        getProblemItems().add( new ProblemItem(ProblemItem.ERROR, problem.getMessage(), expression, location.getXPath()) );
    }

    public java.util.List getProblemItems() {
        return problemItems;
    }

    public void setProblemItems(java.util.List problemItems) {
        this.problemItems = problemItems;
    }

    public void addMarker(IProblem problem, JRExpression expression, SourceLocation location) {

        getProblemItems().add( new ProblemItem(ProblemItem.ERROR, problem.getMessage(), expression, (location==null) ? null : location.getXPath()) );
    }
    
}
