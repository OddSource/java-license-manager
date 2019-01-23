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
package io.oddsource.java.licensing.samples;

import io.oddsource.java.licensing.FeatureRestriction;
import io.oddsource.java.licensing.FeatureRestrictionOperand;

/**
 * 
 * @author Nicholas
 */
@FeatureRestriction("FEATURE1")
@SuppressWarnings("unused")
public class SampleLicenseRestrictedObject
{
    public SampleLicenseRestrictedObject()
    {

    }

    @FeatureRestriction({"FEATURE2", "FEATURE3"})
    public void furtherRestrictedFeature()
    {

    }

    @FeatureRestriction(value={"FEATURE2", "FEATURE3"}, operand=FeatureRestrictionOperand.OR)
    public void furtherLessRestrictedFeature()
    {

    }
}
