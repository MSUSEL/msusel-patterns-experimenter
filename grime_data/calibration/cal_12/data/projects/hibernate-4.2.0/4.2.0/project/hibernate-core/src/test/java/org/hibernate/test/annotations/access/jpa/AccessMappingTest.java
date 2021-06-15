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
package org.hibernate.test.annotations.access.jpa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.hibernate.MappingException;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.property.BasicPropertyAccessor;
import org.hibernate.property.DirectPropertyAccessor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.testing.ServiceRegistryBuilder;
import org.hibernate.testing.TestForIssue;
import org.hibernate.tuple.entity.EntityTuplizer;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * Tests verifying the correct behaviour for the usage of {@code @javax.persistence.Access}.
 *
 * @author Hardy Ferentschik
 */
@SuppressWarnings({ "deprecation" })
public class AccessMappingTest {
    private ServiceRegistry serviceRegistry;

    @Before
    public void setUp() {
        serviceRegistry = ServiceRegistryBuilder.buildServiceRegistry( Environment.getProperties() );
    }

    @After
    public void tearDown() {
        if ( serviceRegistry != null ) {
            ServiceRegistryBuilder.destroy( serviceRegistry );
        }
    }

    @Test
    public void testInconsistentAnnotationPlacement() throws Exception {
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        cfg.addAnnotatedClass( Course1.class );
        cfg.addAnnotatedClass( Student.class );
        try {
            cfg.buildSessionFactory( serviceRegistry );
            fail( "@Id and @OneToMany are not placed consistently in test entities. SessionFactory creation should fail." );
        }
        catch ( MappingException e ) {
            // success
        }
    }

    @Test
    public void testFieldAnnotationPlacement() throws Exception {
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        Class<?> classUnderTest = Course6.class;
        cfg.addAnnotatedClass( classUnderTest );
        cfg.addAnnotatedClass( Student.class );
        SessionFactoryImplementor factory = (SessionFactoryImplementor) cfg.buildSessionFactory( serviceRegistry );
        EntityTuplizer tuplizer = factory.getEntityPersister( classUnderTest.getName() )
                .getEntityMetamodel()
                .getTuplizer();
        assertTrue(
                "Field access should be used.",
                tuplizer.getIdentifierGetter() instanceof DirectPropertyAccessor.DirectGetter
        );
    }

    @Test
    public void testPropertyAnnotationPlacement() throws Exception {
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        Class<?> classUnderTest = Course7.class;
        cfg.addAnnotatedClass( classUnderTest );
        cfg.addAnnotatedClass( Student.class );
        SessionFactoryImplementor factory = (SessionFactoryImplementor) cfg.buildSessionFactory( serviceRegistry );
        EntityTuplizer tuplizer = factory.getEntityPersister( classUnderTest.getName() )
                .getEntityMetamodel()
                .getTuplizer();
        assertTrue(
                "Property access should be used.",
                tuplizer.getIdentifierGetter() instanceof BasicPropertyAccessor.BasicGetter
        );
    }

    @Test
    public void testExplicitPropertyAccessAnnotationsOnProperty() throws Exception {
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        Class<?> classUnderTest = Course2.class;
        cfg.addAnnotatedClass( classUnderTest );
        cfg.addAnnotatedClass( Student.class );
        SessionFactoryImplementor factory = (SessionFactoryImplementor) cfg.buildSessionFactory( serviceRegistry );
        EntityTuplizer tuplizer = factory.getEntityPersister( classUnderTest.getName() )
                .getEntityMetamodel()
                .getTuplizer();
        assertTrue(
                "Property access should be used.",
                tuplizer.getIdentifierGetter() instanceof BasicPropertyAccessor.BasicGetter
        );
    }

    @Test
    public void testExplicitPropertyAccessAnnotationsOnField() throws Exception {
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        cfg.addAnnotatedClass( Course4.class );
        cfg.addAnnotatedClass( Student.class );
        try {
            cfg.buildSessionFactory( serviceRegistry );
            fail( "@Id and @OneToMany are not placed consistently in test entities. SessionFactory creation should fail." );
        }
        catch ( MappingException e ) {
            // success
        }
    }

    @Test
    public void testExplicitPropertyAccessAnnotationsWithHibernateStyleOverride() throws Exception {
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        Class<?> classUnderTest = Course3.class;
        cfg.addAnnotatedClass( classUnderTest );
        cfg.addAnnotatedClass( Student.class );
        SessionFactoryImplementor factory = (SessionFactoryImplementor) cfg.buildSessionFactory( serviceRegistry );
        EntityTuplizer tuplizer = factory.getEntityPersister( classUnderTest.getName() )
                .getEntityMetamodel()
                .getTuplizer();
        assertTrue(
                "Field access should be used.",
                tuplizer.getIdentifierGetter() instanceof DirectPropertyAccessor.DirectGetter
        );

        assertTrue(
                "Property access should be used.",
                tuplizer.getGetter( 0 ) instanceof BasicPropertyAccessor.BasicGetter
        );
    }

    @Test
    public void testExplicitPropertyAccessAnnotationsWithJpaStyleOverride() throws Exception {
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        Class<?> classUnderTest = Course5.class;
        cfg.addAnnotatedClass( classUnderTest );
        cfg.addAnnotatedClass( Student.class );
        SessionFactoryImplementor factory = (SessionFactoryImplementor) cfg.buildSessionFactory( serviceRegistry );
        EntityTuplizer tuplizer = factory.getEntityPersister( classUnderTest.getName() )
                .getEntityMetamodel()
                .getTuplizer();
        assertTrue(
                "Field access should be used.",
                tuplizer.getIdentifierGetter() instanceof DirectPropertyAccessor.DirectGetter
        );

        assertTrue(
                "Property access should be used.",
                tuplizer.getGetter( 0 ) instanceof BasicPropertyAccessor.BasicGetter
        );
    }

    @Test
    public void testDefaultFieldAccessIsInherited() throws Exception {
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        Class<?> classUnderTest = User.class;
        cfg.addAnnotatedClass( classUnderTest );
        cfg.addAnnotatedClass( Person.class );
        cfg.addAnnotatedClass( Being.class );
        SessionFactoryImplementor factory = (SessionFactoryImplementor) cfg.buildSessionFactory( serviceRegistry );
        EntityTuplizer tuplizer = factory.getEntityPersister( classUnderTest.getName() )
                .getEntityMetamodel()
                .getTuplizer();
        assertTrue(
                "Field access should be used since the default access mode gets inherited",
                tuplizer.getIdentifierGetter() instanceof DirectPropertyAccessor.DirectGetter
        );
    }

    @Test
    public void testDefaultPropertyAccessIsInherited() throws Exception {
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        cfg.addAnnotatedClass( Horse.class );
        cfg.addAnnotatedClass( Animal.class );

        SessionFactoryImplementor factory = (SessionFactoryImplementor) cfg.buildSessionFactory( serviceRegistry );
        EntityTuplizer tuplizer = factory.getEntityPersister( Animal.class.getName() )
                .getEntityMetamodel()
                .getTuplizer();
        assertTrue(
                "Property access should be used since explicity configured via @Access",
                tuplizer.getIdentifierGetter() instanceof BasicPropertyAccessor.BasicGetter
        );

        tuplizer = factory.getEntityPersister( Horse.class.getName() )
                .getEntityMetamodel()
                .getTuplizer();
        assertTrue(
                "Field access should be used since the default access mode gets inherited",
                tuplizer.getGetter( 0 ) instanceof DirectPropertyAccessor.DirectGetter
        );
    }

    @TestForIssue(jiraKey = "HHH-5004")
    @Test
    public void testAccessOnClassAndId() throws Exception {
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        cfg.addAnnotatedClass( Course8.class );
        cfg.addAnnotatedClass( Student.class );
        cfg.buildSessionFactory( serviceRegistry );
    }
}