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
Ext.ux.SlidingPager = Ext.extend(Ext.util.Observable, {
    init : function(pbar){
        this.pagingBar = pbar;

        pbar.on('render', this.onRender, this);
        pbar.on('beforedestroy', this.onDestroy, this);
    },

    onRender : function(pbar){
        Ext.each(pbar.items.getRange(2,6), function(c){
            c.hide();
        });
        var td = document.createElement("td");
        pbar.tr.insertBefore(td, pbar.tr.childNodes[5]);

        td.style.padding = '0 5px';

        this.slider = new Ext.Slider({
            width: 114,
            minValue: 1,
            maxValue: 1,
            plugins:new Ext.ux.SliderTip({
                bodyStyle:'padding:5px;',
                getText : function(s){
                    return String.format('Page <b>{0}</b> of <b>{1}</b>', s.value, s.maxValue);
                }
            })
        });
        this.slider.render(td);

        this.slider.on('changecomplete', function(s, v){
            pbar.changePage(v);
        });

        pbar.on('change', function(pb, data){
            this.slider.maxValue = data.pages;
            this.slider.setValue(data.activePage);
        }, this);
    },

    onDestroy : function(){
        this.slider.destroy();
    }
});
