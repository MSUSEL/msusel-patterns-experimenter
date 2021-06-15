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
package org.hibernate.metamodel.source.annotations.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.internal.util.StringHelper;
import org.hibernate.internal.util.ValueHolder;
import org.hibernate.mapping.PropertyGeneration;
import org.hibernate.metamodel.source.LocalBindingContext;
import org.hibernate.metamodel.source.annotations.attribute.AssociationAttribute;
import org.hibernate.metamodel.source.annotations.attribute.AttributeOverride;
import org.hibernate.metamodel.source.annotations.attribute.BasicAttribute;
import org.hibernate.metamodel.source.annotations.attribute.SingularAttributeSourceImpl;
import org.hibernate.metamodel.source.annotations.attribute.ToOneAttributeSourceImpl;
import org.hibernate.metamodel.source.binder.AttributeSource;
import org.hibernate.metamodel.source.binder.ComponentAttributeSource;
import org.hibernate.metamodel.source.binder.ExplicitHibernateTypeSource;
import org.hibernate.metamodel.source.binder.MetaAttributeSource;
import org.hibernate.metamodel.source.binder.RelationalValueSource;
import org.hibernate.metamodel.source.binder.SingularAttributeNature;

/**
 * Annotation backed implementation of {@code ComponentAttributeSource}.
 *
 * @author Steve Ebersole
 * @author Hardy Ferentschik
 */
public class ComponentAttributeSourceImpl implements ComponentAttributeSource {
	private static final String PATH_SEPERATOR = ".";
	private final EmbeddableClass embeddableClass;
	private final ValueHolder<Class<?>> classReference;
	private final Map<String, AttributeOverride> attributeOverrides;
	private final String path;

	public ComponentAttributeSourceImpl(EmbeddableClass embeddableClass, String parentPath, Map<String, AttributeOverride> attributeOverrides) {
		this.embeddableClass = embeddableClass;
		this.classReference = new ValueHolder<Class<?>>( embeddableClass.getConfiguredClass() );
		this.attributeOverrides = attributeOverrides;
		if ( StringHelper.isEmpty( parentPath ) ) {
			path = embeddableClass.getEmbeddedAttributeName();
		}
		else {
			path = parentPath + "." + embeddableClass.getEmbeddedAttributeName();
		}
	}

	@Override
	public boolean isVirtualAttribute() {
		return false;
	}

	@Override
	public SingularAttributeNature getNature() {
		return SingularAttributeNature.COMPONENT;
	}

	@Override
	public boolean isSingular() {
		return true;
	}

	@Override
	public String getClassName() {
		return embeddableClass.getConfiguredClass().getName();
	}

	@Override
	public ValueHolder<Class<?>> getClassReference() {
		return classReference;
	}

	@Override
	public String getName() {
		return embeddableClass.getEmbeddedAttributeName();
	}

	@Override
	public String getExplicitTuplizerClassName() {
		return embeddableClass.getCustomTuplizer();
	}

	@Override
	public String getPropertyAccessorName() {
		return embeddableClass.getClassAccessType().toString().toLowerCase();
	}

	@Override
	public LocalBindingContext getLocalBindingContext() {
		return embeddableClass.getLocalBindingContext();
	}

	@Override
	public Iterable<AttributeSource> attributeSources() {
		List<AttributeSource> attributeList = new ArrayList<AttributeSource>();
		for ( BasicAttribute attribute : embeddableClass.getSimpleAttributes() ) {
			AttributeOverride attributeOverride = null;
			String tmp = getPath() + PATH_SEPERATOR + attribute.getName();
			if ( attributeOverrides.containsKey( tmp ) ) {
				attributeOverride = attributeOverrides.get( tmp );
			}
			attributeList.add( new SingularAttributeSourceImpl( attribute, attributeOverride ) );
		}
		for ( EmbeddableClass embeddable : embeddableClass.getEmbeddedClasses().values() ) {
			attributeList.add(
					new ComponentAttributeSourceImpl(
							embeddable,
							getPath(),
							createAggregatedOverrideMap()
					)
			);
		}
		for ( AssociationAttribute associationAttribute : embeddableClass.getAssociationAttributes() ) {
			attributeList.add( new ToOneAttributeSourceImpl( associationAttribute ) );
		}
		return attributeList;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String getParentReferenceAttributeName() {
		return embeddableClass.getParentReferencingAttributeName();
	}

	@Override
	public Iterable<MetaAttributeSource> metaAttributes() {
		// not relevant for annotations
		return Collections.emptySet();
	}

	@Override
	public List<RelationalValueSource> relationalValueSources() {
		// none, they are defined on the simple sub-attributes
		return null;
	}

	@Override
	public ExplicitHibernateTypeSource getTypeInformation() {
		// probably need to check for @Target in EmbeddableClass (HF)
		return null;
	}

	@Override
	public boolean isInsertable() {
		return true;
	}

	@Override
	public boolean isUpdatable() {
		return true;
	}

	@Override
	public PropertyGeneration getGeneration() {
		return null;
	}

	@Override
	public boolean isLazy() {
		return false;
	}

	@Override
	public boolean isIncludedInOptimisticLocking() {
		return true;
	}

	@Override
	public boolean areValuesIncludedInInsertByDefault() {
		return true;
	}

	@Override
	public boolean areValuesIncludedInUpdateByDefault() {
		return true;
	}

	@Override
	public boolean areValuesNullableByDefault() {
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append( "ComponentAttributeSourceImpl" );
		sb.append( "{embeddableClass=" ).append( embeddableClass.getConfiguredClass().getSimpleName() );
		sb.append( '}' );
		return sb.toString();
	}

	private Map<String, AttributeOverride> createAggregatedOverrideMap() {
		// add all overrides passed down to this instance - they override overrides ;-) which are defined further down
		// the embeddable chain
		Map<String, AttributeOverride> aggregatedOverrideMap = new HashMap<String, AttributeOverride>(
				attributeOverrides
		);

		for ( Map.Entry<String, AttributeOverride> entry : embeddableClass.getAttributeOverrideMap().entrySet() ) {
			String fullPath = getPath() + PATH_SEPERATOR + entry.getKey();
			if ( !aggregatedOverrideMap.containsKey( fullPath ) ) {
				aggregatedOverrideMap.put( fullPath, entry.getValue() );
			}
		}
		return aggregatedOverrideMap;
	}
}
