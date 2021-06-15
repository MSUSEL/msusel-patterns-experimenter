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
//$Id$
package org.hibernate.ejb.test.instrument;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.StackMapTable;

import org.hibernate.testing.TestForIssue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 * @author Dustin Schultz
 */
public class InterceptFieldClassFileTransformerTest {
	
	private List<String> entities = new ArrayList<String>();
	private InstrumentedClassLoader loader = null;
	
	@Before
	public void setup() {
		entities.add( "org.hibernate.ejb.test.instrument.Simple" );
		// use custom class loader which enhances the class
		InstrumentedClassLoader cl = new InstrumentedClassLoader( Thread.currentThread().getContextClassLoader() );
		cl.setEntities( entities );
		this.loader = cl;
	}
	
	/**
	 * Tests that class file enhancement works.
	 * 
	 * @throws Exception in case the test fails.
	 */
    @Test
	public void testEnhancement() throws Exception {
		// sanity check that the class is unmodified and does not contain getFieldHandler()
		try {
			org.hibernate.ejb.test.instrument.Simple.class.getDeclaredMethod( "getFieldHandler" );
			Assert.fail();
		} catch ( NoSuchMethodException nsme ) {
			// success
		}
		
		Class clazz = loader.loadClass( entities.get( 0 ) );
		
		// javassist is our default byte code enhancer. Enhancing will eg add the method getFieldHandler()
		// see org.hibernate.bytecode.internal.javassist.FieldTransformer
		Method method = clazz.getDeclaredMethod( "getFieldHandler" );
		Assert.assertNotNull( method );
	}
    
	/**
	 * Tests that methods that were enhanced by javassist have
	 * StackMapTables for java verification. Without these,
	 * java.lang.VerifyError's occur in JDK7.
	 * 
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	@Test
	@TestForIssue(jiraKey = "HHH-7747")
	public void testStackMapTableEnhancment() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, IOException {
		byte[] classBytes = loader.loadClassBytes(entities.get(0));
		ClassPool classPool = new ClassPool();
		CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(
				classBytes));
		for (CtMethod ctMethod : ctClass.getMethods()) {
			//Only check methods that were added by javassist
			if (ctMethod.getName().startsWith("$javassist_")) {
				AttributeInfo attributeInfo = ctMethod
						.getMethodInfo().getCodeAttribute()
						.getAttribute(StackMapTable.tag);
				Assert.assertNotNull(attributeInfo);
				StackMapTable smt = (StackMapTable)attributeInfo;
				Assert.assertNotNull(smt.get());
			}
		}
	}
}
