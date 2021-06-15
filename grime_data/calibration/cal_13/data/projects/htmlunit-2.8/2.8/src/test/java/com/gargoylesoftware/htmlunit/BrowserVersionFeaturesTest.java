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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

/**
 * Tests for {@link BrowserVersionFeatures}.
 *
 * @version $Revision: 5925 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class BrowserVersionFeaturesTest extends WebTestCase {

    /**
     * Test of alphabetical order.
     */
    @Test
    @Browsers(Browser.NONE)
    public void lexicographicOrder() {
        String lastFeatureName = null;
        for (final BrowserVersionFeatures feature : BrowserVersionFeatures.values()) {
            final String featureName = feature.name();
            if (lastFeatureName != null && featureName.compareTo(lastFeatureName) < 1) {
                fail("BrowserVersionFeatures.java: \""
                    + featureName + "\" should be before \"" + lastFeatureName + '"');
            }
            lastFeatureName = featureName;
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void lexicographicOrder_properties() throws Exception {
        final String path = "com/gargoylesoftware/htmlunit/javascript/configuration/"
            + getBrowserVersion().getNickname() + ".properties";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String lastFeatureName = null;
            String line;
            while ((line = reader.readLine()) != null) {
                final String featureName = line.trim();
                if (lastFeatureName != null && featureName.compareTo(lastFeatureName) < 1) {
                    fail(path + ": \"" + featureName + "\" should be before \"" + lastFeatureName + '"');
                }
                lastFeatureName = featureName;
            }
        }
        finally {
            reader.close();
        }
    }
}
