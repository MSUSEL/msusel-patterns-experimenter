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
package org.geotools.csw;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import javax.xml.namespace.QName;

import net.opengis.cat.csw20.Csw20Factory;

import org.geotools.csw.bindings.SimpleLiteralBinding;
import org.geotools.xml.Configuration;

/**
 * Parser configuration for the http://purl.org/dc/terms/ schema.
 *
 * @generated
 */
public class DCTConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */     
    public DCTConfiguration() {
       super(DCT.getInstance());
       
       addDependency(new DCConfiguration());
    }
    
    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected void registerBindings(Map bindings) {
        bindings.put(DCT.recordAbstract, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.accessRights, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.alternative, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.audience, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.available, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.bibliographicCitation, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.conformsTo, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.created, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.dateAccepted, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.dateCopyrighted, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.dateSubmitted, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.educationLevel, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.extent, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.hasFormat, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.hasPart, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.hasVersion, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.isFormatOf, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.isPartOf, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.isReferencedBy, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.isReplacedBy, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.isRequiredBy, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.issued, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.isVersionOf, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.license, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.mediator, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.medium, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.modified, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.provenance, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.references, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.replaces, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.requires, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.rightsHolder, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.spatial, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.tableOfContents, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.temporal, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.valid, new SimpleLiteralBinding(DC.SimpleLiteral));
    }
    
    /**
     * Generates the bindings registrations for this class
     * @param args
     */
    public static void main(String[] args) {
        for(Field f : DCT.class.getFields()) {
            if((f.getModifiers() & (Modifier.STATIC | Modifier.FINAL)) != 0 && f.getType().equals(QName.class)) {
                System.out.println("bindings.put(DCT." + f.getName() + ", new SimpleLiteralBinding(DC.SimpleLiteral));");
            }
        }
    }
} 