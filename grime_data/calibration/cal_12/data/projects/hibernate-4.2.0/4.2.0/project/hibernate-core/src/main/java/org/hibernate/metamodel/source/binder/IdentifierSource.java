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
package org.hibernate.metamodel.source.binder;

import org.hibernate.metamodel.binding.IdGenerator;

/**
 * Contract describing source of identifier information for the entity.
 *
 * @author Steve Ebersole
 */
public interface IdentifierSource {
    /**
     * Obtain the identifier generator source.
     *
     * @return The generator source.
     */
    IdGenerator getIdentifierGeneratorDescriptor();

    public static enum Nature {
		/**
		 * A single, simple identifier.  Equivalent of an {@code <id/>} mapping or a single {@code @Id}
		 * annotation.  Indicates the {@link IdentifierSource} is castable to {@link SimpleIdentifierSource}.
		 */
		SIMPLE,

		/**
		 * What we used to term an "embedded composite identifier", which is not to be confused with the JPA
		 * term embedded.  Specifically a composite id where there is no component class, though there may be an
		 * {@code @IdClass}.
		 */
		COMPOSITE,

		/**
		 * Composite identifier with an actual component class used to aggregate the individual attributes
		 */
		AGGREGATED_COMPOSITE
	}

	/**
	 * Obtain the nature of this identifier source.
	 *
	 * @return The identifier source's nature.
	 */
	public Nature getNature();
}
