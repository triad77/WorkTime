package com.kbfng.worktime;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.kbfng.worktime.dto.EmpCreateForm;
import com.kbfng.worktime.entity.CorpEnum;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@SpringBootTest(properties = { "spring.config.location=classpath:application-local.properties" })
class WorkTimeApplicationTests {

	private static Validator validator;

	@BeforeEach
	void setUpValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void empFormValid() {

		EmpCreateForm form = new EmpCreateForm();
		form.setEmpNo("T001491");
		form.setEmpNm("오성균");
		form.setIpAddr("127.0.0.1");
		form.setContract("01000000000");
		form.setCtrcStDate("19000101");
		form.setCtrcEdDate("99991231");
		form.setBelngCorp(CorpEnum.KBDS.getCode());
		form.setPrjId(1);

		Set<ConstraintViolation<EmpCreateForm>> violations = validator.validate(form);

		assertThat(violations.size()).isEqualTo(0);
		//assertThat(violations).extracting("message").containsOnly("잘못된 (소속)회사코드입니다");
	}

	/*
	 * @Test void contextLoads() {
	 * 
	 * }
	 */
}
