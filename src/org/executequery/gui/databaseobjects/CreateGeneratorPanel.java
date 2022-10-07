package org.executequery.gui.databaseobjects;

import org.executequery.GUIUtilities;
import org.executequery.databasemediators.DatabaseConnection;
import org.executequery.databaseobjects.DatabaseHost;
import org.executequery.databaseobjects.NamedObject;
import org.executequery.databaseobjects.impl.DefaultDatabaseHost;
import org.executequery.databaseobjects.impl.DefaultDatabaseSequence;
import org.executequery.gui.ActionContainer;
import org.executequery.gui.text.SimpleTextArea;
import org.underworldlabs.swing.NumberTextField;
import org.underworldlabs.util.MiscUtils;
import org.underworldlabs.util.SQLUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class CreateGeneratorPanel extends AbstractCreateObjectPanel {

    public static final String CREATE_TITLE = getCreateTitle(NamedObject.SEQUENCE);
    public static final String ALTER_TITLE = getEditTitle(NamedObject.SEQUENCE);
    private NumberTextField startValueText;
    private NumberTextField incrementText;
    private SimpleTextArea description;
    private JLabel labelIncrement;
    private DefaultDatabaseSequence generator;

    public CreateGeneratorPanel(DatabaseConnection dc, ActionContainer dialog) {
        this(dc, dialog, null);
    }

    public CreateGeneratorPanel(DatabaseConnection dc, ActionContainer dialog, DefaultDatabaseSequence generator) {
        super(dc, dialog, generator);
    }

    protected void initEdited() {
        reset();
        addPrivilegesTab(tabbedPane);
        addDependenciesTab(generator);
        addCreateSqlTab(generator);
    }

    @Override
    public void createObject() {
        try {
            createGenerator();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCreateTitle() {
        return CREATE_TITLE;
    }

    @Override
    public String getEditTitle() {
        return ALTER_TITLE;
    }

    @Override
    public String getTypeObject() {
        return NamedObject.META_TYPES[NamedObject.SEQUENCE];
    }

    @Override
    public void setDatabaseObject(Object databaseObject) {
        generator = (DefaultDatabaseSequence) databaseObject;
    }

    @Override
    public void setParameters(Object[] params) {

    }

    protected void init() {
        startValueText = new NumberTextField();
        startValueText.setValue(0);
        incrementText = new NumberTextField();
        incrementText.setValue(1);
        this.description = new SimpleTextArea();

        centralPanel.setLayout(new GridBagLayout());
        JLabel startLabel = new JLabel(bundleString("start-value"));
        centralPanel.add(startLabel, new GridBagConstraints(0, 0,
                1, 1, 0, 0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
                0, 0));
        centralPanel.add(startValueText, new GridBagConstraints(1, 0,
                1, 1, 1, 0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
                0, 0));

        labelIncrement = new JLabel(bundleString("increment"));
        centralPanel.add(labelIncrement, new GridBagConstraints(0, 1,
                1, 1, 0, 0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
                0, 0));
        centralPanel.add(incrementText, new GridBagConstraints(1, 1,
                1, 1, 1, 0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
                0, 0));
        tabbedPane.add(bundleStaticString("description"), description);
        if (getDatabaseVersion() < 3) {
            labelIncrement.setVisible(false);
            incrementText.setVisible(false);
        }

    }

    int getVersion() throws SQLException {
        DatabaseHost host = new DefaultDatabaseHost(connection);
        return host.getDatabaseMetaData().getDatabaseMajorVersion();
    }

    protected String generateQuery() {
        return SQLUtils.generateCreateSequence(getFormattedName(), startValueText.getLongValue(), incrementText.getLongValue(),
                description.getTextAreaComponent().getText(), getDatabaseVersion(), editing);
    }

    private void createGenerator() throws SQLException {
        if (!MiscUtils.isNull(nameField.getText().trim())) {
            displayExecuteQueryDialog(generateQuery(), ";");
        } else
            GUIUtilities.displayErrorMessage("Name can not be empty");
    }

    protected void reset() {
        nameField.setText(generator.getName().trim());
        nameField.setEnabled(false);
        startValueText.setLongValue(generator.getSequenceValue());
        if (getDatabaseVersion() >= 3)
            incrementText.setValue(generator.getIncrement());
        description.getTextAreaComponent().setText(generator.getRemarks());
    }
}
