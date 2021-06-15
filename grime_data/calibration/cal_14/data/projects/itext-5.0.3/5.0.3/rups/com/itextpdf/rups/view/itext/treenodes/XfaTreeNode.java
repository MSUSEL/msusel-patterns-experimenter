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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import com.itextpdf.rups.io.OutputStreamResource;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfReader;

/**
 * This is the root tree node for the different parts of the XFA resource; it's a child
 * of the root in the FormTree.
 * This resource can be one XDP stream (in which case this root will only have one child)
 * or different streams with individual packets comprising the XML Data Package.
 */
public class XfaTreeNode extends FormTreeNode implements OutputStreamResource {

	/** Start sequence of an artificial boundary between XFA fragments added by RUPS */
	public static final byte[] BOUNDARY_START = "<!--\nRUPS XFA individual packet: end of [".getBytes();
	/** Middle sequence of an artificial boundary between XFA fragments added by RUPS */
	public static final byte[] BOUNDARY_MIDDLE = "]; start of [".getBytes();
	/** End sequence of an artificial boundary between XFA fragments added by RUPS */
	public static final byte[] BOUNDARY_END = "]\n-->".getBytes();

	/**
	 * Creates the root node of the XFA tree.
	 * This will be a child of the FormTree root node.
	 * @param	xfa	the XFA node in the PdfTree (a child of the AcroForm node in the PDF catalog)
	 */
	public XfaTreeNode(PdfObjectTreeNode xfa) {
		super(xfa);
	}

	/**
	 * Writes (part of) the XFA resource to an OutputStream.
	 * If key is <code>null</code>, the complete resource is written;
	 * if key refers to an individual package, this package only is
	 * written to the OutputStream.
	 * @param os	the OutputStream to which the XML is written.
	 * @throws IOException	usual exception when there's a problem writing to an OutputStream
	 */
	@SuppressWarnings("unchecked")
        public void writeTo(OutputStream os) throws IOException {
		Enumeration<FormTreeNode> children = this.children();
		FormTreeNode node;
		PRStream stream;
		String key = null;
		String tmp = null;
		while (children.hasMoreElements()) {
			node = children.nextElement();
			if (key != null) {
				os.write(BOUNDARY_START);
				os.write(key.getBytes());
				os.write(BOUNDARY_MIDDLE);
				tmp = (String)node.getUserObject();
				os.write(tmp.getBytes());
				os.write(BOUNDARY_END);
			}
			key = tmp;
			stream = (PRStream)node.getCorrespondingPdfObjectNode().getPdfObject();
			os.write(PdfReader.getStreamBytes(stream));
		}
		os.flush();
		os.close();
	}

	/**
	 * Adds a child node to the XFA root.
	 * The child node either corresponds with the complete XDP stream
	 * (if the XFA root only has one child) or with individual packet.
	 * @param key	the name of the packet
	 * @param value	the corresponding stream node in the PdfTree
	 */
	public void addPacket(String key, PdfObjectTreeNode value) {
		FormTreeNode node = new FormTreeNode(value);
		node.setUserObject(key);
		this.add(node);
	}

	/** A serial version UID. */
	private static final long serialVersionUID = 2463297568233643790L;

}