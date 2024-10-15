package org.executequery.gui.querybuilder.WorkQuryEditor;

import org.executequery.gui.querybuilder.QueryBuilderPanel;

import java.awt.*;
import java.util.ArrayList;

/**
 * The CreateStringQuery
 *
 * @author Krylov Gleb
 */

public class CreateStringQuery {

    // --- Constants ---

    private final String SELECT = "SELECT";
    private final String FROM = "FROM";

    // --- Other fields ---

    private String ATTRIBUTE;
    private String TABLES;
    private QueryBuilderPanel queryBuilderPanel;
    private Component[] componentsInInputPanel;

    // --- Designer ---

    public CreateStringQuery(QueryBuilderPanel queryBuilderPanel) {
        this.queryBuilderPanel = queryBuilderPanel;
        init();
    }

    /**
     * Method for initialization
     */
    private void init() {
        ATTRIBUTE = "*";
        TABLES = "EMPLOYEE";
    }

    /**
     * A method that returns a composite request
     *
     * @return String (Query)
     */
    public String getQuery() {
        return SELECT + " " + ATTRIBUTE + " " + FROM + " " + TABLES;
    }

    /**
     * The method for setting the table in the query
     *
     * @param NameTable Replacement table
     */
    public void setTables(String NameTable) {
        TABLES = NameTable;
    }

    /**
     * Method for setting attributes in a request
     *
     * @param Array List of attributes
     */
    public void setAttribute(ArrayList<String> Array) {
        String Attributes = "";
        for (int i = 0; i < Array.size(); i++) {
            if (i != Array.size() - 1) {
                Attributes = Attributes + Array.get(i) + ",";
            } else {
                Attributes = Attributes + Array.get(i);
            }
        }
        ATTRIBUTE = Attributes;
    }
}
