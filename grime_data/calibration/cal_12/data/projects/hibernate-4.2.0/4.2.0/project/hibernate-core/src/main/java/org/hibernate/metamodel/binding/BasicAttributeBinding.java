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

import java.util.Properties;

import org.hibernate.MappingException;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.factory.IdentifierGeneratorFactory;
import org.hibernate.mapping.PropertyGeneration;
import org.hibernate.metamodel.domain.SingularAttribute;
import org.hibernate.metamodel.relational.Column;
import org.hibernate.metamodel.relational.Schema;
import org.hibernate.metamodel.relational.SimpleValue;
import org.hibernate.metamodel.source.MetaAttributeContext;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class BasicAttributeBinding
		extends AbstractSingularAttributeBinding
		implements  KeyValueBinding {

	private String unsavedValue;
	private PropertyGeneration generation;
	private boolean includedInOptimisticLocking;

	private boolean forceNonNullable;
	private boolean forceUnique;
	private boolean keyCascadeDeleteEnabled;

	private MetaAttributeContext metaAttributeContext;

	BasicAttributeBinding(
			AttributeBindingContainer container,
			SingularAttribute attribute,
			boolean forceNonNullable,
			boolean forceUnique) {
		super( container, attribute );
		this.forceNonNullable = forceNonNullable;
		this.forceUnique = forceUnique;
	}

	@Override
	public boolean isAssociation() {
		return false;
	}

	@Override
	public String getUnsavedValue() {
		return unsavedValue;
	}

	public void setUnsavedValue(String unsavedValue) {
		this.unsavedValue = unsavedValue;
	}

	@Override
	public PropertyGeneration getGeneration() {
		return generation;
	}

	public void setGeneration(PropertyGeneration generation) {
		this.generation = generation;
	}

	public boolean isIncludedInOptimisticLocking() {
		return includedInOptimisticLocking;
	}

	public void setIncludedInOptimisticLocking(boolean includedInOptimisticLocking) {
		this.includedInOptimisticLocking = includedInOptimisticLocking;
	}

	@Override
	public boolean isKeyCascadeDeleteEnabled() {
		return keyCascadeDeleteEnabled;
	}

	public void setKeyCascadeDeleteEnabled(boolean keyCascadeDeleteEnabled) {
		this.keyCascadeDeleteEnabled = keyCascadeDeleteEnabled;
	}

	public boolean forceNonNullable() {
		return forceNonNullable;
	}

	public boolean forceUnique() {
		return forceUnique;
	}

	public MetaAttributeContext getMetaAttributeContext() {
		return metaAttributeContext;
	}

	public void setMetaAttributeContext(MetaAttributeContext metaAttributeContext) {
		this.metaAttributeContext = metaAttributeContext;
	}

	IdentifierGenerator createIdentifierGenerator(
			IdGenerator idGenerator,
			IdentifierGeneratorFactory identifierGeneratorFactory,
			Properties properties) {
		Properties params = new Properties();
		params.putAll( properties );

		// use the schema/catalog specified by getValue().getTable() - but note that
		// if the schema/catalog were specified as params, they will already be initialized and
		//will override the values set here (they are in idGenerator.getParameters().)
		Schema schema = getValue().getTable().getSchema();
		if ( schema != null ) {
			if ( schema.getName().getSchema() != null ) {
				params.setProperty( PersistentIdentifierGenerator.SCHEMA, schema.getName().getSchema().getName() );
			}
			if ( schema.getName().getCatalog() != null ) {
				params.setProperty( PersistentIdentifierGenerator.CATALOG, schema.getName().getCatalog().getName() );
			}
		}

		// TODO: not sure how this works for collection IDs...
		//pass the entity-name, if not a collection-id
		//if ( rootClass!=null) {
			params.setProperty( IdentifierGenerator.ENTITY_NAME, getContainer().seekEntityBinding().getEntity().getName() );
		//}

		//init the table here instead of earlier, so that we can get a quoted table name
		//TODO: would it be better to simply pass the qualified table name, instead of
		//      splitting it up into schema/catalog/table names
		String tableName = getValue().getTable().getQualifiedName( identifierGeneratorFactory.getDialect() );
		params.setProperty( PersistentIdentifierGenerator.TABLE, tableName );

		//pass the column name (a generated id almost always has a single column)
		if ( getSimpleValueSpan() > 1 ) {
			throw new MappingException(
					"A SimpleAttributeBinding used for an identifier has more than 1 Value: " + getAttribute().getName()
			);
		}
		SimpleValue simpleValue = (SimpleValue) getValue();
		if ( !Column.class.isInstance( simpleValue ) ) {
			throw new MappingException(
					"Cannot create an IdentifierGenerator because the value is not a column: " +
							simpleValue.toLoggableString()
			);
		}
		params.setProperty(
				PersistentIdentifierGenerator.PK,
				( (Column) simpleValue ).getColumnName().encloseInQuotesIfQuoted(
						identifierGeneratorFactory.getDialect()
				)
		);

		// TODO: is this stuff necessary for SimpleValue???
		//if (rootClass!=null) {
		//	StringBuffer tables = new StringBuffer();
		//	Iterator iter = rootClass.getIdentityTables().iterator();
		//	while ( iter.hasNext() ) {
		//		Table table= (Table) iter.next();
		//		tables.append( table.getQuotedName(dialect) );
		//		if ( iter.hasNext() ) tables.append(", ");
		//	}
		//	params.setProperty( PersistentIdentifierGenerator.TABLES, tables.toString() );
		//}
		//else {
		params.setProperty( PersistentIdentifierGenerator.TABLES, tableName );
		//}

		params.putAll( idGenerator.getParameters() );

		return identifierGeneratorFactory.createIdentifierGenerator(
				idGenerator.getStrategy(), getHibernateTypeDescriptor().getResolvedTypeMapping(), params
		);
	}
}
