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
        try {
            if (results == null || !results.isBeforeFirst()) {
                return table;
            }
            ITableRow headerRow = new TableRow();
            headerRow.addDataPart(new TableHeader(new Text("Grading Name")));
            headerRow.addDataPart(new TableHeader(new Text("Test Name")));
            headerRow.addDataPart(new TableHeader(new Text("Average Percent Correct")));
            headerRow.addDataPart(new TableHeader(new Text("Autograded")));
            
            table.addRow(headerRow);

            LinkedHashMap<String, GradingData> gradingMap = new LinkedHashMap<>(10);
            results.beforeFirst();
            System.out.println("um...");
            while (results.next()) {
                int resultID = results.getInt("id");
                System.out.println(resultID);
                GradingData data = new GradingData();
                System.out.println("grading name = " + results.getString("name"));
                try (ResultSet grading = dr.getGradingForResult(resultID)) {
                    int gradingID = grading.getInt("id");
                    try (ResultSet tests = dr.getTestsForGrading(gradingID)) {
                        String name = tests.getString("name");
                        double percent = tests.getDouble("percent");
                        boolean autoGraded = tests.getBoolean("auto_graded");
                        data.addTestData(name, percent, autoGraded);
                        System.out.println("test name = " + name);
                    }
                }
                gradingMap.put(results.getString("name"), data);
            }

            for (Entry<String, GradingData> gradingEntry : gradingMap.entrySet()) {
                Entry<String, GradingData.TestData>[] testEntries = gradingEntry.getValue().getData();
                ITableRow row = new TableRow();
                ITableData grading = new TableData();
                int gradeSpan = testEntries.length;
                if (gradeSpan > 1) {
                    grading.setRowSpan(gradeSpan);
                }
                grading.addContent(new Text(gradingEntry.getKey()));
                row.addDataPart(grading);
                for (Entry<String, GradingData.TestData> testEntry : gradingEntry.getValue().getData()) {
                    GradingData.TestData test = testEntry.getValue();
                    row.addDataPart(new TableData(new Text(testEntry.getKey())));
                    row.addDataPart(new TableData(new Text("" + test.getAverageScore())));
                    row.addDataPart(new TableData(new Text("" + test.getAutoGrade())));

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

        GradingData() {
            testMap = new LinkedHashMap<>(5);
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
}
