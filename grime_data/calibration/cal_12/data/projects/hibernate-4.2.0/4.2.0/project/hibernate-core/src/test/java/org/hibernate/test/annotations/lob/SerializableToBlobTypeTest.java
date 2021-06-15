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
package org.hibernate.test.annotations.lob;

import static org.junit.Assert.assertEquals;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.hibernate.type.SerializableToBlobType;
import org.hibernate.type.Type;
import org.junit.Test;

/**
 * Test type definition for SerializableToBlobType
 * 
 * @author Janario Oliveira
 */
@RequiresDialectFeature( DialectChecks.SupportsExpectedLobUsagePattern.class )
public class SerializableToBlobTypeTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testTypeDefinition() {
		Configuration cfg = configuration();
		PersistentClass pc = cfg.getClassMapping( EntitySerialize.class.getName() );

		// explicitLob of SerializableToBlobType
		Type explicitLobType = pc.getProperty( "explicitLob" ).getType();
		assertEquals( ExplicitSerializable.class, explicitLobType.getReturnedClass() );
		assertEquals( SerializableToBlobType.class.getName(), explicitLobType.getName() );

		// explicit of ExplicitSerializableType
		Type explicitType = pc.getProperty( "explicit" ).getType();
		assertEquals( ExplicitSerializable.class, explicitType.getReturnedClass() );
		assertEquals( ExplicitSerializableType.class.getName(), explicitType.getName() );

		// implicit of ImplicitSerializableType
		Type implicitType = pc.getProperty( "implicit" ).getType();
		assertEquals( ImplicitSerializable.class, implicitType.getReturnedClass() );
		assertEquals( ImplicitSerializableType.class.getName(), implicitType.getName() );

		// explicitOverridingImplicit ExplicitSerializableType overrides ImplicitSerializableType
		Type overrideType = pc.getProperty( "explicitOverridingImplicit" ).getType();
		assertEquals( ImplicitSerializable.class, overrideType.getReturnedClass() );
		assertEquals( ExplicitSerializableType.class.getName(), overrideType.getName() );
	}

	@Test
	public void testPersist() {
		EntitySerialize entitySerialize = new EntitySerialize();

		entitySerialize.explicitLob = new ExplicitSerializable();
		entitySerialize.explicitLob.value = "explicitLob";
		entitySerialize.explicitLob.defaultValue = "defaultExplicitLob";

		entitySerialize.explicit = new ExplicitSerializable();
		entitySerialize.explicit.value = "explicit";

		entitySerialize.implicit = new ImplicitSerializable();
		entitySerialize.implicit.value = "implicit";

		entitySerialize.explicitOverridingImplicit = new ImplicitSerializable();
		entitySerialize.explicitOverridingImplicit.value = "explicitOverridingImplicit";

		Session session = openSession();
		session.getTransaction().begin();
		session.persist( entitySerialize );

		session.getTransaction().commit();
		session.close();

		session = openSession();

		EntitySerialize persistedSerialize = (EntitySerialize) session.get( EntitySerialize.class, entitySerialize.id );
		assertEquals( "explicitLob", persistedSerialize.explicitLob.value );
		assertEquals( "explicit", persistedSerialize.explicit.value );
		assertEquals( "implicit", persistedSerialize.implicit.value );
		assertEquals( "explicitOverridingImplicit", persistedSerialize.explicitOverridingImplicit.value );

		assertEquals( "defaultExplicitLob", persistedSerialize.explicitLob.defaultValue );

		session.close();
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] { EntitySerialize.class };
	}
}
