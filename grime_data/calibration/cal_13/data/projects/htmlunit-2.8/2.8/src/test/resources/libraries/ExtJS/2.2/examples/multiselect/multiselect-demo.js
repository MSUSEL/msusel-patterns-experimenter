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
    
    /*
     * Ext.ux.Multiselect Example Code
     */
    var msForm = new Ext.form.FormPanel({
        title: 'MultiSelect Test',
        width:700,
        bodyStyle: 'padding:10px;',
        renderTo: 'multiselect',
        items:[{
            xtype:"multiselect",
            fieldLabel:"Multiselect<br />(Required)",
            name:"multiselect",
            dataFields:["code", "desc"], 
            valueField:"code",
            displayField:"desc",
            width:250,
            height:200,
            allowBlank:false,
            data:[[123,"One Hundred Twenty Three"],
                ["1", "One"], ["2", "Two"], ["3", "Three"], ["4", "Four"], ["5", "Five"],
                ["6", "Six"], ["7", "Seven"], ["8", "Eight"], ["9", "Nine"]],
            tbar:[{
                text:"clear",
                handler:function(){
	                msForm.getForm().findField("multiselect").reset();
	            }
            }]
        }],
        tbar:[{
            text: 'Options',
            menu: [{
	            text:"Set Value (2,3)",
	            handler: function(){
	                msForm.getForm().findField("multiselect").setValue("2,3");
	            }
	        },{
	            text:"Toggle Enabled",
	            handler: function(){
	                var m=msForm.getForm().findField("multiselect");
	                if (!m.disabled)m.disable();
	                else m.enable();
	            }
            }]
        }],
        
        buttons: [{
            text: 'Save',
            handler: function(){
                if(msForm.getForm().isValid()){
	                Ext.Msg.alert('Submitted Values', 'The following will be sent to the server: <br />'+ 
	                    msForm.getForm().getValues(true));
                }
            }
        }]
    });
    
    
    /*
     * Ext.ux.ItemSelector Example Code
     */
    var isForm = new Ext.form.FormPanel({
        title: 'ItemSelector Test',
        width:700,
        bodyStyle: 'padding:10px;',
        renderTo: 'itemselector',
        items:[{
            xtype:"itemselector",
            name:"itemselector",
            fieldLabel:"ItemSelector",
            dataFields:["code", "desc"],
            toData:[["10", "Ten"]],
            msWidth:250,
            msHeight:200,
            valueField:"code",
            displayField:"desc",
            imagePath:"images/",
            toLegend:"Selected",
            fromLegend:"Available",
            fromData:[[123,"One Hundred Twenty Three"],
                ["1", "One"], ["2", "Two"], ["3", "Three"], ["4", "Four"], ["5", "Five"],
                ["6", "Six"], ["7", "Seven"], ["8", "Eight"], ["9", "Nine"]],
            toTBar:[{
                text:"Clear",
                handler:function(){
                    var i=isForm.getForm().findField("itemselector");
                    i.reset.call(i);
                }
            }]
        }],
        
        buttons: [{
            text: 'Save',
            handler: function(){
                if(isForm.getForm().isValid()){
                    Ext.Msg.alert('Submitted Values', 'The following will be sent to the server: <br />'+ 
                        isForm.getForm().getValues(true));
                }
            }
        }]
    });
    
});
