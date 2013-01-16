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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.junit.Test;

/**
 * Test-Class for the {@link UrlUtil}.
 */
public class UrlUtilTest {

    @Test
    public void siteToTopLevelShouldReturnTopLevelDomain() {
	assertEquals("org.wikipedia", UrlUtil.siteToTopLevel("org.wikipedia.de.wwww"));
    }

    @Test
    public void siteToTopLevelShouldReturnSame() {
	assertEquals("wikipedia", UrlUtil.siteToTopLevel("wikipedia"));
    }

    @Test
    public void siteToTopLevelShouldReturnSameIp() {
	assertEquals("1.2.3.4", UrlUtil.siteToTopLevel("1.2.3.4"));
    }

    @Test
    public void siteToTopLevelShouldReturnRealDomain() {
	assertEquals("uk.co.bbc", UrlUtil.siteToTopLevel("uk.co.bbc.subdomain.www"));
    }

    @Test
    public void siteToTopLevelShouldReturnRealDomain2() {
	assertEquals("museum.trolley.newyork", UrlUtil.siteToTopLevel("museum.trolley.newyork.centralpark.www"));
    }

    @Test
    public void siteToTopLevelShouldReturnRealDomain3() {
	assertEquals("com.livefilestore.bay.sanfrancisco", UrlUtil.siteToTopLevel("com.livefilestore.bay.sanfrancisco.www"));
    }

    @Test
    public void isEscapedFragmentableShouldReturnTrue() throws MalformedURLException {
	assertTrue(UrlUtil.isEscapeFragmentableUrl(new URL("http://some-request-url/test1#!my-request")));
    }

    @Test
    public void isEscapedFragmentableShouldReturnFalse() throws MalformedURLException {
	assertFalse(UrlUtil.isEscapeFragmentableUrl(new URL("http://some-request-url/test1#someparamateer=somevalue")));
    }

    @Test
    public void isEscapedFragmentShouldReturnTrue() throws MalformedURLException {
	assertTrue(UrlUtil.isEscapeFragmentUrl(new URL("http://some-request-url/test1?_escaped_fragment_=my-request")));
    }

    @Test
    public void isEscapedFragmentShouldReturnFalse() throws MalformedURLException {
	assertFalse(UrlUtil.isEscapeFragmentUrl(new URL("http://some-request-url/test1?someparamateer=somevalue")));
    }

    @Test
    public void toEscapedFragmentUrlShouldReturnFragmentUrl() throws MalformedURLException, UnsupportedEncodingException {
	final URL url = new URL("http://some-request-url/test1#!my-request");
	final URL fragmentUrl = new URL("http://some-request-url/test1?_escaped_fragment_=my-request");
	assertEquals(fragmentUrl, UrlUtil.toEscapedFragmentUrl(url));
    }

    @Test
    public void fromEscapedFragmentUrlShouldReturnDefaultUrl() throws MalformedURLException, UnsupportedEncodingException {
	final URL url = new URL("http://some-request-url/test1#!my-request");
	final URL fragmentUrl = new URL("http://some-request-url/test1?_escaped_fragment_=my-request");
	assertEquals(url, UrlUtil.fromEscapedFragmentUrl(fragmentUrl));
    }

    @Test
    public void toEscapedFragmentUrlShouldReturnFragmentUrl2() throws MalformedURLException, UnsupportedEncodingException {
	final URL url = new URL("http://some-request-url/test1?test2=test2#!my-request");
	final URL fragmentUrl = new URL("http://some-request-url/test1?test2=test2&_escaped_fragment_=my-request");
	assertEquals(fragmentUrl, UrlUtil.toEscapedFragmentUrl(url));
    }

    @Test
    public void fromEscapedFragmentUrlShouldReturnDefaultUrl2() throws MalformedURLException, UnsupportedEncodingException {
	final URL url = new URL("http://some-request-url/test1?test2=test2#!my-request");
	final URL fragmentUrl = new URL("http://some-request-url/test1?test2=test2&_escaped_fragment_=my-request");
	assertEquals(url, UrlUtil.fromEscapedFragmentUrl(fragmentUrl));
    }

    @Test
    public void toEscapedFragmentUrlShouldReturnFragmentUrl3() throws MalformedURLException, UnsupportedEncodingException {
	final URL url = new URL("http://some-request-url/test1?test2=test2&test3=test3#!my-request");
	final URL fragmentUrl = new URL("http://some-request-url/test1?test2=test2&test3=test3&_escaped_fragment_=my-request");
	assertEquals(fragmentUrl, UrlUtil.toEscapedFragmentUrl(url));
    }

    @Test
    public void fromEscapedFragmentUrlShouldReturnDefaultUrl3() throws MalformedURLException, UnsupportedEncodingException {
	final URL url = new URL("http://some-request-url/test1?test2=test2&test3=test3#!my-request");
	final URL fragmentUrl = new URL("http://some-request-url/test1?test2=test2&test3=test3&_escaped_fragment_=my-request");
	assertEquals(url, UrlUtil.fromEscapedFragmentUrl(fragmentUrl));
    }

    @Test
    public void toEscapedFragmentUrlShouldReturnFragmentUrl4() throws MalformedURLException, UnsupportedEncodingException {
	final URL url = new URL("http://some-request-url/test1?#!my-request&key=value");
	final URL fragmentUrl = new URL("http://some-request-url/test1?&_escaped_fragment_=my-request%26key=value");
	assertEquals(fragmentUrl, UrlUtil.toEscapedFragmentUrl(url));
    }

    @Test
    public void fromEscapedFragmentUrlShouldReturnDefaultUrl4() throws MalformedURLException, UnsupportedEncodingException {
	final URL url = new URL("http://some-request-url/test1?#!my-request&key=value");
	final URL fragmentUrl = new URL("http://some-request-url/test1?&_escaped_fragment_=my-request%26key=value");
	assertEquals(url, UrlUtil.fromEscapedFragmentUrl(fragmentUrl));
    }

    @Test
    public void subdomainShouldReturnCorrectValue1() throws MalformedURLException {
	final URL url = new URL("http://news.google.co.uk?#!my-request&key=value");
	assertEquals("news", UrlUtil.subDomain(url));
    }

    @Test
    public void subdomainShouldReturnCorrectValue2() throws MalformedURLException {
	final URL url = new URL("http://news.germany.google.co.uk?#!my-request&key=value");
	assertEquals("news.germany", UrlUtil.subDomain(url));
    }

    @Test
    public void subdomainShouldReturnCorrectValue3() throws MalformedURLException {
	final URL url = new URL("http://slavigru.univie.ac.at/news");
	assertEquals("slavigru", UrlUtil.subDomain(url));
    }

    @Test
    public void subdomainShouldReturnCorrectValue4() throws MalformedURLException {
	final URL url = new URL("http://www.seminarraum.co.at/info");
	assertEquals("www", UrlUtil.subDomain(url));
    }

    @Test
    public void subdomainShouldReturnCorrectValue5() throws MalformedURLException {
	final URL url = new URL("http://buch.co.at/bar");
	assertEquals("www", UrlUtil.subDomain(url));
    }

    @Test
    public void subdomainShouldReturnCorrectValue6() throws MalformedURLException {
	final URL url = new URL("http://127.0.0.1/gugus");
	assertEquals("127.0.0.1", UrlUtil.subDomain(url));
    }

    @Test
    public void getParentSiteReturnParentSite() {
	assertEquals("org.wikipedia.de", UrlUtil.getParentSite("org.wikipedia.de.wwww"));
    }

    @Test
    public void getParentSiteReturnParentSite2() {
	assertEquals("org.wikipedia", UrlUtil.getParentSite("org.wikipedia.de"));
    }

    @Test
    public void getParentSiteReturnSameSite() {
	assertEquals("org.wikipedia", UrlUtil.getParentSite("org.wikipedia"));
    }

    @Test
    public void getParentSiteReturnSameSite2() {
	assertEquals("org", UrlUtil.getParentSite("org"));
    }

    @Test
    public void getParentSiteReturnSameSite3() {
	assertEquals("1235", UrlUtil.getParentSite("1235"));
    }

    @Test
    public void getParentSiteReturnSameSite4() {
	assertEquals("asdf", UrlUtil.getParentSite("asdf"));
    }

    @Test
    public void getParentSiteReturnSameSite5() {
	assertEquals("asdf.ok.uk.wikipedia", UrlUtil.getParentSite("asdf.ok.uk.wikipedia"));
    }

    @Test
    public void getParentSiteReturnSameSite6() {
	assertEquals("1.2.3.4", UrlUtil.getParentSite("1.2.3.4"));
    }
}
