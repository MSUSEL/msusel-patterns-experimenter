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
FeedWindow = function() {
    this.feedUrl = new Ext.form.ComboBox({
        id: 'feed-url',
        fieldLabel: 'Enter the URL of the feed to add',
        emptyText: 'http://example.com/blog/feed',
        width: 450,
        validationEvent: false,
        validateOnBlur: false,
        msgTarget: 'under',
        triggerAction: 'all',
        displayField: 'url',
        mode: 'local',

        listeners:{
            valid: this.syncShadow,
            invalid: this.syncShadow,
            scope: this
        },
        tpl: new Ext.XTemplate(
                '<tpl for="."><div class="x-combo-list-item">',
                '<em>{url}</em><strong>{text}</strong>',
                '<div class="x-clear"></div>',
                '</div></tpl>'),
        store: new Ext.data.SimpleStore({
            fields: ['url', 'text'],
            data : this.defaultFeeds
        })
    });

    this.form = new Ext.FormPanel({
        labelAlign:'top',
        items:this.feedUrl,
        border: false,
        bodyStyle:'background:transparent;padding:10px;'
    });

    FeedWindow.superclass.constructor.call(this, {
        title: 'Add Feed',
        iconCls: 'feed-icon',
        id: 'add-feed-win',
        autoHeight: true,
        width: 500,
        resizable: false,
        plain:true,
        modal: true,
        y: 100,
        autoScroll: true,
        closeAction: 'hide',

        buttons:[{
            text: 'Add Feed!',
            handler: this.onAdd,
            scope: this
        },{
            text: 'Cancel',
            handler: this.hide.createDelegate(this, [])
        }],

        items: this.form
    });

    this.addEvents({add:true});
}

Ext.extend(FeedWindow, Ext.Window, {
    defaultFeeds : [
        ['http://www.divergingpath.com/rss.cfm?mode=full', 'Aaron Conran\'s Blog'],
        ['http://feeds.yuiblog.com/YahooUserInterfaceBlog',  'Yahoo! UI Blog'],
        ['http://feeds.feedburner.com/jquery/', 'jQuery Blog'],
        ['http://sports.yahoo.com/nba/rss.xml', 'NBA News'],
        ['http://feeds.dzone.com/dzone/frontpage', 'DZone.com']
    ],

    show : function(){
        if(this.rendered){
            this.feedUrl.setValue('');
        }
        FeedWindow.superclass.show.apply(this, arguments);
    },

    onAdd: function() {
        this.el.mask('Validating Feed...', 'x-mask-loading');
        var url = this.feedUrl.getValue();
        Ext.Ajax.request({
            url: 'feed-proxy.php',
            params: {feed: url},
            success: this.validateFeed,
            failure: this.markInvalid,
            scope: this,
            feedUrl: url
        });
    },

    markInvalid : function(){
        this.feedUrl.markInvalid('The URL specified is not a valid RSS2 feed.');
        this.el.unmask();
    },

    validateFeed : function(response, options){
        var dq = Ext.DomQuery;
        var url = options.feedUrl;

        try{
            var xml = response.responseXML;
            var channel = xml.getElementsByTagName('channel')[0];
            if(channel){
                var text = dq.selectValue('title', channel, url);
                var description = dq.selectValue('description', channel, 'No description available.');
                this.el.unmask();
                this.hide();

                return this.fireEvent('validfeed', {
                    url: url,
                    text: text,
                    description: description
                });
            }
        }catch(e){
        }
        this.markInvalid();
    }
});
