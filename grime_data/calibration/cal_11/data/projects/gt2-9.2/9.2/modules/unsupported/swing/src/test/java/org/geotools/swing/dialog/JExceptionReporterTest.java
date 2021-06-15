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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package org.geotools.swing.dialog;

import java.awt.Dialog;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.util.regex.Pattern;

import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JLabelFixture;

import org.geotools.swing.testutils.GraphicsTestBase;
import org.geotools.swing.testutils.GraphicsTestRunner;
import org.geotools.swing.testutils.WindowActivatedListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;


/**
 * Tests for the JExceptionReporter class.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class JExceptionReporterTest extends GraphicsTestBase<Dialog> {

    private static final Class<? extends Dialog> DIALOG_CLASS = 
            JExceptionReporter.ReportingDialog.class;
    
    private WindowActivatedListener listener;

    @Before
    public void setup() {
        listener = new WindowActivatedListener(DIALOG_CLASS);
        Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.WINDOW_EVENT_MASK);
    }
    
    @After
    public void cleanup() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(listener);
    }

    @Test
    public void showException() throws Exception {
        final String MSG = "Foo is not Bar";
        showDialog(new IllegalArgumentException(MSG));

        ((DialogFixture) windowFixture).requireModal();
        assertEquals("IllegalArgumentException", windowFixture.component().getTitle());
        
        JLabelFixture label = windowFixture.label();
        label.requireText(Pattern.compile(".*" + MSG + ".*"));
    }
    
    @Test
    public void showExceptionWithUserMessage() throws Exception {
        final String EXCEPTION_MSG = "Foo is not Bar";
        final String USER_MSG = "You should see this message";
        showDialog(new IllegalArgumentException(EXCEPTION_MSG), USER_MSG);
        
        assertEquals("IllegalArgumentException", windowFixture.component().getTitle());
        
        JLabelFixture label = windowFixture.label();
        label.requireText(Pattern.compile(".*" + USER_MSG + ".*"));
    }
    
    @Test
    public void emptyUserMessage() throws Exception {
        final String EXCEPTION_MSG = "You should see this message";
        final String USER_MSG = "";
        showDialog(new IllegalArgumentException(EXCEPTION_MSG), USER_MSG);
        
        assertEquals("IllegalArgumentException", windowFixture.component().getTitle());
        
        JLabelFixture label = windowFixture.label();
        label.requireText(Pattern.compile(".*" + EXCEPTION_MSG + ".*"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void nullExceptionArg() throws Exception {
        JExceptionReporter.showDialog(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void nullExceptionArg2() throws Exception {
        JExceptionReporter.showDialog(null, "User message");
    }

    private void showDialog(Exception ex) throws Exception {
        JExceptionReporter.showDialog(ex);
        assertComponentDisplayed(DIALOG_CLASS);
        
        windowFixture = listener.getFixture(DISPLAY_TIMEOUT);
    }
    
    private void showDialog(Exception ex, String msg) throws Exception {
        JExceptionReporter.showDialog(ex, msg);
        assertComponentDisplayed(DIALOG_CLASS);
        
        windowFixture = listener.getFixture(DISPLAY_TIMEOUT);
    }
    
}
