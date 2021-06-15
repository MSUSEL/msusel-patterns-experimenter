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
 * ConnectionSetting represents the various parameters of a data source
 * connection.
 *
 * @author lonbinder@users
 */
public class ConnectionSetting implements java.io.Serializable {

    private String name, driver, url, user, pw;

    String getName() {
        return name;
    }

    String getDriver() {
        return driver;
    }

    String getUrl() {
        return url;
    }

    String getUser() {
        return user;
    }

    String getPassword() {
        return pw;
    }

    // Constructors
    private ConnectionSetting() {}
    ;

    ConnectionSetting(String name, String driver, String url, String user,
                      String pw) {

        this.name   = name;
        this.driver = driver;
        this.url    = url;
        this.user   = user;
        this.pw     = pw;
    }

    public boolean equals(Object obj) {

        if (!(obj instanceof ConnectionSetting)) {
            return false;
        }

        ConnectionSetting other = (ConnectionSetting) obj;

        if (getName() == other.getName()) {
            return true;
        }

        if (getName() == null) {
            return false;
        }

        return getName().trim().equals(other.getName().trim());
    }

    public int hashCode() {
        return getName() == null ? 0
                                 : getName().trim().hashCode();
    }
}
