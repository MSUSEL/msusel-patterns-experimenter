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

import java.util.concurrent.TimeoutException;

import org.jboss.logging.Logger;

/**
 * @author Steve Ebersole
 */
public class TimedExecutor {
	private static final Logger log = Logger.getLogger( TimedExecutor.class );

	private final long timeOut;
	private final int checkMilliSeconds;

	public TimedExecutor(long timeOut) {
		this( timeOut, 1000 );
	}

	public TimedExecutor(long timeOut, int checkMilliSeconds) {
		this.timeOut = timeOut;
		this.checkMilliSeconds = checkMilliSeconds;
	}

	public void execute(Executable executable) throws TimeoutException {
		final ExecutableAdapter adapter = new ExecutableAdapter( executable );
		final Thread separateThread = new Thread( adapter );
		separateThread.start();

		int runningTime = 0;
		do {
			if ( runningTime > timeOut ) {
				try {
					executable.timedOut();
				}
				catch (Exception ignore) {
				}
				throw new TimeoutException();
			}
			try {
				Thread.sleep( checkMilliSeconds );
				runningTime += checkMilliSeconds;
			}
			catch (InterruptedException ignore) {
			}
		} while ( !adapter.isDone() );

		adapter.reThrowAnyErrors();
	}
}
