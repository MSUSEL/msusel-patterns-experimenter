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
// Add the additional 'advanced' VTypes
Ext.apply(Ext.form.VTypes, {
    daterange : function(val, field) {
        var date = field.parseDate(val);

        if(!date){
            return;
        }
        if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
            var start = Ext.getCmp(field.startDateField);
            start.setMaxValue(date);
            start.validate();
            this.dateRangeMax = date;
        } 
        else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
            var end = Ext.getCmp(field.endDateField);
            end.setMinValue(date);
            end.validate();
            this.dateRangeMin = date;
        }
        /*
         * Always return true since we're only using this vtype to set the
         * min/max allowed values (these are tested for after the vtype test)
         */
        return true;
    },

    password : function(val, field) {
        if (field.initialPassField) {
            var pwd = Ext.getCmp(field.initialPassField);
            return (val == pwd.getValue());
        }
        return true;
    },

    passwordText : 'Passwords do not match'
});

Ext.onReady(function(){

    Ext.QuickTips.init();

    // turn on validation errors beside the field globally
    Ext.form.Field.prototype.msgTarget = 'side';

    var bd = Ext.getBody();

		/*
		 * ================  Date Range  =======================
		 */
    
    var dr = new Ext.FormPanel({
      labelWidth: 125,
      frame: true,
      title: 'Date Range',
	  bodyStyle:'padding:5px 5px 0',
	  width: 350,
      defaults: {width: 175},
      defaultType: 'datefield',
      items: [{
        fieldLabel: 'Start Date',
        name: 'startdt',
        id: 'startdt',
        vtype: 'daterange',
        endDateField: 'enddt' // id of the end date field
      },{
        fieldLabel: 'End Date',
        name: 'enddt',
        id: 'enddt',
        vtype: 'daterange',
        startDateField: 'startdt' // id of the start date field
      }]
    });

    dr.render('dr');
    
    /*
     * ================  Password Verification =======================
     */
        
    var pwd = new Ext.FormPanel({
      labelWidth: 125,
      frame: true,
      title: 'Password Verification',
      bodyStyle:'padding:5px 5px 0',
      width: 350,
      defaults: {
        width: 175,
        inputType: 'password'
      },
      defaultType: 'textfield',
      items: [{
        fieldLabel: 'Password',
        name: 'pass',
        id: 'pass'
      },{
        fieldLabel: 'Confirm Password',
        name: 'pass-cfrm',
        vtype: 'password',
        initialPassField: 'pass' // id of the initial password field
      }]
    });

    pwd.render('pw');
});
