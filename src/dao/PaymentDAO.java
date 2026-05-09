/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Pololoers
 */
import database.DatabaseConnection;
import models.Payment;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public int recordPayment(Payment payment) throws SQLException {
        String sql = """
            INSERT INTO payment (
                student_id,
                amount,
                payment_method,
                payment_plan,
                reference_number,
                payment_date
            )
            VALUES (?, ?, ?, ?, ?, ?)
            RETURNING payment_id
        """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payment.getStudentId());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentMethod());
            stmt.setString(4, payment.getPaymentPlan());
            stmt.setString(5, payment.getReferenceNumber());
            stmt.setDate(6, Date.valueOf(payment.getPaymentDate()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("payment_id");
                }
            }
        }

        throw new SQLException("Failed to record payment.");
    }

    public List<Payment> findPaymentsByStudentId(int studentId) throws SQLException {
        List<Payment> payments = new ArrayList<>();

        String sql = """
            SELECT
                payment_id,
                student_id,
                amount,
                payment_method,
                payment_plan,
                reference_number,
                payment_date
            FROM payment
            WHERE student_id = ?
            ORDER BY payment_date DESC, payment_id DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }
        }

        return payments;
    }

    public BigDecimal getTotalPaidByStudentId(int studentId) throws SQLException {
        String sql = """
            SELECT COALESCE(SUM(amount), 0) AS total_paid
            FROM payment
            WHERE student_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total_paid");
                }
            }
        }

        return BigDecimal.ZERO;
    }

    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();

        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setStudentId(rs.getInt("student_id"));
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setPaymentPlan(rs.getString("payment_plan"));
        payment.setReferenceNumber(rs.getString("reference_number"));

        Date paymentDate = rs.getDate("payment_date");
        if (paymentDate != null) {
            payment.setPaymentDate(paymentDate.toLocalDate());
        }

        return payment;
    }
}
