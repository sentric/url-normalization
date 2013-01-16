/**
 * Copyright 2013 The Apache Software Foundation
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
package ch.sentric;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * The {@link IPv4Address} test class.
 */
public class IPv4AddressTest {

    @Test
    public void testParseRegularIPv4AddressWorks() {
	final ArrayList<int[]> cases = new ArrayList<int[]>();
	cases.add(new int[] { 10, 11, 12, 13 });
	cases.add(new int[] { 254, 11, 12, 13 });
	runParseTests(cases);
    }

    @Test
    public void testParseOneDigitIPv4AddressWorks() {
	final ArrayList<int[]> cases = new ArrayList<int[]>();
	cases.add(new int[] { 1, 11, 12, 13 });
	cases.add(new int[] { 10, 1, 12, 13 });
	cases.add(new int[] { 10, 11, 1, 13 });
	cases.add(new int[] { 10, 11, 12, 1 });
	runParseTests(cases);
    }

    @Test
    public void testParseTooShortIPv4AddressFails() {
	runFailingParseTests(new String[] { "10.11.12", "10.11.12." });
    }

    @Test
    public void testParseOutOfRangeIPv4AddressFails() {
	runFailingParseTests(new String[] { "256.11.12.10", "10.256.12.13", "10.11.256.13", "10.11.12.256", "356.11.12.10", "10.356.12.13", "10.11.356.13", "10.11.12.356" });
    }

    @Test
    public void testParseTooLongIPv4AddressFails() {
	runFailingParseTests(new String[] { "10.11.12.1.2", "10.11.12.1." });
    }

    @Test
    public void testParseNonTrimmedIPv4AddressFails() {
	runFailingParseTests(new String[] { "10.11.12.13 ", " 10.11.12.13 ", " 10.11.12.13" });
    }

    @Test
    public void testBuildRegularIPv4AddressesWorks() {
	final ArrayList<int[]> cases = new ArrayList<int[]>();
	cases.add(new int[] { 1, 11, 12, 13 });
	cases.add(new int[] { 10, 1, 12, 13 });
	cases.add(new int[] { 10, 11, 1, 13 });
	cases.add(new int[] { 10, 11, 12, 1 });
	cases.add(new int[] { 89, 185, 254, 1 });
	cases.add(new int[] { 254, 185, 254, 1 });
	cases.add(new int[] { 255, 255, 255, 255 });
	cases.add(new int[] { 1, 1, 1, 1 });
	runBuildTests(cases);
    }

    @Test
    public void testInstantiateOutOfRangeIpFails() {
	try {
	    new IPv4Address(-1);
	    fail("IP address -1 is invalid");
	} catch (final Exception e) {
	    // expected
	}
	try {
	    new IPv4Address((long) Math.pow(2, 32));
	    fail("IP address >=2^32 is invalid");
	} catch (final Exception e) {
	    // expected
	}
    }

    @Test
    public void equalsShouldReturnTrue() {
	final IPv4Address ip1 = new IPv4Address(IPv4Address.parseIPv4String("12.12.12.12"));
	final IPv4Address ip2 = new IPv4Address(IPv4Address.parseIPv4String("12.12.12.12"));
	Assert.assertTrue("equals should be true", ip1.equals(ip2));
    }

    @Test
    public void equalsShouldReturnFalse() {
	final IPv4Address ip1 = new IPv4Address(IPv4Address.parseIPv4String("12.12.12.12"));
	final IPv4Address ip2 = new IPv4Address(IPv4Address.parseIPv4String("12.12.12.13"));
	Assert.assertFalse("equals should be false", ip1.equals(ip2));
    }

    private void runBuildTests(final ArrayList<int[]> cases) {
	for (final int[] c : cases) {
	    try {
		final IPv4Address ip = new IPv4Address(calculateLong(c));
		assertEquals(buildIPv4(c), ip.getAsString());
	    } catch (final Exception e) {
		fail(buildIPv4(c) + " " + e);
	    }
	}
    }

    private long calculateLong(final int[] numbers) {
	int base = 1;
	long result = 0;
	for (int i = 3; i >= 0; --i) {
	    result += (long) numbers[i] * (long) base;
	    base *= 256;
	}
	return result;
    }

    private String buildIPv4(final int[] numbers) {
	return numbers[0] + "." + numbers[1] + "." + numbers[2] + "." + numbers[3];
    }

    private void runParseTests(final List<int[]> cases) {
	for (final int[] c : cases) {
	    assertEquals(c.toString(), calculateLong(c), IPv4Address.parseIPv4String(buildIPv4(c)));
	}
    }

    private void runFailingParseTests(final String[] cases) {
	for (final String c : cases) {
	    final long parseResult = IPv4Address.parseIPv4String(c);
	    assertEquals("String: <" + c + "> did not fail, gave " + parseResult, IPv4Address.ILLEGAL_IPV4, parseResult);
	}
    }
}
