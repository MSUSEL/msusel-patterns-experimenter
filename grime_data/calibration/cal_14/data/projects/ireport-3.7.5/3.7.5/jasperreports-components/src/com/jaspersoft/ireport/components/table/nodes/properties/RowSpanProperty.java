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
package com.jaspersoft.ireport.components.table.nodes.properties;

import com.jaspersoft.ireport.components.table.TableCell;
import com.jaspersoft.ireport.components.table.TableElementNode;
import com.jaspersoft.ireport.components.table.TableMatrix;
import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.sheet.properties.IntegerProperty;
import com.jaspersoft.ireport.designer.undo.PropertyUndoableEdit;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.NbBundle;

    
/**
 *  Class to manage the JRDesignElement.PROPERTY_HEIGHT property
 */
public final class RowSpanProperty extends IntegerProperty
{
    private final DesignCell cell;
    private final StandardTable table;
    private final JasperDesign jd;

    @SuppressWarnings("unchecked")
    public RowSpanProperty(DesignCell cell, StandardTable table, JasperDesign jd)
    {
        super(cell);
        this.cell = cell;
        this.table = table;
        this.jd = jd;
    }

    @Override
    public String getName()
    {
        return DesignCell.PROPERTY_ROW_SPAN;
    }

    @Override
    public String getDisplayName()
    {
        return NbBundle.getMessage(TableElementNode.class, "cell.rowSpan.property");
    }

    @Override
    public String getShortDescription()
    {
        return NbBundle.getMessage(TableElementNode.class, "cell.rowSpan.property.description");
    }

    @Override
    public Integer getInteger()
    {
        return getOwnInteger();
    }

    @Override
    public Integer getOwnInteger()
    {
        return (cell.getRowSpan() == null) ? getDefaultInteger() : cell.getRowSpan();
    }

    @Override
    public Integer getDefaultInteger()
    {
        return 1;
    }

    @Override
    public void setInteger(Integer span)
    {
        // The row span can be set ONLY for headers of StandardColumnGroups...
        TableMatrix matrix = TableModelUtils.createTableMatrix(table, jd);

        TableCell tableCell = matrix.getTableCell(cell);

        int oldSpan = tableCell.getRowSpan(); // get the real row span..

        int changedHLine = -1;
        List<Integer> hlines = new ArrayList<Integer>( matrix.getHorizontalSeparators() );

        if (span != oldSpan)
        {
            // Force the correct height of this cell to be then correctly fixed..
            // Adjust the height of children....
            cell.setHeight(matrix.getHorizontalSeparators().get( tableCell.getRow()+span) - matrix.getHorizontalSeparators().get( tableCell.getRow()));

        }
        // We need to add the undo operation checking the changed rows...
        cell.setRowSpan(span);

        TableModelUtils.fixTableLayout(table, jd);

        matrix.processMatrix();

        for (int i=0; i<hlines.size(); ++i)
        {
            if (hlines.get(i).intValue() != matrix.getHorizontalSeparators().get(i).intValue())
            {
                
                // Restore the cell height having this bottom line...
                int delta = matrix.getHorizontalSeparators().get(i).intValue() - hlines.get(i).intValue();

                System.out.println("Fixing cells on the changed line: " + i + "Delta: " + delta);
                System.out.flush();
                
                // set the new cell hight for all the cells in this row...
                List<TableCell> cells = matrix.getCells();

                for (TableCell cell : cells)
                {
                    if (cell.getRow()+cell.getRowSpan() == i &&  cell.getCell() != null)
                    {
                        cell.getCell().setHeight(cell.getCell().getHeight() - delta);
                    }
                }
                break;
            }
        }

        // Restore cell heights where changed...
        cell.getEventSupport().firePropertyChange("ROW_HEIGHT", null, span);
    }

    /**
     * Overridden because the undo operation is create
     * @param newValue
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Override
    public void setValue(Object newValue) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        Object oldValue = getOwnPropertyValue();

        validate(newValue);

        // Save the height of cells below this

        setPropertyValue(newValue);

        
        PropertyUndoableEdit undo =
            new PropertyUndoableEdit(
                this,
                oldValue,
                newValue
                );
        IReportManager.getInstance().addUndoableEdit(undo);
        
    }

    @Override
    public void validateInteger(Integer span)
    {
        if (span < 1)
        {
            throw annotateException(NbBundle.getMessage(TableElementNode.class, "cell.rowSpan.validationError"));
        }

        TableMatrix matrix = TableModelUtils.createTableMatrix(table, jd);

        TableCell tableCell = matrix.getTableCell(cell);

        int maxSpan = matrix.getMaxRowSpan(cell);
        if (span > maxSpan)
        {
            throw annotateException(NbBundle.getMessage(TableElementNode.class, "cell.rowSpan.maxSpan", maxSpan));
        }
    }

}
