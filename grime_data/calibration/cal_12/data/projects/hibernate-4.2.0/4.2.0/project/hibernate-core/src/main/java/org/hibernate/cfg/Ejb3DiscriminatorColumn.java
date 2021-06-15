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
package org.hibernate.cfg;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;

import org.hibernate.AssertionFailure;
import org.hibernate.annotations.DiscriminatorFormula;

/**
 * Discriminator column
 *
 * @author Emmanuel Bernard
 */
public class Ejb3DiscriminatorColumn extends Ejb3Column {
	private static final String DEFAULT_DISCRIMINATOR_COLUMN_NAME = "DTYPE";
	private static final String DEFAULT_DISCRIMINATOR_TYPE = "string";
	private static final int DEFAULT_DISCRIMINATOR_LENGTH = 31;

	private String discriminatorTypeName;

	public Ejb3DiscriminatorColumn() {
		//discriminator default value
		super();
		setLogicalColumnName( DEFAULT_DISCRIMINATOR_COLUMN_NAME );
		setNullable( false );
		setDiscriminatorTypeName( DEFAULT_DISCRIMINATOR_TYPE );
		setLength( DEFAULT_DISCRIMINATOR_LENGTH );
	}

	public String getDiscriminatorTypeName() {
		return discriminatorTypeName;
	}

	public void setDiscriminatorTypeName(String discriminatorTypeName) {
		this.discriminatorTypeName = discriminatorTypeName;
	}

	public static Ejb3DiscriminatorColumn buildDiscriminatorColumn(
			DiscriminatorType type, DiscriminatorColumn discAnn,
			DiscriminatorFormula discFormulaAnn,
			Mappings mappings) {
		Ejb3DiscriminatorColumn discriminatorColumn = new Ejb3DiscriminatorColumn();
		discriminatorColumn.setMappings( mappings );
		discriminatorColumn.setImplicit( true );
		if ( discFormulaAnn != null ) {
			discriminatorColumn.setImplicit( false );
			discriminatorColumn.setFormula( discFormulaAnn.value() );
		}
		else if ( discAnn != null ) {
			discriminatorColumn.setImplicit( false );
			if ( !BinderHelper.isEmptyAnnotationValue( discAnn.columnDefinition() ) ) {
				discriminatorColumn.setSqlType(
						discAnn.columnDefinition()
				);
			}
			if ( !BinderHelper.isEmptyAnnotationValue( discAnn.name() ) ) {
				discriminatorColumn.setLogicalColumnName( discAnn.name() );
			}
			discriminatorColumn.setNullable( false );
		}
		if ( DiscriminatorType.CHAR.equals( type ) ) {
			discriminatorColumn.setDiscriminatorTypeName( "character" );
			discriminatorColumn.setImplicit( false );
		}
		else if ( DiscriminatorType.INTEGER.equals( type ) ) {
			discriminatorColumn.setDiscriminatorTypeName( "integer" );
			discriminatorColumn.setImplicit( false );
		}
		else if ( DiscriminatorType.STRING.equals( type ) || type == null ) {
			if ( discAnn != null ) discriminatorColumn.setLength( discAnn.length() );
			discriminatorColumn.setDiscriminatorTypeName( "string" );
		}
		else {
			throw new AssertionFailure( "Unknown discriminator type: " + type );
		}
		discriminatorColumn.bind();
		return discriminatorColumn;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append( "Ejb3DiscriminatorColumn" );
		sb.append( "{logicalColumnName'" ).append( getLogicalColumnName() ).append( '\'' );
		sb.append( ", discriminatorTypeName='" ).append( discriminatorTypeName ).append( '\'' );
		sb.append( '}' );
		return sb.toString();
	}
}
