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

import org.hibernate.AssertionFailure;
import org.hibernate.metamodel.relational.ForeignKey;
import org.hibernate.metamodel.relational.TableSpecification;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class CollectionKey {
	private final AbstractPluralAttributeBinding pluralAttributeBinding;

	private ForeignKey foreignKey;
	private boolean inverse;
	private HibernateTypeDescriptor hibernateTypeDescriptor;

// todo : this would be nice to have but we do not always know it, especially in HBM case.
//	private BasicAttributeBinding otherSide;

	public CollectionKey(AbstractPluralAttributeBinding pluralAttributeBinding) {
		this.pluralAttributeBinding = pluralAttributeBinding;
	}

	public AbstractPluralAttributeBinding getPluralAttributeBinding() {
		return pluralAttributeBinding;
	}

	public void prepareForeignKey(String foreignKeyName, String targetTableName) {
		if ( foreignKey != null ) {
			throw new AssertionFailure( "Foreign key already initialized" );
		}
		final TableSpecification collectionTable = pluralAttributeBinding.getCollectionTable();
		if ( collectionTable == null ) {
			throw new AssertionFailure( "Collection table not yet bound" );
		}

		final TableSpecification targetTable = pluralAttributeBinding.getContainer()
				.seekEntityBinding()
				.locateTable( targetTableName );

		// todo : handle implicit fk names...

		foreignKey = collectionTable.createForeignKey( targetTable, foreignKeyName );
	}

	public ForeignKey getForeignKey() {
		return foreignKey;
	}
}
