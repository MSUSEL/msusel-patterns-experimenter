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

import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.language.GanttLanguage.Event;

/**
 * Created by IntelliJ IDEA.
 * 
 * @author bard Date: 25.01.2004
 */
public class RoleImpl implements Role {
    private String myName;

    private final int myID;

    private final RoleSet myRoleSet;

    public RoleImpl(int id, String name, RoleSet roleSet) {
        myID = id;
        myName = name;
        myRoleSet = roleSet;
        
        GanttLanguage.getInstance().addListener(new GanttLanguage.Listener() {
            public void languageChanged(Event event) {
                Role role = myRoleSet.findRole(myID);
                if (role != null) {
                    myName = role.getName();
                }
            }
        });
    }

    public int getID() {
        return myID;
    }

    public String getName() {
        return myName;
    }
    
    public void setName(String name) {
        myName = name;
    }

    public String getPersistentID() {
        return (myRoleSet.getName() == null ? "" : myRoleSet.getName() + ":")
                + getID();
    }

    public String toString() {
        return getName();
    }

}
