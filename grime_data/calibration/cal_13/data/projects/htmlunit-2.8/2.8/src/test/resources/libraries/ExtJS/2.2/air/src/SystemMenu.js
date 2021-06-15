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
/**
 * @class Ext.air.SystemMenu
 * 
 * Provides platform independent handling of adding item to the application menu, creating the menu or 
 * items as needed. <br/><br/>
 * 
 * This class also provides the ability to bind standard Ext.Action instances with NativeMenuItems
 * 
 * @singleton
 */
Ext.air.SystemMenu = function(){
	var menu;
	// windows
	if(air.NativeWindow.supportsMenu && nativeWindow.systemChrome != air.NativeWindowSystemChrome.NONE) {
        menu = new air.NativeMenu();
        nativeWindow.menu = menu;
    }
    
	// mac
    if(air.NativeApplication.supportsMenu) {
		menu = air.NativeApplication.nativeApplication.menu;
    }

    function find(menu, text){
        for(var i = 0, len = menu.items.length; i < len; i++){
            if(menu.items[i]['label'] == text){
                return menu.items[i];
            }
        }
        return null;
    }

    return {
		/**
		 * Add items to one of the application menus
		 * @param {String} text The application menu to add the actions to (e.g. 'File' or 'Edit').
		 * @param {Array} actions An array of Ext.Action objects or menu item configs
		 * @param {Number} mindex The index of the character in "text" which should be used for 
		 * keyboard access
		 * @return air.NativeMenu The raw submenu
		 */
		add: function(text, actions, mindex){

            var item = find(menu, text);
            if(!item){
                item = menu.addItem(new air.NativeMenuItem(text));
                item.mnemonicIndex = mindex || 0;

                item.submenu = new air.NativeMenu();
			}
			for (var i = 0, len = actions.length; i < len; i++) {
				item.submenu.addItem(actions[i] == '-' ? new air.NativeMenuItem("", true) : Ext.air.MenuItem(actions[i]));
			}
            return item.submenu;
        },
		
		/**
		 * Returns the application menu
		 */
		get : function(){
			return menu;
		}
	};	
}();

// ability to bind native menu items to an Ext.Action
Ext.air.MenuItem = function(action){
	if(!action.isAction){
		action = new Ext.Action(action);
	}
	var cfg = action.initialConfig;
	var nativeItem = new air.NativeMenuItem(cfg.itemText || cfg.text);
	
	nativeItem.enabled = !cfg.disabled;

    if(!Ext.isEmpty(cfg.checked)){
        nativeItem.checked = cfg.checked;
    }

    var handler = cfg.handler;
	var scope = cfg.scope;
	
	nativeItem.addEventListener(air.Event.SELECT, function(){
		handler.call(scope || window, cfg);
	});
	
	action.addComponent({
		setDisabled : function(v){
			nativeItem.enabled = !v;
		},
		
		setText : function(v){
			nativeItem.label = v;
		},
		
		setVisible : function(v){
			// could not find way to hide in air so disable?
			nativeItem.enabled = !v;
		},
		
		setHandler : function(newHandler, newScope){
			handler = newHandler;
			scope = newScope;
		},
		// empty function
		on : function(){}
	});
	
	return nativeItem;
}
