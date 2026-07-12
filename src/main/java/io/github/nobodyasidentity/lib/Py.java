package io.github.nobodyasidentity.lib;

import java.util.*;
import java.util.stream.*;

public final class Py{
    private Py(){}
    public static void print(Object... args){
        System.out.println(
            Arrays.stream(args).map(Py::str).collect(Collectors.joining(" "))
        );
    }
    public static void printWith(String sep,String end,Object... args){
        System.out.print(
            Arrays.stream(args).map(Py::str).collect(Collectors.joining(sep))+end
        );
    }
    public static String str(Object...o){
        return Arrays.stream(o).map(Py::str).collect(Collectors.joining(" "));
    }
    public static String str(Object o){
        if(o==null)return"null";
        if(o instanceof Boolean b)return b?"true":"false";
        if(o instanceof int[] a)return Arrays.toString(a);
        if(o instanceof long[] a)return Arrays.toString(a);
        if(o instanceof double[] a)return Arrays.toString(a);
        if(o instanceof float[] a)return Arrays.toString(a);
        if(o instanceof char[] a)return Arrays.toString(a);
        if(o instanceof Object[] a)return Arrays.deepToString(a);
        return o.toString();
    }
    public static boolean bool(Object o){
        if(o==null)return false;
        if(o instanceof Boolean b)return b;
        if(o instanceof Integer i)return i!=0;
        if(o instanceof Long l)return l!=0L;
        if(o instanceof Double d)return d!=0.0;
        if(o instanceof Float f)return f!=0.0f;
        if(o instanceof String s)return !s.isEmpty();
        if(o instanceof Collection<?> c)return !c.isEmpty();
        if(o instanceof Map<?,?> m)return !m.isEmpty();
        if(o instanceof int[] a)return a.length>0;
        if(o instanceof Object[] a)return a.length>0;
        return true;
    }

    public static boolean isinstance(Object o,Class<?>... classes){
        for (Class<?>c:classes)if(c.isInstance(o))return true;
        return false;
    }

    public static int len(String s){return s.length();}
    public static int len(Collection<?> c){return c.size();}
    public static int len(Map<?,?> m){return m.size();}
    public static int len(Object[] a){return a.length;}
    public static int len(int[] a){return a.length;}
    public static int len(double[] a){return a.length;}
    public static int len(long[] a){return a.length;}
    public static int len(char[] a){return a.length;}

    public static List<Integer> range(int stop){return range(0,stop,1);}
    public static List<Integer> range(int start,int stop){return range(start,stop,1);}
    public static List<Integer> range(int start,int stop,int step){
        if(step==0)throw new RuntimeException("range() arg 3 must not be zero");
        List<Integer> r=new ArrayList<>();
        if(step>0){for(int i=start;i<stop;i+=step)r.add(i);}
        else{for(int i=start;i>stop;i+=step)r.add(i);}
        return r;
    }

    @SafeVarargs
    public static <T> List<T> list(T... items){
        return new ArrayList<>(Arrays.asList(items));
    }
    public static <T> List<T> list(Iterable<T> it){
        List<T> r=new ArrayList<>(); it.forEach(r::add);return r;
    }
    @SafeVarargs
    public static <T> Set<T> set(T... items){
        return new LinkedHashSet<>(Arrays.asList(items));
    }
    public static <T> Set<T> set(Iterable<T> it){
        Set<T> r=new LinkedHashSet<>(); it.forEach(r::add);return r;
    }
    public static <K,V> Map<K,V> dict(){return new LinkedHashMap<>();}
    @SafeVarargs
    public static <K,V> Map<K,V> dict(Map.Entry<K,V>... entries){
        Map<K,V> m=new LinkedHashMap<>();
        for(var e:entries)m.put(e.getKey(),e.getValue());
        return m;
    }

    public static <T> List<T> slice(List<T> l,int stop){return slice(l,0,stop,1);}
    public static <T> List<T> slice(List<T> l,int start,int stop){return slice(l,start,stop,1);}
    public static <T> List<T> slice(List<T> l,int start,int stop,int step){
        int n=l.size();
        if(start<0)start=Math.max(0,n+start);
        if(stop<0)stop=Math.max(0,n+stop);
        start=Math.min(start,n);
        stop=Math.min(stop,n);
        List<T> r=new ArrayList<>();
        if(step>0){for(int i=start;i<stop;i+=step)r.add(l.get(i));}
        else{for(int i=start;i>stop;i+=step)r.add(l.get(i));}
        return r;
    }
    public static String slice(String s,int stop){return slice(s,0,stop,1);}
    public static String slice(String s,int start,int stop){return slice(s,start,stop,1);}
    public static String slice(String s,int start,int stop,int step){
        int n=s.length();
        if(start<0)start=Math.max(0,n+start);
        if(stop<0)stop=Math.max(0,n+stop);
        start=Math.min(start,n);
        stop=Math.min(stop, n);
        StringBuilder sb=new StringBuilder();
        if(step>0){for(int i=start;i<stop;i+=step)sb.append(s.charAt(i));}
        else{for(int i=start;i>stop;i+=step)sb.append(s.charAt(i));}
        return sb.toString();
    }

    public static boolean any(Iterable<Boolean> it){
        for(boolean b:it)if(b)return true;
        return false;
    }
    public static boolean all(Iterable<Boolean> it){
        for (boolean b:it)if(!b)return false;
        return true;
    }

    public static long sum(int... v){return Arrays.stream(v).asLongStream().sum();}
    public static double sum(double... v){return Arrays.stream(v).sum();}
    public static <N extends Number> double sum(Collection<N> c){return c.stream().mapToDouble(Number::doubleValue).sum();}
}