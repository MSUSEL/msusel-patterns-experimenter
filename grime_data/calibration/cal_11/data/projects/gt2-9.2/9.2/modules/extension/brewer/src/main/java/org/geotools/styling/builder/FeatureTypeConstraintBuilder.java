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

import org.geotools.styling.Extent;
import org.geotools.styling.FeatureTypeConstraint;
import org.opengis.filter.Filter;

/**
 * 
 *
 * @source $URL$
 */
public class FeatureTypeConstraintBuilder extends AbstractSLDBuilder<FeatureTypeConstraint> {
    private List<Extent> extents = new ArrayList<Extent>();

    private Filter filter;

    private String featureTypeName;

    public FeatureTypeConstraintBuilder() {
        this(null);
    }

    public FeatureTypeConstraintBuilder(AbstractSLDBuilder<?> parent) {
        super(parent);
        reset();
    }

    public FeatureTypeConstraintBuilder extent(String name, String value) {
        this.unset = false;
        extents.add(sf.createExtent(name, value));
        return this;
    }

    public FeatureTypeConstraintBuilder filter(Filter filter) {
        this.unset = false;
        this.filter = filter;
        return this;
    }

    public FeatureTypeConstraintBuilder featureTypeName(String name) {
        this.unset = false;
        this.featureTypeName = name;
        return this;
    }

    public FeatureTypeConstraint build() {
        if (unset) {
            return null;
        }

        Extent[] ea = (Extent[]) extents.toArray(new Extent[extents.size()]);
        FeatureTypeConstraint constraint = sf.createFeatureTypeConstraint(featureTypeName, filter,
                ea);

        if (parent == null) {
            reset();
        }

        return constraint;
    }

    public FeatureTypeConstraintBuilder reset() {
        unset = false;
        featureTypeName = null;
        filter = null;
        extents.clear();
        return this;
    }

    public FeatureTypeConstraintBuilder reset(FeatureTypeConstraint constraint) {
        if (constraint == null) {
            return unset();
        }
        featureTypeName = constraint.getFeatureTypeName();
        filter = constraint.getFilter();
        extents.clear();
        extents.addAll(Arrays.asList(constraint.getExtents()));
        unset = false;
        return this;
    }

    public FeatureTypeConstraintBuilder unset() {
        return (FeatureTypeConstraintBuilder) super.unset();
    }

    @Override
    protected void buildSLDInternal(StyledLayerDescriptorBuilder sb) {
        throw new UnsupportedOperationException("Can't build a SLD out of a feature type contraint");
    }

}
