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

    Ext.QuickTips.init();

    // turn on validation errors beside the field globally
    Ext.form.Field.prototype.msgTarget = 'side';
    
    var bd = Ext.getBody();
    
    bd.createChild({tag: 'h2', html: 'Dutch Form'})
    
    // simple form
    var simple = new Ext.FormPanel({
        labelWidth: 100, // label settings here cascade unless overridden
        url:'save-form.php',
        frame:true,
        title: 'Contact Informatie (Dutch)',
        bodyStyle:'padding:5px 5px 0',
        width: 350,
        defaults: {width: 220},
        defaultType: 'textfield',

        items: [{
                fieldLabel: 'Voornaam',
                name: 'voornaam',
                allowBlank:false
            },{
                fieldLabel: 'Achternaam',
                name: 'achternaam'
            },{
                fieldLabel: 'Tussenvoegsel',
                width: 50,
                name: 'tussenvoegsel'
            },{
                fieldLabel: 'Bedrijf',
                name: 'bedrijf'
            },  new Ext.form.ComboBox({
            fieldLabel: 'Provincie',
            hiddenName: 'state',
            store: new Ext.data.SimpleStore({
                fields: ['provincie'],
                data : Ext.exampledata.dutch_provinces // from dutch-provinces.js
            }),
            displayField: 'provincie',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText:'Kies een provincie...',
            selectOnFocus:true,
            width:190
            }), {
                fieldLabel: 'E-mail',
                name: 'email',
                vtype:'email'
            }, new Ext.form.DateField({
                fieldLabel: 'Geb. Datum',
                name: 'geb_datum'
            })
        ],

        buttons: [{
            text: 'Opslaan'
        },{
            text: 'Annuleren'
        }]
    });

    simple.render(document.body);
});
