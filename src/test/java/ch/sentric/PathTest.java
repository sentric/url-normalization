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

import org.junit.Assert;
import org.junit.Test;

/**
 * The {@link Path} test class.
 */
public class PathTest {
    @Test
    public void testGetAsString() {
	Assert.assertEquals("/hello/world", new Path("/hello/world").getAsString());
    }

    @Test
    public void getAsStringShouldRemoveJsession() {
	Assert.assertEquals("/1270777-1779518.html", new Path("/1270777-1779518.html;jsessionid=9ADD207E33B1E66CE6121BC73AADB986").getAsString());
    }

    @Test
    public void getAsStringShouldReturnSemicolonSplittedPath() {
	Assert.assertEquals("/wirtschaft/soziales/0;1518;769292;00.html", new Path("/wirtschaft/soziales/0;1518;769292;00.html").getAsString());
    }

    @Test
    public void getAsStringShouldNotRemovePhpsess() {
	// this is a query, not a path
	Assert.assertEquals("/1270777-1779518.html?phpsessid=9ADD207E33B1E66CE6121BC73AADB986",
		new Path("/1270777-1779518.html?phpsessid=9ADD207E33B1E66CE6121BC73AADB986").getAsString());
    }
}