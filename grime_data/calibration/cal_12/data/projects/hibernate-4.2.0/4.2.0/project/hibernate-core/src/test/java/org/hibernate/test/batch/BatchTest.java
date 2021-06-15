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
package org.hibernate.test.batch;
import java.math.BigDecimal;

import org.junit.Test;

import org.hibernate.CacheMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * This is how to do batch processing in Hibernate. Remember to enable JDBC batch updates, or this test will take a
 * VeryLongTime!
 *
 * @author Gavin King
 */
public class BatchTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "batch/DataPoint.hbm.xml" };
	}

	@Override
	public String getCacheConcurrencyStrategy() {
		return null;
	}

	@Override
	public void configure(Configuration cfg) {
		cfg.setProperty( Environment.STATEMENT_BATCH_SIZE, "20" );
	}

	@Test
	public void testBatchInsertUpdate() {
		long start = System.currentTimeMillis();
		final int N = 5000; //26 secs with batch flush, 26 without
		//final int N = 100000; //53 secs with batch flush, OOME without
		//final int N = 250000; //137 secs with batch flush, OOME without
		int batchSize = ( ( SessionFactoryImplementor ) sessionFactory() ).getSettings().getJdbcBatchSize();
		doBatchInsertUpdate( N, batchSize );
		System.out.println( System.currentTimeMillis() - start );
	}

	@Test
	public void testBatchInsertUpdateSizeEqJdbcBatchSize() {
		int batchSize = ( ( SessionFactoryImplementor ) sessionFactory() ).getSettings().getJdbcBatchSize();
		doBatchInsertUpdate( 50, batchSize );
	}

	@Test
	public void testBatchInsertUpdateSizeLtJdbcBatchSize() {
		int batchSize = ( ( SessionFactoryImplementor ) sessionFactory() ).getSettings().getJdbcBatchSize();
		doBatchInsertUpdate( 50, batchSize - 1 );
	}

	@Test
	public void testBatchInsertUpdateSizeGtJdbcBatchSize() {
		long start = System.currentTimeMillis();
		int batchSize = ( ( SessionFactoryImplementor ) sessionFactory() ).getSettings().getJdbcBatchSize();
		doBatchInsertUpdate( 50, batchSize + 1 );
	}

	public void doBatchInsertUpdate(int nEntities, int nBeforeFlush) {
		Session s = openSession();
		s.setCacheMode( CacheMode.IGNORE );
		Transaction t = s.beginTransaction();
		for ( int i = 0; i < nEntities; i++ ) {
			DataPoint dp = new DataPoint();
			dp.setX( new BigDecimal( i * 0.1d ).setScale( 19, BigDecimal.ROUND_DOWN ) );
			dp.setY( new BigDecimal( Math.cos( dp.getX().doubleValue() ) ).setScale( 19, BigDecimal.ROUND_DOWN ) );
			s.save( dp );
			if ( i + 1 % nBeforeFlush == 0 ) {
				s.flush();
				s.clear();
			}
		}
		t.commit();
		s.close();

		s = openSession();
		s.setCacheMode( CacheMode.IGNORE );
		t = s.beginTransaction();
		int i = 0;
		ScrollableResults sr = s.createQuery( "from DataPoint dp order by dp.x asc" )
				.scroll( ScrollMode.FORWARD_ONLY );
		while ( sr.next() ) {
			DataPoint dp = ( DataPoint ) sr.get( 0 );
			dp.setDescription( "done!" );
			if ( ++i % nBeforeFlush == 0 ) {
				s.flush();
				s.clear();
			}
		}
		t.commit();
		s.close();

		s = openSession();
		s.setCacheMode( CacheMode.IGNORE );
		t = s.beginTransaction();
		i = 0;
		sr = s.createQuery( "from DataPoint dp order by dp.x asc" )
				.scroll( ScrollMode.FORWARD_ONLY );
		while ( sr.next() ) {
			DataPoint dp = ( DataPoint ) sr.get( 0 );
			s.delete( dp );
			if ( ++i % nBeforeFlush == 0 ) {
				s.flush();
				s.clear();
			}
		}
		t.commit();
		s.close();
	}
}

