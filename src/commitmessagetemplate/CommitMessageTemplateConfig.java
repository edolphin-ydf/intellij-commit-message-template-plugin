package commitmessagetemplate;

import com.alibaba.fastjson.JSON;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import commitmessagetemplate.platform.Templatable;
import commitmessagetemplate.platform.redmine.RedmineConfigurable;

import java.util.HashMap;
import java.util.Map;


public interface CommitMessageTemplateConfig extends PersistentStateComponent<CommitMessageTemplateConfig.SavedState> {
    static CommitMessageTemplateConfig getInstance(Project project) {
        return ServiceManager.getService(project, CommitMessageTemplateConfig.class);
    }

    CommitState getCommitState();

    class CommitState {
        public String selectedPlatform;
        public Map<String, Templatable> platformConfigMap;

        public Templatable getConfig() {
            if (selectedPlatform == null || selectedPlatform.isEmpty())
                selectedPlatform = RedmineConfigurable.PlatformName;
            return platformConfigMap.get(selectedPlatform);
        }
    }

    class SavedState {
        public String selectedPlatform;
        public Map<String, String> platformConfigMap;
    }

    static SavedState CommitStateToSavedState(CommitState cs) {
        SavedState ss = new SavedState();
        ss.selectedPlatform = cs.selectedPlatform;
        ss.platformConfigMap = new HashMap<>();

        cs.platformConfigMap.forEach((name, cfg) -> {
            ss.platformConfigMap.put(name, JSON.toJSONString(cfg));
        });

        return ss;
    }

    static CommitState SavedStateToCommitState(SavedState ss) {
        CommitState cs = new CommitState();
        cs.selectedPlatform = ss.selectedPlatform;
        cs.platformConfigMap = new HashMap<>();
        try {
            for (String name : ss.platformConfigMap.keySet()) {
                String cfgStr = ss.platformConfigMap.get(name);
                Class clazz = platformConfigClassMap.get(name);
                Object o = JSON.parseObject(cfgStr, clazz);
                if (o instanceof Templatable)
                    cs.platformConfigMap.put(name, (Templatable) o);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return cs;
    }

    Map<String, Class<? extends Templatable>> platformConfigClassMap = new HashMap<>();
}
