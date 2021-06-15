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
package org.hibernate.type.descriptor.java;
import org.hibernate.HibernateException;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.type.descriptor.WrapperOptions;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class ClassTypeDescriptor extends AbstractTypeDescriptor<Class> {
	public static final ClassTypeDescriptor INSTANCE = new ClassTypeDescriptor();

	public ClassTypeDescriptor() {
		super( Class.class );
	}

	public String toString(Class value) {
		return value.getName();
	}

	public Class fromString(String string) {
		if ( string == null ) {
			return null;
		}

		try {
			return ReflectHelper.classForName( string );
		}
		catch ( ClassNotFoundException e ) {
			throw new HibernateException( "Unable to locate named class " + string );
		}
	}

	@SuppressWarnings({ "unchecked" })
	public <X> X unwrap(Class value, Class<X> type, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}
		if ( Class.class.isAssignableFrom( type ) ) {
			return (X) value;
		}
		if ( String.class.isAssignableFrom( type ) ) {
			return (X) toString( value );
		}
		throw unknownUnwrap( type );
	}

	public <X> Class wrap(X value, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}
		if ( Class.class.isInstance( value ) ) {
			return (Class) value;
		}
		if ( String.class.isInstance( value ) ) {
			return fromString( (String)value );
		}
		throw unknownWrap( value.getClass() );
	}
}
