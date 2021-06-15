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

import java.util.EventListener;
import java.util.EventObject;

/**
 * @author athomas
 */
public interface RoleManager {
    public RoleSet createRoleSet(String name);

    public RoleSet[] getRoleSets();

    /** Clear the role list */
    public void clear();

    /** Return all roles exept the default roles */
    // public String [] getRolesShort();
    public Role[] getProjectLevelRoles();

    /** Load roles from the file */
    /** Add a role on the list */
    public void add(int ID, String role);

    public class Access {
        public static RoleManager getInstance() {
            return ourInstance;
        }

        private static RoleManager ourInstance = new RoleManagerImpl();
    }

    public static int DEFAULT_ROLES_NUMBER = 11;

    public RoleSet getProjectRoleSet();

    public RoleSet getRoleSet(String rolesetName);

    public Role[] getEnabledRoles();

    public Role getDefaultRole();

    public void importData(RoleManager roleManager);

	public void addRoleListener(Listener listener);

	public void removeRoleListener(Listener listener);
	
	public interface Listener extends EventListener {
		public void rolesChanged(RoleEvent e);
	}
	
	public class RoleEvent extends EventObject {
		private RoleSet myChangedRoleSet;

		public RoleEvent(RoleManager source, RoleSet changedRoleSet) {
			super(source);
			myChangedRoleSet = changedRoleSet;
		}
		
		public RoleSet getChangedRoleSet() {
			return myChangedRoleSet;
		}
	}
}
