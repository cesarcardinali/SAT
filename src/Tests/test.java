package Tests;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.ProgressMonitor;
import com.atlassian.jira.rest.client.domain.Comment;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.Transition;
import com.atlassian.jira.rest.client.domain.input.FieldInput;
import com.atlassian.jira.rest.client.domain.input.TransitionInput;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClient;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;

public class test {

	public static void main(String[] args) throws URISyntaxException {
        final JerseyJiraRestClientFactory factory = new JerseyJiraRestClientFactory();
        final URI jiraServerUri = new URI("http://idart.mot.com");
        final JerseyJiraRestClient restClient = (JerseyJiraRestClient) factory.createWithBasicHttpAuthentication(jiraServerUri, "cesarc", "swHOME27611");
        final NullProgressMonitor pm = new NullProgressMonitor();
        final Issue issue = (Issue) restClient.getIssueClient().getIssue("BATTRIAGE-35", (ProgressMonitor) pm);
 
        System.out.println(issue);

 
        // now let's watch it
        restClient.getIssueClient().watch(issue.getWatchers().getSelf(), pm);
 
        // now let's start progress on this issue
        final Iterable<Transition> transitions = restClient.getIssueClient().getTransitions(issue.getTransitionsUri(), pm);
        final Transition startProgressTransition = getTransitionByName(transitions, "Start Progress");
        restClient.getIssueClient().transition(issue.getTransitionsUri(), new TransitionInput(startProgressTransition.getId()), pm);
 
        // and now let's resolve it as Incomplete
        final Transition resolveIssueTransition = getTransitionByName(transitions, "Resolve Issue");
        Collection<FieldInput> fieldInputs = Arrays.asList(new FieldInput("resolution", "Incomplete"));
        final TransitionInput transitionInput = new TransitionInput(resolveIssueTransition.getId(), fieldInputs, Comment.valueOf("My comment"));
        restClient.getIssueClient().transition(issue.getTransitionsUri(), transitionInput, pm);
 
    }
 
    private static Transition getTransitionByName(Iterable<Transition> transitions, String transitionName) {
        for (Transition transition : transitions) {
            if (transition.getName().equals(transitionName)) {
                return transition;
            }
        }
        return null;
    }

}
