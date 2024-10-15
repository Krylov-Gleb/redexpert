package org.executequery.gui.querybuilder;

import org.executequery.GUIUtilities;
import org.executequery.actions.OpenFrameCommand;
import org.executequery.gui.editor.QueryEditor;
import org.executequery.gui.querybuilder.WorkQuryEditor.CreateStringQuery;

import java.awt.event.ActionEvent;

/**
 * The ExecuteQueryEditor
 *
 * @author Krylov Gleb
 */

public class ExecuteQueryEditor extends OpenFrameCommand {

    /**
     * A method that creates a panel (QueryEditor) for displaying and using created (QueryBuilder) queries
     *
     * @param createStringQuery Passing the query that created the QueryBuilder
     */
    public ExecuteQueryEditor(CreateStringQuery createStringQuery) {
        execute(createStringQuery);
    }

    private QueryEditor execute(CreateStringQuery createStringQuery) {
        QueryEditor queryEditor = new QueryEditor();

        GUIUtilities.addCentralPane(QueryEditor.TITLE,
                QueryEditor.FRAME_ICON,
                queryEditor,
                null,
                true);

        queryEditor.setEditorText(createStringQuery.getQuery());
        return queryEditor;
    }

    @Override
    public void execute(ActionEvent e) {

    }
}
