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
 * The {@link QueryKeyValuePair} test class.
 */
public class QueryKeyValuePairTest {
    static final class Fixture {
	static QueryKeyValuePair x = new QueryKeyValuePair("foo", "bar");
	static QueryKeyValuePair y = new QueryKeyValuePair("foo", "bar");
	static QueryKeyValuePair z = new QueryKeyValuePair("foo", "bar");
	static QueryKeyValuePair notx = new QueryKeyValuePair("bar", "foo");
    }

    @Test
    /**
     * A class is equal to itself.
     */
    public void testEqualToSelf() {
	Assert.assertTrue("Class equal to itself.", Fixture.x.equals(Fixture.x));
    }

    /**
     * x.equals(WrongType) must return false;
     * 
     */
    @Test
    public void testPassIncompatibleTypeIsFalse() {
	Assert.assertFalse("Passing incompatible object to equals should return false", Fixture.x.equals("string"));
    }

    /**
     * x.equals(null) must return false;
     * 
     */
    @Test
    public void testNullReferenceIsFalse() {
	Assert.assertFalse("Passing null to equals should return false", Fixture.x == null);
    }

    /**
     * 1. x, x.equals(x) must return true. 2. x and y, x.equals(y) must return
     * true if and only if y.equals(x) returns true.
     */
    @Test
    public void testEqualsIsReflexiveIsSymmetric() {
	Assert.assertTrue("Reflexive test fail x,y", Fixture.x.equals(Fixture.y));
	Assert.assertTrue("Symmetric test fail y", Fixture.y.equals(Fixture.x));
    }

    /**
     * 1. x.equals(y) returns true 2. y.equals(z) returns true 3. x.equals(z)
     * must return true
     */
    @Test
    public void testEqualsIsTransitive() {
	Assert.assertTrue("Transitive test fails x,y", Fixture.x.equals(Fixture.y));
	Assert.assertTrue("Transitive test fails y,z", Fixture.y.equals(Fixture.z));
	Assert.assertTrue("Transitive test fails x,z", Fixture.x.equals(Fixture.z));
    }

    /**
     * Repeated calls to equals consistently return true or false.
     */
    @Test
    public void testEqualsIsConsistent() {
	Assert.assertTrue("Consistent test fail x,y", Fixture.x.equals(Fixture.y));
	Assert.assertTrue("Consistent test fail x,y", Fixture.x.equals(Fixture.y));
	Assert.assertTrue("Consistent test fail x,y", Fixture.x.equals(Fixture.y));
	Assert.assertFalse(Fixture.notx.equals(Fixture.x));
	Assert.assertFalse(Fixture.notx.equals(Fixture.x));
	Assert.assertFalse(Fixture.notx.equals(Fixture.x));
    }

    /**
     * Repeated calls to hashcode should consistently return the same integer.
     */
    @Test
    public void testHashcodeIsConsistent() {
	final int initial_hashcode = Fixture.x.hashCode();
	Assert.assertEquals("Consistent hashcode test fails", initial_hashcode, Fixture.x.hashCode());
	Assert.assertEquals("Consistent hashcode test fails", initial_hashcode, Fixture.x.hashCode());
    }

    /**
     * Objects that are equal using the equals method should return the same
     * integer.
     */
    @Test
    public void testHashcodeTwoEqualsObjectsProduceSameNumber() {
	final int xhashcode = Fixture.x.hashCode();
	final int yhashcode = Fixture.y.hashCode();
	Assert.assertEquals("Equal object, return equal hashcode test fails", xhashcode, yhashcode);
    }

    /**
     * A more optimal implementation of hashcode ensures that if the objects are
     * unequal different integers are produced.
     * 
     */
    @Test
    public void testHashcodeTwoUnEqualObjectsProduceDifferentNumber() {
	final int xhashcode = Fixture.x.hashCode();
	final int yhashcode = Fixture.notx.hashCode();
	Assert.assertTrue("Equal object, return unequal hashcode test fails", !(xhashcode == yhashcode));
    }
}
