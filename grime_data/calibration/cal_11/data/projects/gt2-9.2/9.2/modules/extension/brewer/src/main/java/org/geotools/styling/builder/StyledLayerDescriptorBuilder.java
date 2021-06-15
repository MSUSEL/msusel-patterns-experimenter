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
import java.util.List;

import org.geotools.styling.NamedLayer;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;

/**
 * 
 *
 * @source $URL$
 */
public class StyledLayerDescriptorBuilder extends AbstractSLDBuilder<StyledLayerDescriptor> {

    List<AbstractSLDBuilder<? extends StyledLayer>> layers = new ArrayList<AbstractSLDBuilder<? extends StyledLayer>>();

    String name;

    String title;

    String sldAbstract;

    public StyledLayerDescriptorBuilder() {
        super(null);
        reset();
    }

    public StyledLayerDescriptorBuilder name(String name) {
        unset = false;
        this.name = name;
        return this;
    }

    public StyledLayerDescriptorBuilder title(String title) {
        unset = false;
        this.title = title;
        return this;
    }

    public StyledLayerDescriptorBuilder sldAbstract(String sldAbstract) {
        unset = false;
        this.sldAbstract = sldAbstract;
        return this;
    }

    public NamedLayerBuilder namedLayer() {
        unset = false;
        NamedLayerBuilder nlb = new NamedLayerBuilder(this);
        layers.add(nlb);
        return nlb;
    }

    public UserLayerBuilder userLayer() {
        unset = false;
        UserLayerBuilder ulb = new UserLayerBuilder(this);
        layers.add(ulb);
        return ulb;
    }

    /**
     * Reset stroke to default values.
     */
    public StyledLayerDescriptorBuilder reset() {
        unset = false;
        this.name = null;
        this.title = null;
        this.sldAbstract = null;
        this.layers.clear();
        return this;
    }

    /**
     * Reset builder to provided original stroke.
     * 
     * @param stroke
     */
    public StyledLayerDescriptorBuilder reset(StyledLayerDescriptor other) {
        if(other == null) {
            return unset();
        }
        this.name = other.getName();
        this.title = other.getTitle();
        this.sldAbstract = other.getAbstract();
        this.layers.clear();
        for (StyledLayer layer : other.getStyledLayers()) {
            if(layer instanceof UserLayer) {
                layers.add(new UserLayerBuilder().reset((UserLayer) layer)); 
            } else if(layer instanceof NamedLayer) {
                layers.add(new NamedLayerBuilder().reset((NamedLayer) layer));
            }
        }
        
        unset = false;
        return this;
    }

    public StyledLayerDescriptor build() {
        if (unset) {
            return null;
        }
        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
        sld.setName(name);
        sld.setTitle(title);
        sld.setAbstract(sldAbstract);
        for (AbstractSLDBuilder<? extends StyledLayer> builder : layers) {
            sld.addStyledLayer(builder.build());
        }
        reset();
        return sld;
    }

    @Override
    public StyledLayerDescriptor buildSLD() {
        return build();
    }

    @Override
    protected void buildSLDInternal(StyledLayerDescriptorBuilder sb) {
        sb.init(this);
    }
    
    @Override
    public StyledLayerDescriptorBuilder unset() {
        return (StyledLayerDescriptorBuilder) super.unset();
    }

}
