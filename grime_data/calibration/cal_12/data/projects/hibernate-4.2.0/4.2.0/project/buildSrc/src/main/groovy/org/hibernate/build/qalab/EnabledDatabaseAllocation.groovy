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
package org.hibernate.build.qalab

import org.gradle.api.Project
import org.hibernate.build.gradle.testing.database.DatabaseProfile
import org.hibernate.build.gradle.util.BuildException
import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger

/**
 * @author Steve Ebersole
 */
class EnabledDatabaseAllocation implements DatabaseAllocation {
    private static final Logger log = Logging.getLogger( DatabaseAllocator.class );

    private static final String DB_ALLOCATOR_URL = "http://dballocator.mw.lab.eng.bos.redhat.com:8080/Allocator/AllocatorServlet";
    private static final String ALLOCATOR_OUTPUT_FILE_NAME = "allocated-db.properties";
    private static final String DB_ALLOCATION_URL_POSTFIX = "hibernate-matrix-dballocation-url-postfix";

    private static final String DRIVER_PROP = "hibernate.connection.driver_class";
    private static final String URL_PROP = "hibernate.connection.url";
    private static final String USERNAME_PROP = "hibernate.connection.username";
    private static final String PASSWORD_PROP = "hibernate.connection.password";

    private static final int RETRIES = 30
    private static final int EXPIRY = 300;

    private final DatabaseProfile databaseProfile
	private final AntBuilder ant;

    private final File allocatorOutputFile;
    private final String requester;
    private final String uuid;

    private final File tmpFile;

    private final Map<String,String> properties;

    EnabledDatabaseAllocation(AntBuilder ant, DatabaseProfile databaseProfile, File outputDirectory) {
		this.ant = ant;
        this.databaseProfile = databaseProfile;

		outputDirectory.mkdirs()

        this.allocatorOutputFile = new File( outputDirectory, ALLOCATOR_OUTPUT_FILE_NAME );
        this.tmpFile = new File( outputDirectory, "tmpfile" );

        if ( System.properties.containsKey("hibernate-matrix-dballocation-requestee") ) {
            requester = System.properties["hibernate-matrix-dballocation-requestee"]
        }
        else {
            requester = "hibernate"
        }

        if ( allocatorOutputFile.exists() ) {
            allocatorOutputFile.delete()
        }

        int attempts = 0;
        while ( !(allocatorOutputFile.exists() && allocatorOutputFile.length() > 0) ) {
            if ( attempts >= RETRIES ) {
                throw new BuildException( 'Database unavailable' );
            }
            if ( attempts > 0 ) {
				log.lifecycle( "Trouble accessing Allocator servlet; waiting before trying again" );
                Thread.sleep( 60000 );
            }
            def allocatorUrl = DB_ALLOCATOR_URL +
                    "?operation=alloc&label=${databaseProfile.name}&requestee=${requester}&expiry=${EXPIRY}"
            ant.get(
                    src: allocatorUrl,
                    dest: allocatorOutputFile.absolutePath,
                    ignoreerrors: 'true'
            );
            attempts++
        }

        def allocatorProps = new Properties();
        allocatorProps.load( new FileInputStream( allocatorOutputFile ) );

        this.uuid = allocatorProps['uuid']
        log.lifecycle( "Finished allocating for DB instance [${databaseProfile.name}], uuid is [${uuid}]" );

        properties = new HashMap<String, String>();
        properties.putAll( databaseProfile.hibernateProperties );
        properties[DRIVER_PROP] = allocatorProps["db.jdbc_class"]
        properties[URL_PROP] = allocatorProps["db.jdbc_url"] + getURLPostfix(databaseProfile.name)
        properties[USERNAME_PROP] = allocatorProps["db.username"]
        properties[PASSWORD_PROP] = allocatorProps["db.password"]
        properties["uuid"] = allocatorProps["uuid"];

        clean();
    }

    private String getURLPostfix(String dbName) {
        for ( String key: System.properties.keySet() ) {
            if ( key.startsWith(DB_ALLOCATION_URL_POSTFIX) ) {
                String db = key.substring(DB_ALLOCATION_URL_POSTFIX.length() + 1, key.length())
                if ( db.equalsIgnoreCase(dbName) ) {
                    String postfix = System.properties[key];
                    log.debug("found URL postfix[%s] for DB[%s]", postfix, db );
                    return postfix;
                }
            }
        }
        return ""
    }

    void clean() {
        log.lifecycle( "Cleaning DB [${databaseProfile.name}]..." );
        final String allocatorUrl = DB_ALLOCATOR_URL + "?operation=erase&uuid=${uuid}";
        ant.get( src: allocatorUrl, dest: tmpFile.absolutePath );
    }

    @Override
    Map<String, String> getProperties() {
        return properties;
    }

    @Override
    void release() {
        log.lifecycle( "De-allocating DB [${databaseProfile.name}]..." );
        final String allocatorUrl = DB_ALLOCATOR_URL + "?operation=dealloc&uuid=${uuid}";
        ant.get( src: allocatorUrl, dest: tmpFile.absolutePath );
    }


}
