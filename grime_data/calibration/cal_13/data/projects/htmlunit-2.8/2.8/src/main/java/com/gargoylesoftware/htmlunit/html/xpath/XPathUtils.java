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
package com.gargoylesoftware.htmlunit.html.xpath;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.html.DomNode;

/**
 * Collection of XPath utility methods.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 */
public final class XPathUtils {

    private static ThreadLocal<Boolean> PROCESS_XPATH_ = new ThreadLocal<Boolean>() {
        @Override
        protected synchronized Boolean initialValue() {
            return false;
        }
    };

    /**
     * Private to avoid instantiation.
     */
    private XPathUtils() {
        // Empty.
    }

    /**
     * Evaluates an XPath expression from the specified node, returning the resultant nodes.
     *
     * @param node the node to start searching from
     * @param xpathExpr the XPath expression
     * @return the list of objects found
     */
    public static List<Object> getByXPath(final DomNode node, final String xpathExpr) {
        if (xpathExpr == null) {
            throw new NullPointerException("Null is not a valid XPath expression");
        }

        PROCESS_XPATH_.set(true);
        final List<Object> list = new ArrayList<Object>();
        try {
            final XObject result = evaluateXPath(node, xpathExpr);

            if (result instanceof XNodeSet) {
                final NodeList nodelist = ((XNodeSet) result).nodelist();
                for (int i = 0; i < nodelist.getLength(); i++) {
                    list.add(nodelist.item(i));
                }
            }
            else if (result instanceof XNumber) {
                list.add(result.num());
            }
            else if (result instanceof XBoolean) {
                list.add(result.bool());
            }
            else if (result instanceof XString) {
                list.add(result.str());
            }
            else {
                throw new RuntimeException("Unproccessed " + result.getClass().getName());
            }
        }
        catch (final Exception e) {
            throw new RuntimeException("Could not retrieve XPath >" + xpathExpr + "< on " + node, e);
        }
        finally {
            PROCESS_XPATH_.set(false);
        }
        return list;
    }

    /**
     * Returns whether the thread is currently evaluating XPath expression or no.
     * @return whether the thread is currently evaluating XPath expression or no
     */
    public static boolean isProcessingXPath() {
        return PROCESS_XPATH_.get();
    }

    /**
     * Evaluates an XPath expression to an XObject.
     * @param contextNode the node to start searching from
     * @param str a valid XPath string
     * @return an XObject, which can be used to obtain a string, number, nodelist, etc (should never be <tt>null</tt>)
     * @throws TransformerException if a syntax or other error occurs
     */
    private static XObject evaluateXPath(final DomNode contextNode, final String str) throws TransformerException {
        final XPathContext xpathSupport = new XPathContext();
        final Node xpathExpressionContext;
        if (contextNode.getNodeType() == Node.DOCUMENT_NODE) {
            xpathExpressionContext = ((Document) contextNode).getDocumentElement();
        }
        else {
            xpathExpressionContext = contextNode;
        }
        final PrefixResolver prefixResolver = new HtmlUnitPrefixResolver(xpathExpressionContext);
        final boolean caseSensitive = contextNode.getPage().hasCaseSensitiveTagNames();
        final XPathAdapter xpath = new XPathAdapter(str, null, prefixResolver, null, caseSensitive);
        final int ctxtNode = xpathSupport.getDTMHandleFromNode(contextNode);
        return xpath.execute(xpathSupport, ctxtNode, prefixResolver);
    }

}
