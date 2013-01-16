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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

/**
 * <p>
 * Percent-encoding, also known as URL encoding, is a mechanism for encoding
 * information in a Uniform Resource Identifier (URI) under certain
 * circumstances. Although it is known as URL encoding it is, in fact, used more
 * generally within the main Uniform Resource Identifier (URI) set, which
 * includes both Uniform Resource Locator (URL) and Uniform Resource Name (URN).
 * As such it is also used in the preparation of data of the
 * "application/x-www-form-urlencoded" media type, as is often used in the
 * submission of HTML form data in HTTP requests.<br />
 * See http://en.wikipedia.org/wiki/Percent-encoding.
 * </p>
 * Some of this code has been copied from the bixo project
 * src/main/java/bixo/urldb/SimpleUrlNormalizer.java.
 */
public class PercentCodec {
    // !...*...'...(...)...;...:...@...&...=...+...$...,.../...?...%...#...[
    // %21 %2A %27 %28 %29 %3B %3A %40 %26 %3D %2B %24 %2C %2F %3F %25 %23 %5B
    // ]
    // %5D

    // You only need to encode "reserved purpose" characters, and that sub-set
    // of the reserved chars varies depending upon the protocol and the
    // component. Since we only are really worried about normalizing http(s)
    // URLs Not really sure about ':' and '?' being reserved in queries private

    private static final String RESERVED_QUERY_CHARS = "%&;=:?#";
    private static final String RESERVED_PATH_CHARS = "%/?#";
    private static final String HEX_CODES = "0123456789abcdefABCDEF";

    public String encodePathPart(final String pathPart) {
	return encode(pathPart, RESERVED_PATH_CHARS);
    }

    public String encodeQueryComponent(final String queryComponent) {
	return encode(queryComponent, RESERVED_QUERY_CHARS);
    }

    public String encode(final String component, final String reservedChars) {
	final StringBuilder result = new StringBuilder();
	for (int i = 0; i < component.length();) {
	    final int codePoint = component.codePointAt(i);
	    if (codePoint == 0x0020) {
		result.append('+');
	    } else if (codePoint >= 0x007F) {
		result.append(encodeCodePoint(codePoint));
	    } else if ((codePoint < 0x0020) || (reservedChars.indexOf((char) codePoint) != -1)) {
		result.append(String.format("%%%02x", codePoint));
	    } else {
		result.append((char) codePoint);
	    }

	    i += Character.charCount(codePoint);
	}

	return result.toString();
    }

    public String decode(final String url) {
	// FUTURE - handle unsupported %uHHHH sequences for Unicode code points.
	// FUTURE - detect & handle incorrectly encoded URLs

	// First, try to catch unescaped '%' characters.
	final String result = escapeIsolatedPercentSigns(url);

	try {
	    return URLDecoder.decode(result, Charset.defaultCharset().toString());
	} catch (final UnsupportedEncodingException e) {
	    throw new IllegalStateException("Unexpected exception during URL decoding", e);
	}
    }

    private static String encodeCodePoint(final int codepoint) {
	try {
	    final int[] codepoints = { codepoint };
	    final byte[] bytes = new String(codepoints, 0, 1).getBytes(Charset.defaultCharset().toString());

	    final StringBuilder result = new StringBuilder();
	    for (final byte value : bytes) {
		result.append(String.format("%%%02x", value));
	    }

	    return result.toString();
	} catch (final UnsupportedEncodingException e) {
	    throw new IllegalStateException("Unexpected exception during URL encoding", e);
	}
    }

    private static String escapeIsolatedPercentSigns(final String in) {
	StringBuilder builder = null;
	int offset = 0;
	int lastOffset = 0;
	while ((offset = in.indexOf('%', offset)) != -1) {

	    offset += 1;

	    if (offset > in.length() - 2 || HEX_CODES.indexOf(in.charAt(offset)) == -1 || HEX_CODES.indexOf(in.charAt(offset + 1)) == -1) {
		if (null == builder) {
		    builder = new StringBuilder();
		}
		builder.append(in.substring(lastOffset, offset));
		builder.append("25");
		lastOffset = offset;
	    }
	}
	if (null != builder) {
	    builder.append(in.substring(lastOffset));
	    return builder.toString();
	}

	return in;
    }
}