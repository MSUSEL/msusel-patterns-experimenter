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
import java.util.Collections;

import groovy.lang.Closure;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;

/**
 * Database profile as defined by a {@code matrix.gradle} file
 *
 * @author Steve Ebersole
 * @author Strong Liu
 */
public class MatrixDotGradleProfile extends AbstractDatabaseProfileImpl {
	private static final String MATRIX_NODE_CONVENTION_KEY = "matrixNode";

	private final Configuration jdbcDependencies;

	protected MatrixDotGradleProfile(File matrixDotGradleFile, Project project) {
		super( matrixDotGradleFile.getParentFile(), project );
		jdbcDependencies = prepareConfiguration( getName() );
        final ConventionImpl convention = new ConventionImpl( jdbcDependencies, project );
        project.getConvention().getPlugins().put( MATRIX_NODE_CONVENTION_KEY, convention );
        try {
            project.apply( Collections.singletonMap( "from", matrixDotGradleFile ) );
        }
        finally {
            project.getConvention().getPlugins().remove( MATRIX_NODE_CONVENTION_KEY );
        }
	}

	@Override
	public Configuration getTestingRuntimeConfiguration() {
		return jdbcDependencies;
	}

	private class ConventionImpl {
        private final Configuration jdbcDependencies;
		private final Project project;

        private ConventionImpl(Configuration jdbcDependencies, Project project) {
            this.jdbcDependencies = jdbcDependencies;
			this.project = project;
		}

		@SuppressWarnings( {"UnusedDeclaration"})
        public void jdbcDependency(Object dependencyNotation, Closure closure) {
            project.getDependencies().add( jdbcDependencies.getName(), dependencyNotation, closure );
        }

		@SuppressWarnings( {"UnusedDeclaration"})
        public void jdbcDependency(Object dependencyNotation) {
            project.getDependencies().add( jdbcDependencies.getName(), dependencyNotation );
        }
	}

}
