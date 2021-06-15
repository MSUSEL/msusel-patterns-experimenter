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

package org.hsqldb.dbinfo;

import java.util.Locale;

import org.hsqldb.Table;
import org.hsqldb.TableBase;
import org.hsqldb.resources.BundleHandler;
import org.hsqldb.store.ValuePool;
import org.hsqldb.types.Types;

/**
 * Provides extended information about HSQLDB tables and their
 * columns/indices. <p>
 *
 * Current version has been reduced in scope.<p>
 *
 * @author boucherb@users
 * @version 1.9.0
 * @since 1.7.2
 */
final class DITableInfo {

    // related to DatabaseMetaData
    int                bestRowTemporary   = 0;
    int                bestRowTransaction = 1;
    int                bestRowSession     = 2;
    int                bestRowUnknown     = 0;
    int                bestRowNotPseudo   = 1;
    static final short tableIndexOther    = 3;

    /** Used in buffer size and character octet length determinations. */
    private static final int HALF_MAX_INT = Integer.MAX_VALUE >>> 1;

    /** BundleHandler id for column remarks resource bundle. */
    private int hnd_column_remarks = -1;

    /** BundleHandler id for table remarks resource bundle. */
    private int hnd_table_remarks = -1;

    /** The Table object upon which this object is reporting. */
    private Table table;

    /**
     * Creates a new DITableInfo object with the default Locale and reporting
     * on no table.  It is absolutely essential the a valid Table object is
     * assigned to this object, using the setTable method, before any Table,
     * Column or Index oriented value retrieval methods are called; this class
     * contains no assertions or exception handling related to a null or
     * invalid table member attribute.
     */
    DITableInfo() {
        setupBundles();
    }

    /**
     * Sets the Locale for table and column remarks. <p>
     */
    void setupBundles() {

        Locale oldLocale;

        synchronized (BundleHandler.class) {
            oldLocale = BundleHandler.getLocale();

            BundleHandler.setLocale(Locale.getDefault());

            hnd_column_remarks =
                BundleHandler.getBundleHandle("column-remarks", null);
            hnd_table_remarks = BundleHandler.getBundleHandle("table-remarks",
                    null);

            BundleHandler.setLocale(oldLocale);
        }
    }

    /**
     * Retrieves whether the best row identifier column is
     * a pseudo column, like an Oracle ROWID. <p>
     *
     * Currently, this always returns an Integer whose value is
     * DatabaseMetaData.bestRowNotPseudo, as HSQLDB does not support
     * pseudo columns such as ROWID. <p>
     *
     * @return whether the best row identifier column is
     * a pseudo column
     */
    Integer getBRIPseudo() {
        return ValuePool.getInt(bestRowNotPseudo);
    }

    /**
     * Retrieves the scope of the best row identifier. <p>
     *
     * This implements the rules described in
     * DatabaseInformationMain.SYSTEM_BESTROWIDENTIFIER. <p>
     *
     * @return the scope of the best row identifier
     */
    Integer getBRIScope() {
        return (table.isWritable()) ? ValuePool.getInt(bestRowTemporary)
                                    : ValuePool.getInt(bestRowSession);
    }

    /**
     * Retrieves the simple name of the specified column. <p>
     *
     * @param i zero-based column index
     * @return the simple name of the specified column.
     */
    String getColName(int i) {
        return table.getColumn(i).getName().name;
    }

    /**
     * Retrieves the remarks, if any, recorded against the specified
     * column. <p>
     *
     * @param i zero-based column index
     * @return the remarks recorded against the specified column.
     */
    String getColRemarks(int i) {

        String key;

        if (table.getTableType() != TableBase.SYSTEM_TABLE) {
            return null;
        }

        key = getName() + "_" + getColName(i);

        return BundleHandler.getString(hnd_column_remarks, key);
    }

    /**
     * Retrieves the HSQLDB-specific type of the table. <p>
     *
     * @return the HSQLDB-specific type of the table
     */
    String getHsqlType() {

        switch (table.getTableType()) {

            case TableBase.MEMORY_TABLE :
            case TableBase.TEMP_TABLE :
            case TableBase.SYSTEM_TABLE :
                return "MEMORY";

            case TableBase.CACHED_TABLE :
                return "CACHED";

            case TableBase.TEMP_TEXT_TABLE :
            case TableBase.TEXT_TABLE :
                return "TEXT";

            case TableBase.VIEW_TABLE :
            default :
                return null;
        }
    }

    /**
     * Retrieves the simple name of the table. <p>
     *
     * @return the simple name of the table
     */
    String getName() {
        return table.getName().name;
    }

    /**
     * Retrieves the remarks (if any) recorded against the Table. <p>
     *
     * @return the remarks recorded against the Table
     */
    String getRemark() {

        return (table.getTableType() == TableBase.SYSTEM_TABLE)
               ? BundleHandler.getString(hnd_table_remarks, getName())
               : table.getName().comment;
    }

    /**
     * Retrieves the standard JDBC type of the table. <p>
     *
     * "TABLE" for user-defined tables, "VIEW" for user-defined views,
     * and so on.
     *
     * @return the standard JDBC type of the table
     */
    String getJDBCStandardType() {

        switch (table.getTableType()) {

            case TableBase.VIEW_TABLE :
                return "VIEW";

            case TableBase.TEMP_TABLE :
            case TableBase.TEMP_TEXT_TABLE :
                return "GLOBAL TEMPORARY";

            case TableBase.SYSTEM_TABLE :
                return "SYSTEM TABLE";

            default :
                if (table.getOwner().isSystem() ) {
                    return "SYSTEM TABLE";
                }
                return "TABLE";
        }
    }

    /**
     * Retrieves the Table object on which this object is currently
     * reporting. <p>
     *
     * @return the Table object on which this object
     *    is currently reporting
     */
    Table getTable() {
        return this.table;
    }

    /**
     * Assigns the Table object on which this object is to report. <p>
     *
     * @param table the Table object on which this object is to report
     */
    void setTable(Table table) {
        this.table = table;
    }
}
