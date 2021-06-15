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
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.logging.Logger;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.GenericGenerators;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.id.MultipleHiLoPerTableGenerator;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.SequenceHiLoGenerator;
import org.hibernate.id.TableHiLoGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.id.enhanced.TableGenerator;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.metamodel.binding.IdGenerator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.metamodel.source.annotations.AnnotationBindingContext;
import org.hibernate.metamodel.source.annotations.EnumConversionHelper;
import org.hibernate.metamodel.source.annotations.HibernateDotNames;
import org.hibernate.metamodel.source.annotations.JPADotNames;
import org.hibernate.metamodel.source.annotations.JandexHelper;

/**
 * Binds {@link SequenceGenerator}, {@link javax.persistence.TableGenerator}, {@link GenericGenerator}, and
 * {@link GenericGenerators} annotations.
 *
 * @author Hardy Ferentschik
 */
public class IdGeneratorBinder {

	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class,
			IdGeneratorBinder.class.getName()
	);

	private IdGeneratorBinder() {
	}

	/**
	 * Binds all {@link SequenceGenerator}, {@link javax.persistence.TableGenerator}, {@link GenericGenerator}, and
	 * {@link GenericGenerators} annotations to the supplied metadata.
	 *
	 * @param bindingContext the context for annotation binding
	 */
	public static void bind(AnnotationBindingContext bindingContext) {
		List<AnnotationInstance> annotations = bindingContext.getIndex()
				.getAnnotations( JPADotNames.SEQUENCE_GENERATOR );
		for ( AnnotationInstance generator : annotations ) {
			bindSequenceGenerator( bindingContext.getMetadataImplementor(), generator );
		}

		annotations = bindingContext.getIndex().getAnnotations( JPADotNames.TABLE_GENERATOR );
		for ( AnnotationInstance generator : annotations ) {
			bindTableGenerator( bindingContext.getMetadataImplementor(), generator );
		}

		annotations = bindingContext.getIndex().getAnnotations( HibernateDotNames.GENERIC_GENERATOR );
		for ( AnnotationInstance generator : annotations ) {
			bindGenericGenerator( bindingContext.getMetadataImplementor(), generator );
		}

		annotations = bindingContext.getIndex().getAnnotations( HibernateDotNames.GENERIC_GENERATORS );
		for ( AnnotationInstance generators : annotations ) {
			for ( AnnotationInstance generator : JandexHelper.getValue(
					generators,
					"value",
					AnnotationInstance[].class
			) ) {
				bindGenericGenerator( bindingContext.getMetadataImplementor(), generator );
			}
		}
	}

	private static void addStringParameter(AnnotationInstance annotation,
										   String element,
										   Map<String, String> parameters,
										   String parameter) {
		String string = JandexHelper.getValue( annotation, element, String.class );
		if ( StringHelper.isNotEmpty( string ) ) {
			parameters.put( parameter, string );
		}
	}

	private static void bindGenericGenerator(MetadataImplementor metadata, AnnotationInstance generator) {
		String name = JandexHelper.getValue( generator, "name", String.class );
		Map<String, String> parameterMap = new HashMap<String, String>();
		AnnotationInstance[] parameterAnnotations = JandexHelper.getValue(
				generator,
				"parameters",
				AnnotationInstance[].class
		);
		for ( AnnotationInstance parameterAnnotation : parameterAnnotations ) {
			parameterMap.put(
					JandexHelper.getValue( parameterAnnotation, "name", String.class ),
					JandexHelper.getValue( parameterAnnotation, "value", String.class )
			);
		}
		metadata.addIdGenerator(
				new IdGenerator(
						name,
						JandexHelper.getValue( generator, "strategy", String.class ),
						parameterMap
				)
		);
		LOG.tracef( "Add generic generator with name: %s", name );
	}

	private static void bindSequenceGenerator(MetadataImplementor metadata, AnnotationInstance generator) {
		String name = JandexHelper.getValue( generator, "name", String.class );
		Map<String, String> parameterMap = new HashMap<String, String>();
		addStringParameter( generator, "sequenceName", parameterMap, SequenceStyleGenerator.SEQUENCE_PARAM );
		boolean useNewIdentifierGenerators = metadata.getOptions().useNewIdentifierGenerators();
		String strategy = EnumConversionHelper.generationTypeToGeneratorStrategyName(
				GenerationType.SEQUENCE,
				useNewIdentifierGenerators
		);
		if ( useNewIdentifierGenerators ) {
			addStringParameter( generator, "catalog", parameterMap, PersistentIdentifierGenerator.CATALOG );
			addStringParameter( generator, "schema", parameterMap, PersistentIdentifierGenerator.SCHEMA );
			parameterMap.put(
					SequenceStyleGenerator.INCREMENT_PARAM,
					String.valueOf( JandexHelper.getValue( generator, "allocationSize", Integer.class ) )
			);
			parameterMap.put(
					SequenceStyleGenerator.INITIAL_PARAM,
					String.valueOf( JandexHelper.getValue( generator, "initialValue", Integer.class ) )
			);
		}
		else {
			if ( JandexHelper.getValue( generator, "initialValue", Integer.class ) != 1 ) {
				LOG.unsupportedInitialValue( AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS );
			}
			parameterMap.put(
					SequenceHiLoGenerator.MAX_LO,
					String.valueOf( JandexHelper.getValue( generator, "allocationSize", Integer.class ) - 1 )
			);
		}
		metadata.addIdGenerator( new IdGenerator( name, strategy, parameterMap ) );
		LOG.tracef( "Add sequence generator with name: %s", name );
	}

	private static void bindTableGenerator(MetadataImplementor metadata, AnnotationInstance generator) {
		String name = JandexHelper.getValue( generator, "name", String.class );
		Map<String, String> parameterMap = new HashMap<String, String>();
		addStringParameter( generator, "catalog", parameterMap, PersistentIdentifierGenerator.CATALOG );
		addStringParameter( generator, "schema", parameterMap, PersistentIdentifierGenerator.SCHEMA );
		boolean useNewIdentifierGenerators = metadata.getOptions().useNewIdentifierGenerators();
		String strategy = EnumConversionHelper.generationTypeToGeneratorStrategyName(
				GenerationType.TABLE,
				useNewIdentifierGenerators
		);
		if ( useNewIdentifierGenerators ) {
			parameterMap.put( TableGenerator.CONFIG_PREFER_SEGMENT_PER_ENTITY, "true" );
			addStringParameter( generator, "table", parameterMap, TableGenerator.TABLE_PARAM );
			addStringParameter( generator, "pkColumnName", parameterMap, TableGenerator.SEGMENT_COLUMN_PARAM );
			addStringParameter( generator, "pkColumnValue", parameterMap, TableGenerator.SEGMENT_VALUE_PARAM );
			addStringParameter( generator, "valueColumnName", parameterMap, TableGenerator.VALUE_COLUMN_PARAM );
			parameterMap.put(
					TableGenerator.INCREMENT_PARAM,
					String.valueOf( JandexHelper.getValue( generator, "allocationSize", String.class ) )
			);
			parameterMap.put(
					TableGenerator.INITIAL_PARAM,
					String.valueOf( JandexHelper.getValue( generator, "initialValue", String.class ) + 1 )
			);
		}
		else {
			addStringParameter( generator, "table", parameterMap, MultipleHiLoPerTableGenerator.ID_TABLE );
			addStringParameter( generator, "pkColumnName", parameterMap, MultipleHiLoPerTableGenerator.PK_COLUMN_NAME );
			addStringParameter( generator, "pkColumnValue", parameterMap, MultipleHiLoPerTableGenerator.PK_VALUE_NAME );
			addStringParameter( generator, "valueColumnName", parameterMap, MultipleHiLoPerTableGenerator.VALUE_COLUMN_NAME );
			parameterMap.put(
					TableHiLoGenerator.MAX_LO,
					String.valueOf( JandexHelper.getValue( generator, "allocationSize", Integer.class ) - 1 )
			);
		}
		if ( JandexHelper.getValue( generator, "uniqueConstraints", AnnotationInstance[].class ).length > 0 ) {
			LOG.ignoringTableGeneratorConstraints( name );
		}
		metadata.addIdGenerator( new IdGenerator( name, strategy, parameterMap ) );
		LOG.tracef( "Add table generator with name: %s", name );
	}
}
