package server.com.webHandler.pages.helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.com.webHandler.sql.IDatabaseReader;
import server.htmlBuilder.body.Text;
import server.htmlBuilder.table.ITable;
import server.htmlBuilder.table.ITableData;
import server.htmlBuilder.table.ITableRow;
import server.htmlBuilder.table.Table;
import server.htmlBuilder.table.TableData;
import server.htmlBuilder.table.TableHeader;
import server.htmlBuilder.table.TableRow;

public class AverageTableBuilder implements ITableBuilder {

    private static final Logger LOG = Logger.getLogger(AverageTableBuilder.class.getName());
    private final ResultSet results;
    private final IDatabaseReader dr;

    public AverageTableBuilder(ResultSet results, IDatabaseReader dbReader) {
        this.results = results;
        dr = dbReader;
    }

    @Override
    public ITable getTable() {
        return buildTable();
    }

    private ITable buildTable() {
        ITable table = new Table();
        table.setClassName("center");
        try {
            if (results == null || !results.isBeforeFirst()) {
                return table;
            }
            ITableRow headerRow = new TableRow();
            headerRow.addDataPart(new TableHeader(new Text("Name")));
            headerRow.addDataPart(new TableHeader(new Text("Average Points")));
            headerRow.addDataPart(new TableHeader(new Text("Possible")));
            headerRow.addDataPart(new TableHeader(new Text("Extra Credit")));
            headerRow.addDataPart(new TableHeader(new Text("Autograded")));
            
            table.addRow(headerRow);

            LinkedHashMap<String, GradingData> gradingMap = new LinkedHashMap<>(10);
            results.beforeFirst();
            while (results.next()) {
                int resultID = results.getInt("id");
                try (ResultSet grading = dr.getGradingForResult(resultID)) {
                    while(grading.next()) {
                        String gradingName = grading.getString("name");
                        int gradingID = grading.getInt("id");
                        int pointsPossible = grading.getInt("possible");
                        double autoGradedPercentage = grading.getDouble("auto_graded_percent");
                        boolean extraCredit = grading.getBoolean("extra_credit");
                        
                        GradingData data;
                        if (gradingMap.containsKey(gradingName)) {
                            data = gradingMap.get(gradingName);
                        } else {
                            data = new GradingData(pointsPossible, autoGradedPercentage, extraCredit);
                            gradingMap.put(gradingName, data);
                        }
                        
                        data.addPointTotal(grading.getInt("points"));
                        try (ResultSet tests = dr.getTestsForGrading(gradingID)) {
                            while(tests.next()) {
                                String name = tests.getString("name");
                                double percent = tests.getDouble("percent");
                                boolean autoGraded = tests.getBoolean("auto_graded");
                                data.addTestData(name, percent, autoGraded);
                            }  
                        }
                    }
                }
            }

            for (Entry<String, GradingData> gradingEntry : gradingMap.entrySet()) {
                GradingData gradingData = gradingEntry.getValue();
                ITableRow row = new TableRow();
                row.setClassName("highlight-row");
                row.addDataPart(new TableData(new Text(gradingEntry.getKey())));
                row.addDataPart(new TableData(new Text(roundToString(gradingData.getAveragePoints(), 2))));
                row.addDataPart(new TableData(new Text(Integer.toString(gradingData.getPossible()))));
                row.addDataPart(new TableData(new Text(gradingData.isExtraCredit() ? "Yes" : "No")));
                row.addDataPart(new TableData(new Text(roundToString(gradingData.getPercentAutograded(), 2) + "%")));
                
                table.addRow(row);
                row = new TableRow();
                
                for (Entry<String, GradingData.TestData> testEntry : gradingData.getData()) {
                    GradingData.TestData test = testEntry.getValue();
                    row.addDataPart(new TableData(new Text(testEntry.getKey())));
                    ITableData avgScore = new TableData(new Text(roundToString(test.getAverageScore(), 2) + "%"));
                    avgScore.setColSpan(2);
                    row.addDataPart(avgScore);
                    row.addDataPart(new TableData());
                    row.addDataPart(new TableData(new Text(test.getAutoGrade() ? "Yes" : "No")));

                    table.addRow(row);
                    row = new TableRow();
                }
            }
        } catch (SQLException ex) {
            LOG.log(Level.FINE, null, ex);
        }
        return table;
    }

    private class GradingData {

        private final LinkedHashMap<String, TestData> testMap;
        private int points;
        private int count;
        private final int possible;
        private final double autoGraded;
        private final boolean extraCredit;

        GradingData(int possible, double autoGraded, boolean extraCredit) {
            testMap = new LinkedHashMap<>(5);
            points = 0;
            count = 0;
            this.possible = possible;
            this.autoGraded = autoGraded;
            this.extraCredit = extraCredit;
        }
        
        void addPointTotal(int points) {
            this.points += points;
            count++;
        }

        void addTestData(String name, double percent, boolean autoGraded) {
            if (testMap.containsKey(name)) {
                testMap.get(name).addPoints(percent);
            } else {
                testMap.put(name, new TestData(percent, autoGraded));
            }
        }

        @SuppressWarnings("unchecked")
        Entry<String, TestData>[] getData() {
            Set<Entry<String, TestData>> testSet = testMap.entrySet();
            return (Entry<String, TestData>[]) testMap.entrySet().toArray(new Entry[testSet.size()]);
        }
        
        double getAveragePoints() {
            return ((double)points)/count;
        }
        
        int getPossible() {
            return possible;
        }
        
        double getPercentAutograded() {
            return autoGraded;
        }
        
        boolean isExtraCredit() {
            return extraCredit;
        }

        class TestData {

            private double percent;
            private int count;
            private final boolean autoGraded;

            TestData(double percent, boolean autoGraded) {
                this.percent = percent;
                this.autoGraded = autoGraded;
                count = 1;
            }

            void addPoints(double percent) {
                this.percent += percent;
                this.count++;
            }

            double getAverageScore() {
                return percent / count;
            }

            boolean getAutoGrade() {
                return autoGraded;
            }
        }
    }
    
    private String roundToString(double d, int precision) {
        d = round(d, precision);
        return Double.toString(d);
    }
    
    private double round(double d, int precision) {
        int mult = (int)Math.pow(10, precision);
        d = d * mult;
        d = Math.round(d);
        d = d / mult;
        return d;
    }
}
