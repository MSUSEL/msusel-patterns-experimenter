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
package org.hibernate.metamodel.source.hbm;

import org.hibernate.internal.jaxb.mapping.hbm.EntityElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbJoinedSubclassElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbSubclassElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbUnionSubclassElement;
import org.hibernate.metamodel.source.binder.SubclassEntitySource;
import org.hibernate.metamodel.source.binder.TableSource;

/**
 * @author Steve Ebersole
 */
public class SubclassEntitySourceImpl extends AbstractEntitySourceImpl implements SubclassEntitySource {
	protected SubclassEntitySourceImpl(MappingDocument sourceMappingDocument, EntityElement entityElement) {
		super( sourceMappingDocument, entityElement );
	}

	@Override
	public TableSource getPrimaryTable() {
		if ( JaxbJoinedSubclassElement.class.isInstance( entityElement() ) ) {
			return new TableSource() {
				@Override
				public String getExplicitSchemaName() {
					return ( (JaxbJoinedSubclassElement) entityElement() ).getSchema();
				}

				@Override
				public String getExplicitCatalogName() {
					return ( (JaxbJoinedSubclassElement) entityElement() ).getCatalog();
				}

				@Override
				public String getExplicitTableName() {
					return ( (JaxbJoinedSubclassElement) entityElement() ).getTable();
				}

				@Override
				public String getLogicalName() {
					// logical name for the primary table is null
					return null;
				}
			};
		}
		else if ( JaxbUnionSubclassElement.class.isInstance( entityElement() ) ) {
			return new TableSource() {
				@Override
				public String getExplicitSchemaName() {
					return ( (JaxbUnionSubclassElement) entityElement() ).getSchema();
				}

				@Override
				public String getExplicitCatalogName() {
					return ( (JaxbUnionSubclassElement) entityElement() ).getCatalog();
				}

				@Override
				public String getExplicitTableName() {
					return ( (JaxbUnionSubclassElement) entityElement() ).getTable();
				}

				@Override
				public String getLogicalName() {
					// logical name for the primary table is null
					return null;
				}
			};
		}
		return null;
	}

	@Override
	public String getDiscriminatorMatchValue() {
		return JaxbSubclassElement.class.isInstance( entityElement() )
				? ( (JaxbSubclassElement) entityElement() ).getDiscriminatorValue()
				: null;
	}
}
