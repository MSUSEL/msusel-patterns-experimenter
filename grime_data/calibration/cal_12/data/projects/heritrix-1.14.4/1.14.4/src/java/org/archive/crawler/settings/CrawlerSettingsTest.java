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
package org.archive.crawler.settings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.management.AttributeNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;


/** Test the CrawlerSettings object
 *
 * @author John Erik Halse
 */
public class CrawlerSettingsTest extends SettingsFrameworkTestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    final public void testAddComplexType() {
        ModuleType mod = new ModuleType("name");
        DataContainer data = getGlobalSettings().addComplexType(mod);
        assertNotNull(data);
    }

    final public void testGetModule() {
        ModuleType mod = new ModuleType("name");
        getGlobalSettings().addComplexType(mod);
        assertSame(mod, getGlobalSettings().getModule("name"));
    }
    
    public void testSerializingSimpleModuleType()
    throws IOException, ClassNotFoundException {
        ModuleType mt =
            new ModuleType("testSerializingSimpleModuleType");
        ModuleType mtDeserialized = (ModuleType)serializeDeserialize(mt);
        assertEquals(mt.getName(), mtDeserialized.getName());
    }
    
    public void testSerializingStringAttributeModuleType()
    throws IOException, ClassNotFoundException, AttributeNotFoundException,
    MBeanException, ReflectionException {
        ModuleType mt =
            new ModuleType("testSerializingStringAttributeModuleType");
        final String value = "value";
        mt.addElementToDefinition(new SimpleType("name", "description",
            value));
        ModuleType mtDeserialized = (ModuleType)serializeDeserialize(mt);
        assertEquals(mt.getName(), mtDeserialized.getName());
        assertEquals(value, (String)mtDeserialized.getAttribute("name"));
    }
    
    public void testSerializingTextField()
    throws IOException, ClassNotFoundException, AttributeNotFoundException,
    MBeanException, ReflectionException {
        TextField tf = new TextField("testSerializingTextField");
        TextField tfDeserialized = (TextField)serializeDeserialize(tf);
        assertEquals(tf.toString(), tfDeserialized.toString());
    }
    
    protected Object serializeDeserialize(Object obj)
    throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        byte [] objectBytes = baos.toByteArray();
        ObjectInputStream ois =
            new ObjectInputStream(new ByteArrayInputStream(objectBytes));
        return ois.readObject();
    }
}
