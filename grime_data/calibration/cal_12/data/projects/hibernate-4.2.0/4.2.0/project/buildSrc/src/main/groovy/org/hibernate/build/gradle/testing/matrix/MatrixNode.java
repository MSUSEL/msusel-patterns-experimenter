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
package org.hibernate.build.gradle.testing.matrix;

import java.io.File;

import org.gradle.api.Project;

import org.hibernate.build.gradle.testing.database.DatabaseProfile;
import org.hibernate.build.gradle.util.Jdk;
import org.hibernate.build.qalab.*;
import org.hibernate.build.qalab.DatabaseAllocation;

/**
 * A testing matrix node combines a database profile and a jdk (eventually) along with managing "db allocation"
 * information.
 *
 * @author Steve Ebersole
 * @author Strong Liu
 */
public class MatrixNode {
	private final DatabaseProfile databaseProfile;
	private final Jdk jdk;
	private final File baseOutputDirectory;

	private final DatabaseAllocation databaseAllocation;

	@SuppressWarnings( {"ResultOfMethodCallIgnored"})
	public MatrixNode(Project project, DatabaseProfile databaseProfile, Jdk jdk) {
		this.databaseProfile = databaseProfile;
		this.jdk = jdk;

		baseOutputDirectory = new File( new File( project.getBuildDir(), "matrix" ), databaseProfile.getName() );
		baseOutputDirectory.mkdirs();

		this.databaseAllocation = DatabaseAllocator.locate( project ).getAllocation( databaseProfile );
	}

    public String getName() {
		return databaseProfile.getName();
	}

	public DatabaseProfile getDatabaseProfile() {
		return databaseProfile;
	}

    public Jdk getJdk() {
		return jdk;
	}

	public File getBaseOutputDirectory() {
		return baseOutputDirectory;
	}

	public DatabaseAllocation getDatabaseAllocation() {
		return databaseAllocation;
	}
}
