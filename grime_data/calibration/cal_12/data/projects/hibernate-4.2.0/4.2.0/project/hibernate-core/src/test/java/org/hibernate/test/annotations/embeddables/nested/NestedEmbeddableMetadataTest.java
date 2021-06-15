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
package org.hibernate.test.annotations.embeddables.nested;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.testing.junit4.BaseUnitTestCase;
import org.hibernate.type.CustomType;

import static org.hibernate.testing.junit4.ExtraAssertions.assertJdbcTypeCode;
import static org.junit.Assert.assertEquals;

/**
 * @author Steve Ebersole
 */
public class NestedEmbeddableMetadataTest extends BaseUnitTestCase {
	@Test
	public void testEnumTypeInterpretation() {
		Configuration cfg = new Configuration().addAnnotatedClass( Customer.class );
		cfg.buildMappings();
		Mapping mapping = cfg.buildMapping();
		PersistentClass classMetadata = cfg.getClassMapping( Customer.class.getName() );
		Property investmentsProperty = classMetadata.getProperty( "investments" );
		Collection investmentsValue = (Collection) investmentsProperty.getValue();
		Component investmentMetadata = (Component) investmentsValue.getElement();
		Component amountMetadata = (Component) investmentMetadata.getProperty( "amount" ).getValue();
		SimpleValue currencyMetadata = (SimpleValue) amountMetadata.getProperty( "currency" ).getValue();
		CustomType currencyType = (CustomType) currencyMetadata.getType();
		int[] currencySqlTypes = currencyType.sqlTypes( mapping );
		assertEquals( 1, currencySqlTypes.length );
		assertJdbcTypeCode( Types.VARCHAR, currencySqlTypes[0] );
	}
}
