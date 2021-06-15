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
package com.jaspersoft.ireport.designer.crosstab;

import com.jaspersoft.ireport.designer.ModelUtils;
import javax.swing.JButton;
import net.sf.jasperreports.crosstabs.design.JRCrosstabOrigin;

/**
 *
 * @version $Id: JCellButton.java 0 2009-10-19 19:15:18 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class JCellButton extends JButton {

    private CellInfo cellInfo = null;

    public JCellButton(CellInfo cellInfo)
    {
        super();
        setToolTipText(ModelUtils.nameOf(cellInfo.getCellContents()));
        this.cellInfo = cellInfo;
        refreshText();
    }

    public void refreshText()
    {
        JRCrosstabOrigin origin = cellInfo.getCellContents().getOrigin();
        switch (origin.getType())
        {
            case JRCrosstabOrigin.TYPE_HEADER_CELL:
                setText("H");
                break;
            case JRCrosstabOrigin.TYPE_DATA_CELL:
                setText("D/D");
                break;
            case JRCrosstabOrigin.TYPE_COLUMN_GROUP_HEADER:
                setText("CH");
                break;
            case JRCrosstabOrigin.TYPE_COLUMN_GROUP_TOTAL_HEADER:
                setText("TCH");
                break;
            case JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER:
                setText("RH");
                break;
            case JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER:
                setText("TRH");
                break;
            case JRCrosstabOrigin.TYPE_WHEN_NO_DATA_CELL:
                setText("NDC");
                break;
        }

        setText( "<html>" + getText() + " [" + getCellInfo().getX() + "," + getCellInfo().getY()+"]<br>(" +getCellInfo().getLeft() + "," + getCellInfo().getTop() + "," + getCellInfo().getCellContents().getWidth() + "," + getCellInfo().getCellContents().getHeight() + ")");
    }

  
    /**
     * @return the cellInfo
     */
    public CellInfo getCellInfo() {
        return cellInfo;
    }

    /**
     * @param cellInfo the cellInfo to set
     */
    public void setCellInfo(CellInfo cellInfo) {
        this.cellInfo = cellInfo;
    }


}
