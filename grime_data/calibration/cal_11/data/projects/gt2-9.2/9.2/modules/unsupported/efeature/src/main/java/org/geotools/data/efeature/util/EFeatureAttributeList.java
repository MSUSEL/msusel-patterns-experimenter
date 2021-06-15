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
package org.geotools.data.efeature.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.geotools.data.efeature.EFeatureAttribute;
import org.geotools.data.efeature.EFeatureProperty;

/**
 * Unmodifiable list of {@link EFeatureAttribute} instances.
 * 
 * @author kengu, 22. apr. 2011
 * 
 *
 * @source $URL$
 */
public class EFeatureAttributeList<V> extends AbstractList<EFeatureAttribute<V>> {
    private static final long serialVersionUID = 1L;

    private final List<EFeatureAttribute<V>> eItems;

    private final Class<V> type;

    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    @SuppressWarnings("unchecked")
    public EFeatureAttributeList(List<? extends EFeatureProperty<V, ?>> eList, Class<V> type) {
        this.type = type;
        List<EFeatureAttribute<V>> eSelected = new ArrayList<EFeatureAttribute<V>>(eList.size());
        for (EFeatureProperty<V, ?> it : eList) {
            if (it instanceof EFeatureAttribute) {
                // Perform unchecked cast. This is always safe, since
                // EFeatureGeometry<V> extends EFeatureProperty<V,GeometryAttribute>
                //
                eSelected.add((EFeatureAttribute<V>) it);
            }
        }
        this.eItems = Collections.unmodifiableList(eSelected);
    }

    public EFeatureAttributeList(EFeatureAttributeList<V> eList) {
        this.type = eList.type;
        this.eItems = Collections.unmodifiableList(eList.eItems);
    }

    // ----------------------------------------------------- 
    //  EFeatureGeometryList implementation
    // -----------------------------------------------------

    public Class<V> getType() {
        return type;
    }

    // ----------------------------------------------------- 
    //  List implementation
    // -----------------------------------------------------


    @Override
    public EFeatureAttribute<V> get(int index) {
        return eItems.get(index);
    }

    @Override
    public int size() {
        return eItems.size();
    }

    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------


//    public static List<EFeatureAttribute<Object>> toList(EFeatureInfo eFeatureInfo,
//            EObject eObject, Collection<EAttribute> eAttributes, Transaction eTx) {
//        return toList(eFeatureInfo, eObject, Object.class, eTx);
//    }
//
//    public static <V> List<EFeatureAttribute<V>> toList(EFeatureInfo eFeatureInfo, EObject eObject,
//            Class<V> type, Transaction eTx) {
//        EClass eClass = eFeatureInfo.eClass();
//        if (eClass.isSuperTypeOf(eObject.eClass())) {
//            Map<String, EAttribute> eAttrMap = EFeatureUtils.eGetAttributeMap(eClass);
//            return toList(eFeatureInfo, eObject, eAttrMap.values(), type, eTx);
//        }
//        return null;
//    }
//
//    public static <V> List<EFeatureAttribute<V>> toList(EFeatureInfo eFeatureInfo, EObject eObject,
//            Collection<EAttribute> eAttributes, Class<V> type, Transaction eTx) {
//        List<EFeatureAttribute<V>> list = new ArrayList<EFeatureAttribute<V>>(eAttributes.size());
//        for (EAttribute it : eAttributes) {
//            EFeatureAttribute<V> d = EFeatureAttributeDelegate.create(eFeatureInfo, 
//                    eObject, it.getName(), type, eTx);
//            if (d != null) {
//                list.add(d);
//            }
//        }
//        return list;
//
//    }

}
