package com.bookstore.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.util.List;
import java.util.Map;

/**
 * Chart Utility - Tạo biểu đồ thống kê sử dụng JavaFX native charts
 */
public class ChartUtil {

    private static final String REVENUE_SERIES = "Doanh thu";
    private static final String QUANTITY_SERIES = "Số lượng";

    /**
     * Tạo biểu đồ cột doanh thu theo ngày
     */
    public static Chart createRevenueBarChart(List<String> dates, List<Double> revenues) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Ngày");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Doanh thu (VNĐ)");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Doanh thu theo ngày");
        chart.setLegendVisible(true);
        chart.setCategoryGap(20.0);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(REVENUE_SERIES);

        for (int i = 0; i < dates.size(); i++) {
            series.getData().add(new XYChart.Data<>(dates.get(i), revenues.get(i)));
        }

        chart.getData().add(series);

        // Style the chart
        chart.setStyle("-fx-background-color: white; -fx-padding: 10;");

        return chart;
    }

    /**
     * Tạo biểu đồ cột số lượng sách theo thể loại
     */
    public static Chart createCategoryBarChart(Map<String, Integer> categoryData) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Thể loại");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Số lượng");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Sách theo thể loại");
        chart.setLegendVisible(true);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(QUANTITY_SERIES);

        for (Map.Entry<String, Integer> entry : categoryData.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        chart.getData().add(series);

        return chart;
    }

    /**
     * Tạo biểu đồ tròn (Pie Chart) cho thể loại sách
     */
    public static Chart createCategoryPieChart(Map<String, Integer> categoryData) {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : categoryData.entrySet()) {
            pieData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        PieChart chart = new PieChart(pieData);
        chart.setTitle("Sách theo thể loại");
        chart.setLegendVisible(true);
        chart.setClockwise(true);
        chart.setLabelLineLength(50);

        return chart;
    }

    /**
     * Tạo biểu đồ top khách hàng
     */
    public static Chart createTopCustomerChart(List<String> names, List<Integer> points) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Khách hàng");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Điểm tích lũy");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Top khách hàng tích lũy");
        chart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Điểm");

        for (int i = 0; i < names.size(); i++) {
            series.getData().add(new XYChart.Data<>(names.get(i), points.get(i)));
        }

        chart.getData().add(series);

        return chart;
    }

    /**
     * Tạo biểu đồ tồn kho thấp
     */
    public static Chart createLowStockChart(List<String> bookNames, List<Integer> quantities) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Tên sách");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Số lượng tồn");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Sách sắp hết hàng");
        chart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (int i = 0; i < bookNames.size(); i++) {
            series.getData().add(new XYChart.Data<>(bookNames.get(i), quantities.get(i)));
        }

        chart.getData().add(series);

        return chart;
    }
}
