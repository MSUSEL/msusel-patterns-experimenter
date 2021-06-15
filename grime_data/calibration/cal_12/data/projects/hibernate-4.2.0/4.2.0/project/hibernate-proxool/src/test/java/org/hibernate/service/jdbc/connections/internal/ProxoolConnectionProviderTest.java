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
package org.hibernate.service.jdbc.connections.internal;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Test;
import org.logicalcobwebs.proxool.ProxoolFacade;

import org.hibernate.cfg.Environment;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test to verify connection pools are closed, and that only the managed one is closed.
 *
 * @author Sanne Grinovero
 */
public class ProxoolConnectionProviderTest extends BaseUnitTestCase {
	@Test
	public void testPoolsClosed() {
		assertDefinedPools(); // zero-length-vararg used as parameter
		
		ProxoolConnectionProvider providerOne = new ProxoolConnectionProvider();
		providerOne.configure( getPoolConfigurarion( "pool-one" ) );
		assertDefinedPools( "pool-one" );
		
		ProxoolConnectionProvider providerTwo = new ProxoolConnectionProvider();
		providerTwo.configure( getPoolConfigurarion( "pool-two" ) );
		assertDefinedPools( "pool-one", "pool-two" );
		
		providerOne.close();
		assertDefinedPools( "pool-two" );
		
		providerTwo.close();
		assertDefinedPools();
	}

	private void assertDefinedPools(String... expectedPoolNames) {
		List<String> aliases = Arrays.asList( ProxoolFacade.getAliases() );
		assertEquals( expectedPoolNames.length,	aliases.size() );
		for (String poolName : expectedPoolNames) {
			assertTrue( "pool named " + poolName + " missing", aliases.contains( poolName ) );
		}
	}

	private Properties getPoolConfigurarion(String poolName) {
		Properties cfg = new Properties();
		cfg.setProperty( Environment.PROXOOL_POOL_ALIAS, poolName );
		cfg.setProperty( Environment.PROXOOL_PROPERTIES, poolName + ".properties" );
		return cfg;
	}
	
}
