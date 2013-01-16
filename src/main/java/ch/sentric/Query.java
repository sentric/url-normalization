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
import java.util.Collections;
import java.util.List;

/**
 * The {@link Query} representing a query path.
 */
public class Query {
    private static final char STANDARD_DELIMITER = '&';
    private List<QueryKeyValuePair> list;
    private final char delimiter;

    /**
     * The constructor, initializing a query.
     * 
     * @param list
     *            list of {@lin k QueryKeyValuePair}s
     * @param delimiter
     *            the delimiter
     */
    public Query(final List<QueryKeyValuePair> list, final char delimiter) {
	if (null == list) {
	    this.list = new ArrayList<QueryKeyValuePair>(0);
	} else {
	    this.list = list;
	}

	this.delimiter = delimiter;
    }

    /**
     * Empty constructor, initializing the query with standard delimiter.
     */
    public Query() {
	this(null, STANDARD_DELIMITER);
    }

    public String getAsString() {
	return getAsString(false, false);
    }

    /**
     * Returns the query as {@link String}. When prefixQuestionMark is true, the
     * query starts with '?'. When sort is true the query is sorted.
     * 
     * @param prefixQuestionMark
     *            true when query should start with '?'
     * @param sort
     *            true when sorting is requested
     * @return the query as {@link String}
     */
    public String getAsString(final boolean prefixQuestionMark, final boolean sort) {
	if (this.list.size() == 0) {
	    return "";
	}
	List<QueryKeyValuePair> list;
	if (sort) {
	    list = new ArrayList<QueryKeyValuePair>(this.list);
	    Collections.sort(list);
	} else {
	    list = this.list;
	}

	return (prefixQuestionMark ? "?" : "") + listToString(list);
    }

    /**
     * Returns a sorted query as {@link String} without a leading '?'.
     * 
     * @return sorted query
     */
    public String getAsSortedString() {
	return getAsString(false, true);
    }

    private String listToString(final List<QueryKeyValuePair> list) {
	final StringBuilder builder = new StringBuilder();
	boolean isFirst = true;
	for (final QueryKeyValuePair pair : list) {
	    if (!isFirst) {
		builder.append(delimiter);
	    }
	    builder.append(pair.getKey());
	    if (null != pair.getValue()) {
		builder.append("=").append(pair.getValue());
	    }
	    isFirst = false;
	}
	return builder.toString();
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + delimiter;
	result = prime * result + ((list == null) ? 0 : list.hashCode());
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
	final Query other = (Query) obj;
	if (delimiter != other.delimiter) {
	    return false;
	}
	if (list == null) {
	    if (other.list != null) {
		return false;
	    }
	} else if (!list.equals(other.list)) {
	    return false;
	}
	return true;
    }

}