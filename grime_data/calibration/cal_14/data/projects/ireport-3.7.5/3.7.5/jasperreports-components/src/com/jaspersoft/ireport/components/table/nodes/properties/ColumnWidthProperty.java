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

import com.jaspersoft.ireport.components.table.TableElementNode;
import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.designer.sheet.properties.IntegerProperty;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.NbBundle;

    
/**
 *  Class to manage the JRDesignElement.PROPERTY_HEIGHT property
 */
public final class ColumnWidthProperty extends IntegerProperty
{
    private final StandardBaseColumn column;
    private final StandardTable table;
    private final JasperDesign jd;

    @SuppressWarnings("unchecked")
    public ColumnWidthProperty(StandardBaseColumn column, StandardTable table, JasperDesign jd)
    {
        super(column);
        this.column = column;
        this.table = table;
        this.jd = jd;
    }

    @Override
    public String getName()
    {
        return StandardBaseColumn.PROPERTY_WIDTH;
    }

    @Override
    public String getDisplayName()
    {
        return NbBundle.getMessage(TableElementNode.class, "column.width.property");
    }

    @Override
    public String getShortDescription()
    {
        return NbBundle.getMessage(TableElementNode.class, "column.width.property.description");
    }

    @Override
    public Integer getInteger()
    {
        return getOwnInteger();
    }

    @Override
    public Integer getOwnInteger()
    {
        return column.getWidth();
    }

    @Override
    public Integer getDefaultInteger()
    {
        return 0;// FIXMEGT: we should get the max height for this row...
    }

    @Override
    public void setInteger(Integer height)
    {
        column.setWidth(height); // FIXMEGT: we should update the with of the involved columns...
        TableModelUtils.fixTableLayout(table, jd);
        column.getEventSupport().firePropertyChange("COLUMN_WIDTH", null, height);
    }

    @Override
    public void validateInteger(Integer height)
    {
        if (height < 0)
        {
            throw annotateException(NbBundle.getMessage(TableElementNode.class, "column.width.validationError"));
        }
    }

    /**
     * @return the column
     */
    public StandardBaseColumn getColumn() {
        return column;
    }

}
