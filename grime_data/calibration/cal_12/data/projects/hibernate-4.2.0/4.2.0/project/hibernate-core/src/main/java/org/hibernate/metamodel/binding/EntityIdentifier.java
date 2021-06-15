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
package org.hibernate.metamodel.binding;

import java.util.Properties;

import org.hibernate.AssertionFailure;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.factory.IdentifierGeneratorFactory;

/**
 * Binds the entity identifier.
 *
 * @author Steve Ebersole
 * @author Hardy Ferentschik
 */
public class EntityIdentifier {
	private final EntityBinding entityBinding;
	private BasicAttributeBinding attributeBinding;
	private IdentifierGenerator identifierGenerator;
	private IdGenerator idGenerator;
	private boolean isIdentifierMapper = false;
	// todo : mappers, etc

	/**
	 * Create an identifier
	 *
	 * @param entityBinding the entity binding for which this instance is the id
	 */
	public EntityIdentifier(EntityBinding entityBinding) {
		this.entityBinding = entityBinding;
	}

	public BasicAttributeBinding getValueBinding() {
		return attributeBinding;
	}

	public void setValueBinding(BasicAttributeBinding attributeBinding) {
		if ( this.attributeBinding != null ) {
			throw new AssertionFailure(
					String.format(
							"Identifier value binding already existed for %s",
							entityBinding.getEntity().getName()
					)
			);
		}
		this.attributeBinding = attributeBinding;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public boolean isEmbedded() {
		return attributeBinding.getSimpleValueSpan() > 1;
	}

	public boolean isIdentifierMapper() {
		return isIdentifierMapper;
	}

	// todo do we really need this createIdentifierGenerator and how do we make sure the getter is not called too early
	// maybe some sort of visitor pattern here!? (HF)
	public IdentifierGenerator createIdentifierGenerator(IdentifierGeneratorFactory factory, Properties properties) {
		if ( idGenerator != null ) {
			identifierGenerator = attributeBinding.createIdentifierGenerator( idGenerator, factory, properties );
		}
		return identifierGenerator;
	}

	public IdentifierGenerator getIdentifierGenerator() {
		return identifierGenerator;
	}
}
