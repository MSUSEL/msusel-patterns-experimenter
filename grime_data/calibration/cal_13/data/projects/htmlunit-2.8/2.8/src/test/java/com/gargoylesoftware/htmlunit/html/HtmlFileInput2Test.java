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
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlFileInput}.
 *
 * @version $Revision: 5911 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlFileInput2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void contentType() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/upload1", Upload1Servlet.class);
        servlets.put("/upload2", ContentTypeUpload2Servlet.class);
        startWebServer("./", new String[0], servlets);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/upload1");
        String path = getClass().getClassLoader().getResource("realm.properties").toExternalForm();
        if (driver instanceof InternetExplorerDriver) {
            path = path.substring(path.indexOf('/') + 1).replace('/', '\\');
        }
        driver.findElement(By.name("myInput")).sendKeys(path);
        driver.findElement(By.id("mySubmit")).click();
        assertTrue(driver.getPageSource().contains("CONTENT_TYPE:application/octet-stream"));
    }

    /**
     * Servlet for '/upload1'.
     */
    public static class Upload1Servlet extends HttpServlet {

        private static final long serialVersionUID = 6693252829875297263L;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            response.getWriter().write("<html>"
                + "<body><form action='upload2' method='post' enctype='multipart/form-data'>\n"
                + "Name: <input name='myInput' type='file'><br>\n"
                + "Name 2 (should stay empty): <input name='myInput2' type='file'><br>\n"
                + "<input type='submit' value='Upload' id='mySubmit'>\n"
                + "</form></body></html>\n");
        }
    }

    /**
     * Servlet for '/upload2'.
     */
    public static class ContentTypeUpload2Servlet extends HttpServlet {

        private static final long serialVersionUID = 9040072936161269041L;

        /**
         * {@inheritDoc}
         */
        @Override
        @SuppressWarnings("unchecked")
        protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            final Writer writer = response.getWriter();
            if (ServletFileUpload.isMultipartContent(request)) {
                try {
                    final ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
                    for (final FileItem item : (List<FileItem>) upload.parseRequest(request)) {
                        if ("myInput".equals(item.getFieldName())) {
                            writer.write("CONTENT_TYPE:" + item.getContentType());
                        }
                    }
                }
                catch (final FileUploadBase.SizeLimitExceededException e) {
                    writer.write("SizeLimitExceeded");
                }
                catch (final Exception e) {
                    writer.write("error");
                }
            }
            writer.close();
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void boundary() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/upload1", Upload1Servlet.class);
        servlets.put("/upload2", PrintRequestServlet.class);
        startWebServer("./", new String[0], servlets);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/upload1");
        String path = getClass().getClassLoader().getResource("realm.properties").toExternalForm();
        if (driver instanceof InternetExplorerDriver) {
            path = path.substring(path.indexOf('/') + 1).replace('/', '\\');
        }
        driver.findElement(By.name("myInput")).sendKeys(path);
        driver.findElement(By.id("mySubmit")).click();
        assertTrue(driver.getPageSource().contains("-----------------------------"));
    }

    /**
     * Prints request content to the response.
     */
    public static class PrintRequestServlet extends HttpServlet {

        private static final long serialVersionUID = 40153839021613234L;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            final Writer writer = response.getWriter();
            final InputStream in = request.getInputStream();
            int i;
            while ((i = in.read()) != -1) {
                writer.write(i);
            }
            writer.close();
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void contentTypeHeader() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/upload1", Upload1Servlet.class);
        servlets.put("/upload2", ContentTypeHeaderUpload2Servlet.class);
        startWebServer("./", new String[0], servlets);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/upload1");
        String path = getClass().getClassLoader().getResource("realm.properties").toExternalForm();
        if (driver instanceof InternetExplorerDriver) {
            path = path.substring(path.indexOf('/') + 1).replace('/', '\\');
        }
        driver.findElement(By.name("myInput")).sendKeys(path);
        driver.findElement(By.id("mySubmit")).click();
        final String source = driver.getPageSource();
        assertTrue(source.contains("CONTENT_TYPE:"));
        assertFalse(source.contains("charset"));
    }

    /**
     * Servlet for '/upload2'.
     */
    public static class ContentTypeHeaderUpload2Servlet extends HttpServlet {

        private static final long serialVersionUID = 4063742744610158029L;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            final Writer writer = response.getWriter();
            writer.write("CONTENT_TYPE:" + request.getContentType());
            writer.close();
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void empty() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/upload1", Upload1Servlet.class);
        servlets.put("/upload2", PrintRequestServlet.class);
        startWebServer("./", new String[0], servlets);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/upload1");
        driver.findElement(By.id("mySubmit")).click();
        assertTrue(driver.getPageSource().contains("filename=\"\""));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void chunked() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/upload1", Upload1Servlet.class);
        servlets.put("/upload2", ChunkedUpload2Servlet.class);
        startWebServer("./", new String[0], servlets);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/upload1");
        driver.findElement(By.id("mySubmit")).click();
        assertFalse(driver.getPageSource().contains("chunked"));
    }

    /**
     * Servlet for '/upload2'.
     */
    public static class ChunkedUpload2Servlet extends HttpServlet {

        private static final long serialVersionUID = -980728270976349919L;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            final Writer writer = response.getWriter();
            writer.write("TRANSFER_ENCODING:" + request.getHeader("TRANSFER-ENCODING"));
            writer.close();
        }
    }
}
