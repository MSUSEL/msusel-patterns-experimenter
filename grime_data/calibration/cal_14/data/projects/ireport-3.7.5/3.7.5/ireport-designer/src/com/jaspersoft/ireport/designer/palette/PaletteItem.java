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
package com.jaspersoft.ireport.designer.palette;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.outline.OutlineTopComponent;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Image;
import java.util.Properties;
import org.openide.util.ImageUtilities;

/**
 *
 * @author gtoffoli
 */
public class PaletteItem {

    private Properties props;
    
    private Image icon16;
    private Image icon32;
    
    private Object data = null;
    
    public static final String PROP_ID = "id";
    public static final String PROP_NAME = "name";
    public static final String PROP_COMMENT = "tooltip";
    public static final String PROP_ICON16 = "icon16";
    public static final String PROP_ICON32 = "icon32";
    public static final String ACTION = "action";
    
    /** Creates a new instance of PaletteItem */
    public PaletteItem( Properties props ) {
        this.props = props;
        loadIcons();
    }
    public String getId() {
        return props.getProperty( PROP_ID );
    }
    public String getDisplayName() {
        if ( props.getProperty( PROP_NAME ) != null)
        {
            try {
                return I18n.getString(props.getProperty( PROP_NAME ));
            } catch (Exception ex)
            {
                return props.getProperty( PROP_NAME );
            }
        }
        return null;
    }
    
    public String getComment() {
        if (props.getProperty( PROP_COMMENT ) != null)
        {
            
            try {
                return I18n.getString(props.getProperty( PROP_COMMENT ));
            } catch (Exception ex)
            {
                return props.getProperty( PROP_COMMENT );
            }
        }
        return null;
    }
    public Image getSmallImage() {
        return icon16;
    }
    public Image getBigImage() {
        return icon32;
    }
    @Override
    public boolean equals(Object obj) {
        if( obj instanceof PaletteItem ) {
            return getId().equals( ((PaletteItem)obj).getId()  );
        }
        return false;
    }
    private void loadIcons() {
        String iconId = props.getProperty( PROP_ICON16 );
        if (iconId == null) return;
        icon16 = ImageUtilities.loadImage( iconId );
        iconId = props.getProperty( PROP_ICON32 );
        icon32 = ImageUtilities.loadImage( iconId );
    }
    
    public void drop(java.awt.dnd.DropTargetDropEvent dtde)
    {
        if (props.getProperty( ACTION ) != null)
        {
            try {
                PaletteItemAction pia = (PaletteItemAction)Class.forName( props.getProperty( ACTION ), true, Thread.currentThread().getContextClassLoader()).newInstance();
                pia.setJasperDesign( IReportManager.getInstance().getActiveReport());
                pia.setPaletteItem(this);
                pia.setScene( OutlineTopComponent.getDefault().getCurrentJrxmlVisualView().getReportDesignerPanel().getActiveScene());
            
                pia.drop(dtde);
                
            } catch (Throwable t)
            {
                t.printStackTrace();
            }
        }
    }
    
    /**
     * This field can be used by special palette item implementations... 
     **/
    public Object getData()
    {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
}
