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
package org.hibernate.id.enhanced;

import java.util.Properties;

import org.junit.Test;

import org.hibernate.MappingException;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.cfg.ObjectNameNormalizer;
import org.hibernate.dialect.Dialect;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.testing.junit4.BaseUnitTestCase;
import org.hibernate.type.StandardBasicTypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests that SequenceStyleGenerator configures itself as expected in various scenarios
 *
 * @author Steve Ebersole
 */
public class SequenceStyleConfigUnitTest extends BaseUnitTestCase {
	private void assertClassAssignability(Class expected, Class actual) {
		if ( ! expected.isAssignableFrom( actual ) ) {
			fail( "Actual type [" + actual.getName() + "] is not assignable to expected type [" + expected.getName() + "]" );
		}
	}


	/**
	 * Test all params defaulted with a dialect supporting sequences
	 */
	@Test
	public void testDefaultedSequenceBackedConfiguration() {
		Dialect dialect = new SequenceDialect();
		Properties props = buildGeneratorPropertiesBase();
		SequenceStyleGenerator generator = new SequenceStyleGenerator();
		generator.configure( StandardBasicTypes.LONG, props, dialect );

		assertClassAssignability( SequenceStructure.class, generator.getDatabaseStructure().getClass() );
		assertClassAssignability( OptimizerFactory.NoopOptimizer.class, generator.getOptimizer().getClass() );
		assertEquals( SequenceStyleGenerator.DEF_SEQUENCE_NAME, generator.getDatabaseStructure().getName() );
	}

	private Properties buildGeneratorPropertiesBase() {
		Properties props = new Properties();
		props.put(
				PersistentIdentifierGenerator.IDENTIFIER_NORMALIZER,
				new ObjectNameNormalizer() {
					protected boolean isUseQuotedIdentifiersGlobally() {
						return false;
					}

					protected NamingStrategy getNamingStrategy() {
						return null;
					}
				}
		);
		return props;
	}

	/**
	 * Test all params defaulted with a dialect which does not support sequences
	 */
	@Test
	public void testDefaultedTableBackedConfiguration() {
		Dialect dialect = new TableDialect();
		Properties props = buildGeneratorPropertiesBase();
		SequenceStyleGenerator generator = new SequenceStyleGenerator();
		generator.configure( StandardBasicTypes.LONG, props, dialect );

		assertClassAssignability( TableStructure.class, generator.getDatabaseStructure().getClass() );
		assertClassAssignability( OptimizerFactory.NoopOptimizer.class, generator.getOptimizer().getClass() );
		assertEquals( SequenceStyleGenerator.DEF_SEQUENCE_NAME, generator.getDatabaseStructure().getName() );
	}

	/**
	 * Test default optimizer selection for sequence backed generators
	 * based on the configured increment size; both in the case of the
	 * dialect supporting pooled sequences (pooled) and not (hilo)
	 */
	@Test
	public void testDefaultOptimizerBasedOnIncrementBackedBySequence() {
		Properties props = buildGeneratorPropertiesBase();
		props.setProperty( SequenceStyleGenerator.INCREMENT_PARAM, "10" );

		// for dialects which do not support pooled sequences, we default to pooled+table
		Dialect dialect = new SequenceDialect();
		SequenceStyleGenerator generator = new SequenceStyleGenerator();
		generator.configure( StandardBasicTypes.LONG, props, dialect );
		assertClassAssignability( TableStructure.class, generator.getDatabaseStructure().getClass() );
		assertClassAssignability( OptimizerFactory.PooledOptimizer.class, generator.getOptimizer().getClass() );
		assertEquals( SequenceStyleGenerator.DEF_SEQUENCE_NAME, generator.getDatabaseStructure().getName() );

		// for dialects which do support pooled sequences, we default to pooled+sequence
		dialect = new PooledSequenceDialect();
		generator = new SequenceStyleGenerator();
		generator.configure( StandardBasicTypes.LONG, props, dialect );
		assertClassAssignability( SequenceStructure.class, generator.getDatabaseStructure().getClass() );
		assertClassAssignability( OptimizerFactory.PooledOptimizer.class, generator.getOptimizer().getClass() );
		assertEquals( SequenceStyleGenerator.DEF_SEQUENCE_NAME, generator.getDatabaseStructure().getName() );
	}

	/**
	 * Test default optimizer selection for table backed generators
	 * based on the configured increment size.  Here we always prefer
	 * pooled.
	 */
	@Test
	public void testDefaultOptimizerBasedOnIncrementBackedByTable() {
		Properties props = buildGeneratorPropertiesBase();
		props.setProperty( SequenceStyleGenerator.INCREMENT_PARAM, "10" );
		Dialect dialect = new TableDialect();
		SequenceStyleGenerator generator = new SequenceStyleGenerator();
		generator.configure( StandardBasicTypes.LONG, props, dialect );
		assertClassAssignability( TableStructure.class, generator.getDatabaseStructure().getClass() );
		assertClassAssignability( OptimizerFactory.PooledOptimizer.class, generator.getOptimizer().getClass() );
		assertEquals( SequenceStyleGenerator.DEF_SEQUENCE_NAME, generator.getDatabaseStructure().getName() );
	}

	/**
	 * Test forcing of table as backing strucuture with dialect supporting sequences
	 */
	@Test
	public void testForceTableUse() {
		Dialect dialect = new SequenceDialect();
		Properties props = buildGeneratorPropertiesBase();
		props.setProperty( SequenceStyleGenerator.FORCE_TBL_PARAM, "true" );
		SequenceStyleGenerator generator = new SequenceStyleGenerator();
		generator.configure( StandardBasicTypes.LONG, props, dialect );
		assertClassAssignability( TableStructure.class, generator.getDatabaseStructure().getClass() );
		assertClassAssignability( OptimizerFactory.NoopOptimizer.class, generator.getOptimizer().getClass() );
		assertEquals( SequenceStyleGenerator.DEF_SEQUENCE_NAME, generator.getDatabaseStructure().getName() );
	}

	/**
	 * Test explicitly specifying both optimizer and increment
	 */
	@Test
	public void testExplicitOptimizerWithExplicitIncrementSize() {
		// with sequence ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		final Dialect dialect = new SequenceDialect();

		// optimizer=none w/ increment > 1 => should honor optimizer
		Properties props = buildGeneratorPropertiesBase();
		props.setProperty( SequenceStyleGenerator.OPT_PARAM, OptimizerFactory.StandardOptimizerDescriptor.NONE.getExternalName() );
		props.setProperty( SequenceStyleGenerator.INCREMENT_PARAM, "20" );
		SequenceStyleGenerator generator = new SequenceStyleGenerator();
		generator.configure( StandardBasicTypes.LONG, props, dialect );
		assertClassAssignability( SequenceStructure.class, generator.getDatabaseStructure().getClass() );
		assertClassAssignability( OptimizerFactory.NoopOptimizer.class, generator.getOptimizer().getClass() );
		assertEquals( 1, generator.getOptimizer().getIncrementSize() );
		assertEquals( 1, generator.getDatabaseStructure().getIncrementSize() );

		// optimizer=hilo w/ increment > 1 => hilo
		props = buildGeneratorPropertiesBase();
		props.setProperty( SequenceStyleGenerator.OPT_PARAM, OptimizerFactory.StandardOptimizerDescriptor.HILO.getExternalName() );
		props.setProperty( SequenceStyleGenerator.INCREMENT_PARAM, "20" );
		generator = new SequenceStyleGenerator();
		generator.configure( StandardBasicTypes.LONG, props, dialect );
		assertClassAssignability( SequenceStructure.class, generator.getDatabaseStructure().getClass() );
		assertClassAssignability( OptimizerFactory.HiLoOptimizer.class, generator.getOptimizer().getClass() );
		assertEquals( 20, generator.getOptimizer().getIncrementSize() );
		assertEquals( 20, generator.getDatabaseStructure().getIncrementSize() );

		// optimizer=pooled w/ increment > 1 => hilo
		props = buildGeneratorPropertiesBase();
		props.setProperty( SequenceStyleGenerator.OPT_PARAM, OptimizerFactory.StandardOptimizerDescriptor.POOLED.getExternalName() );
		props.setProperty( SequenceStyleGenerator.INCREMENT_PARAM, "20" );
		generator = new SequenceStyleGenerator();
		generator.configure( StandardBasicTypes.LONG, props, dialect );
		// because the dialect reports to not support pooled seqyences, the expectation is that we will
		// use a table for the backing structure...
		assertClassAssignability( TableStructure.class, generator.getDatabaseStructure().getClass() );
		assertClassAssignability( OptimizerFactory.PooledOptimizer.class, generator.getOptimizer().getClass() );
		assertEquals( 20, generator.getOptimizer().getIncrementSize() );
		assertEquals( 20, generator.getDatabaseStructure().getIncrementSize() );
	}

	@Test
	public void testPreferPooledLoSettingHonored() {
		final Dialect dialect = new PooledSequenceDialect();

		Properties props = buildGeneratorPropertiesBase();
		props.setProperty( SequenceStyleGenerator.INCREMENT_PARAM, "20" );
		SequenceStyleGenerator generator = new SequenceStyleGenerator();
		generator.configure( StandardBasicTypes.LONG, props, dialect );
		assertClassAssignability( SequenceStructure.class, generator.getDatabaseStructure().getClass() );
		assertClassAssignability( OptimizerFactory.PooledOptimizer.class, generator.getOptimizer().getClass() );

		props.setProperty( Environment.PREFER_POOLED_VALUES_LO, "true" );
		generator = new SequenceStyleGenerator();
		generator.configure( StandardBasicTypes.LONG, props, dialect );
		assertClassAssignability( SequenceStructure.class, generator.getDatabaseStructure().getClass() );
		assertClassAssignability( OptimizerFactory.PooledLoOptimizer.class, generator.getOptimizer().getClass() );
	}

	private static class TableDialect extends Dialect {
		public boolean supportsSequences() {
			return false;
		}
	}

	private static class SequenceDialect extends Dialect {
		public boolean supportsSequences() {
			return true;
		}
		public boolean supportsPooledSequences() {
			return false;
		}
		public String getSequenceNextValString(String sequenceName) throws MappingException {
			return "";
		}
	}

	private static class PooledSequenceDialect extends SequenceDialect {
		public boolean supportsPooledSequences() {
			return true;
		}
	}
}
