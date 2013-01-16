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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A ip v4 address implementation of {@link HostName}. <br />
 * RegEx from http://united-coders.com/nico-heid/regular-expressions-in-java.
 */
public class IPv4Address implements HostName {
    public static final long ILLEGAL_IPV4 = -1;
    private static final String BYTEREGEX = "((?:25[0-5])|(?:2[0-4][0-9])|(?:[01]?[0-9][0-9]?))";
    private static final String REGEXPATTERN = "^" + BYTEREGEX + "\\." + BYTEREGEX + "\\." + BYTEREGEX + "\\." + BYTEREGEX + "$";
    private static final long MAX_IPV4 = (long) (Math.pow(2, 32) - 1);
    private static final Pattern PATTERN = Pattern.compile(REGEXPATTERN);
    private long address;

    /**
     * Constructor, initializing the ip v4 address.
     * 
     * @param address
     *            the address to parse
     */
    public IPv4Address(final long address) {
	if (address < 0 || address > MAX_IPV4) {
	    throw new IllegalArgumentException(address + " is not in the range of a valid IPv4 address. 0 to " + String.valueOf(MAX_IPV4));
	}
	this.address = address;
    }

    @Override
    public String getAsString() {
	long remaining = this.address;
	final long[] parts = new long[4];
	long modBase = 256;
	long base = 1;
	for (int i = 3; i >= 0; --i) {
	    parts[i] = ((remaining % modBase) / base);
	    remaining -= parts[i];
	    base = modBase;
	    modBase *= 256l;
	}
	return parts[0] + "." + parts[1] + "." + parts[2] + "." + parts[3];
    }

    /**
     * Parse the given {@link String} to long.
     * 
     * @param parsable
     *            the string to parse
     * @return the long representation
     */
    public static long parseIPv4String(final String parsable) {
	long result = 0;
	final Matcher matcher = PATTERN.matcher(parsable);

	if (!matcher.find() || matcher.groupCount() != 4) {
	    result = -1;
	} else {
	    long base = 1;
	    for (int i = 4; i > 0; --i) {
		final String match = matcher.group(i);
		final int number = Integer.parseInt(match);
		result += number * base;
		base *= 256;
	    }
	}
	return result;
    }

    @Override
    public String getOptimizedForProximityOrder() {
	return getAsString();
    }

    public long getAddress() {
	return this.address;
    }

    public void setAddress(final long address) {
	this.address = address;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (this.address ^ (this.address >>> 32));
	return result;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final IPv4Address other = (IPv4Address) obj;
	if (this.address != other.address) {
	    return false;
	}
	return true;
    }

}