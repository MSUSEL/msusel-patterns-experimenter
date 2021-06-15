/*
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
/** Map-reduce scripts to gather metadata.
    Needs to be installed on MongoDB server.
    Assumes records in GeoJSON format.
    Only concerned with GeoJSON properties, and
    geometry.type
    
 * @author David Boyd, Data Tactics Corp.
 * @author Alan Mangan, Data Tactics Corp.
 *
 *         Copyright 2011 GeoTools
 *
 * @see The GNU Lesser General Public License (LGPL)
*/
/*This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA */

// mapping function; emits field name and type
mapfields_func = function() {
    mapfields_recursive ("", this);
}

// recursive map fields and types
// base: empty for base object, else holds parent
// obj: json object or array to examine
db.system.js.remove({"_id": "mapfields_recursive"});
db.system.js.insert({"_id": "mapfields_recursive", "value":
function (base, obj) {
	for (var key in obj) {
		if (base == "") {
			switch (key) {
			case "properties" :
				mapfields_recursive(key+".",obj[key]);
				break;
			case "geometry" :
				if (obj[key] != null && obj[key].type != null) {
					emit ({"fieldname" : "geometry.type", "type" : obj[key].type}, 1);
				}
				break;
			}
		}
		else {
			// basic javascript typeof for number, string, boolean
			var ftype = (typeof obj[key]).toLowerCase();
			// dig deeper to find if double/long, null, Date, or Array
			if (ftype == "number") {
			    if (obj[key] % 1 == 0) {
					ftype = "long";
				}
				else {
					ftype = "double";
				}
			}
			else if (ftype == "object") {
				if (obj[key] == null) {
					ftype = "null";
				}
				else if (obj[key] instanceof Date) {
					ftype = "date";
				}
				else if (obj[key] instanceof Array) {
					ftype = "array";
				}
			}
			// emit type
			emit ({"fieldname" : base + key, "type" : ftype}, 1);
			// recursively examine arrays and objects (JSON)
			if ((ftype == "object") || (ftype == "array")) {
				mapfields_recursive (base + key+".", obj[key]);
			}
		}
	}
}});

// reduce function; count type occurrences
reducefields_func = function (key, vals) {
    sum = 0;
    for (var i in vals) sum += vals[i];
    return sum;
}
