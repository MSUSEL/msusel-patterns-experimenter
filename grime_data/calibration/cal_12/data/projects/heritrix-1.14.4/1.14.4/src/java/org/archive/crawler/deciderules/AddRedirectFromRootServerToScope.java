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
package org.archive.crawler.deciderules;

import java.util.logging.Logger;
import org.apache.commons.httpclient.URIException;
import org.archive.crawler.datamodel.CandidateURI;
import org.archive.net.UURI;


public class AddRedirectFromRootServerToScope extends PredicatedDecideRule {

    private static final long serialVersionUID = 2644131585813079064L;

    private static final Logger LOGGER =
	        Logger.getLogger(AddRedirectFromRootServerToScope.class.getName());
	private static final String SLASH = "/";
	public AddRedirectFromRootServerToScope(String name) {
		super(name);
		setDescription("Allow URI only if it is a redirect and via URI is a " +
				"root server (host's slash page) that is within the " +
				"scope. Also mark the URI as a seed."); 
	}

	protected boolean evaluate(Object object) {
		UURI via = getVia(object);
		if (via == null) {
			return false;
		}
		CandidateURI curi = (CandidateURI) object;
		if ( curi == null) {
			return false;
		}
		try {
			// Mark URI as seed if via is from different host, URI is not a seed
			// already, URI is redirect and via is root server
			if (curi.getUURI().getHostBasename() != null &&
					via.getHostBasename() != null &&
					!curi.getUURI().getHostBasename().equals(via.getHostBasename())
				    && curi.isLocation()
					&& via.getPath().equals(SLASH)) {
				curi.setIsSeed(true);
				LOGGER.info("Adding " + object.toString() + " to seeds via "
						+ getVia(object).toString());
				return true;
			}
		} catch (URIException e) {
			e.printStackTrace();
		} catch (Exception e) {
            e.printStackTrace();
			// Return false since we could not get hostname or something else 
			// went wrong
		}		
		return false;
	}

    private UURI getVia(Object o){
        return (o instanceof CandidateURI)? ((CandidateURI)o).getVia(): null;
    }    
}
