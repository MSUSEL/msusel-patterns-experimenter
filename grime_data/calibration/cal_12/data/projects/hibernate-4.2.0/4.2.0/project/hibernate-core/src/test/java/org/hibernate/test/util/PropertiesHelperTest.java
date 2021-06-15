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
package org.hibernate.test.util;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Steve Ebersole
 */
public class PropertiesHelperTest extends BaseUnitTestCase {
	private Properties props;

	@Before
	public void setUp() throws Exception {
		props = new Properties();

		props.setProperty( "my.nonexistent.prop", "${}" );

		props.setProperty( "my.string.prop", "${test.my.sys.string.prop}" );
		System.setProperty( "test.my.sys.string.prop", "string" );

		props.setProperty( "my.boolean.prop", "${test.my.sys.boolean.prop}" );
		System.setProperty( "test.my.sys.boolean.prop", "true" );

		props.setProperty( "my.int.prop", "${test.my.sys.int.prop}" );
		System.setProperty( "test.my.sys.int.prop", "1" );

		props.setProperty( "my.integer.prop", "${test.my.sys.integer.prop}" );
		System.setProperty( "test.my.sys.integer.prop", "1" );

		props.setProperty( "partial.prop1", "${somedir}/middle/dir/${somefile}" );
		props.setProperty( "partial.prop2", "basedir/${somedir}/myfile.txt" );
		System.setProperty( "somedir", "tmp" );
		System.setProperty( "somefile", "tmp.txt" );

		props.setProperty( "parse.error", "steve" );
	}

	@Test
	public void testPlaceholderReplacement() {
		ConfigurationHelper.resolvePlaceHolders( props );

		String str = ConfigurationHelper.getString( "my.nonexistent.prop", props, "did.not.exist" );
		assertEquals( "did.not.exist", str );
		str = ConfigurationHelper.getString( "my.nonexistent.prop", props, null );
		assertNull( str );
		str = ConfigurationHelper.getString( "my.string.prop", props, "na" );
		assertEquals( "replacement did not occur", "string", str );
		str = ConfigurationHelper.getString( "my.string.prop", props, "did.not.exist" );
		assertEquals( "replacement did not occur", "string", str );

		boolean bool = ConfigurationHelper.getBoolean( "my.nonexistent.prop", props );
		assertFalse( "non-exists as boolean", bool );
		bool = ConfigurationHelper.getBoolean( "my.nonexistent.prop", props, false );
		assertFalse( "non-exists as boolean", bool );
		bool = ConfigurationHelper.getBoolean( "my.nonexistent.prop", props, true );
		assertTrue( "non-exists as boolean", bool );
		bool = ConfigurationHelper.getBoolean( "my.boolean.prop", props );
		assertTrue( "boolean replacement did not occur", bool );
		bool = ConfigurationHelper.getBoolean( "my.boolean.prop", props, false );
		assertTrue( "boolean replacement did not occur", bool );

		int i = ConfigurationHelper.getInt( "my.nonexistent.prop", props, -1 );
		assertEquals( -1, i );
		i = ConfigurationHelper.getInt( "my.int.prop", props, 100 );
		assertEquals( 1, i );

		Integer I = ConfigurationHelper.getInteger( "my.nonexistent.prop", props );
		assertNull( I );
		I = ConfigurationHelper.getInteger( "my.integer.prop", props );
		assertEquals( I, new Integer( 1 ) );

		str = props.getProperty( "partial.prop1" );
		assertEquals( "partial replacement (ends)", "tmp/middle/dir/tmp.txt", str );

		str = props.getProperty( "partial.prop2" );
		assertEquals( "partial replacement (midst)", "basedir/tmp/myfile.txt", str );
	}

	@Test
	public void testParseExceptions() {
		boolean b = ConfigurationHelper.getBoolean( "parse.error", props );
		assertFalse( "parse exception case - boolean", b );

		try {
			ConfigurationHelper.getInt( "parse.error", props, 20 );
			fail( "parse exception case - int" );
		}
		catch( NumberFormatException expected ) {
		}

		try {
			ConfigurationHelper.getInteger( "parse.error", props );
			fail( "parse exception case - Integer" );
		}
		catch( NumberFormatException expected ) {
		}
	}
}
