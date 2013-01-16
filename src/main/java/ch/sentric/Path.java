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

import java.util.Arrays;

/**
 * The {@link Path} representing a path delimited by '/'.
 */
public class Path {
    private static final String PATH_SEPARATOR = "/";
    private final String[] pathParts;
    private static PercentCodec percentCodec = new PercentCodec();

    /**
     * Constructor, initializing a path.
     * 
     * @param path
     *            the path as string
     */
    public Path(final String path) {
	this.pathParts = validate(path).split(PATH_SEPARATOR);
    }

    /**
     * Constructor, initializing a path.
     * 
     * @param pathParts
     *            the path parts as string array
     */
    public Path(final String[] pathParts) {
	this.pathParts = pathParts.clone();
    }

    /**
     * Returns a percent codec based encoded path.
     * 
     * @return endocoded path
     */
    public Path getReEncoded() {
	final String[] newPathParts = new String[getPathParts().length];
	for (int i = 0; i < getPathParts().length; i++) {
	    newPathParts[i] = percentCodec.encodePathPart(percentCodec.decode(getPathParts()[i]));
	}
	return new Path(newPathParts);
    }

    /**
     * Returns a new Path with . and .. parts removed.
     * 
     * TODO see bixo for implementation
     * src/main/java/bixo/urldb/SimpleUrlNormalizer.java
     * 
     * @return new path with . and .. parts removed
     */
    public Path removeRelativePathParts() {
	return new Path(getPathParts());
    }

    /**
     * Returns a new Path with a trailing default page like index.html removed.
     * 
     * TODO see bixo for implementation
     * src/main/java/bixo/urldb/SimpleUrlNormalizer.java
     * 
     * @return new Path with a trailing default page like index.html removed
     */
    public Path removeDefaultPage() {
	return new Path(getPathParts());
    }

    /**
     * Returns the path, delimited by '/'. Removes jsession and phpsessid from
     * path.
     * 
     * @return path
     */
    public String getAsString() {
	final StringBuilder builder = new StringBuilder();

	boolean isFirst = true;
	for (final String part : getPathParts()) {
	    if (isFirst) {
		isFirst = false;
	    } else {
		builder.append(PATH_SEPARATOR);
	    }
	    builder.append(part);
	}
	return builder.toString();
    }

    private String[] getPathParts() {
	return this.pathParts;
    }

    private String validate(final String path) {
	if (path.contains(";jsessionid") || path.contains(";JSESSIONID")) {
	    return path.substring(0, path.lastIndexOf(";"));
	}
	return path;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + Arrays.hashCode(pathParts);
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
	final Path other = (Path) obj;
	if (!Arrays.equals(pathParts, other.pathParts)) {
	    return false;
	}
	return true;
    }
}
