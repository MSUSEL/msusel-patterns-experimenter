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
package com.gargoylesoftware.htmlunit.attachment;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.HttpWebConnectionTest;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link Attachment}.
 *
 * @version $Revision: 5658 $
 * @author Bruce Chapman
 * @author Sudhan Moghe
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class AttachmentTest extends WebTestCase {

    /**
     * Tests attachment callbacks and the contents of attachments.
     * @throws Exception if an error occurs
     */
    @Test
    public void basic() throws Exception {
        final String content1 = "<html><body>\n"
            + "<form method='POST' name='form' action='" + URL_SECOND + "'>\n"
            + "<input type='submit' value='ok'>\n"
            + "</form>\n"
            + "<a href='#' onclick='document.form.submit()'>click me</a>\n"
            + "</body></html>";
        final String content2 = "download file contents";

        final WebClient client = getWebClient();
        final List<Attachment> attachments = new ArrayList<Attachment>();
        client.setAttachmentHandler(new CollectingAttachmentHandler(attachments));

        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new NameValuePair("Content-Disposition", "attachment"));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, content1);
        conn.setResponse(URL_SECOND, content2, 200, "OK", "text/html", headers);
        client.setWebConnection(conn);
        assertEquals(0, attachments.size());

        final HtmlPage result = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = result.getAnchors().get(0);
        final Page clickResult = anchor.click();
        assertEquals(result, clickResult);
        assertEquals(1, attachments.size());
        assertTrue(HtmlPage.class.isInstance(attachments.get(0).getPage()));

        final Attachment attachment = attachments.get(0);
        final Page attachedPage = attachment.getPage();
        final WebResponse attachmentResponse = attachedPage.getWebResponse();
        final InputStream attachmentStream = attachmentResponse.getContentAsStream();
        HttpWebConnectionTest.assertEquals(new ByteArrayInputStream(content2.getBytes()), attachmentStream);
        assertEquals("text/html", attachmentResponse.getContentType());
        assertEquals(200, attachmentResponse.getStatusCode());
        assertEquals(URL_SECOND, attachmentResponse.getWebRequest().getUrl());
    }

    /**
     * Tests {@link Attachment#getSuggestedFilename()}.
     * @throws Exception if an error occurs
     */
    @Test
    public void filename() throws Exception {
        final String content = "<html>But is it really?</html>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        client.setWebConnection(conn);
        final List<Attachment> attachments = new ArrayList<Attachment>();
        client.setAttachmentHandler(new CollectingAttachmentHandler(attachments));

        final List<NameValuePair> headers1 = new ArrayList<NameValuePair>();
        headers1.add(new NameValuePair("Content-Disposition", "attachment;filename=\"hello.html\""));
        conn.setResponse(URL_FIRST, content, 200, "OK", "text/html", headers1);
        client.getPage(URL_FIRST);
        final Attachment result = attachments.get(0);
        assertEquals(result.getSuggestedFilename(), "hello.html");
        attachments.clear();

        final List<NameValuePair> headers2 = new ArrayList<NameValuePair>();
        headers2.add(new NameValuePair("Content-Disposition", "attachment; filename=hello2.html; something=else"));
        conn.setResponse(URL_SECOND, content, 200, "OK", "text/plain", headers2);
        client.getPage(URL_SECOND);
        final Attachment result2 = attachments.get(0);
        assertEquals(result2.getSuggestedFilename(), "hello2.html");
        assertEquals(content, ((TextPage) result2.getPage()).getContent());
        attachments.clear();

        final List<NameValuePair> headers3 = new ArrayList<NameValuePair>();
        headers3.add(new NameValuePair("Content-Disposition", "attachment"));
        final byte[] contentb = new byte[] {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE};
        conn.setResponse(URL_THIRD, contentb, 200, "OK", "application/x-rubbish", headers3);
        client.getPage(URL_THIRD);
        final Attachment result3 = attachments.get(0);
        final InputStream result3Stream = result3.getPage().getWebResponse().getContentAsStream();
        assertNull(result3.getSuggestedFilename());
        assertEquals(result3.getPage().getWebResponse().getContentType(), "application/x-rubbish");
        HttpWebConnectionTest.assertEquals(new ByteArrayInputStream(contentb), result3Stream);
        attachments.clear();
    }

}
