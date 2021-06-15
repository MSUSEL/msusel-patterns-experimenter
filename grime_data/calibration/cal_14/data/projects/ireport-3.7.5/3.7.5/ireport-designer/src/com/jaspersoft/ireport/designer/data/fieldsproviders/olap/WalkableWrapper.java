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
package com.jaspersoft.ireport.designer.data.fieldsproviders.olap;

import com.jaspersoft.ireport.designer.utils.Misc;
import javax.swing.tree.DefaultMutableTreeNode;
import mondrian.olap.*;
/**
 *
 * @author gtoffoli
 */
public class WalkableWrapper {
    
    Object walkable = null;
    String name = "";
    private DefaultMutableTreeNode parentNode = null;
    
    
    /** Creates a new instance of WalkableWrapper */
    public WalkableWrapper(Object obj, DefaultMutableTreeNode parentNode) {
        this.walkable = obj;
        
        this.parentNode = parentNode;
        if (walkable instanceof QueryAxis)
        {
            name = ((QueryAxis)walkable).getAxisName();
        }
        else if (walkable instanceof FunCall)
        {
            name = ((FunCall)walkable).getFunName();
        }
        else if (walkable instanceof Level)
        {
            name = ((Level)walkable).getName();
        }
        else if (walkable instanceof Hierarchy)
        {
            name = ((Hierarchy)walkable).getName();
        }
        else if (walkable instanceof Member)
        {
            name = ((Member)walkable).getName();
        }
        else
        {
            name = getWalkable() + " (" + walkable.getClass().getName();
        }
    }
    
    public boolean isMeasure()
    {
        return (walkable instanceof Member) && ((Member)walkable).isMeasure();
    }
    
    public String getExpression()
    {
        if (isMeasure())
        {
           int qmarks = parentNode.getChildCount();
           String s = "Data(" + walkable + "";
           for (int i=0; i<qmarks-1; ++i)
           {
               s +=",?";
           }
           s +=")";
           return  s;
        }
        else if (walkable instanceof Level)
        {
            // Find the ancestor dimension...
            DefaultMutableTreeNode node = getParentNode();
            DefaultMutableTreeNode nodeParent = (DefaultMutableTreeNode)getParentNode().getParent();
            while (nodeParent.getParent() != null)
            {
                node = nodeParent;
                nodeParent = (DefaultMutableTreeNode)nodeParent.getParent();
            }
            int axisIndex = nodeParent.getIndex( node );
            
            String s = "";
            switch (axisIndex)
            {
                case 0:
                    s = "Columns";
                    break;
                case 1:
                    s = "Rows";
                    break;
                case 2:
                    s = "Pages";
                    break;
                case 3:
                    s = "Chapters";
                    break;
                case 4:
                    s = "Sections";
                    break; 
                default:
                    s = "Axis(" + axisIndex +")";
            }
            
            s = s + Misc.string_replace("][","].[", ""+ walkable) + "";
            
            return s;
        }
        else
        {
            return null;
        }
        
        //return walkable+"";
    }
    
    public Object getWalkable()
    {
        return walkable;
    }
    
    public void setWalkable(Object w)
    {
        walkable = w;
    }
    
    public String toString()
    {
        return name;
        
    }

    public DefaultMutableTreeNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(DefaultMutableTreeNode parentNode) {
        this.parentNode = parentNode;
    }
    
}
