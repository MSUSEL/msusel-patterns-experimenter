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

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.metamodel.source.annotations.AnnotationBindingContext;
import org.hibernate.metamodel.source.annotations.HibernateDotNames;
import org.hibernate.metamodel.source.annotations.JandexHelper;
import org.hibernate.type.Type;

/**
 * Binds {@link FilterDefs} and {@link FilterDef} annotations.
 *
 * @author Hardy Ferentschik
 */
public class FilterDefBinder {

	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class,
			FilterDefBinder.class.getName()
	);

	/**
	 * Binds all {@link FilterDefs} and {@link FilterDef} annotations to the supplied metadata.
	 *
	 * @param bindingContext the context for annotation binding
	 */
	public static void bind(AnnotationBindingContext bindingContext) {
		List<AnnotationInstance> annotations = bindingContext.getIndex().getAnnotations( HibernateDotNames.FILTER_DEF );
		for ( AnnotationInstance filterDef : annotations ) {
			bind( bindingContext.getMetadataImplementor(), filterDef );
		}

		annotations = bindingContext.getIndex().getAnnotations( HibernateDotNames.FILTER_DEFS );
		for ( AnnotationInstance filterDefs : annotations ) {
			AnnotationInstance[] filterDefAnnotations = JandexHelper.getValue(
					filterDefs,
					"value",
					AnnotationInstance[].class
			);
			for ( AnnotationInstance filterDef : filterDefAnnotations ) {
				bind( bindingContext.getMetadataImplementor(), filterDef );
			}
		}
	}

	private static void bind(MetadataImplementor metadata, AnnotationInstance filterDef) {
		String name = JandexHelper.getValue( filterDef, "name", String.class );
		Map<String, Type> prms = new HashMap<String, Type>();
		for ( AnnotationInstance prm : JandexHelper.getValue( filterDef, "parameters", AnnotationInstance[].class ) ) {
			prms.put(
					JandexHelper.getValue( prm, "name", String.class ),
					metadata.getTypeResolver().heuristicType( JandexHelper.getValue( prm, "type", String.class ) )
			);
		}
		metadata.addFilterDefinition(
				new FilterDefinition(
						name,
						JandexHelper.getValue( filterDef, "defaultCondition", String.class ),
						prms
				)
		);
		LOG.debugf( "Binding filter definition: %s", name );
	}

	private FilterDefBinder() {
	}
}
