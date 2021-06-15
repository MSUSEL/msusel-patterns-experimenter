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
package org.archive.crawler.selftest;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Variant TestSuite that can build tests including methods with an alternate
 * prefix (other than 'test'). Copies code from TestSuite because necessary
 * methods to change are private rather than protected. 
 *
 * @author gojomo
 * @version $Id: MaxLinkHopsSelfTest.java 4667 2006-09-26 20:38:48 +0000 (Tue, 26 Sep 2006) paul_jack $
 */
public class AltTestSuite extends TestSuite {
    /** a method prefix other than 'test' that is also recognized as tests */
    String altPrefix;
    
    /**
     * Constructs a TestSuite from the given class. Copied from superclass so
     * that local alternate addTestMethod() will be visible, which in turn uses
     * an isTestMethod() that accepts methods with the altPrefix in addition
     * to 'test'.
     * @param theClass Class from which to build suite
     * @param prefix alternate method prefix to also find test methods
     */
     public AltTestSuite(final Class theClass, String prefix) {
        this.altPrefix = prefix;
        setName(theClass.getName());
        try {
            getTestConstructor(theClass); // Avoid generating multiple error messages
        } catch (NoSuchMethodException e) {
            addTest(warning("Class "+theClass.getName()+" has no public constructor TestCase(String name) or TestCase()"));
            return;
        }

        if (!Modifier.isPublic(theClass.getModifiers())) {
            addTest(warning("Class "+theClass.getName()+" is not public"));
            return;
        }

        Class superClass= theClass;
        Vector names= new Vector();
        while (Test.class.isAssignableFrom(superClass)) {
            Method[] methods= superClass.getDeclaredMethods();
            for (int i= 0; i < methods.length; i++) {
                addTestMethod(methods[i], names, theClass);
            }
            superClass= superClass.getSuperclass();
        }
        if (testCount() == 0)
            addTest(warning("No tests found in "+theClass.getName()));
    }

    // copied from superclass
    private void addTestMethod(Method m, Vector names, Class theClass) {
        String name= m.getName();
        if (names.contains(name))
            return;
        if (! isPublicTestMethod(m)) {
            if (isTestMethod(m))
                addTest(warning("Test method isn't public: "+m.getName()));
            return;
        }
        names.addElement(name);
        addTest(createTest(theClass, name));
    }

    // copied from superclass
    private boolean isPublicTestMethod(Method m) {
        return isTestMethod(m) && Modifier.isPublic(m.getModifiers());
     }
    
    // copied & extended from superclass
    private boolean isTestMethod(Method m) {
        String name= m.getName();
        Class[] parameters= m.getParameterTypes();
        Class returnType= m.getReturnType();
        return parameters.length == 0 
            && (name.startsWith("test")||name.startsWith(altPrefix)) 
            && returnType.equals(Void.TYPE);
     }
    
    /* filler constructor to avoid JUNit "no public constructor" warnings */
	public AltTestSuite() {
		super();
	}
	/* noop test to avoid "no tests found" warnings */
	public void testNoop() {
		
	}
}
