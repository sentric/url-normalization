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
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * A domain name based implementation of {@link HostName}. The domain name is
 * represented by first, second and n third level domains, e.g. www.google.com.
 */
public class DomainName implements HostName {
    private static final String DOMAIN_NAME_DELIMITER = ".";
    private final ArrayList<String> parts = new ArrayList<String>(3);

    /**
     * The constructor, initializing the domain.
     * 
     * @param domain
     *            as string
     */
    public DomainName(final String domain) {
	final StringTokenizer tokenizer = new StringTokenizer(domain, DOMAIN_NAME_DELIMITER);
	while (tokenizer.hasMoreTokens()) {
	    final String current = tokenizer.nextToken();
	    this.parts.add(current.toLowerCase(Locale.ENGLISH));
	}
    }

    @Override
    public String getAsString() {
	return concatenate(this.parts, DOMAIN_NAME_DELIMITER);
    }

    public String getAsReversedString() {
	return reverseConcatenate(this.parts, DOMAIN_NAME_DELIMITER);
    }

    private List<String> stripWWW(final List<String> list) {
	final ArrayList<String> result = new ArrayList<String>(list.size());

	boolean isFirst = true;
	for (final String item : list) {
	    if (isFirst && item.equalsIgnoreCase("www")) {
		continue;
	    }
	    isFirst = false;
	    result.add(item);
	}

	return result;
    }

    private String concatenate(final List<String> list, final String separator) {
	final StringBuilder builder = new StringBuilder();
	final ListIterator<String> it = list.listIterator();

	while (it.hasNext()) {
	    builder.append(it.next());

	    if (it.hasNext()) {
		builder.append(separator);
	    }
	}
	return builder.toString();
    }

    private String reverseConcatenate(final List<String> list, final String separator) {
	final StringBuilder builder = new StringBuilder();
	final ListIterator<String> it = list.listIterator(list.size());

	while (it.hasPrevious()) {
	    builder.append(it.previous());

	    if (it.hasPrevious()) {
		builder.append(separator);
	    }
	}
	return builder.toString();
    }

    @Override
    public String getOptimizedForProximityOrder() {
	return reverseConcatenate(stripWWW(this.parts), DOMAIN_NAME_DELIMITER);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((this.parts == null) ? 0 : this.parts.hashCode());
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
	final DomainName other = (DomainName) obj;
	if (this.parts == null) {
	    if (other.parts != null) {
		return false;
	    }
	} else if (!this.parts.equals(other.parts)) {
	    return false;
	}
	return true;
    }

}