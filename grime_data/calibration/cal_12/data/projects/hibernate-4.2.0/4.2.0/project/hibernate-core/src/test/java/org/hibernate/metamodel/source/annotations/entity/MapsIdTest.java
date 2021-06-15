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
package org.hibernate.metamodel.source.annotations.entity;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;

import org.junit.Test;

import org.hibernate.metamodel.MetadataSources;
import org.hibernate.metamodel.source.MappingException;
import org.hibernate.service.ServiceRegistryBuilder;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * @author Hardy Ferentschik
 */
public class MapsIdTest extends BaseAnnotationBindingTestCase {
	@Entity
	public class Employee {
		@Id
		long empId;
		String name;
	}

	@Embeddable
	public class DependentId {
		String name;
		long empid; // corresponds to PK type of Employee
	}

	@Entity
	public class Dependent {
		@Id
		// should be @EmbeddedId, but embedded id are not working atm
				DependentId id;

		@MapsId("empid")
		@OneToMany
		Employee emp; // maps the empid attribute of embedded id @ManyToOne Employee emp;
	}

	@Test
	@Resources(annotatedClasses = DependentId.class)
	public void testMapsIsOnOneToManyThrowsException() {
		try {
			sources = new MetadataSources( new ServiceRegistryBuilder().buildServiceRegistry() );
			sources.addAnnotatedClass( DependentId.class );
			sources.addAnnotatedClass( Dependent.class );
			sources.addAnnotatedClass( Employee.class );
			sources.buildMetadata();
			fail();
		}
		catch ( MappingException e ) {
			assertTrue(
					e.getMessage()
							.startsWith( "@MapsId can only be specified on a many-to-one or one-to-one associations" )
			);
			assertEquals(
					"Wrong error origin",
					"org.hibernate.metamodel.source.annotations.entity.MapsIdTest$Dependent",
					e.getOrigin().getName()
			);
		}
	}
}


