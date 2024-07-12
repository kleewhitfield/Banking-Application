package bankmanagementsystem;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TransactionChart {

    public static ChartPanel createChartPanel(String cardNumber) {
        JFreeChart chart = createChart(createDataset(cardNumber));
        return new ChartPanel(chart);
    }

    private static JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Daily Transaction Summary - Last Week",
                "Date",
                "Amount",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );
        return chart;
    }

    private static CategoryDataset createDataset(String cardNumber) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Calculate the date 7 days ago
            LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
            Date dateOneWeekAgo = Date.from(oneWeekAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());

            String sql = "SELECT DATE(timestamp) AS date, transaction_type, SUM(amount) AS total_amount " +
                         "FROM transactions " +
                         "WHERE user_id = (SELECT id FROM users WHERE card_number = ?) " +
                         "AND timestamp >= ? " +
                         "GROUP BY DATE(timestamp), transaction_type " +
                         "ORDER BY DATE(timestamp), transaction_type";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cardNumber);
            pstmt.setDate(2, new java.sql.Date(dateOneWeekAgo.getTime()));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String date = rs.getDate("date").toString();
                String transactionType = rs.getString("transaction_type");
                double totalAmount = rs.getDouble("total_amount");
                dataset.addValue(totalAmount, transactionType, date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }
}
