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
package org.hibernate.testing.cache;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Generates increasing identifiers (in a single VM only). Not valid across multiple VMs.  Identifiers are not
 * necessarily strictly increasing, but usually are.
 *
 * Core while loop implemented by Alex Snaps - EHCache project - under ASL 2.0
 *
 * @author Hibernate team
 * @author Alex Snaps
 */
public final class Timestamper {
	private static final int BIN_DIGITS = 12;
	public static final short ONE_MS = 1 << BIN_DIGITS;
	private static final AtomicLong VALUE = new AtomicLong();

	public static long next() {
		while ( true ) {
			long base = System.currentTimeMillis() << BIN_DIGITS;
			long maxValue = base + ONE_MS - 1;

			for ( long current = VALUE.get(), update = Math.max( base, current + 1 ); update < maxValue;
				  current = VALUE.get(), update = Math.max( base, current + 1 ) ) {
				if ( VALUE.compareAndSet( current, update ) ) {
					return update;
				}
			}
		}
	}

	private Timestamper() {
	}
}






