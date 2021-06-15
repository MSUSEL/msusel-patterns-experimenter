/**
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
package com.ivata.groupware.container.struts;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.groupware.struts.SetupAction;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.MaskRequestProcessor;

/**
 * <p>
 * This Struts request processor extends the standard class to instantiate
 * actions and action forms in a <strong>PicoContainer</strong> friendly way.
 * </p>
 *
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Apr 14, 2004
 * @version $Revision: 1.4 $
 */
public class PicoRequestProcessor extends MaskRequestProcessor {
    public PicoRequestProcessor() throws SystemException {
        super(PicoContainerFactory.getInstance().getMaskFactory(),
                PicoRequestProcessorImplementation.getPersistenceManager());
        setImplementation(PicoRequestProcessorImplementation
                .getRequestProcessorImplementation());
    }
    /**
     * <p>
     * Overridden to watch out for the setup action and pass the actions to it
     * so they can be cleared when the setup is finished.
     * </p>
     * <p>
     * (This is drastic but I could not see another way to reset the actions in
     * <strong>Struts</strong>.)
     * </p>
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param mapping The mapping we are using
     *
     * @exception IOException if an input/output error occurs
     */
    protected Action processActionCreate(final HttpServletRequest request,
            final HttpServletResponse response,
            final ActionMapping mapping) throws IOException {
        Action action = super.processActionCreate(request,
                response,
                mapping);
        if (action instanceof SetupAction) {
            SetupAction setupAction = (SetupAction) action;
            setupAction.setActions(actions);
        }
        return action;
    }
}
