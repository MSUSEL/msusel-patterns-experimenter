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
 * @author Gail Badner
 */

@Embeddable
public class HelperId implements Serializable {
	private static final long serialVersionUID = 9122480802791185646L;
	
	@Id
	@Column( name = "NAME", length = 12 )
	private String name;
	public String getName( ) { return this.name; }
	public void setName( String value ) { this.name = value; }
	
	@Id
	@Column( name = "HELPER_TYPE", length = 4 )
	private String type;
	public String getType( ) { return this.type; }
	public void setType( String value ) { this.type = value; }
	
	@Override
	public boolean equals( Object obj ) {
		if( obj == null ) return false;
		if( !( obj instanceof HelperId ) ) return false;
		
		HelperId id = ( HelperId )obj;
		if( this.getName() == null || id.getName() == null || this.getType() == null || id.getType() == null ) return false;
		
		return this.toString( ).equals( id.toString( ) );
	}
	
	@Override
	public int hashCode( ) {
		return this.toString( ).hashCode( );
	}
	
	@Override
	public String toString( ) {
		StringBuilder buf = new StringBuilder( "[id:" );
		buf.append( ( this.getName() == null ) ? "null" : this.getName( ) );
		buf.append( ";type:" );
		buf.append( ( this.getType() == null ) ? "null" : this.getType() );
		buf.append( "]" );
		
		return buf.toString( );
	}
}
