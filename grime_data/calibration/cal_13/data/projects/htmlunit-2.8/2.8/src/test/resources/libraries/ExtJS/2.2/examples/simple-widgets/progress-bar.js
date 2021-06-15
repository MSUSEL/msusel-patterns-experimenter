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
    //==== Progress bar 1 ====
    var pbar1 = new Ext.ProgressBar({
       text:'Initializing...'
    });
    var btn1 = Ext.get('btn1');
    btn1.on('click', function(){
        Ext.fly('p1text').update('Working');
        if (!pbar1.rendered){
            pbar1.render('p1');
        }else{
            pbar1.text = 'Initializing...';
            pbar1.show();
        }
        Runner.run(pbar1, Ext.get('btn1'), 10, function(){
            pbar1.reset(true);
            Ext.fly('p1text').update('Done.').show();
        });
    });

    //==== Progress bar 2 ====
    var pbar2 = new Ext.ProgressBar({
        text:'Ready',
        id:'pbar2',
        cls:'left-align',
        renderTo:'p2'
    });
    var btn2 = Ext.get('btn2');
    btn2.on('click', function(){
        Runner.run(pbar2, btn2, 12, function(){
            pbar2.reset();
            pbar2.updateText('Done.');
        });
    });

    //==== Progress bar 3 ====
    var pbar3 = new Ext.ProgressBar({
        id:'pbar3',
        width:300,
        renderTo:'p3'
    });
    pbar3.on('update', function(val){
        //You can handle this event at each progress interval if
        //needed to perform some other action
        Ext.fly('p3text').dom.innerHTML += '.';
    });
    var btn3 = Ext.get('btn3');
    btn3.on('click', function(){
        Ext.fly('p3text').update('Working');
        btn3.dom.disabled = true;
        pbar3.wait({
            interval:200,
            duration:5000,
            increment:15,
            fn:function(){
                btn3.dom.disabled = false;
                Ext.fly('p3text').update('Done');
            }
        });
    });

    //==== Progress bar 4 ====
    var pbar4 = new Ext.ProgressBar({
        text:'Waiting on you...',
        id:'pbar4',
        textEl:'p4text',
        cls:'custom',
        renderTo:'p4'
    });
    var btn4 = Ext.get('btn4');
    btn4.on('click', function(){
        Runner.run(pbar4, btn4, 19, function(){
            pbar4.updateText('All finished!');
        });
    });
});

//Please do not use the following code as a best practice! :)
var Runner = function(){
    var f = function(v, pbar, btn, count, cb){
        return function(){
            if(v > count){
                btn.dom.disabled = false;
                cb();
            }else{
                if(pbar.id=='pbar4'){
                    //give this one a different count style for fun
                    var i = v/count;
                    pbar.updateProgress(i, Math.round(100*i)+'% completed...');
                }else{
                    pbar.updateProgress(v/count, 'Loading item ' + v + ' of '+count+'...');
                }
            }
       };
    };
    return {
        run : function(pbar, btn, count, cb){
            btn.dom.disabled = true;
            var ms = 5000/count;
            for(var i = 1; i < (count+2); i++){
               setTimeout(f(i, pbar, btn, count, cb), i*ms);
            }
        }
    }
}();
