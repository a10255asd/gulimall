package com.atjixue.gulimall.member;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


//@SpringBootTest
public class GulimallMemberApplicationTests {
	@Test
	public void contextLoads() {
//		String s = DigestUtils.md5Hex("1122334455");
//		Md5Crypt.md5Crypt("123456".getBytes());
//		System.out.println(s);

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encode = passwordEncoder.encode("123456");
	}

}
