package commitmessagetemplate;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public interface IssueTrackerPlatformCenter {
    static IssueTrackerPlatformCenter getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, IssueTrackerPlatformCenter.class);
    }

    // 获取所有平台的配置UI
    Map<String, PlatformGUI> getPlatformGuiMap();

    // 获取平台运行时的全局数据
    Object getRuntimeData(String platform);

    Map<String, Class<? extends PlatformGUI>> platformConfigGuiClassMap = new HashMap<>();
}
