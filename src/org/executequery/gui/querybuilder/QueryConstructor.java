package org.executequery.gui.querybuilder;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * A class for dynamically creating SQL queries (SQL Query Constructor).
 * <p>
 * Класс для динамического создания SQL запросов (Конструктор SQL запросов).
 *
 * @author Krylov Gleb
 */
public class QueryConstructor {

    // --- Elements accepted using the constructor. ----
    // --- Поля, которые передаются через конструктор. ---

    private QBPanel queryBuilderPanel;

    // --- Constant fields ---
    // --- Константные поля ---

    private final String SELECT = "SELECT";
    private final String FROM = "FROM";
    private final String EMPTINESS = "";
    private final String WHITESPACE = " ";
    private final String STAR = "*";

    // --- Other field ---
    // --- Остальные поля ---

    private String distinct = "";
    private String table = "EMPLOYEE";
    private String first = "";
    private String skip = "";
    private String attribute = "";
    private String functions = "";
    private String where = "";
    private String groupBy = "";
    private String orderBy = "";
    private String optimization = "";
    private String having = "";
    private String union = "";
    private String with = "";

    /**
     * Creating a query constructor.
     * <p>
     * Создание конструктора запросов.
     */
    public QueryConstructor(QBPanel queryBuilderPanel) {
        this.queryBuilderPanel = queryBuilderPanel;
    }

    /**
     * The main method for creating (assembling) and receiving a request.
     * <p>
     * Главный метод для создания (сборки) и получения запроса.
     */
    public String buildAndGetQuery() {
        StringBuilder Query = new StringBuilder();
        queryBuilderPanel.clearBlocksPanel();
        addWith(Query);
        addUnion(Query);
        addSelect(Query);
        addFirstInQuery(Query);
        addSkipInQuery(Query);
        addDistinct(Query);
        addAttributesInQuery();
        addFunctionIsEmpty();
        addFunctions(Query);
        addFrom(Query);
        addTableInQuery(Query);
        addWhereIfGroupingIsEmpty(Query);
        addGroupingAndSwapWhereOnHaving(Query);
        addOrderByInQuery(Query);
        addOptimizationInQuery(Query);
        Query.append(";");
        return Query.toString();
    }

    private void addFrom(StringBuilder Query) {
        Query.append(WHITESPACE).append("\n").append(FROM);
        queryBuilderPanel.addBlockInBlocksPanel(FROM,
                new Color(191, 34, 51),
                new Color(234, 140, 150));
    }

    private void addSelect(StringBuilder Query) {
        Query.append(SELECT).append(WHITESPACE);
        queryBuilderPanel.addBlockInBlocksPanel(SELECT,
                new Color(100, 149, 237),
                new Color(194, 213, 248));
    }

    private void addUnion(StringBuilder Query) {
        if (!union.isEmpty()) {
            Query.append(union);

            StringBuilder stringBuilder = new StringBuilder(union);
            int countStrUnion = (int) Math.ceil((double) stringBuilder.length() / 10);
            int start = 0;

            for (int i = 0; i < countStrUnion; i++) {
                if (stringBuilder.indexOf(",", start + 20) >= 0) {
                    if (stringBuilder.indexOf(" ", start + 20) >= 0) {
                        if (stringBuilder.indexOf(",", start + 20) < stringBuilder.indexOf(" ", start + 20)) {
                            queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(",", start + 20) + 1),
                                    new Color(230, 168, 215),
                                    new Color(247, 229, 243));
                            start = stringBuilder.indexOf(",", start + 20) + 1;
                        } else {
                            queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(" ", start + 20) + 1),
                                    new Color(230, 168, 215),
                                    new Color(247, 229, 243));
                            start = stringBuilder.indexOf(" ", start + 20) + 1;
                        }
                    } else {
                        queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(",", start + 20) + 1),
                                new Color(230, 168, 215),
                                new Color(247, 229, 243));
                        start = stringBuilder.indexOf(",", start + 20) + 1;
                    }
                } else {
                    if (stringBuilder.indexOf(" ", start + 20) >= 0) {
                        queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(" ", start + 20) + 1),
                                new Color(230, 168, 215),
                                new Color(247, 229, 243));
                        start = stringBuilder.indexOf(" ", start + 20) + 1;
                    } else {
                        queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.length()),
                                new Color(230, 168, 215),
                                new Color(247, 229, 243));
                        break;
                    }
                }
            }
        }
    }

    private void addDistinct(StringBuilder Query) {
        if (!distinct.isEmpty()) {
            Query.append(distinct);
            queryBuilderPanel.addBlockInBlocksPanel(distinct,
                    new Color(255, 117, 20),
                    new Color(255, 185, 136));
        }
    }

    /**
     * A method for adding with to a query.
     * <p>
     * Метод для добавления with в запрос.
     */
    private void addWith(StringBuilder Query) {
        if (!with.isEmpty()) {
            Query.append(with).append("\n");

            StringBuilder stringBuilder = new StringBuilder(with);
            int countStrWith = (int) Math.ceil((double) stringBuilder.length() / 10);
            int start = 0;

            for (int i = 0; i < countStrWith; i++) {
                if (stringBuilder.indexOf(",", start + 20) >= 0) {
                    if (stringBuilder.indexOf(" ", start + 20) >= 0) {
                        if (stringBuilder.indexOf(",", start + 20) < stringBuilder.indexOf(" ", start + 20)) {
                            queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(",", start + 20) + 1),
                                    new Color(170, 240, 209),
                                    new Color(212, 247, 232));
                            start = stringBuilder.indexOf(",", start + 20) + 1;
                        } else {
                            queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(" ", start + 20) + 1),
                                    new Color(170, 240, 209),
                                    new Color(212, 247, 232));
                            start = stringBuilder.indexOf(" ", start + 20) + 1;
                        }
                    } else {
                        queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(",", start + 20) + 1),
                                new Color(170, 240, 209),
                                new Color(212, 247, 232));
                        start = stringBuilder.indexOf(",", start + 20) + 1;
                    }
                } else {
                    if (stringBuilder.indexOf(" ", start + 20) >= 0) {
                        queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(" ", start + 20) + 1),
                                new Color(170, 240, 209),
                                new Color(212, 247, 232));
                        start = stringBuilder.indexOf(" ", start + 20) + 1;
                    } else {
                        queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.length()),
                                new Color(170, 240, 209),
                                new Color(212, 247, 232));
                        break;
                    }
                }
            }
        }
    }

    /**
     * The method for getting the with value.
     * <p>
     * Метод для получения значения with.
     */
    public String getWith() {
        return with;
    }

    /**
     * A method for changing the with value in a request.
     * <p>
     * Метод для смены значения with в запросе.
     */
    public void setWith(String with) {
        this.with = with;
    }

    /**
     * The method for adding First to the request.
     * <p>
     * Метод для добавления First в запрос.
     */
    private void addFirstInQuery(StringBuilder Query) {
        if (!first.isEmpty()) {
            Query.append(first).append(WHITESPACE);
            queryBuilderPanel.addBlockInBlocksPanel(first,
                    new Color(255, 117, 20),
                    new Color(255, 185, 136));
        }
    }

    /**
     * The method for changing the value of First.
     * <p>
     * Метод для смены значения First.
     */
    public void setFirst(String number) {
        if (!number.isEmpty()) {
            first = "FIRST " + number;
        } else {
            first = EMPTINESS;
        }
    }

    /**
     * The method for getting the First value.
     * <p>
     * Метод для получения значения First.
     */
    public String getFirst() {
        if (first.isEmpty()) {
            return "";
        } else {
            return first.split(" ")[1];
        }
    }

    /**
     * Method for resetting first values.
     * <p>
     * Метод для сброса значений first
     */
    private void clearFirst() {
        first = EMPTINESS;
    }

    /**
     * The method for adding Skip to the request.
     * <p>
     * Метод для добавления Skip в запрос.
     */
    private void addSkipInQuery(StringBuilder Query) {
        if (!skip.isEmpty()) {
            Query.append(skip).append(WHITESPACE);
            queryBuilderPanel.addBlockInBlocksPanel(skip,
                    new Color(255, 117, 20),
                    new Color(255, 185, 136));
        }
    }

    /**
     * A method for changing the Skip value.
     * <p>
     * Метод для смены значения Skip.
     */
    public void setSkip(String number) {
        if (!number.isEmpty()) {
            skip = "SKIP " + number;
        } else {
            skip = EMPTINESS;
        }
    }

    /**
     * The method for getting the Skip value.
     * <p>
     * Метод для получения значения Skip.
     */
    public String getSkip() {
        if (skip.isEmpty()) {
            return "";
        } else {
            return skip.split(" ")[1];
        }
    }

    /**
     * A method for resetting skip values.
     * <p>
     * Метод для сброса значений skip.
     */
    private void clearSkip() {
        skip = EMPTINESS;
    }

    /**
     * A method for changing the Distinct value in a request.
     * <p>
     * Метод для смены значения Distinct в запросе.
     */
    public void setDistinct(String distinct) {
        this.distinct = distinct;
    }

    /**
     * A method for getting Distinct values.
     * <p>
     * Метод для получения значений Distinct.
     */
    public String getDistinct() {
        return distinct;
    }

    /**
     * A method for adding attributes to a request.
     * <p>
     * Метод для добавления атрибутов в запрос.
     */
    private void addAttributesInQuery() {
        if (attribute.isEmpty()) {
            attribute = STAR;
        }
    }

    /**
     * A method for changing the attribute values.
     * <p>
     * Метод для смены значения атрибутов.
     */
    public void setAttributes(ArrayList<JTable> tablesOnOutputPanel) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < tablesOnOutputPanel.size(); i++) {
            if (table.indexOf(tablesOnOutputPanel.get(i).getColumnName(0)) == 0) {
                for (int j = 0; j < tablesOnOutputPanel.get(i).getRowCount(); j++) {
                    if ((boolean) tablesOnOutputPanel.get(i).getValueAt(j, 1)) {
                        if (queryBuilderPanel.getBlocksPanel().getComponents().length == 1) {
                            stringBuilder.append(tablesOnOutputPanel.get(i).getColumnName(0)).append(".").append(tablesOnOutputPanel.get(i).getValueAt(j, 0)).append(",");

                        } else {
                            if (table.contains(tablesOnOutputPanel.get(i).getColumnName(0))) {
                                stringBuilder.append(tablesOnOutputPanel.get(i).getColumnName(0)).append(".").append(tablesOnOutputPanel.get(i).getValueAt(j, 0)).append(",");
                            }
                        }

                    }
                }
            } else {
                if (table.contains(" " + tablesOnOutputPanel.get(i).getColumnName(0) + " ")) {
                    for (int j = 0; j < tablesOnOutputPanel.get(i).getRowCount(); j++) {
                        if ((boolean) tablesOnOutputPanel.get(i).getValueAt(j, 1)) {
                            if (queryBuilderPanel.getBlocksPanel().getComponents().length == 1) {
                                stringBuilder.append(tablesOnOutputPanel.get(i).getColumnName(0)).append(".").append(tablesOnOutputPanel.get(i).getValueAt(j, 0)).append(",");

                            } else {
                                if (table.contains(tablesOnOutputPanel.get(i).getColumnName(0))) {
                                    stringBuilder.append(tablesOnOutputPanel.get(i).getColumnName(0)).append(".").append(tablesOnOutputPanel.get(i).getValueAt(j, 0)).append(",");
                                }
                            }

                        }
                    }
                }
            }
        }

        if (!stringBuilder.toString().isEmpty()) {
            attribute = stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "").toString();
            return;
        }

        attribute = stringBuilder.toString();
    }

    /**
     * A method for resetting attribute values.
     * <p>
     * Метод для сброса значений атрибутов.
     */
    public void clearAttribute() {
        attribute = STAR;
    }

    /**
     * A method for getting attribute values.
     * <p>
     * Метод для получения значения атрибутов.
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * A method for overwriting attribute values.
     * <p>
     * Метод для перезаписи значений атрибутов.
     */
    public void replaceAttribute(String attributes) {
        attribute = attributes;
    }

    /**
     * If there are no functions, set an empty value.
     * <p>
     * Если функции отсутствуют задаём пустое значение.
     */
    private void addFunctionIsEmpty() {
        if (functions.isEmpty()) {
            functions = "";
        }
    }

    /**
     * A method for adding functions to a query.
     * <p>
     * Метод для добавления функций в запрос.
     */
    private void addFunctions(StringBuilder Query) {
        if (!functions.isEmpty()) {
            if (attribute.equals("*")) {
                attribute = "";

                if (functions.indexOf(",") == 0) {
                    functions = functions.substring(1);
                }

                Query.append(WHITESPACE).append("\n").append(attribute).append(functions);

                String[] functionElements = functions.split("(?<=\\),)");

                for (int i = 0; i < functionElements.length; i++) {
                    String[] functionsElementIsAlias = functionElements[i].split("(?<=\",)");
                    for (int j = 0; j < functionsElementIsAlias.length; j++) {

                        StringBuilder stringBuilder = new StringBuilder(functionsElementIsAlias[i]);
                        int countStrFunctions = (int) Math.ceil((double) stringBuilder.length() / 10);
                        int start = 0;

                        for (int index = 0; index < countStrFunctions; index++) {
                            if (stringBuilder.indexOf(",", start + 20) >= 0) {
                                if (stringBuilder.indexOf(" ", start + 20) >= 0) {
                                    if (stringBuilder.indexOf(",", start + 20) < stringBuilder.indexOf(" ", start + 20)) {
                                        queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(",", start + 20) + 1),
                                                new Color(255, 207, 64),
                                                new Color(255, 238, 187));
                                        start = stringBuilder.indexOf(",", start + 20) + 1;
                                    } else {
                                        queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(" ", start + 20) + 1),
                                                new Color(255, 207, 64),
                                                new Color(255, 238, 187));
                                        start = stringBuilder.indexOf(" ", start + 20) + 1;
                                    }
                                } else {
                                    queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(",", start + 20) + 1),
                                            new Color(255, 207, 64),
                                            new Color(255, 238, 187));
                                    start = stringBuilder.indexOf(",", start + 20) + 1;
                                }
                            } else {
                                if (stringBuilder.indexOf(" ", start + 20) >= 0) {
                                    queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(" ", start + 20) + 1),
                                            new Color(255, 207, 64),
                                            new Color(255, 238, 187));
                                    start = stringBuilder.indexOf(" ", start + 20) + 1;
                                } else {
                                    queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.length()),
                                            new Color(255, 207, 64),
                                            new Color(255, 238, 187));
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                if (functions.indexOf(",") != 0) {
                    functions = "," + functions;
                }

                Query.append(WHITESPACE).append("\n").append(attribute).append(functions);

                String[] attributes = attribute.split("(?<=,)");

                for (int i = 0; i < attributes.length; i++) {
                    queryBuilderPanel.addBlockInBlocksPanel(attributes[i],
                            new Color(255, 207, 64),
                            new Color(255, 238, 187));
                }

                String[] functionElements = functions.split("(?<=\\),)");

                for (int i = 0; i < functionElements.length; i++) {
                    String[] functionsElementIsAlias = functionElements[i].split("(?<=\",)");
                    for (int j = 0; j < functionsElementIsAlias.length; j++) {

                        StringBuilder stringBuilder = new StringBuilder(functionsElementIsAlias[i]);
                        int countStrFunctions = (int) Math.ceil((double) stringBuilder.length() / 10);
                        int start = 0;

                        for (int index = 0; index < countStrFunctions; index++) {
                            if (stringBuilder.indexOf(",", start + 20) >= 0) {
                                if (stringBuilder.indexOf(" ", start + 20) >= 0) {
                                    if (stringBuilder.indexOf(",", start + 20) < stringBuilder.indexOf(" ", start + 20)) {
                                        queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(",", start + 20) + 1),
                                                new Color(255, 207, 64),
                                                new Color(255, 238, 187));
                                        start = stringBuilder.indexOf(",", start + 20) + 1;
                                    } else {
                                        queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(" ", start + 20) + 1),
                                                new Color(255, 207, 64),
                                                new Color(255, 238, 187));
                                        start = stringBuilder.indexOf(" ", start + 20) + 1;
                                    }
                                } else {
                                    queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(",", start + 20) + 1),
                                            new Color(255, 207, 64),
                                            new Color(255, 238, 187));
                                    start = stringBuilder.indexOf(",", start + 20) + 1;
                                }
                            } else {
                                if (stringBuilder.indexOf(" ", start + 20) >= 0) {
                                    queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(" ", start + 20) + 1),
                                            new Color(255, 207, 64),
                                            new Color(255, 238, 187));
                                    start = stringBuilder.indexOf(" ", start + 20) + 1;
                                } else {
                                    queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.length()),
                                            new Color(255, 207, 64),
                                            new Color(255, 238, 187));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Query.append(WHITESPACE).append("\n").append(attribute);

            String[] attributes = attribute.split("(?<=,)");

            for (int i = 0; i < attributes.length; i++) {
                queryBuilderPanel.addBlockInBlocksPanel(attributes[i],
                        new Color(255, 207, 64),
                        new Color(255, 238, 187));
            }
        }
    }

    /**
     * A method for resetting function values.
     * <p>
     * Метод для сброса значений функций.
     */
    public void clearFunction() {
        functions = "";
    }

    /**
     * A method for getting the value of functions.
     * <p>
     * Метод для получения значения функций.
     */
    public String getFunctions() {
        return functions;
    }

    /**
     * A method for overwriting function values.
     * <p>
     * Метод для перезаписи значений функций.
     */
    public void replaceFunctions(String functions) {
        this.functions = functions;
    }

    /**
     * A method for adding tables to a query.
     * <p>
     * Метод для добавления таблиц в запрос.
     */
    private void addTableInQuery(StringBuilder Query) {
        Query.append(WHITESPACE).append(table).append(WHITESPACE);

        StringBuilder stringBuilder = new StringBuilder(table);
        int countStrTable = (int) Math.ceil((double) stringBuilder.length() / 10);
        int start = 0;

        for (int i = 0; i < countStrTable; i++) {
            if (stringBuilder.indexOf(" ", start + 20) > 0) {
                queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(" ", start + 20) + 1),
                        new Color(119, 221, 119),
                        new Color(201, 241, 201));
                start = stringBuilder.indexOf(" ", start + 20) + 1;
            } else {
                queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.length()),
                        new Color(119, 221, 119),
                        new Color(201, 241, 201));
                break;
            }
        }
    }

    /**
     * A method for changing the value of a table.
     * <p>
     * Метод для смены значения таблицы.
     */
    public void setTable(String Table) {
        this.table = Table;
    }

    /**
     * A method for resetting table values.
     * <p>
     * Метод для сброса значений таблиц.
     */
    private void clearTable() {
        if (!queryBuilderPanel.getListTable().isEmpty()) {
            table = queryBuilderPanel.getListTable().get(0).getColumnName(0);
        } else {
            this.table = "EMPLOYEE";
        }
    }

    /**
     * A method for getting table values.
     * <p>
     * Метод для получения значений таблиц.
     */
    public String getTable() {
        return table;
    }

    /**
     * A method for checking and adding a condition to a request.
     * <p>
     * Метод для проверки и добавления условия в запрос.
     */
    private void addWhereIfGroupingIsEmpty(StringBuilder Query) {
        if (groupBy.isEmpty()) {
            if (!where.isEmpty()) {
                Query.append("\n").append(where).append(WHITESPACE);

                StringBuilder stringBuilder = new StringBuilder(where);
                int countStrWhere = (int) Math.ceil((double) stringBuilder.length() / 10);
                int start = 0;

                for (int i = 0; i < countStrWhere; i++) {
                    if (stringBuilder.indexOf(",", start + 20) >= 0) {
                        if (stringBuilder.indexOf(" ", start + 20) >= 0) {
                            if (stringBuilder.indexOf(",", start + 20) < stringBuilder.indexOf(" ", start + 20)) {
                                queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(",", start + 20) + 1),
                                        new Color(255, 155, 170),
                                        new Color(255, 221, 226));
                                start = stringBuilder.indexOf(",", start + 20) + 1;
                            } else {
                                queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(" ", start + 20) + 1),
                                        new Color(255, 155, 170),
                                        new Color(255, 221, 226));
                                start = stringBuilder.indexOf(" ", start + 20) + 1;
                            }
                        } else {
                            queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(",", start + 20) + 1),
                                    new Color(255, 155, 170),
                                    new Color(255, 221, 226));
                            start = stringBuilder.indexOf(",", start + 20) + 1;
                        }
                    } else {
                        if (stringBuilder.indexOf(" ", start + 20) >= 0) {
                            queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(" ", start + 20) + 1),
                                    new Color(255, 155, 170),
                                    new Color(255, 221, 226));
                            start = stringBuilder.indexOf(" ", start + 20) + 1;
                        } else {
                            queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.length()),
                                    new Color(255, 155, 170),
                                    new Color(255, 221, 226));
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * A method for resetting condition values.
     * <p>
     * Метод для сброса значений условий.
     */
    private void clearWhere() {
        where = "";
    }

    /**
     * A method for getting the value of the conditions.
     * <p>
     * Метод для получения значения условий.
     */
    public String getWhere() {
        return where;
    }

    /**
     * A method for changing the value of where in the request.
     * <p>
     * Метод для смены значения where в запросе.
     */
    public void setWhere(String where) {
        this.where = where;
    }

    /**
     * A method for changing where to having if there is a grouping.
     * <p>
     * Метод для смены where на having при наличии группировки.
     */
    public void swapWhereOnHaving() {
        if (!where.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(where);
            stringBuilder.replace(stringBuilder.indexOf("WHERE"), stringBuilder.indexOf("WHERE") + 5, "HAVING");
            having = stringBuilder.toString();
        }
    }

    /**
     * A method for checking and adding a grouping to a query and replacing where with having.
     * <p>
     * Метод для проверки и добавления группировки в запрос а также замены where на having.
     */
    private void addGroupingAndSwapWhereOnHaving(StringBuilder Query) {
        if (!groupBy.isEmpty()) {
            Query.append("\n").append(groupBy);

            StringBuilder stringBuilder = new StringBuilder(groupBy);
            queryBuilderPanel.addBlockInBlocksPanel("GROUP BY",
                    new Color(234, 141, 247),
                    new Color(243, 191, 251));

            stringBuilder.replace(stringBuilder.indexOf("GROUP BY"), stringBuilder.indexOf("GROUP BY") + "GROUP BY".length(), "");

            String[] attributeGroupBY = stringBuilder.toString().split(",");

            for (int i = 0; i < attributeGroupBY.length; i++) {
                queryBuilderPanel.addBlockInBlocksPanel(attributeGroupBY[i],
                        new Color(234, 141, 247),
                        new Color(243, 191, 251));
            }

            if (!where.isEmpty()) {
                swapWhereOnHaving();

                Query.append("\n").append(having);

                StringBuilder stringBuilderHaving = new StringBuilder(having);
                int countStrHaving = (int) Math.ceil((double) stringBuilderHaving.length() / 10);
                int start = 0;

                for (int i = 0; i < countStrHaving; i++) {
                    if (stringBuilderHaving.indexOf(",", start + 20) >= 0) {
                        if (stringBuilderHaving.indexOf(" ", start + 20) >= 0) {
                            if (stringBuilderHaving.indexOf(",", start + 20) < stringBuilderHaving.indexOf(" ", start + 20)) {
                                queryBuilderPanel.addBlockInBlocksPanel(stringBuilderHaving.substring(start, stringBuilderHaving.indexOf(",", start + 20) + 1),
                                        new Color(255, 155, 170),
                                        new Color(255, 221, 226));
                                start = stringBuilderHaving.indexOf(",", start + 20) + 1;
                            } else {
                                queryBuilderPanel.addBlockInBlocksPanel(stringBuilderHaving.substring(start, stringBuilderHaving.indexOf(" ", start + 20) + 1),
                                        new Color(255, 155, 170),
                                        new Color(255, 221, 226));
                                start = stringBuilderHaving.indexOf(" ", start + 20) + 1;
                            }
                        } else {
                            queryBuilderPanel.addBlockInBlocksPanel(stringBuilderHaving.substring(start, stringBuilderHaving.indexOf(",", start + 20) + 1),
                                    new Color(255, 155, 170),
                                    new Color(255, 221, 226));
                            start = stringBuilderHaving.indexOf(",", start + 20) + 1;
                        }
                    } else {
                        if (stringBuilderHaving.indexOf(" ", start + 20) >= 0) {
                            queryBuilderPanel.addBlockInBlocksPanel(stringBuilderHaving.substring(start, stringBuilderHaving.indexOf(" ", start + 20) + 1),
                                    new Color(255, 155, 170),
                                    new Color(255, 221, 226));
                            start = stringBuilderHaving.indexOf(" ", start + 20) + 1;
                        } else {
                            queryBuilderPanel.addBlockInBlocksPanel(stringBuilderHaving.substring(start, stringBuilderHaving.length()),
                                    new Color(255, 155, 170),
                                    new Color(255, 221, 226));
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * A method for resetting the grouping values.
     * <p>
     * Метод для сброса значений группировки.
     */
    private void clearGroupBy() {
        groupBy = "";
    }

    /**
     * A method for resetting having values.
     * <p>
     * Метод для сброса значений having.
     */
    private void clearHaving() {
        having = "";
    }

    /**
     * The method for getting the grouping value.
     * <p>
     * Метод для получения значения группировки.
     */
    public String getGroupBy() {
        return groupBy;
    }

    /**
     * A method for changing the grouping value in a request.
     * <p>
     * Метод для смены значения группировки в запросе.
     */
    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    /**
     * A method for adding sorting to a query.
     * <p>
     * Метод для добавления сортировки в запрос.
     */
    private void addOrderByInQuery(StringBuilder Query) {
        if (!orderBy.isEmpty()) {
            Query.append("\n").append(orderBy);

            StringBuilder stringBuilder = new StringBuilder(orderBy);
            queryBuilderPanel.addBlockInBlocksPanel("ORDER BY",
                    new Color(62, 180, 137),
                    new Color(154, 220, 196));
            stringBuilder.replace(stringBuilder.indexOf("ORDER BY"), stringBuilder.indexOf("ORDER BY") + "ORDER BY".length(), "");
            String[] attributeOrderBy = stringBuilder.toString().split(",");

            for (int i = 0; i < attributeOrderBy.length; i++) {
                queryBuilderPanel.addBlockInBlocksPanel(attributeOrderBy[i],
                        new Color(62, 180, 137),
                        new Color(154, 220, 196));
            }
        }
    }

    /**
     * A method for resetting sorting values.
     * <p>
     * Метод для сброса значений сортировки.
     */
    private void clearOrderBy() {
        orderBy = "";
    }

    /**
     * The method for getting the sort value.
     * <p>
     * Метод для получения значения сортировки.
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * A method for changing the sort value in a query.
     * <p>
     * Метод для смены значения сортировки в запросе.
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * A method for checking and adding optimization to a query.
     * <p>
     * Метод для проверки и добавления оптимизации в запрос.
     */
    private void addOptimizationInQuery(StringBuilder Query) {
        if (!optimization.isEmpty()) {
            Query.append("\n").append(optimization);
            queryBuilderPanel.addBlockInBlocksPanel(optimization,
                    new Color(91, 58, 41),
                    new Color(197, 150, 126));
        }
    }

    /**
     * A method for changing the optimization value.
     * <p>
     * Метод для смены значения оптимизации.
     */
    public void setOptimization(String strOptimizeFor) {
        if (!strOptimizeFor.isEmpty()) {
            optimization = strOptimizeFor;
        } else {
            optimization = "";
        }
    }

    /**
     * A method for resetting optimization values.
     * <p>
     * Метод для сброса значений оптимизации.
     */
    public void clearOptimization() {
        optimization = "";
    }

    /**
     * A method for changing the value of union.
     * <p>
     * Метод для смены значения union.
     */
    public void setUnion(String strUnion) {
        union = strUnion;
    }

    /**
     * A method for resetting union values.
     * <p>
     * Метод для сброса значений union.
     */
    private void clearUnion() {
        union = "";
    }

    /**
     * The method for getting the union value.
     * <p>
     * Метод для получения значения union.
     */
    public String getUnion() {
        return union;
    }

    /**
     * A method for completely clearing the request.
     * <p>
     * Метод для полной очистки запроса.
     */
    public void clearAll() {
        clearAttribute();
        clearFunction();
        clearSkip();
        clearFirst();
        clearWhere();
        clearHaving();
        clearTable();
        clearGroupBy();
        clearOrderBy();
        clearOptimization();
        clearUnion();
    }
}