package com.taotao.cloud.standalone.domain.dept.repository;

import com.taotao.cloud.standalone.domain.dept.entity.DeptEntity;

public interface DeptDomainRepository {
	/**
	 * 新增部门.
	 *
	 * @param dept 部门对象
	 */
	void create(DeptEntity dept);

	/**
	 * 修改部门.
	 *
	 * @param dept 部门对象
	 */
	void modify(DeptEntity dept);

	/**
	 * 根据ID删除部门.
	 *
	 * @param ids IDS
	 */
	void remove(Long[] ids);
}
