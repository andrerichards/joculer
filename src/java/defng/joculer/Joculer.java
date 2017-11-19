package defng.joculer;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.Keyword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by andre on 07/06/2017.
 */
public class Joculer {

    static final String NAMESPACE = "defng.joculer.core";

    static IFn REQUIRE = Clojure.var("clojure.core", "require");
    static IFn DEREF = Clojure.var("clojure.core", "deref");
    static IFn EQ = Clojure.var("clojure.core", "=");
    static IFn KEYWORD = Clojure.var("clojure.core", "keyword");
    static IFn STRINGIFY_KEYS = Clojure.var("clojure.walk", "stringify-keys");
    static IFn TO_CLOJURE = Clojure.var(NAMESPACE, "->clojure");
    static IFn PRETTY = Clojure.var(NAMESPACE, "pretty");

    static {
        REQUIRE.invoke(Clojure.read(NAMESPACE));
    }

    /**
     * Creates a Clojure list containing the items
     */
    public static List listOf(Object... items) {
        if (items == null) {
            return (List) toClojure(new ArrayList());
        } else {
            List list = new ArrayList(items.length);
            for (int i = 0; i < items.length; ++i) {
                list.add(items[i]);
            }
            return (List) toClojure(list);
        }
    }

    /**
     * Creates a Clojure set containing the items
     */
    public static Set setOf(Object... items) {
        if (items == null) {
            return (Set) toClojure(new HashSet());
        } else {
            Set set = new HashSet(items.length);
            for (int i = 0; i < items.length; ++i) {
                set.add(items[i]);
            }
            return (Set) toClojure(set);
        }
    }

    /**
     * Creates a Clojure map containing the keyvals
     */
    public static Map mapOf(Object... keyvals) {
        if (keyvals == null) {
            return (Map) toClojure(new HashMap());
        } else if (keyvals.length % 2 != 0) {
            throw new IllegalArgumentException("Map must have an even number of elements");
        } else {
            Map m = new HashMap(keyvals.length / 2);

            for (int i = 0; i < keyvals.length; i += 2) {
                m.put(keyvals[i], keyvals[i + 1]);
            }

            return (Map) toClojure(m);
        }
    }

    /**
     * Reads a 'data' Var (i.e. not a function) and returns the data contained in Var (i.e. deref)
     */
    public static Object readData(String namespace, String var) {
        REQUIRE.invoke(Clojure.read(namespace));
        IFn data = Clojure.var(namespace, var);
        return DEREF.invoke(data);
    }

    /**
     * Recursively transforms all map keys from keywords to strings.
     * <p>
     * Calls clojure.walk/stringify-keys
     */
    public static Object stringifyKeys(Object m) {
        return STRINGIFY_KEYS.invoke(m);
    }

    /**
     * Equality. Returns true if x equals y, false if not. Same as
     * Java x.equals(y) except it also works for nil, and compares
     * numbers and collections in a type-independent manner.  Clojure's immutable data
     * structures define equals() (and thus =) as a value, not an identity,
     * comparison.
     * <p>
     * Calls clojure.core/=
     */
    public static boolean eq(Object o1, Object o2) {
        return (Boolean) EQ.invoke(toClojure(o1), toClojure(o2));
    }

    /**
     * Returns a Keyword with the given namespace and name.  Do not use :
     * in the keyword strings, it will be added automatically.
     * <p>
     * Calls clojure.core/keyword
     */
    public static Keyword keyword(String key) {
        return (Keyword) KEYWORD.invoke(key);
    }

    /**
     * Recursively transforms maps, lists and sets to corresponding Clojure persistent data structure
     */
    public static Object toClojure(Object o) {
        return TO_CLOJURE.invoke(o);
    }

    public static String pretty(Object o) {
        return (String) PRETTY.invoke(toClojure(o));
    }

    public static String diff(String descriptionA, Object a, String descriptionB, Object b) {
        IFn diff = Clojure.var(NAMESPACE, "diff");
        List diffs = (List) diff.invoke(a, b);
        StringBuilder sb = new StringBuilder();
        sb.append(
                "\n..........................................  Things only in " + descriptionA + ":  \n"
                        + pretty(diffs.get(0)));
        sb.append(
                "\n..........................................  Things only in " + descriptionB + ":  \n"
                        + pretty(diffs.get(1)));
        sb.append(
                "\n..........................................  Things in both:  \n"
                        + pretty(diffs.get(2)));
        return sb.toString();
    }

}
