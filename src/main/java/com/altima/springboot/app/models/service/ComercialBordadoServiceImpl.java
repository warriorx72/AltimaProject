package com.altima.springboot.app.models.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altima.springboot.app.models.entity.ComercialBordado;
import com.altima.springboot.app.models.entity.ComercialBordadoParteBordado;
import com.altima.springboot.app.models.entity.ComercialCliente;
import com.altima.springboot.app.models.entity.ComercialLookup;
import com.altima.springboot.app.models.entity.DisenioLookup;
import com.altima.springboot.app.repository.ComercialBordadoParteBordadoRepository;
import com.altima.springboot.app.repository.ComercialBordadoRepository;

@Service
public class ComercialBordadoServiceImpl implements ComercialBordadoService {
	@Autowired
	private EntityManager em;
	
	
	@Autowired
	ComercialBordadoRepository repository;
	
	@Autowired
	ComercialBordadoParteBordadoRepository repositoryParteBordado;
	

	
	
	@Override
	public void save(ComercialBordado Bordado) {
		repository.save(Bordado);
	}
	
	@Override
	@Transactional
	public void delete(Long id) {
		// TODO Auto-generated method stub
		em.remove(findOne(id));
	}
	
	@Override
	public ComercialBordado findOne(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id).orElse(null);
	}
	
	
	
	@Override
	public void saveParte(ComercialBordadoParteBordado ParteBordado) {
		repositoryParteBordado.save(ParteBordado);
	}
	
	@Override
	@Transactional
	public void deleteParteBordado(Long id) {
		// TODO Auto-generated method stub
		em.remove(findOneParteBordado(id));
	}
	
	@Override
	public ComercialBordadoParteBordado findOneParteBordado(Long id) {
		// TODO Auto-generated method stub
		return repositoryParteBordado.findById(id).orElse(null);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<ComercialCliente> findListaClientes(String creado) {
		
		return em.createQuery(
				" FROM ComercialCliente WHERE CcreadoPor= '"+creado+"' and   estatus=1 order by nombre")
				.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<ComercialLookup> findListaLookupComercial() {
		
		return em.createQuery(
				" FROM ComercialLookup WHERE Estatus=1 and tipoLookup='Personalizado' order by nombreLookup")
				.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> findListaBordados() {
		
		return em.createNativeQuery("SELECT\r\n" + 
				"				cli.nombre as nombre,\r\n" + 
				"				bor.descripcion as descrip,\r\n" + 
				"				bor.tamaño as tamaño,\r\n" + 
				"				bor.precio as precio,\r\n" + 
				"				bor.estatus_bordado as estatus, \r\n" + 
				"				bor.id_bordado,\r\n" + 
				"				lk.atributo_1 as preciolk,\r\n" + 
				"				bor.id_cliente \r\n" + 
				"				FROM alt_comercial_bordado bor INNER JOIN alt_comercial_cliente cli\r\n" + 
				"				ON bor.id_cliente=cli.id_cliente	\r\n" + 
				"		    inner JOIN alt_comercial_lookup lk on bor.id_lookup=lk.id_lookup\r\n" + 
				"		     WHERE bor.estatus=\"1\" order by bor.fecha_creacion DESC")
				.getResultList();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> findListaParteBordados(Long id) {
		
		return em.createNativeQuery("SELECT\r\n" + 
				"				pt.partes_bordado as parte,\r\n" + 
				"				pt.numero_hilo as hilo,\r\n" + 
				"				pt.color as color,\r\n" + 
				"				pt.id_bordado_parte_bordado as idbord,\r\n" +
				"				pt.id_bordado \r\n" +	
				"				FROM alt_comercial_bordado_parte_bordado pt  WHERE pt.estatus=1 AND pt.id_bordado="+ id)
				.getResultList();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String findPrecio(String Lookup) {
		
		return (String) em.createNativeQuery(" SELECT\r\n" + 
				"lk.atributo_1\r\n" + 
				"\r\n" + 
				"FROM alt_comercial_lookup lk where lk.nombre_lookup= '"+Lookup +"' ").getSingleResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> findListaBordadoCliente(Long id) {
		
		return em.createNativeQuery("SELECT \r\n" + 
				"bor.descripcion as descri,\r\n" + 
				"lk.nombre_lookup as tamano,\r\n" + 
				"lk.atributo_1 as price,\r\n" + 
				"bor.estatus_bordado,\r\n" + 
				"bor.id_bordado,\r\n" + 
				"bor.id_cliente\r\n" + 
				"\r\n" + 
				"FROM\r\n" + 
				"\r\n" + 
				"alt_comercial_bordado bor  inner join alt_comercial_lookup lk on \r\n" + 
				"bor.id_lookup=lk.id_lookup where bor.estatus=1 and bor.id_cliente="+id)
				.getResultList();
	}
	
	

}
