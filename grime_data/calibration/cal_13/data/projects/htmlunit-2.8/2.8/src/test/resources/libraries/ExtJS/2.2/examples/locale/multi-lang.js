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
    Ext.form.Field.prototype.msgTarget = 'side';
    
    /* Language chooser combobox  */
    var store = new Ext.data.SimpleStore({
        fields: ['code', 'language', 'charset'],
        data : Ext.exampledata.languages // from languages.js
    });
    var combo = new Ext.form.ComboBox({
        store: store,
        displayField:'language',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'Select a language...',
        selectOnFocus:true,
	onSelect: function(record) {
	    window.location.search = Ext.urlEncode({"lang":record.get("code"),"charset":record.get("charset")});
	}
    });
    combo.render('languages');

    // get the selected language code parameter from url (if exists)
    var params = Ext.urlDecode(window.location.search.substring(1));
    if (params.lang) {
	// check if there's really a language with that language code
	record = store.data.find(function(item, key) {
	    if (item.data.code==params.lang){
		return true;
	    }
	    return false;
	});
	// if language was found in store assign it as current value in combobox
	if (record) {
	    combo.setValue(record.data.language);
	}
    }

    /* Email field */
    var emailfield = new Ext.FormPanel({
        labelWidth: 100, // label settings here cascade unless overridden
        frame:true,
        title: 'Email Field',
        bodyStyle:'padding:5px 5px 0',
        width: 360,
        defaults: {width: 220},
        defaultType: 'textfield',

        items: [{
                fieldLabel: 'Email',
                name: 'email',
                vtype:'email'
            }
        ]
    });
    emailfield.render('emailfield');

    /* Datepicker */
    var datefield = new Ext.FormPanel({
        labelWidth: 100, // label settings here cascade unless overridden
        frame:true,
        title: 'Datepicker',
        bodyStyle:'padding:5px 5px 0',
        width: 360,
        defaults: {width: 220},
        defaultType: 'datefield',

        items: [{
                fieldLabel: 'Date',
                name: 'date'
            }
        ]
    });
    datefield.render('datefield');
    
    // shorthand alias
    var fm = Ext.form, Ed = Ext.grid.GridEditor;
    var monthArray = Date.monthNames.map(function (e) { return [e]; });    
    var ds = new Ext.data.Store({
		proxy: new Ext.data.PagingMemoryProxy(monthArray),
		reader: new Ext.data.ArrayReader({}, [
			{name: 'month'}
		])
    });
    var cm = new Ext.grid.ColumnModel([{
           header: "Months of the year",
           dataIndex: 'month',
           editor: new Ed(new fm.TextField({
               allowBlank: false
           })),
           width: 240
        }]);
    cm.defaultSortable = true;
    var grid = new Ext.grid.GridPanel({
	el:'grid',
	width: 360,
	height: 203,
	title:'Month Browser',
	store: ds,
	cm: cm,
	sm: new Ext.grid.RowSelectionModel({selectRow:Ext.emptyFn}),

	bbar: new Ext.PagingToolbar({
            pageSize: 6,
            store: ds,
            displayInfo: true
        })
    })
    grid.render();

    // trigger the data store load
    ds.load({params:{start:0, limit:6}});    
});
