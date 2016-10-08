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

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * The url class.
 */
public class URL {
    private final String givenInputUrl;

    private String scheme;
    private Authority authority;
    private Query query;
    private Path path;
    private String fragment;

    /**
     * The constructor, initializing a url from {@link String}.
     *
     * @param url
     *            as string
     * @throws MalformedURLException
     *             when url could not be parsed
     */
    public URL(final String url) throws MalformedURLException {
	this.givenInputUrl = url;
	this.parse(url);
    }

    /**
     * The constructor, initializing a url from {@link URI}.
     *
     * @param uri
     *            as URI
     * @throws MalformedURLException
     *             when url could not be parsed
     */
    public URL(final URI uri) throws MalformedURLException {
	this.givenInputUrl = uri.toString();
	parse(uri.toString());
    }

    private void parse(final String url) throws MalformedURLException {
	final java.net.URL urlObj = new java.net.URL(url);

	this.scheme = urlObj.getProtocol();
	final HostName hostName = new HostNameFactory().build(urlObj.getHost());
	this.authority = new Authority(hostName, urlObj.getPort(), urlObj.getUserInfo());
	this.query = new QueryFactory().build(urlObj.getQuery());
	this.path = new Path(urlObj.getPath());
	this.fragment = urlObj.getRef();
    }

    /**
     * Returns a {@link URI} representation of this object or null when not
     * valid. All fragments will be removed from the original URL.
     *
     * @return the uri
     * @throws URISyntaxException
     *             when the uri couldn't be parsed
     */
    public URI getURI() throws URISyntaxException {
	URI uri = null;
	if (isNotBlank(getFragment())) {
	    uri = new URI(getUrlWithoutFragment());
	} else {
	    uri = new URI(getGivenInputUrl());
	}
	return uri;
    }

    public String getUrlWithoutFragment() {
	return getGivenInputUrl().substring(0, getGivenInputUrl().indexOf("#"));
    }

    public String getGivenInputUrl() {
	return this.givenInputUrl;
    }

    public String getScheme() {
	return this.scheme;
    }

    public Authority getAuthority() {
	return this.authority;
    }

    public Query getQuery() {
	return this.query;
    }

    public Path getPath() {
	return this.path;
    }

    public String getFragment() {
	return this.fragment;
    }

    /**
     * Replaces white spaces with '+' characters, removes jsession and phpsessid
     * parameters.
     *
     * @return a url without white spaces and jession or phpsessid
     */
    public String getRepairedUrl() {
	return this.scheme + "://" + this.authority.getAsString() + this.path.getReEncoded().getAsString() + this.query.getAsString(true, false)
		+ (this.fragment == null ? "" : "#" + this.fragment);
    }

    public String getNormalizedUrl() {
	return this.authority.getOptimizedForProximityOrder() + this.path.getReEncoded().getAsString() + this.query.getAsString(true, true);
    }

    /**
     * Resolve the ip address from the authority.
     *
     * @return ip address as string
     * @throws UnknownHostException
     *             when ip can not be resolved
     */
    public String resolveIp() throws UnknownHostException {
	final String ip = InetAddress.getByName(this.getAuthority().getAsString()).getHostAddress();
	if (ip.equals("127.0.0.1")) {
	    throw new UnknownHostException("IP" + ip + "not valid");
	}
	return ip;
    }

    @Override
    public int hashCode() {
      return Objects.hash(authority, fragment, givenInputUrl, path, query, scheme);
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
	final URL other = (URL) obj;

  return Objects.equals(authority, other.authority) &&
    Objects.equals(fragment, other.fragment) &&
    Objects.equals(givenInputUrl, other.givenInputUrl) &&
    Objects.equals(path, other.path) &&
    Objects.equals(scheme, other.scheme);
    }


    @Override
    public String toString() {
	return this.givenInputUrl;
    }

    private boolean isNotBlank(String s) {
      if (s == null || s.length() == 0) {
        return false;
      }

      int len = s.length();
      for (int i = 0; i < len; i++) {
        if (!Character.isWhitespace(s.charAt(i))) {
          return true;
        }
      }

      return false;
    }
}
