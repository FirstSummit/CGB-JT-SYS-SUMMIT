package com.jt.sys.entity;

import java.io.Serializable;

import com.jt.common.entity.BaseEntity;

public class SysDepartment extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -6682350420965526767L;
	private Integer id;
	private String name;
	private Integer parentId;
	private Integer managerId;
	private Integer sort;
	private Integer count = 0;
	private String note;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getManagerId() {
		return managerId;
	}

	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "SysDepartment [id=" + id + ", name=" + name + ", parentId=" + parentId + ", managerId=" + managerId
				+ ", sort=" + sort + ", count=" + count + ", note=" + note + "]";
	}

}
