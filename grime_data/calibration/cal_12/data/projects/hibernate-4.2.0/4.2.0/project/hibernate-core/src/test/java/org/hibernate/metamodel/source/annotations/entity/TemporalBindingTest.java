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
package org.hibernate.metamodel.source.annotations.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.junit.Test;

import org.hibernate.AnnotationException;
import org.hibernate.metamodel.binding.AttributeBinding;
import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.metamodel.binding.HibernateTypeDescriptor;
import org.hibernate.type.TimestampType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Strong Liu
 */
public class TemporalBindingTest extends BaseAnnotationBindingTestCase {
    @Entity
    class Item1 {
        @Id
        long id;
        Date date;
    }

    @Test(expected = AnnotationException.class)
    @Resources(annotatedClasses = TemporalBindingTest.Item1.class)
    public void testNoTemporalAnnotationOnTemporalTypeAttribute() {
        getEntityBinding( Item1.class );

    }

    @Entity
    class Item2 {
        @Id
        long id;
        @Temporal(TemporalType.TIMESTAMP)
        Date date;
    }

    @Test
    @Resources(annotatedClasses = TemporalBindingTest.Item2.class)
    public void testTemporalTypeAttribute() {
        EntityBinding binding = getEntityBinding( Item2.class );
        AttributeBinding attributeBinding = binding.locateAttributeBinding( "date" );
        HibernateTypeDescriptor descriptor = attributeBinding.getHibernateTypeDescriptor();
        assertEquals( "timestamp", descriptor.getExplicitTypeName() );
        assertEquals( Date.class.getName(), descriptor.getJavaTypeName() );
        assertNotNull( descriptor.getResolvedTypeMapping() );
        assertEquals( TimestampType.class, descriptor.getResolvedTypeMapping().getClass() );
        assertNotNull( descriptor.getTypeParameters() );
        assertTrue( descriptor.getTypeParameters().isEmpty() );
    }

    @Entity
    class Item3 {
        @Id
        @Temporal(TemporalType.TIMESTAMP)
        Date date;
    }

    @Test
    @Resources(annotatedClasses = TemporalBindingTest.Item3.class)
    public void testTemporalTypeAsId() {
        EntityBinding binding = getEntityBinding( Item3.class );
        AttributeBinding attributeBinding = binding.locateAttributeBinding( "date" );
        HibernateTypeDescriptor descriptor = attributeBinding.getHibernateTypeDescriptor();
        assertEquals( "timestamp", descriptor.getExplicitTypeName() );
        assertEquals( Date.class.getName(), descriptor.getJavaTypeName() );
        assertNotNull( descriptor.getResolvedTypeMapping() );
        assertEquals( TimestampType.class, descriptor.getResolvedTypeMapping().getClass() );
        assertNotNull( descriptor.getTypeParameters() );
        assertTrue( descriptor.getTypeParameters().isEmpty() );
    }
}
