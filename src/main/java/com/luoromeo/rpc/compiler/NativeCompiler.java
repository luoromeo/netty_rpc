package com.luoromeo.rpc.compiler;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Locale;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 09:32
 * @modified By
 */
public class NativeCompiler implements Closeable {

    private final File tempFolder;

    private final URLClassLoader classLoader;

    public NativeCompiler(File tempFolder) {
        this.tempFolder = tempFolder;
        this.classLoader = createClassLoader(tempFolder);
    }

    private static URLClassLoader createClassLoader(File tempFolder) {
        try {
            URL[] urls = { tempFolder.toURI().toURL() };
            return new URLClassLoader(urls);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public Class<?> compile(String className, String code) {
        try {
            JavaFileObject sourceFile = new StringJavaFileObject(className, code);
            compileClass(sourceFile);
            return classLoader.loadClass(className);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private void compileClass(JavaFileObject sourceFile) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(collector, Locale.ROOT, null)) {
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singleton(tempFolder));
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, collector, null, null, Collections.singleton(sourceFile));
            task.call();
        }
    }

    @Override
    public void close() {
        try {
            classLoader.close();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public URLClassLoader getClassLoader() {
        return classLoader;
    }
}
