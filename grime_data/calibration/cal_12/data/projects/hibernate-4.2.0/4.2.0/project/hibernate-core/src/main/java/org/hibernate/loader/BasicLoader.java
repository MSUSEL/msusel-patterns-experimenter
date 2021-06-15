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
package org.hibernate.loader;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.entity.Loadable;
import org.hibernate.type.BagType;

/**
 * Uses the default mapping from property to result set column 
 * alias defined by the entities' persisters. Used when Hibernate
 * is generating result set column aliases.
 * 
 * @author Gavin King
 */
public abstract class BasicLoader extends Loader {

	protected static final String[] NO_SUFFIX = {""};

	private EntityAliases[] descriptors;
	private CollectionAliases[] collectionDescriptors;

	public BasicLoader(SessionFactoryImplementor factory) {
		super(factory);
	}

	protected final EntityAliases[] getEntityAliases() {
		return descriptors;
	}

	protected final CollectionAliases[] getCollectionAliases() {
		return collectionDescriptors;
	}

	protected abstract String[] getSuffixes();
	protected abstract String[] getCollectionSuffixes();

	protected void postInstantiate() {
		Loadable[] persisters = getEntityPersisters();
		String[] suffixes = getSuffixes();
		descriptors = new EntityAliases[persisters.length];
		for ( int i=0; i<descriptors.length; i++ ) {
			descriptors[i] = new DefaultEntityAliases( persisters[i], suffixes[i] );
		}

		CollectionPersister[] collectionPersisters = getCollectionPersisters();
		List bagRoles = null;
		if ( collectionPersisters != null ) {
			String[] collectionSuffixes = getCollectionSuffixes();
			collectionDescriptors = new CollectionAliases[collectionPersisters.length];
			for ( int i = 0; i < collectionPersisters.length; i++ ) {
				if ( isBag( collectionPersisters[i] ) ) {
					if ( bagRoles == null ) {
						bagRoles = new ArrayList();
					}
					bagRoles.add( collectionPersisters[i].getRole() );
				}
				collectionDescriptors[i] = new GeneratedCollectionAliases(
						collectionPersisters[i],
						collectionSuffixes[i]
					);
			}
		}
		else {
			collectionDescriptors = null;
		}
		if ( bagRoles != null && bagRoles.size() > 1 ) {
			throw new MultipleBagFetchException( bagRoles );
		}
	}

	private boolean isBag(CollectionPersister collectionPersister) {
		return collectionPersister.getCollectionType().getClass().isAssignableFrom( BagType.class );
	}

	/**
	 * Utility method that generates 0_, 1_ suffixes. Subclasses don't
	 * necessarily need to use this algorithm, but it is intended that
	 * they will in most cases.
	 *
	 * @param length The number of suffixes to generate
	 *
	 * @return The array of generated suffixes (with length=length).
	 */
	public static String[] generateSuffixes(int length) {
		return generateSuffixes( 0, length );
	}

	public static String[] generateSuffixes(int seed, int length) {
		if ( length == 0 ) return NO_SUFFIX;

		String[] suffixes = new String[length];
		for ( int i = 0; i < length; i++ ) {
			suffixes[i] = Integer.toString( i + seed ) + "_";
		}
		return suffixes;
	}

}
