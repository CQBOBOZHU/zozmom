package com.zozmom.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "lyj_usd")
public class UserPer {
	@Column(name = "id")
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "age")
	private String age;
	@Column(name = "useId", isId = true, autoGen = false)
	private String useId;

	public String getUseId() {
		return useId;
	}

	public void setUseId(String useId) {
		this.useId = useId;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
