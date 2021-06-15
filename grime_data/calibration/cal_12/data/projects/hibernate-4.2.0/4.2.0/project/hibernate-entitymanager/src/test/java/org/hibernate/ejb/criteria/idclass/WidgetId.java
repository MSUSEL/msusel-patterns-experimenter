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
package  org.hibernate.ejb.criteria.idclass;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;

/**
 * @author Erich Heard
 */

@Embeddable
public class WidgetId implements Serializable {
	private static final long serialVersionUID = 9122480802791185644L;
	
	@Id
	@Column( name = "CODE", length = 3 )
	private String code;
	public String getCode( ) { return this.code; }
	public void setCode( String value ) { this.code = value; }
	
	@Id
	@Column( name = "DIVISION", length = 4 )
	private String division;
	public String getDivision( ) { return this.division; }
	public void setDivision( String value ) { this.division = value; }
	
	@Override
	public boolean equals( Object obj ) {
		if( obj == null ) return false;
		if( !( obj instanceof WidgetId ) ) return false;
		
		WidgetId id = ( WidgetId )obj;
		if( this.getCode( ) == null || id.getCode( ) == null || this.getDivision( ) == null || id.getDivision( ) == null ) return false;
		
		return this.toString( ).equals( id.toString( ) );
	}
	
	@Override
	public int hashCode( ) {
		return this.toString( ).hashCode( );
	}
	
	@Override
	public String toString( ) {
		StringBuffer buf = new StringBuffer( "[id:" );
		buf.append( ( this.getCode( ) == null ) ? "null" : this.getCode( ).toString( ) );
		buf.append( ";code:" );
		buf.append( ( this.getDivision( ) == null ) ? "null" : this.getDivision( ) );
		buf.append( "]" );
		
		return buf.toString( );
	}
}
