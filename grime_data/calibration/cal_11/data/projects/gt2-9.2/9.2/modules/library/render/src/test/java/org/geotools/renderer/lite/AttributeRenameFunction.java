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
package org.geotools.renderer.lite;

import static org.geotools.filter.capability.FunctionNameImpl.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geotools.data.Query;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.filter.function.RenderingTransformation;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.PropertyName;

/**
 * A rendering transformation that renames one attribute in the input feature collection
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 */
public class AttributeRenameFunction extends FunctionExpressionImpl implements
        RenderingTransformation {

    public static FunctionName NAME = new FunctionNameImpl("AttributeRename", parameter(
            "sourceAttribute", String.class), parameter("targetAttribute", String.class),
            parameter("optimizeQuery", boolean.class, 0, 1));

    public AttributeRenameFunction() {
        super(NAME);
    }
    
    @Override
    public int getArgCount() {
        return -1;
    }

    public Object evaluate(Object object) {
        String source = getAttribute(object, 0, String.class, true);
        String target = getAttribute(object, 1, String.class, true);

        // prepare the source schema
        SimpleFeatureCollection fc = (SimpleFeatureCollection) object;
        SimpleFeatureType sourceSchema = fc.getSchema();
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        for (AttributeDescriptor ad : sourceSchema.getAttributeDescriptors()) {
            if (ad.getLocalName().equals(source)) {
                tb.add(target, ad.getType().getBinding());
            } else {
                tb.add(ad);
            }
        }
        tb.setName(sourceSchema.getName());
        SimpleFeatureType targetSchema = tb.buildFeatureType();

        // build the result feature collection
        final SimpleFeatureBuilder fb = new SimpleFeatureBuilder(targetSchema);
        final List<SimpleFeature> features = new ArrayList<SimpleFeature>();
        try {
            fc.accepts(new FeatureVisitor() {
                
                @Override
                public void visit(Feature feature) {
                    fb.init((SimpleFeature) feature);
                    SimpleFeature f = fb.buildFeature(feature.getIdentifier().getID());
                    features.add(f);
                }
            }, null);
        } catch(IOException e) {
            throw new RuntimeException("Failed to compute output collection", e);
        }
        
        return new ListFeatureCollection(targetSchema, features);
    }

    <T> T getAttribute(Object object, int expressionIdx, Class<T> targetClass, boolean mandatory) {
        try { // attempt to get value and perform conversion
            T result = getExpression(expressionIdx).evaluate(object, targetClass);
            if (result == null && mandatory) {
                throw new IllegalArgumentException("Could not find function argument #"
                        + expressionIdx + ", but it's mandatory");
            }
            return result;
        } catch (Exception e) {
            // probably a type error
            if(mandatory) {
                throw new IllegalArgumentException("Could not find function argument #" + expressionIdx
                    + ", but it's mandatory");
            } else {
                return null;
            }
        }

    }

    @Override
    public Query invertQuery(Query targetQuery, GridGeometry gridGeometry) {
        final String source = getAttribute(null, 0, String.class, true);
        final String target = getAttribute(null, 1, String.class, true);
        Boolean invert = getAttribute(null, 2, Boolean.class, false);
        
        if(invert == null || !invert) {
            return null;
        } else {
            Query q = new Query(targetQuery);
            if(q.getPropertyNames() != null) {
                String[] names = Arrays.copyOf(q.getPropertyNames(), q.getPropertyNames().length);
                for (int i = 0; i < names.length; i++) {
                    if(names[i].equals(target)) {
                        names[i] = source;
                    }
                }
                q.setPropertyNames(names);
            }
            if(q.getFilter() != null) {
                Filter renamed = (Filter) q.getFilter().accept(new DuplicatingFilterVisitor() {
                    @Override
                    public Object visit(PropertyName expression, Object extraData) {
                        if(expression.getPropertyName().equals(target)) {
                            return ff.property(source);
                        } else {
                            return super.visit(expression, extraData);
                        }
                    }
                }, null);
                q.setFilter(renamed);
            }
            
            return q;
        }
    }

    @Override
    public GridGeometry invertGridGeometry(Query targetQuery, GridGeometry targetGridGeometry) {
        return null;
    }

}
