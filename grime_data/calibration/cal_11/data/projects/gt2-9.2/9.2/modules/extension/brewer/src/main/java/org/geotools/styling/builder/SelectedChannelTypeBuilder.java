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

import org.geotools.styling.SelectedChannelType;

/**
 * 
 *
 * @source $URL$
 */
public class SelectedChannelTypeBuilder extends AbstractStyleBuilder<SelectedChannelType> {

    private String channelName;

    private ContrastEnhancementBuilder contrastEnhancement = new ContrastEnhancementBuilder(this)
            .unset();

    public SelectedChannelTypeBuilder() {
        this(null);
    }

    public SelectedChannelTypeBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public SelectedChannelType build() {
        if (unset) {
            return null;
        }
        SelectedChannelType selectedChannelType = sf.selectedChannelType(channelName,
                contrastEnhancement.build());
        return selectedChannelType;
    }

    public SelectedChannelTypeBuilder reset() {
        contrastEnhancement.reset();
        channelName = null;
        unset = false;
        return this;
    }

    public SelectedChannelTypeBuilder reset(SelectedChannelType selectedChannelType) {
        if (selectedChannelType == null) {
            return reset();
        }
        contrastEnhancement.reset(selectedChannelType.getContrastEnhancement());
        channelName = selectedChannelType.getChannelName();
        unset = false;
        return this;
    }

    public SelectedChannelTypeBuilder unset() {
        return (SelectedChannelTypeBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().raster().channelSelection().gray().init(this);
    }

}
