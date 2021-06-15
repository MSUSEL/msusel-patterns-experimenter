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
import com.jaspersoft.ireport.designer.compiler.xml.SourceTraceDigester;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRCompilationSourceCode;
import net.sf.jasperreports.engine.design.JRCompilationUnit;
import net.sf.jasperreports.engine.design.JRJdtCompiler;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;

/**
 *
 * @author gtoffoli
 */
public class ExtendedJRJdtCompiler extends JRJdtCompiler {
    
    private JasperReportErrorHandler errorHandler = null;
    private SourceTraceDigester digester = null;
    private ICompilerRequestor superCompilerRequestor = null;
    
    @Override
    protected ICompilerRequestor getCompilerRequestor(JRCompilationUnit[] units, StringBuffer problemBuffer)
    {
            return new CompilerRequestor(super.getCompilerRequestor(units, problemBuffer), units);
    }
    
    
    protected class CompilerRequestor implements ICompilerRequestor
	{
                private ICompilerRequestor superCompilerRequestor = null;
		private final Map expressionErrors = new HashMap();
		private final JRCompilationUnit[] units;

		protected CompilerRequestor(ICompilerRequestor superCompilerRequestor, JRCompilationUnit[] units)
		{
                        this.superCompilerRequestor = superCompilerRequestor;
			this.units = units;
		}

		public void acceptResult(CompilationResult result)
		{
                        if (superCompilerRequestor != null) superCompilerRequestor.acceptResult(result);
			if (result.hasErrors())
			{
				String className = String.valueOf(result.getCompilationUnit().getMainTypeName());
				
				JRCompilationUnit unit = null;
				for (int classIdx = 0; classIdx < units.length; ++classIdx)
				{
					if (className.equals(units[classIdx].getName()))
					{
						unit = units[classIdx];
						break;
					}
				}
				
				IProblem[] errors = result.getErrors();
				for (int i = 0; i < errors.length; i++)
				{
					IProblem problem = errors[i];
					int line = problem.getSourceLineNumber();
					JRCompilationSourceCode sourceCode = unit.getCompilationSource();
					JRExpression expression = sourceCode.getExpressionAtLine(line);
					if (expression == null)
					{
						getErrorHandler().addMarker( problem, null);
					}
					else if (addExpressionError(expression, problem))
					{
						SourceLocation location = getDigester().getLocation(expression);
						getErrorHandler().addMarker(problem, expression, location);
					}
				}
			}
		}
		
        @SuppressWarnings("unchecked")
		protected boolean addExpressionError(JRExpression expression, IProblem problem)
		{
			Set errors = (Set) expressionErrors.get(expression);
			if (errors == null)
			{
				errors = new HashSet();
				expressionErrors.put(expression, errors);
			}
			return errors.add(new ExpressionErrorKey(problem));
		}
	}
	
	protected static class ExpressionErrorKey
	{
		private final IProblem problem;
		private final int hash;
		
		public ExpressionErrorKey(IProblem problem)
		{
			this.problem = problem;
			this.hash = computeHash();
		}

		private int computeHash()
		{
			int h = problem.getMessage().hashCode();
			return h;
		}
		
		public int hashCode()
		{
			return hash;
		}
		
		public boolean equals(Object o)
		{
			if (o == null || !(o instanceof ExpressionErrorKey) || this.hash != o.hashCode())
			{
				return false;
			}
			
			if (this == o)
			{
				return true;
			}
			
			ExpressionErrorKey k = (ExpressionErrorKey) o;
			return problem.getMessage().equals(k.problem.getMessage());
		}
	}

    public JasperReportErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(JasperReportErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public SourceTraceDigester getDigester() {
        return digester;
    }

    public void setDigester(SourceTraceDigester digester) {
        this.digester = digester;
    }
}
