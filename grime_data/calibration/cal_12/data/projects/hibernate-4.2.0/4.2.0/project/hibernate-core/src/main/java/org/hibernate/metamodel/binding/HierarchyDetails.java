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

import org.hibernate.EntityMode;
import org.hibernate.engine.OptimisticLockStyle;

/**
 * @author Steve Ebersole
 */
public class HierarchyDetails {
	private final EntityBinding rootEntityBinding;
	private final InheritanceType inheritanceType;
	private final EntityMode entityMode;

	private final EntityIdentifier entityIdentifier;

	private EntityDiscriminator entityDiscriminator;

	private OptimisticLockStyle optimisticLockStyle;
	private BasicAttributeBinding versioningAttributeBinding;

	private Caching caching;

	private boolean explicitPolymorphism;

	public HierarchyDetails(EntityBinding rootEntityBinding, InheritanceType inheritanceType, EntityMode entityMode) {
		this.rootEntityBinding = rootEntityBinding;
		this.inheritanceType = inheritanceType;
		this.entityMode = entityMode;
		this.entityIdentifier = new EntityIdentifier( rootEntityBinding );
	}

	public EntityBinding getRootEntityBinding() {
		return rootEntityBinding;
	}

	public InheritanceType getInheritanceType() {
		return inheritanceType;
	}

	public EntityMode getEntityMode() {
		return entityMode;
	}

	public EntityIdentifier getEntityIdentifier() {
		return entityIdentifier;
	}

	public EntityDiscriminator getEntityDiscriminator() {
		return entityDiscriminator;
	}

	public OptimisticLockStyle getOptimisticLockStyle() {
		return optimisticLockStyle;
	}

	public void setOptimisticLockStyle(OptimisticLockStyle optimisticLockStyle) {
		this.optimisticLockStyle = optimisticLockStyle;
	}

	public void setEntityDiscriminator(EntityDiscriminator entityDiscriminator) {
		this.entityDiscriminator = entityDiscriminator;
	}

	public BasicAttributeBinding getVersioningAttributeBinding() {
		return versioningAttributeBinding;
	}

	public void setVersioningAttributeBinding(BasicAttributeBinding versioningAttributeBinding) {
		this.versioningAttributeBinding = versioningAttributeBinding;
	}

	public Caching getCaching() {
		return caching;
	}

	public void setCaching(Caching caching) {
		this.caching = caching;
	}

	public boolean isExplicitPolymorphism() {
		return explicitPolymorphism;
	}

	public void setExplicitPolymorphism(boolean explicitPolymorphism) {
		this.explicitPolymorphism = explicitPolymorphism;
	}
}
