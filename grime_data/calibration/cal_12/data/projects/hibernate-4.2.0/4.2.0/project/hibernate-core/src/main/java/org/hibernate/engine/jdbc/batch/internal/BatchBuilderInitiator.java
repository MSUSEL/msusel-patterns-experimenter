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

import org.hibernate.cfg.Environment;
import org.hibernate.engine.jdbc.batch.spi.BatchBuilder;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.spi.BasicServiceInitiator;
import org.hibernate.service.spi.ServiceException;
import org.hibernate.service.spi.ServiceRegistryImplementor;

/**
 * Initiator for the {@link BatchBuilder} service
 *
 * @author Steve Ebersole
 */
public class BatchBuilderInitiator implements BasicServiceInitiator<BatchBuilder> {
	public static final BatchBuilderInitiator INSTANCE = new BatchBuilderInitiator();
	public static final String BUILDER = "hibernate.jdbc.batch.builder";

	@Override
	public Class<BatchBuilder> getServiceInitiated() {
		return BatchBuilder.class;
	}

	@Override
	public BatchBuilder initiateService(Map configurationValues, ServiceRegistryImplementor registry) {
		final Object builder = configurationValues.get( BUILDER );
		if ( builder == null ) {
			return new BatchBuilderImpl(
					ConfigurationHelper.getInt( Environment.STATEMENT_BATCH_SIZE, configurationValues, 1 )
			);
		}

		if ( BatchBuilder.class.isInstance( builder ) ) {
			return (BatchBuilder) builder;
		}

		final String builderClassName = builder.toString();
		try {
			return (BatchBuilder) registry.getService( ClassLoaderService.class ).classForName( builderClassName ).newInstance();
		}
		catch (Exception e) {
			throw new ServiceException( "Could not build explicit BatchBuilder [" + builderClassName + "]", e );
		}
	}
}
