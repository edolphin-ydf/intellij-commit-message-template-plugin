package commitmessagetemplate;

import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory;
import commitmessagetemplate.util.NotificationHelper;
import org.jetbrains.annotations.NotNull;

/**
 * Created by edolphin on 20/05/2017.
 */
public class RedmineCheckinHandlerFactory extends CheckinHandlerFactory {

    @NotNull
    @Override
    public CheckinHandler createHandler(@NotNull CheckinProjectPanel checkinProjectPanel, @NotNull CommitContext commitContext) {
        return new RedmineCheckinHandler(checkinProjectPanel, commitContext);
    }
}
