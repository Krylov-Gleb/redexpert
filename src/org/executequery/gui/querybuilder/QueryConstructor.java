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

    private final QBPanel queryBuilderPanel;

    // --- Constant fields ---
    // --- Константные поля ---

    private final String EMPTINESS = "";
    private final String WHITESPACE = " ";
    private final String STAR = "*";

    // --- Fields of colors ---
    // --- Поля цветов ---

    private final Color colorBorderBlockFrom = new Color(191, 34, 51);
    private final Color colorBackgroundBlockFrom = new Color(234, 140, 150);
    private final Color colorBorderBlockSelect = new Color(100, 149, 237);
    private final Color colorBackgroundBlockSelect = new Color(194, 213, 248);
    private final Color colorBorderBlockUnion = new Color(230, 168, 215);
    private final Color colorBackgroundBlockUnion = new Color(247, 229, 243);
    private final Color colorBorderBlockAdditions = new Color(255, 117, 20);
    private final Color colorBackgroundBlockAdditions = new Color(255, 185, 136);
    private final Color colorBorderBlockWith = new Color(170, 240, 209);
    private final Color colorBackgroundBlockWith = new Color(212, 247, 232);
    private final Color colorBorderBlockFunctionsAndAttributes = new Color(255, 207, 64);
    private final Color colorBackgroundBlockFunctionsAndAttributes = new Color(255, 238, 187);
    private final Color colorBorderBlockTable = new Color(119, 221, 119);
    private final Color colorBackgroundBlockTable = new Color(201, 241, 201);
    private final Color colorBorderBlockWhereAndHaving = new Color(255, 155, 170);
    private final Color colorBackgroundBlockWhereAndHaving = new Color(255, 221, 226);
    private final Color colorBorderBlockGroupBy = new Color(234, 141, 247);
    private final Color colorBackgroundBlockGroupBy = new Color(243, 191, 251);
    private final Color colorBorderBlockOrderBy = new Color(62, 180, 137);
    private final Color colorBackgroundBlockOrderBy = new Color(154, 220, 196);
    private final Color colorBorderBlockOptimization = new Color(91, 58, 41);
    private final Color colorBackgroundBlockOptimization = new Color(197, 150, 126);

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
        StringBuilder query = new StringBuilder();
        queryBuilderPanel.clearBlocksPanel();
        addWith(query);
        addUnion(query);
        addSelect(query);
        addFirstInQuery(query);
        addSkipInQuery(query);
        addDistinct(query);
        addAttributesInQuery();
        addFunctionIsEmpty();
        addFunctions(query);
        addFrom(query);
        addTableInQuery(query);
        addWhereIfGroupingIsEmpty(query);
        addGroupingAndSwapWhereOnHaving(query);
        addOrderByInQuery(query);
        addOptimizationInQuery(query);
        query.append(";");
        return query.toString();
    }

    /**
     * A method for adding from to a request.
     * <p>
     * Метод для добавления from в запрос.
     */
    private void addFrom(StringBuilder query) {
        query.append("\n").append("FROM").append(WHITESPACE);
        queryBuilderPanel.addBlockInBlocksPanel("FROM", colorBorderBlockFrom, colorBackgroundBlockFrom);
    }

    /**
     * A method for adding select to a query.
     * <p>
     * Метод для добавления select в запрос.
     */
    private void addSelect(StringBuilder query) {
        query.append("SELECT").append(WHITESPACE);
        queryBuilderPanel.addBlockInBlocksPanel("SELECT", colorBorderBlockSelect, colorBackgroundBlockSelect);
    }

    /**
     * A method for adding a union to a request.
     * <p>
     * Метод для добавления union в запрос.
     */
    private void addUnion(StringBuilder query) {
        if (!union.isEmpty()) {

            query.append(union).append(WHITESPACE);

            StringBuilder stringBuilder = new StringBuilder(union);
            int countStrFunctions = (int) Math.ceil((double) stringBuilder.length() / 30);
            int start = 0;

            for (int index = 0; index < countStrFunctions; index++) {
                if (stringBuilder.indexOf(",", start + 30) >= 0) {
                    if (stringBuilder.indexOf(" ", start + 30) >= 0) {
                        if (stringBuilder.indexOf(",", start + 30) < stringBuilder.indexOf(" ", start + 30)) {
                            queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(",", start + 30) + 1), colorBorderBlockUnion, colorBackgroundBlockUnion);
                            start = stringBuilder.indexOf(",", start + 30) + 1;
                        } else {
                            queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(" ", start + 30) + 1), colorBorderBlockUnion, colorBackgroundBlockUnion);
                            start = stringBuilder.indexOf(" ", start + 30) + 1;
                        }
                    } else {
                        queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(",", start + 30) + 1), colorBorderBlockUnion, colorBackgroundBlockUnion);
                        start = stringBuilder.indexOf(",", start + 30) + 1;
                    }
                } else {
                    if (stringBuilder.indexOf(" ", start + 30) >= 0) {
                        queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.indexOf(" ", start + 30) + 1), colorBorderBlockUnion, colorBackgroundBlockUnion);
                        start = stringBuilder.indexOf(" ", start + 30) + 1;
                    } else {
                        queryBuilderPanel.addBlockInBlocksPanel(stringBuilder.substring(start, stringBuilder.length()), colorBorderBlockUnion, colorBackgroundBlockUnion);
                        break;
                    }
                }
            }
        }
    }

    /**
     * A method for adding distinct to a query.
     * <p>
     * Метод для добавления distinct в запрос.
     */
    private void addDistinct(StringBuilder query) {
        if (!distinct.isEmpty()) {
            query.append("\n").append(distinct).append(WHITESPACE);
            queryBuilderPanel.addBlockInBlocksPanel(distinct, colorBorderBlockAdditions, colorBackgroundBlockAdditions);
        }
    }

    /**
     * A method for adding with to a query.
     * <p>
     * Метод для добавления with в запрос.
     */
    private void addWith(StringBuilder query) {
        if (!with.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(with);
            stringBuilder.replace(stringBuilder.indexOf("WITH RECURSIVE"), stringBuilder.indexOf("WITH RECURSIVE") + "WITH RECURSIVE".length() + 1, "");

            query.append("WITH RECURSIVE");
            queryBuilderPanel.addBlockInBlocksPanel("WITH RECURSIVE", colorBorderBlockWith, colorBackgroundBlockWith);

            String[] withSplit = stringBuilder.toString().split("(?<=\\),)");

            for (int i = 0; i < withSplit.length; i++) {
                query.append("\n");

                StringBuilder stringBuilderWith = new StringBuilder(withSplit[i]);
                int countStrFunctions = (int) Math.ceil((double) stringBuilderWith.length() / 30);
                int start = 0;

                for (int index = 0; index < countStrFunctions; index++) {
                    if (stringBuilderWith.indexOf(",", start + 30) >= 0) {
                        if (stringBuilderWith.indexOf(" ", start + 30) >= 0) {
                            if (stringBuilderWith.indexOf(",", start + 30) < stringBuilderWith.indexOf(" ", start + 30)) {
                                if (stringBuilderWith.indexOf(",", start + 30) + 1 == stringBuilderWith.length()) {
                                    query.append(stringBuilderWith.substring(start, stringBuilderWith.indexOf(",", start + 30) + 1));
                                } else {
                                    query.append(stringBuilderWith.substring(start, stringBuilderWith.indexOf(",", start + 30) + 1)).append("\n");
                                }

                                queryBuilderPanel.addBlockInBlocksPanel(stringBuilderWith.substring(start, stringBuilderWith.indexOf(",", start + 30) + 1), colorBorderBlockWith, colorBackgroundBlockWith);
                                start = stringBuilderWith.indexOf(",", start + 30) + 1;
                            } else {
                                if (stringBuilderWith.indexOf(" ", start + 30) + 1 == stringBuilderWith.length()) {
                                    query.append(stringBuilderWith.substring(start, stringBuilderWith.indexOf(" ", start + 30) + 1));
                                } else {
                                    query.append(stringBuilderWith.substring(start, stringBuilderWith.indexOf(" ", start + 30) + 1)).append("\n");
                                }

                                queryBuilderPanel.addBlockInBlocksPanel(stringBuilderWith.substring(start, stringBuilderWith.indexOf(" ", start + 30) + 1), colorBorderBlockWith, colorBackgroundBlockWith);
                                start = stringBuilderWith.indexOf(" ", start + 30) + 1;
                            }
                        } else {
                            if (stringBuilderWith.indexOf(",", start + 30) + 1 == stringBuilderWith.length()) {
                                query.append(stringBuilderWith.substring(start, stringBuilderWith.indexOf(",", start + 30) + 1));
                            } else {
                                query.append(stringBuilderWith.substring(start, stringBuilderWith.indexOf(",", start + 30) + 1)).append("\n");
                            }

                            queryBuilderPanel.addBlockInBlocksPanel(stringBuilderWith.substring(start, stringBuilderWith.indexOf(",", start + 30) + 1), colorBorderBlockWith, colorBackgroundBlockWith);
                            start = stringBuilderWith.indexOf(",", start + 30) + 1;
                        }
                    } else {
                        if (stringBuilderWith.indexOf(" ", start + 30) >= 0) {
                            if (stringBuilderWith.indexOf(" ", start + 30) + 1 == stringBuilderWith.length()) {
                                query.append(stringBuilderWith.substring(start, stringBuilderWith.indexOf(" ", start + 30) + 1));
                            } else {
                                query.append(stringBuilderWith.substring(start, stringBuilderWith.indexOf(" ", start + 30) + 1)).append("\n");
                            }

                            queryBuilderPanel.addBlockInBlocksPanel(stringBuilderWith.substring(start, stringBuilderWith.indexOf(" ", start + 30) + 1), colorBorderBlockWith, colorBackgroundBlockWith);
                            start = stringBuilderWith.indexOf(" ", start + 30) + 1;
                        } else {
                            query.append(stringBuilderWith.substring(start, stringBuilderWith.length()));
                            queryBuilderPanel.addBlockInBlocksPanel(stringBuilderWith.substring(start, stringBuilderWith.length()), colorBorderBlockWith, colorBackgroundBlockWith);
                            break;
                        }
                    }
                }

                query.append(WHITESPACE);
            }

            query.append("\n");
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
    private void addFirstInQuery(StringBuilder query) {
        if (!first.isEmpty()) {
            query.append("\n").append(first).append(WHITESPACE);
            queryBuilderPanel.addBlockInBlocksPanel(first, colorBorderBlockAdditions, colorBackgroundBlockAdditions);
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

    private void clearDistinct() {
        distinct = "";
    }

    /**
     * The method for adding Skip to the request.
     * <p>
     * Метод для добавления Skip в запрос.
     */
    private void addSkipInQuery(StringBuilder query) {
        if (!skip.isEmpty()) {
            query.append("\n").append(skip).append(WHITESPACE);
            queryBuilderPanel.addBlockInBlocksPanel(skip, colorBorderBlockAdditions, colorBackgroundBlockAdditions);
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
     * A method for changing the attribute values.
     * <p>
     * Метод для смены значения атрибутов.
     */
    public void setAttributes(String attribute) {
        this.attribute = attribute;
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
    private void addFunctions(StringBuilder query) {
        if (!functions.isEmpty()) {
            if (attribute.equals("*")) {
                attribute = "";

                if (functions.indexOf(",") == 0) {
                    functions = functions.substring(1);
                }

                query.append(WHITESPACE).append(attribute);

                String[] functionElements = functions.split("(?<=\\),)");

                for (int i = 0; i < functionElements.length; i++) {
                    String[] functionsElementIsAlias = functionElements[i].split("(?<=\",)");
                    for (int j = 0; j < functionsElementIsAlias.length; j++) {
                        query.append("\n").append(functionsElementIsAlias[j]);
                        queryBuilderPanel.addBlockInBlocksPanel(functionsElementIsAlias[j], colorBorderBlockFunctionsAndAttributes, colorBackgroundBlockFunctionsAndAttributes);
                    }
                }

                query.append(WHITESPACE);

            } else {
                if (attribute.lastIndexOf(",") != attribute.length() - 1) {
                    attribute = attribute + ",";
                }

                query.append(WHITESPACE);

                String[] attributes = attribute.split("(?<=,)");

                for (int i = 0; i < attributes.length; i++) {
                    query.append("\n").append(attributes[i]);
                    queryBuilderPanel.addBlockInBlocksPanel(attributes[i], colorBorderBlockFunctionsAndAttributes, colorBackgroundBlockFunctionsAndAttributes);
                }

                String[] functionElements = functions.split("(?<=\\),)");

                for (int i = 0; i < functionElements.length; i++) {
                    String[] functionsElementIsAlias = functionElements[i].split("(?<=\",)");
                    for (int j = 0; j < functionsElementIsAlias.length; j++) {
                        query.append("\n").append(functionsElementIsAlias[j]);
                        queryBuilderPanel.addBlockInBlocksPanel(functionsElementIsAlias[j], colorBorderBlockFunctionsAndAttributes, colorBackgroundBlockFunctionsAndAttributes);
                    }
                }

                query.append(WHITESPACE);
            }
        } else {
            query.append(WHITESPACE);

            String[] attributes = attribute.split("(?<=,)");

            for (int i = 0; i < attributes.length; i++) {
                query.append("\n").append(attributes[i]);
                queryBuilderPanel.addBlockInBlocksPanel(attributes[i], colorBorderBlockFunctionsAndAttributes, colorBackgroundBlockFunctionsAndAttributes);
            }

            query.append(WHITESPACE);
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
    private void addTableInQuery(StringBuilder query) {
        query.append(WHITESPACE);

        String[] joins = table.split("(?=INNER JOIN)|(?=LEFT JOIN)|(?=RIGHT JOIN)" +
                "|(?=FULL OUTER JOIN)|(?=CROSS JOIN)|(?=NATURAL JOIN)");

        for (int i = 0; i < joins.length; i++) {
            String[] splitOn = joins[i].split("(?=ON)");
            for (int j = 0; j < splitOn.length; j++) {
                query.append("\n").append(splitOn[j]);
                queryBuilderPanel.addBlockInBlocksPanel(splitOn[j], colorBorderBlockTable, colorBackgroundBlockTable);
            }
        }

        query.append(WHITESPACE);
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
    private void addWhereIfGroupingIsEmpty(StringBuilder query) {
        if (groupBy.isEmpty()) {
            if (!where.isEmpty()) {

                StringBuilder stringBuilder = new StringBuilder(where);

                if (stringBuilder.indexOf("WHERE") >= 0) {
                    stringBuilder.replace(stringBuilder.indexOf("WHERE"), stringBuilder.indexOf("WHERE") + "WHERE".length() + 1, "");
                }

                query.append("\n").append("WHERE");
                queryBuilderPanel.addBlockInBlocksPanel("WHERE", colorBorderBlockWhereAndHaving, colorBackgroundBlockWhereAndHaving);

                String[] conditions = stringBuilder.toString().split("(?=OR )|(?=AND )");

                for (int i = 0; i < conditions.length; i++) {
                    query.append("\n").append(conditions[i]);
                    queryBuilderPanel.addBlockInBlocksPanel(conditions[i], colorBorderBlockWhereAndHaving, colorBackgroundBlockWhereAndHaving);
                }

                query.append(WHITESPACE);
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
    private void addGroupingAndSwapWhereOnHaving(StringBuilder query) {
        if (!groupBy.isEmpty()) {

            StringBuilder stringBuilder = new StringBuilder(groupBy);
            queryBuilderPanel.addBlockInBlocksPanel("GROUP BY", colorBorderBlockGroupBy, colorBackgroundBlockGroupBy);
            stringBuilder.replace(stringBuilder.indexOf("GROUP BY"), stringBuilder.indexOf("GROUP BY") + "GROUP BY".length() + 1, "");

            query.append("\n").append("GROUP BY");

            String[] attributeGroupBY = stringBuilder.toString().split("(?<=,)");

            for (int i = 0; i < attributeGroupBY.length; i++) {
                query.append("\n").append(attributeGroupBY[i]);
                queryBuilderPanel.addBlockInBlocksPanel(attributeGroupBY[i], colorBorderBlockGroupBy, colorBackgroundBlockGroupBy);
            }

            query.append(WHITESPACE);

            if (!where.isEmpty()) {
                swapWhereOnHaving();

                StringBuilder stringBuilderHaving = new StringBuilder(having);

                if (stringBuilderHaving.indexOf("HAVING") >= 0) {
                    stringBuilderHaving.replace(stringBuilderHaving.indexOf("HAVING"), stringBuilderHaving.indexOf("HAVING") + "HAVING".length() + 1, "");
                }

                query.append("\n").append("HAVING").append(WHITESPACE);
                queryBuilderPanel.addBlockInBlocksPanel("HAVING", colorBorderBlockWhereAndHaving, colorBackgroundBlockWhereAndHaving);

                String[] conditions = stringBuilderHaving.toString().split("(?=OR )|(?=AND )");

                for (int i = 0; i < conditions.length; i++) {
                    query.append("\n").append(conditions[i]);
                    queryBuilderPanel.addBlockInBlocksPanel(conditions[i], colorBorderBlockWhereAndHaving, colorBackgroundBlockWhereAndHaving);
                }

                query.append(WHITESPACE);
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
    private void addOrderByInQuery(StringBuilder query) {
        if (!orderBy.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(orderBy);
            queryBuilderPanel.addBlockInBlocksPanel("ORDER BY", colorBorderBlockOrderBy, colorBackgroundBlockOrderBy);
            stringBuilder.replace(stringBuilder.indexOf("ORDER BY"), stringBuilder.indexOf("ORDER BY") + "ORDER BY".length() + 1, "");

            query.append("\n").append("ORDER BY");

            String[] attributeOrderBy = stringBuilder.toString().split("(?<=,)");

            for (int i = 0; i < attributeOrderBy.length; i++) {
                query.append("\n").append(attributeOrderBy[i]);
                queryBuilderPanel.addBlockInBlocksPanel(attributeOrderBy[i], colorBorderBlockOrderBy, colorBackgroundBlockOrderBy);
            }

            query.append(WHITESPACE);
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
    private void addOptimizationInQuery(StringBuilder query) {
        if (!optimization.isEmpty()) {
            query.append("\n").append(optimization);
            queryBuilderPanel.addBlockInBlocksPanel(optimization, colorBorderBlockOptimization, colorBackgroundBlockOptimization);
            query.append(WHITESPACE);
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
     * A method for resetting with values.
     * <p>
     * Метод для сброса значений with.
     */
    private void clearWith() {
        with = "";
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
     * A method for obtaining the optimization value.
     * <p>
     * Метод для получения значения оптимизации.
     */
    public String getOptimization() {
        return optimization;
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
        clearDistinct();
        clearWhere();
        clearHaving();
        clearTable();
        clearGroupBy();
        clearOrderBy();
        clearOptimization();
        clearUnion();
        clearWith();
    }
}