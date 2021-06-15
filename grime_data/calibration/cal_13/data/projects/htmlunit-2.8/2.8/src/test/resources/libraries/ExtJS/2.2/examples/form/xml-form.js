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

    var fs = new Ext.FormPanel({
        frame: true,
        title:'XML Form',
        labelAlign: 'right',
        labelWidth: 85,
        width:340,
        waitMsgTarget: true,

        // configure how to read the XML Data
        reader : new Ext.data.XmlReader({
            record : 'contact',
            success: '@success'
        }, [
            {name: 'first', mapping:'name/first'}, // custom mapping
            {name: 'last', mapping:'name/last'},
            'company', 'email', 'state',
            {name: 'dob', type:'date', dateFormat:'m/d/Y'} // custom data types
        ]),

        // reusable eror reader class defined at the end of this file
        errorReader: new Ext.form.XmlErrorReader(),

        items: [
            new Ext.form.FieldSet({
                title: 'Contact Information',
                autoHeight: true,
                defaultType: 'textfield',
                items: [{
                        fieldLabel: 'First Name',
                        name: 'first',
                        width:190
                    }, {
                        fieldLabel: 'Last Name',
                        name: 'last',
                        width:190
                    }, {
                        fieldLabel: 'Company',
                        name: 'company',
                        width:190
                    }, {
                        fieldLabel: 'Email',
                        name: 'email',
                        vtype:'email',
                        width:190
                    },

                    new Ext.form.ComboBox({
                        fieldLabel: 'State',
                        hiddenName:'state',
                        store: new Ext.data.SimpleStore({
                            fields: ['abbr', 'state'],
                            data : Ext.exampledata.states // from states.js
                        }),
                        valueField:'abbr',
                        displayField:'state',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText:'Select a state...',
                        selectOnFocus:true,
                        width:190
                    }),

                    new Ext.form.DateField({
                        fieldLabel: 'Date of Birth',
                        name: 'dob',
                        width:190,
                        allowBlank:false
                    })
                ]
            })
        ]
    });

    // simple button add
    fs.addButton('Load', function(){
        fs.getForm().load({url:'xml-form.xml', waitMsg:'Loading'});
    });

    // explicit add
    var submit = fs.addButton({
        text: 'Submit',
        disabled:true,
        handler: function(){
            fs.getForm().submit({url:'xml-errors.xml', waitMsg:'Saving Data...'});
        }
    });

    fs.render('form-ct');

    fs.on({
        actioncomplete: function(form, action){
            if(action.type == 'load'){
                submit.enable();
            }
        }
    });

});

// A reusable error reader class for XML forms
Ext.form.XmlErrorReader = function(){
    Ext.form.XmlErrorReader.superclass.constructor.call(this, {
            record : 'field',
            success: '@success'
        }, [
            'id', 'msg'
        ]
    );
};
Ext.extend(Ext.form.XmlErrorReader, Ext.data.XmlReader);
