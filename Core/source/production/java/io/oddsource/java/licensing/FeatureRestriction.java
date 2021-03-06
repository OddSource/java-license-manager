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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation for indicating license-restricted methods, packages and types. For example, one might set an AspectJ
 * pointcut that is intercepted and asserts that annotated accesses are done with permission. If not, it might throw an
 * exception, which would be caught by the user interface (such as a servlet filter in a Java EE environment) and
 * handled accordingly.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
@Documented
@Target({ElementType.METHOD, ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FeatureRestriction
{
    /**
     * The enum value.
     *
     * @return the value.
     */
    String[] value();

    /**
     * The operand that applies to this feature restriction.
     *
     * @return the operand.
     */
    FeatureRestrictionOperand operand() default FeatureRestrictionOperand.AND;
}
