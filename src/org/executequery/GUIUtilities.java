/*
 * GUIUtilities.java
 *
 * Copyright (C) 2002-2017 Takis Diakoumis
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.executequery;

import org.executequery.actions.editcommands.RedoCommand;
import org.executequery.actions.editcommands.UndoCommand;
import org.executequery.base.DesktopMediator;
import org.executequery.base.DockedTabListener;
import org.executequery.base.DockedTabView;
import org.executequery.base.TabComponent;
import org.executequery.components.StatusBarPanel;
import org.executequery.databasemediators.ConnectionMediator;
import org.executequery.databasemediators.DatabaseConnection;
import org.executequery.gui.*;
import org.executequery.gui.browser.ConnectionHistory;
import org.executequery.gui.browser.ConnectionPanel;
import org.executequery.gui.browser.ConnectionsTreePanel;
import org.executequery.gui.editor.QueryEditor;
import org.executequery.gui.editor.history.QueryEditorHistory;
import org.executequery.gui.menu.ExecuteQueryMenu;
import org.executequery.gui.text.TextEditor;
import org.executequery.gui.text.TextEditorContainer;
import org.executequery.http.ReddatabaseAPI;
import org.executequery.io.RecentFileIOListener;
import org.executequery.listeners.*;
import org.executequery.localization.Bundles;
import org.executequery.plaf.LookAndFeelType;
import org.executequery.print.PrintFunction;
import org.executequery.repository.*;
import org.executequery.toolbars.ToolBarManager;
import org.executequery.util.SystemErrLogger;
import org.executequery.util.SystemResources;
import org.executequery.util.ThreadUtils;
import org.executequery.util.UserProperties;
import org.underworldlabs.jdbc.DataSourceException;
import org.underworldlabs.swing.ExceptionErrorDialog;
import org.underworldlabs.swing.GUIUtils;
import org.underworldlabs.swing.actions.ActionBuilder;
import org.underworldlabs.swing.actions.BaseActionCommand;
import org.underworldlabs.swing.plaf.UIUtils;
import org.underworldlabs.swing.toolbar.ToolBarProperties;
import org.underworldlabs.util.MiscUtils;
import org.underworldlabs.util.SystemProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.*;

/**
 * The GUIUtilities is the primary 'controller' class for all
 * in addition to many utility helper methods such as displaying
 * simple dialogs and updating menus.
 *
 * <p>This class will hold a reference to all primary components
 * for access by other classes. This includes those currently in-focus
 * components such as the Query Editor or other text components.
 *
 * <p>All internal frames are added (and closed via relevant 'Close'
 * buttons as may apply) from here.
 *
 * @author Takis Diakoumis
 */

@SuppressWarnings("unused")
public final class GUIUtilities {

    /**
     * The toolbar manager instance
     */
    private static ToolBarManager toolBar;

    /**
     * The window status bar
     */
    private static StatusBarPanel statusBar;

    /**
     * The open dialog in focus
     */
    private static JDialog focusedDialog;

    /**
     * register for all open components - dialogs, tabs etc.
     */
    private static OpenComponentRegister register;

    /**
     * the application frame
     */
    private static JFrame frame;

    /**
     * panel and desktop mediator object
     */
    private static DesktopMediator desktopMediator;

    /**
     * the layout properties controller
     */
    private static UserLayoutProperties layoutProperties;

    /**
     * docked panel cache of non-central pane tabs
     */
    private static final Map<String, JPanel> dockedTabComponents;

    /**
     * System.err logger
     */
    private static final SystemErrLogger errLogger;

    /**
     * System.out logger
     */
    private static final SystemErrLogger outLogger;

    private static SystemOutputPanel systemOutputPanel;

    static {

        dockedTabComponents = new HashMap<>();

        errLogger = new SystemErrLogger(
                SystemProperties.getBooleanProperty("user", "system.log.err"),
                SystemErrLogger.SYSTEM_ERR
        );

        outLogger = new SystemErrLogger(
                SystemProperties.getBooleanProperty("user", "system.log.out"),
                SystemErrLogger.SYSTEM_OUT
        );
    }

    public static void initDesktop(JFrame aFrame) {

        frame = aFrame;

        // create the mediator object
        desktopMediator = new DesktopMediator(frame);

        // initialise and add the status bar
        statusBar = new StatusBarPanel(Bundles.get(ConnectionPanel.class, "status.NotConnected"), Constants.EMPTY);
        statusBar.setFourthLabelText("JDK" + System.getProperty("java.version"), SwingConstants.CENTER);
        displayStatusBar(SystemProperties.getBooleanProperty("user", "system.display.statusbar"));

        frame.add(statusBar, BorderLayout.SOUTH);

        // init the layout properties
        layoutProperties = new UserLayoutProperties();

        EventMediator.registerListener(new DefaultConnectionListener());
        EventMediator.registerListener(new OpenEditorConnectionListener());
        EventMediator.registerListener(new ConnectionRepositoryChangeListener());
        EventMediator.registerListener(new ConnectionFoldersRepositoryChangeListener());
        EventMediator.registerListener(new DefaultUserPreferenceListener());
        EventMediator.registerListener(new RecentFileIOListener());
        EventMediator.registerListener(new ToolBarVisibilityListener());
        EventMediator.registerListener(new PreferencesChangesListener(layoutProperties));
        EventMediator.registerListener(new HttpProxyUserPreferenceListener());
        EventMediator.registerListener(new LogUserPreferenceListener(errLogger, outLogger));
        EventMediator.registerListener(new KeyboardShortcutsUserPreferenceListener());
        EventMediator.registerListener(ConnectionHistory.getInstance());
    }

    public static void initPanels() {

        // init the open component register and set as a listener
        register = new OpenComponentRegister();
        desktopMediator.addDockedTabListener(register);

        // set up the default docked tabs and their positions
        setDockedTabViews(false);

        String startupConnection = SystemProperties.getStringProperty("user", "startup.connection");
        boolean openEditorOnConnect = SystemProperties.getBooleanProperty("user", "editor.open.on-connect");
        String defaultStartupConnection = SystemProperties.getStringProperty("default", "startup.connection");
        if (!Objects.equals(startupConnection, defaultStartupConnection) && !openEditorOnConnect)
            addCentralPane(QueryEditor.TITLE, QueryEditor.FRAME_ICON, new QueryEditor(), null, false);

        // divider locations
        setDividerLocations();

        // add the split pane divider listener
        desktopMediator.addPropertyChangeListener(layoutProperties);
        desktopMediator.addDockedTabDragListener(layoutProperties);
        desktopMediator.addDockedTabListener(layoutProperties);
    }

    public static void loadAuthorisationInfo() {

        if (!MiscUtils.isNull(SystemProperties.getStringProperty("user", "reddatabase.token"))) {

            while (statusBar.getLabel(4).getMouseListeners().length > 0)
                statusBar.getLabel(4).removeMouseListener(statusBar.getLabel(4).getMouseListeners()[0]);

            statusBar.getLabel(4).setText(" user:" + SystemProperties.getStringProperty("user", "reddatabase.user"));
            statusBar.getLabel(4).addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (GUIUtilities.displayConfirmDialog(bundledString("want-exit")) == JOptionPane.YES_OPTION) {
                        SystemProperties.setStringProperty("user", "reddatabase.token", "");
                        statusBar.getLabel(4).removeMouseListener(this);
                        loadAuthorisationInfo();
                    }
                }
            });

        } else {
            while (statusBar.getLabel(4).getMouseListeners().length > 0)
                statusBar.getLabel(4).removeMouseListener(statusBar.getLabel(4).getMouseListeners()[0]);

            statusBar.getLabel(4).setText("  " + bundledString("notAuthorized"));
            statusBar.getLabel(4).addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    ReddatabaseAPI.getToken();
                }
            });

        }
    }

    /**
     * Sets the divider locations to previously saved (or default) values.
     */
    private static void setDividerLocations() {

        for (String key : DesktopMediator.DIVIDER_LOCATION_KEYS) {
            int location = SystemProperties.getIntProperty("user", key);
            if (location > 0)
                desktopMediator.setSplitPaneDividerLocation(key, location);
        }
    }

    /**
     * Removes the specified docked tab listener.
     */
    public void removeDockedTabListener(DockedTabListener listener) {
        desktopMediator.removeDockedTabListener(listener);
    }

    /**
     * Adds the specified docked tab listener.
     */
    public void addDockedTabListener(DockedTabListener listener) {
        desktopMediator.addDockedTabListener(listener);
    }

    /**
     * Adds the specified component as a docked tab component
     * in the specified position.
     */
    public static void addCentralPane(String title, Icon icon, Component component, String tip, boolean selected) {
        addDockedTab(title, icon, component, tip, SwingConstants.CENTER, selected);
    }

    /**
     * Adds the specified component as a docked tab component
     * in the specified position.
     */
    public static void addCentralPane(String title, String icon, Component component, String tip, boolean selected) {
        addDockedTab(title, IconManager.getIcon(icon), component, tip, SwingConstants.CENTER, selected);
    }

    /**
     * Adds the specified component as a docked tab component
     * in the specified position.
     */
    public static void addDockedTab(String title, Component component, int position, boolean selected) {
        addDockedTab(title, null, component, null, position, selected);
    }


    /**
     * Adds the specified component as a docked tab component
     * in the specified position.
     */
    public static void addDockedTab(String title, Icon icon, Component component, int position, boolean selected) {
        addDockedTab(title, icon, component, null, position, selected);
    }

    /**
     * Adds the specified component as a docked tab component
     * in the specified position.
     */
    public static void addDockedTab(
            String title, Icon icon, Component component, String tip, int position, boolean selected) {

        // change the title if a save function
        if (component instanceof NamedView) {

            NamedView namedViewComponent = (NamedView) component;
            String displayName = namedViewComponent.getDisplayName();

            if (!displayName.isEmpty()) {
                title = displayName;
                tip = displayName;
            }
        }

        // if this is a main window component, add to cache
        if (position == SwingConstants.CENTER)
            register.addOpenPanel(title, component);

        desktopMediator.addDockedTab(title, icon, component, tip, position, selected);
        GUIUtils.scheduleGC();
    }

    public static void closeSelectedCentralPane() {

        TabComponent tabComponent = desktopMediator.getSelectedComponent(SwingConstants.CENTER);
        if (tabComponent != null)
            closeDockedComponent(tabComponent.getTitle(), SwingConstants.CENTER);
    }

    public static void closeSelectedTab() {
        desktopMediator.closeSelectedTab();
    }

    public static void closeTab(String name) {
        closeDockedComponent(name, SwingConstants.CENTER);
    }


    /**
     * Closed the specified docked component with name at the specified position.
     *
     * @param name     name of the tab component
     * @param position position of the tab component
     */
    public static void closeDockedComponent(String name, int position) {
        desktopMediator.closeTabComponent(name, position);
    }

    // -------------------------------------------------------

    /**
     * Retrieves the parent frame of the application.
     *
     * @return the parent frame
     */
    public static Frame getParentFrame() {
        return frame;
    }

    public static Component getInFocusDialogOrWindow() {

        if (getFocusedDialog() != null) {
            return getFocusedDialog();

        } else if (register != null && register.getOpenDialogCount() > 0) {

            for (JDialog dialog : register.getOpenDialogs())
                if (dialog.isFocused())
                    return dialog;
        }

        return getParentFrame();
    }

    public static ExecuteQueryMenu getExecuteQueryMenu() {
        return (ExecuteQueryMenu) frame.getJMenuBar();
    }

    /**
     * Selects the next tab from the current selection.
     */
    public static void selectNextTab() {
        desktopMediator.selectNextTab();
    }

    /**
     * Selects the previous tab from the current selection.
     */
    public static void selectPreviousTab() {
        desktopMediator.selectPreviousTab();
    }

    /**
     * Builds and sets the main toolbar.
     */
    public static void createToolBar() {
        toolBar = new ToolBarManager();
        frame.add(toolBar.getToolBarBasePanel(), BorderLayout.NORTH);
    }

    /**
     * <p>Determines whether upon selection of the print
     * action, the currently open and in focus frame does
     * have a printable area - is an instance of a <code>
     * BrowserPanel</code> or <code>TextEditor</code>.
     *
     * @return whether printing may be performed from the
     * open frame
     */
    public static boolean canPrint() {

        if (focusedDialog instanceof PrintFunction)
            return ((PrintFunction) focusedDialog).canPrint();

        Object object = getSelectedCentralPane();
        if (!(object instanceof PrintFunction))
            return false;

        return ((PrintFunction) object).canPrint();
    }

    /**
     * Returns the <code>PrintFunction</code> object
     * from the currently in-focus frame. If the in-focus
     * frame is not an instance of <code>PrintFunction</code>,
     * <code>null</code> is returned.
     *
     * @return the in-focus <code>PrintFunction</code> object
     */
    public static PrintFunction getPrintableInFocus() {
        // check the open dialogs first
        if (register.getOpenDialogCount() > 0) {

            for (JDialog dialog : register.getOpenDialogs()) {
                if (!dialog.isModal() || dialog.isFocused()) {

                    if (dialog instanceof BaseDialog) {

                        // check the content panel
                        JPanel panel = ((BaseDialog) dialog).getContentPanel();
                        if (panel instanceof PrintFunction)
                            return (PrintFunction) panel;

                    } else if (dialog instanceof PrintFunction)
                        return (PrintFunction) dialog;
                }
            }
        }

        // check the open panels register
        if (register.getOpenPanelCount() > 0) {

            Component component = register.getSelectedComponent();
            if (component instanceof PrintFunction)
                return (PrintFunction) component;
        }

        return null;
    }

    /**
     * Sets the selected tab in the central pane as the tab
     * component with the specified name.
     *
     * @param name name of the tab to be selected in the central pane
     */
    public static void setSelectedCentralPane(String name) {
        desktopMediator.setSelectedPane(SwingConstants.CENTER, name);
    }

    public static JPanel getCentralPane(String name) {
        return (JPanel) register.getOpenPanel(name);
    }

    /**
     * Returns the tab component with the specified name at
     * the specified position within the tab structure.
     *
     * @param position position (SwingConstants)
     * @param name     panel name/title
     */
    public static TabComponent getTabComponent(int position, String name) {
        return desktopMediator.getTabComponent(position, name);
    }

    /**
     * Registers the specified dialog with the cache.
     *
     * @param dialog dialog to be registered
     */
    public static void registerDialog(JDialog dialog) {
        register.addDialog(dialog);
    }

    /**
     * Registers the specified dialog with the cache.
     *
     * @param dialog dialog to be registered
     */
    public static void deregisterDialog(JDialog dialog) {
        register.removeDialog(dialog);
    }

    public static void setFocusedDialog(JDialog focusedDialog) {
        GUIUtilities.focusedDialog = focusedDialog;
    }

    public static JDialog getFocusedDialog() {
        return focusedDialog;
    }

    public static void removeFocusedDialog(JDialog focusedDialog) {
        if (GUIUtilities.focusedDialog == focusedDialog)
            GUIUtilities.focusedDialog = null;
    }

    /**
     * Retrieves the <code>TextEditor</code> instance
     * that currently has focus or NULL if none exists.
     *
     * @return that instance of <code>TeTextEditor</code>
     */
    public static TextEditor getTextEditorInFocus() {

        if (register.getOpenDialogCount() > 0) {

            for (JDialog dialog : register.getOpenDialogs()) {
                if (!dialog.isModal() || dialog.isFocused()) {

                    if (dialog instanceof BaseDialog) {

                        JPanel panel = ((BaseDialog) dialog).getContentPanel();
                        if (panel instanceof TextEditor)
                            return (TextEditor) panel;
                        else if (panel instanceof TextEditorContainer)
                            return ((TextEditorContainer) panel).getTextEditor();

                    } else if (dialog instanceof TextEditor) {
                        return (TextEditor) dialog;

                    } else if (dialog instanceof TextEditorContainer)
                        return ((TextEditorContainer) dialog).getTextEditor();
                }
            }
        }

        // check the open panels register
        if (register.getOpenPanelCount() > 0) {
            Component component = register.getSelectedComponent();
            if (component instanceof TextEditor) {
                return (TextEditor) component;
            } else if (component instanceof TextEditorContainer) {
                return ((TextEditorContainer) component).getTextEditor();
            }

        }
        return null;
    }

    /**
     * Retrieves the contents of the in-focus
     * internal frame as a <code>JPanel</code>.
     *
     * @return the panel in focus
     */
    public static JPanel getSelectedCentralPane() {
        return (JPanel) register.getSelectedComponent();
    }

    /**
     * Retrieves the <code>SaveFunction</code> in focus.
     *
     * @return the <code>SaveFunction</code> in focus
     */
    public static SaveFunction getSaveFunctionInFocus() {

        // check the open dialogs first
        if (register.getOpenDialogCount() > 0) {

            for (JDialog dialog : register.getOpenDialogs()) {
                if (!dialog.isModal() || dialog.isFocused()) {

                    if (dialog instanceof BaseDialog) {

                        // check the content panel
                        JPanel panel = ((BaseDialog) dialog).getContentPanel();
                        if (panel instanceof SaveFunction)
                            return (SaveFunction) panel;

                    } else if (dialog instanceof SaveFunction)
                        return (SaveFunction) dialog;
                }
            }
        }

        // check the open panels register
        if (register.getOpenPanelCount() > 0) {
            Component component = register.getSelectedComponent();
            if (component instanceof SaveFunction)
                return (SaveFunction) component;
        }

        return null;
    }

    public static UndoableComponent getUndoableInFocus() {

        if (register.getOpenDialogCount() > 0) {

            for (JDialog dialog : register.getOpenDialogs())
                if (dialog.isFocused() && dialog instanceof UndoableComponent)
                    return (UndoableComponent) dialog;

            // check the open panels register
            if (register.getOpenPanelCount() > 0) {
                Component component = register.getSelectedComponent();
                if (component instanceof UndoableComponent)
                    return (UndoableComponent) component;
            }
        }

        return null;
    }

    public static void registerUndoRedoComponent(UndoableComponent undoable) {

        BaseActionCommand undo = (BaseActionCommand) ActionBuilder.get("undo-command");
        BaseActionCommand redo = (BaseActionCommand) ActionBuilder.get("redo-command");

        UndoCommand _undo = (UndoCommand) undo.getCommand();
        RedoCommand _redo = (RedoCommand) redo.getCommand();

        _undo.setUndoableComponent(undoable);
        _redo.setUndoableComponent(undoable);
    }

    /**
     * Retrieves the applications <code>InputMap</code>.
     *
     * @return the <code>InputMap</code>
     */
    public static InputMap getInputMap(int condition) {
        return desktopMediator.getInputMap(condition);
    }

    /**
     * Retrieves the applications <code>ActionMap</code>.
     *
     * @return the <code>ActionMap</code>
     */
    public static ActionMap getActionMap() {
        return desktopMediator.getActionMap();
    }

    /**
     * Initialises and starts the system logger.
     * The logger's stream is also registered for
     * <code>System.err</code> and <code>System.out</code>.
     */
    public static void startLogger() {

        systemOutputPanel = new SystemOutputPanel();
        dockedTabComponents.put(SystemOutputPanel.PROPERTY_KEY, systemOutputPanel);

        // set system error stream to the output panel
        PrintStream errStream = new PrintStream(errLogger, true);
        System.setErr(errStream);

        // set system error stream to the output panel
        PrintStream outStream = new PrintStream(outLogger, true);
        System.setOut(outStream);
    }

    public static void clearSystemOutputPanel() {
        systemOutputPanel.clear();
    }

    /**
     * <p>Calculates and returns the centered position
     * of a dialog with the specified size to be added
     * to the desktop area - i.e. taking into account the
     * size and location of all docked panels.
     *
     * @param dialogDim size of the dialog to be added as a
     *                  <code>Dimension</code> object
     * @return the <code>Point</code> at which to add the dialog
     */
    public static Point getLocationForDialog(Dimension dialogDim) {
        return GUIUtils.getPointToCenter(frame, dialogDim);
    }

    public static void copyToClipBoard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipBoard.setContents(stringSelection, stringSelection);
    }

    /**
     * Propagates the call to GUIUtils and schedules
     * the garbage collector to run.
     */
    public static void scheduleGC() {
        GUIUtils.scheduleGC();
    }

    /**
     * Returns whether the frame's glass pane is visible or not.
     *
     * @return true | false
     */
    public static boolean isGlassPaneVisible() {
        return frame.getRootPane().getGlassPane().isVisible();
    }

    /**
     * Shows/hides the frame's glass pane as specified.
     *
     * @param visible - true | false
     */
    public static void setGlassPaneVisible(final boolean visible) {

        ThreadUtils.invokeLater(() -> {
            if (isGlassPaneVisible() == visible)
                return;
            frame.getRootPane().getGlassPane().setVisible(visible);
        });
    }

    /**
     * Sets the application cursor to the system normal cursor
     */
    public static void showNormalCursor() {
        ThreadUtils.invokeAndWait(() -> GUIUtils.showNormalCursor(frame));
    }

    /**
     * Sets the application cursor to the system wait cursor
     */
    public static void showWaitCursor() {
        ThreadUtils.invokeAndWait(() -> GUIUtils.showWaitCursor(frame));
    }

    /**
     * Sets the application cursor to the system hand cursor
     */
    public static void showHandCursor() {
        ThreadUtils.invokeAndWait(() -> GUIUtils.showHandCursor(frame));
    }

    /**
     * Sets the application cursor to the system hand cursor
     */
    public static void showTextCursor() {
        ThreadUtils.invokeAndWait(() -> GUIUtils.showTextCursor(frame));
    }

    public static void showChangeSizeCursor(int location) {
        ThreadUtils.invokeAndWait(() -> GUIUtils.showChangeSizeCursor(frame, location));
    }

    /**
     * Sets the application cursor to the system wait cursor
     * on the specified component.
     */
    public static void showWaitCursor(final Component component) {
        ThreadUtils.invokeAndWait(() -> GUIUtils.showWaitCursor(component));
    }

    /**
     * Sets the application cursor to the system normal cursor
     * on the specified component.
     */
    public static void showNormalCursor(final Component component) {
        ThreadUtils.invokeAndWait(() -> GUIUtils.showNormalCursor(component));
    }

    /**
     * Sets the application cursor to the system hand cursor
     * on the specified component.
     */
    public static void showHandCursor(final Component component) {
        ThreadUtils.invokeAndWait(() -> GUIUtils.showHandCursor(component));
    }

    /**
     * Sets the application cursor to the system text cursor
     * on the specified component.
     */
    public static void showTextCursor(final Component component) {
        ThreadUtils.invokeAndWait(() -> GUIUtils.showTextCursor(component));
    }

    /**
     * Resets the toolbars.
     */
    public static void resetToolBar() {

        ThreadUtils.invokeLater(() -> {
            toolBar.buildToolbars(true);
            ToolBarProperties.saveTools();
        });
    }

    /**
     * Convenience method for consistent border colour.
     *
     * @return the system default border colour
     */
    public static Color getDefaultBorderColour() {
        return UIUtils.getDefaultBorderColour();
    }

    /**
     * Returns the docked component (non-central pane) with
     * the specified name.
     *
     * @param key name of the component
     * @return the panel component
     */
    public static JPanel getDockedTabComponent(String key) {

        if (dockedTabComponents == null || dockedTabComponents.isEmpty() || !dockedTabComponents.containsKey(key))
            return null;

        return dockedTabComponents.get(key);
    }

    /**
     * Initialises the docked tab view with the specified
     * property key.
     *
     * @param key property key of the panel to be initialised
     */
    private static void initDockedTabView(String key) {

        if (dockedTabComponents.containsKey(key))
            return;

        JPanel panel = null;
        switch (key) {

            case ConnectionsTreePanel.PROPERTY_KEY:
                panel = new ConnectionsTreePanel();
                break;

            case SystemOutputPanel.PROPERTY_KEY:
                startLogger();
                break;
        }

        if (panel != null)
            dockedTabComponents.put(key, panel);
    }

    /**
     * Ensures the docked tab with the specified key is visible.
     *
     * @param key the property key of the component
     */
    public static void ensureDockedTabVisible(String key) {

        JPanel panel = getDockedTabComponent(key);
        if (panel instanceof DockedTabView) {

            String title = ((DockedTabView) panel).getTitle();
            int position = getDockedComponentPosition(key);

            if (getTabComponent(position, title) == null) {

                if (desktopMediator.isMinimised(position, title))
                    desktopMediator.restore(position, title);
                else
                    addDockedTab(title, panel, position, true);

            } else
                desktopMediator.setSelectedPane(position, title);

        } else {
            initDockedTabView(key);
            ensureDockedTabVisible(key);
        }
    }

    /**
     * Returns the user specified (or default) position for the
     * non-central pane docked component with the specified name.
     *
     * @return the position (SwingConstants)
     */
    public static int getDockedComponentPosition(String key) {

        int position = layoutProperties.getPosition(key);
        if (position == -1)
            position = SwingConstants.NORTH_WEST;

        return position;
    }

    /**
     * Displays or hides the docked tab component of the specified type.
     *
     * @param key     property key of the component
     * @param visible show/hide the view
     */
    public static void displayDockedComponent(String key, boolean visible) {

        setDockedComponentVisible(key, visible);

        layoutProperties.setDockedPaneVisible(key, visible);
        layoutProperties.save();

        SystemProperties.setBooleanProperty(Constants.USER_PROPERTIES_KEY, key, visible);
        updatePreferencesToFile();
    }

    public static void updatePreference(String key, boolean value) {
        SystemProperties.setBooleanProperty(Constants.USER_PROPERTIES_KEY, key, value);
        updatePreferencesToFile();
    }

    private static void setDockedComponentVisible(String key, boolean visible) {

        if (visible)
            ensureDockedTabVisible(key);
        else
            hideDockedComponent(key);
    }

    /**
     * Displays the docked tab component of the specified type.
     *
     * @param key property key of the component
     */
    public static void hideDockedComponent(String key) {

        JPanel panel = getDockedTabComponent(key);
        if (panel instanceof DockedTabView)
            closeDockedComponent(((DockedTabView) panel).getTitle(), getDockedComponentPosition(key));
    }

    /**
     * Closes the dialog with the specified title.
     */
    public static void closeDialog(String title) {

        if (register.getOpenDialogCount() > 0) {
            for (JDialog dialog : register.getOpenDialogs()) {
                if (dialog.getTitle().startsWith(title)) {
                    dialog.dispose();
                    return;
                }
            }
        }
    }

    /**
     * Closes the currently in-focus dialog.
     */
    public static void closeSelectedDialog() {

        if (register.getOpenDialogCount() > 0) {
            for (JDialog dialog : register.getOpenDialogs()) {
                if (dialog.isFocused()) {
                    dialog.dispose();
                    return;
                }
            }
        }
    }

    /**
     * Displays or hides the main application status bar.
     *
     * @param display <code>true</code> to display | <code>false</code> to hide
     */
    public static void displayStatusBar(boolean display) {
        statusBar.setVisible(display);
        SystemProperties.setBooleanProperty("user", "system.display.statusbar", display);
    }

    /**
     * Retrieves the main frame's layered pane object.
     *
     * @return the frame's <code>JLayeredPane</code>
     */
    public static JLayeredPane getFrameLayeredPane() {
        return ((JFrame) getParentFrame()).getLayeredPane();
    }

    /**
     * Retrieves the application's status bar as
     * registered with this class.
     *
     * @return the application status bar
     */
    public static StatusBarPanel getStatusBar() {
        return statusBar;
    }

    /**
     * Returns the current look and feel value.
     */
    public static LookAndFeelType getLookAndFeel() {
        return LookAndFeelType.valueOf(UserProperties.getInstance().getStringProperty("startup.display.lookandfeel"));
    }

    /**
     * Saves the user preferences to file.
     */
    public static void updatePreferencesToFile() {
        GUIUtils.startWorker(() -> SystemResources.setUserPreferences(SystemProperties.getProperties(Constants.USER_PROPERTIES_KEY)));
    }


    /**
     * Sets the docked tab views according to user preference.
     */
    public static void setDockedTabViews(boolean reload) {

        for (UserLayoutObject object : layoutProperties.getLayoutObjectsSorted()) {

            String key = object.getKey();
            if (object.isVisible()) {

                initDockedTabView(key);

                JPanel panel = getDockedTabComponent(key);
                String title = panel != null ? ((DockedTabView) panel).getTitle() : null;
                int position = object.getPosition();

                if (desktopMediator.getTabComponent(position, title) == null) {
                    addDockedTab(title, panel, position, false);
                    if (object.isMinimised())
                        desktopMediator.minimiseDockedTab(position, title);
                }

            } else if (reload)
                setDockedComponentVisible(key, false);
        }
    }

    /**
     * Retrieves a list of the open central panels that implement
     * SaveFunction.
     *
     * @return the open SaveFunction panels
     */
    public static List<SaveFunction> getOpenSaveFunctionPanels() {

        List<SaveFunction> saveFunctions = new ArrayList<>();

        for (ComponentPanel panel : register.getOpenPanels()) {

            Component component = panel.getComponent();
            if (component instanceof SaveFunction) {

                SaveFunction saveFunction = (SaveFunction) component;
                if (saveFunction.contentCanBeSaved())
                    saveFunctions.add(saveFunction);
            }
        }

        return saveFunctions;
    }

    public static List<ComponentPanel> getOpenPanels() {
        return register.getOpenPanels();
    }

    /**
     * Retrieves the number of open central panels that implement
     * SaveFunction.
     *
     * @return the open SaveFunction panels count
     */
    public static int getOpenSaveFunctionCount() {

        int count = 0;
        for (ComponentPanel panel : register.getOpenPanels())
            if (panel.getComponent() instanceof SaveFunction)
                count++;

        return count;
    }

    public static void closeSelectedConnection() {

        ConnectionsTreePanel panel = (ConnectionsTreePanel) getDockedTabComponent(ConnectionsTreePanel.PROPERTY_KEY);
        if (panel != null) {

            DatabaseConnection dc = panel.getSelectedDatabaseConnection();
            if (dc != null && dc.isConnected()) {

                try {
                    panel.setSelectedConnection(dc);
                    ConnectionMediator.getInstance().disconnect(dc);

                } catch (DataSourceException e) {
                    displayErrorMessage("Error disconnecting selected data source:\n" + e.getMessage());
                }
            }
        }
    }

    /**
     * Sets the title for the specified component to the newTitle
     * within central tab pane.
     *
     * @param panel component to be renamed
     * @param title new title to set
     */
    public static void setTabTitleForComponent(JPanel panel, String title) {
        setTabTitleForComponent(SwingUtilities.CENTER, panel, title);
    }

    /**
     * Sets the tool tip for the specified component to toolTipText.
     *
     * @param position    tab pane position
     * @param component   component where the tool tip should be set
     * @param toolTipText tool tip text to be displayed in the tab
     */
    public static void setToolTipTextForComponent(int position, Component component, String toolTipText) {
        desktopMediator.setToolTipTextForComponent(position, component, toolTipText);
    }

    /**
     * Sets the title for the specified component to toolTipText.
     *
     * @param position  tab pane position
     * @param component component where the tool tip should be set
     * @param title     title to be displayed in the tab
     */
    public static void setTabTitleForComponent(int position, Component component, String title) {
        desktopMediator.setTabTitleForComponent(position, component, title);
    }

    /**
     * Sets the tool tip for the specified component within the
     * central main pane to title.
     *
     * @param component   component where the tool tip should be set
     * @param toolTipText tool tip text to be displayed in the tab
     */
    public static void setToolTipTextForComponent(Component component, String toolTipText) {
        setToolTipTextForComponent(SwingConstants.CENTER, component, toolTipText);
    }

    /**
     * Sets the title for the specified component within the
     * central main pane to toolTipText.
     *
     * @param component component where the tool tip should be set
     * @param title     title text to be displayed in the tab
     */
    public static void setTabTitleForComponent(Component component, String title) {
        setTabTitleForComponent(SwingConstants.CENTER, component, title);
    }

    /**
     * Attempts to locate the actionable dialog that is
     * currently open and brings it to the front.
     */
    public static void actionableDialogToFront() {

        if (register.getOpenDialogCount() > 0) {

            for (JDialog dialog : register.getOpenDialogs()) {
                if (dialog instanceof BaseDialog) {

                    JPanel panel = ((BaseDialog) dialog).getContentPanel();
                    if (panel instanceof ActiveComponent)
                        dialog.toFront();

                } else if (dialog instanceof ActiveComponent)
                    dialog.toFront();
            }
        }
    }

    /**
     * Checks if an actionable dialog is currently open.
     *
     * @return true | false
     */
    public static boolean isActionableDialogOpen() {

        if (register.getOpenDialogCount() > 0) {

            for (JDialog dialog : register.getOpenDialogs()) {
                if (dialog instanceof BaseDialog) {

                    JPanel panel = ((BaseDialog) dialog).getContentPanel();
                    if (panel instanceof ActiveComponent)
                        return true;

                } else if (dialog instanceof ActiveComponent)
                    return true;
            }
        }

        return false;
    }

    public static boolean hasValidSaveFunction() {

        // check the open panels register first
        if (register.getOpenPanelCount() > 0) {
            for (ComponentPanel componentPanel : register.getOpenPanels()) {

                Component component = componentPanel.getComponent();
                if (component instanceof SaveFunction) {

                    SaveFunction saveFunction = (SaveFunction) component;
                    if (saveFunction.contentCanBeSaved())
                        return true;
                }
            }
        }

        // check the open dialogs
        if (register.getOpenDialogCount() > 0) {
            for (JDialog dialog : register.getOpenDialogs()) {

                if (dialog instanceof SaveFunction) {

                    SaveFunction saveFunction = (SaveFunction) dialog;
                    if (saveFunction.contentCanBeSaved())
                        return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if the panel with the specified title is open.
     */
    public static boolean isPanelOpen(String title) {
        return register.isPanelOpen(title);
    }

    /**
     * Checks if the dialog with the specified title is open.
     */
    public static boolean isDialogOpen(String title) {
        return register.isDialogOpen(title);
    }

    /**
     * Checks if the dialog with the specified title is open.
     */
    public static void setSelectedDialog(String title) {

        JDialog dialog = register.getOpenDialog(title);
        if (dialog != null)
            dialog.toFront();
    }

    public static JPanel getOpenFrame(String title) {
        return (JPanel) register.getOpenPanel(title);
    }

    public static boolean saveOpenChanges(SaveFunction saveFunction) {

        int result = displayConfirmCancelDialog(Bundles.getCommon("save-changes.message", saveFunction.getDisplayName()));
        if (result == JOptionPane.YES_OPTION) {

            boolean saveAs = false;
            if (saveFunction instanceof QueryEditor)
                saveAs = QueryEditorHistory.isDefaultDirectory((QueryEditor) saveFunction);

            return saveFunction.save(saveAs) == SaveFunction.SAVE_COMPLETE;

        } else
            return result != JOptionPane.CANCEL_OPTION;
    }


    /**
     * Displays the error dialog displaying the stack trace from a
     * throws/caught exception.
     *
     * @param message     the error message to display
     * @param e           the throwable
     * @param sourceClass the class that call method
     */
    public static void displayExceptionErrorDialog(final String message, final Throwable e, final Class<?> sourceClass) {
        GUIUtils.invokeAndWait(() -> new ExceptionErrorDialog(frame, message, e, sourceClass));
    }

    // -------------------------------------------------------
    // ------ Helper methods for various option dialogs ------
    // -------------------------------------------------------

    // These have been revised to use JDialog as the wrapper to
    // ensure the dialog is centered within the dektop pane and not
    // within the entire screen as you get with JOptionPane.showXXX()

    public static void displayInformationMessage(Object message) {
        GUIUtils.displayInformationMessage(getInFocusDialogOrWindow(), message);
    }

    public static void displayWarningMessage(Object message) {
        GUIUtils.displayWarningMessage(getInFocusDialogOrWindow(), message);
    }

    public static void displayErrorMessage(Object message) {
        GUIUtils.displayErrorMessage(getInFocusDialogOrWindow(), message);
    }

    public static String displayInputMessage(String title, Object message) {
        return GUIUtils.displayInputMessage(getInFocusDialogOrWindow(), title, message);
    }

    public static int displayConfirmCancelErrorMessage(Object message) {
        return GUIUtils.displayConfirmCancelErrorMessage(getInFocusDialogOrWindow(), message);
    }

    public static int displayYesNoDialog() {
        return displayYesNoDialog(Bundles.getCommon("file.override"), Bundles.getCommon("confirmation"));
    }

    public static int displayYesNoDialog(Object message, String title) {
        return GUIUtils.displayYesNoDialog(getInFocusDialogOrWindow(), message, title);
    }

    public static int displayYesNoCancelDialog(Object message, String title) {
        return GUIUtils.displayYesNoCancelDialog(getInFocusDialogOrWindow(), message, title);
    }

    public static int displayConfirmCancelDialog(Object message) {
        return GUIUtils.displayConfirmCancelDialog(getInFocusDialogOrWindow(), message);
    }

    public static int displayConfirmDialog(Object message) {
        return GUIUtils.displayConfirmDialog(getInFocusDialogOrWindow(), message);
    }

    public static void paintImmediatelyLater(final JComponent c) {

        Runnable update = () -> {
            c.repaint();
            Dimension dim = c.getSize();
            c.paintImmediately(0, 0, dim.width, dim.height);
        };
        SwingUtilities.invokeLater(update);
    }

    public static void paintImmediatelyAndWait(final JComponent c) {

        Runnable update = () -> {
            c.repaint();
            Dimension dim = c.getSize();
            c.paintImmediately(0, 0, dim.width, dim.height);
        };

        try {
            SwingUtilities.invokeAndWait(update);
        } catch (InvocationTargetException | InterruptedException e) {
            e.printStackTrace(System.out);
        }
    }

    public static ToolBarManager getToolBar() {
        return toolBar;
    }

    public static String bundledString(String key) {
        return Bundles.get(GUIUtilities.class, key);
    }

}
