package com.projectmanagerapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectmanagerapi.entity.ParentEntity;
import com.projectmanagerapi.repository.ParentRepository;

@Service("parentService")
public class ParentService {

	@Autowired
	ParentRepository parentRepository;
	
	public List<ParentEntity> getAllParents() {
		return parentRepository.findAll();
	}
	
	public Integer dupCheck(String parent) {
		return parentRepository.getParentCount(parent);
	}
	
	public ParentEntity getParent(Integer parentId) {
		return parentRepository.getForParentId(parentId);
	}
	
	public Integer getParentId(String parent) {
		return parentRepository.getParentId(parent);
	}
	
	public void addParent(ParentEntity parent) {
		parentRepository.save(parent);
	}
}
