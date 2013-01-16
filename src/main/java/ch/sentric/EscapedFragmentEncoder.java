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

import java.io.CharArrayWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.AccessController;
import java.util.BitSet;

import sun.security.action.GetPropertyAction;

/**
 * Modified Version of {@link java.net.URLEncoder} to match GoogleBots
 * specifications for AJAX Applications.
 * 
 * Ranges that are converted:
 * <ul>
 * <li>%00..20</li>
 * <li>%23</li>
 * <li>%25..26</li>
 * <li>%2B</li>
 * <li>%7F..FF</li>
 * </ul>
 * 
 * All Javadoc has been removed to ensure corectness. Please see JavaDoc of the
 * original {java.net.UrlEncoder} instead.
 * 
 * @see https
 *      ://code.google.com/intl/de-DE/web/ajaxcrawling/docs/specification.html
 */
public final class EscapedFragmentEncoder {

    static BitSet needEncoding;
    static final int caseDiff = ('a' - 'A');
    static String dfltEncName;

    static {

	needEncoding = new BitSet(256);
	int i;
	for (i = 0x00; i <= 0x20; i++) {
	    needEncoding.set(i);
	}
	needEncoding.set(0x23);
	needEncoding.set(0x25);
	needEncoding.set(0x26);
	needEncoding.set(0x2B);
	for (i = 0x7F; i <= 0xFF; i++) {
	    needEncoding.set(i);
	}

	dfltEncName = AccessController.doPrivileged(new GetPropertyAction("file.encoding"));
    }

    private EscapedFragmentEncoder() {
    }

    /**
     * Encodes the given string using the default encoding.
     * 
     * @param s
     *            the string being encoded
     * @return the encoded string or the input in case of an error
     */
    @Deprecated
    public static String encode(final String s) {

	String str = null;

	try {
	    str = encode(s, dfltEncName);
	} catch (final UnsupportedEncodingException e) {
	    // The system should always have the platform default
	    str = s;
	}

	return str;
    }

    /**
     * Returns the encoded string.
     * 
     * @param s
     *            the string to encode
     * @param enc
     *            the name of a supported {@code Charset}
     * @return the encoded string
     * @throws UnsupportedEncodingException
     *             if the given encoding is invalid
     */
    public static String encode(final String s, final String enc) throws UnsupportedEncodingException {

	boolean needToChange = false;
	final StringBuffer out = new StringBuffer(s.length());
	Charset charset;
	final CharArrayWriter charArrayWriter = new CharArrayWriter();

	if (enc == null) {
	    throw new NullPointerException("charsetName");
	}
	try {
	    charset = Charset.forName(enc);
	} catch (final IllegalCharsetNameException e) {
	    throw new UnsupportedEncodingException(enc);
	} catch (final UnsupportedCharsetException e) {
	    throw new UnsupportedEncodingException(enc);
	}

	for (int i = 0; i < s.length();) {
	    int c = s.charAt(i);
	    // System.out.println("Examining character: " + c);
	    if (!needEncoding.get(c)) {
		// System.out.println("Storing: " + c);
		out.append((char) c);
		i++;
	    } else {
		// convert to external encoding before hex conversion
		do {
		    charArrayWriter.write(c);
		    /*
		     * If this character represents the start of a Unicode
		     * surrogate pair, then pass in two characters. It's not
		     * clear what should be done if a bytes reserved in the
		     * surrogate pairs range occurs outside of a legal surrogate
		     * pair. For now, just treat it as if it were any other
		     * character.
		     */
		    if (c >= 0xD800 && c <= 0xDBFF) {
			/*
			 * System.out.println(Integer.toHexString(c) +
			 * " is high surrogate");
			 */
			if ((i + 1) < s.length()) {
			    final int d = s.charAt(i + 1);
			    /*
			     * System.out.println("\tExamining " +
			     * Integer.toHexString(d));
			     */
			    if (d >= 0xDC00 && d <= 0xDFFF) {
				/*
				 * System.out.println("\t" +
				 * Integer.toHexString(d) +
				 * " is low surrogate");
				 */
				charArrayWriter.write(d);
				i++;
			    }
			}
		    }
		    i++;
		} while (i < s.length() && needEncoding.get((c = s.charAt(i))));

		charArrayWriter.flush();
		final String str = new String(charArrayWriter.toCharArray());
		final byte[] ba = str.getBytes(charset);
		for (final byte element : ba) {
		    out.append('%');
		    char ch = Character.forDigit((element >> 4) & 0xF, 16);
		    // converting to use uppercase letter as part of
		    // the hex value if ch is a letter.
		    if (Character.isLetter(ch)) {
			ch -= caseDiff;
		    }
		    out.append(ch);
		    ch = Character.forDigit(element & 0xF, 16);
		    if (Character.isLetter(ch)) {
			ch -= caseDiff;
		    }
		    out.append(ch);
		}
		charArrayWriter.reset();
		needToChange = true;
	    }
	}

	return (needToChange ? out.toString() : s);
    }

}
