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
package org.archive.crawler.processor.recrawl;

import org.archive.util.bdbje.EnhancedEnvironment;

import st.ata.util.AList;

import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;

/**
 * Common superclass for persisting Processors which directly store/load
 * to persistence (as opposed to logging for batch load later). 
 * @author gojomo
 */
public abstract class PersistOnlineProcessor extends PersistProcessor {
    private static final long serialVersionUID = -666479480942267268L;
    
    protected StoredSortedMap<String,AList> store;
    protected Database historyDb;

    /**
     * Usual constructor
     * 
     * @param name
     * @param string
     */
    public PersistOnlineProcessor(String name, String string) {
        super(name, string);
    }

    protected void initialTasks() {
        // TODO: share single store instance between Load and Store processors
        // (shared context? EnhancedEnvironment?)
        if (isEnabled()) {
            store = initStore();
        }
    }

    protected StoredSortedMap<String,AList> initStore() {
        StoredSortedMap<String,AList> historyMap;
        try {
            EnhancedEnvironment env = getController().getBdbEnvironment();
            StoredClassCatalog classCatalog = env.getClassCatalog();
            DatabaseConfig dbConfig = historyDatabaseConfig();
            historyDb = env.openDatabase(null, URI_HISTORY_DBNAME, dbConfig);
            historyMap = new StoredSortedMap<String,AList>(historyDb,
                    new StringBinding(), new SerialBinding<AList>(classCatalog,
                            AList.class), true);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        return historyMap;
    }

    @Override
    protected void finalTasks() {
        if (isEnabled()) {
            try {
                historyDb.sync();
                historyDb.close();
            } catch (DatabaseException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
