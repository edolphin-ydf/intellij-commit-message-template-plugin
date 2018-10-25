package commitmessagetemplate.impl;

import com.intellij.openapi.project.Project;
import commitmessagetemplate.ClassLoader;
import commitmessagetemplate.IssueTrackerPlatformCenter;
import commitmessagetemplate.PlatformGUI;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class IssueTrackerPlatformCenterImpl implements IssueTrackerPlatformCenter {

    private Map<String, PlatformGUI> platformGuiMap = new TreeMap<>();

    private Map<String, Object> platformDataMap = new HashMap<>();

    public IssueTrackerPlatformCenterImpl(Project project) {
        ClassLoader.LoadClass();

        IssueTrackerPlatformCenter.platformConfigGuiClassMap.forEach((name, clazz) -> {
            try {
                PlatformGUI gui = clazz.newInstance();
                gui.createUI(project);
                platformGuiMap.put(name, gui);
            } catch (ReflectiveOperationException e) {

            }
        });
    }

    public Map<String, PlatformGUI> getPlatformGuiMap() {
        return platformGuiMap;
    }

    public Object getRuntimeData(String platform) {
        return platformDataMap.get(platform);
    }
}
