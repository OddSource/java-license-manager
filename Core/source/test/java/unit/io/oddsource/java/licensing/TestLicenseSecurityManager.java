/*
 * Copyright © 2010-2019 OddSource Code (license@oddsource.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.oddsource.java.licensing;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ReflectPermission;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Permission;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import io.oddsource.java.licensing.immutable.ImmutableAbstractCollection;
import io.oddsource.java.licensing.immutable.ImmutableArrayList;
import io.oddsource.java.licensing.immutable.ImmutableIterator;
import io.oddsource.java.licensing.immutable.ImmutableLinkedHashSet;
import io.oddsource.java.licensing.immutable.ImmutableListIterator;
import io.oddsource.java.licensing.mock.StateFlag;

/**
 * Test class for LicenseSecurityManager.
 */
@SuppressWarnings("EmptyMethod")
public class TestLicenseSecurityManager
{
    private LicenseSecurityManager manager;

    @Before
    public void setUp()
    {
        this.manager = LicenseSecurityManager.getInstance();
    }

    @After
    public void tearDown()
    {

    }

    @Test(expected = IllegalArgumentException.class)
    public void testSecurityManagerIsSuitableReplacement01()
    {
        LicenseSecurityManager.securityManagerIsSuitableReplacement(null);
    }

    @Test
    public void testSecurityManagerIsSuitableReplacement02()
    {
        final StateFlag checkPermission = new StateFlag();

        assertFalse(
            "The security manager should not be suitable.",
            LicenseSecurityManager.securityManagerIsSuitableReplacement(new SecurityManager()
            {
                @SuppressWarnings("deprecation")
                public void checkMemberAccess(Class<?> reflectionClass, int memberAccessType)
                {
                    fail("checkMemberAccess should not have been called.");
                }

                @Override
                public void checkPermission(Permission permission)
                {
                    if(checkPermission.state)
                    {
                        fail("checkPermission should not have been called twice.");
                    }
                    checkPermission.state = true;
                    assertTrue(
                        "The permission is not correct",
                        permission instanceof LicenseSecurityManager.ObjectReflectionPermission
                    );
                    AccessibleObject target = (
                        (LicenseSecurityManager.ObjectReflectionPermission) permission
                    ).targets[0];
                    assertSame("The class is not correct.", License.class, ((Member) target).getDeclaringClass());
                }
            })
        );

        assertTrue("checkPermission should have been called.", checkPermission.state);
    }

    @Test
    public void testSecurityManagerIsSuitableReplacement03()
    {
        final StateFlag checkPermission1 = new StateFlag();
        final StateFlag checkPermission2 = new StateFlag();

        assertFalse(
            "The security manager should not be suitable.",
            LicenseSecurityManager.securityManagerIsSuitableReplacement(new SecurityManager()
            {
                @SuppressWarnings("deprecation")
                public void checkMemberAccess(Class<?> reflectionClass, int memberAccessType)
                {
                    fail("checkMemberAccess should not have been called.");
                }

                @Override
                public void checkPermission(Permission permission)
                {
                    assertTrue(
                        "The permission is not correct",
                        permission instanceof LicenseSecurityManager.ObjectReflectionPermission
                    );
                    AccessibleObject target = (
                        (LicenseSecurityManager.ObjectReflectionPermission) permission
                    ).targets[0];
                    assertSame(
                        "The class is not correct.",
                        checkPermission1.state ? LicenseManager.class : License.class,
                        ((Member) target).getDeclaringClass()
                    );

                    if(checkPermission1.state)
                    {
                        checkPermission2.state = true;
                    }
                    checkPermission1.state = true;

                    if(((Member) target).getDeclaringClass() == License.class)
                    {
                        throw new SecurityException();
                    }
                }
            })
        );

        assertTrue("checkPermission should have been called.", checkPermission1.state);
        assertTrue("checkPermission should have been called twice.", checkPermission2.state);
    }

    @Test
    public void testSecurityManagerIsSuitableReplacement04()
    {
        final StateFlag checkPermission1 = new StateFlag();
        final StateFlag checkPermission2 = new StateFlag();
        final StateFlag checkPermission3 = new StateFlag();

        assertFalse(
            "The security manager should not be suitable.",
            LicenseSecurityManager.securityManagerIsSuitableReplacement(new SecurityManager()
            {
                @SuppressWarnings("deprecation")
                public void checkMemberAccess(Class<?> reflectionClass, int memberAccessType)
                {
                    fail("checkMemberAccess should not have been called.");
                }

                @Override
                public void checkPermission(Permission permission)
                {
                    if(permission instanceof LicenseSecurityManager.ObjectReflectionPermission)
                    {
                        AccessibleObject target = (
                            (LicenseSecurityManager.ObjectReflectionPermission) permission
                        ).targets[0];
                        Class<?> reflectionClass = ((Member) target).getDeclaringClass();
                        assertSame(
                            "The class is not correct.",
                            checkPermission1.state ? LicenseManager.class : License.class,
                            reflectionClass
                        );

                        if(checkPermission1.state)
                        {
                            checkPermission2.state = true;
                        }
                        checkPermission1.state = true;

                        if(reflectionClass == License.class || reflectionClass == LicenseManager.class)
                        {
                            throw new SecurityException();
                        }
                    }

                    assertTrue("The permission object is not correct.", permission instanceof RuntimePermission);
                    assertEquals("The checked permission is not correct.", "setSecurityManager", permission.getName());

                    checkPermission3.state = true;
                }
            })
        );

        assertTrue("checkPermission should have been called.", checkPermission1.state);
        assertTrue("checkPermission should have been called twice.", checkPermission2.state);
        assertTrue("checkPermission should have been called thrice.", checkPermission3.state);
    }

    @Test
    public void testSecurityManagerIsSuitableReplacement05()
    {
        final StateFlag checkPermission1 = new StateFlag();
        final StateFlag checkPermission2 = new StateFlag();
        final StateFlag checkPermission3 = new StateFlag();

        assertTrue(
            "The security manager should be suitable.",
            LicenseSecurityManager.securityManagerIsSuitableReplacement(new SecurityManager()
            {
                @SuppressWarnings("deprecation")
                public void checkMemberAccess(Class<?> reflectionClass, int memberAccessType)
                {
                    fail("checkMemberAccess should not have been called.");
                }

                @Override
                public void checkPermission(Permission permission)
                {
                    if(permission instanceof LicenseSecurityManager.ObjectReflectionPermission)
                    {
                        AccessibleObject target = (
                            (LicenseSecurityManager.ObjectReflectionPermission) permission
                        ).targets[0];
                        Class<?> reflectionClass = ((Member) target).getDeclaringClass();
                        assertSame(
                            "The class is not correct.",
                            checkPermission1.state ? LicenseManager.class : License.class,
                            reflectionClass
                        );

                        if(checkPermission1.state)
                        {
                            checkPermission2.state = true;
                        }
                        checkPermission1.state = true;

                        if(reflectionClass == License.class || reflectionClass == LicenseManager.class)
                        {
                            throw new SecurityException();
                        }
                    }

                    assertTrue("The permission object is not correct.", permission instanceof RuntimePermission);
                    assertEquals("The checked permission is not correct.", "setSecurityManager", permission.getName());

                    checkPermission3.state = true;

                    throw new SecurityException();
                }
            })
        );

        assertTrue("checkPermission should have been called.", checkPermission1.state);
        assertTrue("checkPermission should have been called twice.", checkPermission2.state);
        assertTrue("checkPermission should have been called thrice.", checkPermission3.state);
    }

    @Test(expected = SecurityException.class)
    public void testCheckPermission01()
    {
        this.manager.checkPermission(new RuntimePermission("setSecurityManager"));
    }

    @Test
    public void testCheckPermission02()
    {
        this.manager.checkPermission(new ReflectPermission("suppressAccessChecks"));
    }

    private void checkMemberAccess(Class<?> targetClass, String methodName, Class<?>... parameterTypes)
        throws NoSuchMethodException
    {
        this.manager.checkPermission(
            new LicenseSecurityManager.ObjectReflectionPermission(
                "suppressAccessChecks",
                new AccessibleObject[] {targetClass.getDeclaredMethod(methodName, parameterTypes)}
            )
        );
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess01() throws NoSuchMethodException
    {
        this.checkMemberAccess(DataSignatureManager.class, "getSignature");
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess02() throws NoSuchMethodException
    {
        this.checkMemberAccess(License.class, "deserialize", byte[].class);
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess03() throws NoSuchMethodException
    {
        this.checkMemberAccess(LicenseManager.class, "validateLicense", License.class);
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess04() throws NoSuchMethodException
    {
        this.checkMemberAccess(LicenseSecurityManager.class, "getSecurityContext");
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess05() throws NoSuchMethodException
    {
        this.checkMemberAccess(ObjectSerializer.class, "writeObject", Serializable.class);
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess06() throws NoSuchMethodException
    {
        this.checkMemberAccess(SignedLicense.class, "getSignatureContent");
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess07() throws NoSuchMethodException
    {
        this.checkMemberAccess(ImmutableAbstractCollection.class, "checkValidity");
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess08() throws NoSuchMethodException
    {
        this.checkMemberAccess(ImmutableArrayList.class, "indexOf", Object.class);
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess09() throws NoSuchMethodException
    {
        this.checkMemberAccess(ImmutableIterator.class, "hasNext");
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess10() throws NoSuchMethodException
    {
        this.checkMemberAccess(ImmutableLinkedHashSet.class, "get", int.class);
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess11() throws NoSuchMethodException
    {
        this.checkMemberAccess(ImmutableListIterator.class, "hasNext");
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess12() throws NoSuchMethodException
    {
        this.checkMemberAccess(String.class, "checkBounds", byte[].class, int.class, int.class);
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess13() throws NoSuchMethodException
    {
        this.checkMemberAccess(System.class, "checkIO");
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess14() throws NoSuchMethodException
    {
        this.checkMemberAccess(Class.class, "getInterfaces0");
    }

    @Test
    public void testCheckMemberAccess15() throws NoSuchMethodException
    {
        this.checkMemberAccess(FeatureRestriction.class, "operand");
    }

    @Test
    public void testCheckMemberAccess16() throws NoSuchMethodException
    {
        this.checkMemberAccess(Test.class, "timeout");
    }

    @Test
    public void testCheckMemberAccess17() throws NoSuchMethodException
    {
        this.checkMemberAccess(Integer.class, "toString");
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess18() throws NoSuchMethodException
    {
        this.checkMemberAccess(AccessibleObject.class, "setAccessible", boolean.class);
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess19() throws NoSuchMethodException
    {
        this.checkMemberAccess(Constructor.class, "acquireConstructorAccessor");
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess20() throws NoSuchMethodException
    {
        this.checkMemberAccess(Executable.class, "synthesizeAllParams");
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess21() throws NoSuchMethodException
    {
        this.checkMemberAccess(Field.class, "getGenericInfo");
    }

    @Test(expected = SecurityException.class)
    public void testCheckMemberAccess22() throws NoSuchMethodException
    {
        this.checkMemberAccess(Method.class, "getFactory");
    }

    @Test
    public void testCheckMemberAccess23() throws NoSuchMethodException
    {
        this.checkMemberAccess(Object.class, "hashCode");
    }

    @Test
    public void testCheckMemberAccess24() throws NoSuchMethodException
    {
        this.checkMemberAccess(java.util.ArrayList.class, "ensureCapacityInternal", int.class);
    }

    @Test(expected = SecurityException.class)
    @Ignore("Un-skip after OddSource/java-license-manager#5 is fixed.")
    public void testPrivateReflection01() throws NoSuchFieldException
    {
        License.class.getDeclaredField("goodAfterDate").setAccessible(true);
    }

    @Test(expected = SecurityException.class)
    @Ignore("Un-skip after OddSource/java-license-manager#5 is fixed.")
    public void testPrivateReflection02() throws NoSuchFieldException
    {
        LicenseManager.class.getDeclaredField("licenseCache").setAccessible(true);
    }

    @Test
    public void testSecurityManagerWasSet()
    {
        assertSame("The security manager mas not set correctly.", this.manager, System.getSecurityManager());
    }

    @Test(expected = SecurityException.class)
    public void testSetSecurityManager01()
    {
        System.setSecurityManager(null);
    }

    @Test(expected = SecurityException.class)
    public void testSetSecurityManager02()
    {
        System.setSecurityManager(this.manager);
    }

    @Test(expected = SecurityException.class)
    @Ignore("Un-skip after OddSource/java-license-manager#5 is fixed.")
    public void testReflectionOnClassClassBlocked() throws NoSuchMethodException
    {
        java.lang.Class.class.getDeclaredMethod("privateGetDeclaredMethods", boolean.class).setAccessible(true);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testReflectionOnSecurityManagerBlocked() throws NoSuchFieldException
    {
        // This used to get all the way to SecurityException, but now it's NoSuchFieldException because built-in Java
        // security prevents reflective access to the security manager
        java.lang.System.class.getDeclaredField("security").setAccessible(true);
    }

    @Test(expected = SecurityException.class)
    @Ignore("Un-skip after OddSource/java-license-manager#5 is fixed.")
    public void testReflectionOnSecurityManagerSetterBlocked() throws NoSuchMethodException
    {
        java.lang.System.class.getDeclaredMethod("setSecurityManager0", SecurityManager.class).setAccessible(true);
    }

    @Test
    public void testCheckPermission()
    {
        this.manager.checkPermission(new ReflectPermission("fakePermission"), new Object());
    }

    @Test
    public void testCheckExec()
    {
        this.manager.checkExec("test");
    }

    @Test
    public void testCheckLink()
    {
        this.manager.checkLink("test");
    }

    @Test
    public void testCheckRead()
    {
        this.manager.checkRead("test", new Object());
    }

    @Test
    public void testCheckDelete()
    {
        this.manager.checkDelete("test");
    }

    @Test
    public void testCheckConnect()
    {
        this.manager.checkConnect("test", 1, new Object());
    }

    @Test
    public void testCheckListen()
    {
        this.manager.checkListen(1);
    }

    @Test
    public void testCheckAccept()
    {
        this.manager.checkAccept("test", 1);
    }

    @Test
    public void testCheckMulticast() throws UnknownHostException
    {
        this.manager.checkMulticast(InetAddress.getByName("127.0.0.1"));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testCheckMulticastWithByte() throws UnknownHostException
    {
        this.manager.checkMulticast(InetAddress.getByName("127.0.0.1"), (byte) 0x00);
    }

    @Test
    public void testCheckPropertiesAccess()
    {
        this.manager.checkPropertiesAccess();
    }

    @Test
    public void testCheckPrintJobAccess()
    {
        this.manager.checkPrintJobAccess();
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testCheckSystemClipBoardAccess()
    {
        this.manager.checkSystemClipboardAccess();
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testCheckAwtEventQueueAccess()
    {
        this.manager.checkAwtEventQueueAccess();
    }

    @Test
    public void testCheckPackageDefinition()
    {
        this.manager.checkPackageDefinition("test");
    }

    @Test
    public void testCheckSetFactory()
    {
        this.manager.checkSetFactory();
    }
}
