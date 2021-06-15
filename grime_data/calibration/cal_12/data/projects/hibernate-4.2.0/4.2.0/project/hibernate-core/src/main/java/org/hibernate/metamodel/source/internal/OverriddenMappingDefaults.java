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
package org.hibernate.metamodel.source.internal;

import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.metamodel.source.MappingDefaults;

/**
 * Represents a "nested level" in the mapping defaults stack.
 *
 * @author Steve Ebersole
 */
public class OverriddenMappingDefaults implements MappingDefaults {
	private MappingDefaults overriddenValues;

	private final String packageName;
	private final String schemaName;
	private final String catalogName;
	private final String idColumnName;
	private final String discriminatorColumnName;
	private final String cascade;
	private final String propertyAccess;
	private final Boolean associationLaziness;

	public OverriddenMappingDefaults(
			MappingDefaults overriddenValues,
			String packageName,
			String schemaName,
			String catalogName,
			String idColumnName,
			String discriminatorColumnName,
			String cascade,
			String propertyAccess,
			Boolean associationLaziness) {
		if ( overriddenValues == null ) {
			throw new IllegalArgumentException( "Overridden values cannot be null" );
		}
		this.overriddenValues = overriddenValues;
		this.packageName = packageName;
		this.schemaName = schemaName;
		this.catalogName = catalogName;
		this.idColumnName = idColumnName;
		this.discriminatorColumnName = discriminatorColumnName;
		this.cascade = cascade;
		this.propertyAccess = propertyAccess;
		this.associationLaziness = associationLaziness;
	}

	@Override
	public String getPackageName() {
		return packageName == null ? overriddenValues.getPackageName() : packageName;
	}

	@Override
	public String getSchemaName() {
		return schemaName == null ? overriddenValues.getSchemaName() : schemaName;
	}

	@Override
	public String getCatalogName() {
		return catalogName == null ? overriddenValues.getCatalogName() : catalogName;
	}

	@Override
	public String getIdColumnName() {
		return idColumnName == null ? overriddenValues.getIdColumnName() : idColumnName;
	}

	@Override
	public String getDiscriminatorColumnName() {
		return discriminatorColumnName == null ? overriddenValues.getDiscriminatorColumnName() : discriminatorColumnName;
	}

	@Override
	public String getCascadeStyle() {
		return cascade == null ? overriddenValues.getCascadeStyle() : cascade;
	}

	@Override
	public String getPropertyAccessorName() {
		return propertyAccess == null ? overriddenValues.getPropertyAccessorName() : propertyAccess;
	}

	@Override
	public boolean areAssociationsLazy() {
		return associationLaziness == null ? overriddenValues.areAssociationsLazy() : associationLaziness;
	}

	@Override
	public AccessType getCacheAccessType() {
		return overriddenValues.getCacheAccessType();
	}
}
