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
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.hibernate.build.gradle.testing.database.DatabaseProfile

/**
 * Helper for dealing with the "DB Allocator" service set up in the JBoss/Red Hat QE lab.
 *
 * Use the <code>hibernate-matrix-dballocation</code> setting to control db allocation.  By default,
 * no allocations are performed.  <code>hibernate-matrix-dballocation</code> could be either:<ul>
 *     <li><b>all</b> - allocate all non-ignored databases</li>
 *     <li><b>profile1{,profile2,...}</b> - allocate only the named profiles, provided the name is also one of the supported names</li>
 * </ul>
 *
 * @author mvecera
 * @author Strong Liu
 * @author Steve Ebersole
 */
class DatabaseAllocator {
    private static final Logger log = Logging.getLogger( DatabaseAllocator.class );

	public static final String ALLOCATION_ENABLED = "hibernate-matrix-dballocation";
    public static final String REQUESTEE = "hibernate-matrix-dballocation-requestee";

	public static final String DB_ALLOCATOR_KEY = "dbAllocator";

    public static def SUPPORTED_DB_NAMES = [
            "oracle9i", "oracle10g", "oracle11gR1", "oracle11gR2", "oracle11gR2RAC", "oracle11gR1RAC",
            "postgresql82", "postgresql83", "postgresql84", "postgresql91",
            "mysql50", "mysql51","mysql55",
            "db2-91", "db2-97",
            "mssql2005", "mssql2008R1", "mssql2008R2",
            "sybase155", "sybase157"
    ];

	private Map<String,DatabaseAllocation> databaseAllocationMap = new HashMap<String, DatabaseAllocation>();
	private final Project rootProject;

    DatabaseAllocator(Project rootProject) {
        this.rootProject = rootProject
    }

    public DatabaseAllocation getAllocation(DatabaseProfile profile) {
		DatabaseAllocation databaseAllocation = databaseAllocationMap.get( profile.name );
		if ( databaseAllocation == null ) {
			databaseAllocation = createAllocation( profile );
			databaseAllocationMap.put( profile.name, databaseAllocation );
		}
		return databaseAllocation;
	}

	private DatabaseAllocation createAllocation(DatabaseProfile profile) {
        if ( isAllocationEnabled( profile.name ) ) {
            log.lifecycle( "using Allocator to get database [${profile.name}] connection info" );
			final File outputDirectory = new File( new File( rootProject.getBuildDir(), "matrix" ), profile.getName() )
            return new EnabledDatabaseAllocation( rootProject.getAnt(), profile, outputDirectory );
        }
		return new DisabledDatabaseAllocation( profile );
	}

    private boolean isAllocationEnabled(String name) {
        if ( !SUPPORTED_DB_NAMES.contains(name) ) {
            return false
        };
        String value = System.properties[ALLOCATION_ENABLED]
        return value != null && (value.contains(name) || value.equals("all"));
    }

	public static DatabaseAllocator locate(Project project) {
		if ( ! project.rootProject.hasProperty( DB_ALLOCATOR_KEY ) ) {
			project.rootProject.ext.setProperty( DB_ALLOCATOR_KEY, new DatabaseAllocator( project.rootProject ) );
		}
		return (DatabaseAllocator) project.rootProject.properties[ DB_ALLOCATOR_KEY ];
	}
}