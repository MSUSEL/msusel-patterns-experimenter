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
package org.archive.crawler.datamodel;

import java.io.File;

/**
 * A UriUniqFilter passes URI objects to a destination
 * (receiver) if the passed URI object has not been previously seen.
 * 
 * If already seen, the passed URI object is dropped.
 *
 * <p>For efficiency in comparison against a large history of
 * seen URIs, URI objects may not be passed immediately, unless 
 * the addNow() is used or a flush() is forced.
 * 
 * @author gojomo
 * @version $Date: 2005-12-16 03:10:54 +0000 (Fri, 16 Dec 2005) $, $Revision: 4036 $
 */
public interface UriUniqFilter {
    /**
     * @return Count of already seen URIs.
     */
    public long count();
    
    /**
     * Count of items added, but not yet filtered in or out. 
     * 
     * Some implementations may buffer up large numbers of pending
     * items to be evaluated in a later large batch/scan/merge with 
     * disk files. 
     * 
     * @return Count of items added not yet evaluated 
     */
    public long pending();

    /**
     * Receiver of uniq URIs.
     * 
     * Items that have not been seen before are pass through to this object.
     * @param receiver Object that will be passed items. Must implement
     * HasUriReceiver interface.
     */
    public void setDestination(HasUriReceiver receiver);
    
    /**
     * Add given uri, if not already present.
     * @param key Usually a canonicalized version of <code>value</code>.
     * This is the key used doing lookups, forgets and insertions on the
     * already included list.
     * @param value item to add.
     */
    public void add(String key, CandidateURI value);
    
    /**
     * Immediately add uri.
     * @param key Usually a canonicalized version of <code>uri</code>.
     * This is the key used doing lookups, forgets and insertions on the
     * already included list.
     * @param value item to add.
     */
    public void addNow(String key, CandidateURI value);
    
    /**
     * Add given uri, all the way through to underlying destination, even 
     * if already present.
     * 
     * (Sometimes a URI must be fetched, or refetched, for example when
     * DNS or robots info expires or the operator forces a refetch. A
     * normal add() or addNow() would drop the URI without forwarding
     * on once it is determmined to already be in the filter.) 
     * 
     * @param key Usually a canonicalized version of <code>uri</code>.
     * This is the key used doing lookups, forgets and insertions on the
     * already included list.
     * @param value item to add.
     */
    public void addForce(String key, CandidateURI value);
    
    /**
     * Note item as seen, without passing through to receiver.
     * @param key Usually a canonicalized version of an <code>URI</code>.
     * This is the key used doing lookups, forgets and insertions on the
     * already included list.
     */
    public void note(String key);
    
    /**
     * Forget item was seen
     * @param key Usually a canonicalized version of an <code>URI</code>.
     * This is the key used doing lookups, forgets and insertions on the
     * already included list.
     * @param value item to add.
     */
    public void forget(String key, CandidateURI value);
    
    /**
     * Request that any pending items be added/dropped. Implementors
     * may ignore the request if a flush would be too expensive/too 
     * soon. 
     * 
     * @return Number added.
     */
    public long requestFlush();
    
    /**
     * Close down any allocated resources.
     * Makes sense calling this when checkpointing.
     */
    public void close();
    
    /**
     * Set a File to receive a log for replay profiling. 
     */
    public void setProfileLog(File logfile);
    
    /**
     * URIs that have not been seen before 'visit' this 'Visitor'.
     * 
     * Usually implementations of Frontier implement this interface.
     * @author gojomo
     */
    public interface HasUriReceiver {
        /**
         * @param item Candidate uri tem that is 'visiting'.
         */
        public void receive(CandidateURI item);
    }
}