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
package com.ivata.groupware.web.format;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import javax.xml.transform.TransformerConfigurationException;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.tidy.Tidy;

import com.ivata.mask.util.StringHandling;
import com.ivata.mask.web.format.CharacterEntityFormat;
import com.ivata.mask.web.format.HTMLFormat;
import com.ivata.mask.web.format.HTMLFormatter;

/*
 * Copyright (c) 2001 - 2005 ivata limited.
 * All rights reserved.
 * -----------------------------------------------------------------------------
 * ivata groupware may be redistributed under the GNU General Public
 * License as published by the Free Software Foundation;
 * version 2 of the License.
 *
 * These programs are free software; you can redistribute them and/or
 * modify them under the terms of the GNU General Public License
 * as published by the Free Software Foundation; version 2 of the License.
 *
 * These programs are distributed in the hope that they will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU General Public License in the file LICENSE.txt for more
 * details.
 *
 * If you would like a copy of the GNU General Public License write to
 *
 * Free Software Foundation, Inc.
 * 59 Temple Place - Suite 330
 * Boston, MA 02111-1307, USA.
 *
 *
 * To arrange commercial support and licensing, contact ivata at
 *                  http://www.ivata.com/contact.jsp
 * -----------------------------------------------------------------------------
 * $Log: SanitizerFormat.java,v $
 * Revision 1.4  2005/04/30 13:07:31  colinmacleod
 * Added EntityResolver so you don't need an
 * internet connection.
 *
 * Revision 1.3  2005/04/10 20:32:00  colinmacleod
 * Added new themes.
 * Changed id type to String.
 * Changed i tag to em and b tag to strong.
 * Improved PicoContainerFactory with NanoContainer scripts.
 *
 * Revision 1.2  2005/04/09 17:19:42  colinmacleod
 * Changed copyright text to GPL v2 explicitly.
 *
 * Revision 1.1.1.1  2005/03/10 17:49:53  colinmacleod
 * Restructured ivata op around Hibernate/PicoContainer.
 * Renamed ivata groupware.
 *
 * Revision 1.7  2004/11/03 15:54:32  colinmacleod
 * Changed todo comments to TODO: all caps.
 *
 * Revision 1.6  2004/09/30 14:58:06  colinmacleod
 * Bugfixes for documents with no surrounding tags.
 * Added log4j.
 *
 * Revision 1.5  2004/08/01 11:54:07  colinmacleod
 * Removed ivata groupware custom HTML parser in favor of JTidy.
 *
 * Revision 1.4  2004/07/13 19:48:08  colinmacleod
 * Moved project to POJOs from EJBs.
 * Applied PicoContainer to services layer (replacing session EJBs).
 * Applied Hibernate to persistence layer (replacing entity EJBs).
 *
 * Revision 1.3  2004/03/21 21:16:27  colinmacleod
 * Shortened name to ivata op.
 *
 * Revision 1.2  2004/02/01 22:07:30  colinmacleod
 * Added full names to author tags
 *
 * Revision 1.1.1.1  2004/01/27 20:58:30  colinmacleod
 * Moved ivata openportal to SourceForge..
 *
 * Revision 1.2  2003/10/15 14:15:36  colin
 * fixing for XDoclet
 *
 * Revision 1.2  2003/05/06 13:42:25  peter
 * added embedded IMG attachments functionality
 *
 * Revision 1.1  2003/02/24 19:33:33  colin
 * moved to jsp
 *
 * Revision 1.2  2003/02/04 17:43:46  colin
 * copyright notice
 *
 * Revision 1.1  2002/08/10 21:17:48  colin
 * first version of HTML sanitizer/parser to clean up HTML code
 */

/**
 * <p>
 * This class uses the parser defined in {@linkcom.ivata.groupware.web.parser} to
 * tidy up the HTML and posibly convert it to text-only.
 * </p>
 *
 * @since 2002-08-10
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 * @see com.ivata.groupware.web.parser
 */
public class SanitizerFormat implements HTMLFormat {
    /**
     * <p>
     * <strong>Log4J</strong> logger.
     * </p>
     */
    private static Logger log = Logger.getLogger(SanitizerFormat.class);
    /**
     * <p>Used to convert character entities back again in text mode.</p>
     */
    private CharacterEntityFormat characterEntities = new CharacterEntityFormat();
    /**
     * <p>
     * Stores whether or not <code>format</code> should return just plain
     * text. If <code>true</code>, only text is returned, otherwise formatted
     * HTML is returned.
     * </p>
     */
    private boolean formattedText = false;
    /**
     * <p>Used to convert character entities back again in text mode.</p>
     */
    private HTMLFormatter formatter = new HTMLFormatter();

    /**
     * <p>
     * Contains uri to prepend when src attribute of an image begins with
     * <em>cid: </em>- it's and embedded attachment
     * </p>
     */
    private String imageUri = null;

    /**
     * <p>
     * The information to append to rewritten uris of embedded attachments
     * </p>
     */
    private String imageUriAppend = null;

    /**
     * <p>
     * If <code>true</code> then only the contents of the body tag are returned.
     * </p>
     */
    private boolean onlyBodyContents = false;

    /**
     * <p>
     * Stores name of the source or file to output for debugging.
     * </p>
     */
    private String sourceName = "user input";

    /**
     * <p>Remember whether or not we're at the start of a line in a text file.</p>
     */
    private boolean textAtStartOfLine = true;

    /**
     * <p>Remember how many newlines we've made in a text file.</p>
     */
    private int textNewLineCount = 0;

    /**
     * <p>
     * Stores whether or not <code>format</code> should return just plain
     * text with line feeds and converted horizonal rule. If <code>true</code>,
     * fomratted text is returned, otherwise formatted
     * HTML is returned.
     * </p>
     */
    private boolean textOnly = false;

    /**
     * <p>
     * This tidy instance does all the hard work.
     * </p>
     */
    private Tidy tidy = new Tidy();

    /**
     * <p>
     * Default constructor.
     * </p>
     */
    public SanitizerFormat() {
        tidy.setBreakBeforeBR(true);
        tidy.setIndentContent(true);
        tidy.setMakeClean(true);
        tidy.setOnlyErrors(true);
        tidy.setQuiet(true);
        tidy.setUpperCaseAttrs(false);
        tidy.setUpperCaseTags(false);
        tidy.setXmlOut(true);
        // these objects are used to convert character entities back again
        characterEntities.setReverse(true);
        formatter.add(characterEntities);
    }

    /**
     * <p>Convert an closing tag element to text.</p>
     *
     * @param element element which is closed.
     * @param buffer <code>PrintWriter</code> to send the results to.
     */
    private void addCloseElementAsText(final Element element,
            final StringBuffer buffer) {
        // follow table cells with a tab
        if(element.getTagName().equals("A")) {
            // see what the link was
            if(element.hasAttribute("href")) {
                notTextNewLine();
                buffer.append(" (" + element.getAttribute("href") + ")");
            }
        } else if(element.getTagName().equals("HR") ||
                  element.getTagName().equals("H1") ||
                  element.getTagName().equals("H2") ||
                  element.getTagName().equals("H3") ||
                  element.getTagName().equals("H4") ||
                  element.getTagName().equals("H5") ||
                  element.getTagName().equals("H6")) {
            addTextNewLine(buffer);
            buffer.append("____________________________________________________________\n");
        } else if(element.getTagName().equals("B") ||
                  element.getTagName().equals("BIG") ||
                  element.getTagName().equals("EM") ||
                  element.getTagName().equals("I") ||
                  element.getTagName().equals("STRONG") ||
                  element.getTagName().equals("U")) {
            notTextNewLine();
            buffer.append("__");
        }else if(element.getTagName().equals("TR") ||
                 element.getTagName().equals("TD") ||
                 element.getTagName().equals("TH") ||
                 element.getTagName().equals("P") ||
                 element.getTagName().equals("BR") ||
                 element.getTagName().equals("CITE") ||
                 element.getTagName().equals("LI") ||
                 element.getTagName().equals("BLOCKQUOTE")) {
            addTextNewLine(buffer);
        }
    }


    /**
     * <p>Convert an open tag element to text.</p>
     *
     * @param element element which is opened.
     * @param buffer <code>PrintWriter</code> to send the results to.
     */
    private void addOpenElementAsText(final Element element,
            final StringBuffer buffer) {
        // precede some tags with a character in read-only mode
        if(element.getTagName().equals("BLOCKQUOTE") ||
           element.getTagName().equals("CITE") ||
           element.getTagName().equals("H1") ||
           element.getTagName().equals("H2") ||
           element.getTagName().equals("H3") ||
           element.getTagName().equals("H4") ||
           element.getTagName().equals("H5") ||
           element.getTagName().equals("H6") ||
           element.getTagName().equals("OL") ||
           element.getTagName().equals("UL") ||
           element.getTagName().equals("TABLE") ||
           element.getTagName().equals("P") ||
           element.getTagName().equals("CITE") ||
           element.getTagName().equals("BLOCKQUOTE")) {
            addTextNewLine(buffer);
        } else if(element.getTagName().equals("B") ||
                  element.getTagName().equals("BIG") ||
                  element.getTagName().equals("EM") ||
                  element.getTagName().equals("I") ||
                  element.getTagName().equals("STRONG") ||
                  element.getTagName().equals("U")) {
            notTextNewLine();
            buffer.append("__");
        } else if(element.getTagName().equals("LI")) {
            // TODO: work buffer somehow if it is ol or ul
            addTextNewLine(buffer);
            notTextNewLine();
            buffer.append(" * ");
        } else if(element.getTagName().equals("IMG")) {
            // see if there is an alternate text for this image
            if(element.hasAttribute("alt")) {
                notTextNewLine();
                buffer.append(formatter.format(element.getAttribute("alt").trim()));
            } else if(element.hasAttribute("title")) {
                notTextNewLine();
                buffer.append(formatter.format(element.getAttribute("title").trim()));
            }
        }
    }


    /**
     * <p>Write a text new line.</p>
     */
    private void addTextNewLine(final StringBuffer buffer) {
        if(textNewLineCount < 2) {
            textAtStartOfLine = true;
            buffer.append("\n");
            ++textNewLineCount;
        }
    }

    /**
     * <p>Add a string representation of the given element to the buffer.</p>
     *
     * @param node node to add, and to add all of the children for.
     * @param onlyChildren if <code>true</code> then only the children of this
     * node are added, otherwise the node itself is added too.
     * @param buffer <code>PrintWriter</code> to send the results to.
     * @param textOnly if <code>true</code>, only text is returned, otherwise
     * formatted HTML is returned.
     */
    private void addToBuffer(final Node node,
            final StringBuffer buffer) throws IOException {
        Element element = null;
        if(formattedText && Element.class.isInstance(node)) {
            element = (Element) node;
            NamedNodeMap attributes = element.getAttributes();
            addOpenElementAsText(element, buffer);
        } else if(formattedText && Comment.class.isInstance(node)) {
            // ignore comments in text mode
        } else if(formattedText && EntityReference.class.isInstance(node)) {
            EntityReference entity = (EntityReference) node;
            buffer.append("&");
            buffer.append(entity.getNodeName());
            buffer.append(";");
        } else if(Text.class.isInstance(node)) {
            Text text = (Text) node;
            String data = text.getData();
            StringBuffer dataReformatted = new StringBuffer();
            if(data != null) {
                // strip buffer any funny characters and double spaces
                int length = data.length();
                boolean lastWasSpace = false;
                boolean atStart = textAtStartOfLine;
                for(int index = 0; index < length; ++index) {
                    // newlines, carriage returns and tabs are all spaces now
                    if((data.charAt(index) == '\n') ||
                       (data.charAt(index) == '\r') ||
                       (data.charAt(index) == ' ') ||
                       (data.charAt(index) == '\t')) {
                        // ignore double spaces
                        if(!lastWasSpace) {
                            lastWasSpace = true;
                            if(!textAtStartOfLine) {
                                dataReformatted.append(' ');
                            }
                        }
                    } else {
                        lastWasSpace = false;
                        atStart = false;
                        dataReformatted.append(data.charAt(index));
                    }
                }
                if(!(data = dataReformatted.toString()).equals("")) {
                    buffer.append(formatter.format(data));
                    notTextNewLine();
                }
            }
        } else {
            String value = node.getNodeValue();
            if(!StringHandling.isNullOrEmpty(value)) {
                notTextNewLine();
                buffer.append(value);
            }
        }

        // if that doesn't work, try the children
        if(node.hasChildNodes() &&
           ((element == null) ||
            // these are the tags to ignore the contents of in text mode
            (!element.getTagName().equals("APPLET") &&
             !element.getTagName().equals("EMBED") &&
             !element.getTagName().equals("SCRIPT")))) {
            NodeList children = node.getChildNodes();
            for(int index = 0; index < children.getLength(); ++index) {
                Node nextChild = children.item(index);
                addToBuffer(nextChild, buffer);
            }
        }
        // in text only mode, certain elements are followed by a special character
        if(element != null) {
            addCloseElementAsText(element, buffer);
        }

    }

    /**
     * <p>
     * Internal method which converts the <strong>HTML</strong> into plain text.
     * </p>
     *
     * @param element Root <strong>HTML</strong> element to be converted.
     * @return Plain text matching the <strong>HTML</strong>.
     */
    private String convertToText(final Document document) {
        StringBuffer buffer = new StringBuffer();
        try {
            addToBuffer(document, buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
        return buffer.toString();
    }

    /**
     * <p>
     * Format the string given in <code>hTMLText</code> and clean up the
     * syntax of the HTML.
     * </p>
     *
     * @param hTMLTextParam
     *            the text to truncate.
     * @throws TransformerConfigurationException
     */
    public String format(final String hTMLTextParam) {
        if (hTMLTextParam == null) {
            if (log.isDebugEnabled()) {
                log.debug("Null input received - returning null.");
            }
            return null;
        }
        if (hTMLTextParam.trim().length() == 0) {
            if (log.isDebugEnabled()) {
                log.debug("Empty input received - returning input unchanged.");
            }
            return hTMLTextParam;
        }
        // basic sanity check - if there is no HTML tag, assume we only have
        // body content.
        String lowerCaseText = hTMLTextParam.toLowerCase();
        boolean hasHTMLTag = lowerCaseText.indexOf("<HTML") != -1;
        String hTMLText;
        if (!hasHTMLTag) {
            if (log.isDebugEnabled()) {
                log.debug("No HTML tag found - surrounding everything with HTML and BODY.");
            }
            StringBuffer newHTMLText = new StringBuffer();
            newHTMLText.append("<HTML><head><title></title></head><body>");
            newHTMLText.append(hTMLTextParam);
            newHTMLText.append("</body></HTML>");
            hTMLText = newHTMLText.toString();
        } else {
            hTMLText = hTMLTextParam;
        }


        // TOTAL HACK to convert JSP tags to entities
        if (hTMLText.indexOf("<%") != -1) {
            hTMLText = hTMLText.replaceAll("<%", "&lt;%");
        }
        if (hTMLText.indexOf("%>") != -1) {
            hTMLText = hTMLText.replaceAll("%>", "%&gt;");
        }

        InputStream inStream = new ByteArrayInputStream(hTMLText.getBytes());
        Document document = tidy.parseDOM(inStream, null);
        if (textOnly) {
            if (log.isDebugEnabled()) {
                log.debug("Converting document to text.");
            }
            return convertToText(document);
        } else {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            tidy.pprint(document, outStream);

            if (onlyBodyContents
                    && (outStream.toString().trim().length() > 0)) {
                SAXReader saxReader = new SAXReader();
                String text = outStream.toString();
                // EVEN BIGGER HACK to remove previous over-zealous dash replacement
                if (text.indexOf("&minus;") != -1) {
                    text = text.replaceAll("&minus;", "-");
                }
                inStream = new ByteArrayInputStream(text.getBytes());
                org.dom4j.Document dom4jDocument;
                try {
                    dom4jDocument = saxReader.read(inStream);
                } catch (DocumentException e) {
                    log.error("Error ("
                            + e.getClass().getName()
                            + ") reading the document back in after Tidy:\n"
                            + outStream.toString(),
                            e);
                    throw new RuntimeException(e);
                }
                org.dom4j.Element rootElement = dom4jDocument.getRootElement();
                org.dom4j.Element bodyElement = rootElement.element("body");
                if (bodyElement == null) {
                    return null;
                }
                outStream = new ByteArrayOutputStream();
                XMLWriter writer;
                try {
                    writer = new XMLWriter(outStream,
                            new org.dom4j.io.OutputFormat("", true));
                } catch (UnsupportedEncodingException e) {
                    log.error("Error ("
                            + e.getClass().getName()
                            + ") creating the document to write back out.",
                            e);
                    throw new RuntimeException(e);
                }
                Iterator bodyNodeIterator = bodyElement.nodeIterator();
                while(bodyNodeIterator.hasNext()) {
                    try {
                        writer.write((org.dom4j.Node)bodyNodeIterator.next());
                    } catch (IOException e) {
                        log.error("Error ("
                                + e.getClass().getName()
                                + ") writing the body back out:\n"
                                + bodyElement.asXML(),
                                e);
                        throw new RuntimeException(e);
                    }
                }
            }
            return outStream.toString();
        }
    }


    /**
     * <p>
     * Get the name of the source or file, used for debugging.
     * </p>
     *
     * @return the current value of the source name, output by the parser for
     *         debugging.
     */
    public final String getSourceName() {
        return sourceName;
    }

    /**
     * <p>
     * Stores whether or not <code>format</code> should return just plain
     * text with line feeds and converted horizonal rule. If <code>true</code>,
     * fomratted text is returned, otherwise formatted
     * HTML is returned.
     * </p>
     *
     * @return Returns formattedText.
     */
    public boolean isFormattedText() {
        return formattedText;
    }

    /**
     * <p>
     * Get whether or not the parser will only return plain text.
     * </p>
     *
     * @return <code>true</code> if the parser will only return plain text,
     *         otherwise <code>false</code>.
     */
    public boolean isTextOnly() {
        return textOnly;
    }

    /**
     * <p>Write something other than a new line.</p>
     */
    private void notTextNewLine() {
        textNewLineCount = 0;
        textAtStartOfLine = false;
    }
    /**
     * <p>
     * Stores whether or not <code>format</code> should return just plain
     * text with line feeds and converted horizonal rule. If <code>true</code>,
     * fomratted text is returned, otherwise formatted
     * HTML is returned.
     * </p>
     *
     * @param formattedText The new value of formattedText to set.
     */
    public final void setFormattedText(final boolean formattedText) {
        this.formattedText = formattedText;
    }

    /**
     * <p>
     * Contains uri to prepend when src attribute of an image begins with
     * <em>cid: </em>- it's and embedded attachment
     * </p>
     *
     * @param imageUri -
     *            the uri to prepend when src attribute of an image begins with
     *            <em>cid: </em>
     */
    public final void setImageUri(final String imageUri) {
        this.imageUri = imageUri;
    }

    /**
     * <p>
     * The information to append to rewritten uris of embedded attachments
     * </p>
     *
     * @param imageUriAppend
     *            the information to append to rewritten uris of embedded
     *            attachments
     */
    public final void setImageUriAppend(final String imageUriAppend) {
        this.imageUriAppend = imageUriAppend;
    }

    /**
     * <p>
     * If <code>true</code> then only the contents of the body tag are returned.
     * </p>
     *
     * @param onlyBodyContents
     *            set to <code>true</code to specify that the
     * parser should only include the contents of the body tag.
     */
    public final void setOnlyBodyContents(final boolean onlyChildren) {
        this.onlyBodyContents = onlyChildren;
    }

    /**
     * <p>
     * Set the name of the source or file, used for debugging.
     * </p>
     *
     * @param sourceName
     *            the current value of the source name, output by the parser for
     *            debugging.
     */
    public final void setSourceName(final String sourceName) {
        this.sourceName = sourceName;
    }

    /**
     * <p>
     * Set whether or not the parser should only return plain text.
     * </p>
     *
     * @param textOnly
     *            set to <code>true</code> if the parser should only return
     *            plain text, otherwise <code>false</code>.
     */
    public final void setTextOnly(final boolean textOnly) {
        this.textOnly = textOnly;
    }
}