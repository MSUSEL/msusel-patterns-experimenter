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

import java.util.HashMap;
import java.util.Map;

import org.hibernate.mapping.PropertyGeneration;
import org.hibernate.metamodel.domain.AttributeContainer;
import org.hibernate.metamodel.domain.Component;
import org.hibernate.metamodel.domain.PluralAttribute;
import org.hibernate.metamodel.domain.PluralAttributeNature;
import org.hibernate.metamodel.domain.SingularAttribute;
import org.hibernate.metamodel.source.MetaAttributeContext;

/**
 * @author Steve Ebersole
 */
public class ComponentAttributeBinding extends AbstractSingularAttributeBinding implements AttributeBindingContainer {
	private final String path;
	private Map<String, AttributeBinding> attributeBindingMap = new HashMap<String, AttributeBinding>();
	private SingularAttribute parentReference;
	private MetaAttributeContext metaAttributeContext;

	public ComponentAttributeBinding(AttributeBindingContainer container, SingularAttribute attribute) {
		super( container, attribute );
		this.path = container.getPathBase() + '.' + attribute.getName();
	}

	@Override
	public EntityBinding seekEntityBinding() {
		return getContainer().seekEntityBinding();
	}

	@Override
	public String getPathBase() {
		return path;
	}

	@Override
	public AttributeContainer getAttributeContainer() {
		return getComponent();
	}

	public Component getComponent() {
		return (Component) getAttribute().getSingularAttributeType();
	}

	@Override
	public boolean isAssociation() {
		return false;
	}

	@Override
	public MetaAttributeContext getMetaAttributeContext() {
		return metaAttributeContext;
	}

	public void setMetaAttributeContext(MetaAttributeContext metaAttributeContext) {
		this.metaAttributeContext = metaAttributeContext;
	}

	@Override
	public AttributeBinding locateAttributeBinding(String name) {
		return attributeBindingMap.get( name );
	}

	@Override
	public Iterable<AttributeBinding> attributeBindings() {
		return attributeBindingMap.values();
	}

	@Override
	protected void checkValueBinding() {
		// do nothing here...
	}

	@Override
	public BasicAttributeBinding makeBasicAttributeBinding(SingularAttribute attribute) {
		final BasicAttributeBinding binding = new BasicAttributeBinding(
				this,
				attribute,
				isNullable(),
				isAlternateUniqueKey() // todo : is this accurate?
		);
		registerAttributeBinding( attribute.getName(), binding );
		return binding;
	}

	protected void registerAttributeBinding(String name, AttributeBinding attributeBinding) {
		// todo : hook this into the EntityBinding notion of "entity referencing attribute bindings"
		attributeBindingMap.put( name, attributeBinding );
	}

	@Override
	public ComponentAttributeBinding makeComponentAttributeBinding(SingularAttribute attribute) {
		final ComponentAttributeBinding binding = new ComponentAttributeBinding( this, attribute );
		registerAttributeBinding( attribute.getName(), binding );
		return binding;
	}

	@Override
	public ManyToOneAttributeBinding makeManyToOneAttributeBinding(SingularAttribute attribute) {
		final ManyToOneAttributeBinding binding = new ManyToOneAttributeBinding( this, attribute );
		registerAttributeBinding( attribute.getName(), binding );
		return binding;
	}

	@Override
	public BagBinding makeBagAttributeBinding(PluralAttribute attribute, CollectionElementNature nature) {
		Helper.checkPluralAttributeNature( attribute, PluralAttributeNature.BAG );
		final BagBinding binding = new BagBinding( this, attribute, nature );
		registerAttributeBinding( attribute.getName(), binding );
		return binding;
	}

	@Override
	public SetBinding makeSetAttributeBinding(PluralAttribute attribute, CollectionElementNature nature) {
		Helper.checkPluralAttributeNature( attribute, PluralAttributeNature.SET );
		final SetBinding binding = new SetBinding( this, attribute, nature );
		registerAttributeBinding( attribute.getName(), binding );
		return binding;
	}

	@Override
	public Class<?> getClassReference() {
		return getComponent().getClassReference();
	}

	public SingularAttribute getParentReference() {
		return parentReference;
	}

	public void setParentReference(SingularAttribute parentReference) {
		this.parentReference = parentReference;
	}

	@Override
	public PropertyGeneration getGeneration() {
		// todo : not sure the correct thing to return here since it essentially relies on the simple sub-attributes.
		return null;
	}
}
