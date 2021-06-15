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

import org.geotools.data.DataStore;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.Style;
import org.geotools.styling.UserLayer;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * 
 *
 * @source $URL$
 */
public class UserLayerBuilder extends AbstractSLDBuilder<UserLayer> {

    DataStore inlineFeatureDataStore;

    SimpleFeatureType inlineFeatureType;

    RemoteOWSBuilder remoteOWS = new RemoteOWSBuilder().unset();

    List<FeatureTypeConstraintBuilder> featureTypeConstraint = new ArrayList<FeatureTypeConstraintBuilder>();

    List<StyleBuilder> userStyles = new ArrayList<StyleBuilder>();

    public UserLayerBuilder() {
        this(null);
    }

    public UserLayerBuilder(AbstractSLDBuilder<?> parent) {
        super(parent);
        reset();
    }

    public UserLayerBuilder inlineData(DataStore store, SimpleFeatureType sft) {
        this.unset = false;
        this.inlineFeatureDataStore = store;
        this.inlineFeatureType = sft;
        return this;
    }

    public UserLayerBuilder remoteOWS(String onlineResource, String service) {
        this.unset = false;
        remoteOWS.resource(onlineResource).service(service);
        return this;
    }

    public FeatureTypeConstraintBuilder featureTypeConstraint() {
        this.unset = false;
        FeatureTypeConstraintBuilder builder = new FeatureTypeConstraintBuilder(this);
        featureTypeConstraint.add(builder);
        return builder;
    }

    public StyleBuilder style() {
        this.unset = false;
        StyleBuilder sb = new StyleBuilder(this);
        userStyles.add(sb);
        return sb;
    }

    /**
     * Reset stroke to default values.
     */
    public UserLayerBuilder reset() {
        unset = false;
        inlineFeatureDataStore = null;
        inlineFeatureType = null;
        remoteOWS.unset();
        featureTypeConstraint.clear();
        userStyles.clear();
        return this;
    }

    /**
     * Reset builder to provided original stroke.
     * 
     * @param stroke
     */
    public UserLayerBuilder reset(UserLayer other) {
        if (other == null) {
            return unset();
        }

        inlineFeatureDataStore = other.getInlineFeatureDatastore();
        inlineFeatureType = other.getInlineFeatureType();
        remoteOWS.reset(other.getRemoteOWS());
        featureTypeConstraint.clear();
        for (FeatureTypeConstraint ftc : other.getLayerFeatureConstraints()) {
            featureTypeConstraint.add(new FeatureTypeConstraintBuilder(this).reset(ftc));
        }
        userStyles.clear();
        for (Style style : other.getUserStyles()) {
            userStyles.add(new StyleBuilder(this).reset(style));
        }

        unset = false;
        return this;
    }

    @Override
    public UserLayerBuilder unset() {
        return (UserLayerBuilder) super.unset();
    }

    public UserLayer build() {
        if (unset) {
            return null;
        }
        UserLayer layer = sf.createUserLayer();
        layer.setRemoteOWS(remoteOWS.build());
        layer.setInlineFeatureDatastore(inlineFeatureDataStore);
        layer.setInlineFeatureType(inlineFeatureType);
        if (featureTypeConstraint.size() > 0) {
            FeatureTypeConstraint[] constraints = new FeatureTypeConstraint[featureTypeConstraint
                    .size()];
            for (int i = 0; i < constraints.length; i++) {
                constraints[i] = featureTypeConstraint.get(i).build();
            }
            layer.setLayerFeatureConstraints(constraints);
        }
        for (StyleBuilder sb : userStyles) {
            layer.addUserStyle(sb.build());
        }
        return layer;
    }

    @Override
    protected void buildSLDInternal(StyledLayerDescriptorBuilder sb) {
        sb.userLayer().init(this);
    }
}
