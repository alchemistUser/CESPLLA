/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author Pololoers
 */
import dao.EnrollmentDAO;

import database.DatabaseConnection;

import java.sql.Connection;

import models.Enrollment;

public class ReEnrollmentService {

    private final EnrollmentDAO enrollmentDAO;

    public ReEnrollmentService() {

        enrollmentDAO = new EnrollmentDAO();
    }

    // =====================================================
    // CREATE RE-ENROLLMENT
    // =====================================================
    public int createReEnrollment(
            Enrollment enrollment
    ) throws Exception {

        Connection conn = null;

        try {

            conn = DatabaseConnection.getConnection();

            conn.setAutoCommit(false);

            int enrollmentId
                    = enrollmentDAO.createEnrollment(
                            conn,
                            enrollment
                    );

            conn.commit();

            return enrollmentId;

        } catch (Exception ex) {

            if (conn != null) {
                conn.rollback();
            }

            throw ex;

        } finally {

            if (conn != null) {

                conn.setAutoCommit(true);

                conn.close();
            }
        }
    }
}
