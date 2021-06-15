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

import org.archive.crawler.datamodel.CandidateURI;
import org.archive.net.PublicSuffixes;
import org.archive.net.UURI;

/**
 * Applies its decision if the current URI differs in that portion of
 * its hostname/domain that is assigned/sold by registrars (AKA its
 * 'topmost assigned SURT' or 'public suffix'.)
 * 
 * @author Olaf Freyer
 */
public class IsCrossTopmostAssignedSurtHopDecideRule extends PredicatedDecideRule {
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = Logger
            .getLogger(IsCrossTopmostAssignedSurtHopDecideRule.class.getName());

    public IsCrossTopmostAssignedSurtHopDecideRule(String name) {
        super(name);
        setDescription(
            "Matches if the registrar-assigned portion of a URI's " +
            "hostname (AKA 'topmost assigned SURT') differs from that " +
            "of its referrer. ");
    }

    protected boolean evaluate(Object object) {
        UURI via = (object instanceof CandidateURI) ? ((CandidateURI) object).getVia() : null;
        if (via == null) {
            return false;
        }
        CandidateURI curi = (CandidateURI) object;
        if (curi == null) {
            return false;
        }
        try {
            // determine if this hop crosses domain borders
            String myTopmostAssignedSurt = getTopmostAssignedSurt(curi.getUURI());
            String viaTopmostAssignetSurt = getTopmostAssignedSurt(via);
            if (myTopmostAssignedSurt != null && viaTopmostAssignetSurt != null
                    && !myTopmostAssignedSurt.equals(viaTopmostAssignetSurt)) {
                LOGGER.fine("rule matched for \"" + myTopmostAssignedSurt+"\" vs. \""+viaTopmostAssignetSurt+"\"");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Return false since we could not get hostname or something else
            // went wrong
        }
        return false;
    }
    
    private String getTopmostAssignedSurt(UURI uuri){
        String surt = uuri.getSurtForm().replaceFirst(".*://\\((.*?)\\).*", "$1");
        return PublicSuffixes.reduceSurtToTopmostAssigned(surt);
        
    }

}
