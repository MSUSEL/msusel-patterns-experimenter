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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.efeature;

import java.io.IOException;
import java.lang.ref.WeakReference;

import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.geotools.data.Transaction;
import org.geotools.data.Transaction.State;

/**
 * @author kengu - 7. juli 2011
 *
 *
 * @source $URL$
 */
public class EFeatureTransactionState implements State {

    private WeakReference<Transaction> eTx;
    private WeakReference<EFeatureDataStore> eDataStore;
    
    private ChangeRecorder eRecorder;
    private ChangeDescription eDescription;
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    public EFeatureTransactionState(EFeatureDataStore eDataStore) {
        //
        // Store weak reference to data store
        //
        this.eDataStore = new WeakReference<EFeatureDataStore>(eDataStore);
        //
        // Create change recording capabilities
        //
        this.eRecorder = new ChangeRecorder();
        //
        // End current recording (initialize the stepwise write recording cycle)
        //
        this.eDescription = this.eRecorder.endRecording();
    }

    // ----------------------------------------------------- 
    //  State implementation
    // -----------------------------------------------------
    
    public Transaction getTransaction() {
        return eTx.get();
    }
    
    @Override
    public void setTransaction(Transaction eTx) {
        this.eTx = new WeakReference<Transaction>(eTx);
    }

    public EFeatureDataStore eDataStore() {
        return eDataStore.get();
    }
    
    @Override
    public void addAuthorization(String AuthID) throws IOException {
        // TODO who does this fit into the picture?
        
    }

    @Override
    public void commit() throws IOException {
        
    }

    @Override
    public void rollback() throws IOException {
        // TODO Auto-generated method stub
        
    }

}
