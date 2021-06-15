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

package org.hsqldb.types;

import java.io.Serializable;

import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.InOutUtil;

/**
 * Represents of an instance of an OTHER field value. <p>
 *
 * Prior to 1.7.0 there were problems storing Objects of normal column types
 * in columns of the type OTHER. In 1.7.0 changes were made to allow this,
 * but as all the conversion took place inside the engine, it introduced a
 * requirement for all classes for objects stored in OTHER columns to be
 * available on the runtime class path of the engine. <p>
 *
 * In 1.7.2, the introduction of real preprared statement support allows us
 * revert to the pre 1.7.0 behaviour without the artificial limitations. <p>
 *
 * The classes for stored objects need not be available to open and operate
 * the database in general. The classes need to be available only if a
 * conversion from one of these objects to another type is performed inside
 * the engine while operating the database.
 *
 * The current limitation is that in SQL statements, values of type String
 * (CHARACTER and related SQL types) cannot be stored in columns of type
 * OTHER. This limitation does not exist for String values assigned to
 * PreparedStatement variables.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.7.2
 */
public class JavaObjectData {

    private byte[] data;

    JavaObjectData() {}

    /**
     * Constructor used inside the engine when an already serialized
     * Object is read from a file (.log, .script, .data or text table source).
     */
    public JavaObjectData(byte[] data) {
        this.data = data;
    }

    /**
     * Constructor used inside the engine to convert an Object into an
     * object of type OTHER.
     * Used also with JDBC setParameter().
     * If parameter serialize is true, the Object is serialized for storage.
     */
    public JavaObjectData(Serializable o) {

        try {
            data = InOutUtil.serialize(o);
        } catch (Exception e) {
            throw Error.error(ErrorCode.X_22521, e.toString());
        }
    }

    public byte[] getBytes() {
        return data;
    }

    public int getBytesLength() {
        return data.length;
    }

    /**
     * This method is called from classes implementing the JDBC
     * interfaces. Inside the engine it is used for conversion from a value of
     * type OTHER to another type. It will throw if the OTHER is an instance
     * of a classe that is not available.
     */
    public Serializable getObject() {

        try {
            return InOutUtil.deserialize(data);
        } catch (Exception e) {
            throw Error.error(ErrorCode.X_22521, e.toString());
        }
    }

    /**
     * Returns String repsentation of this object.
     *
     * @return a String represntation of this object.
     */
    public String toString() {
        return super.toString();
    }
}
