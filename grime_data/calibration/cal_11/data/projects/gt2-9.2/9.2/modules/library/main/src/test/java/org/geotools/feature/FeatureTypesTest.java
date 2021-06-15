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
package org.geotools.feature;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

import com.vividsolutions.jts.geom.Point;

/**
 * 
 *
 * @source $URL$
 */
public class FeatureTypesTest {

    @Test
    public void testNoLength() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("NoLength");
        builder.setCRS(null);
        builder.add("name", String.class);
        builder.add("geom", Point.class);
        SimpleFeatureType ft = builder.buildFeatureType();

        assertEquals(FeatureTypes.ANY_LENGTH, FeatureTypes.getFieldLength(ft.getDescriptor("name")));
        assertEquals(FeatureTypes.ANY_LENGTH, FeatureTypes.getFieldLength(ft.getDescriptor("geom")));
    }

    @Test
    public void testStandardLength() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("StdLength");
        builder.setCRS(null);
        builder.length(20);
        builder.add("name", String.class);
        builder.add("geom", Point.class);
        SimpleFeatureType ft = builder.buildFeatureType();

        assertEquals(20, FeatureTypes.getFieldLength(ft.getDescriptor("name")));
        assertEquals(FeatureTypes.ANY_LENGTH, FeatureTypes.getFieldLength(ft.getDescriptor("geom")));
    }

    @Test
    public void testCustomLengthExpressions() {
        AttributeTypeBuilder builder = new AttributeTypeBuilder();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Function length = ff.function("LengthFunction", new Expression[]{ff.property(".")});
        
        // strict less
        builder.addRestriction(ff.less(length, ff.literal(20)));
        builder.setBinding(String.class);
        AttributeDescriptor attribute = builder.buildDescriptor("attribute");
        assertEquals(19, FeatureTypes.getFieldLength(attribute));
        
        // flip expression
        builder.addRestriction(ff.greater(ff.literal(20), length));
        builder.setBinding(String.class);
        attribute = builder.buildDescriptor("attribute");
        assertEquals(19, FeatureTypes.getFieldLength(attribute));
    }
    
    @Test
    public void testGetAncestors() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("SomeFeature");
        builder.setCRS(null);
        builder.add("name", String.class);
        builder.add("geom", Point.class);
        SimpleFeatureType ft = builder.buildFeatureType();
        List<FeatureType> types = FeatureTypes.getAncestors(ft);
        Assert.assertEquals(1, types.size());
        Assert.assertEquals("Feature", types.get(0).getName().getLocalPart());
    }
    
}
