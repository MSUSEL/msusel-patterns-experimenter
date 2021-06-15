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
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.geometry.visualization;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * @author Jackson Roehrig & Sanjay Jena
 *
 *
 *
 *
 * @source $URL$
 */
public class ObjectScreen extends JFrame {
    
	private static final long serialVersionUID = 3573830165005892925L;
	
	private Container cp;


    public static void main(String[] args) {
        ObjectScreen app1 =  new ObjectScreen();
        app1.pack();
        app1.setVisible(true);
    }
    
    public ObjectScreen() {
        this.cp = this.getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(BorderLayout.NORTH, new JLabel("Painting GM_Object"));
    }
    
    /**
     * 
     * @author sanjay
     *
     */
    public class graphicObject extends JPanel {
        
        /**
		 * Comment for <code>serialVersionUID</code>
		 */
		private static final long serialVersionUID = 1L;
		private int m_x[];
        private int m_y[];
        private int m_objectType = 0;
        private int m_scale = 1;
        private int m_YOrientation = -1;
        
        /* Coordinate System */
        private int sizeX = 150;
        private int sizeY = 150;
        
        public graphicObject(int x[], int y[], int objectType, int scale){
            this.m_x = x;
            this.m_y = y;
            this.m_objectType = objectType;
            if (scale > 0)
                this.m_scale = scale;
            setBackground(Color.WHITE);
        }
        
        
        public void paintComponent(Graphics g) {
            
            
            super.paintComponent(g);

            if (this.m_YOrientation < 0)
                g.translate(5, (this.sizeY)*this.m_scale);

            paintCoordinateSystem(g);

            g.setColor(Color.BLACK);

            for (int i=0; i<this.m_x.length-1; i++) {
	            if (this.m_objectType == PaintGMObject.TYPE_LINESTRING) {
	                g.drawLine(this.m_x[i] *this.m_scale , this.m_y[i] *this.m_scale *this.m_YOrientation, this.m_x[i+1] *this.m_scale, this.m_y[i+1] *this.m_scale *this.m_YOrientation);
	            } else if (this.m_objectType == PaintGMObject.TYPE_POINT) {
	                g.drawLine(this.m_x[i] *this.m_scale , this.m_y[i] *this.m_scale *this.m_YOrientation, this.m_x[i] *this.m_scale, this.m_y[i] *this.m_scale *this.m_YOrientation);
	            } else if (this.m_objectType == PaintGMObject.TYPE_LINES) {
	                g.drawLine(this.m_x[i] *this.m_scale , this.m_y[i] *this.m_scale *this.m_YOrientation, this.m_x[i+1] *this.m_scale, this.m_y[i+1] *this.m_scale *this.m_YOrientation);
	                i++;
	            }
            }
            
        }
        
        public Dimension getMinimumSize(){
            return new Dimension(150,100);
        }
        
        public Dimension getPreferredSize(){
            return getMinimumSize();
        }

        private void paintCoordinateSystem(Graphics g) {

            g.setColor(Color.CYAN);
            int xMax = this.sizeX *this.m_scale;
            int yMax = this.sizeY *this.m_scale *this.m_YOrientation;
            
            /* Horizontale Linien */
            for (int i=0; i<=(int)(this.sizeY/10); i++) {
                g.drawLine(i*10 *this.m_scale, 0 , i*10 *this.m_scale, yMax);
            }
            /* Vertikale Linien */
            for (int i=0; i<=(int)(this.sizeX/10); i++) {
                g.drawLine(0, i*10*this.m_scale *this.m_YOrientation, xMax, i*10*this.m_scale *this.m_YOrientation);
            }
        }
        
    }
    
    public void paintObject(int x[], int y[], int objectType, int scale) {

        graphicObject graphics1 = new graphicObject(x, y, objectType, scale);
        this.cp.add(BorderLayout.CENTER, graphics1);
       
    }
    


}
