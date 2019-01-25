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

import io.oddsource.java.licensing.exception.KeyNotFoundException;
import io.oddsource.java.licensing.licensor.encryption.PrivateKeyDataProvider;

/**
 * A sample implementation of the {@link PrivateKeyDataProvider} interface that embeds the public key in Java code.
 * The License Manager user interfaces can generate this code for you.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @see PrivateKeyDataProvider
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public final class SampleEmbeddedPrivateKeyDataProvider implements PrivateKeyDataProvider
{
    /**
     * Constructor.
     */
    public SampleEmbeddedPrivateKeyDataProvider()
    {

    }

    @Override
    public byte[] getEncryptedPrivateKeyData() throws KeyNotFoundException
    {
        return new byte[] {
            0x00000036, 0xFFFFFFAC, 0x00000001, 0x0000004F, 0xFFFFFFC8, 0x0000000D, 0x00000035, 0x00000038,
            0x00000070, 0xFFFFFFD0, 0x0000000B, 0xFFFFFF8A, 0x00000017, 0x00000031, 0x0000001D, 0xFFFFFFAB,
            0x0000001A, 0xFFFFFF94, 0x0000000A, 0xFFFFFFD6, 0x00000028, 0xFFFFFFF1, 0xFFFFFFB1, 0x00000023,
            0x0000005C, 0xFFFFFFE8, 0x0000002D, 0xFFFFFF80, 0xFFFFFFB0, 0xFFFFFFD5, 0x00000042, 0xFFFFFF89,
            0x0000000A, 0x00000067, 0x00000043, 0xFFFFFFB2, 0xFFFFFFFF, 0xFFFFFFE3, 0x00000040, 0x00000072,
            0x0000000D, 0xFFFFFFC6, 0x0000007F, 0x0000004D, 0x00000073, 0x00000054, 0x0000003E, 0x00000008,
            0x00000060, 0x00000023, 0xFFFFFFB6, 0x0000003F, 0xFFFFFFE4, 0xFFFFFFB4, 0xFFFFFFD7, 0xFFFFFFF4,
            0x00000023, 0x00000079, 0xFFFFFF8A, 0x00000047, 0x00000011, 0xFFFFFFB4, 0xFFFFFF8A, 0xFFFFFF9B,
            0x00000075, 0xFFFFFFC5, 0x00000067, 0x00000029, 0x0000007B, 0xFFFFFFCB, 0x0000002B, 0x00000014,
            0xFFFFFF89, 0x00000043, 0xFFFFFFEC, 0xFFFFFFC5, 0x00000005, 0xFFFFFFD5, 0xFFFFFFB1, 0xFFFFFFE9,
            0xFFFFFFCF, 0xFFFFFFFA, 0x0000005F, 0xFFFFFFE8, 0x00000004, 0xFFFFFFD2, 0x0000002A, 0xFFFFFFEB,
            0xFFFFFFD9, 0x00000053, 0x00000037, 0x00000046, 0xFFFFFF99, 0x00000008, 0xFFFFFFBA, 0x00000031,
            0x00000021, 0x00000003, 0xFFFFFFD2, 0x00000018, 0x00000064, 0xFFFFFFB4, 0x0000000E, 0xFFFFFF9A,
            0x0000004D, 0xFFFFFFA3, 0x00000064, 0x00000000, 0x00000033, 0x00000022, 0x0000007D, 0xFFFFFFEB,
            0xFFFFFFA1, 0xFFFFFFCC, 0x0000004E, 0xFFFFFF9E, 0x00000009, 0xFFFFFFAD, 0xFFFFFFC5, 0xFFFFFFC6,
            0x00000051, 0xFFFFFFD0, 0xFFFFFFF8, 0xFFFFFFBA, 0xFFFFFFED, 0xFFFFFF86, 0x0000004F, 0x00000017,
            0x00000015, 0x00000046, 0xFFFFFFFD, 0xFFFFFFF8, 0x0000006F, 0x00000036, 0x00000061, 0xFFFFFF93,
            0xFFFFFFAA, 0xFFFFFFD8, 0xFFFFFFF9, 0x00000019, 0x00000077, 0x00000020, 0xFFFFFF8C, 0x00000050,
            0x0000001C, 0xFFFFFFA5, 0x00000053, 0xFFFFFFA3, 0xFFFFFFF8, 0xFFFFFF81, 0x0000000E, 0x00000066,
            0xFFFFFFB9, 0xFFFFFFF1, 0xFFFFFFCD, 0xFFFFFF80, 0xFFFFFFC5, 0xFFFFFF97, 0x0000000A, 0x00000063,
            0x0000003F, 0x00000074, 0x0000004D, 0x00000028, 0x00000018, 0x0000004F, 0xFFFFFF90, 0x0000006D,
            0x00000015, 0x00000003, 0xFFFFFFD8, 0xFFFFFF81, 0x00000053, 0x00000041, 0xFFFFFFCE, 0x00000007,
            0x00000005, 0xFFFFFFCC, 0xFFFFFF93, 0x00000067, 0xFFFFFFD6, 0xFFFFFFD9, 0xFFFFFFB5, 0xFFFFFFC6,
            0xFFFFFFED, 0x00000016, 0x00000063, 0x00000061, 0x00000060, 0xFFFFFFD5, 0x0000002F, 0xFFFFFF8E,
            0xFFFFFFE3, 0xFFFFFFED, 0xFFFFFFF4, 0x0000005C, 0xFFFFFFC1, 0x00000003, 0x0000005B, 0xFFFFFF9C,
            0x00000074, 0x00000025, 0xFFFFFFE2, 0xFFFFFFF4, 0xFFFFFF86, 0x00000026, 0xFFFFFFA3, 0x00000015,
            0x0000001A, 0xFFFFFFA6, 0xFFFFFF94, 0xFFFFFFF5, 0x00000074, 0x0000005C, 0x00000076, 0x0000002A,
            0xFFFFFFAF, 0xFFFFFFA6, 0xFFFFFFF0, 0x00000025, 0x00000073, 0x0000005A, 0x00000043, 0x00000054,
            0xFFFFFFFA, 0x00000079, 0xFFFFFFCF, 0x0000007D, 0xFFFFFFC4, 0xFFFFFFFD, 0x0000004F, 0x00000070,
            0xFFFFFF8B, 0xFFFFFF92, 0xFFFFFFA2, 0xFFFFFFAA, 0x00000056, 0xFFFFFFE8, 0x00000054, 0x0000005E,
            0xFFFFFFAF, 0x00000026, 0x0000007B, 0xFFFFFFE9, 0xFFFFFFCF, 0x00000069, 0xFFFFFF8A, 0xFFFFFF95,
            0x00000002, 0xFFFFFF82, 0xFFFFFFAD, 0xFFFFFF9C, 0x0000002A, 0x00000079, 0x00000045, 0x00000072,
            0xFFFFFF91, 0x00000011, 0x00000029, 0xFFFFFFA3, 0x00000055, 0xFFFFFFD0, 0x00000039, 0x0000000A,
            0x00000027, 0xFFFFFF83, 0x00000003, 0xFFFFFFBA, 0xFFFFFF89, 0x00000033, 0x0000003A, 0x00000024,
            0xFFFFFFE1, 0x0000000C, 0xFFFFFFED, 0xFFFFFFB0, 0x00000032, 0xFFFFFFA3, 0xFFFFFFCB, 0x0000006B,
            0x00000056, 0x00000062, 0xFFFFFF91, 0x00000047, 0x0000006A, 0x00000011, 0x00000078, 0x00000037,
            0x00000045, 0xFFFFFFE4, 0x00000073, 0xFFFFFFE7, 0xFFFFFFD3, 0x00000054, 0xFFFFFFE3, 0x00000060,
            0x0000007D, 0xFFFFFFED, 0xFFFFFFFA, 0xFFFFFFBC, 0x0000006B, 0x00000003, 0xFFFFFFF0, 0xFFFFFFE1,
            0xFFFFFFF7, 0xFFFFFFE6, 0xFFFFFFCE, 0x0000007B, 0xFFFFFFFC, 0x0000000C, 0x00000027, 0xFFFFFFC0,
            0xFFFFFF8A, 0xFFFFFF82, 0xFFFFFFE9, 0x00000022, 0xFFFFFFF8, 0xFFFFFFDF, 0x00000037, 0xFFFFFF9C,
            0x00000064, 0x0000001B, 0x00000060, 0xFFFFFFBA, 0x00000021, 0xFFFFFFDD, 0xFFFFFFA6, 0x00000078,
            0x00000003, 0x00000000, 0x00000067, 0x00000043, 0x0000002A, 0xFFFFFF81, 0xFFFFFF80, 0xFFFFFFA9,
            0x00000034, 0xFFFFFFEC, 0xFFFFFF9C, 0xFFFFFFCB, 0x00000057, 0xFFFFFFE8, 0xFFFFFFDB, 0x00000021,
            0xFFFFFF9A, 0xFFFFFFE3, 0x00000076, 0xFFFFFFFA, 0xFFFFFFDF, 0xFFFFFFAB, 0x00000018, 0x00000065,
            0xFFFFFFA2, 0x0000006C, 0x00000026, 0xFFFFFF81, 0x00000065, 0x00000065, 0x0000002A, 0x00000064,
            0x00000040, 0xFFFFFFFD, 0xFFFFFF88, 0x00000076, 0xFFFFFFE3, 0xFFFFFFB2, 0x00000073, 0x00000036,
            0xFFFFFF8D, 0xFFFFFFBE, 0xFFFFFFDE, 0x0000002F, 0x0000007A, 0x00000011, 0x00000003, 0x00000015,
            0x00000052, 0x0000002E, 0x0000006C, 0xFFFFFFFB, 0x00000058, 0xFFFFFFCB, 0xFFFFFFDB, 0x0000005F,
            0x00000035, 0xFFFFFFE4, 0xFFFFFFE1, 0xFFFFFFFD, 0x0000000C, 0xFFFFFF90, 0xFFFFFF81, 0x0000005E,
            0x0000002E, 0xFFFFFFEB, 0xFFFFFFF2, 0x00000054, 0x0000001B, 0xFFFFFFF0, 0xFFFFFFA3, 0x0000006E,
            0x00000012, 0xFFFFFFA9, 0x0000002A, 0x00000075, 0xFFFFFFF7, 0x00000065, 0xFFFFFF9B, 0xFFFFFF95,
            0xFFFFFFF4, 0xFFFFFF8B, 0x0000007C, 0x00000052, 0xFFFFFFA7, 0x00000070, 0xFFFFFFC5, 0x0000000A,
            0xFFFFFFAD, 0x00000061, 0x00000072, 0x00000002, 0xFFFFFFBA, 0xFFFFFF98, 0x00000000, 0x00000023,
            0x0000005C, 0xFFFFFF91, 0xFFFFFF9A, 0xFFFFFF8A, 0xFFFFFFD0, 0x00000017, 0x00000026, 0x00000030,
            0x00000014, 0xFFFFFF94, 0x0000004C, 0x0000004B, 0xFFFFFFA7, 0x0000007B, 0x0000001C, 0xFFFFFF81,
            0x0000007C, 0x00000035, 0x00000025, 0xFFFFFFF1, 0x00000032, 0x0000005B, 0x0000005A, 0x0000006C,
            0xFFFFFFE3, 0x0000001B, 0xFFFFFFDD, 0xFFFFFFA7, 0x00000000, 0x0000006F, 0x00000043, 0xFFFFFFC9,
            0xFFFFFFD0, 0xFFFFFF81, 0x0000007E, 0xFFFFFF82, 0x0000004A, 0xFFFFFFE3, 0xFFFFFF96, 0x0000001B,
            0x0000005C, 0x00000009, 0x00000055, 0xFFFFFFB5, 0xFFFFFFEF, 0x00000050, 0xFFFFFFCD, 0xFFFFFFA7,
            0x0000007E, 0x00000000, 0x00000037, 0xFFFFFF96, 0x00000042, 0x0000007B, 0xFFFFFFC5, 0x00000079,
            0x00000023, 0xFFFFFFDA, 0x0000007C, 0x0000001D, 0xFFFFFFE6, 0x00000014, 0x00000057, 0xFFFFFF9A,
            0xFFFFFFD3, 0xFFFFFFF1, 0xFFFFFFF3, 0x00000038, 0xFFFFFFC6, 0xFFFFFF8A, 0xFFFFFF9B, 0x00000059,
            0xFFFFFFC3, 0x0000006B, 0xFFFFFFC2, 0xFFFFFFD2, 0xFFFFFFE9, 0xFFFFFFC9, 0xFFFFFFC1, 0x00000048,
            0x0000003B, 0x0000004E, 0xFFFFFFE1, 0xFFFFFFA4, 0x00000068, 0xFFFFFF9D, 0xFFFFFFD5, 0x00000073,
            0xFFFFFF9F, 0xFFFFFFD2, 0x00000076, 0x00000010, 0xFFFFFFBF, 0xFFFFFFD8, 0x00000073, 0xFFFFFFC8,
            0x00000000, 0x00000054, 0xFFFFFFD1, 0x00000033, 0x00000045, 0xFFFFFF8A, 0x00000075, 0x0000006B,
            0x00000029, 0xFFFFFFB5, 0x00000025, 0xFFFFFFBA, 0x0000001D, 0x00000046, 0xFFFFFFE1, 0x0000006F,
            0xFFFFFFCD, 0xFFFFFFD0, 0xFFFFFFF2, 0xFFFFFFE3, 0x00000043, 0x00000022, 0x00000071, 0xFFFFFFD6,
            0xFFFFFFDE, 0x0000007F, 0xFFFFFFEF, 0x0000004F, 0xFFFFFFDD, 0xFFFFFFB5, 0xFFFFFFED, 0x0000002F,
            0x00000069, 0x0000006E, 0x0000005D, 0x00000015, 0x0000001C, 0xFFFFFFEB, 0xFFFFFFCC, 0xFFFFFFBE,
            0xFFFFFFFF, 0xFFFFFF8C, 0x0000005B, 0xFFFFFF86, 0x00000033, 0x00000060, 0x0000001E, 0xFFFFFF8A,
            0xFFFFFFBA, 0x00000072, 0x00000003, 0xFFFFFFCD, 0xFFFFFF8B, 0xFFFFFFC7, 0xFFFFFFF4, 0x00000077,
            0x00000076, 0xFFFFFF9D, 0xFFFFFF9B, 0xFFFFFFB9, 0xFFFFFF8A, 0xFFFFFF8E, 0x00000002, 0x00000062,
            0xFFFFFFF5, 0xFFFFFFBD, 0xFFFFFFDA, 0xFFFFFFF3, 0xFFFFFFF7, 0x00000008, 0x0000000F, 0xFFFFFFB6,
            0x00000067, 0x00000043, 0xFFFFFFA4, 0x0000007B, 0x00000047, 0xFFFFFF9A, 0xFFFFFFF8, 0x0000004C,
            0x0000002D, 0x0000005B, 0x00000026, 0xFFFFFFB7, 0xFFFFFFB0, 0x00000043, 0x0000000B, 0x00000021,
            0x00000046, 0xFFFFFFD5, 0x0000003C, 0x0000001D, 0x0000007B, 0xFFFFFFE4, 0x00000076, 0x0000007D,
            0x00000023, 0xFFFFFFC7, 0x00000021, 0xFFFFFF8D, 0xFFFFFFB3, 0x0000003F, 0x0000003A, 0x00000031,
            0xFFFFFFF4, 0x00000043, 0xFFFFFFF8, 0xFFFFFFD8, 0xFFFFFF83, 0x0000006C, 0xFFFFFF8F, 0x00000040,
            0x00000002, 0xFFFFFF8E, 0x0000002B, 0xFFFFFFBA, 0x00000041, 0xFFFFFFCD, 0x00000055, 0xFFFFFFDE,
            0x00000023, 0xFFFFFFE6, 0xFFFFFFC1, 0x00000004, 0x00000017, 0xFFFFFFDB, 0xFFFFFFF4, 0x00000020,
            0xFFFFFF82, 0x00000054, 0xFFFFFFB9, 0x00000070, 0x0000007F, 0xFFFFFFC5, 0xFFFFFF91, 0xFFFFFFAF,
            0x00000060, 0x0000004E, 0x00000072, 0xFFFFFFD9, 0xFFFFFFC1, 0xFFFFFFC4, 0xFFFFFFEE, 0x00000034,
            0x00000078, 0x0000002B, 0x0000006D, 0xFFFFFFC0, 0x00000046, 0x00000077, 0xFFFFFF9D, 0xFFFFFFB6,
            0x0000003A, 0x00000071, 0x0000007F, 0xFFFFFFFF, 0xFFFFFF9D, 0x0000000D, 0xFFFFFFAE, 0xFFFFFFE0,
            0xFFFFFF9D, 0xFFFFFFAA, 0xFFFFFFEE, 0xFFFFFFCA, 0xFFFFFF89, 0x00000042, 0x00000030, 0x00000005,
            0xFFFFFFC8, 0xFFFFFFAA, 0xFFFFFF99, 0x00000077, 0x00000011, 0x00000031, 0x0000007D, 0xFFFFFF9C,
            0x00000011, 0xFFFFFFC0, 0x00000072, 0x00000009, 0xFFFFFF88, 0x0000001C, 0x0000001F, 0xFFFFFFFA,
            0xFFFFFFFD, 0x00000011, 0x0000002D, 0xFFFFFFB8, 0xFFFFFFC8, 0x00000048, 0xFFFFFFE3, 0xFFFFFFB6,
            0x00000073, 0x00000078, 0xFFFFFFFF, 0xFFFFFFBD, 0xFFFFFF9E, 0xFFFFFFC6, 0x0000006C, 0x0000001F,
            0xFFFFFFD1, 0x0000001F, 0xFFFFFFC0, 0xFFFFFFC4, 0x0000004A, 0x00000049, 0xFFFFFF85, 0xFFFFFFC2,
            0xFFFFFFD7, 0x00000060, 0xFFFFFFEF, 0x0000007A, 0x00000044, 0x00000016, 0x0000001C, 0x00000023,
            0x0000000E, 0x00000061, 0x00000079, 0x0000007B, 0x00000044, 0xFFFFFFB6, 0xFFFFFFF6, 0xFFFFFFBF,
            0x0000001C, 0xFFFFFFBB, 0xFFFFFFD5, 0x00000001, 0x00000059, 0xFFFFFFDC, 0xFFFFFFF7, 0x00000028,
            0xFFFFFF81, 0x0000001E, 0xFFFFFFD1, 0x00000032, 0x0000006B, 0x00000036, 0x0000000A, 0xFFFFFFF4,
            0x00000034, 0xFFFFFFEF, 0x00000049, 0x00000029, 0x00000079, 0xFFFFFFBA, 0x00000031, 0xFFFFFFB6,
            0x0000007C, 0x0000004A, 0x00000032, 0xFFFFFFFB, 0xFFFFFFA8, 0x0000001F, 0x0000007D, 0x00000020,
            0x00000066, 0x0000000A, 0xFFFFFFB3, 0x00000044, 0x00000057, 0xFFFFFF90, 0xFFFFFF8F, 0x00000058,
            0xFFFFFF98, 0x00000033, 0xFFFFFFF0, 0xFFFFFFD0, 0x0000003E, 0xFFFFFFB2, 0x00000057, 0x00000041,
            0x00000013, 0xFFFFFFE3, 0x00000076, 0x00000077, 0x00000028, 0xFFFFFFDA, 0x0000006C, 0x00000023,
            0x00000058, 0xFFFFFFE4, 0x00000072, 0xFFFFFF81, 0xFFFFFFCA, 0xFFFFFFDE, 0xFFFFFFAE, 0xFFFFFFBB,
            0x0000000E, 0x00000073, 0xFFFFFFCB, 0xFFFFFFD9, 0xFFFFFFB1, 0x00000018, 0xFFFFFFA0, 0xFFFFFFFA,
            0x00000007, 0x00000051, 0xFFFFFFA2, 0x0000000B, 0xFFFFFFDA, 0x0000001A, 0x00000063, 0xFFFFFF8A,
            0x00000022, 0x00000004, 0x0000002F, 0x00000035, 0xFFFFFF9E, 0xFFFFFF9B, 0x00000057, 0xFFFFFFD2,
            0xFFFFFFE5, 0xFFFFFF83, 0xFFFFFFB2, 0xFFFFFFAB, 0x00000050, 0xFFFFFF89, 0x0000006C, 0x0000003D,
            0xFFFFFF94, 0xFFFFFFDC, 0x00000039, 0x0000007F, 0xFFFFFFF0, 0xFFFFFFE8, 0xFFFFFFA3, 0x00000011,
            0x00000072, 0x00000048, 0xFFFFFFEE, 0x00000040, 0xFFFFFFCB, 0x00000000, 0x00000019, 0xFFFFFFD0,
            0x0000005B, 0x00000016, 0x0000006A, 0x00000013, 0x00000032, 0xFFFFFFCA, 0x00000074, 0x0000000C,
            0x00000075, 0x00000049, 0xFFFFFFBF, 0x0000007E, 0xFFFFFFDF, 0x0000004C, 0x0000005F, 0xFFFFFF88,
            0x0000002D, 0xFFFFFF8B, 0xFFFFFFD2, 0xFFFFFFF0, 0xFFFFFF94, 0x0000004E, 0x00000030, 0x0000005B,
            0x0000002A, 0x0000004C, 0xFFFFFFB7, 0xFFFFFFE0, 0xFFFFFFD7, 0x00000016, 0xFFFFFF81, 0xFFFFFFDF,
            0x00000007, 0xFFFFFF96, 0x00000009, 0xFFFFFFC7, 0x00000044, 0xFFFFFFA0, 0xFFFFFFCA, 0x0000007E,
            0x00000071, 0x0000002D, 0x0000006B, 0x0000000F, 0xFFFFFFF5, 0xFFFFFFFE, 0xFFFFFFA3, 0x0000000D,
            0xFFFFFFD9, 0xFFFFFFBB, 0x00000032, 0x00000070, 0xFFFFFFA7, 0x00000061, 0x0000006F, 0x00000077,
            0xFFFFFFF1, 0xFFFFFFE9, 0x00000022, 0xFFFFFFCF, 0x00000070, 0xFFFFFF9F, 0xFFFFFFD0, 0x0000007F,
            0xFFFFFFFB, 0xFFFFFFCF, 0x00000046, 0x00000061, 0xFFFFFFCF, 0xFFFFFFDB, 0xFFFFFF99, 0xFFFFFFAD,
            0x0000000C, 0xFFFFFFCE, 0x00000047, 0xFFFFFFF3, 0xFFFFFFA7, 0xFFFFFFD7, 0xFFFFFFB2, 0x00000034,
            0x00000035, 0xFFFFFFA1, 0x00000040, 0xFFFFFFE1, 0xFFFFFFEB, 0xFFFFFFD6, 0xFFFFFFC3, 0xFFFFFF87,
            0xFFFFFFB9, 0x00000024, 0x0000000B, 0x00000061, 0x0000000D, 0x00000061, 0xFFFFFFBD, 0x00000053,
            0xFFFFFFE3, 0x0000007A, 0xFFFFFFFD, 0x00000042, 0xFFFFFF9E, 0xFFFFFFDA, 0xFFFFFFDE, 0x00000042,
            0xFFFFFFCA, 0xFFFFFF93, 0x00000029, 0xFFFFFFC6, 0xFFFFFF83, 0x00000074, 0xFFFFFFC8, 0x00000046,
            0x00000058, 0xFFFFFFFD, 0xFFFFFFD5, 0xFFFFFF9B, 0xFFFFFFB1, 0xFFFFFF81, 0x00000060, 0x00000033,
            0x00000002, 0x00000044, 0x0000000E, 0xFFFFFFCE, 0x00000065, 0x0000001E, 0x0000006E, 0x00000005,
            0xFFFFFFAC, 0x00000052, 0x00000018, 0x00000037, 0x0000005F, 0xFFFFFFA8, 0x00000054, 0x00000012,
            0x00000041, 0xFFFFFFF6, 0xFFFFFF96, 0xFFFFFFAA, 0x0000000F, 0xFFFFFFC7, 0x00000059, 0xFFFFFFB9,
            0xFFFFFF83, 0xFFFFFFC2, 0x00000010, 0x0000000A, 0x00000043, 0xFFFFFF81, 0x00000047, 0x00000074,
            0xFFFFFFE7, 0xFFFFFFFC, 0xFFFFFF84, 0x0000002D, 0xFFFFFFFF, 0xFFFFFFA9, 0xFFFFFFEE, 0xFFFFFFE8,
            0x0000003F, 0xFFFFFFE5, 0x00000070, 0xFFFFFFE0, 0xFFFFFFC2, 0xFFFFFFD2, 0xFFFFFFD5, 0x0000005D,
            0xFFFFFFFA, 0x0000000D, 0x00000033, 0xFFFFFFD2, 0xFFFFFF81, 0x0000004B, 0x00000016, 0x00000001,
            0x00000046, 0xFFFFFFB0, 0xFFFFFFE5, 0x0000002F, 0x0000002C, 0xFFFFFFCD, 0xFFFFFFA3, 0xFFFFFFF8,
            0x00000040, 0xFFFFFFD2, 0x00000028, 0x00000008, 0x00000079, 0x00000054, 0x00000077, 0xFFFFFFA7,
            0x0000001F, 0xFFFFFFB0, 0x0000000E, 0xFFFFFFAF, 0xFFFFFF80, 0x00000026, 0xFFFFFFF1, 0xFFFFFFD2,
            0xFFFFFFCD, 0x0000001A, 0x0000006B, 0x00000025, 0x00000008, 0x00000002, 0x0000004D, 0xFFFFFF92,
            0x00000076, 0x00000024, 0x00000069, 0x00000065, 0xFFFFFF90, 0x0000005F, 0x00000017, 0xFFFFFFFA,
            0x00000053, 0xFFFFFFCE, 0xFFFFFF9D, 0x00000045, 0xFFFFFFDF, 0xFFFFFFB9, 0xFFFFFF83, 0x00000077,
            0x0000005B, 0xFFFFFF9F, 0xFFFFFFD1, 0x00000003, 0x0000002A, 0xFFFFFF95, 0xFFFFFFC4, 0x00000051,
            0x00000024, 0xFFFFFFAA, 0xFFFFFFBD, 0xFFFFFFC3, 0xFFFFFFE2, 0xFFFFFF9C, 0x00000071, 0x00000051,
            0x00000061, 0x00000046, 0x00000078, 0xFFFFFF98, 0xFFFFFFA2, 0xFFFFFFA1, 0xFFFFFFD6, 0x00000033,
            0x00000032, 0x00000069, 0x00000001, 0x0000003F, 0x00000078, 0x0000006B, 0xFFFFFFCB, 0x00000006,
            0xFFFFFF8A, 0xFFFFFF9A, 0xFFFFFF9F, 0xFFFFFFBD, 0xFFFFFF9C, 0x0000002C, 0x00000036, 0xFFFFFFBE,
            0xFFFFFF8C, 0xFFFFFFB0, 0xFFFFFFC5, 0x00000000, 0x0000000B, 0x0000001E, 0xFFFFFF8F, 0xFFFFFFF1,
            0x00000057, 0x00000061, 0xFFFFFFCE, 0x0000005A, 0xFFFFFFED, 0xFFFFFFAD, 0x0000001E, 0xFFFFFFB0,
            0x00000073, 0xFFFFFFE7, 0xFFFFFFB3, 0x0000002C, 0x0000003E, 0xFFFFFFC1, 0xFFFFFFAE, 0xFFFFFFCF,
            0xFFFFFF8D, 0xFFFFFFEE, 0x00000069, 0x0000001E, 0xFFFFFF81, 0x0000001F, 0xFFFFFFCC, 0xFFFFFFE0,
            0x0000005F, 0x00000054, 0xFFFFFFCB, 0x00000038, 0x0000001C, 0x00000021, 0x00000008, 0x0000007D,
            0xFFFFFFF2, 0x0000002B, 0x0000002B, 0x00000065, 0x0000007E, 0xFFFFFFF5, 0x00000025, 0xFFFFFFA5,
            0xFFFFFFBF, 0x0000002F, 0x00000014, 0xFFFFFF81, 0x00000062, 0x0000001A, 0xFFFFFFFF, 0x00000022,
            0x0000002B, 0xFFFFFFF0, 0x0000003F, 0x0000007C, 0xFFFFFFCF, 0xFFFFFFF5, 0xFFFFFF92, 0x00000001,
            0x00000020, 0xFFFFFF8F, 0xFFFFFFE8, 0x0000000D, 0xFFFFFFD5, 0xFFFFFFC3, 0xFFFFFF95, 0x00000079,
            0x00000056, 0xFFFFFF8C, 0xFFFFFF8D, 0x0000003C, 0xFFFFFFA1, 0xFFFFFFEF, 0x00000039, 0x00000014,
            0x00000077, 0x00000011, 0xFFFFFF91, 0x0000002C, 0x00000055, 0x0000004A, 0x00000068, 0x00000028,
            0xFFFFFFD1, 0xFFFFFFEA, 0xFFFFFFD3, 0x00000008, 0x0000007A, 0x0000003D, 0x0000006F, 0x00000058,
            0x00000037, 0xFFFFFFC5, 0xFFFFFFBF, 0x00000048, 0xFFFFFFA3, 0x0000002C, 0xFFFFFFEA, 0x00000075,
            0x00000059, 0xFFFFFFFC, 0x00000046, 0x0000002D, 0x00000060, 0xFFFFFFA9, 0xFFFFFFC0, 0xFFFFFFB2,
        };
    }
}
