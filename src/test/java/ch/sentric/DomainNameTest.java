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

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

/**
 * The {@link DomainName} test class.
 */
public class DomainNameTest {

    @Test
    public void getAsStringShouldReturnGivenDomainAsString() {
	Assert.assertEquals("www.koch.ro", new DomainName("www.koch.ro").getAsString());
	Assert.assertEquals("faz.net", new DomainName("faz.net").getAsString());
	Assert.assertEquals("äö.mydomain.de", new DomainName("äö.mydomain.de").getAsString());
    }

    @Test
    public void getAsStringShouldLowerCaseGivenDomain() {
	Assert.assertEquals("www.KOCH.rO".toLowerCase(Locale.ENGLISH), new DomainName("www.KOCH.rO").getAsString());
	Assert.assertEquals("faz.Net".toLowerCase(Locale.ENGLISH), new DomainName("faz.Net").getAsString());
	Assert.assertEquals("ÄÖ.mydomain.org".toLowerCase(Locale.ENGLISH), new DomainName("ÄÖ.mydomain.org").getAsString());
    }

    @Test
    public void testGetAsReversedString() {
	Assert.assertEquals("ro.koch.www", new DomainName("www.KOCH.rO").getAsReversedString());
	Assert.assertEquals("www.koch.ro", new DomainName("ro.koch.www").getAsReversedString());
	Assert.assertEquals("net.faz", new DomainName("faz.Net").getAsReversedString());
	Assert.assertEquals("faz.net", new DomainName("net.faz").getAsReversedString());
	Assert.assertEquals("org.mydomain.ao", new DomainName("ao.mydomain.org").getAsReversedString());
	Assert.assertEquals("ao.mydomain.org", new DomainName("org.mydomain.ao").getAsReversedString());
    }

    @Test
    public void testGetOptimizedForProximityOrder() {
	Assert.assertEquals("ro.koch", new DomainName("www.KOCH.rO").getOptimizedForProximityOrder());
	Assert.assertEquals("ro.koch", new DomainName("WWW.KOCH.rO").getOptimizedForProximityOrder());
	Assert.assertEquals("net.faz", new DomainName("faz.Net").getOptimizedForProximityOrder());
	Assert.assertEquals("net.faz.ww", new DomainName("ww.faz.Net").getOptimizedForProximityOrder());
	Assert.assertEquals("net.faz.wwww", new DomainName("wwww.faz.Net").getOptimizedForProximityOrder());
	Assert.assertEquals("obscure.www.net", new DomainName("net.www.obscure").getOptimizedForProximityOrder());
    }

    @Test
    public void equalsShouldReturnTrue() {
	final DomainName domain1 = new DomainName("gamespot.com");
	final DomainName domain2 = new DomainName("gamespot.com");
	Assert.assertTrue("equals should be true", domain1.equals(domain2));
    }

    @Test
    public void equalsShouldReturnFalse() {
	final DomainName domain1 = new DomainName("gamespot.com");
	final DomainName domain2 = new DomainName("gamespot2.com");
	Assert.assertFalse("equals should be false", domain1.equals(domain2));
    }
}
