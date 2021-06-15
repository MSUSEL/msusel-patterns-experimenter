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
 * @class Ext.air.NativeObservable
 * @extends Ext.util.Observable
 * 
 * Adds ability for Ext Observable functionality to proxy events for native (AIR) object wrappers
 * 
 * @constructor
 */

Ext.air.NativeObservable = Ext.extend(Ext.util.Observable, {
	addListener : function(name){
		this.proxiedEvents = this.proxiedEvents || {};
		if(!this.proxiedEvents[name]){
			var instance = this;
			var f = function(){
				var args = Array.prototype.slice.call(arguments, 0);
				args.unshift(name);
				instance.fireEvent.apply(instance, args);
			};
			this.proxiedEvents[name] = f;
			this.getNative().addEventListener(name, f);
		}
		Ext.air.NativeObservable.superclass.addListener.apply(this, arguments);
	}
});

Ext.air.NativeObservable.prototype.on = Ext.air.NativeObservable.prototype.addListener;
