package com.course_registration_system.api.mapper;

import com.course_registration_system.api.dto.request.CreateClassCourseRequest;
import com.course_registration_system.api.dto.response.ClassCourseDetailResponse;
import com.course_registration_system.api.dto.response.ClassCourseSummaryResponse;
import com.course_registration_system.api.dto.response.ScheduleSlotDto;
import com.course_registration_system.business.classcourse.model.ClassCourse;
import com.course_registration_system.business.classcourse.model.ClassCourseWithStats;
import com.course_registration_system.business.classcourse.model.ScheduleSlot;

import java.util.List;

public final class ClassCourseMapper {

    private ClassCourseMapper() {}

    public static ClassCourse toModel(CreateClassCourseRequest request) {
        if (request == null) {
            return null;
        }
        List<ScheduleSlot> domainSlots = null;
        if (request.schedule() != null) {
            domainSlots = request.schedule().stream()
                    .map(s -> s == null ? null : new ScheduleSlot(s.dayOfWeek(), s.shift()))
                    .toList();
        }
        return new ClassCourse(
                null,
                request.name(),
                request.credits() != null ? request.credits() : 0,
                request.capacity() != null ? request.capacity() : 0,
                domainSlots,
                null
        );
    }

    public static ClassCourseDetailResponse toDetailResponse(ClassCourseWithStats stats) {
        if (stats == null) {
            return null;
        }
        ClassCourse c = stats.classCourse();
        return new ClassCourseDetailResponse(
                c.getId(),
                c.getName(),
                c.getCredits(),
                c.getCapacity(),
                stats.enrolledCount(),
                stats.availableSeats(),
                toDtoSlots(c.getSchedule())
        );
    }

    public static ClassCourseDetailResponse toDetailResponse(ClassCourse classCourse, int enrolledCount) {
        if (classCourse == null) {
            return null;
        }
        return toDetailResponse(new ClassCourseWithStats(classCourse, enrolledCount));
    }

    public static ClassCourseSummaryResponse toSummaryResponse(ClassCourseWithStats stats) {
        if (stats == null) {
            return null;
        }
        ClassCourse c = stats.classCourse();
        return new ClassCourseSummaryResponse(
                c.getId(),
                c.getName(),
                c.getCredits(),
                c.getCapacity(),
                stats.enrolledCount(),
                stats.availableSeats(),
                toDtoSlots(c.getSchedule())
        );
    }

    public static List<ClassCourseSummaryResponse> toSummaryResponseList(List<ClassCourseWithStats> list) {
        if (list == null) {
            return List.of();
        }
        return list.stream()
                .map(ClassCourseMapper::toSummaryResponse)
                .toList();
    }

    private static List<ScheduleSlotDto> toDtoSlots(List<ScheduleSlot> slots) {
        if (slots == null) {
            return List.of();
        }
        return slots.stream()
                .filter(s -> s != null)
                .map(s -> new ScheduleSlotDto(s.dayOfWeek(), s.shift()))
                .toList();
    }
}
