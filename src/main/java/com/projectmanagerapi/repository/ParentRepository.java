package com.projectmanagerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projectmanagerapi.entity.ParentEntity;

@Repository("parentRepository")
public interface ParentRepository extends JpaRepository<ParentEntity, Integer> {

	@Query("select count(p) from ParentEntity p where p.parent = :parent")
	public Integer getParentCount(@Param("parent") String parent);
	
	@Query("select p.parentId from ParentEntity p where p.parent = :parent")
	public Integer getParentId(@Param("parent") String parent);
	
	@Query("select p from ParentEntity p where p.parentId = :parentId")
	public ParentEntity getForParentId(@Param("parentId") Integer parenIdt);
}
