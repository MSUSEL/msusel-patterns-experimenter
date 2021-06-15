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
package org.hibernate.hql.internal.ast;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import antlr.RecognitionException;
import org.jboss.logging.Logger;

import org.hibernate.QueryException;
import org.hibernate.internal.CoreMessageLogger;

/**
 * An error handler that counts parsing errors and warnings.
 */
public class ErrorCounter implements ParseErrorHandler {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, ErrorCounter.class.getName());

	private List errorList = new ArrayList();
	private List warningList = new ArrayList();
	private List recognitionExceptions = new ArrayList();

	public void reportError(RecognitionException e) {
		reportError( e.toString() );
		recognitionExceptions.add( e );
        LOG.error(e.toString(), e);
	}

	public void reportError(String message) {
        LOG.error(message);
		errorList.add( message );
	}

	public int getErrorCount() {
		return errorList.size();
	}

	public void reportWarning(String message) {
		LOG.debug(message);
		warningList.add( message );
	}

	private String getErrorString() {
		StringBuilder buf = new StringBuilder();
		for ( Iterator iterator = errorList.iterator(); iterator.hasNext(); ) {
			buf.append( ( String ) iterator.next() );
			if ( iterator.hasNext() ) buf.append( "\n" );

		}
		return buf.toString();
	}

	public void throwQueryException() throws QueryException {
		if ( getErrorCount() > 0 ) {
            if (recognitionExceptions.size() > 0) throw QuerySyntaxException.convert((RecognitionException)recognitionExceptions.get(0));
            throw new QueryException(getErrorString());
        }
		LOG.debug("throwQueryException() : no errors");
	}
}
