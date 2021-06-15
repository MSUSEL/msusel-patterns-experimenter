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
package com.itextpdf.rups.view.itext.treenodes;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;

import com.itextpdf.rups.view.icons.IconFetcher;
import com.itextpdf.rups.view.icons.IconTreeNode;

public class XdpTreeNode extends IconTreeNode {

	/** A serial version UID. */
	private static final long serialVersionUID = -6431790925424045933L;

	/**
	 * Constructs an XdpTreeNode
	 * @param node	the XML node
	 */
	@SuppressWarnings("unchecked")
    public XdpTreeNode(Node node) {
		super(null, node);
		if (node instanceof Element) {
			Element element = (Element)node;
			addChildNodes(element.attributes());
		}
		if (node instanceof Branch) {
			Branch branch = (Branch) node;
			addChildNodes(branch.content());
		}
		if (node instanceof Attribute) {
			icon = IconFetcher.getIcon("attribute.png");
			return;
    	}
    	if (node instanceof Text) {
    		icon = IconFetcher.getIcon("text.png");
    		return;
    	}
    	if (node instanceof ProcessingInstruction) {
    		icon = IconFetcher.getIcon("pi.png");
    		return;
    	}
    	if (node instanceof Document) {
    		icon = IconFetcher.getIcon("xfa.png");
    		return;
    	}
    	icon = IconFetcher.getIcon("tag.png");
	}

	private void addChildNodes(List <Node>list) {
		for (Node node : list) {
			Node n = node;
			if (n instanceof Namespace) continue;
			if (n instanceof Comment) continue;
			this.add(new XdpTreeNode(n));
		}
	}

	public Node getNode() {
    	return (Node)getUserObject();
	}

	@Override
    public String toString() {
		Node node = getNode();
		if (node instanceof Element) {
			Element e = (Element)node;
			return e.getName();
		}
		if (node instanceof Attribute) {
			Attribute a = (Attribute)node;
			StringBuffer buf = new StringBuffer();
			buf.append(a.getName());
			buf.append("=\"");
			buf.append(a.getValue());
			buf.append('"');
			return buf.toString();
		}
		if (node instanceof Text) {
			Text t = (Text)node;
			return t.getText();
		}
		if (node instanceof ProcessingInstruction) {
			ProcessingInstruction pi = (ProcessingInstruction)node;
			StringBuffer buf = new StringBuffer("<?");
			buf.append(pi.getName());
			buf.append(' ');
			buf.append(pi.getText());
			buf.append("?>");
			return buf.toString();
		}
		if (node instanceof Document) {
			return "XFA Document";
		}
		return getNode().toString();
	}
}