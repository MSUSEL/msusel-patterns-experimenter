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
package com.jaspersoft.ireport.components.barcode.barcode4j;

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.properties.ByteProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.util.List;
import net.sf.jasperreports.components.barcode4j.BarcodeComponent;
import net.sf.jasperreports.engine.design.JRDesignTextElement;


/**
 *  Class to manage the JRDesignChart.PROPERTY_TITLE_POSITION property
 */
public final class Barcode4JOrientationProperty extends ByteProperty
{

    private final BarcodeComponent component;

    
    public Barcode4JOrientationProperty(BarcodeComponent component)
    {
        super(component);
        this.component = component;
        setName(BarcodeComponent.PROPERTY_ORIENTATION);
        setDisplayName(I18n.getString("barcode4j.property.orientation.name"));
        setShortDescription(I18n.getString("barcode4j.property.orientation.description"));
    }


    @Override
    public List getTagList()
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(new Byte(JRDesignTextElement.ROTATION_NONE), I18n.getString("Global.Property.None")));
        tags.add(new Tag(new Byte(JRDesignTextElement.ROTATION_LEFT), I18n.getString("Global.Property.Left")));
        tags.add(new Tag(new Byte(JRDesignTextElement.ROTATION_RIGHT), I18n.getString("Global.Property.Right")));
        tags.add(new Tag(new Byte(JRDesignTextElement.ROTATION_UPSIDE_DOWN), I18n.getString("Global.Property.UpsideDown")));
        return tags;
    }

    @Override
    public Byte getByte()
    {
        return getOwnByte();
    }

    @Override
    public Byte getOwnByte()
    {
        if (component.getOrientation() == 0) return JRDesignTextElement.ROTATION_NONE;
        if (component.getOrientation() == 90) return JRDesignTextElement.ROTATION_LEFT;
        if (component.getOrientation() == 180) return JRDesignTextElement.ROTATION_UPSIDE_DOWN;
        if (component.getOrientation() == 270) return JRDesignTextElement.ROTATION_RIGHT;

        return 0;
    }

    @Override
    public Byte getDefaultByte()
    {
        return 0;
    }

    @Override
    public void setByte(Byte position)
    {
        if (position == null)
        {
            component.setOrientation(0);
        }
        else if (position.equals(JRDesignTextElement.ROTATION_NONE))
        {
            component.setOrientation(0);
        }
        else if (position.equals(JRDesignTextElement.ROTATION_LEFT))
        {
            component.setOrientation(90);
        }
        else if (position.equals(JRDesignTextElement.ROTATION_UPSIDE_DOWN))
        {
            component.setOrientation(180);
        }
        else if (position.equals(JRDesignTextElement.ROTATION_RIGHT))
        {
            component.setOrientation(270);
        }
    }

}
