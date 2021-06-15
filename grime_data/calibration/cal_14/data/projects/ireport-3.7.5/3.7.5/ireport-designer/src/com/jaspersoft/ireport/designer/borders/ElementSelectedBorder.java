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
package com.jaspersoft.ireport.designer.borders;

import org.netbeans.api.visual.border.Border;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;

/**
 * @author David Kaspar
 */
public class ElementSelectedBorder implements Border {

    public static int STATUS_NONE = 0;
    public static int STATUS_PRIMARY_SELECTION = 1;  // blue
    public static int STATUS_SECONDARY_SELECTION = 2;  // gray
    public static int STATUS_PARTIAL_OVERLAP = 3; // green
    public static int STATUS_TOTAL_OVERLAP = 4; // pink
    
    protected static final BasicStroke STROKE = new BasicStroke(1f);
    protected static final Insets INSETS = new Insets(5, 5, 5, 5); //new Insets(9, 8, 10, 9);
    protected Color COLOR_1 = Color.LIGHT_GRAY;
    protected Color COLOR_2 = new Color(255,175,0, 200);
    private static final ImageIcon gripIcon = new ImageIcon(ImageUtilities.loadImage("com/jaspersoft/ireport/designer/borders/grip.png"));
    
    private int status = 0;

    protected Color[] colors = new Color[]{
                                        Color.WHITE,
                                        new Color(70,130,180), //new Color(162,185,206)
                                        Color.LIGHT_GRAY,
                                        Color.GREEN,
                                        Color.PINK
                                    };


    public ElementSelectedBorder() {
    }



    public Insets getInsets () {
        return INSETS;
    }

    public void paint (Graphics2D g, Rectangle bounds) {
        
        
        g.setPaint(COLOR_2);
        g.setStroke(new BasicStroke(2));
        //Rectangle2D r = new Rectangle2D.Double(bounds.x + INSETS.left + 0.5, bounds.y + INSETS.top + 0.5, bounds.width - INSETS.left - INSETS.right - 1.0, bounds.height - INSETS.top - INSETS.bottom - 1.0);
        //g.draw(r);
        Rectangle2D r = new Rectangle2D.Double(bounds.x + INSETS.left -1 + 0.5, bounds.y + INSETS.top -1 + 0.5, bounds.width - INSETS.left - INSETS.right - 1.0 +2, bounds.height - INSETS.top - INSETS.bottom - 1.0+2);
        g.draw(r);
        
        paintGrip(g, bounds.x, bounds.y);
        paintGrip(g, bounds.x + bounds.width-5, bounds.y);
        paintGrip(g, bounds.x, bounds.y + bounds.height-5);
        paintGrip(g, bounds.x + bounds.width-5, bounds.y + bounds.height-5);
        paintGrip(g, bounds.x + (bounds.width/2)-2, bounds.y);
        paintGrip(g, bounds.x + (bounds.width/2)-2, bounds.y + bounds.height-5);
        paintGrip(g, bounds.x, bounds.y + (bounds.height/2)-3);
        paintGrip(g, bounds.x + bounds.width-5, bounds.y + (bounds.height/2)-3);
        /*
        Stroke stroke = g.getStroke ();
        g.setStroke(STROKE);
        g.setPaint(COLOR_2);
        g.draw(new Rectangle2D.Double (bounds.x + INSETS.left - 2.0, bounds.y + INSETS.top - 2.0, bounds.width - INSETS.right - INSETS.left + 4.0, bounds.height - INSETS.top - INSETS.bottom + 4.0));
        
        g.setStroke (stroke);
        
        g.setPaint(COLOR_1);
        //g.draw(bounds);
        g.drawImage(gripIcon.getImage(),bounds.x, bounds.y, gripIcon.getImageObserver());
        g.drawImage(gripIcon.getImage(),bounds.x + bounds.width-5, bounds.y, gripIcon.getImageObserver());
        g.drawImage(gripIcon.getImage(),bounds.x, bounds.y + bounds.height-5, gripIcon.getImageObserver());
        g.drawImage(gripIcon.getImage(),bounds.x + bounds.width-5, bounds.y + bounds.height-5, gripIcon.getImageObserver());
        g.drawImage(gripIcon.getImage(),bounds.x + (bounds.width/2)-2, bounds.y, gripIcon.getImageObserver());
        g.drawImage(gripIcon.getImage(),bounds.x + (bounds.width/2)-2, bounds.y + bounds.height-5, gripIcon.getImageObserver());
        g.drawImage(gripIcon.getImage(),bounds.x, bounds.y + (bounds.height/2)-3, gripIcon.getImageObserver());
        g.drawImage(gripIcon.getImage(),bounds.x + bounds.width-5, bounds.y + (bounds.height/2)-3, gripIcon.getImageObserver());
        */

    }

    public boolean isOpaque() {
        return false;
    }
    
    
    public void paintGrip(Graphics2D g, double x, double y)
    {
        Stroke oldStroke = g.getStroke();
        Paint oldPain = g.getPaint();
        
        Rectangle2D r = new Rectangle2D.Double(x+ 0.5, y + 0.5, 4, 4);
        
        GradientPaint gp = new GradientPaint((float)x,(float)y,Color.WHITE,(float)x+3,(float)y+3, colors[getStatus()]); //new Color(194,209,219));
        g.setPaint(gp);
        
        g.fill(r);
        
        g.setPaint(colors[getStatus()].darker());  //new Color(162,185,206));
        g.setStroke(new BasicStroke(1));
        g.draw(r);
        
        g.setPaint(oldPain);
        g.setStroke(oldStroke);
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }
}

