package commitmessagetemplate;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by edolphin on 17-5-23.
 */
public class NotifiedIssues {

    public static class NotifiedIssue {
        Long id;
        Long statusId;

        public NotifiedIssue() {
        }

        public NotifiedIssue(Long id, Long statusId) {
            this.id = id;
            this.statusId = statusId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NotifiedIssue that = (NotifiedIssue) o;

            if (id != null ? !id.equals(that.id) : that.id != null) return false;
            return statusId != null ? statusId.equals(that.statusId) : that.statusId == null;

        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (statusId != null ? statusId.hashCode() : 0);
            return result;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getStatusId() {
            return statusId;
        }

        public void setStatusId(Long statusId) {
            this.statusId = statusId;
        }
    }

    Set<NotifiedIssue> notifiedIssues = new HashSet<>();

    public Set<NotifiedIssue> getNotifiedIssues() {
        return notifiedIssues;
    }

    public void setNotifiedIssues(Set<NotifiedIssue> notifiedIssues) {
        this.notifiedIssues = notifiedIssues;
    }

    public NotifiedIssues() {
    }
}
