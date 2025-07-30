package com.rays.dao;

import java.util.ArrayList;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.rays.dto.DepartmentDTO;



@Repository
public class DepartmentDAO {
	
	@PersistenceContext
	public EntityManager entityManager;

	public long add(DepartmentDTO dto) {
		entityManager.persist(dto);
		return dto.getId();
	}

	public void update(DepartmentDTO dto) {
		entityManager.merge(dto);
	}

	public void delete(DepartmentDTO dto) {
		entityManager.remove(dto);
	}

	public DepartmentDTO findByPk(long pk) {
		DepartmentDTO dto = entityManager.find(DepartmentDTO.class, pk);
		return dto;
	}

	public DepartmentDTO findByUniqueKey(String attribute, String value) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DepartmentDTO> cq = builder.createQuery(DepartmentDTO.class);
		Root<DepartmentDTO> qroot = cq.from(DepartmentDTO.class);
		Predicate condition = builder.equal(qroot.get(attribute), value);

		cq.where(condition);
		TypedQuery<DepartmentDTO> tq = entityManager.createQuery(cq);
		List<DepartmentDTO> list = tq.getResultList();
		DepartmentDTO dto = null;
		if (list.size() > 0) {
			dto = list.get(0);
		}

		return dto;

	}
	public List search(DepartmentDTO dto, int pageNo, int pageSize) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<DepartmentDTO> cq = builder.createQuery(DepartmentDTO.class);

		Root<DepartmentDTO> qRoot = cq.from(DepartmentDTO.class);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		if (dto != null) {

			if (dto.getName() != null && dto.getName().length() > 0) {
				predicateList.add(builder.like(qRoot.get("name"), dto.getName() + "%"));
			}

			if (dto.getLocation() != null && dto.getLocation().length() > 0) {
				predicateList.add(builder.like(qRoot.get("location"), dto.getLocation() + "%"));
			}
			
			if (dto.getHeadOfDepartment() != null && dto.getHeadOfDepartment().length() > 0) {
				predicateList.add(builder.like(qRoot.get("headOfDepartment"), dto.getHeadOfDepartment() + "%"));
			}
		}

		cq.where(predicateList.toArray(new Predicate[predicateList.size()]));

		TypedQuery<DepartmentDTO> tq = entityManager.createQuery(cq);

		if (pageSize > 0) {
			tq.setFirstResult(pageNo * pageSize);
			tq.setMaxResults(pageSize);
		}

		List<DepartmentDTO> list = tq.getResultList();

		return list;
	}


}