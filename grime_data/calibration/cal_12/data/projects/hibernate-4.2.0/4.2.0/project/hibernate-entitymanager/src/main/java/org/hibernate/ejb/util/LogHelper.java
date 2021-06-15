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
package org.hibernate.ejb.util;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import javax.persistence.spi.PersistenceUnitInfo;

/**
 * @author Emmanuel Bernard
 */
public final class LogHelper {
	private LogHelper() {
	}

	public static String logPersistenceUnitInfo(PersistenceUnitInfo unitInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append( "PersistenceUnitInfo [\n\t" )
				.append( "name: " )
				.append( unitInfo.getPersistenceUnitName() )
				.append( "\n\t" )
				.append( "persistence provider classname: " )
				.append( unitInfo.getPersistenceProviderClassName() )
				.append( "\n\t" )
				.append( "classloader: " )
				.append( unitInfo.getClassLoader() )
				.append( "\n\t" )
				.append( "Temporary classloader: " )
				.append( unitInfo.getNewTempClassLoader() )
				.append( "\n\t" )
				.append( "excludeUnlistedClasses: " )
				.append( unitInfo.excludeUnlistedClasses() )
				.append( "\n\t" )
				.append( "JTA datasource: " )
				.append( unitInfo.getJtaDataSource() )
				.append( "\n\t" )
				.append( "Non JTA datasource: " )
				.append( unitInfo.getNonJtaDataSource() )
				.append( "\n\t" )
				.append( "Transaction type: " )
				.append( unitInfo.getTransactionType() )
				.append( "\n\t" )
				.append( "PU root URL: " )
				.append( unitInfo.getPersistenceUnitRootUrl() )
				.append( "\n\t" )
				.append( "Shared Cache Mode: " )
				.append( unitInfo.getSharedCacheMode() )
				.append( "\n\t" )
				.append( "Validation Mode: " )
				.append( unitInfo.getValidationMode() )
				.append( "\n\t" );
		sb.append( "Jar files URLs [" );
		List<URL> jarFileUrls = unitInfo.getJarFileUrls();
		if ( jarFileUrls != null ) {
			for ( URL url : jarFileUrls ) {
				sb.append( "\n\t\t" ).append( url );
			}
		}
		sb.append( "]\n\t" );
		sb.append( "Managed classes names [" );
		List<String> classNames = unitInfo.getManagedClassNames();
		if ( classNames != null ) {
			for ( String className : classNames ) {
				sb.append( "\n\t\t" ).append( className );
			}
		}
		sb.append( "]\n\t" );
		sb.append( "Mapping files names [" );
		List<String> mappingFiles = unitInfo.getMappingFileNames();
		if ( mappingFiles != null ) {
			for ( String file : mappingFiles ) {
				sb.append( "\n\t\t" ).append( file );
			}
		}
		sb.append( "]\n\t" );
		sb.append( "Properties [" );
		Properties properties = unitInfo.getProperties();
		if (properties != null) {
			Enumeration names = properties.propertyNames();
			while ( names.hasMoreElements() ) {
				String name = (String) names.nextElement();
				sb.append( "\n\t\t" ).append( name ).append( ": " ).append( properties.getProperty( name ) );
			}
		}
		sb.append( "]" );
		return sb.toString();
	}

}
