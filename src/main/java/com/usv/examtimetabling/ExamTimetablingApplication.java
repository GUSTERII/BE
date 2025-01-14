package com.usv.examtimetabling;

import com.usv.examtimetabling.seeding.Seeder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.usv.examtimetabling"})
public class ExamTimetablingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamTimetablingApplication.class, args);
	}
}
