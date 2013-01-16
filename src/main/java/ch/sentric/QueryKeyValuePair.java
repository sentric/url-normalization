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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A {@link Comparable} key value pair, representing a query.
 */
class QueryKeyValuePair implements Comparable<QueryKeyValuePair> {
    private final String key;
    private final String value;

    /**
     * The constructor, initializing the object.
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     */
    public QueryKeyValuePair(final String key, final String value) {
	this.key = key;
	if (null == value) {
	    this.value = "";
	} else {
	    this.value = value;
	}
    }

    @Override
    public int compareTo(final QueryKeyValuePair other) {
	final int keyComparission = getKey().compareTo(other.getKey());
	if (keyComparission != 0) {
	    return keyComparission;
	}
	return getValue().compareTo(other.getValue());
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
		append(getKey()).append(getValue()).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
	if (obj == null) {
	    return false;
	}
	if (obj == this) {
	    return true;
	}
	if (obj.getClass() != getClass()) {
	    return false;
	}

	final QueryKeyValuePair rhs = (QueryKeyValuePair) obj;
	return new EqualsBuilder().append(getKey(), rhs.getKey()).append(getValue(), rhs.getValue()).isEquals();
    }

    public String getKey() {
	return key;
    }

    public String getValue() {
	return value;
    }
}