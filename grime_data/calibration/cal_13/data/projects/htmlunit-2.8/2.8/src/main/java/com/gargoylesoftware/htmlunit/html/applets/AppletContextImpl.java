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

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
 * {@link AppletContext} implementation for HtmlUnit.
 * @author Marc Guillemot
 * @version $Revision: 5301 $
 */
public class AppletContextImpl implements AppletContext {
    private static final Enumeration<Applet> EMPTY_ENUMERATION
        = Collections.enumeration(Collections.<Applet>emptyList());
    private HtmlPage htmlPage_;

    AppletContextImpl(final HtmlPage page) {
        htmlPage_ = page;
    }

    /**
     * {@inheritDoc}
     */
    public Applet getApplet(final String name) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Enumeration<Applet> getApplets() {
        return EMPTY_ENUMERATION;
    }

    /**
     * {@inheritDoc}
     */
    public AudioClip getAudioClip(final URL url) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public Image getImage(final URL url) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public InputStream getStream(final String key) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<String> getStreamKeys() {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void setStream(final String key, final InputStream stream) throws IOException {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void showDocument(final URL url) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void showDocument(final URL url, final String target) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void showStatus(final String status) {
        // perhaps should we move status handling to WebWindow
        // on the other side this allows "orphaned" pages to be usable
        final Window window = ((SimpleScriptable) htmlPage_.getScriptObject()).getWindow();
        window.jsxSet_status(status);
    }
}
