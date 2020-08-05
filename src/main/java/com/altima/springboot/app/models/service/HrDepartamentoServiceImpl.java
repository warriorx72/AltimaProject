package com.altima.springboot.app.models.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altima.springboot.app.models.entity.HrDepartamento;
import com.altima.springboot.app.models.entity.HrLookup;
import com.altima.springboot.app.repository.HrDepartamentoRepository;

@Service
public class HrDepartamentoServiceImpl implements IHrDepartamentoService {
	@Autowired
	EntityManager em;
	@Autowired
	HrDepartamentoRepository repository;

	@Override
	@Transactional
	public void save(HrDepartamento hrdepartamento) {
		repository.save(hrdepartamento);
	}

	@Override
	@Transactional
	public HrDepartamento findOne(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public List<HrDepartamento> findAll() {
		return (List<HrDepartamento>) repository.findAll();
	}

	@Override
	@Transactional
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<HrLookup> findAllAreas() {
		return em.createQuery("from HrLookup").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<HrLookup> findAllEmpresas(String tipo) {
		return em.createQuery("from HrLookup where tipoLookup='" + tipo + "'").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> listarDepartamentos() {
		List<Object[]> mv;
		mv = em.createNativeQuery("{call alt_pr_listarDepartamentos}").getResultList();
		return mv;
	}

	@Override
	public Object obtenerDepartamento(Long id) {
		return em.createNativeQuery(
				"SELECT	dp.id_departamento AS idDepartamento, dp.nombre_departamento AS nombreDepa, ar.id_lookup AS idArea, ar.nombre_lookup AS nombreArea, ar.estatus AS estatus"
						+ " FROM alt_hr_departamento dp INNER JOIN alt_hr_lookup ar ON dp.id_area = ar.id_lookup"
						+ " WHERE ar.id_lookup = dp.id_area AND dp.id_departamento = " + id)
				.getSingleResult();
	}
}