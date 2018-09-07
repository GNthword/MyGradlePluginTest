package com.milog.test.plugin;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MyPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {


        System.out.println("========================");
        transform(project);
    }

    private void transform(Project project) {
        ClassFileTransform classFileTransform = new ClassFileTransform(project);
        AppExtension appExtension = project.getExtensions().findByType(AppExtension.class);
        appExtension.registerTransform(classFileTransform);
    }


}
