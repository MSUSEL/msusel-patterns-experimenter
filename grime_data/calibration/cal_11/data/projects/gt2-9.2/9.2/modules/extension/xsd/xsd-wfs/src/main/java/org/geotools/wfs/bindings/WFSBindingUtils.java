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
package org.geotools.wfs.bindings;

import java.math.BigInteger;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.geotools.xml.Node;


/**
 * Utility class to be used by bindings.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class WFSBindingUtils {
    /**
     * Sets the service feature of the object passed in.
     * <p>
     * The service value is retreived as an attribute from the node, if
     * <code>null</code>, the default "WFS" is used.
     * </p>
     * @param object An object which contains a feature named "service"
     * @param node The parse node.
     */
    public static void service(EObject object, Node node) {
        String service = (String) node.getAttributeValue("service");

        if (service == null) {
            service = "WFS";
        }

        set(object, "service", service);
    }

    /**
     * Sets the version feature of the object passed in.
     * <p>
     * The version value is retreived as an attribute from the node, if
     * <code>null</code>, the default "1.0.0" is used.
     * </p>
     * @param object An object which contains a feature named "version"
     * @param node The parse node.
     */
    public static void version(EObject object, Node node) {
        String version = (String) node.getAttributeValue("version");

        if (version == null) {
            version = "1.0.0";
        }

        set(object, "version", version);
    }

    /**
     * Sets the outputFormat feature of the object passed in.
     * <p>
     * The outputFormat value is retreived as an attribute from the node, if
     * <code>null</code>, the default <code>default</code> is used.
     * </p>
     * @param object An object which contains a feature named "version"
     * @param node The parse node.
     */
    public static void outputFormat(EObject object, Node node, String defalt) {
        String outputFormat = (String) node.getAttributeValue("outputFormat");

        if (outputFormat == null) {
            outputFormat = defalt;
        }

        set(object, "outputFormat", outputFormat);
    }

    static void set(EObject object, String featureName, Object value) {
        EStructuralFeature feature = object.eClass().getEStructuralFeature(featureName);

        if (feature != null) {
            object.eSet(feature, value);
        }
    }

    /**
     * @param value A number
     *
     * @return The number as a {@link BigInteger}.
     */
    public static BigInteger asBigInteger(Number number) {
        if (number == null) {
            return null;
        }

        if (number instanceof BigInteger) {
            return (BigInteger) number;
        }

        return BigInteger.valueOf(number.longValue());
    }
}
