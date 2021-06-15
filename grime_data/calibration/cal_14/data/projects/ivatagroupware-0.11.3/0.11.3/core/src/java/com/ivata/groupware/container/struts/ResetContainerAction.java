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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;

/**
 * Call this action to reset the main container and reload the configuration.
 *
 * @since ivata groupware 0.11 (2005-03-31)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.1 $
 */

public class ResetContainerAction extends MaskAction {
    public ResetContainerAction(MaskFactory maskFactoryParam,
            MaskAuthenticator authenticatorParam) {
        super(maskFactoryParam, authenticatorParam);
    }

    /**
     * This method simply calls {@link PicoContainerFactory#reset
     * PicoContainerFactory.reset()}.
     *
     * @param mappingParam
     * Refer to {@link MaskAction#execute}.
     * @param errorsParam
     * Refer to {@link MaskAction#execute}.
     * @param formParam
     * Refer to {@link MaskAction#execute}.
     * @param requestParam
     * Refer to {@link MaskAction#execute}.
     * @param responseParam
     * Refer to {@link MaskAction#execute}.
     * @param sessionParam
     * Refer to {@link MaskAction#execute}.
     * @return
     * Refer to {@link MaskAction#execute}.
     * @throws SystemException
     * Refer to {@link MaskAction#execute}.
     */
    public String execute(ActionMapping mappingParam, ActionErrors errorsParam,
            ActionForm formParam, HttpServletRequest requestParam,
            HttpServletResponse responseParam, HttpSession sessionParam)
            throws SystemException {
        // just reset the container!
        PicoContainerFactory.getInstance().initialize();
        return "success";
    }
}
