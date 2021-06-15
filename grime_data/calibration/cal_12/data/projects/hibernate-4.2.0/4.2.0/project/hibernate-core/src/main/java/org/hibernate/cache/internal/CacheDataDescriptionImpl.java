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
package org.hibernate.cache.internal;

import java.util.Comparator;

import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.metamodel.binding.PluralAttributeBinding;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.VersionType;

/**
 * @author Steve Ebersole
 */
public class CacheDataDescriptionImpl implements CacheDataDescription {
	private final boolean mutable;
	private final boolean versioned;
	private final Comparator versionComparator;

	public CacheDataDescriptionImpl(boolean mutable, boolean versioned, Comparator versionComparator) {
		this.mutable = mutable;
		this.versioned = versioned;
		this.versionComparator = versionComparator;
	}

	public boolean isMutable() {
		return mutable;
	}

	public boolean isVersioned() {
		return versioned;
	}

	public Comparator getVersionComparator() {
		return versionComparator;
	}

	public static CacheDataDescriptionImpl decode(PersistentClass model) {
		return new CacheDataDescriptionImpl(
				model.isMutable(),
				model.isVersioned(),
				model.isVersioned() ? ( ( VersionType ) model.getVersion().getType() ).getComparator() : null
		);
	}

	public static CacheDataDescriptionImpl decode(EntityBinding model) {
		return new CacheDataDescriptionImpl(
				model.isMutable(),
				model.isVersioned(),
				getVersionComparator( model )
		);
	}

	public static CacheDataDescriptionImpl decode(Collection model) {
		return new CacheDataDescriptionImpl(
				model.isMutable(),
				model.getOwner().isVersioned(),
				model.getOwner().isVersioned() ? ( ( VersionType ) model.getOwner().getVersion().getType() ).getComparator() : null
		);
	}

	public static CacheDataDescriptionImpl decode(PluralAttributeBinding model) {
		return new CacheDataDescriptionImpl(
				model.isMutable(),
				model.getContainer().seekEntityBinding().isVersioned(),
				getVersionComparator( model.getContainer().seekEntityBinding() )
		);
	}

    public static CacheDataDescriptionImpl decode(EntityPersister persister) {
        return new CacheDataDescriptionImpl(
                !persister.getEntityMetamodel().hasImmutableNaturalId(),
                false,
                null
        );
    }

	private static Comparator getVersionComparator(EntityBinding model ) {
		Comparator versionComparator = null;
		if ( model.isVersioned() ) {
			versionComparator = (
					( VersionType ) model.getHierarchyDetails()
							.getVersioningAttributeBinding()
							.getHibernateTypeDescriptor()
							.getResolvedTypeMapping()
			).getComparator();
		}
		return versionComparator;
	}
}
