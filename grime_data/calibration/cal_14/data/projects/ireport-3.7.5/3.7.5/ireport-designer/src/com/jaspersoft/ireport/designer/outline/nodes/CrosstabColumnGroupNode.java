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
package com.jaspersoft.ireport.designer.outline.nodes;

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.properties.ByteProperty;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabGroup;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.crosstabs.type.CrosstabColumnPositionEnum;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class CrosstabColumnGroupNode extends CrosstabGroupNode implements PropertyChangeListener {

    private JRDesignCrosstabColumnGroup group = null;

    public CrosstabColumnGroupNode(JasperDesign jd, JRDesignCrosstab crosstab, JRDesignCrosstabColumnGroup group, Lookup doLkp)
    {
        super (jd, crosstab, group, doLkp);
        this.group = group;
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/crosstabcolumns-16.png");
    }

    /**
     *  This is the function to create the sheet...
     * 
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();

        Sheet.Set set = sheet.get(Sheet.PROPERTIES);
        set.put(new PositionProperty(group,getCrosstab()));
        sheet.put(set);
        
        return sheet;
    }
    
    
    @Override
    public List<JRDesignCrosstabGroup> getGroups() {
        List list = Arrays.asList(getCrosstab().getColumnGroups());
        return (List<JRDesignCrosstabGroup>)list;
    }

    @Override
    public int getType() {
        return COLUMN_GROUP;
    }


    /**
     *  Class to manage the JRDesignElement.PROPERTY_POSITION_TYPE property
     */
    public static final class PositionProperty extends ByteProperty
    {
        private final JRDesignCrosstabColumnGroup group;
        private final JRDesignCrosstab crosstab;

        @SuppressWarnings("unchecked")
        public PositionProperty(JRDesignCrosstabColumnGroup group, JRDesignCrosstab crosstab)
        {
            super(group);
            setName( JRDesignCrosstabColumnGroup.PROPERTY_POSITION );
            setDisplayName("Header Position");
            setShortDescription("This property set the position of the content of the group header cell");
            this.crosstab = crosstab;
            this.group = group;
        }

        @Override
        public List getTagList()
        {
            List tags = new java.util.ArrayList();
            tags.add(new Tag(CrosstabColumnPositionEnum.LEFT.getValueByte(), "Left"));
            tags.add(new Tag(CrosstabColumnPositionEnum.CENTER.getValueByte(), "Center"));
            tags.add(new Tag(CrosstabColumnPositionEnum.RIGHT.getValueByte(), "Right"));
            tags.add(new Tag(CrosstabColumnPositionEnum.STRETCH.getValueByte(), "Stretch"));
            return tags;
        }

        @Override
        public Byte getByte()
        {
            return (group.getTotalPositionValue()) == null ? null : group.getTotalPositionValue().getValueByte();
        }

        @Override
        public Byte getOwnByte()
        {
            return getByte();
        }

        @Override
        public Byte getDefaultByte()
        {
            return null;
        }

        @Override
        public void setByte(Byte positionType)
        {
            group.setTotalPosition(positionType);
        }

    }
}
