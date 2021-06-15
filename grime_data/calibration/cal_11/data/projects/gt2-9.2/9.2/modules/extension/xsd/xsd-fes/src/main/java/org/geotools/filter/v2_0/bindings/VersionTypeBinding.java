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
package org.geotools.filter.v2_0.bindings;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.namespace.QName;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.InstanceComponent;
import org.geotools.xml.SimpleBinding;
import org.geotools.xml.impl.DatatypeConverterImpl;
import org.opengis.filter.identity.Version;

/**
 * Binding for FES 2.0 {@code VersionType} mapping to {@link Version}
 * 
 */
public class VersionTypeBinding implements SimpleBinding {

    @Override
    public QName getTarget() {
        return FES.VersionType;
    }

    @Override
    public Class<?> getType() {
        return Version.class;
    }

    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }

    @Override
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        final String content = (String) value;
        if(null == content || content.length() == 0){
            return new Version();
        }
        try {
            Version.Action versionAction = Version.Action.valueOf(content);
            return new Version(versionAction);
        } catch (IllegalArgumentException e) {
            try {
                Integer index = Integer.valueOf(content);
                return new Version(index);
            } catch (NumberFormatException nfe) {
                Calendar dateTime = DatatypeConverterImpl.getInstance().parseDateTime(content);
                return new Version(dateTime.getTime());
            }
        }
    }

    @Override
    public String encode(Object object, String value) throws Exception {
        Version version = (Version) object;
        if(version.isEmpty()){
            return null;
        }
        if(version.isDateTime()){
            Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
            cal.setTimeInMillis(version.getDateTime().getTime());
            String dateTime = DatatypeConverterImpl.getInstance().printDateTime(cal);
            return dateTime;
        }
        if(version.isIndex()){
            return String.valueOf(version.getIndex());
        }
        if(version.isVersionAction()){
            return String.valueOf(version.getVersionAction());
        }
        throw new IllegalArgumentException("Empty version union");
    }
}
