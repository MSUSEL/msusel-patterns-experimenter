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
package org.hibernate.tool.hbm2ddl;

import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.spi.BasicServiceInitiator;
import org.hibernate.service.spi.ServiceRegistryImplementor;

/**
 * Instantiates and configures an appropriate {@link ImportSqlCommandExtractor}. By default
 * {@link SingleLineSqlCommandExtractor} is used.
 *
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class ImportSqlCommandExtractorInitiator implements BasicServiceInitiator<ImportSqlCommandExtractor> {
	public static final ImportSqlCommandExtractorInitiator INSTANCE = new ImportSqlCommandExtractorInitiator();
	public static final ImportSqlCommandExtractor DEFAULT_EXTRACTOR = new SingleLineSqlCommandExtractor();

	@Override
	public ImportSqlCommandExtractor initiateService(Map configurationValues, ServiceRegistryImplementor registry) {
		String extractorClassName = (String) configurationValues.get( Environment.HBM2DDL_IMPORT_FILES_SQL_EXTRACTOR );
		if ( StringHelper.isEmpty( extractorClassName ) ) {
			return DEFAULT_EXTRACTOR;
		}
		final ClassLoaderService classLoaderService = registry.getService( ClassLoaderService.class );
		return instantiateExplicitCommandExtractor( extractorClassName, classLoaderService );
	}

	private ImportSqlCommandExtractor instantiateExplicitCommandExtractor(String extractorClassName,
																		  ClassLoaderService classLoaderService) {
		try {
			return (ImportSqlCommandExtractor) classLoaderService.classForName( extractorClassName ).newInstance();
		}
		catch ( Exception e ) {
			throw new HibernateException(
					"Could not instantiate import sql command extractor [" + extractorClassName + "]", e
			);
		}
	}

	@Override
	public Class<ImportSqlCommandExtractor> getServiceInitiated() {
		return ImportSqlCommandExtractor.class;
	}
}
