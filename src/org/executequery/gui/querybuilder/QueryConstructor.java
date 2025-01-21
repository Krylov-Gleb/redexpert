package org.executequery.gui.querybuilder;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class QueryConstructor {

    private final QBPanel queryBuilderPanel;
    private final String EMPTINESS = "";
    private final String WHITESPACE = " ";
    private final String STAR = "*";

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

    private boolean isChangingValueClick = true;

    public QueryConstructor(QBPanel queryBuilderPanel) {
        this.queryBuilderPanel = queryBuilderPanel;
    }

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

    private void addFrom(StringBuilder query) {
        query.append("\n").append("FROM").append(WHITESPACE);
        queryBuilderPanel.addBlockInBlocksPanel("FROM", colorBorderBlockFrom, colorBackgroundBlockFrom);
    }

    private void addSelect(StringBuilder query) {
        query.append("SELECT").append(WHITESPACE);
        queryBuilderPanel.addBlockInBlocksPanel("SELECT", colorBorderBlockSelect, colorBackgroundBlockSelect);
    }

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

    private void addDistinct(StringBuilder query) {
        if (!distinct.isEmpty()) {
            query.append("\n").append(distinct).append(WHITESPACE);
            queryBuilderPanel.addBlockInBlocksPanel(distinct, colorBorderBlockAdditions, colorBackgroundBlockAdditions);
        }
    }

    private void addWith(StringBuilder query) {
        if (!with.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(with);
            stringBuilder.replace(stringBuilder.indexOf("WITH RECURSIVE"), stringBuilder.indexOf("WITH RECURSIVE") + "WITH RECURSIVE".length() + 1, "");

            query.append("WITH RECURSIVE");
            queryBuilderPanel.addBlockInBlocksPanel("WITH RECURSIVE", colorBorderBlockWith, colorBackgroundBlockWith);

            String[] withSplit = stringBuilder.toString().split("(?<=\\),)");

            for (String s : withSplit) {
                query.append("\n");

                StringBuilder stringBuilderWith = new StringBuilder(s);
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

    public String getWith() {
        return with;
    }

    public void setWith(String with, String stepUpOrBack) {

        if (stepUpOrBack.equals("stepBack")) {
            queryBuilderPanel.addStepBackActionInHistory("Set With " + this.with);
        }

        if (stepUpOrBack.equals("stepUp")) {
            queryBuilderPanel.addStepUpActionInHistory("Set With " + this.with);
        }

        this.with = with;
    }

    private void addFirstInQuery(StringBuilder query) {
        if (!first.isEmpty()) {
            query.append("\n").append(first).append(WHITESPACE);
            queryBuilderPanel.addBlockInBlocksPanel(first, colorBorderBlockAdditions, colorBackgroundBlockAdditions);
        }
    }

    public void setFirst(String number, String stepUpOrBack) {

        if (stepUpOrBack.equals("stepBack")) {
            queryBuilderPanel.addStepBackActionInHistory("Set First " + first);
        }

        if (stepUpOrBack.equals("stepUp")) {
            queryBuilderPanel.addStepUpActionInHistory("Set First " + first);
        }

        if (!number.isEmpty()) {
            first = "FIRST " + number;
        } else {
            first = EMPTINESS;
        }
    }

    public String getFirst() {
        if (first.isEmpty()) {
            return "";
        } else {
            return first.split(" ")[1];
        }
    }

    private void clearFirst() {
        first = EMPTINESS;
    }

    private void clearDistinct() {
        distinct = "";
    }

    private void addSkipInQuery(StringBuilder query) {
        if (!skip.isEmpty()) {
            query.append("\n").append(skip).append(WHITESPACE);
            queryBuilderPanel.addBlockInBlocksPanel(skip, colorBorderBlockAdditions, colorBackgroundBlockAdditions);
        }
    }

    public void setSkip(String number, String stepUpOrBack) {

        if (stepUpOrBack.equals("stepBack")) {
            queryBuilderPanel.addStepBackActionInHistory("Set Skip " + skip);
        }

        if (stepUpOrBack.equals("stepUp")) {
            queryBuilderPanel.addStepUpActionInHistory("Set Skip " + skip);
        }

        if (!number.isEmpty()) {
            skip = "SKIP " + number;
        } else {
            skip = EMPTINESS;
        }
    }

    public String getSkip() {
        if (skip.isEmpty()) {
            return "";
        } else {
            return skip.split(" ")[1];
        }
    }

    private void clearSkip() {
        skip = EMPTINESS;
    }

    public void setDistinct(String distinct, String stepUpOrBack) {

        if (stepUpOrBack.equals("stepBack")) {
            queryBuilderPanel.addStepBackActionInHistory("Set Distinct " + this.distinct);
        }

        if (stepUpOrBack.equals("stepUp")) {
            queryBuilderPanel.addStepUpActionInHistory("Set Distinct " + this.distinct);
        }

        this.distinct = distinct;
    }

    public String getDistinct() {
        return distinct;
    }

    private void addAttributesInQuery() {
        if (attribute.isEmpty()) {
            attribute = STAR;
        }
    }

    public void setAttributes(ArrayList<JTable> tablesOnOutputPanel) {
        StringBuilder stringBuilder = new StringBuilder();

        for (JTable jTable : tablesOnOutputPanel) {
            if (table.indexOf(jTable.getColumnName(0)) == 0) {
                for (int j = 0; j < jTable.getRowCount(); j++) {
                    if ((boolean) jTable.getValueAt(j, 1)) {
                        if (queryBuilderPanel.getBlocksPanel().getComponents().length == 1) {
                            stringBuilder.append(jTable.getColumnName(0)).append(".").append(jTable.getValueAt(j, 0)).append(",");

                        } else {
                            if (table.contains(jTable.getColumnName(0))) {
                                stringBuilder.append(jTable.getColumnName(0)).append(".").append(jTable.getValueAt(j, 0)).append(",");
                            }
                        }

                    }
                }
            } else {
                if (table.contains(" " + jTable.getColumnName(0) + " ")) {
                    for (int j = 0; j < jTable.getRowCount(); j++) {
                        if ((boolean) jTable.getValueAt(j, 1)) {
                            if (queryBuilderPanel.getBlocksPanel().getComponents().length == 1) {
                                stringBuilder.append(jTable.getColumnName(0)).append(".").append(jTable.getValueAt(j, 0)).append(",");

                            } else {
                                if (table.contains(jTable.getColumnName(0))) {
                                    stringBuilder.append(jTable.getColumnName(0)).append(".").append(jTable.getValueAt(j, 0)).append(",");
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

    public void clearAttribute() {
        attribute = STAR;
    }

    public String getAttribute() {
        return attribute;
    }

    public void replaceAttribute(String attribute, String stepUpOrBack) {

        if (stepUpOrBack.equals("stepBack")) {
            queryBuilderPanel.addStepBackActionInHistory("Set Attribute " + this.attribute);
        }

        if (stepUpOrBack.equals("stepUp")) {
            queryBuilderPanel.addStepUpActionInHistory("Set Attribute " + this.attribute);
        }

        this.attribute = attribute;
    }

    public void replaceAttribute(String attribute) {
        this.attribute = attribute;
    }

    private void addFunctionIsEmpty() {
        if (functions.isEmpty()) {
            functions = "";
        }
    }

    private void addFunctions(StringBuilder query) {
        if (!functions.isEmpty()) {
            if (attribute.equals("*")) {
                attribute = "";

                if (functions.indexOf(",") == 0) {
                    functions = functions.substring(1);
                }

                query.append(WHITESPACE).append(attribute);

                String[] functionElements = functions.split("(?<=\\),)");

                for (String functionElement : functionElements) {
                    String[] functionsElementIsAlias = functionElement.split("(?<=\",)");
                    for (String elementIsAlias : functionsElementIsAlias) {
                        query.append("\n").append(elementIsAlias);
                        queryBuilderPanel.addBlockInBlocksPanel(elementIsAlias, colorBorderBlockFunctionsAndAttributes, colorBackgroundBlockFunctionsAndAttributes);
                    }
                }

                query.append(WHITESPACE);

            } else {
                if (attribute.lastIndexOf(",") != attribute.length() - 1) {
                    attribute = attribute + ",";
                }

                query.append(WHITESPACE);

                String[] attributes = attribute.split("(?<=,)");

                for (String s : attributes) {
                    query.append("\n").append(s);
                    queryBuilderPanel.addBlockInBlocksPanel(s, colorBorderBlockFunctionsAndAttributes, colorBackgroundBlockFunctionsAndAttributes);
                }

                String[] functionElements = functions.split("(?<=\\),)");

                for (String functionElement : functionElements) {
                    String[] functionsElementIsAlias = functionElement.split("(?<=\",)");
                    for (String elementIsAlias : functionsElementIsAlias) {
                        query.append("\n").append(elementIsAlias);
                        queryBuilderPanel.addBlockInBlocksPanel(elementIsAlias, colorBorderBlockFunctionsAndAttributes, colorBackgroundBlockFunctionsAndAttributes);
                    }
                }

                query.append(WHITESPACE);
            }
        } else {
            query.append(WHITESPACE);

            String[] attributes = attribute.split("(?<=,)");

            for (String s : attributes) {
                query.append("\n").append(s);
                queryBuilderPanel.addBlockInBlocksPanel(s, colorBorderBlockFunctionsAndAttributes, colorBackgroundBlockFunctionsAndAttributes);
            }

            query.append(WHITESPACE);
        }
    }

    public void clearFunction() {
        functions = "";
    }

    public String getFunctions() {
        return functions;
    }

    public void replaceFunctions(String functions, String stepUpOrBack) {

        if (stepUpOrBack.equals("stepBack")) {
            queryBuilderPanel.addStepBackActionInHistory("Set Function " + this.functions);
        }

        if (stepUpOrBack.equals("stepUp")) {
            queryBuilderPanel.addStepUpActionInHistory("Set Function " + this.functions);
        }

        this.functions = functions;
    }

    private void addTableInQuery(StringBuilder query) {
        query.append(WHITESPACE);

        String[] joins = table.split("(?=INNER JOIN)|(?=LEFT JOIN)|(?=RIGHT JOIN)" +
                "|(?=FULL OUTER JOIN)|(?=CROSS JOIN)|(?=NATURAL JOIN)");

        for (String join : joins) {
            String[] splitOn = join.split("(?=ON)");
            for (String s : splitOn) {
                query.append("\n").append(s);
                queryBuilderPanel.addBlockInBlocksPanel(s, colorBorderBlockTable, colorBackgroundBlockTable);
            }
        }

        query.append(WHITESPACE);
    }

    public void setTable(String Table, String stepUpOrBack) {
        if (stepUpOrBack.equals("stepBack")) {
            queryBuilderPanel.addStepBackActionInHistory("Set Table " + this.table);
        }

        if (stepUpOrBack.equals("stepUp")) {
            queryBuilderPanel.addStepUpActionInHistory("Set Table " + this.table);
        }

        this.table = Table;
    }

    public void setTable(String Table) {
        this.table = Table;
    }

    public void clearTable() {
        if (!queryBuilderPanel.getListTable().isEmpty()) {
            table = queryBuilderPanel.getListTable().get(0).getColumnName(0);
        } else {
            this.table = "EMPLOYEE";
        }
    }

    public String getTable() {
        return table;
    }

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

                for (String condition : conditions) {
                    query.append("\n").append(condition);
                    queryBuilderPanel.addBlockInBlocksPanel(condition, colorBorderBlockWhereAndHaving, colorBackgroundBlockWhereAndHaving);
                }

                query.append(WHITESPACE);
            }
        }
    }

    public void clearWhere() {
        where = "";
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where, String stepUpOrBack) {

        if (stepUpOrBack.equals("stepBack")) {
            queryBuilderPanel.addStepBackActionInHistory("Set Where " + this.where);
        }

        if (stepUpOrBack.equals("stepUp")) {
            queryBuilderPanel.addStepUpActionInHistory("Set Where " + this.where);
        }

        this.where = where;
    }

    public void swapWhereOnHaving() {
        if (!where.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(where);
            stringBuilder.replace(stringBuilder.indexOf("WHERE"), stringBuilder.indexOf("WHERE") + 5, "HAVING");
            having = stringBuilder.toString();
        }
    }

    private void addGroupingAndSwapWhereOnHaving(StringBuilder query) {
        if (!groupBy.isEmpty()) {

            StringBuilder stringBuilder = new StringBuilder(groupBy);
            queryBuilderPanel.addBlockInBlocksPanel("GROUP BY", colorBorderBlockGroupBy, colorBackgroundBlockGroupBy);
            stringBuilder.replace(stringBuilder.indexOf("GROUP BY"), stringBuilder.indexOf("GROUP BY") + "GROUP BY".length() + 1, "");

            query.append("\n").append("GROUP BY");

            String[] attributeGroupBY = stringBuilder.toString().split("(?<=,)");

            for (String s : attributeGroupBY) {
                query.append("\n").append(s);
                queryBuilderPanel.addBlockInBlocksPanel(s, colorBorderBlockGroupBy, colorBackgroundBlockGroupBy);
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

                for (String condition : conditions) {
                    query.append("\n").append(condition);
                    queryBuilderPanel.addBlockInBlocksPanel(condition, colorBorderBlockWhereAndHaving, colorBackgroundBlockWhereAndHaving);
                }

                query.append(WHITESPACE);
            }
        }
    }

    public void clearGroupBy() {
        groupBy = "";
    }

    public void clearHaving() {
        having = "";
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy, String stepUpOrBack) {

        if (stepUpOrBack.equals("stepBack")) {
            queryBuilderPanel.addStepBackActionInHistory("Set GroupBy " + this.groupBy);
        }

        if (stepUpOrBack.equals("stepUp")) {
            queryBuilderPanel.addStepUpActionInHistory("Set GroupBy " + this.groupBy);
        }

        this.groupBy = groupBy;
    }

    private void addOrderByInQuery(StringBuilder query) {
        if (!orderBy.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(orderBy);
            queryBuilderPanel.addBlockInBlocksPanel("ORDER BY", colorBorderBlockOrderBy, colorBackgroundBlockOrderBy);
            stringBuilder.replace(stringBuilder.indexOf("ORDER BY"), stringBuilder.indexOf("ORDER BY") + "ORDER BY".length() + 1, "");

            query.append("\n").append("ORDER BY");

            String[] attributeOrderBy = stringBuilder.toString().split("(?<=,)");

            for (String s : attributeOrderBy) {
                query.append("\n").append(s);
                queryBuilderPanel.addBlockInBlocksPanel(s, colorBorderBlockOrderBy, colorBackgroundBlockOrderBy);
            }

            query.append(WHITESPACE);
        }
    }

    public void clearOrderBy() {
        orderBy = "";
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy, String stepUpOrBack) {

        if (stepUpOrBack.equals("stepBack")) {
            queryBuilderPanel.addStepBackActionInHistory("Set OrderBy " + this.orderBy);
        }

        if (stepUpOrBack.equals("stepUp")) {
            queryBuilderPanel.addStepUpActionInHistory("Set OrderBy " + this.orderBy);
        }

        this.orderBy = orderBy;
    }

    private void addOptimizationInQuery(StringBuilder query) {
        if (!optimization.isEmpty()) {
            query.append("\n").append(optimization);
            queryBuilderPanel.addBlockInBlocksPanel(optimization, colorBorderBlockOptimization, colorBackgroundBlockOptimization);
            query.append(WHITESPACE);
        }
    }

    public void setOptimization(String optimization, String stepUpOrBack) {

        if (stepUpOrBack.equals("stepBack")) {
            queryBuilderPanel.addStepBackActionInHistory("Set Optimize " + this.optimization);
        }

        if (stepUpOrBack.equals("stepUp")) {
            queryBuilderPanel.addStepUpActionInHistory("Set Optimize " + this.optimization);
        }

        if (!optimization.isEmpty()) {
            this.optimization = optimization;
        } else {
            this.optimization = "";
        }
    }

    public void clearOptimization() {
        optimization = "";
    }

    public void setUnion(String union, String stepUpOrBack) {

        if (stepUpOrBack.equals("stepBack")) {
            queryBuilderPanel.addStepBackActionInHistory("Set Union " + this.union);
        }

        if (stepUpOrBack.equals("stepUp")) {
            queryBuilderPanel.addStepUpActionInHistory("Set Union " + this.union);
        }

        this.union = union;
    }

    private void clearUnion() {
        union = "";
    }

    private void clearWith() {
        with = "";
    }

    public String getUnion() {
        return union;
    }

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

    public void setChangingValueClick(boolean values) {
        isChangingValueClick = values;
    }

    public boolean getChangingValueClick() {
        return isChangingValueClick;
    }
}