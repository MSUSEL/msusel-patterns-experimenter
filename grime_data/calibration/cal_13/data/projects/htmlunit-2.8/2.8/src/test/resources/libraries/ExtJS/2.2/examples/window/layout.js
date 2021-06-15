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
    
    Ext.state.Manager.setProvider(
            new Ext.state.SessionProvider({state: Ext.appState}));

    var button = Ext.get('show-btn');

    button.on('click', function(){

        // tabs for the center
        var tabs = new Ext.TabPanel({
            region    : 'center',
            margins   : '3 3 3 0', 
            activeTab : 0,
            defaults  : {
				autoScroll : true
			},
            items     : [{
                title    : 'Bogus Tab',
                html     : Ext.example.bogusMarkup
             },{
                title    : 'Another Tab',
                html     : Ext.example.bogusMarkup
             },{ 
                title    : 'Closable Tab',
                html     : Ext.example.bogusMarkup,
                closable : true
            }]
        });

        // Panel for the west
        var nav = new Ext.Panel({
            title       : 'Navigation',
            region      : 'west',
            split       : true,
            width       : 200,
            collapsible : true,
            margins     : '3 0 3 3',
            cmargins    : '3 3 3 3'
        }); 

        var win = new Ext.Window({
            title    : 'Layout Window',
            closable : true,
            width    : 600,
            height   : 350,
            //border : false,
            plain    : true,
            layout   : 'border',
            items    : [nav, tabs]
        });

        win.show(button);
    });
});
