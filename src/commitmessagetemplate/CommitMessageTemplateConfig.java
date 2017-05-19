package commitmessagetemplate;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import commitmessagetemplate.CommitMessageTemplateConfig.CommitState;
import org.jetbrains.annotations.Nullable;


@State(
        name = "CommitMessageTemplateConfig",
        storages = {
                @Storage("CommitMessageTemplateConfig.xml")}
)
public class CommitMessageTemplateConfig implements PersistentStateComponent<CommitState> {

    private CommitState cmState = new CommitState();

    public String getHost() {
        return cmState.host;
    }

    public void setHost(String host) {
        this.cmState.host = host;
    }

    public String getKey() {
        return cmState.key;
    }

    public void setKey(String key) {
        this.cmState.key = key;
    }

    public String getTemplate() {
        return cmState.template;
    }

    public void setTemplate(String template) {
        this.cmState.template = template;
    }



    @Nullable
    @Override
    public CommitState getState() {
        return cmState;
    }

    @Override
    public void loadState(CommitState state) {
        cmState = state;
    }

    @Nullable
    static CommitMessageTemplateConfig getInstance(Project project) {
        return ServiceManager.getService(project, CommitMessageTemplateConfig.class);
    }

    public static class CommitState {
        public String host;
        public String key;
        public String template;

    }
}
