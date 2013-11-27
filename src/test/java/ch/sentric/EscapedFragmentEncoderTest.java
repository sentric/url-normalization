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

import junit.framework.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * The {@link EscapedFragmentEncoder} test class.
 */
public class EscapedFragmentEncoderTest {

    @Test
    public void encodeShouldNotChangeEquals() throws UnsupportedEncodingException {
        assertEncoding("=", "=");
    }

    @Test
    public void encodeShouldChangeAmp() throws UnsupportedEncodingException {
        assertEncoding("&", "%26");
    }

    @Test
    public void encodeShouldChangeSpace() throws UnsupportedEncodingException {
        assertEncoding(" ", "%20");
    }

    @Test
    public void encodeShouldChangeHash() throws UnsupportedEncodingException {
        assertEncoding("#", "%23");
    }

    @Test
    public void encodeShouldChangePercent() throws UnsupportedEncodingException {
        assertEncoding("%", "%25");
    }

    @Test
    public void encodeShouldNotChangeExplanation() throws UnsupportedEncodingException {
        assertEncoding("!", "!");
    }

    @Test
    public void encodeShouldChangeFullString() throws UnsupportedEncodingException {
        assertEncoding("key1=value1&key2=value2", "key1=value1%26key2=value2");
    }

    private void assertEncoding(final String from, final String to) throws UnsupportedEncodingException {
        Assert.assertEquals(to, EscapedFragmentEncoder.encode(from, Charset.defaultCharset().toString()));
    }
}
