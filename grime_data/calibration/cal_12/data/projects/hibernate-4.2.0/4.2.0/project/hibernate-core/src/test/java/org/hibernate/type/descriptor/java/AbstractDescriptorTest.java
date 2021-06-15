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
package org.hibernate.type.descriptor.java;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;

import org.junit.Before;
import org.junit.Test;

import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Steve Ebersole
 */
public abstract class AbstractDescriptorTest<T> extends BaseUnitTestCase {
	protected class Data<T> {
		private final T originalValue;
		private final T copyOfOriginalValue;
		private final T differentValue;

		public Data(T originalValue, T copyOfOriginalValue, T differentValue) {
			this.originalValue = originalValue;
			this.copyOfOriginalValue = copyOfOriginalValue;
			this.differentValue = differentValue;
		}
	}

	private final JavaTypeDescriptor<T> typeDescriptor;

	public AbstractDescriptorTest(JavaTypeDescriptor<T> typeDescriptor) {
		this.typeDescriptor = typeDescriptor;
	}

	private Data<T> testData;

	@Before
	public void setUp() throws Exception {
		testData = getTestData();
	}

	protected abstract Data<T> getTestData();

	protected abstract boolean shouldBeMutable();

	@Test
	public void testEquality() {
		assertFalse( testData.originalValue == testData.copyOfOriginalValue );
		assertTrue( typeDescriptor.areEqual( testData.originalValue, testData.originalValue ) );
		assertTrue( typeDescriptor.areEqual( testData.originalValue, testData.copyOfOriginalValue ) );
		assertFalse( typeDescriptor.areEqual( testData.originalValue, testData.differentValue ) );
	}

	@Test
	public void testExternalization() {
		// ensure the symmetry of toString/fromString
		String externalized = typeDescriptor.toString( testData.originalValue );
		T consumed = typeDescriptor.fromString( externalized );
		assertTrue( typeDescriptor.areEqual( testData.originalValue, consumed ) );
	}

	@Test
	public void testMutabilityPlan() {
		assertTrue( shouldBeMutable() == typeDescriptor.getMutabilityPlan().isMutable() );

		if ( Clob.class.isInstance( testData.copyOfOriginalValue )
				|| Blob.class.isInstance( testData.copyOfOriginalValue ) ) {
			return;
		}

		T copy = typeDescriptor.getMutabilityPlan().deepCopy( testData.copyOfOriginalValue );
		assertTrue( typeDescriptor.areEqual( copy, testData.copyOfOriginalValue ) );
		if ( ! shouldBeMutable() ) {
			assertTrue( copy == testData.copyOfOriginalValue );
		}

		// ensure the symmetry of assemble/disassebly
		Serializable cached = typeDescriptor.getMutabilityPlan().disassemble( testData.copyOfOriginalValue );
		if ( ! shouldBeMutable() ) {
			assertTrue( cached == testData.copyOfOriginalValue );
		}
		T reassembled = typeDescriptor.getMutabilityPlan().assemble( cached );
		assertTrue( typeDescriptor.areEqual( testData.originalValue, reassembled ) );
	}
}
