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

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebServerTestCase;

/**
 * Tests for {@link HtmlImage}.
 *
 * @version $Revision: 5805 $
 * @author Knut Johannes Dahle
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HtmlImageDownloadTest extends WebServerTestCase {
    private static final String base_file_path_ = "src/test/resources/com/gargoylesoftware/htmlunit/html";

    /**
     * Constructor.
     * @throws Exception if an exception occurs
     */
    public HtmlImageDownloadTest() throws Exception {
        startWebServer(base_file_path_);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void imageHeight() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        Assert.assertEquals("Image height", 612, htmlimage.getHeight());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void imageWidth() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        Assert.assertEquals("Image width", 879, htmlimage.getWidth());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void imageFileSize() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        Assert.assertEquals("Image filesize", 140144,
                IOUtils.toByteArray(htmlimage.getWebResponse(true).getContentAsStream()).length);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getImageReader() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        Assert.assertNotNull("ImageReader should not be null", htmlimage.getImageReader());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getImageReaderNoneSupportedImage() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        final String url = "/HtmlImageDownloadTest.html";
        htmlimage.setAttribute("src", url);
        try {
            htmlimage.getImageReader();
            Assert.fail("it was not an image!");
        }
        catch (final IOException ioe) {
            // Correct behavior
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getWebResponse() throws Exception {
        final HtmlImage htmlimage = getHtmlElementToTest("image1");
        final URL url = htmlimage.getPage().getWebResponse().getWebRequest().getUrl();
        Assert.assertNull(htmlimage.getWebResponse(false));
        final WebResponse resp = htmlimage.getWebResponse(true);
        Assert.assertNotNull(resp);
        assertEquals(url.toExternalForm(), resp.getWebRequest().getAdditionalHeaders().get("Referer"));
    }

    /**
     * Common code for the tests to load the test page and fetch the HtmlImage object.
     * @param id value of image id attribute
     * @return the found HtmlImage
     * @throws Exception if an error occurs
     */
    private HtmlImage getHtmlElementToTest(final String id) throws Exception {
        final String url = "http://localhost:" + PORT + "/HtmlImageDownloadTest.html";
        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage(url);
        return (HtmlImage) page.getElementById(id);
    }

    /**
     * {@inheritDoc}
     */
    @After
    public void tearDown() throws Exception {
        Thread.sleep(100);
        super.tearDown();
    }
}
