package helper;

import java.sql.*;
import java.util.*;
import database.DatabaseConnection;
import database.Session;

public class MarkCalculator {

    // Immutable result container
    public static final class StudentResults {
        private final List<String> courseMarks;
        private final double gpa;

        public StudentResults(List<String> courseMarks, double gpa) {
            this.courseMarks = Collections.unmodifiableList(new ArrayList<>(courseMarks));
            this.gpa = gpa;
        }

        public List<String> getCourseMarks() {
            return courseMarks;
        }

        public double getGpa() {
            return gpa;
        }
    }

    // Main calculation method
    public static StudentResults calculateResults() {
        List<String> courseResults = new ArrayList<>();
        List<Double> gradePoints = new ArrayList<>();

        String query = "SELECT Course_code, Quiz_01, Quiz_02, Quiz_03, Quiz_04, "
                + "Assignment_01, Assignment_02, Mid_theory, Mid_practical, "
                + "End_theory, End_practical FROM Marks WHERE Stu_id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, Session.loggedInUsername);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String courseCode = rs.getString("Course_code");
                    double finalMark = calculateCourseMark(rs, courseCode);

                    if (finalMark >= 0) { // Valid mark
                        courseResults.add(formatCourseResult(courseCode, finalMark));
                        gradePoints.add(convertMarkToGradePoint(finalMark));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return new StudentResults(Collections.emptyList(), 0.0);
        }

        return new StudentResults(courseResults, calculateOverallGPA(gradePoints));
    }

    // Course-specific mark calculation
    private static double calculateCourseMark(ResultSet rs, String courseCode)
            throws SQLException {
        try {
            // Calculate components
            double quizMark = calculateQuizComponent(rs, courseCode);
            double assignmentMark = calculateAssignmentComponent(rs, courseCode);
            double midMark = calculateMidComponent(rs);
            double finalExamMark = calculateFinalExamComponent(rs);

            // Sum components with validation
            double total = quizMark + assignmentMark + midMark + finalExamMark;
            return validateMark(total);
        } catch (SQLException e) {
            System.err.println("Error processing " + courseCode + ": " + e.getMessage());
            return -1;
        }
    }

    // Component calculation methods
    private static double calculateQuizComponent(ResultSet rs, String courseCode)
            throws SQLException {
        double[] quizzes = {
                rs.getDouble("Quiz_01"),
                rs.getDouble("Quiz_02"),
                rs.getDouble("Quiz_03"),
                courseCode.equals("ICT2122") ? rs.getDouble("Quiz_04") : 0
        };
        Arrays.sort(quizzes);

        return courseCode.equals("ICT2122")
                ? (quizzes[3] + quizzes[2] + quizzes[1]) / 3 * 0.10
                : (quizzes[2] + quizzes[1]) / 2 * 0.10;
    }

    private static double calculateAssignmentComponent(ResultSet rs, String courseCode)
            throws SQLException {
        double avg = (rs.getDouble("Assignment_01") + rs.getDouble("Assignment_02")) / 2;
        return avg * (courseCode.equals("ICT2122") ? 0.10 : 0.20);
    }

    private static double calculateMidComponent(ResultSet rs) throws SQLException {
        return (rs.getDouble("Mid_theory") + rs.getDouble("Mid_practical")) / 2 * 0.20;
    }

    private static double calculateFinalExamComponent(ResultSet rs) throws SQLException {
        return (rs.getDouble("End_theory") + rs.getDouble("End_practical")) / 2 * 0.50;
    }

    // Utility methods
    private static double validateMark(double mark) {
        return Math.min(100, Math.max(0, mark)); // Clamp to 0-100 range
    }

    private static String formatCourseResult(String code, double mark) {
        return String.format("%s: %.2f/100", code, mark);
    }

    private static double convertMarkToGradePoint(double mark) {
        if (mark >= 85) return 4.0;
        if (mark >= 80) return 3.7;
        if (mark >= 75) return 3.3;
        if (mark >= 70) return 3.0;
        if (mark >= 65) return 2.7;
        if (mark >= 60) return 2.3;
        if (mark >= 55) return 2.0;
        if (mark >= 50) return 1.7;
        if (mark >= 45) return 1.3;
        if (mark >= 40) return 1.0;
        return 0.0;
    }

    private static double calculateOverallGPA(List<Double> gradePoints) {
        if (gradePoints.isEmpty()) return 0.0;

        double sum = 0.0;
        for (double gp : gradePoints) {
            sum += gp;
        }
        return sum / gradePoints.size();
    }

    // Convenience method for getting just GPA
    public static double getGPA() {
        return calculateResults().getGpa();
    }
}