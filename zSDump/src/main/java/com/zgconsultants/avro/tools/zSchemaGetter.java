package com.zgconsultants.avro.tools;

import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

class zSchemaGetter {

    private final Map<String, Object> conf;
    private final String namespace;
    private static final Logger LOG = LoggerFactory.getLogger(zSchemaGetter.class);


    class zJarWalker {
        private final String jarMask;

        zJarWalker() {
            jarMask = (String) conf.get("JarMask");
        }

        @SuppressWarnings({ "restriction", "unchecked" })
        private  URL[] getUrls(ClassLoader classLoader) {
            if (classLoader instanceof URLClassLoader) {
                return ((URLClassLoader) classLoader).getURLs();
            }

            // jdk9
            if (classLoader.getClass().getName().startsWith("jdk.internal.loader.ClassLoaders$")) {
                try {
                    Field field = Unsafe.class.getDeclaredField("theUnsafe");
                    field.setAccessible(true);
                    Unsafe unsafe = (Unsafe) field.get(null);

                    // jdk.internal.loader.ClassLoaders.AppClassLoader.ucp
                    Field ucpField = classLoader.getClass().getDeclaredField("ucp");
                    long ucpFieldOffset = unsafe.objectFieldOffset(ucpField);
                    Object ucpObject = unsafe.getObject(classLoader, ucpFieldOffset);

                    // jdk.internal.loader.URLClassPath.path
                    Field pathField = ucpField.getType().getDeclaredField("path");
                    long pathFieldOffset = unsafe.objectFieldOffset(pathField);
                    ArrayList<URL> path = (ArrayList<URL>) unsafe.getObject(ucpObject, pathFieldOffset);

                    return path.toArray(new URL[path.size()]);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        }
        private String jarFileName() {
            ClassLoader cl = ClassLoader.getSystemClassLoader();

            URL[] urls = getUrls(cl);

            for (URL url : urls) {
                if (url.getFile().contains(jarMask))
                    return url.getFile();
            }

            String m = "no jar file with mask " + jarMask + " was found";
            throw new IllegalArgumentException(m);
        }

        List<String> classNamesFromJar() throws IOException{
            String jfn = jarFileName();
            List<String> l = getCrunchifyClassNamesFromJar(jfn);

            return l.stream().filter(s -> s.endsWith("Msg")).collect(Collectors.toList());
        }

        @SuppressWarnings("resource")
        private List<String> getCrunchifyClassNamesFromJar(String crunchifyJarName) throws IOException {
            List<String> l;
            try {
                JarInputStream crunchifyJarFile = new JarInputStream(new FileInputStream(crunchifyJarName));
                JarEntry crunchifyJar;

                l = new LinkedList<>();
                while (true) {
                    crunchifyJar = crunchifyJarFile.getNextJarEntry();
                    if (crunchifyJar == null) {
                        break;
                    }
                    if ((crunchifyJar.getName().endsWith(".class"))) {

                        String className = crunchifyJar.getName().replaceAll("/", "\\.");
                        String myClass = className.substring(0, className.lastIndexOf('.'));
                        l.add(myClass);
                    }
                }
            } catch (Exception e) {
                LOG.error("Oops.. Encounter an issue while parsing jar" + e.toString());
                throw e;
            }

            return l;
        }

    }

    zSchemaGetter(Map<String, Object> conf) {
        this.conf = conf;
        namespace = (String)this.conf.get("namespace");
    }

    List<Schema> getSchemas() throws Exception{
        List<Schema> l = new LinkedList<>();

        for(String className  : getClassNames()){
            l.add(getSchema(className));
        }

        return l;
    }

    private List<String> getClassNames() throws IOException{

        LinkedList<String> l = new LinkedList<>();
        //if we have a specific class name (from the command line most likely)==> use it
        String className = (String)conf.get("className");
        if (className != null) {
            l.add(namespace + "." + className);
            return l;
        }
        // otherwise walk the entire jar file
        zJarWalker zjw = new zJarWalker();

        return zjw.classNamesFromJar();
    }

    private static Schema getSchema(String className) throws Exception {

        Schema schema;

        try {
            Class<?> c = Class.forName(className);
            Method method = c.getDeclaredMethod("getClassSchema");
            schema = (Schema) method.invoke(null);

        } catch (Exception e) {
            LOG.error("wrong class name");
            throw e;
        }

        return schema;
    }
}
