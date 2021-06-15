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
Ext.onReady(function(){

    var data = {
        name: 'Jack Slocum',
        company: 'Ext JS, LLC',
        address: '4 Red Bulls Drive',
        city: 'Cleveland',
        state: 'Ohio',
        zip: '44102',
        kids: [{
            name: 'Sara Grace',
            age:3
        },{
            name: 'Zachary',
            age:2
        },{
            name: 'John James',
            age:0
        }]
    };

    var p = new Ext.Panel({
        title: 'Basic Template',
        width: 300,
        html: '<p><i>Apply the template to see results here</i></p>',
        tbar: [{
            text: 'Apply Template',
            handler: function(){

                var tpl = new Ext.Template(
                    '<p>Name: {name}</p>',
                    '<p>Company: {company}</p>',
                    '<p>Location: {city}, {state}</p>'
                );

                tpl.overwrite(p.body, data);
                p.body.highlight('#c3daf9', {block:true});
            }
        }],

        renderTo: document.body
    });


    var p2 = new Ext.Panel({
        title: 'XTemplate',
        width: 300,
        html: '<p><i>Apply the template to see results here</i></p>',
        tbar: [{
            text: 'Apply Template',
            handler: function(){

                var tpl = new Ext.XTemplate(
                    '<p>Name: {name}</p>',
                    '<p>Company: {company}</p>',
                    '<p>Location: {city}, {state}</p>',
                    '<p>Kids: ',
                    '<tpl for="kids" if="name==\'Jack Slocum\'">',
                        '<tpl if="age &gt; 1"><p>{#}. {parent.name}\'s kid - {name}</p></tpl>',
                    '</tpl></p>'
                );
                tpl.overwrite(p2.body, data);
                p2.body.highlight('#c3daf9', {block:true});
            }
        }],

        renderTo: document.body
    });
});
