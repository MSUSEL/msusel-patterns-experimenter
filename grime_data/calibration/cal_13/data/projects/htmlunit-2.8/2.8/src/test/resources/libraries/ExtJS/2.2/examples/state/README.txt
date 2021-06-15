====
    The MIT License (MIT)

    MSUSEL Arc Framework
    Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
    Software Engineering Laboratory and Idaho State University, Informatics and
    Computer Science, Empirical Software Engineering Laboratory

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
====

This session scope state provider is in use in window/layout.html.

Usage:

get-state.php is included as JS file in the header of any page that needs state information. For a single
page application, that would be the main page of the application. It is NOT loaded via XHR/Ajax.

save-state.php is included in every page of the application, including pages loaded via ajax.

Inilialization of the SessionProvider looks like:
Ext.state.Manager.setProvider(new Ext.state.SessionProvider({state: Ext.appState}));

The way the session provider works is when a state change occurs, a cookie is set on the client 
with the new state data. The next time any page is requested on the server, save-state.php
will see that cookie, save it in the application state and CLEAR THE COOKIE. This way your application
doesn't have cookies creating unneccessary network latency.
