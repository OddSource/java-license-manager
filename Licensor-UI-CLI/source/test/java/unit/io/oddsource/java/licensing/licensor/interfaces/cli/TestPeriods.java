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
package io.oddsource.java.licensing.licensor.interfaces.cli;

import static org.junit.Assert.*;

import java.io.PrintStream;
import java.util.Arrays;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Periods.
 */
@SuppressWarnings("EmptyMethod")
public class TestPeriods
{
    private Periods periods;

    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp()
    {
        this.outputStream = new ByteArrayOutputStream();

        PrintStream printStream = new PrintStream(outputStream);

        this.periods = new Periods(25, printStream);
    }

    @After
    public void tearDown()
    {

    }

    @Test
    public void testRun01() throws InterruptedException
    {
        new Thread(this.periods).start();

        Thread.sleep(100);

        this.periods.stop();

        byte[] bytes = this.outputStream.toByteArray();
        assertTrue("The length is not correct.", bytes.length >= 3);
        assertArrayEquals("The arrays are not correct.", Arrays.copyOf(bytes, 3), new byte[] {'.', '.', '.'});
    }

    @Test
    public void testRun02() throws InterruptedException
    {
        Thread thread = new Thread(this.periods);
        thread.start();

        Thread.sleep(200);

        thread.interrupt();

        byte[] bytes = this.outputStream.toByteArray();
        assertTrue("The length is not correct.", bytes.length >= 6);
        assertArrayEquals(
            "The arrays are not correct.",
            Arrays.copyOf(bytes, 6),
            new byte[] {'.', '.', '.', '.', '.', '.'}
        );
    }

    @Test
    public void testRun03() throws InterruptedException
    {
        PrintStream printStream = new PrintStream(outputStream);
        this.periods = new Periods(50, printStream);

        new Thread(this.periods).start();

        Thread.sleep(400);

        this.periods.stop();

        byte[] bytes = this.outputStream.toByteArray();
        assertTrue("The length is not correct.", bytes.length >= 6);
        assertArrayEquals(
            "The arrays are not correct.",
            Arrays.copyOf(bytes, 6),
            new byte[] {'.', '.', '.', '.', '.', '.'}
        );
    }
}
