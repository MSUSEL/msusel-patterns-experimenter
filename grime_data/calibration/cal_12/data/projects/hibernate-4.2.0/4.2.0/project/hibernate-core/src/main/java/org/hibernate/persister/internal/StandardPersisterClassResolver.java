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
package org.hibernate.persister.internal;

import java.util.Iterator;

import org.hibernate.mapping.Collection;
import org.hibernate.mapping.JoinedSubclass;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.RootClass;
import org.hibernate.mapping.SingleTableSubclass;
import org.hibernate.mapping.UnionSubclass;
import org.hibernate.metamodel.binding.CollectionElementNature;
import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.metamodel.binding.PluralAttributeBinding;
import org.hibernate.persister.collection.BasicCollectionPersister;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.collection.OneToManyPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.JoinedSubclassEntityPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.hibernate.persister.entity.UnionSubclassEntityPersister;
import org.hibernate.persister.spi.PersisterClassResolver;
import org.hibernate.persister.spi.UnknownPersisterException;

/**
 * @author Steve Ebersole
 */
public class StandardPersisterClassResolver implements PersisterClassResolver {

	public Class<? extends EntityPersister> getEntityPersisterClass(EntityBinding metadata) {
		if ( metadata.isRoot() ) {
            Iterator<EntityBinding> subEntityBindingIterator = metadata.getDirectSubEntityBindings().iterator();
            if ( subEntityBindingIterator.hasNext() ) {
                //If the class has children, we need to find of which kind
                metadata = subEntityBindingIterator.next();
            }
            else {
			    return singleTableEntityPersister();
            }
		}
		switch ( metadata.getHierarchyDetails().getInheritanceType() ) {
			case JOINED: {
				return joinedSubclassEntityPersister();
			}
			case SINGLE_TABLE: {
				return singleTableEntityPersister();
			}
			case TABLE_PER_CLASS: {
				return unionSubclassEntityPersister();
			}
			default: {
				throw new UnknownPersisterException(
						"Could not determine persister implementation for entity [" + metadata.getEntity().getName() + "]"
				);
			}

		}
	}

	@Override
	public Class<? extends EntityPersister> getEntityPersisterClass(PersistentClass metadata) {
		// todo : make sure this is based on an attribute kept on the metamodel in the new code, not the concrete PersistentClass impl found!
		if ( RootClass.class.isInstance( metadata ) ) {
            if ( metadata.hasSubclasses() ) {
                //If the class has children, we need to find of which kind
                metadata = (PersistentClass) metadata.getDirectSubclasses().next();
            }
            else {
			    return singleTableEntityPersister();
            }
		}
		if ( JoinedSubclass.class.isInstance( metadata ) ) {
			return joinedSubclassEntityPersister();
		}
		else if ( UnionSubclass.class.isInstance( metadata ) ) {
			return unionSubclassEntityPersister();
		}
        else if ( SingleTableSubclass.class.isInstance( metadata ) ) {
			return singleTableEntityPersister();
		}
		else {
			throw new UnknownPersisterException(
					"Could not determine persister implementation for entity [" + metadata.getEntityName() + "]"
			);
		}
	}

    public Class<? extends EntityPersister> singleTableEntityPersister() {
		return SingleTableEntityPersister.class;
	}

	public Class<? extends EntityPersister> joinedSubclassEntityPersister() {
		return JoinedSubclassEntityPersister.class;
	}

	public Class<? extends EntityPersister> unionSubclassEntityPersister() {
		return UnionSubclassEntityPersister.class;
	}

	@Override
	public Class<? extends CollectionPersister> getCollectionPersisterClass(Collection metadata) {
		return metadata.isOneToMany() ? oneToManyPersister() : basicCollectionPersister();
	}

	@Override
	public Class<? extends CollectionPersister> getCollectionPersisterClass(PluralAttributeBinding metadata) {
		return metadata.getCollectionElement().getCollectionElementNature() == CollectionElementNature.ONE_TO_MANY
				? oneToManyPersister()
				: basicCollectionPersister();
	}

	private Class<OneToManyPersister> oneToManyPersister() {
		return OneToManyPersister.class;
	}

	private Class<BasicCollectionPersister> basicCollectionPersister() {
		return BasicCollectionPersister.class;
	}
}
