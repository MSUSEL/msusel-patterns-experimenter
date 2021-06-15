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
package net.sourceforge.ganttproject.roles;

public class RolePersistentID {
    private static final String ROLESET_DELIMITER = ":";

    private final String myRoleSetID;

    private final int myRoleID;

    public RolePersistentID(String persistentID) {
        int posDelimiter = persistentID.lastIndexOf(ROLESET_DELIMITER);
        String rolesetName = posDelimiter == -1 ? null : persistentID
                .substring(0, posDelimiter);
        String roleIDasString = posDelimiter == -1 ? persistentID
                : persistentID.substring(posDelimiter + 1);
        int roleID;
        try {
            roleID = Integer.parseInt(roleIDasString);
        } catch (NumberFormatException e) {
            roleID = 0;
        }
        myRoleID = roleID;
        myRoleSetID = rolesetName;

    }

    public String getRoleSetID() {
        return myRoleSetID;
    }

    public int getRoleID() {
        return myRoleID;
    }

    public String asString() {
        return myRoleSetID + ROLESET_DELIMITER + myRoleID;
    }

}
