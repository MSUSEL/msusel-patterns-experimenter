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
package com.gargoylesoftware.htmlunit.html.applets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
 * Class loader for loading applets.
 *
 * @version $Revision: 5806 $
 * @author Marc Guillemot
 */
public class AppletClassLoader extends ClassLoader {

    private static final Log LOG = LogFactory.getLog(AppletClassLoader.class);

    private final Set<String> definedClasses_ = new HashSet<String>();
    private final Map<String, JarFile> jarFiles_ = new HashMap<String, JarFile>();

    /**
     * The constructor.
     */
    public AppletClassLoader() {
        super(AppletClassLoader.class.getClassLoader());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class< ? > loadClass(final String name) throws ClassNotFoundException {
        if (!definedClasses_.contains(name) && jarFiles_.containsKey(name)) {
            defineClass(name);
        }
        return super.loadClass(name);
    }

    private void defineClass(final String name) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Defining class " + name);
        }
        final String classFileName = name.replace('.', '/') + ".class";
        final JarFile jarFile = jarFiles_.get(name);
        try {
            final InputStream is = jarFile.getInputStream(jarFile.getEntry(classFileName));
            final byte[] bytes = IOUtils.toByteArray(is);
            defineClass(name, bytes, 0, bytes.length);
            definedClasses_.add(name);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds the content of specified WebResponse to the classpath for the applet.
     * @param webResponse the web response
     * @throws IOException in case of problem working with the response content
     */
    public void addToClassPath(final WebResponse webResponse) throws IOException {
        // normally content type should be "application/java-archive"
        // but it works when it is not the case
        // TODO: handle the case where it is not a jar archive
        readClassesFromJar(webResponse);
    }

    private void readClassesFromJar(final WebResponse webResponse) throws IOException {
        final File tmpFile = File.createTempFile("HtmlUnit", "jar");
        tmpFile.deleteOnExit();
        final OutputStream output = new FileOutputStream(tmpFile);
        IOUtils.copy(webResponse.getContentAsStream(), output);
        output.close();
        final JarFile jarFile = new JarFile(tmpFile);
        final Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            final String name = entry.getName();
            if (name.endsWith(".class")) {
                final String className = name.replace('/', '.').substring(0, name.length() - 6);
                jarFiles_.put(className, jarFile);
                if (LOG.isTraceEnabled()) {
                    LOG.trace("Jar entry: " + className);
                }
            }
        }
    }

    /**
     * Reads the class name from the bytes of a .class file.
     * @param webResponse response containing the bytes the class
     * @return the full class name
     */
    public static String readClassName(final WebResponse webResponse) {
        try {
            return readClassName(IOUtils.toByteArray(webResponse.getContentAsStream()));
        }
        catch (final IOException e) {
            return null;
        }
    }

    /**
     * Reads the class name from the bytes of a .class file.
     * @param bytes the class bytes
     * @return the full class name
     */
    public static String readClassName(final byte[] bytes) {
        // seems to work ;-)
        final StringBuilder sb = new StringBuilder();
        int i = 16; // skip 16 first bytes
        byte b = bytes[16];
        while (b != 7) {
            sb.append((char) b);
            b = bytes[++i];
        }
        return sb.toString().replace('/', '.');
    }
}
