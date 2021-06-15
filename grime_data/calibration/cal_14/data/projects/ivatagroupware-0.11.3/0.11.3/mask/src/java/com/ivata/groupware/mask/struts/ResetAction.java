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
package com.ivata.groupware.mask.struts;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.groupware.container.struts.PicoRequestProcessorImplementation;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;

/**
 * <p>
 * This action is useful for debugging - it reload the ivata masks
 * configuration, which is a good thing if you need to change the
 * <code>ivataMasks.xml</code> file after testing some new feature.
 * </p>
 * <p>
 * <strong>Note:</strong>
 * You should disable this action on a production system (by commenting out the
 * line in the <strong>Struts</strong> config file
 * (<code>struts-config.xml</code>).
 * </p>
 *
 * @since ivata groupware 0.10 (2004-12-29)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public class ResetAction extends MaskAction {
    /**
     * <p>
     * Construct the action to reset masks.
     * </p>
     *
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public ResetAction(MaskFactory maskFactory, MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
    }

    /**
     * <p>
     * Resets the ivata masks implementation. Should be disabled on a production
     * system (see comments above).
     * </p>
     *
     * @param mapping current action mapping from <em>Struts</em> config.
     * @param log valid logging object to write messages to.
     * @param errors valid errors object to append errors to. If there are
     * any errors, the action will return to the input.
     * @param form optional ActionForm bean for this request (if any)
     * @param request non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @param session  returned from the <code>request</code> parameter.
     * @param userName current user name from session. .
     * @param settings valid, non-null settings from session.
     * @exception SystemException if there is any problem which
     * prevents processing. It will result in the webapp being forwarded
     * to
     * the standard error page.
     * @return this method returns the string used to identify the correct
     * <em>Struts</em> <code>ActionForward</code> which should follow this
     * page, or <code>null</code> if it should return to the input.
     *
     */
    public String execute(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session)
                throws SystemException {

        MaskFactory maskFactory = PicoContainerFactory.getInstance()
            .getMaskFactory();
        ServletContext context = servlet.getServletContext();
        try {
            maskFactory.readConfiguration(context.getResourceAsStream(
                    PicoRequestProcessorImplementation.MASKS_FILE_NAME));
        } catch (IOException e) {
            throw new SystemException(e);
        }

        // this action goes nowhere afterwards! (it's just for debugging)
        return null;
    }
}
