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

import javax.xml.transform.TransformerException;

import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.IntStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionDef1Arg;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.html.HtmlForm;

/**
 * An XPath function which returns <tt>true</tt> if the current node is a descendant of the node used as the
 * initial XPath context. In addition, if the node used as the initial XPath context was an {@link HtmlForm}
 * instance, this function considers {@link HtmlForm#getLostChildren() lost children} to be descendants of the
 * form, even if they aren't really descendants from a DOM point of view.
 *
 * @version $Revision: 5798 $
 * @author Daniel Gredler
 * @deprecated since HtmlUnit-2.8 without replacement as there is no internal usage for this classe.
 */
@Deprecated
public class IsDescendantOfContextualFormFunction extends FunctionDef1Arg {

    /** Serial version UID. */
    private static final long serialVersionUID = -7865865499040147609L;

    /**
     * {@inheritDoc}
     */
    @Override
    public XObject execute(final XPathContext ctx) throws TransformerException {
        boolean descendant = false;

        final int possibleAncestor;
        final IntStack nodeStack = ctx.getCurrentNodeStack();
        if (nodeStack.size() > 1) {
            possibleAncestor = nodeStack.elementAt(1);
        }
        else {
            possibleAncestor = DTM.NULL;
        }

        if (DTM.NULL != possibleAncestor) {
            final int currentNode = ctx.getContextNode();
            final DTM dtm = ctx.getDTM(currentNode);
            for (int ancestor = dtm.getParent(currentNode); ancestor != DTM.NULL; ancestor = dtm.getParent(ancestor)) {
                if (ancestor == possibleAncestor) {
                    descendant = true;
                    break;
                }
            }
            if (!descendant) {
                final Node n = dtm.getNode(possibleAncestor);
                if (n instanceof HtmlForm) {
                    final HtmlForm f = (HtmlForm) n;
                    descendant = f.getLostChildren().contains(dtm.getNode(currentNode));
                }
            }
        }

        return new XBoolean(descendant);
    }

}
