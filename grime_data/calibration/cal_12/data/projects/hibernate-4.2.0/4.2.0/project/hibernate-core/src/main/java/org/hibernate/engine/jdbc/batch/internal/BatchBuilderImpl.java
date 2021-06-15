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
package org.hibernate.engine.jdbc.batch.internal;

import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.cfg.Environment;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.hibernate.engine.jdbc.batch.spi.BatchBuilder;
import org.hibernate.engine.jdbc.batch.spi.BatchKey;
import org.hibernate.engine.jdbc.spi.JdbcCoordinator;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.spi.Configurable;

/**
 * A builder for {@link Batch} instances.
 *
 * @author Steve Ebersole
 */
public class BatchBuilderImpl implements BatchBuilder, Configurable {

	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, BatchBuilderImpl.class.getName() );

	private int size;

	public BatchBuilderImpl() {
	}

	@Override
	public void configure(Map configurationValues) {
		size = ConfigurationHelper.getInt( Environment.STATEMENT_BATCH_SIZE, configurationValues, size );
	}

	public BatchBuilderImpl(int size) {
		this.size = size;
	}

	public void setJdbcBatchSize(int size) {
		this.size = size;
	}

	@Override
	public Batch buildBatch(BatchKey key, JdbcCoordinator jdbcCoordinator) {
		LOG.tracef( "Building batch [size=%s]", size );
		return size > 1
				? new BatchingBatch( key, jdbcCoordinator, size )
				: new NonBatchingBatch( key, jdbcCoordinator );
	}

	@Override
	public String getManagementDomain() {
		return null; // use Hibernate default domain
	}

	@Override
	public String getManagementServiceType() {
		return null;  // use Hibernate default scheme
	}

	@Override
	public Object getManagementBean() {
		return this;
	}
}

