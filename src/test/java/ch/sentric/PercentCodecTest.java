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

import org.junit.Assert;
import org.junit.Test;

/**
 * The {@link PercentCodec} test class.
 */
public class PercentCodecTest {

    private final PercentCodec codec = new PercentCodec();

    @Test
    public void testDecodeIgnoresIsolatedPercent() {
	Assert.assertEquals("a%b", codec.decode("a%b"));
	Assert.assertEquals("%", codec.decode("%"));
	Assert.assertEquals("%%", codec.decode("%%"));
	Assert.assertEquals("% %", codec.decode("% %"));
	Assert.assertEquals("%% ", codec.decode("%% "));
	Assert.assertEquals("hallo%welt%wie%gehts", codec.decode("hallo%welt%wie%gehts"));
	Assert.assertEquals(" % ", codec.decode("%20%%20"));
    }

    @Test
    public void testEncodeQueryComponent() {
	Assert.assertEquals("%25%26%3b%3d%3a%3f%23", codec.encodeQueryComponent("%&;=:?#"));
	Assert.assertEquals("h%25a%26l%3bl%3do%3a%3fw%23elt", codec.encodeQueryComponent("h%a&l;l=o:?w#elt"));
	Assert.assertEquals("", codec.encodeQueryComponent(""));
	Assert.assertEquals("/!'()@[],", codec.encodeQueryComponent("/!'()@[],"));
    }

    @Test
    public void testEncodeWhiteSpace() {
	Assert.assertEquals("+", codec.encodeQueryComponent(" "));
	Assert.assertEquals("+", codec.encodePathPart(" "));
	Assert.assertEquals("++", codec.encodeQueryComponent("  "));
	Assert.assertEquals("++", codec.encodePathPart("  "));
	Assert.assertEquals("a+a", codec.encodeQueryComponent("a a"));
	Assert.assertEquals("a+a", codec.encodePathPart("a a"));
    }
}