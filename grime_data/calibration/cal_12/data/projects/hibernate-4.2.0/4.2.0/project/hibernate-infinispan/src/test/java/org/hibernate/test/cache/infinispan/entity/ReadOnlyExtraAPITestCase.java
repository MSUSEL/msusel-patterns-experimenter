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
package org.hibernate.test.cache.infinispan.entity;
import org.hibernate.cache.spi.access.AccessType;

import static org.junit.Assert.fail;

/**
 * Tests for the "extra API" in EntityRegionAccessStrategy;
 * <p/>
 * By "extra API" we mean those methods that are superfluous to the
 * function of the Infinispan integration, where the impl is a no-op or a static
 * false return value, UnsupportedOperationException, etc.
 *
 * @author Galder Zamarreño
 * @since 3.5
 */
public class ReadOnlyExtraAPITestCase extends TransactionalExtraAPITestCase {
	@Override
	protected AccessType getAccessType() {
		return AccessType.READ_ONLY;
	}

	@Override
	public void testAfterUpdate() {
		try {
			getEntityAccessStrategy().afterUpdate(
               KEY, VALUE2, 1, 2, new MockSoftLock());
			fail( "Call to afterUpdate did not throw exception" );
		}
		catch (UnsupportedOperationException expected) {
		}
	}

}
