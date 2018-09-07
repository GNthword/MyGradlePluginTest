package com.milog.test.plugin;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * Created by miloway on 2018/8/30.
 */

public class ClassFileInjectBtnClick {


    //private static final String inject = "MainActivity.this.tvShow.setText(MainActivity.this.tvShow.getText() + \" inject\");";
    private static final String inject = "this$0.tvShow.setText(this$0.tvShow.getText() + \" inject\");";
    private static final String inject2 = "btnOk.setText(btnOk.getText() + \" inject2\");";
    private static final String TAG = "ClassFileInjectBtnClick ";

    private static ClassPool classPool = ClassPool.getDefault();
    private static String basePath = "";

    public static void inject(String path, Project project, String packageName) {
        try {
            System.out.println("transform2 inject1" + path);
            AppExtension extension = (AppExtension) project.getExtensions().findByName("android");

            classPool.appendClassPath(path);
            classPool.appendClassPath(extension.getBootClasspath().get(0).toString());

            basePath = path;
            searchFile(path, packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void searchFile(String path, String packageName) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files == null) {
                System.out.println("transform2 inject end1");
                return;
            }

            System.out.println("transform2 inject3.5 "+ path);
            for (File file : files) {
                searchFile(file.getAbsolutePath(), packageName);
            }

        }else {
            try {
                processFile(dir, packageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void processFile(File file, String packageName) throws NotFoundException, CannotCompileException, IOException {
        String filePath = file.getPath();
        System.out.println("transform2 inject3.6 " + filePath);
        if (filterFile(filePath)) {
            int index = filePath.indexOf(packageName);
            boolean isMyPackage = index != -1;
            if (isMyPackage) {
                int end = filePath.length() - 6; // .class = 6
                String className = filePath.substring(index, end)
                        .replace('\\', '.').replace('/', '.');

                //class file
                System.out.println("transform2 inject4 " + className);
                CtClass c = classPool.getCtClass(className);
                if (c.isFrozen()) {
                    c.defrost();
                }

                CtMethod[] methods = c.getDeclaredMethods("onClick");
                if (methods != null) {
                    for (CtMethod method : methods) {
                        System.out.println("onclick method " + method.getLongName());
                        if (className.equals("com.milog.test.mygradleplugintest.MainActivity")) {
                            dealAnnotation(c);
                            method.insertAfter(inject2);
                        }else {
                            method.insertAfter(inject);
                        }
                    }
                    c.writeFile(basePath);
                }


                c.detach();
            }
        }
    }

    private static void dealAnnotation(CtClass c) {
        try {
            CtField field = c.getDeclaredField("state");
            Object[] ans = field.getAvailableAnnotations();
            if (ans != null) {
                for (Object o : ans) {
//                    if (o instanceof FunctionManager)
                    //simplify module as known
                    Method method = o.getClass().getDeclaredMethod("value");
                    String value = (String) method.invoke(o);

                    //set back the value to field
                    //the full getResources().getString(com.milog.test.mygradleplugintest.R.string.function_state)
                    CtMethod[] methods = c.getDeclaredMethods("onCreate");
                    if (methods != null) {
                        for (CtMethod method1 : methods) {
                            System.out.println("onclick method1 " + method1.getLongName());
                            method1.insertBefore("state = getResources().getString(com.milog.test.mygradleplugintest.R.string."+value+");");;
                        }
                    }
                }
            }
        } catch (NotFoundException | NoSuchMethodException | InvocationTargetException | CannotCompileException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private static boolean filterFile(String path) {
        return path.endsWith(".class")
                && !path.contains("R$")
                && !path.contains("R.class")
                && !path.contains("BuildConfig.class");
    }


}
