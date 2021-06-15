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

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.Index;

import org.hibernate.cfg.NamingStrategy;
import org.hibernate.internal.jaxb.Origin;
import org.hibernate.internal.jaxb.SourceType;
import org.hibernate.internal.util.ValueHolder;
import org.hibernate.metamodel.domain.Type;
import org.hibernate.metamodel.source.LocalBindingContext;
import org.hibernate.metamodel.source.MappingDefaults;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.metamodel.source.annotations.AnnotationBindingContext;
import org.hibernate.service.ServiceRegistry;

/**
 * Annotation version of a local binding context.
 * 
 * @author Steve Ebersole
 */
public class EntityBindingContext implements LocalBindingContext, AnnotationBindingContext {
	private final AnnotationBindingContext contextDelegate;
	private final Origin origin;

	public EntityBindingContext(AnnotationBindingContext contextDelegate, ConfiguredClass source) {
		this.contextDelegate = contextDelegate;
		this.origin = new Origin( SourceType.ANNOTATION, source.getName() );
	}

	@Override
	public Origin getOrigin() {
		return origin;
	}

	@Override
	public ServiceRegistry getServiceRegistry() {
		return contextDelegate.getServiceRegistry();
	}

	@Override
	public NamingStrategy getNamingStrategy() {
		return contextDelegate.getNamingStrategy();
	}

	@Override
	public MappingDefaults getMappingDefaults() {
		return contextDelegate.getMappingDefaults();
	}

	@Override
	public MetadataImplementor getMetadataImplementor() {
		return contextDelegate.getMetadataImplementor();
	}

	@Override
	public <T> Class<T> locateClassByName(String name) {
		return contextDelegate.locateClassByName( name );
	}

	@Override
	public Type makeJavaType(String className) {
		return contextDelegate.makeJavaType( className );
	}

	@Override
	public boolean isGloballyQuotedIdentifiers() {
		return contextDelegate.isGloballyQuotedIdentifiers();
	}

	@Override
	public ValueHolder<Class<?>> makeClassReference(String className) {
		return contextDelegate.makeClassReference( className );
	}

	@Override
	public String qualifyClassName(String name) {
		return contextDelegate.qualifyClassName( name );
	}

	@Override
	public Index getIndex() {
		return contextDelegate.getIndex();
	}

	@Override
	public ClassInfo getClassInfo(String name) {
		return contextDelegate.getClassInfo( name );
	}

	@Override
	public void resolveAllTypes(String className) {
		contextDelegate.resolveAllTypes( className );
	}

	@Override
	public ResolvedType getResolvedType(Class<?> clazz) {
		return contextDelegate.getResolvedType( clazz );
	}

	@Override
	public ResolvedTypeWithMembers resolveMemberTypes(ResolvedType type) {
		return contextDelegate.resolveMemberTypes( type );
	}
}
