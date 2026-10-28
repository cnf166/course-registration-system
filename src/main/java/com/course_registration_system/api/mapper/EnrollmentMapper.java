package com.course_registration_system.api.mapper;

import com.course_registration_system.api.dto.response.BulkEnrollResponse;
import com.course_registration_system.api.dto.response.EnrollResultItem;
import com.course_registration_system.api.dto.response.MyClassItem;
import com.course_registration_system.api.dto.response.MyClassesResponse;
import com.course_registration_system.business.classcourse.model.ClassCourse;
import com.course_registration_system.business.enrollment.model.EnrollResult;
import com.course_registration_system.business.enrollment.model.Enrollment;

import java.util.List;
import java.util.Map;

public final class EnrollmentMapper {

    private EnrollmentMapper() {}

    public static BulkEnrollResponse toBulkResponse(List<EnrollResult> results) {
        List<EnrollResultItem> items = results.stream()
                .map(r -> new EnrollResultItem(r.classId(), r.status(), r.reason()))
                .toList();
        return new BulkEnrollResponse(items);
    }

    public static MyClassesResponse toMyClassesResponse(List<Enrollment> enrollments,
                                                        Map<String, ClassCourse> classById) {
        List<MyClassItem> items = enrollments.stream()
                .filter(e -> classById.containsKey(e.getClassId()))
                .map(e -> {
                    ClassCourse cls = classById.get(e.getClassId());
                    return new MyClassItem(cls.getId(), cls.getName(), cls.getCredits(), "ENROLLED");
                })
                .toList();

        int totalCredits = items.stream().mapToInt(MyClassItem::credits).sum();
        return new MyClassesResponse(items, totalCredits);
    }
}
