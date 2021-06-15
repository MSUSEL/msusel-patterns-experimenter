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
package org.hibernate.metamodel.source.annotations.attribute;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;

import org.hibernate.AssertionFailure;
import org.hibernate.metamodel.source.annotations.JPADotNames;

/**
 * Container for the properties defined by {@link javax.persistence.Column}.
 *
 * @author Hardy Ferentschik
 */
public class ColumnValues {
	private String name = "";
	private boolean unique = false;
	private boolean nullable = true;
	private boolean insertable = true;
	private boolean updatable = true;
	private String columnDefinition = "";
	private String table = null;
	private int length = 255;
	private int precision = 0;
	private int scale = 0;

	ColumnValues() {
		this( null );
	}

	public ColumnValues(AnnotationInstance columnAnnotation) {
		if ( columnAnnotation != null && !JPADotNames.COLUMN.equals( columnAnnotation.name() ) ) {
			throw new AssertionFailure( "A @Column annotation needs to be passed to the constructor" );
		}
		applyColumnValues( columnAnnotation );
	}

	private void applyColumnValues(AnnotationInstance columnAnnotation) {
		// if the column annotation is null we don't have to do anything. Everything is already defaulted.
		if ( columnAnnotation == null ) {
			return;
		}

		AnnotationValue nameValue = columnAnnotation.value( "name" );
		if ( nameValue != null ) {
			this.name = nameValue.asString();
		}

		AnnotationValue uniqueValue = columnAnnotation.value( "unique" );
		if ( uniqueValue != null ) {
			this.unique = nameValue.asBoolean();
		}

		AnnotationValue nullableValue = columnAnnotation.value( "nullable" );
		if ( nullableValue != null ) {
			this.nullable = nullableValue.asBoolean();
		}

		AnnotationValue insertableValue = columnAnnotation.value( "insertable" );
		if ( insertableValue != null ) {
			this.insertable = insertableValue.asBoolean();
		}

		AnnotationValue updatableValue = columnAnnotation.value( "updatable" );
		if ( updatableValue != null ) {
			this.updatable = updatableValue.asBoolean();
		}

		AnnotationValue columnDefinition = columnAnnotation.value( "columnDefinition" );
		if ( columnDefinition != null ) {
			this.columnDefinition = columnDefinition.asString();
		}

		AnnotationValue tableValue = columnAnnotation.value( "table" );
		if ( tableValue != null ) {
			this.table = tableValue.asString();
		}

		AnnotationValue lengthValue = columnAnnotation.value( "length" );
		if ( lengthValue != null ) {
			this.length = lengthValue.asInt();
		}

		AnnotationValue precisionValue = columnAnnotation.value( "precision" );
		if ( precisionValue != null ) {
			this.precision = precisionValue.asInt();
		}

		AnnotationValue scaleValue = columnAnnotation.value( "scale" );
		if ( scaleValue != null ) {
			this.scale = scaleValue.asInt();
		}
	}

	public final String getName() {
		return name;
	}

	public final boolean isUnique() {
		return unique;
	}

	public final boolean isNullable() {
		return nullable;
	}

	public final boolean isInsertable() {
		return insertable;
	}

	public final boolean isUpdatable() {
		return updatable;
	}

	public final String getColumnDefinition() {
		return columnDefinition;
	}

	public final String getTable() {
		return table;
	}

	public final int getLength() {
		return length;
	}

	public final int getPrecision() {
		return precision;
	}

	public final int getScale() {
		return scale;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	public void setColumnDefinition(String columnDefinition) {
		this.columnDefinition = columnDefinition;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append( "ColumnValues" );
		sb.append( "{name='" ).append( name ).append( '\'' );
		sb.append( ", unique=" ).append( unique );
		sb.append( ", nullable=" ).append( nullable );
		sb.append( ", insertable=" ).append( insertable );
		sb.append( ", updatable=" ).append( updatable );
		sb.append( ", columnDefinition='" ).append( columnDefinition ).append( '\'' );
		sb.append( ", table='" ).append( table ).append( '\'' );
		sb.append( ", length=" ).append( length );
		sb.append( ", precision=" ).append( precision );
		sb.append( ", scale=" ).append( scale );
		sb.append( '}' );
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		ColumnValues that = (ColumnValues) o;

		if ( insertable != that.insertable ) {
			return false;
		}
		if ( length != that.length ) {
			return false;
		}
		if ( nullable != that.nullable ) {
			return false;
		}
		if ( precision != that.precision ) {
			return false;
		}
		if ( scale != that.scale ) {
			return false;
		}
		if ( unique != that.unique ) {
			return false;
		}
		if ( updatable != that.updatable ) {
			return false;
		}
		if ( columnDefinition != null ? !columnDefinition.equals( that.columnDefinition ) : that.columnDefinition != null ) {
			return false;
		}
		if ( name != null ? !name.equals( that.name ) : that.name != null ) {
			return false;
		}
		if ( table != null ? !table.equals( that.table ) : that.table != null ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + ( unique ? 1 : 0 );
		result = 31 * result + ( nullable ? 1 : 0 );
		result = 31 * result + ( insertable ? 1 : 0 );
		result = 31 * result + ( updatable ? 1 : 0 );
		result = 31 * result + ( columnDefinition != null ? columnDefinition.hashCode() : 0 );
		result = 31 * result + ( table != null ? table.hashCode() : 0 );
		result = 31 * result + length;
		result = 31 * result + precision;
		result = 31 * result + scale;
		return result;
	}
}


