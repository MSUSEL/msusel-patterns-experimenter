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
package net.sourceforge.ganttproject.resource;

import javax.swing.tree.DefaultMutableTreeNode;

import net.sourceforge.ganttproject.roles.Role;

public class ResourceNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 3834033541318392117L;

    private final ProjectResource resource;

    public ResourceNode(ProjectResource res) {
        super(res);
        resource = res;
    }

    public void setName(String name) {
        resource.setName(name);
    }

    public String getName() {
        return resource.getName();
    }

    public void setPhone(String phoneNumber) {
        if (resource instanceof HumanResource)
            ((HumanResource) resource).setPhone(phoneNumber);
    }

    public String getPhone() {
        if (resource instanceof HumanResource)
            return ((HumanResource) resource).getPhone();
        return null;
    }

    public void setEMail(String email) {
        if (resource instanceof HumanResource)
            ((HumanResource) resource).setMail(email);
    }

    public String getEMail() {
        if (resource instanceof HumanResource)
            return ((HumanResource) resource).getMail();
        return null;
    }

    public void setDefaultRole(Role defRole) {
        if (resource instanceof HumanResource)
            ((HumanResource) resource).setRole(defRole);
    }

    public Role getDefaultRole() {
        if (resource instanceof HumanResource)
            return ((HumanResource) resource).getRole();
        return null;
    }
    
    /* gets the value of a custom field referenced by it's title */
    public Object getCustomField(String title) {
    	if (resource instanceof HumanResource)
            return ((HumanResource) resource).getCustomFieldVal(title);
        return null;
    }
    
    /* gets the new value to the custom field referenced by it's title */
    public void setCustomField(String title, Object val) {
    	if (resource instanceof HumanResource)
            ((HumanResource) resource).setCustomFieldVal(title, val);
    }

    /**
     * @inheritDoc
     */
    public String toString() {
        if (resource != null)
            return resource.getName();
        return "-";
    }

    public ProjectResource getResource() {
        return resource;
    }

    public boolean equals(Object obj) {
        boolean res = false;
        if (this == obj)
            return true;
        if (obj instanceof ResourceNode) {
            ResourceNode rn = (ResourceNode) obj;
            res = rn.getUserObject() != null
                    && rn.getUserObject().equals(this.getUserObject());
        }
        return res;
    }
}
