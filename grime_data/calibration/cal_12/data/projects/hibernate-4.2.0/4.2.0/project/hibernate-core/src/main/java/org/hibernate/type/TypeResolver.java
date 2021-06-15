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
package org.hibernate.type;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.MappingException;
import org.hibernate.classic.Lifecycle;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.usertype.CompositeUserType;
import org.hibernate.usertype.UserType;

/**
 * Acts as the contract for getting types and as the mediator between {@link BasicTypeRegistry} and {@link TypeFactory}.
 *
 * @author Steve Ebersole
 */
public class TypeResolver implements Serializable {
	private final BasicTypeRegistry basicTypeRegistry;
	private final TypeFactory typeFactory;

	public TypeResolver() {
		this(  new BasicTypeRegistry(), new TypeFactory() );
	}

	public TypeResolver(BasicTypeRegistry basicTypeRegistry, TypeFactory typeFactory) {
		this.basicTypeRegistry = basicTypeRegistry;
		this.typeFactory = typeFactory;
	}

	public TypeResolver scope(SessionFactoryImplementor factory) {
		typeFactory.injectSessionFactory( factory );
		return new TypeResolver( basicTypeRegistry.shallowCopy(), typeFactory );
	}

	public void registerTypeOverride(BasicType type) {
		basicTypeRegistry.register( type );
	}

	public void registerTypeOverride(UserType type, String[] keys) {
		basicTypeRegistry.register( type, keys );
	}

	public void registerTypeOverride(CompositeUserType type, String[] keys) {
		basicTypeRegistry.register( type, keys );
	}

	public TypeFactory getTypeFactory() {
		return typeFactory;
	}

	/**
	 * Locate a Hibernate {@linkplain BasicType basic type} given (one of) its registration names.
	 *
	 * @param name The registration name
	 *
	 * @return The registered type
	 */
	public BasicType basic(String name) {
		return basicTypeRegistry.getRegisteredType( name );
	}

	/**
	 * See {@link #heuristicType(String, Properties)}
	 *
	 * @param typeName The name (see heuristic algorithm discussion on {@link #heuristicType(String, Properties)}).
	 *
	 * @return The deduced type; may be null.
	 *
	 * @throws MappingException Can be thrown from {@link #heuristicType(String, Properties)}
	 */
	public Type heuristicType(String typeName) throws MappingException {
		return heuristicType( typeName, null );
	}

	/**
	 * Uses heuristics to deduce the proper {@link Type} given a string naming the type or Java class.
	 * <p/>
	 * The search goes as follows:<ol>
	 * 	<li>search for a basic type with 'typeName' as a registration key</li>
	 * 	<li>
	 * 		look for 'typeName' as a class name and<ol>
	 *			<li>if it names a {@link Type} implementor, return an instance</li>
	 *			<li>if it names a {@link CompositeUserType} or a {@link UserType}, return an instance of class wrapped intot the appropriate {@link Type} adapter</li>
	 * 			<li>if it implements {@link Lifecycle}, return the corresponding entity type</li>
	 * 			<li>if it implements {@link Serializable}, return the corresponding serializable type</li>
	 * 		</ol>
	 * 	</li>
	 * </ol>
	 *
	 * @param typeName The name (see heuristic algorithm above).
	 * @param parameters Any parameters for the type.  Only applied if built!
	 *
	 * @return The deduced type; may be null.
	 *
	 * @throws MappingException Indicates a problem attempting to resolve 'typeName' as a {@link Class}
	 */
	public Type heuristicType(String typeName, Properties parameters) throws MappingException {
		Type type = basic( typeName );
		if ( type != null ) {
			return type;
		}

		try {
			Class typeClass = ReflectHelper.classForName( typeName );
			if ( typeClass != null ) {
				return typeFactory.byClass( typeClass, parameters );
			}
		}
		catch ( ClassNotFoundException ignore ) {
		}

		return null;
	}
}
