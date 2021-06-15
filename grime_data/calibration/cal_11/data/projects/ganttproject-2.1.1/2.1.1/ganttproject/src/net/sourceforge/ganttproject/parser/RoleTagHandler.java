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
/***************************************************************************
 ResourceTagHandler.java  -  description
 -------------------
 begin                : may 2003

 ***************************************************************************/

/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package net.sourceforge.ganttproject.parser;

import net.sourceforge.ganttproject.roles.Role;
import net.sourceforge.ganttproject.roles.RoleManager;
import net.sourceforge.ganttproject.roles.RolePersistentID;
import net.sourceforge.ganttproject.roles.RoleSet;

import org.xml.sax.Attributes;

/** Class to parse the attibute of resources handler */
public class RoleTagHandler implements TagHandler {
    private RoleSet myRoleSet;

    public RoleTagHandler(RoleManager roleManager) {
        myRoleManager = roleManager;
        myRoleManager.clear(); // CleanUP the old stuff
    }

    /**
     * @see net.sourceforge.ganttproject.parser.TagHandler#endElement(String,
     *      String, String)
     */
    public void endElement(String namespaceURI, String sName, String qName) {
        if (qName.equals("roles")) {
            clearRoleSet();
        }
    }

    private void clearRoleSet() {
        myRoleSet = null;
    }

    /**
     * @see net.sourceforge.ganttproject.parser.TagHandler#startElement(String,
     *      String, String, Attributes)
     */
    public void startElement(String namespaceURI, String sName, String qName,
            Attributes attrs) {

        if (qName.equals("roles")) {
            findRoleSet(attrs.getValue("roleset-name"));
        } else if (qName.equals("role")) {
            loadRoles(attrs);
        }
    }

    private void findRoleSet(String roleSetName) {
        if (roleSetName == null) {
            myRoleSet = myRoleManager.getProjectRoleSet();
        } else {
            myRoleSet = myRoleManager.getRoleSet(roleSetName);
            if (myRoleSet == null) {
                myRoleSet = myRoleManager.createRoleSet(roleSetName);
            }
            myRoleSet.setEnabled(true);
        }
    }

    /** Las a role */
    private void loadRoles(Attributes atts) {
        String roleName = atts.getValue("name");
        RolePersistentID persistentID = new RolePersistentID(atts
                .getValue("id"));
        Role existingRole = myRoleSet.findRole(persistentID.getRoleID());
        if (existingRole == null) {
            myRoleSet.createRole(roleName, persistentID.getRoleID());
        }
    }

    private RoleManager getRoleManager() {
        return myRoleManager;
    }

    private final RoleManager myRoleManager;
}
