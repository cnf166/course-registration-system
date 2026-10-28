package com.course_registration_system.business.enrollment.model;

public record EnrollResult(String classId, String status, String reason) {

    public static EnrollResult enrolled(String classId) {
        return new EnrollResult(classId, "ENROLLED", null);
    }

    public static EnrollResult rejected(String classId, String reason) {
        return new EnrollResult(classId, "REJECTED", reason);
    }

    public static EnrollResult retry(String classId) {
        return new EnrollResult(classId, "RETRY", null);
    }

    public static final String ENROLLMENT_PERIOD_CLOSED = "ENROLLMENT_PERIOD_CLOSED";
    public static final String CLASS_NOT_FOUND          = "CLASS_NOT_FOUND";
    public static final String CLASS_FULL               = "CLASS_FULL";
    public static final String ALREADY_ENROLLED         = "ALREADY_ENROLLED";
    public static final String SCHEDULE_CONFLICT        = "SCHEDULE_CONFLICT";
    public static final String CREDIT_LIMIT_EXCEEDED    = "CREDIT_LIMIT_EXCEEDED";
}
