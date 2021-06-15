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

import net.sourceforge.ganttproject.CustomPropertyDefinition;
import net.sourceforge.ganttproject.CustomPropertyManager;
import net.sourceforge.ganttproject.gui.TableHeaderUIFacade;

import org.jdesktop.swing.table.TableColumnExt;

/**@author nokiljevic
 *  
 * Describes one column in the resources table. */ 
    public class ResourceColumn implements CustomPropertyDefinition, TableHeaderUIFacade.Column
    {
    	/** Swing column composant representing the column */
    	TableColumnExt column;
    	/** Datatype of the column */
    	Class type;
    	/** Default value for the column */
    	Object defaultVal;
    	/** Visible on the screen */
    	boolean visible;
    	/** Default index in the table. When the column is shown
    	 *  that index will be forced. */
    	int defaultIndex;
		private int myOrder;
    	
//    	public ResourceColumn(String name, int index, Class type) {
//    		column = new TableColumnExt(index);
//    		this.type = type;
//    		this.setTitle(name);
//    		defaultIndex = index;
//    	}
//    	
    	public ResourceColumn(TableColumnExt col, int index){
    		this(col, index, String.class);
    	}
    	
    	public ResourceColumn(TableColumnExt col, int index, Class type){
    		column = col;
    		this.type = type;
    		defaultIndex = index;
    		visible = true;
    	}
    	
//    	public ResourceColumn(TableColumnExt col, int index, Class type, Object def) {
//    		column = col;
//    		defaultIndex = index;
//    		visible = true;
//    		this.type = type;
//    		defaultVal = def;
//    	}
    	
    	public boolean nameCmp(String name) {
    		System.out.println("comparing: "+name+" - "+column.getTitle()+" ");
    		return column.getTitle().equals(name);
    	}
    	
    	public void setTitle(String title) {
    		column.setTitle(title);
    	}
    	
    	public String getTitle() {
    		return column.getTitle();
    	}
    	
    	public boolean isVisible() {
    		return visible;
    	}
    	
    	public void setVisible(boolean vis) {
    		visible = vis;
    	}

		public int getIndex() {
			return defaultIndex;
		}

//		public void setIndex(int defaultIndex)  {
//			this.defaultIndex = defaultIndex;
//		}
//		
		public TableColumnExt getColumn() {
			return column;
		}
		
		public Object getDefaultVal() {
			return defaultVal;
		}
		
		public void setDefaultVal(Object defaultVal) {
			this.defaultVal = defaultVal;
		}
		
		public Class getType() {
			return type;
		}
		
		public void setType(Class type) {
			this.type = type;
		}

		public Object getDefaultValue() {
			return defaultVal;
		}

		public String getDefaultValueAsString() {
			return HumanResourceManager.getValueAsString(defaultVal);
		}
		public String getID() {
			return String.valueOf(defaultIndex);
		}

		public String getName() {
			return getTitle();
		}

		public String getTypeAsString() {
			return CustomPropertyManager.PropertyTypeEncoder.encodeFieldType(type);
		}

		public int getOrder() {
			return myOrder;
		}

		public int getWidth() {
			return column.getWidth();
		}
		
		public void setWidth(int width) {
			column.setWidth(width);
		}

		public void setOrder(int order) {
			myOrder = order;
		}
    }