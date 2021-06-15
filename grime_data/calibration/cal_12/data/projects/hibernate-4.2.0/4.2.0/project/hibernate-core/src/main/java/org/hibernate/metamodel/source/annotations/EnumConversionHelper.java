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
package org.hibernate.metamodel.source.annotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.GenerationType;

import org.hibernate.AssertionFailure;
import org.hibernate.FetchMode;
import org.hibernate.engine.spi.CascadeStyle;
import org.hibernate.id.MultipleHiLoPerTableGenerator;
import org.hibernate.internal.util.collections.CollectionHelper;

/**
 * Helper class which converts between different enum types.
 *
 * @author Hardy Ferentschik
 */
public class EnumConversionHelper {
	private EnumConversionHelper() {
	}

	public static String generationTypeToGeneratorStrategyName(GenerationType generatorEnum, boolean useNewGeneratorMappings) {
		switch ( generatorEnum ) {
			case IDENTITY:
				return "identity";
			case AUTO:
				return useNewGeneratorMappings
						? "enhanced-sequence"
						: "native";
			case TABLE:
				return useNewGeneratorMappings
						? "enhanced-table"
						: MultipleHiLoPerTableGenerator.class.getName();
			case SEQUENCE:
				return useNewGeneratorMappings
						? "enhanced-sequence"
						: "seqhilo";
		}
		throw new AssertionFailure( "Unknown GeneratorType: " + generatorEnum );
	}

	public static CascadeStyle cascadeTypeToCascadeStyle(CascadeType cascadeType) {
		switch ( cascadeType ) {
			case ALL: {
				return CascadeStyle.ALL;
			}
			case PERSIST: {
				return CascadeStyle.PERSIST;
			}
			case MERGE: {
				return CascadeStyle.MERGE;
			}
			case REMOVE: {
				return CascadeStyle.DELETE;
			}
			case REFRESH: {
				return CascadeStyle.REFRESH;
			}
			case DETACH: {
				return CascadeStyle.EVICT;
			}
			default: {
				throw new AssertionFailure( "Unknown cascade type" );
			}
		}
	}

	public static FetchMode annotationFetchModeToHibernateFetchMode(org.hibernate.annotations.FetchMode annotationFetchMode) {
		switch ( annotationFetchMode ) {
			case JOIN: {
				return FetchMode.JOIN;
			}
			case SELECT: {
				return FetchMode.SELECT;
			}
			case SUBSELECT: {
				// todo - is this correct? can the conversion be made w/o any additional information, eg
				// todo - association nature
				return FetchMode.SELECT;
			}
			default: {
				throw new AssertionFailure( "Unknown fetch mode" );
			}
		}
	}

	public static Set<CascadeStyle> cascadeTypeToCascadeStyleSet(Set<CascadeType> cascadeTypes) {
		if ( CollectionHelper.isEmpty( cascadeTypes ) ) {
			return Collections.emptySet();
		}
		Set<CascadeStyle> cascadeStyleSet = new HashSet<CascadeStyle>();
		for ( CascadeType cascadeType : cascadeTypes ) {
			cascadeStyleSet.add( cascadeTypeToCascadeStyle( cascadeType ) );
		}
		return cascadeStyleSet;
	}
}


