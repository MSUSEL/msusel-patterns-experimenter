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
package com.jaspersoft.ireport.designer.templates;

import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import net.sf.jasperreports.engine.JRFont;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author gtoffoli
 */
public class TemplateDescriptor {

    private TemplateCategory category = new TemplateCategory();
    private String   displayName;
    private ImageIcon  image;
    private long timestamp = 0;
    private File     file;
    private Dimension pageSize = null;
    
    /**
     * The image is reloaded if changed on disk.
     * The method looks for an image with the same name of the template
     * and with extension of type png,gif,jpg,jpeg
     * 
     * @return the image
     */
    public ImageIcon getPreviewIcon() {

        String filePath = getFile().getPath();
        
        String[] extensions = new String[]{".png",".gif",".jpg",".jpeg"};
        File f = null;
        for (int i=0; i<extensions.length; ++i)
        {
            String fname = Misc.changeFileExtension(filePath, ".png");
            f = new File(fname);
            if (f.exists())
            {
                break;
            }
            else
            {
                f = null;
            }
        }


        if (f != null && (image == null || timestamp < f.lastModified()))
        {
            this.image = new ImageIcon(f.getPath());
            timestamp = f.lastModified();
        }
        else if (image == null && getPageSize() != null)
        {
            BufferedImage img = new BufferedImage(getPageSize().width, getPageSize().height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gfx = img.createGraphics();
            GradientPaint gradient = new GradientPaint(0,getPageSize().height,  new Color(232,232,232,232), getPageSize().width, 0, Color.WHITE);
            gfx.setPaint(gradient);
            gfx.fillRect(0,0,getPageSize().width, getPageSize().height);
            gfx.setFont(new Font("SandSerif",Font.PLAIN, getPageSize().height/3));
            FontMetrics fm = gfx.getFontMetrics();
            gfx.setColor(Color.DARK_GRAY);
            gfx.drawRect(0,0,getPageSize().width-1, getPageSize().height-1);
            Rectangle2D rect = fm.getStringBounds("?", gfx);
            gfx.drawString("?", (int)((getPageSize().width - rect.getWidth())/2), (int)( (getPageSize().height/3) + (getPageSize().height - rect.getHeight())/2));
            this.image = new ImageIcon(img);
        }
        return image;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    /**
     * @return the category
     */
    public void setCategory(TemplateCategory cat) {
        this.category = cat;
    }

    /**
     * @return the category
     */
    public TemplateCategory getCategory() {
        return category;
    }

    public FileObject getFileObject()
    {
        if (getFile().exists())
        {
            return FileUtil.toFileObject(getFile());
        }
        return null;
    }

    /**
     * @return the pageSize
     */
    public Dimension getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(Dimension pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof TemplateDescriptor)
        {
            return ((TemplateDescriptor)obj).getFile().equals(getFile());
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.displayName != null ? this.displayName.hashCode() : 0);
        hash = 79 * hash + (this.file != null ? this.file.hashCode() : 0);
        return hash;
    }



}
