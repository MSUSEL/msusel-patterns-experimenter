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
package org.geotools.maven.xmlcodegen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.xml.Schemas;

/**
 * Utility class used for generating.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class GeneratorUtils {

	/**
	 * Returns all the types defined in <param>schema</param>, including anonymous complex
	 * types.
	 * 
	 * @param schema the schema.
	 * 
	 * @return A list of types, including anonymous complex types.
	 */
	public static List allTypes( XSDSchema schema ) {
		//get the named types
		List types = new ArrayList( schema.getTypeDefinitions() );
    	
		//get anonymous types
		List anonymous = new ArrayList();
		
		//add any anonymous types of global elements + attributes
		for ( Iterator e = schema.getElementDeclarations().iterator(); e.hasNext(); ) {
			XSDElementDeclaration element = (XSDElementDeclaration ) e.next();
			if ( element.getAnonymousTypeDefinition() != null ) {
				element.getAnonymousTypeDefinition().setName( "_" + element.getName() );
				anonymous.add( element.getAnonymousTypeDefinition() );
			}
		}
		for ( Iterator a = schema.getAttributeDeclarations().iterator(); a.hasNext(); ) {
			XSDAttributeDeclaration attribute = (XSDAttributeDeclaration) a.next();
			if ( attribute.getAnonymousTypeDefinition() != null ) {
				attribute.getAnonymousTypeDefinition().setName( "_" + attribute.getName() );
				anonymous.add( attribute.getAnonymousTypeDefinition() );
			}
		}
    	//add any anonymous types foudn with type definitions
    	for ( Iterator t = types.iterator(); t.hasNext(); ) {
    		XSDTypeDefinition type = (XSDTypeDefinition) t.next();
    		List particles = Schemas.getChildElementParticles( type, false );
    		for ( Iterator p = particles.iterator(); p.hasNext(); ) {
    			XSDParticle particle = (XSDParticle) p.next();
    			XSDElementDeclaration element = (XSDElementDeclaration) particle.getContent();
    			
    			//ignore element references, caught in teh above loop
    			if ( element.isElementDeclarationReference() )
    				continue;
    			
    			if ( element.getAnonymousTypeDefinition() != null ) {
    				element.getAnonymousTypeDefinition().setName( type.getName() + "_" + element.getName() );
    				anonymous.add( element.getAnonymousTypeDefinition() );
    			}
    		}
    	}
    	
    	types.addAll( anonymous );
    	return types;
	}
	
	/**
     * Returns all the types defined in <param>schema</param> whose names are 
     * included in the specified set of names.
     * <p>
     * If <tt>names</tt> is null or empty all types are returned.
     * </p>
     * @param schema the schema.
     * @param names the names of types to include.
     * 
     * @return A list of types, including anonymous complex types.
     */
	public static List types( XSDSchema schema, Set names ) {
	    List allTypes = allTypes( schema );
	    if ( names != null && !names.isEmpty() ) {
	        for (Iterator t = allTypes.iterator(); t.hasNext(); ) {
	            XSDTypeDefinition type = (XSDTypeDefinition) t.next();
	            if ( type.getName() != null && names.contains(type.getName())) {
	                continue;
	            }
	            
	            t.remove();
	        }
	    }
	    
	    return allTypes;
	}
}
