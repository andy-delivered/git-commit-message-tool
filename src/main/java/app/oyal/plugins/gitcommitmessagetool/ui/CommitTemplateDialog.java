package app.oyal.plugins.gitcommitmessagetool.ui;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class CommitTemplateDialog extends DialogWrapper {
    private ComboBox<String> jiraKeyComboBox;

    private JBTextField jiraNumberField;
    private ComboBox<String> typeComboBox;
    private JBTextField shortDescriptionField;
    private JBTextArea detailedDescriptionArea;
    private JBTextArea breakingChangeCheckBox;
    private JBTextField closedIssuesField;
    private JCheckBox skipCICheckBox;

    public CommitTemplateDialog() {
        super(true);
        setTitle("Commit");
        setSize(750, 520);
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = JBUI.insets(5);

        gbc.weightx = 0.2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Jira Key"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Jira Number"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Change Type"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Short Description"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Detailed Description"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Breaking Change"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Closed Issues"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Skip CI"), gbc);

        gbc.weightx = 0.8;
        gbc.gridx = 1;
        gbc.gridy = 0;

        jiraKeyComboBox = new ComboBox<>(new String[]{"XBD", "GCD", "DBTS"});
        formPanel.add(jiraKeyComboBox, gbc);
        gbc.gridy++;
        jiraNumberField = new JBTextField();
        formPanel.add(jiraNumberField, gbc);
        gbc.gridy++;

        typeComboBox = new ComboBox<>(new String[]{
                "feature - 새로운 기능을 개발했을때 사용합니다.",
                "fix - 버그를 수정했을때 사용합니다.",
                "docs - 코드에 영향을 주지 않는 문서 관련 변경 사항에 사용합니다. 예) docs: README 파일수정",
                "style - 스타일을 변경할때 사용합니다. 기능변화가 없어야합니다. (white-space, formatting, missing semi-colons, etc)",
                "refactor - 코드 리팩토링했을때 사용합니다.",
                "perf - 코드 성능을 향상시키는 코드로 변경했을때 사용합니다",
                "test - 테스트 코드 추가했을떄 혹은 기존 테스트 코드 수정했을때 사용합니다.",
                "build - 빌드 관련 코드 수정했을때 사용합니다 (npm, gradle, maven)",
                "ci - ci 관련 구성파일을 수정했을때 사용합니다 (jenkins github action등)",
                "chore - 기타 변경사항 적용 , 코드나 기능에 큰영향을 미치지않는 업무(빌드 작업변경 패키지업데이트)",
                "revert - 커밋을 이전으로 되돌렸을때 사용합니다"
        });
        formPanel.add(typeComboBox, gbc);
        gbc.gridy++;
        shortDescriptionField = new JBTextField();
        formPanel.add(shortDescriptionField, gbc);
        gbc.gridy++;
        detailedDescriptionArea = new JBTextArea(7, 20);
        formPanel.add(new JScrollPane(detailedDescriptionArea), gbc);
        gbc.gridy++;
        breakingChangeCheckBox = new JBTextArea(3, 20);
        formPanel.add(new JScrollPane(breakingChangeCheckBox), gbc);
        gbc.gridy++;
        closedIssuesField = new JBTextField();
        formPanel.add(closedIssuesField, gbc);
        gbc.gridy++;
        skipCICheckBox = new JCheckBox();
        formPanel.add(skipCICheckBox, gbc);

        dialogPanel.add(formPanel, BorderLayout.CENTER);

        return dialogPanel;
    }

    public String getJiraKeyComboBox() {
        return (String) jiraKeyComboBox.getSelectedItem();
    }

    public void setJiraKeyComboBox(String jiraKey) {
        for (int i = 0; i < typeComboBox.getItemCount(); i++) {
            if (typeComboBox.getItemAt(i).startsWith(jiraKey)) {
                typeComboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    public String getJiraNumberField() {
        return jiraNumberField.getText();
    }

    public void setJiraNumberField(String jiraNumber) {
        jiraNumberField.setText(jiraNumber);
    }

    public String getType() {
        return (String) typeComboBox.getSelectedItem();
    }

    public void setType(String type) {
        for (int i = 0; i < typeComboBox.getItemCount(); i++) {
            if (typeComboBox.getItemAt(i).startsWith(type)) {
                typeComboBox.setSelectedIndex(i);
                break;
            }
        }
    }



    public String getShortDescription() {
        return shortDescriptionField.getText();
    }

    public void setShortDescription(String shortDescription) {
        shortDescriptionField.setText(shortDescription);
    }

    public String getDetailedDescription() {
        return detailedDescriptionArea.getText();
    }

    public void setDetailedDescription(String detailedDescription) {
        detailedDescriptionArea.setText(detailedDescription);
    }

    public String getBreakingChange() {
        return breakingChangeCheckBox.getText();
    }

    public void setBreakingChange(String breakingChange) {
        breakingChangeCheckBox.setText(breakingChange);
    }

    public String getClosedIssues() {
        return closedIssuesField.getText();
    }

    public void setClosedIssues(String closedIssues) {
        closedIssuesField.setText(closedIssues);
    }

    public boolean isSkipCI() {
        return skipCICheckBox.isSelected();
    }

    public void setSkipCI(boolean skipCI) {
        skipCICheckBox.setSelected(skipCI);
    }
}
