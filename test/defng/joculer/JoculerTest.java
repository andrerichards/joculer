package defng.joculer;

import clojure.lang.IPersistentMap;
import clojure.lang.IPersistentSet;
import clojure.lang.IPersistentVector;
import org.junit.Test;

import java.util.*;

import static defng.joculer.Joculer.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JoculerTest {

    @Test
    public void test_listOf() throws Exception {
        List l = listOf(1, 2, 3);
        assertEquals(3, l.size());
        assertTrue(l instanceof IPersistentVector);
    }

    @Test
    public void test_mapOf() throws Exception {
        Map m = mapOf("one", 1, "two", 2, "three", 3);
        assertEquals(3, m.size());
        assertEquals(3, m.get("three"));
        assertTrue(m instanceof IPersistentMap);
    }

    @Test
    public void test_setOf() throws Exception {
        Set s = setOf(1, 2, 3, 4);
        assertEquals(3, s.size());
        assertTrue(s instanceof IPersistentSet);
    }

    @Test
    public void test_readData() throws Exception {
        List v = (List) Joculer.readData("defng.joculer.test-data", "v");
        assertTrue(eq(listOf(1, 2, 3), v));
    }

    @Test
    public void test_stringifyKeys() throws Exception {
        {
            Map m = (Map) Joculer.readData("defng.joculer.test-data", "m");
            assertTrue(eq(mapOf("one", 1, "two", 2, "three", 3), Joculer.stringifyKeys(m)));
        }
        {
            Map m = (Map) Joculer.readData("defng.joculer.test-data", "nested-m");
            assertTrue(eq(mapOf("one", 1, "two", 2, "nested", mapOf("three", 3)), Joculer.stringifyKeys(m)));
        }
    }

    @Test
    public void test_keywordizeKeys() throws Exception {
        {
            Map m = (Map) Joculer.readData("defng.joculer.test-data", "m");
            assertTrue(eq(m, Joculer.keywordizeKeys(Joculer.stringifyKeys(m))));
        }
        {
            Map m = (Map) Joculer.readData("defng.joculer.test-data", "nested-m");
            assertTrue(eq(m, Joculer.keywordizeKeys(Joculer.stringifyKeys(m))));
        }
    }

    @Test
    public void test_eq() throws Exception {
        List l1 = listOf(1, 2, 3);
        List l2 = listOf(1, 2, 3);
        assertTrue(eq(l1, l2));

        List l3 = listOf(1, 2, 4);
        assertFalse(eq(l1, l3));
    }

    @Test
    public void test_keyword() throws Exception {
        Object kw = Joculer.readData("defng.joculer.test-data", "kw");
        assertTrue(eq(kw, keyword("one")));
    }

    @Test
    public void test_toClojure() throws Exception {
        {
            Object s = "string";
            assertEquals("string", toClojure(s));
        }
        {
            List l1 = new ArrayList();
            l1.add(1);
            l1.add(2);
            l1.add(3);
            List l2 = (List) toClojure(l1);
            assertEquals(3, l2.size());
            assertEquals(1, l2.get(0));
            assertEquals(2, l2.get(1));
            assertEquals(3, l2.get(2));
            assertTrue(l2 instanceof IPersistentVector);
        }
        {
            Map m1 = new HashMap();
            m1.put("one", 1);
            m1.put("two", 2);
            m1.put("three", 3);
            Map m2 = (Map) toClojure(m1);
            assertEquals(3, m2.size());
            assertEquals(1, m2.get("one"));
            assertEquals(2, m2.get("two"));
            assertEquals(3, m2.get("three"));
            assertTrue(m2 instanceof IPersistentMap);
        }
    }

}
