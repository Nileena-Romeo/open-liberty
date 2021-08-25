/*******************************************************************************
 * Copyright (c) 2019, 2021 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package com.ibm.ws.jpa.tests.spec10.packaging;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.ws.jpa.tests.spec10.packaging.tests.AbstractFATSuite;
import com.ibm.ws.jpa.tests.spec10.packaging.tests.Packaging_EJB;
import com.ibm.ws.jpa.tests.spec10.packaging.tests.Packaging_Web;

import componenttest.rules.repeater.RepeatTests;

@RunWith(Suite.class)
@SuiteClasses({
                Packaging_Web.class,
                Packaging_EJB.class,
})
public class JPA20Suite extends AbstractFATSuite {

    @ClassRule
    public static RepeatTests r = RepeatTests.with(new RepeatWithJPA20());

}
