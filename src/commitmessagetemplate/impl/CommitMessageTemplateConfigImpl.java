package commitmessagetemplate.impl;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import commitmessagetemplate.ClassLoader;
import commitmessagetemplate.CommitMessageTemplateConfig;
import commitmessagetemplate.platform.Templatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

@State(
        name = "CommitMessageTemplateConfig",
        storages = {
                @Storage(
                        value = "CommitMessageTemplateConfig.xml"
                )
        }
)
public class CommitMessageTemplateConfigImpl implements CommitMessageTemplateConfig {

    private CommitState cmState = new CommitState();

    public CommitMessageTemplateConfigImpl(Project project) {
        ClassLoader.LoadClass();
        cmState.platformConfigMap = new HashMap<>();

        CommitMessageTemplateConfig.platformConfigClassMap.forEach((name, clazz) -> {
            try {
                cmState.platformConfigMap.put(name, clazz.newInstance());
            } catch (IllegalAccessException|InstantiationException e) {
            }
        });
    }

    @Nullable
    @Override
    public SavedState getState() {
        return CommitMessageTemplateConfig.CommitStateToSavedState(cmState);
    }

    public CommitState getCommitState() {
        return cmState;
    }

    @Override
    public void loadState(@NotNull SavedState state) {
        cmState = CommitMessageTemplateConfig.SavedStateToCommitState(state);
        if (cmState.platformConfigMap == null)
            cmState.platformConfigMap = new HashMap<>();

        CommitMessageTemplateConfig.platformConfigClassMap.forEach((name, clazz) -> {
            try {
                if (!cmState.platformConfigMap.containsKey(name))
                    cmState.platformConfigMap.put(name, (Templatable) clazz.newInstance());
            } catch (IllegalAccessException|InstantiationException e) {
            }
        });
    }
}
