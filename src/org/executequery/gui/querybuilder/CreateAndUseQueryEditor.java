package org.executequery.gui.querybuilder;

import org.executequery.GUIUtilities;
import org.executequery.actions.OpenFrameCommand;
import org.executequery.gui.editor.QueryEditor;

import java.awt.event.ActionEvent;

/**
 * A class for creating a query constructor.
 * <p>
 * Класс для создания конструктора запросов.
 *
 * @author Krylov Gleb
 */
public class CreateAndUseQueryEditor extends OpenFrameCommand {

    // --- Fields ---
    // --- Поля ---

    private final String Query = "";

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private QueryEditor queryEditor;

    /**
     * Creates a new query editor.
     * <p>
     * Создаёт новый редактор запросов.
     */
    public CreateAndUseQueryEditor() {
        execute();
    }

    /**
     * A method for creating and using the query editor.
     * <p>
     * Метод для создания и использования редактора запросов.
     */
    private void execute() {
        QueryEditor queryEditor = new QueryEditor();
        this.queryEditor = queryEditor;
        addQueryEditorInCentralPanel(this.queryEditor);
        queryEditor.setEditorText(Query);
    }

    /**
     * A method for placing the query editor on the central panel.
     * <p>
     * Метод для размещения редактора запросов на центральной панели.
     */
    private static void addQueryEditorInCentralPanel(QueryEditor queryEditor) {
        GUIUtilities.addCentralPane(QueryEditor.TITLE,
                QueryEditor.FRAME_ICON,
                queryEditor,
                null,
                true);
    }

    /**
     * The method for getting the query editor.
     * <p>
     * Метод для получения редактора запросов.
     */
    public QueryEditor getQueryEditor() {
        return queryEditor;
    }

    @Override
    public void execute(ActionEvent e) {}
}
