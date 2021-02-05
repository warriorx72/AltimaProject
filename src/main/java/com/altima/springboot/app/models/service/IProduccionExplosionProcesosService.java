package com.altima.springboot.app.models.service;

import java.util.List;

import com.altima.springboot.app.models.entity.ProduccionExplosionPrendas;
import com.altima.springboot.app.models.entity.ProduccionExplosionProcesos;

public interface IProduccionExplosionProcesosService {

	void save (ProduccionExplosionProcesos produccionExplosionProcesos);
	
	ProduccionExplosionProcesos findOne (Long id);

	List<Object[]> findAllByPrograma(String programa);

	List<ProduccionExplosionProcesos> findProgramas();

	List<ProduccionExplosionProcesos> listExplosionByProceso(Long id);

	
	//Métodos para el modal de explosion de prendas en la pantalla de control de avances
	
	List<Object[]> listarPrendasByExplosionProceso(Long idExplosionProceso, String tipo);

	void saveExplosionPrendas(ProduccionExplosionPrendas explosionPrendas);

	ProduccionExplosionPrendas findOnePrendas (Long id);

	List<Object[]> prendasExplosionarByProceso(Long idExplosionProceso);

	List<Object []> listarEmpleadosbyProduccion();

	List<Object []> listarMaquilerosbyProceso(Long idProceso);
	
}
