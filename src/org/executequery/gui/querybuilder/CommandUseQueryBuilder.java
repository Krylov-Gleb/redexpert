package org.executequery.gui.querybuilder;

import org.executequery.GUIUtilities;
import org.executequery.actions.OpenFrameCommand;
import org.underworldlabs.swing.actions.BaseCommand;

import java.awt.event.ActionEvent;

/**
 * A class to start using the query builder.
 * <p>
 * Класс для начала использования построителя запросов.
 *
 * @author Krylov Gleb
 */
public class CommandUseQueryBuilder extends OpenFrameCommand implements BaseCommand {

    /**
     * The method that adjusts the display and shows the panel (QueryBuilderPanel)
     *
     * @param queryBuilderPanel
     */
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
