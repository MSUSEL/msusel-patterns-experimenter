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
// Grid column plugin that does the complete/active button in the left-most column
CompleteColumn = function(){
    var grid;

    function getRecord(t){
        var index = grid.getView().findRowIndex(t);
        return grid.store.getAt(index);
    }

    function onMouseDown(e, t){
        if(Ext.fly(t).hasClass('task-check')){
            e.stopEvent();
            var record = getRecord(t);
            record.set('completed', !record.data.completed);
            grid.store.applyFilter();
        }
    }

    function onMouseOver(e, t){
        if(Ext.fly(t).hasClass('task-check')){
            Ext.fly(t.parentNode).addClass('task-check-over');
        }
    }

    function onMouseOut(e, t){
        if(Ext.fly(t).hasClass('task-check')){
            Ext.fly(t.parentNode).removeClass('task-check-over');
        }
    }

    Ext.apply(this, {
        width: 22,
        header: '<div class="task-col-hd"></div>',
        fixed: true,
		menuDisabled: true,
        id: 'task-col',
        renderer: function(){
            return '<div class="task-check"></div>';
        },
        init : function(xg){
            grid = xg;
            grid.on('render', function(){
                var view = grid.getView();
                view.mainBody.on('mousedown', onMouseDown);
                view.mainBody.on('mouseover', onMouseOver);
                view.mainBody.on('mouseout', onMouseOut);
            });
        }
    });
};


ReminderColumn = function(){
    var grid, menu, record;

	function getRecord(t){
        var index = grid.getView().findRowIndex(t);
        return grid.store.getAt(index);
    }
	
	function onMenuCheck(item){
		if(item.reminder === false){
			record.set('reminder', '');
		}else{
			var s = record.data.dueDate ? record.data.dueDate.clearTime(true) : new Date().clearTime();
			s = s.add('mi', Ext.state.Manager.get('defaultReminder'));
			s = s.add('mi', item.reminder*-1);
			record.set('reminder', s);
		}
	}

	function getMenu(){
		if(!menu){
			menu = new Ext.menu.Menu({
				plain: true,
				items: [{
					text: 'No Reminder',
					reminder: false,
					handler: onMenuCheck
				},'-',{
					text: 'On the Due Date',
					reminder: 0,
					handler: onMenuCheck
				},'-',{
					text: '1 day before',
					reminder: 24*60,
					handler: onMenuCheck
				},{
					text: '2 days before',
					reminder: 48*60,
					handler: onMenuCheck
				},{
					text: '3 days before',
					reminder: 72*60,
					handler: onMenuCheck
				},{
					text: '1 week before',
					reminder: 7*24*60,
					handler: onMenuCheck
				},{
					text: '2 weeks before',
					reminder: 14*24*60,
					handler: onMenuCheck
				},'-',{
					text: 'Set Default Time...',
					handler: function(){
						Ext.air.NativeWindowManager.getPrefWindow();
					}
				}]
			});
		}
		return menu;
	}

    function onMouseDown(e, t){
        if(Ext.fly(t).hasClass('reminder')){
			e.stopEvent();
            record = getRecord(t);
			if (!record.data.completed) {
				var rmenu = getMenu();
				rmenu.show(t, 'tr-br?');
			}
        }
    }

    function onMouseOver(e, t){
        if(Ext.fly(t).hasClass('reminder')){
            Ext.fly(t.parentNode).addClass('reminder-over');
        }
    }

    function onMouseOut(e, t){
        if(Ext.fly(t).hasClass('reminder')){
            Ext.fly(t.parentNode).removeClass('reminder-over');
        }
    }

    Ext.apply(this, {
        width: 26,
        header: '<div class="reminder-col-hd"></div>',
        fixed: true,
        id: 'reminder-col',
		menuDisabled: true,
        dataIndex:'reminder',
        renderer: function(v){
			return '<div class="reminder '+(v ? 'reminder-active' : '')+'"></div>';
        },
        init : function(xg){
            grid = xg;
            grid.on('render', function(){
                var view = grid.getView();
                view.mainBody.on('contextmenu', onMouseDown);
                view.mainBody.on('mousedown', onMouseDown);
                view.mainBody.on('mouseover', onMouseOver);
                view.mainBody.on('mouseout', onMouseOut);
            });
        }
    });
};
