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
package com.gargoylesoftware.htmlunit.html;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link HtmlImage}.
 *
 * @version $Revision: 5864 $
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlImageTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void isMapClick() throws Exception {
        isMapClick("img1", false, "?0,0", "?25,30");
        isMapClick("img2", false, "", "");
        isMapClick("img3", true, "", "");
        isMapClick("img3", true, "", "");
    }

    private void isMapClick(final String imgId, final boolean samePage,
            final String urlSuffixClick, final String urlSuffixClickXY) throws Exception {

        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='http://server/foo'>\n"
            + "<img id='img1' src='foo.png' ismap>\n"
            + "<img id='img2' src='foo.png'>\n"
            + "</a>\n"
            + "<img id='img3' src='foo.png' ismap>\n"
            + "<img id='img4' src='foo.png'>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlImage img = page.getHtmlElementById(imgId);

        final Page page2 = img.click();
        Assert.assertEquals("same page after click", samePage, (page == page2));
        if (!samePage) {
            assertEquals("http://server/foo" + urlSuffixClick, page2.getWebResponse().getWebRequest().getUrl());
        }

        final Page page3 = img.click(25, 30);
        Assert.assertEquals("same page after click(25, 30)", samePage, (page == page3));
        if (!samePage) {
            assertEquals("http://server/foo" + urlSuffixClickXY, page3.getWebResponse().getWebRequest().getUrl());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void useMapClick() throws Exception {
        useMapClick(0, 0, "/");
        useMapClick(10, 10, "a.html");
        useMapClick(20, 10, "a.html");
        useMapClick(29, 10, "b.html");
        useMapClick(50, 50, "/");
    }

    /**
     * @throws Exception if the test fails
     */
    private void useMapClick(final int x, final int y, final String urlSuffix) throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<img id='myImg' src='foo.png' usemap='#map1'>\n"
            + "<map name='map1'>\n"
            + "<area href='a.html' shape='rect' coords='5,5,20,20'>\n"
            + "<area href='b.html' shape='circle' coords='25,10,10'>\n"
            + "</map>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlImage img = page.getHtmlElementById("myImg");

        final Page page2 = img.click(x, y);
        final URL url = page2.getWebResponse().getWebRequest().getUrl();
        assertTrue(url.toExternalForm(), url.toExternalForm().endsWith(urlSuffix));
    }

    /**
     * Tests circle radius of percentage value.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    public void useMapClick_CircleRadiusPercentage() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<img id='myImg' src='foo.png' usemap='#map1'>\n"
            + "<map name='map1'>\n"
            + "<area href='a.html' shape='rect' coords='5,5,20,20'>\n"
            + "<area href='b.html' shape='circle' coords='25,10,10%'>\n"
            + "</map>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final HtmlImage img = page.getHtmlElementById("myImg");
        img.click(0, 0);
    }
}
