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

import java.util.ArrayList;
import java.util.List;

import org.hibernate.AssertionFailure;
import org.hibernate.FetchMode;
import org.hibernate.engine.FetchStyle;
import org.hibernate.engine.FetchTiming;
import org.hibernate.engine.spi.CascadeStyle;
import org.hibernate.metamodel.domain.SingularAttribute;

/**
 * TODO : javadoc
 *
 * @author Gail Badner
 * @author Steve Ebersole
 */
public class ManyToOneAttributeBinding extends BasicAttributeBinding implements SingularAssociationAttributeBinding {
	private String referencedEntityName;
	private String referencedAttributeName;
	private AttributeBinding referencedAttributeBinding;

	private boolean isLogicalOneToOne;
	private String foreignKeyName;

	private CascadeStyle cascadeStyle;
	private FetchTiming fetchTiming;
	private FetchStyle fetchStyle;

	ManyToOneAttributeBinding(AttributeBindingContainer container, SingularAttribute attribute) {
		super( container, attribute, false, false );
	}

	@Override
	public boolean isAssociation() {
		return true;
	}

	@Override
	public final boolean isPropertyReference() {
		return referencedAttributeName != null;
	}

	@Override
	public final String getReferencedEntityName() {
		return referencedEntityName;
	}

	@Override
	public void setReferencedEntityName(String referencedEntityName) {
		this.referencedEntityName = referencedEntityName;
	}

	@Override
	public final String getReferencedAttributeName() {
		return referencedAttributeName;
	}

	@Override
	public void setReferencedAttributeName(String referencedEntityAttributeName) {
		this.referencedAttributeName = referencedEntityAttributeName;
	}

	@Override
	public CascadeStyle getCascadeStyle() {
		return cascadeStyle;
	}

	@Override
	public void setCascadeStyles(Iterable<CascadeStyle> cascadeStyles) {
		List<CascadeStyle> cascadeStyleList = new ArrayList<CascadeStyle>();
		for ( CascadeStyle style : cascadeStyles ) {
			if ( style != CascadeStyle.NONE ) {
				cascadeStyleList.add( style );
			}
		}
		if ( cascadeStyleList.isEmpty() ) {
			cascadeStyle = CascadeStyle.NONE;
		}
		else if ( cascadeStyleList.size() == 1 ) {
			cascadeStyle = cascadeStyleList.get( 0 );
		}
		else {
			cascadeStyle = new CascadeStyle.MultipleCascadeStyle(
					cascadeStyleList.toArray( new CascadeStyle[ cascadeStyleList.size() ] )
			);
		}
	}

	@Override
	public FetchTiming getFetchTiming() {
		return fetchTiming;
	}

	@Override
	public void setFetchTiming(FetchTiming fetchTiming) {
		this.fetchTiming = fetchTiming;
	}

	@Override
	public FetchStyle getFetchStyle() {
		return fetchStyle;
	}

	@Override
	public void setFetchStyle(FetchStyle fetchStyle) {
		if ( fetchStyle == FetchStyle.SUBSELECT ) {
			throw new AssertionFailure( "Subselect fetching not yet supported for singular associations" );
		}
		this.fetchStyle = fetchStyle;
	}

	@Override
	public FetchMode getFetchMode() {
		if ( fetchStyle == FetchStyle.JOIN ) {
			return FetchMode.JOIN;
		}
		else if ( fetchStyle == FetchStyle.SELECT ) {
			return FetchMode.SELECT;
		}
		else if ( fetchStyle == FetchStyle.BATCH ) {
			// we need the subsequent select...
			return FetchMode.SELECT;
		}

		throw new AssertionFailure( "Unexpected fetch style : " + fetchStyle.name() );
	}

	@Override
	public final boolean isReferenceResolved() {
		return referencedAttributeBinding != null;
	}

	@Override
	public final void resolveReference(AttributeBinding referencedAttributeBinding) {
		if ( ! EntityBinding.class.isInstance( referencedAttributeBinding.getContainer() ) ) {
			throw new AssertionFailure( "Illegal attempt to resolve many-to-one reference based on non-entity attribute" );
		}
		final EntityBinding entityBinding = (EntityBinding) referencedAttributeBinding.getContainer();
		if ( !referencedEntityName.equals( entityBinding.getEntity().getName() ) ) {
			throw new IllegalStateException(
					"attempt to set EntityBinding with name: [" +
							entityBinding.getEntity().getName() +
							"; entity name should be: " + referencedEntityName
			);
		}
		if ( referencedAttributeName == null ) {
			referencedAttributeName = referencedAttributeBinding.getAttribute().getName();
		}
		else if ( !referencedAttributeName.equals( referencedAttributeBinding.getAttribute().getName() ) ) {
			throw new IllegalStateException(
					"Inconsistent attribute name; expected: " + referencedAttributeName +
							"actual: " + referencedAttributeBinding.getAttribute().getName()
			);
		}
		this.referencedAttributeBinding = referencedAttributeBinding;
//		buildForeignKey();
	}

	@Override
	public AttributeBinding getReferencedAttributeBinding() {
		if ( !isReferenceResolved() ) {
			throw new IllegalStateException( "Referenced AttributeBiding has not been resolved." );
		}
		return referencedAttributeBinding;
	}

	@Override
	public final EntityBinding getReferencedEntityBinding() {
		return (EntityBinding) referencedAttributeBinding.getContainer();
	}

//	private void buildForeignKey() {
//		// TODO: move this stuff to relational model
//		ForeignKey foreignKey = getValue().getTable()
//				.createForeignKey( referencedAttributeBinding.getValue().getTable(), foreignKeyName );
//		Iterator<SimpleValue> referencingValueIterator = getSimpleValues().iterator();
//		Iterator<SimpleValue> targetValueIterator = referencedAttributeBinding.getSimpleValues().iterator();
//		while ( referencingValueIterator.hasNext() ) {
//			if ( !targetValueIterator.hasNext() ) {
//				// TODO: improve this message
//				throw new MappingException(
//						"number of values in many-to-one reference is greater than number of values in target"
//				);
//			}
//			SimpleValue referencingValue = referencingValueIterator.next();
//			SimpleValue targetValue = targetValueIterator.next();
//			if ( Column.class.isInstance( referencingValue ) ) {
//				if ( !Column.class.isInstance( targetValue ) ) {
//					// TODO improve this message
//					throw new MappingException( "referencing value is a column, but target is not a column" );
//				}
//				foreignKey.addColumnMapping( Column.class.cast( referencingValue ), Column.class.cast( targetValue ) );
//			}
//			else if ( Column.class.isInstance( targetValue ) ) {
//				// TODO: improve this message
//				throw new MappingException( "referencing value is not a column, but target is a column." );
//			}
//		}
//		if ( targetValueIterator.hasNext() ) {
//			throw new MappingException( "target value has more simple values than referencing value" );
//		}
//	}
//
//	public void validate() {
//		// can't check this until both the domain and relational states are initialized...
//		if ( getCascadeTypes().contains( CascadeType.DELETE_ORPHAN ) ) {
//			if ( !isLogicalOneToOne ) {
//				throw new MappingException(
//						"many-to-one attribute [" + locateAttribute().getName() + "] does not support orphan delete as it is not unique"
//				);
//			}
//		}
//		//TODO: validate that the entity reference is resolved
//	}
}