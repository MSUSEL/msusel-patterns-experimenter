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
package org.hibernate.envers.test.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.envers.enhanced.SequenceIdRevisionEntity;
import org.hibernate.envers.configuration.GlobalConfiguration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class TestTools {
    public static <T> Set<T> makeSet(T... objects) {
        Set<T> ret = new HashSet<T>();
        //noinspection ManualArrayToCollectionCopy
        for (T o : objects) {
            ret.add(o);
        }

        return ret;
    }

    public static <T> List<T> makeList(T... objects) {
        return Arrays.asList(objects);
    }

    public static Map<Object, Object> makeMap(Object... objects) {
        Map<Object, Object> ret = new HashMap<Object, Object>();
        // The number of objects must be divisable by 2.
        //noinspection ManualArrayToCollectionCopy
        for (int i=0; i<objects.length; i+=2) {
            ret.put(objects[i], objects[i+1]);
        }

        return ret;
    }

    public static <T> boolean checkList(List<T> list, T... objects) {
        if (list.size() != objects.length) {
            return false;
        }

        for (T obj : objects) {
            if (!list.contains(obj)) {
                return false;
            }
        }

        return true;
    }

	public static List<Integer> extractRevisionNumbers(List queryResults) {
		List<Integer> result = new ArrayList<Integer>();
		for (Object queryResult : queryResults) {
			result.add(((SequenceIdRevisionEntity) ((Object[]) queryResult)[1])
					.getId());
		}
		return result;
	}

	public static Set<String> extractModProperties(
			PersistentClass persistentClass) {
		return extractModProperties(persistentClass,
				GlobalConfiguration.DEFAULT_MODIFIED_FLAG_SUFFIX);
	}

	public static Set<String> extractModProperties(
			PersistentClass persistentClass, String suffix) {
		Set<String> result = new HashSet<String>();
		Iterator iterator = persistentClass.getPropertyIterator();

		while (iterator.hasNext()) {
			Property property = (Property) iterator.next();
			String propertyName = property.getName();
			if (propertyName.endsWith(suffix)) {
				result.add(propertyName);
			}
		}
		return result;
	}
}
