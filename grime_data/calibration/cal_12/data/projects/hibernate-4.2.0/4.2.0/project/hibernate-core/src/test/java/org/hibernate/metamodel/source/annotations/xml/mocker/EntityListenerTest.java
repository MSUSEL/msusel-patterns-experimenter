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
package org.hibernate.metamodel.source.annotations.xml.mocker;

import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.junit.Test;

import org.hibernate.metamodel.source.annotations.JPADotNames;
import org.hibernate.metamodel.source.annotations.xml.PseudoJpaDotNames;

import static org.junit.Assert.assertEquals;

/**
 * @author Strong Liu
 */
public class EntityListenerTest extends AbstractMockerTest {
	@Test
	public void basicEntityListenerMockTest() {
		Index index = getMockedIndex( "listener.xml" );
		DotName itemName = DotName.createSimple( Item.class.getName() );
		DotName itemListenerName = DotName.createSimple( ItemListener.class.getName() );
		ClassInfo itemClassInfo = index.getClassByName( itemName );
		assertEquals( 2, itemClassInfo.annotations().size() );
		//entity
		assertHasAnnotation( index, itemName, JPADotNames.ENTITY );
		assertHasAnnotation( index, itemName, JPADotNames.ENTITY_LISTENERS );
		//listener
		assertHasAnnotation( index, itemListenerName, JPADotNames.PRE_PERSIST );
		assertHasAnnotation( index, itemListenerName, JPADotNames.POST_PERSIST );
		//assert global configurations
		assertHasAnnotation( index, PseudoJpaDotNames.DEFAULT_DELIMITED_IDENTIFIERS );
		assertHasAnnotation( index, PseudoJpaDotNames.DEFAULT_ACCESS );
		assertHasAnnotation( index, PseudoJpaDotNames.DEFAULT_ENTITY_LISTENERS );
		assertHasAnnotation( index, PseudoJpaDotNames.DEFAULT_PRE_PERSIST );
		assertHasAnnotation( index, PseudoJpaDotNames.DEFAULT_POST_PERSIST );


	}
}
