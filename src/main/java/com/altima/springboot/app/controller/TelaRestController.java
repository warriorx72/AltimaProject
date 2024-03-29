package com.altima.springboot.app.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.altima.springboot.app.models.entity.DisenioLookup;
import com.altima.springboot.app.models.entity.DisenioMaterial;
import com.altima.springboot.app.models.entity.DisenioTela;
import com.altima.springboot.app.models.entity.ProduccionTelaCalidad;
import com.altima.springboot.app.models.entity.ProduccionTelaCalidadImagen;
import com.altima.springboot.app.models.service.IDisenioMaterialService;
import com.altima.springboot.app.models.service.IDisenioPruebaEncogimientoLavadoService;
import com.altima.springboot.app.models.service.IDisenioTelaService;
import com.altima.springboot.app.models.service.IProduccionTelaCalidadImagenService;
import com.altima.springboot.app.models.service.IProduccionTelaCalidadService;
import com.altima.springboot.app.models.service.UploadServiceImpl;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TelaRestController {
	@Autowired
	private IDisenioTelaService disenioTelaService;

	@Autowired
	private IProduccionTelaCalidadService telaCalidadService;

	@Autowired
	private IDisenioPruebaEncogimientoLavadoService pruebaEncogimientoLavadoService;

	@Autowired
	UploadServiceImpl uService;

	@Autowired
	private IProduccionTelaCalidadImagenService telaCalidadImagenService;

	@Autowired
	private IDisenioMaterialService materialService;

	@GetMapping("/getMaterial")
	public List<DisenioMaterial> listarMaterial(@RequestParam(name = "tipo") String tipo) {
		System.out.println("el tepo" + tipo);
		return disenioTelaService.findAllMaterial(tipo);
	}

	@GetMapping("/getTipo")
	public List<DisenioLookup> listarTipo() {
		return disenioTelaService.findAllTipo();
	}

	@RequestMapping(value = "/buscar-tela-nombre", method = RequestMethod.POST)
	@ResponseBody
	public String buscarNombreTela(String nombre) {

		System.out.println("------->" + disenioTelaService.buscar_tela(nombre));

		return disenioTelaService.buscar_tela(nombre);
	}

	// guardar porcentaje de encogimiento/estiramiento de la tela en pantalla de
	// producción
	@RequestMapping(value = "/agregarPorcentajeEncogimiento", method = RequestMethod.POST)
	@ResponseBody
	public void guardarPorcentajeEncogimiento(@RequestParam(name = "encogiTela") int encogiTela,
			@RequestParam(name = "idTela") Long id) throws Exception {
		DisenioTela disenioTela = disenioTelaService.findOne(id);
		System.out.println(encogiTela);
		disenioTela.setPruebaEncogimientoLargo(encogiTela);

		disenioTelaService.save(disenioTela);

	}

	@PostMapping("/postTelaCalidad")
	@ResponseStatus(HttpStatus.CREATED)
	@Transactional
	public ResponseEntity<?> postTelaCalidad(@RequestBody ProduccionTelaCalidad telaCalidad,
			@RequestParam float promedioAncho, @RequestParam float promedioLargo) throws Exception {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> response = new HashMap<>();
		try {
			DisenioTela tela = disenioTelaService.findOne(telaCalidad.getIdTela());
			telaCalidad.setCreadoPor(auth.getName());
			telaCalidad.setActualizadoPor(auth.getName());
			telaCalidadService.save(telaCalidad);
			tela.setPruebaEncogimientoAncho(promedioAncho);
			tela.setPruebaEncogimientoLargo(promedioLargo);
			disenioTelaService.save(tela);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar en la BD");
			response.put("error", e.getMessage() + ": " + e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProduccionTelaCalidad>(telaCalidad, HttpStatus.CREATED);
	}

	@GetMapping("getIdEntretelaByIdTela/{id}")
	public ResponseEntity<?> getIdEntretelaByIdTela(@PathVariable(name = "id") Long id) {
		Map<String, Object> response = new HashMap<>();
		Long idEntretela = null;
		DisenioMaterial entretela=null;
		DisenioMaterial entretela2=null;
		ProduccionTelaCalidad telaCalidad=null;
		telaCalidad=telaCalidadService.findOneByIdTela(id);
		JSONObject entretelas=new JSONObject();
		try {
			if(telaCalidad!=null){
				entretela2=materialService.findOne(telaCalidad.getIdEntretela());
			}
			idEntretela=pruebaEncogimientoLavadoService.findEntretelaByIdTela(id);
			entretela= materialService.findOne(idEntretela);
			entretelas.put("diseno", entretela).put("produccion", entretela2);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la BD");
			response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (entretela == null) {
			response.put("mensaje", "La entretela no existe");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Map<String,Object>>(entretelas.toMap(), HttpStatus.OK);
	}
	

	@PostMapping("/guardarDocumentoCalidad")
	@ResponseStatus(HttpStatus.CREATED)
	@Transactional
	public ResponseEntity<?> guardarDocumentoCalidad(@RequestParam Long idTela, @RequestParam String descripcion,
			@RequestParam MultipartFile imagenDocumento) {

		ProduccionTelaCalidadImagen documento = new ProduccionTelaCalidadImagen();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Cloudinary cloudinary = uService.CloudinaryApi();
		String uniqueFilename;
		Map<String, Object> response = new HashMap<>();
		try {
			uniqueFilename = uService.copy2(imagenDocumento);
			documento.setIdTela(idTela);
			documento.setRuta(uniqueFilename);
			documento.setDescripcion(descripcion);
			documento.setCreadoPor(auth.getName());
			documento.setActualizadoPor(auth.getName());
			cloudinary.uploader().upload(uService.filePrenda(uniqueFilename), ObjectUtils.asMap("public_id",
					"documentosCalidad/" + uniqueFilename.substring(0, uniqueFilename.length() - 4)));
			telaCalidadImagenService.save(documento);
		}

		catch (IOException e) {
			response.put("mensaje", "Error al insertar en la BD");
			response.put("error", e.getMessage() + ": " + e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProduccionTelaCalidadImagen>(documento, HttpStatus.CREATED);
	}

	@GetMapping("getTelaCalidad/{id}")
	public ResponseEntity<?> getCliente(@PathVariable(name = "id") Long id) {

		Map<String, Object> response = new HashMap<>();
		ProduccionTelaCalidad telaCalidad = null;
		try {
			telaCalidad = telaCalidadService.findOneByIdTela(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la BD");
			response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (telaCalidad == null) {
			response.put("mensaje", "La tela no existe");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ProduccionTelaCalidad>(telaCalidad, HttpStatus.OK);
	}

	@GetMapping("getDocumentoCalidadByIdTela/{id}")
	public ResponseEntity<?> getDocumentoByIdTela(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		List<ProduccionTelaCalidadImagen> documentos = null;
		try {
			documentos = telaCalidadImagenService.findByIdTela(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la BD");
			response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (documentos.size() == 0) {
			response.put("mensaje", "La tela con el id " + id + " no tiene documentos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<ProduccionTelaCalidadImagen>>(documentos, HttpStatus.OK);
	}

	@DeleteMapping("deleteDocumentoCalidad/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteEmpresa(@PathVariable(name = "id") Long id) {
        Map<String, Object> response = new HashMap<>();
        Cloudinary cloudinary = uService.CloudinaryApi();
        try {
            ProduccionTelaCalidadImagen documento=telaCalidadImagenService.findOne(id);
            cloudinary.uploader().destroy("documentosCalidad/" + documento.getRuta().substring(0, documento.getRuta().length() - 4)
            , ObjectUtils.asMap("resourceType", "image"));
            telaCalidadImagenService.delete(id);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar Documento en la BD");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            response.put("mensaje", "Error al subir la imagen");
            response.put("error", e.getMessage() + ": " + e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El documento fue eliminado con exito");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
