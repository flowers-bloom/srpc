package xjh.rpc.common.spi;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author XJH
 * @date 2020/12/01
 *
 * 实现类加载器
 */
@Slf4j
public class ExtensionLoader<T> {
    private static final String PREFIX = "META-INF/services/";
    /**
     * 实现类加载器缓存
     * eg: clazz => clazzLoader
     */
    private static final Map<Class<?>, ExtensionLoader<?>> CACHE_EXTENSION_LOADER
            = new ConcurrentHashMap<>();
    /**
     * 接口信息缓存
     * eg: xjh.rpc.xxx.yyyInterface => (hello => xjh.rpc.xxx.yyyImpl)
     */
    private static final Map<String, Map<String, String>> CACHE_INTERFACE
            = new ConcurrentHashMap<>();
    /**
     * 实现类实例缓存
     * eg: hello => instance
     */
    private static final Map<String, Object> CACHE_EXTENSION
            = new ConcurrentHashMap<>();

    /**
     * 缓存包装类
     * eg: xjh.rpc.xxx.yyyInterface => [xjh.rpc.xxx.aaaWrapper, xjh.rpc.xxx.bbbWrapper, ...]
     */
    private static final Map<String, Set<String>> CACHE_WRAPPER
            = new ConcurrentHashMap<>();

    private Class<T> clazz;
    private String name;
    private ClassLoader classLoader;

    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> load(Class<T> clazz) {
        Objects.requireNonNull(clazz, "service interface cannot be null");

        // 找缓存
        ExtensionLoader<T> loader = (ExtensionLoader<T>) CACHE_EXTENSION_LOADER.get(clazz);

        // 没有缓存，实例化一个并存入缓存
        if (loader == null) {
            loader = new ExtensionLoader<>(clazz);
            CACHE_EXTENSION_LOADER.putIfAbsent(clazz, loader);
        }

        return loader;
    }

    private ExtensionLoader(Class<T> clazz) {
        boolean exist = isClassExist(clazz.getName());
        if (!exist) {
            throw new RuntimeException(clazz.getName() + " not exist");
        }

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        this.clazz = clazz;
        this.classLoader = (classLoader == null) ? ClassLoader.getSystemClassLoader() : classLoader;
    }

    @SuppressWarnings("unchecked")
    public T getExtension(String name) {
        Objects.requireNonNull(name, "extension name cannot be null");
        this.name = name;

        // 找缓存
        T instance = (T) CACHE_EXTENSION.get(name);

        // 没有缓存，读取约定的目录，加载到缓存
        if (instance == null) {
            loadDirectory();
        }

        // 读取缓存
        return (T) CACHE_EXTENSION.get(name);
    }

    public T getExtension() {
        boolean hasSPI = this.clazz.isAnnotationPresent(SPI.class);
        if (!hasSPI) {
            throw new RuntimeException(this.clazz + " not declare the default extension with SPI");
        }

        SPI spi = this.clazz.getAnnotation(SPI.class);
        String name = spi.value();

        return getExtension(name);
    }

    private void loadDirectory() {
        // 加载接口信息
        String extension = loadInterface();

        // 加载实现类
        loadExtension(extension);
    }

    private String loadInterface() {
        Map<String, String> map = CACHE_INTERFACE.get(this.clazz.getName());

        String extension = "";
        if (map != null) {
            extension = map.get(this.name);
        }else {
            map = new ConcurrentHashMap<>();
            Set<String> wrapperSet = new HashSet<>();

            String classpath = PREFIX + this.clazz.getName();
            URL resource = this.classLoader.getResource(classpath);

            String absolutePath = resource.getFile();
            Objects.requireNonNull(absolutePath, absolutePath + " cannot be null");
            log.info("resource file's absolutePath = {}", absolutePath);

            try {
                BufferedReader reader = new BufferedReader(new FileReader(absolutePath));

                String line;
                while ((line = reader.readLine()) != null) {
                    int sep = line.indexOf('=');
                    if (sep == -1 && !line.endsWith("Wrapper")) {
                        throw new RuntimeException(line + " format error");
                    }

                    if (sep == -1 || line.endsWith("Wrapper")) {
                        /*
                        wrapper class
                         */
                        if (sep != -1) {
                            line = line.substring(sep+1).trim();
                        }

                        log.info("load wrapper = {}", line);
                        wrapperSet.add(line.trim());
                    }else {
                        /*
                        ordinary class
                         */
                        String id = line.substring(0, sep).trim();
                        String className = line.substring(sep+1).trim();

                        log.info("load interface = {}, {}", id, className);

                        boolean exist = isClassExist(className);
                        if (!exist) {
                            throw new Exception(className + " cannot exist");
                        }

                        String value = map.get(id);
                        if (value != null) {
                            throw new RuntimeException(id + " is repetitive");
                        }

                        if (id.equals(this.name)) {
                            extension = className;
                        }

                        map.put(id, className);
                    }
                }

                CACHE_INTERFACE.put(this.clazz.getName(), map);
                CACHE_WRAPPER.put(this.clazz.getName(), wrapperSet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return extension;
    }

    private void loadExtension(String extension) {
        if ("".equals(extension)) {
            throw new RuntimeException("cannot find extension");
        }

        try {
            Class<?> extensionClass = Class.forName(extension);
            Object instance = extensionClass.newInstance();
            log.info("{} , object hashcode = {}", extensionClass, instance.hashCode());

            dependencyInject(extensionClass, instance);

            /*
            包装类
             */
            Set<String> cacheWrapperSet = CACHE_WRAPPER.get(this.clazz.getName());
            if (cacheWrapperSet != null && !cacheWrapperSet.isEmpty()) {
                for (String wrapper : cacheWrapperSet) {
                    Class<?> wrapperClass = Class.forName(wrapper);

                    if (isWrapperClass(wrapperClass, extensionClass)) {
                        log.info("before wrap class, wrapperClass = {}", wrapperClass.getName());

                        instance = dependencyInject(wrapperClass,
                                wrapperClass.getConstructor(extensionClass)
                                        .newInstance(instance));

                        log.info("after wrap class, instance hashcode = {}", instance.hashCode());
                    }else {
                        log.info("{} is not {}'s wrapperClass", wrapperClass, extensionClass);
                    }
                }
            }else {
                log.info("cacheWrapperSet is null or isEmpty");
            }

            CACHE_EXTENSION.put(this.name, instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object dependencyInject(Class<?> clazz, Object instance) {
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.getName().startsWith("set") &&
                    Modifier.isPublic(method.getModifiers()) &&
                    method.getParameterCount() == 1) {
                Class<?> parameterClazz = method.getParameterTypes()[0];

                try {
                    Object parameter = new ExtensionLoader<>(parameterClazz).getExtension();
                    method.invoke(instance, parameter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return instance;
    }

    private boolean isWrapperClass(Class<?> wrapperClass, Class<?> extensionClass) {
        try {
            wrapperClass.getConstructor(extensionClass);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * 判断类是否存在
     *
     * 不用 Class.forName() 原因在于该方法会加载类的静态代码块和初始化静态属性
     * Class.forName(className) -> Class.forName(className, true, classLoader)
     * 第二个参数表示类是否初始化，默认 true
     *
     * ClassLoader.loadClass(className) -> ClassLoader.loadClass(className, false)
     * 第二个参数表示对象是否链接(给类的静态变量分配并初始化存储空间)，默认 false
     *
     * @param clazz
     * @return
     */
    private boolean isClassExist(String clazz) {
        try {
            Thread.currentThread().getContextClassLoader().loadClass(clazz);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
