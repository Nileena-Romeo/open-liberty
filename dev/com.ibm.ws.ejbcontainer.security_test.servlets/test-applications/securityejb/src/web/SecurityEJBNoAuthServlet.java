/*******************************************************************************
 * Copyright (c) 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package web;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import com.ibm.ws.ejbcontainer.security.test.SecurityEJBInterface;

/**
 * Security EJB servlet used for Pure Annotation tests - PureAnnA0xTest.
 */
@SuppressWarnings("serial")
public class SecurityEJBNoAuthServlet extends SecurityEJBBaseServlet {

    final String servletName;

    public SecurityEJBNoAuthServlet() {
        this.servletName = "SecurityEJBServlet";
    }

    @Override
    String servletName() {
        return servletName;
    }

    //Inject Stateless/Singleton EJB interfaces for Pure Annotation Tests - PureAnnA0xTest.

    @EJB(beanName = "SecurityEJBA01Bean")
    private SecurityEJBInterface injectedEJB01;

    protected Map<String, Invoke> methodMap = new HashMap<String, Invoke>();

    @Override
    protected Map<String, SecurityEJBInterface> statelessBeans() {

        Map<String, SecurityEJBInterface> beanMap = new HashMap<String, SecurityEJBInterface>();
        if (beanMap.size() == 0) {
            beanMap.put("ejb01", injectedEJB01);
        }
        return beanMap;
    }

    @Override
    protected Map<String, String> statefulBeans() {

        Map<String, String> beanMap = new HashMap<String, String>();
        beanMap.put("ejb04", "java:app/SecurityEJB/SecurityEJBA04Bean");
        return beanMap;
    }

    protected void buildMethods() {
        if (methodMap.size() == 0) {
            methodMap.put("checkAuthenticated", new Invoke() {
                @Override
                public String go(SecurityEJBInterface ejb) {
                    return ejb.checkAuthenticated();
                }
            });
            methodMap.put("permitAuthenticated", new Invoke() {
                @Override
                public String go(SecurityEJBInterface ejb) {
                    return ejb.permitAuthenticated();
                }
            });
        }

    }

    @Override
    protected void invokeEJBMethod(StringBuffer sb, String testInstance, String testMethod) {

        try {

            stateless.populate(statelessBeans());
            stateful.populate(statefulBeans());
            buildMethods();

            SecurityBean bean = lookup(testInstance);
            validateInstance(testInstance, bean);

            Invoke invokeMe = methodMap.get(testMethod);
            validateMethodWithArgs(testMethod, invokeMe);

            writeLine(sb, invokeMe.go(bean.get()));

        } catch (RuntimeException e) {
            if (e.getClass().getCanonicalName().equals("javax.ejb.EJBAccessException")) {
                writeLine(sb, "EJBAccessException: " + e.getMessage());
            } else {
                writeLine(sb, "RuntimeException: " + e.getMessage());
            }
        } catch (Exception e) {
            writeLine(sb, "Unexpected exception: " + e.toString());
        }
    }

    private SecurityBean lookup(String beanname) {
        SecurityBean b;
        b = stateless.lookup(beanname);
        if (b != null)
            return b;
        b = stateful.lookup(beanname);
        if (b != null)
            return b;
        throw new RuntimeException("No bean found on lookup");
    }

    private void validateInstance(String testInstanceKey, SecurityBean bean) {
        if (bean == null)
            throw new RuntimeException("Unrecognized EJB test instance name: " + testInstanceKey);
    }

    private void validateMethodWithArgs(String testMethodKey, Invoke invoker) {
        if (invoker == null)
            throw new RuntimeException("Unrecognized EJB test method key: " + testMethodKey);
    }

}
