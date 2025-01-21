package org.executequery.gui.querybuilder;

import org.executequery.GUIUtilities;
import org.executequery.actions.OpenFrameCommand;
import org.underworldlabs.swing.actions.BaseCommand;

import java.awt.event.ActionEvent;

public class QBLaunch extends OpenFrameCommand implements BaseCommand {

    private void showPanel(QBPanel queryBuilderPanel) {

        String title = QBPanel.TITLE;
        if (isCentralPaneOpen(title))
            return;

        try {
            GUIUtilities.showWaitCursor();

            GUIUtilities.addCentralPane(
                    title,
                    QBPanel.FRAME_ICON,
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
            showPanel(new QBPanel());
    }

}
