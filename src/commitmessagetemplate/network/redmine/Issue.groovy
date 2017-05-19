package commitmessagetemplate.network.redmine
/**
 * Created by edolphin on 17-5-19.
 */
class Issue {

    def id

    Project project

    Tracker tracker

    Status status

    Priority priority

    Author author

    def subject

    def description

    def start_data

    def done_ratio

    def create_on

    def updated_on
}
