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

import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.Style;

/**
 * 
 *
 * @source $URL$
 */
public class NamedLayerBuilder extends AbstractSLDBuilder<NamedLayer> {

    List<FeatureTypeConstraintBuilder> featureTypeConstraint = new ArrayList<FeatureTypeConstraintBuilder>();

    private String name;

    List<StyleBuilder> styles = new ArrayList<StyleBuilder>();

    public NamedLayerBuilder() {
        this(null);
    }

    public NamedLayerBuilder(AbstractSLDBuilder<?> parent) {
        super(parent);
        reset();
    }

    public NamedLayerBuilder name(String name) {
        this.unset = false;
        this.name = name;
        return this;
    }

    public StyleBuilder style() {
        this.unset = false;
        StyleBuilder sb = new StyleBuilder(this);
        styles.add(sb);
        return sb;
    }

    @SuppressWarnings("unchecked")
    public NamedLayer build() {
        if (unset) {
            return null;
        }
        NamedLayer layer = sf.createNamedLayer();
        layer.setName(name);
        List<FeatureTypeConstraint> list = new ArrayList<FeatureTypeConstraint>();
        for (FeatureTypeConstraintBuilder constraint : featureTypeConstraint) {
            list.add(constraint.build());
        }
        layer.layerFeatureConstraints().addAll(list);
        for (StyleBuilder sb : styles) {
            layer.addStyle(sb.build());
        }

        if (parent == null) {
            reset();
        }

        return layer;
    }

    public NamedLayerBuilder reset() {
        unset = false;
        this.name = null;
        featureTypeConstraint.clear();
        return this;
    }

    public NamedLayerBuilder reset(NamedLayer layer) {
        if (layer == null) {
            return unset();
        }
        this.name = layer.getName();
        featureTypeConstraint.clear();
        if (layer.layerFeatureConstraints() != null) {
            for (FeatureTypeConstraint featureConstraint : layer.layerFeatureConstraints()) {
                featureTypeConstraint.add(new FeatureTypeConstraintBuilder(this)
                        .reset(featureConstraint));
            }
        }
        styles.clear();
        for (Style style : layer.getStyles()) {
            styles.add(new StyleBuilder().reset(style));
        }
        unset = false;
        return this;
    }

    public NamedLayerBuilder unset() {
        return (NamedLayerBuilder) super.unset();
    }

    @Override
    protected void buildSLDInternal(StyledLayerDescriptorBuilder sb) {
        sb.namedLayer().init(this);
    }

}
