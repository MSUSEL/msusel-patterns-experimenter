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
package com.gargoylesoftware.htmlunit.gae;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;

/**
 * Test runner for GAE support tests. This runner uses a custom class loader that
 * tries to enforce GAE class loading rules. Test class and HtmlUnit classes are loaded by the
 * same loader what allows to write tests "normally" without any need
 * for reflection to access method/members.
 *
 * @version $Revision: 5922 $
 * @author Marc Guillemot
 * @author Amit Manjhi
 */
public class GAETestRunner extends BlockJUnit4ClassRunner {
    private static final Set<String> whitelist = loadWhiteList();
    private static final Set<String> additionalWhitelist = getAdditionalWhitelist();

    static class GAELikeClassLoader extends ClassLoader {
        @Override
        public Class<?> loadClass(final String name) throws ClassNotFoundException {
            final String baseName = StringUtils.substringBefore(name, "$");
            if (baseName.startsWith("java") && !whitelist.contains(baseName)
                && !additionalWhitelist.contains(baseName)) {
                throw new NoClassDefFoundError(name + " is a restricted class for GAE");
            }
            if (!name.startsWith("com.gargoylesoftware")) {
                return super.loadClass(name);
            }
            super.loadClass(name);
            final InputStream is = getResourceAsStream(name.replaceAll("\\.", "/") + ".class");
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                IOUtils.copy(is, bos);
                final byte[] bytes = bos.toByteArray();
                return defineClass(name, bytes, 0, bytes.length);
            }
            catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Called by JUnit.
     * @param klass the test class
     * @throws Throwable in case of problem
     */
    public GAETestRunner(final Class<?> klass) throws Throwable {
        super(getClassFromGAELikeClassLoader(klass));
    }

    private static Class<?> getClassFromGAELikeClassLoader(final Class<?> klass) {
        final ClassLoader gaeLikeLoader = new GAELikeClassLoader();
        try {
            return gaeLikeLoader.loadClass(klass.getName());
        }
        catch (final ClassNotFoundException e) {
            throw new RuntimeException("Can't find existing test class through GAELikeClassLoader: " + klass.getName());
        }
    }

    /**
     * Get list of classes that are not being used
     * but are loaded by getHtmlJavaScriptMapping() in
     * {@link com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration}
     *
     * @return the list of additional classes.
     */
    private static Set<String> getAdditionalWhitelist() {
        final String additionalClasses[] = {"java.awt.image.RenderedImage",
            "java.awt.geom.Rectangle2D", "java.awt.geom.Ellipse2D", "java.applet.AppletStub"};
        final Set<String> classesAsSet = new HashSet<String>();
        for (String additionalClass : additionalClasses) {
            classesAsSet.add(additionalClass);
        }
        return classesAsSet;
    }

    /**
     * Loads the white list.
     * @return the list of classes in the white list
     */
    @SuppressWarnings("unchecked")
    private static Set<String> loadWhiteList() {
        final InputStream is = GAETestRunner.class.getResourceAsStream("whitelist.txt");
        Assert.assertNotNull(is);
        List<String> lines;
        try {
            lines = IOUtils.readLines(is);
        }
        catch (final IOException e) {
            throw new Error("Failed to load while list content", e);
        }
        finally {
            IOUtils.closeQuietly(is);
        }

        return new HashSet<String>(lines);
    }

    @Override
    protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
        try {
            // See http://code.google.com/appengine/docs/java/runtime.html#The_Environment
            System.setProperty("com.google.appengine.runtime.environment", "Production");
            super.runChild(method, notifier);
        }
        finally {
            System.clearProperty("com.google.appengine.runtime.environment");
        }
    }
}
