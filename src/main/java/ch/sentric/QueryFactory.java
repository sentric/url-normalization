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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * <p>
 * The QueryFactory parses the in order to assemble a list of key and value
 * objects.
 * </p>
 * Example: ?val=12&a=15&x=10 will result in 3 keys (val,a and x) and their
 * associated values (12, 15 and 10).
 */
public class QueryFactory {

    /**
     * URL query string filters to apply.
     * 
     * <ul>
     * <li>WebTrends (WT.): see
     * http://www.heureka.com/upload/AdministrationUsersGuide.pdf, Chapter 27
     * <li>
     * <li>Google Analytics (utm): see
     * http://support.google.com/analytics/bin/answer.py?hl=en&answer=1033863</li>
     * <li>Yahoo! (OV*, YS*):
     * http://help.yahoo.com/l/de/yahoo/ysm/mss/manage/16897.html</li>
     * </ul>
     */
    private static ArrayList<String> filters = new ArrayList<String>(Arrays.asList("utm", "WT.", "OVKEY", "YSMKEY", "OVRAW", "YSMRAW", "OVMTC", "YSMMTC", "OVADID", "YSMADID",
	    "OVADID", "YSMADID", "OVKWID", "YSMKWID", "OVCAMPGID", "YSMCAMPGID", "OVADGRPID", "YSMADGRPID"));

    public Query build(final String q) {
	if (null == q || "".equalsIgnoreCase(q)) {
	    return new Query();
	}
	final ArrayList<QueryKeyValuePair> list = new ArrayList<QueryKeyValuePair>(0);

	ParserState state = ParserState.START;
	final StringTokenizer tokenizer = new StringTokenizer(q, "=&", true);
	String key = null;
	while (tokenizer.hasMoreTokens()) {
	    final String token = tokenizer.nextToken();

	    switch (state) {
	    case DELIMITER:
		if (token.equals("&")) {
		    state = ParserState.KEY;
		}
		break;

	    case KEY:
		if (!token.equals("=") && !token.equals("&") && !token.equalsIgnoreCase("PHPSESSID") && !token.equalsIgnoreCase("JSESSIONID")) {
		    key = token;
		    state = ParserState.EQUAL;
		}
		break;

	    case EQUAL:
		if (token.equals("=")) {
		    state = ParserState.VALUE;
		} else if (token.equals("&")) {
		    list.add(new QueryKeyValuePair(key, null));
		    state = ParserState.KEY;
		}
		break;

	    case VALUE:
		if (!token.equals("=") && !token.equals("&")) {
		    if (token.contains(";jsessionid") || token.contains(";JSESSIONID")) {
			list.add(new QueryKeyValuePair(key, token.substring(0, token.lastIndexOf(";"))));
		    } else {
			list.add(new QueryKeyValuePair(key, token));
		    }
		    state = ParserState.DELIMITER;
		} else if (token.equals("&")) {
		    list.add(new QueryKeyValuePair(key, null));
		    state = ParserState.KEY;
		}
		break;

	    case START:
		if (!token.equalsIgnoreCase("PHPSESSID") && !token.equalsIgnoreCase("JSESSIONID")) {
		    key = token;
		    state = ParserState.EQUAL;
		}
		break;

	    default:
		break;
	    }
	}
	CollectionUtils.filter(list, new Predicate() {

	    @Override
	    public boolean evaluate(final Object object) {
		boolean allowedQueryParameter = true;
		final QueryKeyValuePair queryKeyValuePair = (QueryKeyValuePair) object;
		for (final String filter : filters) {
		    if (queryKeyValuePair.getKey().startsWith(filter)) {
			allowedQueryParameter = false;
		    }
		}
		return allowedQueryParameter;
	    }
	});

	return new Query(list, '&');
    }

    private enum ParserState {
	KEY, VALUE, DELIMITER, EQUAL, START
    }
}
