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
package org.hibernate.metamodel.source.annotations.xml.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.DotName;

import org.hibernate.metamodel.source.annotations.xml.mocker.MockHelper;

/**
 * @author Strong Liu
 */
class ExclusiveAnnotationFilter extends AbstractAnnotationFilter {

	public static ExclusiveAnnotationFilter INSTANCE = new ExclusiveAnnotationFilter();
	private DotName[] targetNames;
	private List<ExclusiveGroup> exclusiveGroupList;

	private ExclusiveAnnotationFilter() {
		this.exclusiveGroupList = getExclusiveGroupList();
		Set<DotName> names = new HashSet<DotName>();
		for ( ExclusiveGroup group : exclusiveGroupList ) {
			names.addAll( group.getNames() );
		}
		targetNames = names.toArray( new DotName[names.size()] );
	}

	private List<ExclusiveGroup> getExclusiveGroupList() {
		if ( exclusiveGroupList == null ) {
			exclusiveGroupList = new ArrayList<ExclusiveGroup>();
			ExclusiveGroup group = new ExclusiveGroup();
			group.add( ENTITY );
			group.add( MAPPED_SUPERCLASS );
			group.add( EMBEDDABLE );
			group.scope = Scope.TYPE;
			exclusiveGroupList.add( group );

			group = new ExclusiveGroup();
			group.add( SECONDARY_TABLES );
			group.add( SECONDARY_TABLE );
			group.scope = Scope.TYPE;
			exclusiveGroupList.add( group );

			group = new ExclusiveGroup();
			group.add( PRIMARY_KEY_JOIN_COLUMNS );
			group.add( PRIMARY_KEY_JOIN_COLUMN );
			group.scope = Scope.ATTRIBUTE;
			exclusiveGroupList.add( group );

			group = new ExclusiveGroup();
			group.add( SQL_RESULT_SET_MAPPING );
			group.add( SQL_RESULT_SET_MAPPINGS );
			group.scope = Scope.TYPE;
			exclusiveGroupList.add( group );

			group = new ExclusiveGroup();
			group.add( NAMED_NATIVE_QUERY );
			group.add( NAMED_NATIVE_QUERIES );
			group.scope = Scope.TYPE;
			exclusiveGroupList.add( group );

			group = new ExclusiveGroup();
			group.add( NAMED_QUERY );
			group.add( NAMED_QUERIES );
			group.scope = Scope.TYPE;
			exclusiveGroupList.add( group );

			group = new ExclusiveGroup();
			group.add( ATTRIBUTE_OVERRIDES );
			group.add( ATTRIBUTE_OVERRIDE );
			group.scope = Scope.ATTRIBUTE;
			exclusiveGroupList.add( group );

			group = new ExclusiveGroup();
			group.add( ASSOCIATION_OVERRIDE );
			group.add( ASSOCIATION_OVERRIDES );
			group.scope = Scope.ATTRIBUTE;
			exclusiveGroupList.add( group );

			group = new ExclusiveGroup();
			group.add( MAP_KEY_JOIN_COLUMN );
			group.add( MAP_KEY_JOIN_COLUMNS );
			group.scope = Scope.ATTRIBUTE;
			exclusiveGroupList.add( group );

		}
		return exclusiveGroupList;
	}

	@Override
	protected void overrideIndexedAnnotationMap(DotName annName, AnnotationInstance annotationInstance, Map<DotName, List<AnnotationInstance>> map) {
		ExclusiveGroup group = getExclusiveGroup( annName );
		if ( group == null ) {
			return;
		}
		AnnotationTarget target = annotationInstance.target();
		for ( DotName entityAnnName : group ) {
			if ( !map.containsKey( entityAnnName ) ) {
				continue;
			}
			switch ( group.scope ) {
				case TYPE:
					map.put( entityAnnName, Collections.<AnnotationInstance>emptyList() );
					break;
				case ATTRIBUTE:
					List<AnnotationInstance> indexedAnnotationInstanceList = map.get( entityAnnName );
					Iterator<AnnotationInstance> iter = indexedAnnotationInstanceList.iterator();
					while ( iter.hasNext() ) {
						AnnotationInstance ann = iter.next();
						if ( MockHelper.targetEquals( target, ann.target() ) ) {
							iter.remove();
						}
					}
					break;
			}
		}
	}

	@Override
	protected DotName[] targetAnnotation() {
		return targetNames;
	}

	private ExclusiveGroup getExclusiveGroup(DotName annName) {
		for ( ExclusiveGroup group : exclusiveGroupList ) {
			if ( group.contains( annName ) ) {
				return group;
			}
		}
		return null;
	}

	enum Scope {TYPE, ATTRIBUTE}

	private class ExclusiveGroup implements Iterable<DotName> {
		public Set<DotName> getNames() {
			return names;
		}

		private Set<DotName> names = new HashSet<DotName>();
		Scope scope = Scope.ATTRIBUTE;

		@Override
		public Iterator iterator() {
			return names.iterator();
		}

		boolean contains(DotName name) {
			return names.contains( name );
		}

		void add(DotName name) {
			names.add( name );
		}
	}
}
