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
package org.hibernate.test.cfg;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.junit.Test;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.util.SerializationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.testing.ServiceRegistryBuilder;
import org.hibernate.testing.junit4.BaseUnitTestCase;

/**
 * Copied over mostly from ConfigurationPerformanceTest
 *
 * @author Steve Ebersole
 * @author Max Andersen
 */
public class ConfigurationSerializationTest extends BaseUnitTestCase {
	private static final String[] FILES = new String[] {
			"legacy/ABC.hbm.xml",
			"legacy/ABCExtends.hbm.xml",
			"legacy/Baz.hbm.xml",
			"legacy/Blobber.hbm.xml",
			"legacy/Broken.hbm.xml",
			"legacy/Category.hbm.xml",
			"legacy/Circular.hbm.xml",
			"legacy/Commento.hbm.xml",
			"legacy/ComponentNotNullMaster.hbm.xml",
			"legacy/Componentizable.hbm.xml",
			"legacy/Container.hbm.xml",
			"legacy/Custom.hbm.xml",
			"legacy/CustomSQL.hbm.xml",
			"legacy/Eye.hbm.xml",
			"legacy/Fee.hbm.xml",
			"legacy/Fo.hbm.xml",
			"legacy/FooBar.hbm.xml",
			"legacy/Fum.hbm.xml",
			"legacy/Fumm.hbm.xml",
			"legacy/Glarch.hbm.xml",
			"legacy/Holder.hbm.xml",
			"legacy/IJ2.hbm.xml",
			"legacy/Immutable.hbm.xml",
			"legacy/Location.hbm.xml",
			"legacy/Many.hbm.xml",
			"legacy/Map.hbm.xml",
			"legacy/Marelo.hbm.xml",
			"legacy/MasterDetail.hbm.xml",
			"legacy/Middle.hbm.xml",
			"legacy/Multi.hbm.xml",
			"legacy/MultiExtends.hbm.xml",
			"legacy/Nameable.hbm.xml",
			"legacy/One.hbm.xml",
			"legacy/ParentChild.hbm.xml",
			"legacy/Qux.hbm.xml",
			"legacy/Simple.hbm.xml",
			"legacy/SingleSeveral.hbm.xml",
			"legacy/Stuff.hbm.xml",
			"legacy/UpDown.hbm.xml",
			"legacy/Vetoer.hbm.xml",
			"legacy/WZ.hbm.xml",
			"cfg/orm-serializable.xml"
	};

	@Test
	public void testConfigurationSerializability() {
		Configuration cfg = new Configuration();
		for ( String file : FILES ) {
			cfg.addResource( "org/hibernate/test/" + file );
		}

		cfg.addAnnotatedClass( Serial.class );

		byte[] bytes = SerializationHelper.serialize( cfg );
		cfg = ( Configuration ) SerializationHelper.deserialize( bytes );

		SessionFactory factory = null;
		ServiceRegistry serviceRegistry = null;
		try {
			serviceRegistry = ServiceRegistryBuilder.buildServiceRegistry( cfg.getProperties() );
			// try to build SF
			factory = cfg.buildSessionFactory( serviceRegistry );
		}
		finally {
			if ( factory != null ) {
				factory.close();
			}
			if ( serviceRegistry != null ) {
				ServiceRegistryBuilder.destroy( serviceRegistry );
			}
		}
	}

	@Entity
	public static class Serial {
		private String id;
		private String value;

		@Id
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}
}
