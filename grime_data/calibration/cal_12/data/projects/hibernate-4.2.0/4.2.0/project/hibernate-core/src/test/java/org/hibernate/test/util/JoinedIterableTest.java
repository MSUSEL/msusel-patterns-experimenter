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
package org.hibernate.test.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Test;

import org.hibernate.internal.util.collections.JoinedIterable;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Steve Ebersole
 */
public class JoinedIterableTest extends BaseUnitTestCase {
	@Test
	public void testNullIterables() {
		try {
			new JoinedIterable<String>( null );
			fail();
		}
		catch (NullPointerException ex) {
			// expected
		}
	}

	@Test
	public void testSingleEmptyIterable() {
		Set<String> emptyList = new HashSet<String>();
		List<Iterable<String>> iterableSets = new ArrayList<Iterable<String>>(  );
		iterableSets.add( emptyList );
		Iterable<String> iterable = new JoinedIterable<String>( iterableSets );
		assertFalse( iterable.iterator().hasNext() );
		try {
			iterable.iterator().next();
			fail( "Should have thrown NoSuchElementException because the underlying collection is empty.");
		}
		catch ( NoSuchElementException ex ) {
			// expected
		}
		try {
			iterable.iterator().remove();
			fail( "Should have thrown IllegalStateException because the underlying collection is empty." );
		}
		catch ( IllegalStateException ex ) {
			// expected
		}
		for ( String s : iterable ) {
			fail( "Should not have entered loop because underlying collection is empty");
		}
	}

	@Test
	public void testSingleIterableOfSingletonCollection() {
		final String str = "a string";
		Set<String> singleTonSet = new HashSet<String>( 1 );
		singleTonSet.add( str );
		List<Iterable<String>> iterableSets = new ArrayList<Iterable<String>>(  );
		iterableSets.add( singleTonSet );
		Iterable<String> iterable = new JoinedIterable<String>( iterableSets );
		assertTrue( iterable.iterator().hasNext() );
		assertSame( str, iterable.iterator().next() );
		assertFalse( iterable.iterator().hasNext() );
		try {
			iterable.iterator().next();
			fail( "Should have thrown NoSuchElementException because the underlying collection is empty.");
		}
		catch ( NoSuchElementException ex ) {
			// expected
		}
		for ( String s : iterable ) {
			fail( "should not have entered loop because underlying iterator should have been exhausted." );
		}
		assertEquals( 1, singleTonSet.size() );
		iterable = new JoinedIterable<String>( iterableSets );
		for ( String s : iterable ) {
			assertSame( str, s );
			iterable.iterator().remove();
		}
		assertTrue( singleTonSet.isEmpty() );
	}

	@Test
	public void testJoinedIterables() {
		List<Iterable<Integer>> listOfIterables = new ArrayList<Iterable<Integer>>(  );

		List<Integer> twoElementList = Arrays.asList( 0, 1 );
		listOfIterables.add( twoElementList );

		List<Integer> emptyList = new ArrayList<Integer>(  );
		listOfIterables.add( emptyList );

		List<Integer> oneElementList = Arrays.asList( 2 );
		listOfIterables.add( oneElementList );

		List<Integer> threeElementList = Arrays.asList( 3, 4, 5 );
		listOfIterables.add( threeElementList );

		JoinedIterable<Integer> joinedIterable = new JoinedIterable<Integer>( listOfIterables );

		int i = 0;
		for ( Integer val : joinedIterable ) {
			assertEquals( Integer.valueOf( i ), val );
			i++;
		}
	}
}
