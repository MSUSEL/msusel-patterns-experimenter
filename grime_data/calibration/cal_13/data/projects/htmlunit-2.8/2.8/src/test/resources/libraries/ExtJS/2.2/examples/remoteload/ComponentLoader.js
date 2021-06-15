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
Ext.namespace('Ext.ux');

/**
 * @class Ext.ux.ComponentLoader
 * Provides an easy way to load components dynamically. If you provide these components
 * with an id you can use Ext.ComponentMgr's onAvailable function to manipulate the components
 * as they are added.
 * @singleton
 */
Ext.ux.ComponentLoader = function() {
	var cm = Ext.ComponentMgr;			
	return {
		/*
		 *  
		 */
		root: 'components',
		/*
		 * Load components from a server resource, config options include anything available in @link Ext.data.Connect#request
		 * Note: Always uses the connection of Ext.Ajax 
		 */
		load : function(config) {
			Ext.apply(config, {
				callback: this.onLoad.createDelegate(this, [config.container], true),
				scope: this
			});	
			if (config.container) {
				Ext.apply(config.params, {
					container: config.container
				});
			}
			Ext.Ajax.request(config);
		},
		// private
		onLoad : function(opts, success, response, ct) {			
			var config = Ext.decode(response.responseText);
			if (config.success) {
				var comps = config[this.root];				
				// loop over each component returned.				
				for (var i = 0; i < comps.length; i++) {
					var c = comps[i];
					// special case of viewport, no container to add to
					if (c.xtype && c.xtype === 'viewport') {
						cm.create(c);
					// add to container
					} else {
						var ct = c.container || ct;
						Ext.getCmp(ct).add(c);
						Ext.getCmp(ct).doLayout();
					}
				}
				
			} else {
				this.onFailure();
			}
		},
		onFailure: function() {
			Ext.Msg.alert('Load failed.');
		}
	};
}();
