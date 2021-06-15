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
package org.hibernate.metamodel.source.annotations.global;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.logging.Logger;

import org.hibernate.AnnotationException;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.metamodel.binding.TypeDef;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.metamodel.source.annotations.AnnotationBindingContext;
import org.hibernate.metamodel.source.annotations.HibernateDotNames;
import org.hibernate.metamodel.source.annotations.JandexHelper;

/**
 * Binds {@link org.hibernate.annotations.TypeDef} and {@link TypeDefs}.
 *
 * @author Hardy Ferentschik
 */
public class TypeDefBinder {

	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class,
			TypeDefBinder.class.getName()
	);

	/**
	 * Binds all {@link org.hibernate.annotations.TypeDef} and {@link TypeDefs} annotations to the supplied metadata.
	 *
	 * @param bindingContext the context for annotation binding
	 */
	public static void bind(AnnotationBindingContext bindingContext) {
		List<AnnotationInstance> annotations = bindingContext.getIndex().getAnnotations( HibernateDotNames.TYPE_DEF );
		for ( AnnotationInstance typeDef : annotations ) {
			bind( bindingContext.getMetadataImplementor(), typeDef );
		}

		annotations = bindingContext.getIndex().getAnnotations( HibernateDotNames.TYPE_DEFS );
		for ( AnnotationInstance typeDefs : annotations ) {
			AnnotationInstance[] typeDefAnnotations = JandexHelper.getValue(
					typeDefs,
					"value",
					AnnotationInstance[].class
			);
			for ( AnnotationInstance typeDef : typeDefAnnotations ) {
				bind( bindingContext.getMetadataImplementor(), typeDef );
			}
		}
	}

	private static void bind(MetadataImplementor metadata, AnnotationInstance typeDefAnnotation) {
		String name = JandexHelper.getValue( typeDefAnnotation, "name", String.class );
		String defaultForType = JandexHelper.getValue( typeDefAnnotation, "defaultForType", String.class );
		String typeClass = JandexHelper.getValue( typeDefAnnotation, "typeClass", String.class );

		boolean noName = StringHelper.isEmpty( name );
		boolean noDefaultForType = defaultForType == null || defaultForType.equals( void.class.getName() );

		if ( noName && noDefaultForType ) {
			throw new AnnotationException(
					"Either name or defaultForType (or both) attribute should be set in TypeDef having typeClass "
							+ typeClass
			);
		}

		Map<String, String> parameterMaps = new HashMap<String, String>();
		AnnotationInstance[] parameterAnnotations = JandexHelper.getValue(
				typeDefAnnotation,
				"parameters",
				AnnotationInstance[].class
		);
		for ( AnnotationInstance parameterAnnotation : parameterAnnotations ) {
			parameterMaps.put(
					JandexHelper.getValue( parameterAnnotation, "name", String.class ),
					JandexHelper.getValue( parameterAnnotation, "value", String.class )
			);
		}

		if ( !noName ) {
			bind( name, typeClass, parameterMaps, metadata );
		}
		if ( !noDefaultForType ) {
			bind( defaultForType, typeClass, parameterMaps, metadata );
		}
	}

	private static void bind(
			String name,
			String typeClass,
			Map<String, String> prms,
			MetadataImplementor metadata) {
		LOG.debugf( "Binding type definition: %s", name );
		metadata.addTypeDefinition( new TypeDef( name, typeClass, prms ) );
	}

	private TypeDefBinder() {
	}
}
