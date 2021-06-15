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
/** support for bounding box queries
    
 * @author David Boyd, Data Tactics Corp.
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

// Simple point in Bounding box function.
// boundingbox is of the form float[4] = [Left Longitude,  Bottom Latitude,
//                                Right Longitude,  Top Latitued ]
// point isof the form float[2]= [longitude, latitude ]
var gtmpPointInBBOX = { _id : "gtmpPointInBBOX", value : function(bbox, point) {

        var leftLongitude = bbox[0];
        var bottomLatitude = bbox[1];
        var rightLongitude = bbox[2];
        var topLatitude = bbox[3];
        var tLongitude = point[0];
        var tLatitude = point[1];

        // For the bounding box assume that when box crosses
        // the 180 longitude that it will be the smaller of the
        // defined box queried.

        //checks to see if bounding box crosses 180 degrees
        var boxcase = 0;
        if  (leftLongitude < 0) {
            if (rightLongitude < 0) {
               boxcase = 3;
            } else {
               boxcase = 2;
            }
        } else {
            boxcase = 1;
        }

        var inX = false;
        switch (boxcase) {
               case 1:
                       if ((tLongitude >= leftLongitude) &&
                           (tLongitude <= rightLongitude))
                               inX = true;
                       else
                               inX = false;
                       break;
               case 2:
                       var normLongitude = 360.0 + leftLongitude;
                       if ((tLongitude >= normLongitude) &&
                           (tLongitude <= rightLongitude))
                               inX = true;
                       else
                               inX = false;
                       break;
               case 3:
                       if ((tLongitude >= leftLongitude) &&
                           (tLongitude <= rightLongitude))
                               inX = true;
                       else
                               inX = false;
                       break;
        }
        if (!inX)
           return false;

        // Now test the Latitude
        if ((tLatitude >= bottomLatitude) &&
            (tLatitude <= topLatitude))
            return true;

        return false;
    }
}

// Simple bounding box intersection test
// Both boxes are of the form:  float[4] = [Left Longitude,  Bottom Latitude,
//                                Right Longitude,  Top Latitued ]
var gtmpBBoxIntersect = { _id: "gtmpBBoxIntersect", value: function(bbox1, bbox2) {
        var AminX = bbox1[0];
        var AminY = bbox1[1];
        var AmaxX = bbox1[2];
        var AmaxY = bbox1[3];

        var BminX = bbox2[0];
        var BminY = bbox2[1];
        var BmaxX = bbox2[2];
        var BmaxY = bbox2[3];

        if ((AmaxX < BminX) || (AminX > BmaxX) ||
            (AmaxY < BminY) || (AminY > BmaxY)) {
            return false;
        }

        return true;
    }
}

var gtmpGeoQuery = { _id : 'gtmpGeoQuery', value: function(bbox) {
        if (!obj.geometry) return false;
        if (obj.geometry.type == 'Point') {
            return gtmpPointInBBOX(bbox, obj.geometry.coordinates);
        }
        if (obj.geometry.type == 'Polygon') {
            return gtmpBBoxIntersect(bbox, obj.bbox);
        }
        return false;
    }
}

db.system.js.remove('gtmpPointInBBOX');
db.system.js.remove('gtmpBBoxIntersect');
db.system.js.remove('gtmpGeoQuery');
db.system.js.save(gtmpPointInBBOX);
db.system.js.save(gtmpBBoxIntersect);
db.system.js.save(gtmpGeoQuery);

exit
