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
 * Created on 18.06.2004
 *
 */
package net.sourceforge.ganttproject.roles;

import java.util.ArrayList;

/**
 * @author bard
 */
public class RoleSetImpl implements RoleSet {
    private final String myName;

    private final ArrayList myRoles = new ArrayList();

    private boolean isEnabled;

	private final RoleManagerImpl myRoleManager;

    RoleSetImpl(String name, RoleManagerImpl roleManager) {
        myName = name;
        myRoleManager = roleManager;
    }

    public String getName() {
        return myName;
    }

    public Role[] getRoles() {
        return (Role[]) myRoles.toArray(new Role[0]);
    }

    public Role createRole(String name, int persistentID) {
        RoleImpl result = new RoleImpl(persistentID, name, this);
        myRoles.add(result);
        myRoleManager.fireRolesChanged(this);
        return result;
    }

    public void deleteRole(Role role) {
        myRoles.remove(role);
        myRoleManager.fireRolesChanged(this);
    }
    
    public void changeRole(String name, int roleID) {
        Role role = findRole(roleID);
        if (role != null) {
            role.setName(name);
        }
    }

    public Role findRole(int roleID) {
        Role result = null;
        for (int i = 0; i < myRoles.size(); i++) {
            Role next = (Role) myRoles.get(i);
            if (next.getID() == roleID) {
                result = next;
                break;
            }
        }
        return result;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String toString() {
        return getName();
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        myRoleManager.fireRolesChanged(this);
    }

    public boolean isEmpty() {
        return myRoles.isEmpty();
    }

    public void clear() {
        myRoles.clear();

    }

    void importData(RoleSet original) {
        Role[] originalRoles = original.getRoles();
        for (int i = 0; i < originalRoles.length; i++) {
            Role nextRole = originalRoles[i];
            createRole(nextRole.getName(), nextRole.getID());
        }
    }

}
