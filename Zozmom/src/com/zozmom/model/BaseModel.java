package com.zozmom.model;

import java.io.Serializable;
import java.util.Observable;

import org.xutils.db.annotation.Column;


public abstract class BaseModel  implements Serializable {
	@Column(name = "id")
	int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
