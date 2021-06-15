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
package com.itextpdf.text.xml.xmp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.xml.XmlDomWriter;

/**
 * Reads an XMP stream into an org.w3c.dom.Document objects.
 * Allows you to replace the contents of a specific tag.
 * @since 2.1.3
 */

public class XmpReader {

    private Document domDocument;
    
    /**
     * Constructs an XMP reader
     * @param	bytes	the XMP content
     * @throws ExceptionConverter 
     * @throws IOException 
     * @throws SAXException 
     */
	public XmpReader(byte[] bytes) throws SAXException, IOException {
		try {
	        DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
	        fact.setNamespaceAware(true);
			DocumentBuilder db = fact.newDocumentBuilder();
	        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
	        domDocument = db.parse(bais);
		} catch (ParserConfigurationException e) {
			throw new ExceptionConverter(e);
		}
	}
	
	/**
	 * Replaces the content of a tag.
	 * @param	namespaceURI	the URI of the namespace
	 * @param	localName		the tag name
	 * @param	value			the new content for the tag
	 * @return	true if the content was successfully replaced
	 * @since	2.1.6 the return type has changed from void to boolean
	 */
	public boolean replaceNode(String namespaceURI, String localName, String value) {
		NodeList nodes = domDocument.getElementsByTagNameNS(namespaceURI, localName);
		Node node;
		if (nodes.getLength() == 0)
			return false;
		for (int i = 0; i < nodes.getLength(); i++) {
			node = nodes.item(i);
			setNodeText(domDocument, node, value);
		}
		return true;
	}

	/**
	 * Replaces the content of an attribute in the description tag.
	 * @param	namespaceURI	the URI of the namespace
	 * @param	localName		the tag name
	 * @param	value			the new content for the tag
	 * @return	true if the content was successfully replaced
	 * @since	5.0.0 the return type has changed from void to boolean
	 */
	public boolean replaceDescriptionAttribute(String namespaceURI, String localName, String value) {
		NodeList descNodes = domDocument.getElementsByTagNameNS("http://www.w3.org/1999/02/22-rdf-syntax-ns#","Description");
		if(descNodes.getLength() == 0) {
			return false;
		}
		Node node;
		for(int i = 0; i < descNodes.getLength(); i++) {
			node = descNodes.item(i);
			Node attr = node.getAttributes().getNamedItemNS(namespaceURI, localName);
			if(attr != null) {
				attr.setNodeValue(value);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds a tag.
	 * @param	namespaceURI	the URI of the namespace
	 * @param	parent			the tag name of the parent
	 * @param	localName		the name of the tag to add
	 * @param	value			the new content for the tag
	 * @return	true if the content was successfully added
	 * @since	2.1.6
	 */
	public boolean add(String parent, String namespaceURI, String localName, String value) {
		NodeList nodes = domDocument.getElementsByTagName(parent);
		if (nodes.getLength() == 0)
			return false;
		Node pNode;
		Node node;
		for (int i = 0; i < nodes.getLength(); i++) {
			pNode = nodes.item(i);
			NamedNodeMap attrs = pNode.getAttributes();
			for (int j = 0; j < attrs.getLength(); j++) {
				node = attrs.item(j);
				if (namespaceURI.equals(node.getNodeValue())) {
					node = domDocument.createElement(localName);
					node.appendChild(domDocument.createTextNode(value));
					pNode.appendChild(node);
					return true;
				}
			}
		}
		return false;
	}
	
    /**
     * Sets the text of this node. All the child's node are deleted and a new
     * child text node is created.
     * @param domDocument the <CODE>Document</CODE> that contains the node
     * @param n the <CODE>Node</CODE> to add the text to
     * @param value the text to add
     */
    public boolean setNodeText(Document domDocument, Node n, String value) {
        if (n == null)
            return false;
        Node nc = null;
        while ((nc = n.getFirstChild()) != null) {
            n.removeChild(nc);
        }
        n.appendChild(domDocument.createTextNode(value));
        return true;
    }
	
    /**
     * Writes the document to a byte array.
     */
	public byte[] serializeDoc() throws IOException {
		XmlDomWriter xw = new XmlDomWriter();
        ByteArrayOutputStream fout = new ByteArrayOutputStream();
        xw.setOutput(fout, null);
        fout.write(XmpWriter.XPACKET_PI_BEGIN.getBytes("UTF-8"));
        fout.flush();
        NodeList xmpmeta = domDocument.getElementsByTagName("x:xmpmeta");
        xw.write(xmpmeta.item(0));
        fout.flush();
		for (int i = 0; i < 20; i++) {
			fout.write(XmpWriter.EXTRASPACE.getBytes());
		}
        fout.write(XmpWriter.XPACKET_PI_END_W.getBytes());
        fout.close();
        return fout.toByteArray();
	}
}
