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
package org.hibernate.testing.async;

/**
 * @author Steve Ebersole
 */
public class ExecutableAdapter implements Runnable {
	private final Executable executable;
	private boolean isDone;
	private Throwable error;

	public ExecutableAdapter(Executable executable) {
		this.executable = executable;
	}

	public boolean isDone() {
		return isDone;
	}

	public void reThrowAnyErrors() {
		if ( error != null ) {
			if ( RuntimeException.class.isInstance( error ) ) {
				throw RuntimeException.class.cast( error );
			}
			else if ( Error.class.isInstance( error ) ) {
				throw Error.class.cast(  error );
			}
			else {
				throw new ExceptionWrapper( error );
			}
		}
	}

	@Override
	public void run() {
		isDone = false;
		error = null;
		try {
			executable.execute();
		}
		catch (Throwable t) {
			error = t;
		}
		finally {
			isDone = true;
		}
	}

	public static class ExceptionWrapper extends RuntimeException {
		public ExceptionWrapper(Throwable cause) {
			super( cause );
		}
	}
}
