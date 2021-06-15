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
package org.hibernate.test.service;

import org.hibernate.service.classloading.internal.ClassLoaderServiceImpl;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.Entity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Artem V. Navrotskiy
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class ClassLoaderServiceImplTest {
    /**
     * Test for bug: HHH-7084
     */
    @Test
    public void testSystemClassLoaderNotOverriding() throws IOException, ClassNotFoundException {
        Class<?> testClass = Entity.class;

        // Check that class is accessible by SystemClassLoader.
        ClassLoader.getSystemClassLoader().loadClass(testClass.getName());

        // Create ClassLoader with overridden class.
        TestClassLoader anotherLoader = new TestClassLoader();
        anotherLoader.overrideClass(testClass);
        Class<?> anotherClass = anotherLoader.loadClass(testClass.getName());
        Assert.assertNotSame( testClass, anotherClass );

        // Check ClassLoaderServiceImpl().classForName() returns correct class (not from current ClassLoader).
        ClassLoaderServiceImpl loaderService = new ClassLoaderServiceImpl(anotherLoader);
        Class<Object> objectClass = loaderService.classForName(testClass.getName());
        Assert.assertSame("Should not return class loaded from the parent classloader of ClassLoaderServiceImpl",
				objectClass, anotherClass);
    }

    private static class TestClassLoader extends ClassLoader {
        /**
         * Reloading class from binary file.
         *
         * @param originalClass Original class.
         * @throws IOException .
         */
        public void overrideClass(final Class<?> originalClass) throws IOException {
            String originalPath = "/" + originalClass.getName().replaceAll("\\.", "/") + ".class";
            InputStream inputStream = originalClass.getResourceAsStream(originalPath);
            Assert.assertNotNull(inputStream);
            try {
                byte[] data = toByteArray( inputStream );
                defineClass(originalClass.getName(), data, 0, data.length);
            } finally {
                inputStream.close();
            }
        }

		private byte[] toByteArray(InputStream inputStream) throws IOException {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int read;
			byte[] slice = new byte[2000];
			while ( (read = inputStream.read(slice, 0, slice.length) ) != -1) {
			  out.write( slice, 0, read );
			}
			out.flush();
			return out.toByteArray();
		}
    }
}
