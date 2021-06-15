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
package org.hibernate.build.gradle.testing.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.logging.Logging;
import org.slf4j.Logger;

import org.hibernate.build.qalab.DatabaseAllocation;
import org.hibernate.build.qalab.DatabaseAllocator;

/**
 * Basic support for {@link DatabaseProfile} implementations
 *
 * @author Steve Ebersole
 * @author Strong Liu
 */
public abstract class AbstractDatabaseProfileImpl implements DatabaseProfile {
	private static final Logger log = Logging.getLogger( AbstractDatabaseProfileImpl.class );

    private final String name;
	private final File profileDirectory;
	private final Project project;
	private final Map<String,String> hibernateProperties;
	private final DatabaseAllocation databaseAllocation;

	@SuppressWarnings( {"unchecked"})
	protected AbstractDatabaseProfileImpl(File profileDirectory, Project project) {
		this.profileDirectory = profileDirectory;
		this.name = profileDirectory.getName();
		this.project = project;

		this.hibernateProperties = new HashMap<String, String>();
		final File hibernatePropertiesFile = new File(
				new File( profileDirectory, "resources" ),
				"hibernate.properties"
		);
		if ( hibernatePropertiesFile.exists() ) {
			Properties props = new Properties();
			try {
				FileInputStream stream = new FileInputStream( hibernatePropertiesFile );
				try {
					props.load( stream );
				}
				finally {
					try {
						stream.close();
					}
					catch (IOException ignore) {
					}
				}
			}
			catch (IOException e) {
				log.warn( "Unable to read Hibernate properties for database profile [" + name + "]", e );
			}
			for ( String propName : props.stringPropertyNames() ) {
				hibernateProperties.put( propName, props.getProperty( propName ) );
			}
		}

		this.databaseAllocation = DatabaseAllocator.locate( project ).getAllocation( this );
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public File getDirectory() {
		return profileDirectory;
	}

	@Override
	public Map<String, String> getHibernateProperties() {
		return hibernateProperties;
	}

	@Override
	public DatabaseAllocation getDatabaseAllocation() {
		return databaseAllocation;
	}

	protected Configuration prepareConfiguration(String name) {
        Configuration configuration = getOrCreateConfiguration( name );
        configuration.setDescription( "The JDBC dependency configuration for the [" + name + "] profile" );
        return configuration;
    }

    protected Configuration getOrCreateConfiguration(String name) {
        Configuration configuration = project.getConfigurations().findByName( name );
        if ( configuration == null ) {
            configuration = project.getConfigurations().add( name );
        }
        return configuration;
    }
}
