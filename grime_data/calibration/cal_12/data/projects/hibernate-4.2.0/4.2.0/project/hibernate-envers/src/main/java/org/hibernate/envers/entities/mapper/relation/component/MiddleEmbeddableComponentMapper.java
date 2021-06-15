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
package org.hibernate.envers.entities.mapper.relation.component;

import java.util.Map;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.entities.EntityInstantiator;
import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.entities.mapper.CompositeMapperBuilder;
import org.hibernate.envers.entities.mapper.MultiPropertyMapper;
import org.hibernate.envers.entities.mapper.PropertyMapper;
import org.hibernate.envers.entities.mapper.relation.ToOneIdMapper;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.tools.query.Parameters;
import org.hibernate.internal.util.ReflectHelper;

/**
 * @author Kristoffer Lundberg (kristoffer at cambio dot se)
 */
public class MiddleEmbeddableComponentMapper implements MiddleComponentMapper, CompositeMapperBuilder {
	private final MultiPropertyMapper delegate;
	private final Class componentClass;

	public MiddleEmbeddableComponentMapper(MultiPropertyMapper delegate, String componentClassName) {
		this.delegate = delegate;
		try {
			componentClass = Thread.currentThread().getContextClassLoader().loadClass( componentClassName );
		}
		catch ( Exception e ) {
			throw new AuditException( e );
		}
	}

	@Override
	public Object mapToObjectFromFullMap(EntityInstantiator entityInstantiator, Map<String, Object> data, Object dataObject, Number revision) {
		try {
			final Object componentInstance = dataObject != null ? dataObject : ReflectHelper.getDefaultConstructor( componentClass ).newInstance();
			delegate.mapToEntityFromMap(
					entityInstantiator.getAuditConfiguration(), componentInstance, data, null,
					entityInstantiator.getAuditReaderImplementor(), revision
			);
			return componentInstance;
		}
		catch ( Exception e ) {
			throw new AuditException( e );
		}
	}

	@Override
	public void mapToMapFromObject(SessionImplementor session, Map<String, Object> idData, Map<String, Object> data, Object obj) {
		delegate.mapToMapFromEntity( session, data, obj, obj );
	}

	@Override
	public void addMiddleEqualToQuery(Parameters parameters, String idPrefix1, String prefix1, String idPrefix2, String prefix2) {
		addMiddleEqualToQuery( delegate, parameters, idPrefix1, prefix1, idPrefix2, prefix2 );
	}

	protected void addMiddleEqualToQuery(CompositeMapperBuilder compositeMapper, Parameters parameters, String idPrefix1, String prefix1, String idPrefix2, String prefix2) {
		for ( final Map.Entry<PropertyData, PropertyMapper> entry : compositeMapper.getProperties().entrySet() ) {
			final String propertyName = entry.getKey().getName();
			final PropertyMapper nestedMapper = entry.getValue();
			if ( nestedMapper instanceof CompositeMapperBuilder ) {
				addMiddleEqualToQuery( (CompositeMapperBuilder) nestedMapper, parameters, idPrefix1, prefix1, idPrefix2, prefix2 );
			}
			else if ( nestedMapper instanceof ToOneIdMapper ) {
				( (ToOneIdMapper) nestedMapper ).addMiddleEqualToQuery( parameters, idPrefix1, prefix1, idPrefix2, prefix2 );
			}
			else {
				parameters.addWhere( prefix1 + '.' + propertyName, false, "=", prefix2 + '.' + propertyName, false );
			}
		}
	}

	@Override
	public CompositeMapperBuilder addComponent(PropertyData propertyData, String componentClassName) {
		return delegate.addComponent( propertyData, componentClassName );
	}

	@Override
	public void addComposite(PropertyData propertyData, PropertyMapper propertyMapper) {
		delegate.addComposite( propertyData, propertyMapper );
	}

	@Override
	public void add(PropertyData propertyData) {
		delegate.add( propertyData );
	}

	public Map<PropertyData, PropertyMapper> getProperties() {
		return delegate.getProperties();
	}
}
