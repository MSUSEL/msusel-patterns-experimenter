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
package org.hibernate.cfg.annotations;
import java.util.Collections;
import java.util.Map;

import org.hibernate.AnnotationException;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.common.reflection.XClass;
import org.hibernate.annotations.common.reflection.XProperty;
import org.hibernate.cfg.BinderHelper;
import org.hibernate.cfg.Ejb3Column;
import org.hibernate.cfg.Ejb3JoinColumn;
import org.hibernate.cfg.Mappings;
import org.hibernate.cfg.PropertyData;
import org.hibernate.cfg.PropertyInferredData;
import org.hibernate.cfg.WrappedInferredData;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.IdentifierCollection;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.mapping.Table;

/**
 * @author Emmanuel Bernard
 */
public class IdBagBinder extends BagBinder {
	protected Collection createCollection(PersistentClass persistentClass) {
		return new org.hibernate.mapping.IdentifierBag( getMappings(), persistentClass );
	}

	@Override
	protected boolean bindStarToManySecondPass(
			Map persistentClasses,
			XClass collType,
			Ejb3JoinColumn[] fkJoinColumns,
			Ejb3JoinColumn[] keyColumns,
			Ejb3JoinColumn[] inverseColumns,
			Ejb3Column[] elementColumns,
			boolean isEmbedded,
			XProperty property,
			boolean unique,
			TableBinder associationTableBinder,
			boolean ignoreNotFound,
			Mappings mappings) {
		boolean result = super.bindStarToManySecondPass(
				persistentClasses, collType, fkJoinColumns, keyColumns, inverseColumns, elementColumns, isEmbedded,
				property, unique, associationTableBinder, ignoreNotFound, mappings
		);
		CollectionId collectionIdAnn = property.getAnnotation( CollectionId.class );
		if ( collectionIdAnn != null ) {
			SimpleValueBinder simpleValue = new SimpleValueBinder();

			PropertyData propertyData = new WrappedInferredData(
					new PropertyInferredData(
							null,
							property,
							null, //default access should not be useful
							mappings.getReflectionManager()
					),
					"id"
			);
			Ejb3Column[] idColumns = Ejb3Column.buildColumnFromAnnotation(
					collectionIdAnn.columns(),
					null,
					Nullability.FORCED_NOT_NULL,
					propertyHolder,
					propertyData,
					Collections.EMPTY_MAP,
					mappings
			);
			//we need to make sure all id columns must be not-null.
			for(Ejb3Column idColumn:idColumns){
				idColumn.setNullable(false);
			}
			Table table = collection.getCollectionTable();
			simpleValue.setTable( table );
			simpleValue.setColumns( idColumns );
			Type typeAnn = collectionIdAnn.type();
			if ( typeAnn != null && !BinderHelper.isEmptyAnnotationValue( typeAnn.type() ) ) {
				simpleValue.setExplicitType( typeAnn );
			}
			else {
				throw new AnnotationException( "@CollectionId is missing type: "
						+ StringHelper.qualify( propertyHolder.getPath(), propertyName ) );
			}
			simpleValue.setMappings( mappings );
			SimpleValue id = simpleValue.make();
			( (IdentifierCollection) collection ).setIdentifier( id );
			String generator = collectionIdAnn.generator();
			String generatorType;
			if ( "identity".equals( generator ) || "assigned".equals( generator )
					|| "sequence".equals( generator ) || "native".equals( generator ) ) {
				generatorType = generator;
				generator = "";
			}
			else {
				generatorType = null;
			}
			BinderHelper.makeIdGenerator( id, generatorType, generator, mappings, localGenerators );
		}
		return result;
	}
}
