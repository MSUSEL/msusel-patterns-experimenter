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
package com.jaspersoft.ireport.designer.connection.gui;

import com.jaspersoft.ireport.designer.IReportManager;
import java.text.MessageFormat;
import java.util.Formatter;

/**
 *
 * @author gtoffoli
 */
public class JDBCDriverDefinition implements Comparable {
    private String urlPattern = "";
    private String dbName = "";
    private String driverName = "";
    private String defaultDBName = "MYDATABASE";

    public JDBCDriverDefinition(String dbName, String driverName, String urlPattern)
    {
        this(dbName, driverName, urlPattern, "MYDATABASE");
    }

    public JDBCDriverDefinition(String dbName, String driverName, String urlPattern, String defaultDBName)
    {
        this.dbName = dbName;
        this.driverName = driverName;
        this.urlPattern = urlPattern;
        this.defaultDBName = defaultDBName;
    }

    public String getUrl(String server, String database)
    {
        if (database == null || database.trim().length() == 0)
        {
            database = getDefaultDBName();
        }
        database = database.trim();
        return MessageFormat.format(getUrlPattern(), new Object[]{server,database});
    }

    public boolean isAvailable()
    {
        try {
            Class.forName(getDriverName(), false, IReportManager.getReportClassLoader());
        } catch (ClassNotFoundException ex)
        {
            return false;
        } catch (Throwable ex)
        {
            return false;
        }
        return true;
    }

    /**
     * @return the urlPattern
     */
    public String getUrlPattern() {
        return urlPattern;
    }

    /**
     * @param urlPattern the urlPattern to set
     */
    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    /**
     * @return the dbName
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * @param dbName the dbName to set
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * @return the driverName
     */
    public String getDriverName() {
        return driverName;
    }

    /**
     * @param driverName the driverName to set
     */
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    /**
     * @return the defaultDBName
     */
    public String getDefaultDBName() {
        return defaultDBName;
    }

    /**
     * @param defaultDBName the defaultDBName to set
     */
    public void setDefaultDBName(String defaultDBName) {
        this.defaultDBName = defaultDBName;
    }

    public String toString()
    {
        return dbName + " (" + driverName + ")";
    }

    public int compareTo(Object o) {
        return (o+"").compareTo(toString());
    }


}
