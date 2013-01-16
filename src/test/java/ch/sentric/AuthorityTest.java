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
 * The {@link Authority} test class.
 */
public class AuthorityTest {

    @Test
    public void equalsShouldReturnTrue() {
	final Authority auth1 = new Authority(new DomainName("gamespot.com"), -1, null);
	final Authority auth2 = new Authority(new DomainName("gamespot.com"), -1, null);
	Assert.assertTrue("equals should be true", auth1.equals(auth2));
    }

    @Test
    public void equalsShouldReturnFalse() {
	final Authority auth1 = new Authority(new DomainName("gamespot.com"), -1, null);
	final Authority auth2 = new Authority(new DomainName("gamespot2.com"), -1, null);
	Assert.assertFalse("equals should be false", auth1.equals(auth2));
    }
}
