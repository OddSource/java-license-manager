/*
 * CliOptionsBuilder.java from LicenseManager modified Thursday, January 24, 2013 14:51:21 CST (-0600).
 *
 * Copyright 2010-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.nicholaswilliams.java.licensing.licensor.interfaces.cli.spi;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

/**
 * Class description here.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class CliOptionsBuilder
{
    private static CliOptionsBuilder instance = new CliOptionsBuilder();

    private CliOptionsBuilder()
    {

    }

    public static CliOptionsBuilder get()
    {
        return CliOptionsBuilder.instance;
    }

    public Option create()
    {
        return OptionBuilder.create();
    }

    public Option create(char opt)
    {
        return OptionBuilder.create(opt);
    }

    public Option create(String opt)
    {
        return OptionBuilder.create(opt);
    }

    public CliOptionsBuilder hasArg()
    {
        OptionBuilder.hasArg();
        return this;
    }

    public CliOptionsBuilder hasArg(boolean hasArg)
    {
        OptionBuilder.hasArg(hasArg);
        return this;
    }

    public CliOptionsBuilder hasArgs()
    {
        OptionBuilder.hasArgs();
        return this;
    }

    public CliOptionsBuilder hasArgs(int num)
    {
        OptionBuilder.hasArgs(num);
        return this;
    }

    public CliOptionsBuilder hasOptionalArg()
    {
        OptionBuilder.hasOptionalArg();
        return this;
    }

    public CliOptionsBuilder hasOptionalArgs()
    {
        OptionBuilder.hasOptionalArgs();
        return this;
    }

    public CliOptionsBuilder hasOptionalArgs(int numArgs)
    {
        OptionBuilder.hasOptionalArgs(numArgs);
        return this;
    }

    public CliOptionsBuilder isRequired()
    {
        OptionBuilder.isRequired();
        return this;
    }

    public CliOptionsBuilder isRequired(boolean newRequired)
    {
        OptionBuilder.isRequired(newRequired);
        return this;
    }

    public CliOptionsBuilder withArgName(String name)
    {
        OptionBuilder.withArgName(name);
        return this;
    }

    public CliOptionsBuilder withDescription(String newDescription)
    {
        OptionBuilder.withDescription(newDescription);
        return this;
    }

    public CliOptionsBuilder withLongOpt(String newLongopt)
    {
        OptionBuilder.withLongOpt(newLongopt);
        return this;
    }

    public CliOptionsBuilder withType(Object newType)
    {
        OptionBuilder.withType(newType);
        return this;
    }

    public CliOptionsBuilder withValueSeparator()
    {
        OptionBuilder.withValueSeparator();
        return this;
    }

    public CliOptionsBuilder withValueSeparator(char sep)
    {
        OptionBuilder.withValueSeparator(sep);
        return this;
    }
}
