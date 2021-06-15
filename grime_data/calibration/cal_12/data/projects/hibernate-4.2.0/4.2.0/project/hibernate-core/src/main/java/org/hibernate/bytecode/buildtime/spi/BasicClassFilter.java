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
package org.hibernate.bytecode.buildtime.spi;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * BasicClassFilter provides class filtering based on a series of packages to
 * be included and/or a series of explicit class names to be included.  If
 * neither is specified, then no restrictions are applied.
 *
 * @author Steve Ebersole
 */
public class BasicClassFilter implements ClassFilter {
	private final String[] includedPackages;
	private final Set<String> includedClassNames = new HashSet<String>();
	private final boolean isAllEmpty;

	public BasicClassFilter() {
		this( null, null );
	}

	public BasicClassFilter(String[] includedPackages, String[] includedClassNames) {
		this.includedPackages = includedPackages;
		if ( includedClassNames != null ) {
			this.includedClassNames.addAll( Arrays.asList( includedClassNames ) );
		}

		isAllEmpty = ( this.includedPackages == null || this.includedPackages.length == 0 )
		             && ( this.includedClassNames.isEmpty() );
	}

	public boolean shouldInstrumentClass(String className) {
		return isAllEmpty ||
				includedClassNames.contains( className ) ||
				isInIncludedPackage( className );
	}

	private boolean isInIncludedPackage(String className) {
		if ( includedPackages != null ) {
			for ( String includedPackage : includedPackages ) {
				if ( className.startsWith( includedPackage ) ) {
					return true;
				}
			}
		}
		return false;
	}
}
