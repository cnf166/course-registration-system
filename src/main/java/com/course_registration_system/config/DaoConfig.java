package com.course_registration_system.config;

import com.course_registration_system.data.entity.ClassScheduleEntity;
import com.course_registration_system.data.entity.ClassCourseEntity;
import com.course_registration_system.data.entity.EnrollmentEntity;
import com.course_registration_system.data.entity.EnrollmentPeriodEntity;
import com.course_registration_system.data.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thingai.base.dao.Dao;
import org.thingai.base.log.ILog;
import org.thingai.platform.dao.DaoSqlite;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

// Consider use --enable-native-access=ALL-UNNAMED, due to SQLite JDBC run native C and load with System.load
@Configuration
public class DaoConfig {

	// @DaoTable classes whose tables DaoSqlite creates/migrates on startup
	private static final Class<?>[] DAO_ENTITIES = {
			UserEntity.class,
			ClassCourseEntity.class,
			ClassScheduleEntity.class,
			EnrollmentPeriodEntity.class,
			EnrollmentEntity.class,
	};

	/**
	 * Provides the app-wide Dao backed by the SQLite file at dao.sqlite.path.
	 * DaoSqlite uses HikariCP pool so Spring calls close() on shutdown to release it.
	 */
	@Bean(destroyMethod = "close")
	public Dao dao(@Value("${dao.sqlite.path}") String dbPath) {
		initDaoLog();

		// SQLite creates the db file but not its parent folder
		Path parent = Path.of(dbPath).toAbsolutePath().getParent();
		try {
			Files.createDirectories(parent);
		} catch (IOException e) {
			throw new UncheckedIOException("Cannot create SQLite directory " + parent, e);
		}

		DaoSqlite dao = new DaoSqlite(dbPath);
		dao.initDao(DAO_ENTITIES);
		return dao;
	}

	private void initDaoLog() {
		ILog.LOG_LEVEL = ILog.DEBUG;
		ILog.ENABLE_LOGGING = true;
	}
}
