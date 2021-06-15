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
package org.hibernate.event.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

/**
 * 2011/10/20 Unit test for code added in EventCache for performance improvement. 
 * @author Wim Ockerman @ CISCO
 * 
 * 
 *
 */
public class EventCacheTest extends TestCase {
    
    public void testEntityToCopyFillFollowedByCopyToEntityMapping() {
        EventCache cache = new EventCache(); 
        Object entity = new Simple( 1 );
        Object copy = new Simple( 2 );
        
        cache.put(entity, copy);

		checkCacheConsistency( cache, 1 );

		assertTrue( cache.containsKey( entity ) );
        assertFalse( cache.containsKey( copy ) );
		assertTrue( cache.containsValue( copy ) );

		assertTrue( cache.invertMap().containsKey( copy ) );
        assertFalse( cache.invertMap().containsKey( entity ) );
		assertTrue( cache.invertMap().containsValue( entity ) );

		cache.clear();

		checkCacheConsistency( cache, 0 );

		assertFalse(cache.containsKey(entity));
        assertFalse(cache.invertMap().containsKey(copy));
	}

    public void testEntityToCopyFillFollowedByCopyToEntityMappingOnRemove() {
        EventCache cache = new EventCache(); 
        Object entity = new Simple( 1 );
        Object copy = new Simple( 2 );
        
        cache.put(entity, copy);

		checkCacheConsistency( cache, 1 );

		assertTrue(cache.containsKey(entity));
        assertFalse( cache.containsKey( copy ) );
        
        assertTrue( cache.invertMap().containsKey( copy ) );
        assertFalse( cache.invertMap().containsKey( entity ) );
        
        cache.remove( entity );

		checkCacheConsistency( cache, 0 );

        assertFalse(cache.containsKey(entity)); 
        assertFalse(cache.invertMap().containsKey(copy));        
    }
    
    public void testEntityToCopyFillFollowedByCopyToEntityUsingPutAll() {
        EventCache cache = new EventCache(); 
        Map<Object,Object> input = new HashMap<Object,Object>();
        Object entity1 = new Simple( 1 );
		//
        Object copy1 = new Integer( 2 );
        input.put(entity1, copy1); 
        Object entity2 = new Simple( 3 );
        Object copy2 = new Integer( 2 );
        input.put(entity2, copy2);
        cache.putAll(input);

		checkCacheConsistency( cache, 2 );

		assertTrue(cache.containsKey(entity1));
        assertFalse(cache.containsKey(copy1)); 
        assertTrue(cache.containsKey(entity2)); 
        assertFalse(cache.containsKey(copy2)); 

        assertTrue(cache.invertMap().containsKey(copy1));
        assertFalse(cache.invertMap().containsKey(entity1));

        assertTrue(cache.invertMap().containsKey(copy2));
        assertFalse(cache.invertMap().containsKey(entity2));
    }
    
    public void testEntityToCopyFillFollowedByCopyToEntityMappingUsingPutWithSetOperatedOnArg() {
        EventCache cache = new EventCache(); 
        Object entity = new Simple( 1 );
        Object copy = new Simple( 2 );
        
        cache.put(entity, copy, true);

		checkCacheConsistency( cache, 1 );

		assertTrue(cache.containsKey(entity));
        assertFalse( cache.containsKey( copy ) );

        assertTrue( cache.invertMap().containsKey( copy ) );
        assertFalse( cache.invertMap().containsKey( entity ) );
        
        cache.clear();

		checkCacheConsistency( cache, 0 );

		cache.put(entity, copy, false);

		checkCacheConsistency( cache, 1 );

		assertTrue(cache.containsKey(entity));
        assertFalse(cache.containsKey(copy)); 
    }

	public void testEntityToCopyFillFollowedByIterateEntrySet() {
		EventCache cache = new EventCache();
		Object entity = new Simple( 1 );
		Object copy = new Simple( 2 );

		cache.put( entity, copy, true );

		checkCacheConsistency( cache, 1 );

		Iterator it = cache.entrySet().iterator();
		assertTrue( it.hasNext() );
		Map.Entry entry = ( Map.Entry ) it.next();
		assertSame( entity, entry.getKey() );
		assertSame( copy, entry.getValue() );
		assertFalse( it.hasNext() );

	}

	public void testEntityToCopyFillFollowedByModifyEntrySet() {
		EventCache cache = new EventCache();
		Object entity = new Simple( 1 );
		Object copy = new Simple( 2 );

		cache.put( entity, copy, true );

		Iterator it = cache.entrySet().iterator();
		try {
			it.remove();
			fail( "should have thrown UnsupportedOperationException" );
		}
		catch ( UnsupportedOperationException ex ) {
			// expected
		}

		Map.Entry entry = (Map.Entry) cache.entrySet().iterator().next();
		try {
			cache.entrySet().remove( entry );
			fail( "should have thrown UnsupportedOperationException" );
		}
		catch ( UnsupportedOperationException ex ) {
			// expected
		}

		Map.Entry anotherEntry = new Map.Entry() {
			private Object key = new Simple( 3 );
			private Object value = 4;
			@Override
			public Object getKey() {
				return key;
			}

			@Override
			public Object getValue() {
				return value;
			}

			@Override
			public Object setValue(Object value) {
				Object oldValue = this.value;
				this.value = value;
				return oldValue;
			}
		};
		try {
			cache.entrySet().add( anotherEntry );
			fail( "should have thrown UnsupportedOperationException" );
		}
		catch ( UnsupportedOperationException ex ) {
			// expected
		}

	}

	public void testEntityToCopyFillFollowedByModifyKeys() {
		EventCache cache = new EventCache();
		Object entity = new Simple( 1 );
		Object copy = new Simple( 2 );

		cache.put( entity, copy, true );

		Iterator it = cache.keySet().iterator();
		try {
			it.remove();
			fail( "should have thrown UnsupportedOperationException" );
		}
		catch ( UnsupportedOperationException ex ) {
			// expected
		}

		try {
			cache.keySet().remove( entity );
			fail( "should have thrown UnsupportedOperationException" );
		}
		catch ( UnsupportedOperationException ex ) {
			// expected
		}

		Object newCopy = new Simple( 3 );
		try {
			cache.keySet().add( newCopy );
			fail( "should have thrown UnsupportedOperationException" );
		}
		catch ( UnsupportedOperationException ex ) {
			// expected
		}
	}

	public void testEntityToCopyFillFollowedByModifyValues() {
		EventCache cache = new EventCache();
		Object entity = new Simple( 1 );
		Object copy = new Simple( 2 );

		cache.put( entity, copy, true );

		Iterator it = cache.values().iterator();
		try {
			it.remove();
			fail( "should have thrown UnsupportedOperationException" );
		}
		catch ( UnsupportedOperationException ex ) {
			// expected
		}

		try {
			cache.values().remove( copy );
			fail( "should have thrown UnsupportedOperationException" );
		}
		catch ( UnsupportedOperationException ex ) {
			// expected
		}

		Object newCopy = new Simple( 3 );
		try {
			cache.values().add( newCopy );
			fail( "should have thrown UnsupportedOperationException" );
		}
		catch ( UnsupportedOperationException ex ) {
			// expected
		}
	}

	public void testEntityToCopyFillFollowedByModifyKeyOfEntrySetElement() {

		EventCache cache = new EventCache();
		Simple entity = new Simple( 1 );
		Simple copy = new Simple( 0 );
		cache.put(entity, copy, true);

		Map.Entry entry = (Map.Entry) cache.entrySet().iterator().next();
		( ( Simple ) entry.getKey() ).setValue( 2 );
		assertEquals( 2, entity.getValue() );

		checkCacheConsistency( cache, 1 );

		entry = (Map.Entry) cache.entrySet().iterator().next();
		assertSame( entity, entry.getKey() );
		assertSame( copy, entry.getValue() );
	}

	public void testEntityToCopyFillFollowedByModifyValueOfEntrySetElement() {

		EventCache cache = new EventCache();
		Simple entity = new Simple( 1 );
		Simple copy = new Simple( 0 );
		cache.put(entity, copy, true);

		Map.Entry entry = (Map.Entry) cache.entrySet().iterator().next();
		( ( Simple ) entry.getValue() ).setValue( 2 );
		assertEquals( 2, copy.getValue() );

		checkCacheConsistency( cache, 1 );

		entry = (Map.Entry) cache.entrySet().iterator().next();
		assertSame( entity, entry.getKey() );
		assertSame( copy, entry.getValue() );
	}

	public void testReplaceEntityCopy() {

		EventCache cache = new EventCache();
		Simple entity = new Simple( 1 );
		Simple copy = new Simple( 0 );
		cache.put(entity, copy);

		Simple copyNew = new Simple( 0 );
		assertSame( copy, cache.put( entity, copyNew ) );
		assertSame( copyNew, cache.get( entity ) );

		checkCacheConsistency( cache, 1 );

		copy = copyNew;
		copyNew = new Simple( 1 );
		assertSame( copy, cache.put( entity, copyNew ) );
		assertSame( copyNew, cache.get( entity ) );

		checkCacheConsistency( cache, 1 );
	}

	public void testCopyAssociatedWithNewAndExistingEntity() {

		EventCache cache = new EventCache();
		Simple entity = new Simple( 1 );
		Simple copy = new Simple( 0 );
		cache.put(entity, copy);

		try {
			cache.put( new Simple( 1 ), copy );
			fail( "should have thrown IllegalStateException");
		}
		catch( IllegalStateException ex ) {
			// expected
		}

	}

	public void testCopyAssociatedWith2ExistingEntities() {

		EventCache cache = new EventCache();
		Simple entity1 = new Simple( 1 );
		Simple copy1 = new Simple( 0 );
		cache.put(entity1, copy1);
		Simple entity2 = new Simple( 2 );
		Simple copy2 = new Simple( 0 );
		cache.put( entity2, copy2 );

		try {
			cache.put( entity1, copy2 );
			fail( "should have thrown IllegalStateException");
		}
		catch( IllegalStateException ex ) {
			// expected
		}
	}

	public void testRemoveNonExistingEntity() {

		EventCache cache = new EventCache();
		assertNull( cache.remove( new Simple( 1 ) ) );
	}

	private void checkCacheConsistency(EventCache cache, int expectedSize) {

		Set entrySet = cache.entrySet();
		Set cacheKeys = cache.keySet();
		Collection cacheValues = cache.values();
		Map invertedMap = cache.invertMap();

		assertEquals( expectedSize, entrySet.size() );
		assertEquals( expectedSize, cache.size() );
		assertEquals( expectedSize, cacheKeys.size() );
		assertEquals( expectedSize, cacheValues.size() );
		assertEquals( expectedSize, invertedMap.size() );

		for ( Object entry : cache.entrySet() ) {
			Map.Entry mapEntry = ( Map.Entry ) entry;
			assertSame( cache.get( mapEntry.getKey() ), mapEntry.getValue() );
			assertTrue( cacheKeys.contains( mapEntry.getKey() ) );
			assertTrue( cacheValues.contains( mapEntry.getValue() ) );
			assertSame( mapEntry.getKey(), invertedMap.get( mapEntry.getValue() ) );
		}
	}

	private static class Simple {
		private int value;

		public Simple(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}
}
