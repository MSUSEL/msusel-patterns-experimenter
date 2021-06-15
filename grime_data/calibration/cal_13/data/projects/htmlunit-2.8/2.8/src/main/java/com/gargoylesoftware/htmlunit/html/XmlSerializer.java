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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageReader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.Page;

/**
 * Utility to handle conversion from HTML code to XML string.
 * @version $Revision: 5776 $
 * @author Ahmed Ashour
 */
class XmlSerializer {

    private final StringBuilder buffer_ = new StringBuilder();
    private final StringBuilder indent_ = new StringBuilder();
    private File outputDir_;

    public void save(final HtmlPage page, final File file) throws IOException {
        String fileName = file.getName();
        if (!fileName.endsWith(".htm") && !fileName.endsWith(".html")) {
            fileName += ".html";
        }
        final File outputFile = new File(file.getParentFile(), fileName);
        if (outputFile.exists()) {
            throw new IOException("File already exists: " + outputFile);
        }
        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        outputDir_ = new File(file.getParentFile(), fileName);
        FileUtils.writeStringToFile(outputFile, asXml(page.getDocumentElement()));
    }

    /**
     * Converts an HTML element to XML.
     * @param node a node
     * @return the text representation according to the setting of this serializer
     * @throws IOException in case of problem saving resources
     */
    public String asXml(final HtmlElement node) throws IOException {
        buffer_.setLength(0);
        indent_.setLength(0);
        String charsetName = null;
        if (node.getPage() instanceof HtmlPage) {
            charsetName = node.getPage().getPageEncoding();
        }
        if (charsetName != null && node instanceof HtmlHtml) {
            buffer_.append("<?xml version=\"1.0\" encoding=\"").append(charsetName).append("\"?>").append('\n');
        }
        printXml(node);
        final String response = buffer_.toString();
        buffer_.setLength(0);
        return response;
    }

    protected void printXml(final DomElement node) throws IOException {
        if (!isExcluded(node)) {
            final boolean hasChildren = node.getFirstChild() != null;
            buffer_.append(indent_).append('<');
            printOpeningTag(node);

            if (!hasChildren && !isEmptyXmlTagExpanded(node)) {
                buffer_.append("/>").append('\n');
            }
            else {
                buffer_.append(">").append('\n');
                for (DomNode child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
                    indent_.append("  ");
                    if (child instanceof DomElement) {
                        printXml((DomElement) child);
                    }
                    else {
                        buffer_.append(child);
                    }
                    indent_.setLength(indent_.length() - 2);
                }
                buffer_.append(indent_).append("</").append(node.getTagName()).append('>').append('\n');
            }
        }
    }

    protected boolean isEmptyXmlTagExpanded(final DomNode node) {
        return node instanceof HtmlDivision || node instanceof HtmlInlineFrame || node instanceof HtmlOrderedList
            || node instanceof HtmlScript || node instanceof HtmlSpan || node instanceof HtmlStyle
            || node instanceof HtmlTable || node instanceof HtmlTitle || node instanceof HtmlUnorderedList;
    }

    /**
     * Prints the content between "&lt;" and "&gt;" (or "/&gt;") in the output of the tag name
     * and its attributes in XML format.
     * @param node the node whose opening tag is to be printed
     * @throws IOException in case of problem saving resources
     */
    protected void printOpeningTag(final DomElement node) throws IOException {
        buffer_.append(node.getTagName());
        final Map<String, DomAttr> attributes;
        if (node instanceof HtmlImage) {
            attributes = getAttributesFor((HtmlImage) node);
        }
        else if (node instanceof HtmlLink) {
            attributes = getAttributesFor((HtmlLink) node);
        }
        else if (node instanceof BaseFrame) {
            attributes = getAttributesFor((BaseFrame) node);
        }
        else {
            attributes = node.getAttributesMap();
        }

        for (final String name : attributes.keySet()) {
            buffer_.append(" ");
            buffer_.append(name);
            buffer_.append("=\"");
            buffer_.append(StringEscapeUtils.escapeXml(attributes.get(name).getNodeValue()));
            buffer_.append("\"");
        }
    }

    private Map<String, DomAttr> getAttributesFor(final BaseFrame frame) throws IOException {
        final Map<String, DomAttr> map = createAttributesCopyWithClonedAttribute(frame, "src");
        final DomAttr srcAttr = map.get("src");

        final Page enclosedPage = frame.getEnclosedPage();
        final String suffix = getFileExtension(enclosedPage);
        final File file = createFile(srcAttr.getValue(), "." + suffix);

        if (enclosedPage instanceof HtmlPage) {
            file.delete(); // TODO: refactor as it is stupid to create empty file at one place
            // and then to complain that it already exists
            ((HtmlPage) enclosedPage).save(file);
        }
        else {
            final InputStream is = enclosedPage.getWebResponse().getContentAsStream();
            final FileOutputStream fos = new FileOutputStream(file);
            IOUtils.copyLarge(is, fos);
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(fos);
        }

        srcAttr.setValue(file.getParentFile().getName() + "/" + file.getName());
        return map;
    }

    private String getFileExtension(final Page enclosedPage) {
        if (enclosedPage instanceof HtmlPage) {
            return "html";
        }

        final URL url = enclosedPage.getUrl();
        if (url.getPath().contains(".")) {
            return StringUtils.substringAfterLast(url.getPath(), ".");
        }

        return ".unknown";
    }

    protected Map<String, DomAttr> getAttributesFor(final HtmlLink link) throws IOException {
        final Map<String, DomAttr> map = createAttributesCopyWithClonedAttribute(link, "href");
        final DomAttr hrefAttr = map.get("href");
        final File file = createFile(hrefAttr.getValue(), ".css");
        FileUtils.writeStringToFile(file, link.getWebResponse(true).getContentAsString());
        hrefAttr.setValue(outputDir_.getName() + File.separatorChar + file.getName());

        return map;
    }

    protected Map<String, DomAttr> getAttributesFor(final HtmlImage image) throws IOException {
        final Map<String, DomAttr> map = createAttributesCopyWithClonedAttribute(image, "src");
        final DomAttr srcAttr = map.get("src");
        final ImageReader reader = image.getImageReader();
        final File file = createFile(srcAttr.getValue(), "." + reader.getFormatName());
        image.saveAs(file);
        outputDir_.mkdirs();
        final String valueOnFileSystem = outputDir_.getName() + File.separatorChar + file.getName();
        srcAttr.setValue(valueOnFileSystem); // this is the clone attribute node, not the original one of the page

        return map;
    }

    private Map<String, DomAttr> createAttributesCopyWithClonedAttribute(final HtmlElement elt, final String attrName) {
        final Map<String, DomAttr> newMap = new HashMap<String, DomAttr>(elt.getAttributesMap());

        // clone the specified element
        final DomAttr attr = newMap.get(attrName);
        final DomAttr clonedAttr = new DomAttr(attr.getPage(), attr.getNamespaceURI(),
            attr.getQualifiedName(), attr.getValue(), attr.getSpecified());

        newMap.put(attrName, clonedAttr);

        return newMap;
    }

    protected boolean isExcluded(final DomElement element) {
        return element instanceof HtmlScript;
    }

    /**
     * Computes the best file to save the response to the given URL.
     * @param url the requested URL
     * @param extension the preferred extension
     * @return the file to create
     * @throws IOException if a problem occurs creating the file
     */
    private File createFile(final String url, final String extension) throws IOException {
        String name = url.replaceFirst("/$", "").replaceAll(".*/", "");
        name = StringUtils.substringBefore(name, "?"); // remove query
        name = StringUtils.substringBefore(name, ";"); // remove additional info
        if (!name.endsWith(extension)) {
            name += extension;
        }
        int counter = 0;
        while (true) {
            final String fileName;
            if (counter != 0) {
                fileName = StringUtils.substringBeforeLast(name, ".")
                    + "_" + counter + "." + StringUtils.substringAfterLast(name, ".");
            }
            else {
                fileName = name;
            }
            outputDir_.mkdirs();
            final File f = new File(outputDir_, fileName);
            if (f.createNewFile()) {
                return f;
            }
            counter++;
        }
    }
}
