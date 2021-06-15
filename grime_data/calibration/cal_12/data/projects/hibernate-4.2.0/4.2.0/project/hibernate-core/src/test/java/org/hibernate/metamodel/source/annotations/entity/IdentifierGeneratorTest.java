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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.junit.Test;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.id.Assigned;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.id.MultipleHiLoPerTableGenerator;
import org.hibernate.id.SequenceHiLoGenerator;
import org.hibernate.id.UUIDHexGenerator;
import org.hibernate.metamodel.MetadataSources;
import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.metamodel.binding.EntityIdentifier;
import org.hibernate.metamodel.source.MappingException;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.testing.DialectCheck;
import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialect;
import org.hibernate.testing.RequiresDialectFeature;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * @author Hardy Ferentschik
 */
@RequiresDialect(H2Dialect.class)
public class IdentifierGeneratorTest extends BaseAnnotationBindingTestCase {
	@Entity
	class NoGenerationEntity {
		@Id
		private long id;
	}

	@Test
	@Resources(annotatedClasses = NoGenerationEntity.class)
	public void testNoIdGeneration() {
		EntityBinding binding = getEntityBinding( NoGenerationEntity.class );
        EntityIdentifier identifier = binding.getHierarchyDetails().getEntityIdentifier();
		IdentifierGenerator generator =identifier.getIdentifierGenerator();
        assertNotNull( generator );
        assertEquals( "Wrong generator", Assigned.class, generator.getClass() );
        assertFalse( identifier.isEmbedded() );

	}

	@Entity
	class AutoEntity {
		@Id
		@GeneratedValue
		private long id;

		public long getId() {
			return id;
		}
	}

	@Test
	@Resources(annotatedClasses = AutoEntity.class)
	public void testAutoGenerationType() {
		EntityBinding binding = getEntityBinding( AutoEntity.class );
		IdentifierGenerator generator = binding.getHierarchyDetails().getEntityIdentifier().getIdentifierGenerator();

		assertEquals( "Wrong generator", IdentityGenerator.class, generator.getClass() );
	}

	@Entity
	class TableEntity {
		@Id
		@GeneratedValue(strategy = GenerationType.TABLE)
		private long id;

		public long getId() {
			return id;
		}
	}

	@Test
	@Resources(annotatedClasses = TableEntity.class)
	public void testTableGenerationType() {
		EntityBinding binding = getEntityBinding( TableEntity.class );
		IdentifierGenerator generator = binding.getHierarchyDetails().getEntityIdentifier().getIdentifierGenerator();

		assertEquals( "Wrong generator", MultipleHiLoPerTableGenerator.class, generator.getClass() );
	}

	@Entity
	class SequenceEntity {
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE)
		private long id;

		public long getId() {
			return id;
		}
	}

	@Test
	@Resources(annotatedClasses = SequenceEntity.class)
	public void testSequenceGenerationType() {
		EntityBinding binding = getEntityBinding( SequenceEntity.class );
		IdentifierGenerator generator = binding.getHierarchyDetails().getEntityIdentifier().getIdentifierGenerator();

		assertEquals( "Wrong generator", SequenceHiLoGenerator.class, generator.getClass() );
	}


	@Entity
	class NamedGeneratorEntity {
		@Id
		@GeneratedValue(generator = "my-generator")
		private long id;

		public long getId() {
			return id;
		}
	}

	@Test
	public void testUndefinedGenerator() {
		try {
			sources = new MetadataSources( new ServiceRegistryBuilder().buildServiceRegistry() );
			sources.addAnnotatedClass( NamedGeneratorEntity.class );
			sources.buildMetadata();
			fail();
		}
		catch ( MappingException e ) {
			assertTrue( e.getMessage().startsWith( "Unable to find named generator" ) );
		}
	}

	@Entity
	@GenericGenerator(name = "my-generator", strategy = "uuid")
	class NamedGeneratorEntity2 {
		@Id
		@GeneratedValue(generator = "my-generator")
		private long id;

		public long getId() {
			return id;
		}
	}

	@Test
	@Resources(annotatedClasses = NamedGeneratorEntity2.class)
	public void testNamedGenerator() {
		EntityBinding binding = getEntityBinding( NamedGeneratorEntity2.class );
		IdentifierGenerator generator = binding.getHierarchyDetails().getEntityIdentifier().getIdentifierGenerator();

		assertEquals( "Wrong generator", UUIDHexGenerator.class, generator.getClass() );
	}
}


