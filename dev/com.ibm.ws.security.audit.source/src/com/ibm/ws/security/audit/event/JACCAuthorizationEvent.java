/*******************************************************************************
 * Copyright (c) 2018, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.security.audit.event;

import java.net.URLDecoder;
import java.security.Permission;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.websphere.ras.Tr;
import com.ibm.websphere.ras.TraceComponent;
import com.ibm.websphere.security.audit.AuditAuthenticationResult;
import com.ibm.websphere.security.audit.AuditConstants;
import com.ibm.websphere.security.audit.AuditEvent;
import com.ibm.ws.security.audit.source.utils.AuditUtils;

/**
 * Class with default values for authorization events
 */
public class JACCAuthorizationEvent extends AuditEvent {

    private static final TraceComponent tc = Tr.register(JACCAuthorizationEvent.class);

    @SuppressWarnings("unchecked")
    public JACCAuthorizationEvent() {
        set(AuditEvent.EVENTNAME, AuditConstants.SECURITY_AUTHZ);
        setInitiator((Map<String, Object>) AuditEvent.STD_INITIATOR.clone());
        setObserver((Map<String, Object>) AuditEvent.STD_OBSERVER.clone());
        setTarget((Map<String, Object>) AuditEvent.STD_TARGET.clone());
    }

    public JACCAuthorizationEvent(HttpServletRequest req, AuditAuthenticationResult authResult, Permission webPerm, String uriName, String containerType,
                                  Integer statusCode) {
        this();
        try {
            // add initiator
            if (req != null && req.getRemoteAddr() != null)
                set(AuditEvent.INITIATOR_HOST_ADDRESS, req.getRemoteAddr());
            String agent = req.getHeader("User-Agent");
            if (agent != null)
                set(AuditEvent.INITIATOR_HOST_AGENT, agent);
            // add target
            set(AuditEvent.TARGET_NAME, URLDecoder.decode(req.getRequestURI(), "UTF-8"));
            if (req.getQueryString() != null) {
                String str = URLDecoder.decode(req.getQueryString(), "UTF-8");
                str = AuditUtils.hidePassword(str);
                set(AuditEvent.TARGET_PARAMS, str);
            }
            set(AuditEvent.TARGET_HOST_ADDRESS, req.getLocalAddr() + ":" + req.getLocalPort());
            set(AuditEvent.TARGET_CREDENTIAL_TYPE, authResult.getAuditCredType());

            if (authResult.getAuditCredValue() != null)
                set(AuditEvent.TARGET_CREDENTIAL_TOKEN, authResult.getAuditCredValue());
            else if (req.getUserPrincipal() != null && req.getUserPrincipal().getName() != null)
                set(AuditEvent.TARGET_CREDENTIAL_TOKEN, req.getUserPrincipal().getName());

            set(AuditEvent.TARGET_METHOD, AuditUtils.getRequestMethod(req));
            String sessionID = AuditUtils.getSessionID(req);
            if (sessionID != null) {
                set(AuditEvent.TARGET_SESSION, sessionID);
            }
            if (uriName != null) {
                set(AuditEvent.TARGET_URINAME, uriName);
            }

            set(AuditEvent.TARGET_REALM, AuditUtils.getRealmName());

            if (webPerm != null) {
                set(AuditEvent.TARGET_JACC_PERMISSIONS, webPerm.getActions());
            }

            if (containerType != null) {
                set(AuditEvent.TARGET_JACC_CONTAINER, containerType);
            }

            if (statusCode == HttpServletResponse.SC_OK) {
                setOutcome("success");
                set(AuditEvent.REASON_CODE, statusCode);
                set(AuditEvent.REASON_TYPE, AuditUtils.getRequestScheme(req));
            } else {
                setOutcome("failure");
                set(AuditEvent.REASON_CODE, statusCode);
                set(AuditEvent.REASON_TYPE, AuditUtils.getRequestScheme(req));
            }
        } catch (Exception e) {
            if (TraceComponent.isAnyTracingEnabled() && tc.isDebugEnabled()) {
                Tr.debug(tc, "Internal error creating AuthorizationEvent", e);
            }
        }
    }
}
