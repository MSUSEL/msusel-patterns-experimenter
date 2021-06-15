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
package org.hibernate.envers.test.performance;

import java.io.IOException;
import java.util.Properties;
import javax.persistence.EntityManager;

import org.hibernate.envers.test.AbstractEnversTest;
import org.junit.Before;

import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.event.EnversIntegrator;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.BootstrapServiceRegistryBuilder;
import org.hibernate.service.internal.StandardServiceRegistryImpl;
import org.hibernate.testing.AfterClassOnce;
import org.hibernate.testing.BeforeClassOnce;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public abstract class AbstractEntityManagerTest extends AbstractEnversTest {
    public static final Dialect DIALECT = Dialect.getDialect();

    private EntityManagerFactoryImpl emf;
    private EntityManager entityManager;
    private AuditReader auditReader;
    private Ejb3Configuration cfg;
	private StandardServiceRegistryImpl serviceRegistry;
    private boolean audited;

    public abstract void configure(Ejb3Configuration cfg);

    public void addConfigurationProperties(Properties configuration) { }

    protected static Dialect getDialect() {
        return DIALECT;
    }

    private void closeEntityManager() {
        if (entityManager != null) {
            entityManager.close();
            entityManager = null;
        }
    }

    @Before
    public void newEntityManager() {
        closeEntityManager();
        
        entityManager = emf.createEntityManager();

        if (audited) {
            auditReader = AuditReaderFactory.get(entityManager);
        }
    }

    @BeforeClassOnce
    public void init() throws IOException {
        init(true, getAuditStrategy());
    }

    protected void init(boolean audited, String auditStrategy) throws IOException {
        this.audited = audited;

        Properties configurationProperties = new Properties();
		configurationProperties.putAll( Environment.getProperties() );
        if (!audited) {
			configurationProperties.setProperty(EnversIntegrator.AUTO_REGISTER, "false");
        }
		if ( createSchema() ) {
			configurationProperties.setProperty( Environment.HBM2DDL_AUTO, "create-drop" );
			configurationProperties.setProperty( Environment.USE_NEW_ID_GENERATOR_MAPPINGS, "true" );
			configurationProperties.setProperty("org.hibernate.envers.use_revision_entity_with_native_id", "false");
		}
        if (auditStrategy != null && !"".equals(auditStrategy)) {
            configurationProperties.setProperty("org.hibernate.envers.audit_strategy", auditStrategy);
        }

        addConfigurationProperties(configurationProperties);

        cfg = new Ejb3Configuration();
        configure(cfg);
        cfg.configure(configurationProperties);

        emf = (EntityManagerFactoryImpl) cfg.buildEntityManagerFactory( createBootstrapRegistryBuilder() );

		serviceRegistry = (StandardServiceRegistryImpl) ( (SessionFactoryImpl) emf.getSessionFactory() ).getServiceRegistry().getParentServiceRegistry();

        newEntityManager();
    }

	protected boolean createSchema() {
		return true;
	}

	private BootstrapServiceRegistryBuilder createBootstrapRegistryBuilder() {
		return new BootstrapServiceRegistryBuilder();
	}

	@AfterClassOnce
    public void close() {
        closeEntityManager();
        emf.close();
		//NOTE we don't build the service registry so we don't destroy it
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public AuditReader getAuditReader() {
        return auditReader;
    }

    public Ejb3Configuration getCfg() {
        return cfg;
    }
}
