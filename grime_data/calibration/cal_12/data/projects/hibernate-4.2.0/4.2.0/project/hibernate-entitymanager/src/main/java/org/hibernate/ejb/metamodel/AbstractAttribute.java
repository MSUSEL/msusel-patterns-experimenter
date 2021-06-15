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
package org.hibernate.ejb.metamodel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;

import org.hibernate.internal.util.ReflectHelper;

/**
 * Models the commonality of the JPA {@link Attribute} hierarchy.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractAttribute<X, Y>
		implements Attribute<X, Y>, AttributeImplementor<X,Y>, Serializable {
	private final String name;
	private final Class<Y> javaType;
	private final AbstractManagedType<X> declaringType;
	private transient Member member;
	private final PersistentAttributeType persistentAttributeType;

	public AbstractAttribute(
			String name,
			Class<Y> javaType,
			AbstractManagedType<X> declaringType,
			Member member,
			PersistentAttributeType persistentAttributeType) {
		this.name = name;
		this.javaType = javaType;
		this.declaringType = declaringType;
		this.member = member;
		this.persistentAttributeType = persistentAttributeType;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedType<X> getDeclaringType() {
		return declaringType;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<Y> getJavaType() {
		return javaType;
	}

	/**
	 * {@inheritDoc}
	 */
	public Member getJavaMember() {
		return member;
	}

	/**
	 * {@inheritDoc}
	 */
	public PersistentAttributeType getPersistentAttributeType() {
		return persistentAttributeType;
	}

	/**
	 * Used by JDK serialization...
	 *
	 * @param ois The input stream from which we are being read...
	 * @throws java.io.IOException Indicates a general IO stream exception
	 * @throws ClassNotFoundException Indicates a class resolution issue
	 */
	protected void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		final String memberDeclaringClassName = ( String ) ois.readObject();
		final String memberName = ( String ) ois.readObject();
		final String memberType = ( String ) ois.readObject();

		final Class memberDeclaringClass = Class.forName(
				memberDeclaringClassName,
				false,
				declaringType.getJavaType().getClassLoader()
		);
		try {
			this.member = "method".equals( memberType )
					? memberDeclaringClass.getMethod( memberName, ReflectHelper.NO_PARAM_SIGNATURE )
					: memberDeclaringClass.getField( memberName );
		}
		catch ( Exception e ) {
			throw new IllegalStateException(
					"Unable to locate member [" + memberDeclaringClassName + "#"
							+ memberName + "]"
			);
		}
	}

	/**
	 * Used by JDK serialization...
	 *
	 * @param oos The output stream to which we are being written...
	 * @throws IOException Indicates a general IO stream exception
	 */
	protected void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		oos.writeObject( getJavaMember().getDeclaringClass().getName() );
		oos.writeObject( getJavaMember().getName() );
		// should only ever be a field or the getter-method...
		oos.writeObject( Method.class.isInstance( getJavaMember() ) ? "method" : "field" );
	}
}
