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

import org.geotools.styling.ChannelSelection;

/**
 * 
 *
 * @source $URL$
 */
public class ChannelSelectionBuilder extends AbstractStyleBuilder<ChannelSelection> {

    SelectedChannelTypeBuilder gray = new SelectedChannelTypeBuilder().unset();

    SelectedChannelTypeBuilder red = new SelectedChannelTypeBuilder().unset();

    SelectedChannelTypeBuilder green = new SelectedChannelTypeBuilder().unset();

    SelectedChannelTypeBuilder blue = new SelectedChannelTypeBuilder().unset();

    public ChannelSelectionBuilder() {
        this(null);
    }

    public ChannelSelectionBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public SelectedChannelTypeBuilder gray() {
        unset = false;
        return gray;
    }

    public SelectedChannelTypeBuilder red() {
        unset = false;
        return red;
    }

    public SelectedChannelTypeBuilder green() {
        unset = false;
        return green;
    }

    public SelectedChannelTypeBuilder blue() {
        unset = false;
        return blue;
    }

    public ChannelSelection build() {
        if (unset) {
            return null;
        }
        ChannelSelection result;
        if (gray.isUnset()) {
            result = sf.channelSelection(red.build(), green.build(), blue.build());
        } else {
            result = sf.channelSelection(gray.build());
        }
        if (parent == null) {
            reset();
        }
        return result;
    }

    public ChannelSelectionBuilder reset() {
        gray.unset();
        red.unset();
        green.unset();
        blue.unset();
        unset = false;
        return this;
    }

    public ChannelSelectionBuilder reset(ChannelSelection original) {
        if (original == null) {
            return unset();
        }

        if (original.getRGBChannels() != null) {
            red.reset(original.getGrayChannel());
            green.reset(original.getGrayChannel());
            blue.reset(original.getGrayChannel());
        } else {
            gray.reset(original.getGrayChannel());
        }
        unset = false;
        return this;
    }

    public ChannelSelectionBuilder unset() {
        return (ChannelSelectionBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().raster().channelSelection().init(this);
    }

}
