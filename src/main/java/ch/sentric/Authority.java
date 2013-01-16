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

/**
 * The {@link Authority}, represented by a hostname:port and optional prefixed
 * with username:password@hostname:port.
 */
public class Authority {
    private final HostName hostName;
    private final int port;
    private final String user;
    private final String password;

    /**
     * Constructor, initializing a authority.
     * 
     * @param hostName
     *            the host name
     * @param port
     *            the port
     * @param user
     *            the username
     * @param password
     *            the password
     */
    public Authority(final HostName hostName, final int port, final String user, final String password) {
	this.hostName = hostName;
	this.port = port;
	this.user = user;
	this.password = password;
    }

    /**
     * Constructor, initializing a authority.
     * 
     * @param hostName
     *            the host name
     * @param port
     *            the port
     * @param userInfo
     *            the user info, separeted by :, e.g user:password
     */
    public Authority(final HostName hostName, final int port, final String userInfo) {
	String user = null;
	String password = null;
	if (null != userInfo) {
	    final String[] userInfoParts = userInfo.split(":", 2);
	    if (userInfoParts.length == 2) {
		user = userInfoParts[0];
		password = userInfoParts[1];
	    }
	}
	this.hostName = hostName;
	this.port = port;
	this.user = user;
	this.password = password;
    }

    public HostName getHostName() {
	return this.hostName;
    }

    public int getPort() {
	return this.port;
    }

    public String getUser() {
	return this.user;
    }

    public String getPassword() {
	return this.password;
    }

    public String getAsString() {
	return (this.user != null || this.password != null ? this.user + ":" + this.password + "@" : "") + this.hostName.getAsString() + (this.port != -1 ? ":" + this.port : "");
    }

    public String getOptimizedForProximityOrder() {
	return this.hostName.getOptimizedForProximityOrder();
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((this.hostName == null) ? 0 : this.hostName.hashCode());
	result = prime * result + ((this.password == null) ? 0 : this.password.hashCode());
	result = prime * result + this.port;
	result = prime * result + ((this.user == null) ? 0 : this.user.hashCode());
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
	final Authority other = (Authority) obj;
	if (this.hostName == null) {
	    if (other.hostName != null) {
		return false;
	    }
	} else if (!this.hostName.equals(other.hostName)) {
	    return false;
	}
	if (this.password == null) {
	    if (other.password != null) {
		return false;
	    }
	} else if (!this.password.equals(other.password)) {
	    return false;
	}
	if (this.port != other.port) {
	    return false;
	}
	if (this.user == null) {
	    if (other.user != null) {
		return false;
	    }
	} else if (!this.user.equals(other.user)) {
	    return false;
	}
	return true;
    }
}
