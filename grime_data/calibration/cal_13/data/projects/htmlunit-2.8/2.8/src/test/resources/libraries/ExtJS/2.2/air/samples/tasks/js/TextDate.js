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
// generates a renderer function to be used for textual date groups
Ext.util.Format.createTextDateRenderer = function(){
    // create the cache of ranges to be reused
    var today = new Date().clearTime(true);
    var year = today.getFullYear();
    var todayTime = today.getTime();
    var yesterday = today.add('d', -1).getTime();
    var tomorrow = today.add('d', 1).getTime();
    var weekDays = today.add('d', 6).getTime();
    var lastWeekDays = today.add('d', -6).getTime();

    var weekAgo1 = today.add('d', -13).getTime();
    var weekAgo2 = today.add('d', -20).getTime();
    var weekAgo3 = today.add('d', -27).getTime();

    var f = function(date){
        if(!date) {
            return '(No Date)';
        }
        var notime = date.clearTime(true).getTime();

        if (notime == todayTime) {
            return 'Today';
        }
        if(notime > todayTime){
            if (notime == tomorrow) {
                return 'Tomorrow';
            }
            if (notime <= weekDays) {
                return date.format('l');
            }
        }else {
        	if(notime == yesterday) {
            	return 'Yesterday';
            }
            if(notime >= lastWeekDays) {
                return 'Last ' + date.format('l');
            }
        }            
        return date.getFullYear() == year ? date.format('D m/d') : date.format('D m/d/Y');
   };
   
   f.date = today;
   return f;
};
