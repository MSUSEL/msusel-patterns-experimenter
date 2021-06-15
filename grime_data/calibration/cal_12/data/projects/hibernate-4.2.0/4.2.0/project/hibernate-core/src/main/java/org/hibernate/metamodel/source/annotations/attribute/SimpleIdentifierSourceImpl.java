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
package org.hibernate.metamodel.source.annotations.attribute;

import java.util.Map;

import org.hibernate.AssertionFailure;
import org.hibernate.metamodel.binding.IdGenerator;
import org.hibernate.metamodel.source.binder.SimpleIdentifierSource;
import org.hibernate.metamodel.source.binder.SingularAttributeSource;

/**
 * @author Hardy Ferentschik
 */
public class SimpleIdentifierSourceImpl implements SimpleIdentifierSource {
	private final BasicAttribute attribute;
	private final Map<String, AttributeOverride> attributeOverrideMap;

	public SimpleIdentifierSourceImpl(BasicAttribute attribute, Map<String, AttributeOverride> attributeOverrideMap) {
		if ( !attribute.isId() ) {
			throw new AssertionFailure(
					String.format(
							"A non id attribute was passed to SimpleIdentifierSourceImpl: %s",
							attribute.toString()
					)
			);
		}
		this.attribute = attribute;
		this.attributeOverrideMap = attributeOverrideMap;
	}

	@Override
	public Nature getNature() {
		return Nature.SIMPLE;
	}

	@Override
	public SingularAttributeSource getIdentifierAttributeSource() {
		return new SingularAttributeSourceImpl( attribute );
	}

	@Override
	public IdGenerator getIdentifierGeneratorDescriptor() {
		return attribute.getIdGenerator();
	}
}


