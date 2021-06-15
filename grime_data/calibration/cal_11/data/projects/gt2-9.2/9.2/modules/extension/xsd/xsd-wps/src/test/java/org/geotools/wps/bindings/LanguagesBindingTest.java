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
package org.geotools.wps.bindings;

import javax.xml.namespace.QName;

import net.opengis.wps10.DefaultType2;
import net.opengis.wps10.LanguagesType;
import net.opengis.wps10.LanguagesType1;
import net.opengis.wps10.Wps10Factory;

import org.geotools.ows.v1_1.OWS;
import org.geotools.wps.WPS;
import org.geotools.wps.WPSTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 *
 * @source $URL$
 */
public class LanguagesBindingTest extends WPSTestSupport {

    public void testEncode() throws Exception {
        Wps10Factory f = Wps10Factory.eINSTANCE;
        LanguagesType1 languages = f.createLanguagesType1();
        
        DefaultType2 defaultLanguage = f.createDefaultType2();
        languages.setDefault(defaultLanguage);
        defaultLanguage.setLanguage("en-US");
        
        LanguagesType supportedLanguages = f.createLanguagesType();
        languages.setSupported( supportedLanguages );
        supportedLanguages.getLanguage().add( "en-US");
        
        Document dom = encode( languages, WPS.Languages );
        
        Element def = getElementByQName( dom.getDocumentElement(), new QName( WPS.NAMESPACE, "Default") ) ;
        assertNotNull( def );
       
        assertNotNull( getElementByQName( def, OWS.Language ) );
        assertEquals( "en-US", getElementByQName( def, OWS.Language ).getFirstChild().getTextContent() );
        
        assertEquals( "en-US",  getElementByQName( dom.getDocumentElement(), new QName( WPS.NAMESPACE, "Default")).getFirstChild().getTextContent() );
        assertNotNull( getElementByQName( dom.getDocumentElement(), new QName( WPS.NAMESPACE, "Supported") ) );
    }
}
