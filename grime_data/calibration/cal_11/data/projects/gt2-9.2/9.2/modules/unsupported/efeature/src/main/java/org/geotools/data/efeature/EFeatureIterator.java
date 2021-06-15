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
package org.geotools.data.efeature;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.efeature.query.EFeatureFilter;

/**
 * EFeature {@link TreeIterator} class.
 * <p>
 * This class implements a EObject Tree iterator that:
 * <ol>
 * <li>support lazy loading</li>
 * <li>support spatial filtering</li>
 * </ol>
 * </p>
 * 
 * @author kengu
 * 
 *
 * @source $URL$
 */
public class EFeatureIterator implements TreeIterator<EObject> {

    private static final long serialVersionUID = 1L;

    private EObject eNext = null;

    private EFeatureFilter where;

    private TreeIterator<EObject> it;

    public EFeatureIterator(TreeIterator<EObject> from, EFeatureFilter where) {
        // Initialize
        //
        this.where = where;
        //
        // Get EMF tree iterator
        //
        this.it = from;
    }

    @Override
    public boolean hasNext() {
        if (eNext == null || it.hasNext()) {
            eNext = next();
        }
        return eNext != null;
    }

    public EObject peek() {
        //
        // Already matched by hasNext()?
        //
        if (eNext != null) {
            return eNext;
        }

        // Prepare
        //
        EObject eObject = null;

        // Loop until next match or end of collection
        //
        while (it.hasNext() && eObject == null) {
            // Get next object
            //
            eObject = it.next();
            //
            // Prune?
            //
            if (where.shouldPrune(eObject)) {
                it.prune();
            }
            //
            // Not a match?
            //
            if (!where.matches(eObject)) {
                eObject = null;
            }
        } // while
        
        //
        // Set as next
        //
        eNext = eObject;
        //
        // Finished
        //
        return eObject;
    }

    @Override
    public EObject next() {
        try {
            return eNext != null ? eNext : peek();
        } finally {
            eNext = null;
        }

    }

    /**
     * Remove is not supported by this iterator
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void prune() {
        it.prune();
    }

}
