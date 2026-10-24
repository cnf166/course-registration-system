package com.course_registration_system.utils;

import java.util.UUID;

public final class IdUtils {
    public static String generateUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
