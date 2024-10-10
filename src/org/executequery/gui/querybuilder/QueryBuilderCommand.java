package org.executequery.gui.querybuilder;

import org.executequery.GUIUtilities;
import org.executequery.actions.OpenFrameCommand;
import org.executequery.gui.editor.QueryEditor;
import org.underworldlabs.swing.actions.BaseCommand;

import java.awt.event.ActionEvent;

public class QueryBuilderCommand extends OpenFrameCommand implements BaseCommand {

    private void showPanel(QueryBuilderPanel queryBuilderPanel) {

        String title = QueryBuilderPanel.TITLE;
        if (isCentralPaneOpen(title))
            return;

        try {
            GUIUtilities.showWaitCursor();

            GUIUtilities.addCentralPane(
                    title,
                    QueryBuilderPanel.FRAME_ICON,
                    queryBuilderPanel,
                    null, true
            );

        } finally {
            GUIUtilities.showNormalCursor();
        }
    }

    @Override
    public void execute(ActionEvent e) {
        if (isConnected())
            showPanel(new QueryBuilderPanel());
    }

}
