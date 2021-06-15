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
package com.jaspersoft.ireport.jasperserver.ui;

import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

/**
 *
 * @author gtoffoli
 */
public class RepositoryListCellRenderer extends DefaultListCellRenderer  {
    
    final static ImageIcon serverIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/server.png"));
    final static ImageIcon folderIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/folder.png"));
    final static ImageIcon reportUnitIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/reportunit.png"));
    final static ImageIcon datasourceJndiIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/datasource_jndi.png"));
    final static ImageIcon datasourceIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/datasource.png"));
    final static ImageIcon datasourceJdbcIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/datasource_jdbc.png"));
    final static ImageIcon imageIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/picture.png"));
    final static ImageIcon jrxmlIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/jrxml_file.png"));
    final static ImageIcon refIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/link.png"));
    final static ImageIcon bundleIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/bundle.png"));
    final static ImageIcon fontIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/font.png"));
    final static ImageIcon jarIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/jar.png"));
    final static ImageIcon inputcontrolIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/inputcontrol.png"));
    final static ImageIcon datatypeIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/datatype.png"));
    final static ImageIcon lovIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/lov.png"));
    final static ImageIcon datasourceBeanIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/datasource_bean.png"));
    final static ImageIcon unknowIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/unknow.png"));
    final static ImageIcon queryIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/query.png"));
    final static ImageIcon waitingIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/waiting.png"));
    final static ImageIcon reportOptionsResourceIcon  = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/reportunit_options.png"));
    
    
    boolean comboboxMode = false;
    /* This is the only method defined by ListCellRenderer.  We just
     * reconfigure the Jlabel each time we're called.
     */
    public RepositoryListCellRenderer(boolean mode)
    {
        super();
        this.comboboxMode = mode;
    }
    
    public RepositoryListCellRenderer()
    {
        super();
    }
    
    public Component getListCellRendererComponent(
        JList list,
	Object value,   // value to display
	int index,      // cell index
	boolean iss,    // is the cell selected
	boolean chf)    // the list and the cell have the focus
    {
        /* The DefaultListCellRenderer class will take care of
         * the JLabels text property, it's foreground and background
         * colors, and so on.
         */
        super.getListCellRendererComponent(list, value, index, iss, chf);

        /* We additionally set the JLabels icon property here.
         */
        if (value instanceof ResourceDescriptor)
        {
            ResourceDescriptor rd = (ResourceDescriptor)value;
            
            if (rd.getUriString().equals("/"))
            {
                setIcon(getResourceIcon( null ) );
                
                setText( JasperServerManager.getString("misc.labelRepositoryRoot","Repository root (/)") );
            }
            else
            {
                
                ImageIcon iconImage = getResourceIcon( rd );
                
                if (comboboxMode && index > 0)
                {
                    Image image = new java.awt.image.BufferedImage(iconImage.getIconWidth() + 8*index, iconImage.getIconHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB );
                    
                    Graphics g = image.getGraphics();
                    
                    g.setColor( (iss) ? list.getSelectionBackground() : list.getBackground() );
                    g.fillRect(0,0,image.getWidth(null), image.getHeight(null));
                    g.drawImage(iconImage.getImage(), 8*index,0,null);
                    
                    
                    setIcon(new ImageIcon(image));
                }
                else
                {
                  setIcon(iconImage);  
                }
                setText( rd.getName() );
            }
        }
	return this;
    }
    
    public static ImageIcon getResourceIcon(ResourceDescriptor resource)
    {
        if (resource == null) return serverIcon;
        else if (resource.getWsType() == null) return serverIcon;
        else if (resource.getIsReference()) return refIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_FOLDER)) return folderIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_REPORTUNIT)) return reportUnitIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_JNDI)) return datasourceJndiIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_JDBC)) return datasourceJdbcIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_BEAN)) return datasourceBeanIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_OLAP_XMLA_CONNECTION)) return datasourceIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_IMAGE)) return imageIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_JRXML)) return jrxmlIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_FONT)) return fontIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_CLASS_JAR)) return jarIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_RESOURCE_BUNDLE)) return bundleIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_INPUT_CONTROL)) return inputcontrolIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_DATA_TYPE)) return datatypeIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_LOV)) return lovIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_QUERY)) return queryIcon;
        else if (resource.getWsType().equals("ReportOptionsResource")) return reportOptionsResourceIcon;
        return unknowIcon;
    }
}
