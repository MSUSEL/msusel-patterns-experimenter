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

package org.hsqldb.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

// sqlbob@users 20020407 - patch 1.7.0 - reengineering

/**
 * Common code in Swing and AWT versions of Tranfer
 * New class based on Hypersonic code
 * @author Thomas Mueller (Hypersonic SQL Group)
 * @version 1.7.2
 * @since Hypersonic SQL
 */
class TransferCommon {

    static void savePrefs(String f, DataAccessPoint sourceDb,
                          DataAccessPoint targetDb, Traceable tracer,
                          Vector tTable) {

        TransferTable t;

        try {
            FileOutputStream   fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for (int i = 0; i < tTable.size(); i++) {
                t          = (TransferTable) tTable.elementAt(i);
                t.sourceDb = null;
                t.destDb   = null;
                t.tracer   = null;
            }

            oos.writeObject(tTable);

            for (int i = 0; i < tTable.size(); i++) {
                t          = (TransferTable) tTable.elementAt(i);
                t.tracer   = tracer;
                t.sourceDb = (TransferDb) sourceDb;
                t.destDb   = targetDb;
            }
        } catch (IOException e) {
            System.out.println("pb in SavePrefs : " + e.toString());
            e.printStackTrace();
        }
    }

    static Vector loadPrefs(String f, DataAccessPoint sourceDb,
                            DataAccessPoint targetDb, Traceable tracer) {

        TransferTable     t;
        Vector            tTable = null;
        ObjectInputStream ois    = null;

        try {
            FileInputStream fis = new FileInputStream(f);

            ois    = new ObjectInputStream(fis);
            tTable = (Vector) ois.readObject();

            for (int i = 0; i < tTable.size(); i++) {
                t          = (TransferTable) tTable.elementAt(i);
                t.tracer   = tracer;
                t.sourceDb = (TransferDb) sourceDb;
                t.destDb   = targetDb;
            }
        } catch (ClassNotFoundException e) {
            System.out.println("class not found pb in LoadPrefs : "
                               + e.toString());

            tTable = new Vector();
        } catch (IOException e) {
            System.out.println("IO pb in LoadPrefs : actionPerformed"
                               + e.toString());

            tTable = new Vector();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException ioe) {}
            }
        }

        return (tTable);
    }

    private TransferCommon() {}
}
