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
Ext.grid.filter.DateFilter = Ext.extend(Ext.grid.filter.Filter, {
    /**
     * @cfg {Date} dateFormat
     * The date format applied to the menu's {@link Ext.menu.DateMenu}
     */
	dateFormat: 'm/d/Y',
    /**
     * @cfg {Object} pickerOpts
     * The config object that will be passed to the menu's {@link Ext.menu.DateMenu} during
     * initialization (sets minDate, maxDate and format to the same configs specified on the filter)
     */
	pickerOpts: {},
    /**
     * @cfg {String} beforeText
     * The text displayed for the "Before" menu item
     */
    beforeText: 'Before',
    /**
     * @cfg {String} afterText
     * The text displayed for the "After" menu item
     */
    afterText: 'After',
    /**
     * @cfg {String} onText
     * The text displayed for the "On" menu item
     */
    onText: 'On',
    /**
     * @cfg {Date} minDate
     * The minimum date allowed in the menu's {@link Ext.menu.DateMenu}
     */
    /**
     * @cfg {Date} maxDate
     * The maximum date allowed in the menu's {@link Ext.menu.DateMenu}
     */
	
	init: function() {
		var opts = Ext.apply(this.pickerOpts, {
			minDate: this.minDate, 
			maxDate: this.maxDate, 
			format:  this.dateFormat
		});
		var dates = this.dates = {
			'before': new Ext.menu.CheckItem({text: this.beforeText, menu: new Ext.menu.DateMenu(opts)}),
			'after':  new Ext.menu.CheckItem({text: this.afterText, menu: new Ext.menu.DateMenu(opts)}),
			'on':     new Ext.menu.CheckItem({text: this.onText, menu: new Ext.menu.DateMenu(opts)})
    };
				
		this.menu.add(dates.before, dates.after, "-", dates.on);
		
		for(var key in dates) {
			var date = dates[key];
			date.menu.on('select', this.onSelect.createDelegate(this, [date]), this);
  
      date.on('checkchange', function(){
        this.setActive(this.isActivatable());
			}, this);
		};
	},
  
	onSelect: function(date, menuItem, value, picker) {
    date.setChecked(true);
    var dates = this.dates;
    
    if(date == dates.on) {
      dates.before.setChecked(false, true);
      dates.after.setChecked(false, true);
    } else {
      dates.on.setChecked(false, true);
      
      if(date == dates.after && dates.before.menu.picker.value < value) {
        dates.before.setChecked(false, true);
      } else if (date == dates.before && dates.after.menu.picker.value > value) {
        dates.after.setChecked(false, true);
      }
    }
    
    this.fireEvent("update", this);
  },
  
	getFieldValue: function(field) {
		return this.dates[field].menu.picker.getValue();
	},
	
	getPicker: function(field) {
		return this.dates[field].menu.picker;
	},
	
	isActivatable: function() {
		return this.dates.on.checked || this.dates.after.checked || this.dates.before.checked;
	},
	
	setValue: function(value) {
		for(var key in this.dates) {
			if(value[key]) {
				this.dates[key].menu.picker.setValue(value[key]);
				this.dates[key].setChecked(true);
			} else {
				this.dates[key].setChecked(false);
			}
    }
	},
	
	getValue: function() {
		var result = {};
		for(var key in this.dates) {
			if(this.dates[key].checked) {
				result[key] = this.dates[key].menu.picker.getValue();
      }
    }	
		return result;
	},
	
	serialize: function() {
		var args = [];
		if(this.dates.before.checked) {
			args = [{type: 'date', comparison: 'lt', value: this.getFieldValue('before').format(this.dateFormat)}];
    }
		if(this.dates.after.checked) {
			args.push({type: 'date', comparison: 'gt', value: this.getFieldValue('after').format(this.dateFormat)});
    }
		if(this.dates.on.checked) {
			args = {type: 'date', comparison: 'eq', value: this.getFieldValue('on').format(this.dateFormat)};
    }

    this.fireEvent('serialize', args, this);
		return args;
	},
	
	validateRecord: function(record) {
		var val = record.get(this.dataIndex).clearTime(true).getTime();
		
		if(this.dates.on.checked && val != this.getFieldValue('on').clearTime(true).getTime()) {
			return false;
    }
		if(this.dates.before.checked && val >= this.getFieldValue('before').clearTime(true).getTime()) {
			return false;
    }
		if(this.dates.after.checked && val <= this.getFieldValue('after').clearTime(true).getTime()) {
			return false;
    }
		return true;
	}
});
