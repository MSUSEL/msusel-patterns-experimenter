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
package org.geotools.styling.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;

/**
 * 
 *
 * @source $URL$
 */
public class ColorMapBuilder extends AbstractStyleBuilder<ColorMap> {

    int type = ColorMap.TYPE_RAMP;

    boolean extended = false;

    List<ColorMapEntry> entries = new ArrayList<ColorMapEntry>();

    ColorMapEntryBuilder colorMapEntryBuilder = null;

    public ColorMapBuilder() {
        this(null);
    }

    public ColorMapBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public ColorMapBuilder type(int type) {
        this.type = type;
        unset = false;
        return this;
    }

    public ColorMapBuilder extended(boolean extended) {
        this.extended = extended;
        unset = false;
        return this;
    }

    public ColorMapEntryBuilder entry() {
        if (colorMapEntryBuilder != null && !colorMapEntryBuilder.isUnset()) {
            entries.add(colorMapEntryBuilder.build());
            unset = false;
        }
        colorMapEntryBuilder = new ColorMapEntryBuilder();
        return colorMapEntryBuilder;
    }

    public ColorMap build() {
        // force the dump of the last entry builder
        entry();

        if (unset) {
            return null;
        }
        ColorMap colorMap = sf.createColorMap();
        colorMap.setType(type);
        colorMap.setExtendedColors(extended);
        for (ColorMapEntry entry : entries) {
            colorMap.addColorMapEntry(entry);
        }
        if (parent == null) {
            reset();
        }
        return colorMap;
    }

    public ColorMapBuilder reset() {
        type = ColorMap.TYPE_RAMP;
        extended = false;
        entries = new ArrayList<ColorMapEntry>();
        unset = false;
        return this;
    }

    public ColorMapBuilder reset(ColorMap original) {
        if (original == null) {
            return reset();
        }
        type = original.getType();
        extended = original.getExtendedColors();
        entries = new ArrayList<ColorMapEntry>(Arrays.asList(original.getColorMapEntries()));
        unset = false;
        return this;
    }

    public ColorMapBuilder unset() {
        return (ColorMapBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().raster().colorMap().init(this);
    }

}
