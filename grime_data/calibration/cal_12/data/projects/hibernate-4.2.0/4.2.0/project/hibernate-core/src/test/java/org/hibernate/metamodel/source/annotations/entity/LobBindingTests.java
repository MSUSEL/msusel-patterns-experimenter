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

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.junit.Test;

import org.hibernate.metamodel.binding.AttributeBinding;
import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.metamodel.binding.HibernateTypeDescriptor;
import org.hibernate.type.BlobType;
import org.hibernate.type.CharacterArrayClobType;
import org.hibernate.type.ClobType;
import org.hibernate.type.MaterializedBlobType;
import org.hibernate.type.MaterializedClobType;
import org.hibernate.type.PrimitiveCharacterArrayClobType;
import org.hibernate.type.SerializableToBlobType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.WrappedMaterializedBlobType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Strong Liu
 */
public class LobBindingTests extends BaseAnnotationBindingTestCase {
    @Entity
    class Item {
        @Id
        long id;
        @Lob
        Clob clob;
        @Lob
        Blob blob;
        @Lob
        String str;
        @Lob
        Character[] characters;
        @Lob
        char[] chars;
        @Lob
        Byte[] bytes;
        @Lob
        byte[] bytes2;
        @Lob
        Thing serializable;
        String noLob;
    }

    class Thing implements Serializable {
        int size;
    }

    private HibernateTypeDescriptor getTypeDescriptor(String attributeName) {
        EntityBinding binding = getEntityBinding( Item.class );
        AttributeBinding attributeBinding = binding.locateAttributeBinding( attributeName );
        return attributeBinding.getHibernateTypeDescriptor();
    }

    private class ExpectedValue {
        String explicitTypeName;
        String javaTypeName;
        boolean isResolvedTypeMappingNull;
        Class resolvedTypeMappingClass;
        boolean isTypeParametersNull;
        boolean isTypeParametersEmpty;

        private ExpectedValue(String explicitTypeName,
                              String javaTypeName,
                              boolean resolvedTypeMappingNull,
                              Class resolvedTypeMappingClass,
                              boolean typeParametersNull,
                              boolean typeParametersEmpty
        ) {
            this.explicitTypeName = explicitTypeName;
            this.isResolvedTypeMappingNull = resolvedTypeMappingNull;
            this.isTypeParametersEmpty = typeParametersEmpty;
            this.isTypeParametersNull = typeParametersNull;
            this.javaTypeName = javaTypeName;
            this.resolvedTypeMappingClass = resolvedTypeMappingClass;
        }
    }

    private void checkHibernateTypeDescriptor(ExpectedValue expectedValue, String attributeName) {
        HibernateTypeDescriptor descriptor = getTypeDescriptor( attributeName );
        assertEquals( expectedValue.explicitTypeName, descriptor.getExplicitTypeName() );
        assertEquals( expectedValue.javaTypeName, descriptor.getJavaTypeName() );
        assertEquals( expectedValue.isResolvedTypeMappingNull, descriptor.getResolvedTypeMapping() == null );
        assertEquals( expectedValue.resolvedTypeMappingClass, descriptor.getResolvedTypeMapping().getClass() );
        assertEquals( expectedValue.isTypeParametersNull, descriptor.getTypeParameters() == null );
        assertEquals( expectedValue.isTypeParametersEmpty, descriptor.getTypeParameters().isEmpty() );
    }

    @Test
    @Resources(annotatedClasses = Item.class)
    public void testClobWithLobAnnotation() {
        ExpectedValue expectedValue = new ExpectedValue(
                "clob",
                Clob.class.getName(),
                false,
                ClobType.class,
                false,
                true
        );
        checkHibernateTypeDescriptor( expectedValue, "clob" );
    }

    @Test
    @Resources(annotatedClasses = Item.class)
    public void testBlobWithLobAnnotation() {
        ExpectedValue expectedValue = new ExpectedValue(
                "blob",
                Blob.class.getName(),
                false,
                BlobType.class,
                false,
                true
        );
        checkHibernateTypeDescriptor( expectedValue, "blob" );
    }

    @Test
    @Resources(annotatedClasses = Item.class)
    public void testStringWithLobAnnotation() {
        ExpectedValue expectedValue = new ExpectedValue(
                "materialized_clob",
                String.class.getName(),
                false,
                MaterializedClobType.class,
                false,
                true
        );
        checkHibernateTypeDescriptor( expectedValue, "str" );
    }

    @Test
    @Resources(annotatedClasses = Item.class)
    public void testCharacterArrayWithLobAnnotation() {
        ExpectedValue expectedValue = new ExpectedValue(
                CharacterArrayClobType.class.getName(),
                Character[].class.getName(),
                false,
                CharacterArrayClobType.class,
                false,
                true
        );
        checkHibernateTypeDescriptor( expectedValue, "characters" );
    }

    @Test
    @Resources(annotatedClasses = Item.class)
    public void testPrimitiveCharacterArrayWithLobAnnotation() {
        ExpectedValue expectedValue = new ExpectedValue(
                PrimitiveCharacterArrayClobType.class.getName(),
                char[].class.getName(),
                false,
                PrimitiveCharacterArrayClobType.class,
                false,
                true
        );
        checkHibernateTypeDescriptor( expectedValue, "chars" );
    }

    @Test
    @Resources(annotatedClasses = Item.class)
    public void testByteArrayWithLobAnnotation() {
        ExpectedValue expectedValue = new ExpectedValue(
                WrappedMaterializedBlobType.class.getName(),
                Byte[].class.getName(),
                false,
                WrappedMaterializedBlobType.class,
                false,
                true
        );
        checkHibernateTypeDescriptor( expectedValue, "bytes" );
    }

    @Test
    @Resources(annotatedClasses = Item.class)
    public void testPrimitiveByteArrayWithLobAnnotation() {
        ExpectedValue expectedValue = new ExpectedValue(
                StandardBasicTypes.MATERIALIZED_BLOB.getName(),
                byte[].class.getName(),
                false,
                MaterializedBlobType.class,
                false,
                true
        );
        checkHibernateTypeDescriptor( expectedValue, "bytes2" );
    }

    @Test
    @Resources(annotatedClasses = Item.class)
    public void testSerializableWithLobAnnotation() {
        ExpectedValue expectedValue = new ExpectedValue(
                SerializableToBlobType.class.getName(),
                Thing.class.getName(),
                false,
                SerializableToBlobType.class,
                false,
                false
        );
        checkHibernateTypeDescriptor( expectedValue, "serializable" );

        assertTrue(
                getTypeDescriptor( "serializable" ).getTypeParameters()
                        .get( SerializableToBlobType.CLASS_NAME )
                        .equals( Thing.class.getName() )
        );
    }


    @Test
    @Resources(annotatedClasses = Item.class)
    public void testNoLobAttribute() {
        assertNull( getTypeDescriptor( "noLob" ).getExplicitTypeName() );
        assertTrue( getTypeDescriptor( "noLob" ).getTypeParameters().isEmpty() );

    }
}
