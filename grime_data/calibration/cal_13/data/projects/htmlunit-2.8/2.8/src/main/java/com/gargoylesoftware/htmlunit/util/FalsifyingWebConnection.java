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
package com.gargoylesoftware.htmlunit.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;

/**
 * Extension of {@link WebConnectionWrapper} providing facility methods to deliver something other than
 * what the wrapped connection would deliver.
 *
 * @version $Revision: 5679 $
 * @author Marc Guillemot
 */
public abstract class FalsifyingWebConnection extends WebConnectionWrapper {

    /**
     * Constructs a WebConnection object wrapping provided WebConnection.
     * @param webConnection the webConnection that does the real work
     * @throws IllegalArgumentException if the connection is <code>null</code>
     */
    public FalsifyingWebConnection(final WebConnection webConnection) throws IllegalArgumentException {
        super(webConnection);
    }

    /**
     * Constructs an instance and places itself as connection of the WebClient.
     * @param webClient the WebClient which WebConnection should be wrapped
     * @throws IllegalArgumentException if the WebClient is <code>null</code>
     */
    public FalsifyingWebConnection(final WebClient webClient) throws IllegalArgumentException {
        super(webClient);
    }

    /**
     * Delivers the content for an alternate URL as if it comes from the requested URL.
     * @param webRequest the original web request
     * @param url the URL from which the content should be retrieved
     * @return the response
     * @throws IOException if a problem occurred
     */
    protected WebResponse deliverFromAlternateUrl(final WebRequest webRequest, final URL url)
        throws IOException {
        final URL originalUrl = webRequest.getUrl();
        webRequest.setUrl(url);
        final WebResponse resp = super.getResponse(webRequest);
        resp.getWebRequest().setUrl(originalUrl);
        return resp;
    }

    /**
     * Builds a WebResponse with new content, preserving all other information.
     * @param wr the web response to adapt
     * @param newContent the new content to place in the response
     * @return a web response with the new content
     * @throws IOException if an encoding problem occurred
     */
    protected WebResponse replaceContent(final WebResponse wr, final String newContent) throws IOException {
        final byte[] body = newContent.getBytes(wr.getContentCharset());
        final WebResponseData wrd = new WebResponseData(body, wr.getStatusCode(), wr.getStatusMessage(),
            wr.getResponseHeaders());
        return new WebResponse(wrd, wr.getWebRequest().getUrl(), wr.getWebRequest().getHttpMethod(),
                wr.getLoadTime());
    }

    /**
     * Creates a faked WebResponse for the request with the provided content.
     * @param wr the web request for which a response should be created
     * @param content the content to place in the response
     * @param contentType the content type of the response
     * @return a web response with the provided content
     * @throws IOException if an encoding problem occurred
     */
    protected WebResponse createWebResponse(final WebRequest wr, final String content,
            final String contentType) throws IOException {
        return createWebResponse(wr, content, contentType, 200, "OK");
    }

    /**
     * Creates a faked WebResponse for the request with the provided content.
     * @param wr the web request for which a response should be created
     * @param content the content to place in the response
     * @param contentType the content type of the response
     * @param responseCode the HTTP code for the response
     * @param responseMessage the HTTP message for the response
     * @return a web response with the provided content
     * @throws IOException if an encoding problem occurred
     */
    protected WebResponse createWebResponse(final WebRequest wr, final String content,
            final String contentType, final int responseCode, final String responseMessage) throws IOException {
        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        final String encoding = "UTF-8";
        headers.add(new NameValuePair("content-type", contentType + "; charset=" + encoding));
        final byte[] body = content.getBytes(encoding);
        final WebResponseData wrd = new WebResponseData(body, responseCode, responseMessage, headers);
        return new WebResponse(wrd, wr.getUrl(), wr.getHttpMethod(), 0);
    }
}
