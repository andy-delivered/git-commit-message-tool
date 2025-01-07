package app.oyal.plugins.gitcommitmessagetool.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.vcs.CommitMessageI;
import com.intellij.openapi.vcs.VcsDataKeys;
import org.jetbrains.annotations.NotNull;
import app.oyal.plugins.gitcommitmessagetool.ui.CommitTemplateDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommitTemplateAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        CommitMessageI commitMessageComponent = e.getData(VcsDataKeys.COMMIT_MESSAGE_CONTROL);
        Document commitMessageDocument = e.getData(VcsDataKeys.COMMIT_MESSAGE_DOCUMENT);
        String existingCommitMessage = commitMessageDocument != null ? commitMessageDocument.getText() : "";

        CommitTemplateDialog dialog = new CommitTemplateDialog();
        if (!existingCommitMessage.isEmpty()) {
            CommitMessageData data = parseCommitMessage(existingCommitMessage);
            dialog.setJiraKeyComboBox(data.jiraKey);
            dialog.setJiraNumberField(data.jiraKeyNumber);
            dialog.setType(data.type);
            dialog.setShortDescription(data.shortDescription);
            dialog.setDetailedDescription(data.detailedDescription);
            dialog.setBreakingChange(data.breakingChange);
            dialog.setClosedIssues(data.closedIssues);
            dialog.setSkipCI(data.skipCI);
        }

        if (dialog.showAndGet()) {
            String jiraKey = dialog.getJiraKeyComboBox();
            String jiraKeyNumber = dialog.getJiraNumberField();
            String typeWithDescription = dialog.getType();
            String type = typeWithDescription.split(" - ")[0];
            String shortDescription = dialog.getShortDescription();
            String detailedDescription = dialog.getDetailedDescription();
            String breakingChange = dialog.getBreakingChange();
            String closedIssues = dialog.getClosedIssues();
            boolean skipCI = dialog.isSkipCI();

            String commitMessage = formatCommitMessage(
                    jiraKey,
                    jiraKeyNumber,type, shortDescription, detailedDescription, breakingChange, closedIssues, skipCI);

            if (commitMessageComponent != null) {
                commitMessageComponent.setCommitMessage(commitMessage);
            }
        }
    }

    private CommitMessageData parseCommitMessage(String commitMessage) {
        Pattern pattern = Pattern.compile("^([A-Za-z]+)-(\\\\d+) (\\\\w+) (.+?)(?:\\\\n\\\\n(.+?))?(?:\\\\n\\\\nBREAKING CHANGE: (.+?))?(?:\\\\n\\\\nCloses: (.+?))?(?:\\\\n\\\\n\\\\[skip ci])?$\n", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(commitMessage);

        if (matcher.find()) {
            String jiraKey = matcher.group(1); // 앞부분 (영문자)
            String jiraKeyNumber = matcher.group(2); // 숫자
            String type = matcher.group(3); // 타입
            String shortDescription = matcher.group(4); // 헤드라인
            String detailedDescription = matcher.group(5) != null ? matcher.group(5) : ""; // 본문 (선택적)
            String breakingChange = matcher.group(6) != null ? matcher.group(6) : ""; // BREAKING CHANGE (선택적)
            String closedIssues = matcher.group(7) != null ? matcher.group(7) : ""; // Closes (선택적)
            boolean skipCI = commitMessage.contains("[skip ci]");

            return new CommitMessageData(jiraKey, jiraKeyNumber, type, shortDescription, detailedDescription, breakingChange, closedIssues, skipCI);
        }

        return new CommitMessageData("","", "", "", "", "", "", false);
    }

    private static class CommitMessageData {
        String jiraKey;
        String type;
        String jiraKeyNumber;
        String shortDescription;
        String detailedDescription;
        String breakingChange;
        String closedIssues;
        boolean skipCI;

        CommitMessageData(String jiraKey, String jiraKeyNumber, String type, String shortDescription, String detailedDescription, String breakingChange, String closedIssues, boolean skipCI) {
            this.jiraKey = jiraKey;
            this.jiraKeyNumber = jiraKeyNumber;
            this.type = type;
            this.shortDescription = shortDescription;
            this.detailedDescription = detailedDescription;
            this.breakingChange = breakingChange;
            this.closedIssues = closedIssues;
            this.skipCI = skipCI;
        }
    }

    private String formatCommitMessage(String jiraKey, String jiraKeyNumber, String type, String shortDescription, String detailedDescription, String breakingChange, String closedIssues, boolean skipCI) {
        StringBuilder commitMessage = new StringBuilder();
        commitMessage.append(jiraKey).append("-").append(jiraKeyNumber).append(" ");
        commitMessage.append(type);

        commitMessage.append(" ").append(shortDescription);
        if (!detailedDescription.isEmpty()) {
            commitMessage.append("\n\n").append(detailedDescription);
        }
        if (!breakingChange.isEmpty()) {
            commitMessage.append("\n\n").append("BREAKING CHANGE: ").append(breakingChange);
        }
        if (!closedIssues.isEmpty()) {
            commitMessage.append("\n\n").append("Closes: ").append(closedIssues);
        }
        if (skipCI) {
            commitMessage.append("\n\n").append("[skip ci]").append("\n");
        }
        return commitMessage.toString();
    }
}
