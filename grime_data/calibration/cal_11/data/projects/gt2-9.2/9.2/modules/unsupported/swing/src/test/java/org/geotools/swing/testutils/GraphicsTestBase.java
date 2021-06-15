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

package org.geotools.swing.testutils;

import java.awt.Component;
import java.awt.Window;

import javax.swing.JButton;

import org.fest.swing.core.BasicComponentFinder;
import org.fest.swing.core.ComponentFoundCondition;
import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.core.TypeMatcher;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.WindowFixture;
import org.fest.swing.timing.Pause;

import org.junit.After;
import org.junit.BeforeClass;
import static org.junit.Assert.assertNotNull;

/**
 * Base for test classes which use a FEST {@linkplain WindowFixture} to hold dialogs,
 * frames etc. 
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public abstract class GraphicsTestBase<T extends Window> {
    
    // Max waiting time for dialog display (milliseconds)
    public static final long DISPLAY_TIMEOUT = 1000;
    
    protected WindowFixture<T> windowFixture;

    /**
     * Installs the FEST repaint manager.
     */
    @BeforeClass 
    public static void baseSetUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    /**
     * Run after each test to call {@linkplain WindowFixture#cleanUp()}.
     */
    @After
    public void baseCleanup() {
        if (windowFixture != null) {
            windowFixture.cleanUp();
        }
    }
    
    /**
     * Waits up to {@linkplain #DISPLAY_TIMEOUT} milliseconds for a given
     * dialog class to be displayed. If the waiting time is exceeded an
     * assertion error is thrown.
     * 
     * @param dialogClass dialog class
     */
    protected void assertComponentDisplayed(Class<? extends Component> componentClass) {
        Pause.pause(new ComponentFoundCondition("component to be displayed", 
                BasicComponentFinder.finderWithCurrentAwtHierarchy(), 
                new TypeMatcher(componentClass, true)),
                DISPLAY_TIMEOUT);
    }

    /**
     * Gets a dialog button with the specified text.
     * 
     * @param buttonText button text
     * 
     * @return the button fixture
     */
    protected JButtonFixture getButton(final String buttonText) {
        JButtonFixture button = windowFixture.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton component) {
                return buttonText.equals(component.getText());
            }
        });
        
        assertNotNull(button);
        return button;
    }

}
