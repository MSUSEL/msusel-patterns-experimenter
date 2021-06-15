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
Ext.ux.PanelResizer = Ext.extend(Ext.util.Observable, {
    minHeight: 0,
    maxHeight:10000000,

    constructor: function(config){
        Ext.apply(this, config);
        this.events = {};
        Ext.ux.PanelResizer.superclass.constructor.call(this, config);
    },

    init : function(p){
        this.panel = p;

        if(this.panel.elements.indexOf('footer')==-1){
            p.elements += ',footer';
        }
        p.on('render', this.onRender, this);
    },

    onRender : function(p){
        this.handle = p.footer.createChild({cls:'x-panel-resize'});

        this.tracker = new Ext.dd.DragTracker({
            onStart: this.onDragStart.createDelegate(this),
            onDrag: this.onDrag.createDelegate(this),
            onEnd: this.onDragEnd.createDelegate(this),
            tolerance: 3,
            autoStart: 300
        });
        this.tracker.initEl(this.handle);
        p.on('beforedestroy', this.tracker.destroy, this.tracker);
    },

	// private
    onDragStart: function(e){
        this.dragging = true;
        this.startHeight = this.panel.el.getHeight();
        this.fireEvent('dragstart', this, e);
    },

	// private
    onDrag: function(e){
        this.panel.setHeight((this.startHeight-this.tracker.getOffset()[1]).constrain(this.minHeight, this.maxHeight));
        this.fireEvent('drag', this, e);
    },

	// private
    onDragEnd: function(e){
        this.dragging = false;
        this.fireEvent('dragend', this, e);
    }
});
