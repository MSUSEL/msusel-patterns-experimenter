--
-- The MIT License (MIT)
--
-- MSUSEL Arc Framework
-- Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
-- Software Engineering Laboratory and Idaho State University, Informatics and
-- Computer Science, Empirical Software Engineering Laboratory
--
-- Permission is hereby granted, free of charge, to any person obtaining a copy
-- of this software and associated documentation files (the "Software"), to deal
-- in the Software without restriction, including without limitation the rights
-- to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
-- copies of the Software, and to permit persons to whom the Software is
-- furnished to do so, subject to the following conditions:
--
-- The above copyright notice and this permission notice shall be included in all
-- copies or substantial portions of the Software.
--
-- THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
-- IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
-- FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
-- AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
-- LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
-- OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
-- SOFTWARE.
--

--==============================================================================
-- == SETTINGS ==
--==============================================================================
insert into setting (name, value, type, description, enable, created, created_by, modified, modified_by) values ('siteTheme', 'shadow', 1, 'Theme to use by default.', true, now(), 1, now(), 1);
update setting set value='/images/logo.gif' where name='siteLogo';
update setting set value='ivatagroupware v0.11' where name='siteName';
--==============================================================================
-- == ADDRESS BOOK ==
--==============================================================================
update address_country set priority=99 where priority is null;
alter table person_user drop column person cascade;
--==============================================================================
-- == NAVIGATION ==
--==============================================================================
update navigation_menu_item set text='new doc' where text='new document';
--==============================================================================

