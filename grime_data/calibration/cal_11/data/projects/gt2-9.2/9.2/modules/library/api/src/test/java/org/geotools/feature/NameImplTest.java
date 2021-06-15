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
package org.geotools.feature;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.*;

import static org.junit.Assert.*;
import org.opengis.feature.type.Name;

/**
 * 
 *
 * @source $URL$
 */
public class NameImplTest {

    @Test
    public void testSerialize()  throws Exception {
        NameImpl name = new NameImpl("hello","world");
        
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(buffer);
        
        out.writeObject( name );
        
        byte[] bytes = buffer.toByteArray();
        
        ByteArrayInputStream input = new ByteArrayInputStream( bytes );
        ObjectInputStream in = new ObjectInputStream( input );
        
        Name copy = (Name) in.readObject();
        
        assertNotSame( name, copy );
        assertEquals( name, copy );
    }
    
    @Test
    public void testCompare(){
        NameImpl scoped1 = new NameImpl("hello","world");
        NameImpl scoped2 = new NameImpl("hello","fred");
        NameImpl fred = new NameImpl("world");

        assertTrue( 0 == scoped1.compareTo(scoped1));
        assertTrue( 0 == scoped2.compareTo(scoped2));
        assertTrue( 0 == fred.compareTo(fred));
        
        assertTrue( scoped1.compareTo(scoped2) > 0 );
        assertTrue( scoped2.compareTo(scoped1) < 0 );
        
        assertTrue( scoped2.compareTo(scoped1) < 0 );        
    }
}
