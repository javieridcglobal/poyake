package com.jsantos.common.util;

import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;

import com.jsantos.orm.MainDb;

public class PostingDate {
	
	public static Date get() {
		JdbcTemplate t = new JdbcTemplate(MainDb.getMainDataSource());
		return t.queryForObject("select config.getPostingDate()", Date.class);
	}
}
