package com.course_registration_system.api.mapper;

import com.course_registration_system.api.dto.request.CreateClassCourseRequest;
import com.course_registration_system.api.dto.response.ClassCourseDetailResponse;
import com.course_registration_system.api.dto.response.ClassCourseSummaryResponse;
import com.course_registration_system.api.dto.response.ScheduleSlotDto;
import com.course_registration_system.business.classcourse.model.ClassCourse;
import com.course_registration_system.business.classcourse.model.ClassCourseWithStats;
import com.course_registration_system.business.classcourse.model.ScheduleSlot;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClassCourseMapperTest {

    @Test
    void toModel_mapsAllFieldsCorrectly() {
        CreateClassCourseRequest request = new CreateClassCourseRequest(
                "Software Architecture",
                3,
                60,
                List.of(new ScheduleSlotDto(2, 1), new ScheduleSlotDto(4, 2))
        );

        ClassCourse model = ClassCourseMapper.toModel(request);

        assertThat(model).isNotNull();
        assertThat(model.getId()).isNull();
        assertThat(model.getName()).isEqualTo("Software Architecture");
        assertThat(model.getCredits()).isEqualTo(3);
        assertThat(model.getCapacity()).isEqualTo(60);
        assertThat(model.getSchedule()).hasSize(2);
        assertThat(model.getSchedule().get(0).dayOfWeek()).isEqualTo(2);
        assertThat(model.getSchedule().get(0).shift()).isEqualTo(1);
        assertThat(model.getSchedule().get(1).dayOfWeek()).isEqualTo(4);
        assertThat(model.getSchedule().get(1).shift()).isEqualTo(2);
    }

    @Test
    void toModel_nullRequest_returnsNull() {
        assertThat(ClassCourseMapper.toModel(null)).isNull();
    }

    @Test
    void toDetailResponse_mapsStatsAndSlots() {
        ClassCourse cls = new ClassCourse(
                "cls-101",
                "Operating Systems",
                3,
                50,
                List.of(new ScheduleSlot(3, 1)),
                Instant.EPOCH
        );
        ClassCourseWithStats stats = new ClassCourseWithStats(cls, 20);

        ClassCourseDetailResponse response = ClassCourseMapper.toDetailResponse(stats);

        assertThat(response).isNotNull();
        assertThat(response.classId()).isEqualTo("cls-101");
        assertThat(response.name()).isEqualTo("Operating Systems");
        assertThat(response.credits()).isEqualTo(3);
        assertThat(response.capacity()).isEqualTo(50);
        assertThat(response.enrolledCount()).isEqualTo(20);
        assertThat(response.availableSeats()).isEqualTo(30);
        assertThat(response.schedule()).hasSize(1);
        assertThat(response.schedule().get(0).dayOfWeek()).isEqualTo(3);
        assertThat(response.schedule().get(0).shift()).isEqualTo(1);
    }

    @Test
    void toSummaryResponseList_mapsAllItems() {
        ClassCourse cls1 = new ClassCourse(
                "cls-1",
                "Course 1",
                2,
                40,
                List.of(new ScheduleSlot(2, 1)),
                Instant.EPOCH
        );
        ClassCourse cls2 = new ClassCourse(
                "cls-2",
                "Course 2",
                3,
                30,
                List.of(),
                Instant.EPOCH
        );

        List<ClassCourseWithStats> list = List.of(
                new ClassCourseWithStats(cls1, 10),
                new ClassCourseWithStats(cls2, 30)
        );

        List<ClassCourseSummaryResponse> summaries = ClassCourseMapper.toSummaryResponseList(list);

        assertThat(summaries).hasSize(2);
        assertThat(summaries.get(0).classId()).isEqualTo("cls-1");
        assertThat(summaries.get(0).availableSeats()).isEqualTo(30);
        assertThat(summaries.get(1).classId()).isEqualTo("cls-2");
        assertThat(summaries.get(1).availableSeats()).isZero();
    }
}
