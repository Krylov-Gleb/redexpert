package org.executequery.gui.querybuilder.WorkQuryEditor;

import javax.swing.*;
import java.util.ArrayList;

/**
 * A class for dynamically composing a query that the user will use. (query constructor)
 *
 * @author Krylov Gleb
 */
public class CreateStringQuery {

    // --- Constant field ---
    private final String SELECT = "SELECT";
    private final String FROM = "FROM";
    private final String ON = "ON";
    private final String EMPTINESS = "";
    private final String WHITESPACE = " ";
    private final String POINT = ".";
    private final String EQUALS = "=";
    private final String COMMA = ",";
    private final String STAR = "*";

    // --- Other field ---
    private String first = "";
    private String skip = "";
    private String distinct = "ALL";
    private String join = "";
    private String table = "EMPLOYEE";
    private String attribute = "";

    /**
     * A method for adding the first value to the query.
     *
     * @param number
     */
    public void addFirst(String number) {
        if (!number.isEmpty()) {
            first = "FIRST " + number;
        } else {
            first = EMPTINESS;
        }
    }

    /**
     * A method for clearing (deleting) the first value from the query.
     */
    public void clearFirst() {
        first = EMPTINESS;
    }

    /**
     * A method for adding skip values to a query.
     *
     * @param number
     */
    public void addSkip(String number) {
        if (!number.isEmpty()) {
            skip = "SKIP " + number;
        } else {
            skip = EMPTINESS;
        }
    }

    /**
     * A method for clearing (deleting) skip values from a query.
     */
    public void clearSkip() {
        skip = EMPTINESS;
    }

    /**
     * A method for adding a distinct value to a query.
     *
     * @param distinct
     */
    public void setDistinct(String distinct) {
        this.distinct = distinct;
    }

    /**
     * A method for adding attributes to a request.
     * Also, if the user added attributes from several tables and did not specify a join,
     * then this method will create not only attributes but also a natural join.
     *
     * @param attributes
     * @param queryTable
     */
    public void addAttributes(ArrayList<ArrayList<String>> attributes, ArrayList<String> queryTable) {
        StringBuilder stringBuilder = new StringBuilder(EMPTINESS);

        for (int i = 0; i < attributes.size(); i++) {
            if (i >= 1) {
                if(!join.contains(queryTable.get(i))){
                    JOptionPane.showMessageDialog(new JFrame(),"Пожалуйста укажите соединение (Join) для таблиц!","Отсутствует объединение (Join)",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if (i < attributes.size()-1) {
                stringBuilder.append(setAttribute(attributes.get(i))).append(COMMA);
            } else {
                stringBuilder.append(setAttribute(attributes.get(i)));
            }
        }

        attribute = stringBuilder.toString();

    }

    /**
     * Clears (removes) all attributes from the query.
     * Replaces them with *.
     */
    public void clearAttribute() {
        attribute = STAR;
    }

    /**
     * This method creates the specified join between the two passed tables
     * and also connects them using the key parameters passed to the method.
     *
     * @param tableNameOne
     * @param tableNameTwo
     * @param joinName
     * @param keyAttributeOne
     * @param keyAttributeTwo
     */
    public void addJoins(String tableNameOne, String tableNameTwo, String joinName, String keyAttributeOne, String keyAttributeTwo) {

        StringBuilder stringBuilder = new StringBuilder(join);

        if (stringBuilder.indexOf(WHITESPACE + tableNameOne + WHITESPACE) == -1) {
            if (stringBuilder.indexOf(WHITESPACE + tableNameTwo + WHITESPACE) == -1) {
                stringBuilder.replace(0,stringBuilder.length(),EMPTINESS);
                stringBuilder.append(tableNameOne).append(WHITESPACE).append(joinName.toUpperCase()).append(WHITESPACE).append(tableNameTwo).append(WHITESPACE).append(ON).append(WHITESPACE).append(tableNameOne).append(POINT).append(keyAttributeOne).append(EQUALS).append(tableNameTwo).append(POINT).append(keyAttributeTwo);
                join = stringBuilder.toString();
                return;

            } else {
                stringBuilder.append(WHITESPACE).append(joinName.toUpperCase()).append(WHITESPACE).append(tableNameOne).append(WHITESPACE).append(ON).append(WHITESPACE).append(tableNameOne).append(POINT).append(keyAttributeOne).append(EQUALS).append(tableNameTwo).append(POINT).append(keyAttributeTwo);
                join = stringBuilder.toString();
                return;
            }
        }

        if (stringBuilder.indexOf(WHITESPACE + tableNameTwo + WHITESPACE) == -1) {
            if (stringBuilder.indexOf(WHITESPACE + tableNameOne + WHITESPACE) == -1) {
                stringBuilder.replace(0,stringBuilder.length(),EMPTINESS);
                stringBuilder.append(tableNameOne).append(WHITESPACE).append(joinName.toUpperCase()).append(WHITESPACE).append(tableNameTwo).append(WHITESPACE).append(ON).append(WHITESPACE).append(tableNameOne).append(POINT).append(keyAttributeOne).append(EQUALS).append(tableNameTwo).append(POINT).append(keyAttributeTwo);
                join = stringBuilder.toString();
                return;

            } else {
                stringBuilder.append(WHITESPACE).append(joinName.toUpperCase()).append(WHITESPACE).append(tableNameTwo).append(WHITESPACE).append(ON).append(WHITESPACE).append(tableNameOne).append(POINT).append(keyAttributeOne).append(EQUALS).append(tableNameTwo).append(POINT).append(keyAttributeTwo);
                join = stringBuilder.toString();
                return;
            }
        }
    }

    /**
     * Cleans (removes) join.
     */
    public void clearJoin() {
        join = EMPTINESS;
    }

    /**
     * Adds a table to the query.
     *
     * @param Table
     */
    public void addTable(String Table) {
        this.table = Table;
    }

    /**
     * Clears (deletes) the table from the query.
     */
    public void clearTable() {
        this.table = "EMPLOYEE";
    }

    /**
     * Compiles and returns a query.
     *
     * @return
     */
    public String getQuery() {
        StringBuilder Query = new StringBuilder(WHITESPACE);

        Query.append(SELECT).append(WHITESPACE);

        if (!first.isEmpty()) {
            Query.append(first).append(WHITESPACE);
        }

        if (!skip.isEmpty()) {
            Query.append(skip).append(WHITESPACE);
        }

        if (attribute.isEmpty()) {
            attribute = STAR;
        }

        Query.append(distinct).append(WHITESPACE).append(attribute).append(WHITESPACE).append(FROM);

        if (!join.isEmpty()) {
            if(join.charAt(0) != ' ') {
                join = WHITESPACE + join;
            }
            Query.append(join).append(WHITESPACE);
        } else {
            if(table.charAt(0) != ' ') {
                table = WHITESPACE + table;
            }
            Query.append(table).append(WHITESPACE);
        }

        Query.append(";");

        return Query.toString();
    }

    /**
     * It helps to create a carriage list of attributes and turns them into one line.
     *
     * @param Array
     * @return
     */
    private String setAttribute(ArrayList<String> Array) {
        StringBuilder stringBuilderAttributes = new StringBuilder(EMPTINESS);

        for (int i = 0; i < Array.size(); i++) {
            if (i != Array.size() - 1) {
                stringBuilderAttributes.append(Array.get(i)).append(COMMA);
            } else {
                stringBuilderAttributes.append(Array.get(i));
            }
        }
        return stringBuilderAttributes.toString();
    }
}