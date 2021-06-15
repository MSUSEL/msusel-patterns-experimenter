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
package org.hibernate.cache.spi;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class NaturalIdCacheKeyTest {
    @Test
    public void testSerializationRoundTrip() throws Exception {
        final EntityPersister entityPersister = mock(EntityPersister.class);
        final SessionImplementor sessionImplementor = mock(SessionImplementor.class);
        final SessionFactoryImplementor sessionFactoryImplementor = mock(SessionFactoryImplementor.class);
        final Type mockType = mock(Type.class);
        
        when (entityPersister.getRootEntityName()).thenReturn("EntityName");
        
        when(sessionImplementor.getFactory()).thenReturn(sessionFactoryImplementor);
        
        when(entityPersister.getNaturalIdentifierProperties()).thenReturn(new int[] {0, 1, 2});
        when(entityPersister.getPropertyTypes()).thenReturn(new Type[] {
                mockType,
                mockType,
                mockType
        });
        
        when(mockType.getHashCode(anyObject(), eq(sessionFactoryImplementor))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArguments()[0].hashCode();
            }
        });
        
        when(mockType.disassemble(anyObject(), eq(sessionImplementor), eq(null))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArguments()[0];
            }
        });
        
        final NaturalIdCacheKey key = new NaturalIdCacheKey(new Object[] {"a", "b", "c"}, entityPersister, sessionImplementor);
        
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(key);
        
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        final NaturalIdCacheKey keyClone = (NaturalIdCacheKey)ois.readObject();
        
        assertEquals(key, keyClone);
        assertEquals(key.hashCode(), keyClone.hashCode());
        assertEquals(key.toString(), keyClone.toString());
        assertEquals(key.getEntityName(), keyClone.getEntityName());
        assertArrayEquals(key.getNaturalIdValues(), keyClone.getNaturalIdValues());
        assertEquals(key.getTenantId(), keyClone.getTenantId());
        
    }
}
