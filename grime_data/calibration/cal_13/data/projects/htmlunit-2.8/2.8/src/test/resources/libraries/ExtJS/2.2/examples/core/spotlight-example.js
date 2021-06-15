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
    
    var spot = new Ext.Spotlight({
        easing: 'easeOut',
        duration: .3
    });
    
    var DemoPanel = Ext.extend(Ext.Panel, {
        title: 'Demo Panel',
        frame: true,
        width: 200,
        height: 150,
        html: 'Some panel content goes here!',
        bodyStyle: 'padding:10px 15px;',
        
        toggle: function(on){
            this.buttons[0].setDisabled(!on);
        }
    });
    
    var p1, p2, p3;
    var updateSpot = function(id){
        if(typeof id == 'string'){
            spot.show(id);
        }else if (!id && spot.active){
            spot.hide();
        }
        p1.toggle(id==p1.id);
        p2.toggle(id==p2.id);
        p3.toggle(id==p3.id);
    };
    
    new Ext.Panel({
        renderTo: Ext.getBody(),
        layout: 'table',
        id: 'demo-ct',
        border: false,
        layoutConfig: {
            columns: 3
        },
        items: [p1 = new DemoPanel({
            id: 'panel1',
            buttons: [{
                text: 'Next Panel',
                handler: updateSpot.createDelegate(this, ['panel2'])
            }]
        }),
        p2 = new DemoPanel({
            id: 'panel2',
            buttons: [{
                text: 'Next Panel',
                handler: updateSpot.createDelegate(this, ['panel3'])
            }]
        }),
        p3 = new DemoPanel({
            id: 'panel3',
            buttons: [{
                text: 'Done',
                handler: updateSpot.createDelegate(this, [false])
            }]
        })]
    });
    
    new Ext.Button({
        text: 'Start',
        renderTo: 'start-ct',
        handler: updateSpot.createDelegate(this, ['panel1'])
    });
    
    updateSpot(false);
});
