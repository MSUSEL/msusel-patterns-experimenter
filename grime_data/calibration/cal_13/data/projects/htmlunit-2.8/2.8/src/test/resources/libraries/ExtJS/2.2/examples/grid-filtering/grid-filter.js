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
	Ext.menu.RangeMenu.prototype.icons = {
	  gt: 'img/greater_then.png', 
	  lt: 'img/less_then.png',
	  eq: 'img/equals.png'
	};
	Ext.grid.filter.StringFilter.prototype.icon = 'img/find.png';
    
    // NOTE: This is an example showing simple state management. During development,
    // it is generally best to disable state management as dynamically-generated ids
    // can change across page loads, leading to unpredictable results.  The developer
    // should ensure that stable state ids are set for stateful components in real apps.
	Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
	  
	var ds = new Ext.data.JsonStore({
	  url:'grid-filter.php',
    id: 'id',
    totalProperty: 'total',
    root: 'data',
    fields: [
      {name:'id'}, 
      {name:'company'}, 
      {name:'price'}, 
      {name:'date',type: 'date', dateFormat: 'Y-m-d H:i:s'}, 
      {name:'visible'}, 
      {name:'size'}
    ],
	  sortInfo: {field: 'company', direction: 'ASC'},
	  remoteSort: true
	});
  
	var filters = new Ext.grid.GridFilters({
	  filters:[
	    {type: 'numeric',  dataIndex: 'id'},
	    {type: 'string',  dataIndex: 'company'},
	    {type: 'numeric', dataIndex: 'price'},
	    {type: 'date',  dataIndex: 'date'},
	    {
	      type: 'list',  
	      dataIndex: 'size', 
	      options: ['small', 'medium', 'large', 'extra large'],
	      phpMode: true
	    },
	    {type: 'boolean', dataIndex: 'visible'}
	]});
	
	var cm = new Ext.grid.ColumnModel([
	  {dataIndex: 'id', header: 'Id'},
	  {dataIndex: 'company', header: 'Company', id: 'company'},
	  {dataIndex: 'price', header: 'Price'},
	  {dataIndex: 'date',header: 'Date', renderer: Ext.util.Format.dateRenderer('m/d/Y')}, 
	  {dataIndex: 'size', header: 'Size'}, 
	  {dataIndex: 'visible',header: 'Visible'}
	]);
	cm.defaultSortable = true;
	
	var grid = new Ext.grid.GridPanel({
	  id: 'example',
	  title: 'Grid Filters Example',
	  ds: ds,
	  cm: cm,
	  enableColLock: false,
	  loadMask: true,
	  plugins: filters,
	  height:400,
	  width:700,        
	  el: 'grid-example',
    autoExpandColumn: 'company',
	  bbar: new Ext.PagingToolbar({
	    store: ds,
	    pageSize: 15,
	    plugins: filters
	  })
	});
	grid.render();
	
	ds.load({params:{start: 0, limit: 15}});
});
