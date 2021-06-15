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
Ext.Spotlight = function(config){
    Ext.apply(this, config);
}
Ext.Spotlight.prototype = {
    active : false,
    animate : true,
    animated : false,
    duration: .25,
    easing:'easeNone',

    createElements : function(){
        var bd = Ext.getBody();

        this.right = bd.createChild({cls:'x-spotlight'});
        this.left = bd.createChild({cls:'x-spotlight'});
        this.top = bd.createChild({cls:'x-spotlight'});
        this.bottom = bd.createChild({cls:'x-spotlight'});

        this.all = new Ext.CompositeElement([this.right, this.left, this.top, this.bottom]);
    },

    show : function(el, callback, scope){
        if(this.animated){
            this.show.defer(50, this, [el, callback, scope]);
            return;
        }
        this.el = Ext.get(el);
        if(!this.right){
            this.createElements();
        }
        if(!this.active){
            this.all.setDisplayed('');
            this.applyBounds(true, false);
            this.active = true;
            Ext.EventManager.onWindowResize(this.syncSize, this);
            this.applyBounds(false, this.animate, false, callback, scope);
        }else{
            this.applyBounds(false, false, false, callback, scope); // all these booleans look hideous
        }
    },

    hide : function(callback, scope){
        if(this.animated){
            this.hide.defer(50, this, [callback, scope]);
            return;
        }
        Ext.EventManager.removeResizeListener(this.syncSize, this);
        this.applyBounds(true, this.animate, true, callback, scope);
    },

    doHide : function(){
        this.active = false;
        this.all.setDisplayed(false);
    },

    syncSize : function(){
        this.applyBounds(false, false);
    },

    applyBounds : function(basePts, anim, doHide, callback, scope){

        var rg = this.el.getRegion();

        var dw = Ext.lib.Dom.getViewWidth(true);
        var dh = Ext.lib.Dom.getViewHeight(true);

        var c = 0, cb = false;
        if(anim){
            cb = {
                callback: function(){
                    c++;
                    if(c == 4){
                        this.animated = false;
                        if(doHide){
                            this.doHide();
                        }
                        Ext.callback(callback, scope, [this]);
                    }
                },
                scope: this,
                duration: this.duration,
                easing: this.easing
            };
            this.animated = true;
        }

        this.right.setBounds(
                rg.right,
                basePts ? dh : rg.top,
                dw - rg.right,
                basePts ? 0 : (dh - rg.top),
                cb);

        this.left.setBounds(
                0,
                0,
                rg.left,
                basePts ? 0 : rg.bottom,
                cb);

        this.top.setBounds(
                basePts ? dw : rg.left,
                0,
                basePts ? 0 : dw - rg.left,
                rg.top,
                cb);

        this.bottom.setBounds(
                0,
                rg.bottom,
                basePts ? 0 : rg.right,
                dh - rg.bottom,
                cb);

        if(!anim){
            if(doHide){
                this.doHide();
            }
            if(callback){
                Ext.callback(callback, scope, [this]);
            }
        }
    },

    destroy : function(){
        this.doHide();
        Ext.destroy(
                this.right,
                this.left,
                this.top,
                this.bottom);
        delete this.el;
        delete this.all;
    }
};
