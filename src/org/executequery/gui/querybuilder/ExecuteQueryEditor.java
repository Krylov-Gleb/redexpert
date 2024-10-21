package org.executequery.gui.querybuilder;

import org.executequery.GUIUtilities;
import org.executequery.actions.OpenFrameCommand;
import org.executequery.gui.editor.QueryEditor;

import java.awt.event.ActionEvent;

/**
 * A class for creating a query editor.
 *
 * @author Krylov Gleb
 */
public class ExecuteQueryEditor extends OpenFrameCommand {

    // --- Field constant ---
    private final String Query = "SELECT * FROM EMPLOYEE;";

    // --- GUI Components ---
    private QueryEditor queryEditor;

    /**
     * Creating a new QueryEditor
     */
    public ExecuteQueryEditor() {
        execute();
    }

    /**
     * A method for creating a new query editor and creating a new tab in the workspace.
     */
    private void execute() {
        QueryEditor queryEditor = new QueryEditor();
        this.queryEditor = queryEditor;

        GUIUtilities.addCentralPane(QueryEditor.TITLE,
                QueryEditor.FRAME_ICON,
                queryEditor,
                null,
                true);

        queryEditor.setEditorText(Query);
    }

    /**
     * The method for getting the query editor that we are working with.
     *
     * @return QueryEditor
     */
    public QueryEditor getQueryEditor() {
        return queryEditor;
    }

    @Override
    public void execute(ActionEvent e) {

    }
}
