package com.projectmanagerapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="parent")
public class ParentEntity {
	
	@Id
	@Column(name="parent_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer parentId;
	
	@Column(name="parent")
	private String parent;

	public ParentEntity() {
		super();
	}

	public Integer getParentId() {
		return parentId;
	}
	
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

}
