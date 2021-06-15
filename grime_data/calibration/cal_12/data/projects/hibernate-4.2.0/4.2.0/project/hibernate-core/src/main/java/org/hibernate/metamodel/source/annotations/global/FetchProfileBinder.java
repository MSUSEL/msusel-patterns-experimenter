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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.jandex.AnnotationInstance;

import org.hibernate.MappingException;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfiles;
import org.hibernate.metamodel.binding.FetchProfile;
import org.hibernate.metamodel.binding.FetchProfile.Fetch;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.metamodel.source.annotations.AnnotationBindingContext;
import org.hibernate.metamodel.source.annotations.HibernateDotNames;
import org.hibernate.metamodel.source.annotations.JandexHelper;

/**
 * Binds fetch profiles found in annotations.
 *
 * @author Hardy Ferentschik
 */
public class FetchProfileBinder {

	private FetchProfileBinder() {
	}

	/**
	 * Binds all {@link FetchProfiles} and {@link org.hibernate.annotations.FetchProfile} annotations to the supplied metadata.
	 *
	 * @param bindingContext the context for annotation binding
	 */
	// TODO verify that association exists. See former VerifyFetchProfileReferenceSecondPass
	public static void bind(AnnotationBindingContext bindingContext) {

		List<AnnotationInstance> annotations = bindingContext.getIndex()
				.getAnnotations( HibernateDotNames.FETCH_PROFILE );
		for ( AnnotationInstance fetchProfile : annotations ) {
			bind( bindingContext.getMetadataImplementor(), fetchProfile );
		}

		annotations = bindingContext.getIndex().getAnnotations( HibernateDotNames.FETCH_PROFILES );
		for ( AnnotationInstance fetchProfiles : annotations ) {
			AnnotationInstance[] fetchProfileAnnotations = JandexHelper.getValue(
					fetchProfiles,
					"value",
					AnnotationInstance[].class
			);
			for ( AnnotationInstance fetchProfile : fetchProfileAnnotations ) {
				bind( bindingContext.getMetadataImplementor(), fetchProfile );
			}
		}
	}

	private static void bind(MetadataImplementor metadata, AnnotationInstance fetchProfile) {
		String name = JandexHelper.getValue( fetchProfile, "name", String.class );
		Set<Fetch> fetches = new HashSet<Fetch>();
		AnnotationInstance[] overrideAnnotations = JandexHelper.getValue(
				fetchProfile,
				"fetchOverrides",
				AnnotationInstance[].class
		);
		for ( AnnotationInstance override : overrideAnnotations ) {
			FetchMode fetchMode = JandexHelper.getEnumValue( override, "mode", FetchMode.class );
			if ( !fetchMode.equals( org.hibernate.annotations.FetchMode.JOIN ) ) {
				throw new MappingException( "Only FetchMode.JOIN is currently supported" );
			}
			final String entityName = JandexHelper.getValue( override, "entity", String.class );
			final String associationName = JandexHelper.getValue( override, "association", String.class );
			fetches.add( new Fetch( entityName, associationName, fetchMode.toString().toLowerCase() ) );
		}
		metadata.addFetchProfile( new FetchProfile( name, fetches ) );
	}
}
