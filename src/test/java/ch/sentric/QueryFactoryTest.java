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

import org.junit.Test;

/**
 * The {@link QueryFactory} test class.
 */
public class QueryFactoryTest {
    @Test
    public void buildShouldWorkWithSimpleQueries() {
	assertRoundTrip("a=b");
	assertRoundTrip("a1=b1");
	assertRoundTrip("aa=bb");
	assertRoundTrip("alles=klar");
    }

    @Test
    public void buildShouldWorkWithMultipleParameterQueries() {
	assertRoundTrip("a=b&b=c");
	assertRoundTrip("a1=b1&b1=c1");
	assertRoundTrip("aa=bb&bb=aa");
	assertRoundTrip("alles=klar&auf=der&andrea=doria");
    }

    @Test
    public void buildSortQuery() {
	final Query query = new QueryFactory().build("b=a&a=b");
	assertEquals("a=b&b=a", query.getAsSortedString());
    }

    @Test
    public void buildShouldIgnoreJessionId() {
	final Query query = new QueryFactory().build("val=12&a=15&x=10;jsessionid=9ADD207E33B1E66CE6121BC73AADB986");
	assertEquals("val=12&a=15&x=10", query.getAsString());
    }

    @Test
    public void buildShouldIgnoreJessionIdWhenOrdering() {
	final Query query = new QueryFactory().build("val=12&a=15&x=10;jsessionid=9ADD207E33B1E66CE6121BC73AADB986");
	assertEquals("a=15&val=12&x=10", query.getAsSortedString());
    }

    @Test
    public void buildShouldReturnNothingWhenJessionIsTheOnlyParameter() {
	final Query query = new QueryFactory().build("jsessionid=9ADD207E33B1E66CE6121BC73AADB986");
	assertEquals("", query.getAsSortedString());
    }

    @Test
    public void buildShouldReturnNothingWhenPhpsessidIsTheOnlyParameter() {
	final Query query = new QueryFactory().build("phpsessid=9ADD207E33B1E66CE6121BC73AADB986");
	assertEquals("", query.getAsSortedString());
    }

    @Test
    public void buildShouldSortQueryWithEqualKeys() {
	final Query query = new QueryFactory().build("a=b&a=a");
	assertEquals("a=a&a=b", query.getAsSortedString());
    }

    @Test
    public void buildShouldRemoveGoogleUrlTrackingParameter() {
	final Query query = new QueryFactory().build("utm_campaign=Feed%3A+TheSouthwesternSunRss+%28The+Southwestern+Sun+RSS%29&utm_medium=feed&utm_source=feedburner");
	assertEquals("", query.getAsSortedString());
    }

    @Test
    public void buildShouldRemoveGoogleGifRequestTrackingParameter() {
	final Query query = new QueryFactory()
		.build("utmwv=4&utmn=769876874&utmhn=example.com&utmcs=ISO-8859-1&utmsr=1280x1024&utmsc=32-bit&utmul=en-us&utmje=1&utmfl=9.0%20%20r115&utmcn=1&utmdt=GATC012%20setting%20variables&utmhid=2059107202&utmr=0&utmp=/auto/GATC012.html?utm_source=www.gatc012.org&utm_campaign=campaign+gatc012&utm_term=keywords+gatc012&utm_content=content+gatc012&utm_medium=medium+gatc012&utmac=UA-30138-1&utmcc=__utma%3D97315849.1774621898.1207701397.1207701397.1207701397.1%3B...  ");
	assertEquals("", query.getAsSortedString());
    }

    @Test
    public void buildShouldRemoveWebtrendsRequestTrackingParameter() {
	Query query = new QueryFactory().build("WT.vtid=abcdefghij&a=b&WT.mc_id=EmailCampaign1&WT.si_n=name1;name2&WT.si_x=position1;position2.");
	assertEquals("a=b", query.getAsSortedString());
	// but not lowercase
	query = new QueryFactory().build("wt.vtid=abcdefghij&a=b");
	assertEquals("a=b&wt.vtid=abcdefghij", query.getAsSortedString());
    }

    @Test
    public void buildShouldRemoveYahooRequestTrackingParameter() {
	Query query = new QueryFactory().build("OVRAW=cheap%20television&OVKEY=television&OVMTC=advanced&OVKWID=4317717511&OVADID=7306185511");
	assertEquals("", query.getAsSortedString());
	query = new QueryFactory().build("YSMCAMPGID=123456&YSMADGRPID=654321");
	assertEquals("", query.getAsSortedString());
	// but not lowercase
	query = new QueryFactory().build("ysmcampgid=123456&YSMADGRPID=654321");
	assertEquals("ysmcampgid=123456", query.getAsSortedString());
    }

    private void assertRoundTrip(final String q) {
	final Query query = new QueryFactory().build(q);
	assertEquals(q, query.getAsString());
    }
}
