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
package org.hibernate.testing.byteman;

import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.byteman.rule.Rule;
import org.jboss.byteman.rule.helper.Helper;
import org.jboss.logging.Logger;

/**
 * @author Sanne Grinovero <sanne@hibernate.org> (C) 2011 Red Hat Inc.
 * @author Hardy Ferentschik
 */
public class BytemanHelper extends Helper {
	private static final Logger log = Logger.getLogger( BytemanHelper.class );

	public static final AtomicInteger counter = new AtomicInteger();

	protected BytemanHelper(Rule rule) {
		super( rule );
	}

	public void sleepASecond() {
		try {
			log.info( "Byteman rule triggered: sleeping a second" );
			Thread.sleep( 1000 );
		}
		catch ( InterruptedException e ) {
			Thread.currentThread().interrupt();
			log.error( "unexpected interruption", e );
		}
	}

	public void throwNPE(String message) {
		//Needed because of Bug BYTEMAN-173: can't simply inject a NPE from the rule
		throw new NullPointerException( message );
	}

	public void countInvocation() {
		log.debug( "Increment call count" );
		counter.incrementAndGet();
	}

	public static int getAndResetInvocationCount() {
		return counter.getAndSet( 0 );
	}
}
