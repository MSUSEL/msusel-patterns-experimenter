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

/**
 * @author Nicolas BAZIN, INGENICO
 * @version 1.7.0
 */

// brian.porter@siteforce.de 20020703 - added reference to OracleTransferHelper
class HelperFactory {

    HelperFactory() {}

    // TransferHelper factory
    static TransferHelper getHelper(String productLowerName) {

        TransferHelper f = null;

        if (productLowerName.indexOf("hsql database") != -1) {
            f = new HsqldbTransferHelper();
        } else if (productLowerName.indexOf("postgresql") != -1) {
            f = new PostgresTransferHelper();
        } else if (productLowerName.indexOf("mckoi") != -1) {
            f = new McKoiTransferHelper();
        } else if (productLowerName.indexOf("informix") != -1) {
            f = new InformixTransferHelper();
        } else if (productLowerName.indexOf("oracle") != -1) {
            System.out.println("using the Oracle helper");

            f = new OracleTransferHelper();
        } else if (productLowerName.equals("access")
                   || (productLowerName.indexOf("microsoft") != -1)) {
            f = new SqlServerTransferHelper();
        } else {
            f = new TransferHelper();
        }

        return (f);
    }
}
