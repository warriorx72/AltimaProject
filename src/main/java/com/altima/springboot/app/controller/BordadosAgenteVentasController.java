package com.altima.springboot.app.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.altima.springboot.app.models.entity.ComercialBordado;
import com.altima.springboot.app.models.entity.ComercialBordadoParteBordado;
import com.altima.springboot.app.models.entity.ComercialCliente;
import com.altima.springboot.app.models.entity.ComercialLookup;
import com.altima.springboot.app.models.entity.DisenioLookup;
import com.altima.springboot.app.models.entity.DisenioMaterial;
import com.altima.springboot.app.models.service.ComercialBordadoService;
import com.altima.springboot.app.models.service.IDisenioMaterialService;
import com.altima.springboot.app.models.service.IUploadService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Controller
public class BordadosAgenteVentasController {
	
	@Autowired
	private ComercialBordadoService bordadoService;
	
	@Autowired
	private IDisenioMaterialService disenioMaterialService;
	
	@Autowired
	private IUploadService UploadService;
	
	
    @GetMapping("/bordados")
    public String listBordados(Model model) {
    	
    	List<Object[]> listaBordados= bordadoService.findListaBordados();
    	model.addAttribute("listBordado", listaBordados);
    	
    	
    	
    	
    	
        return "bordados";
    }

    @GetMapping("/agregar-bordado")
    public String addBordados(Model model) {
    	
    	ComercialBordado bordado = new ComercialBordado();
    	ComercialBordadoParteBordado objetoBordadoParte= new ComercialBordadoParteBordado();
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	
    	String nombre = auth.getName();
    	
    	List<ComercialCliente> listClientes = bordadoService.findListaClientes(nombre);
    	List<ComercialLookup> listLookup= bordadoService.findListaLookupComercial();
    	model.addAttribute("objetoBordado", bordado);
    	model.addAttribute("listaCli", listClientes);   	
    	model.addAttribute("listLookup", listLookup);
    	model.addAttribute("objetoBordadoParte", objetoBordadoParte);
    	
    	
        return "agregar-bordado";
    }
    
    
    
	@PostMapping("/guardar_bordado")
	public String guardarMaterial(@ModelAttribute ComercialBordado objetoBordado, RedirectAttributes redirectAttrs,
            @RequestParam(value = "inputGroupFile01", required = true) MultipartFile imagenParteBordado,
            @RequestParam(value = "inputFileBordado", required = true) MultipartFile inputFileBordado)
			throws IllegalStateException, IOException {
		
		System.out.println("Entre el metodo de guardar la primera parte de bordado");		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Date date = new Date();
	    DateFormat hourdateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    
		if (!imagenParteBordado.isEmpty()) {// imagen
			if (objetoBordado.getRutaBordado() != null && objetoBordado.getRutaBordado().length() > 0) {
				UploadService.deleteBordadoParte(objetoBordado.getRutaBordado());
				
			}
			String uniqueFilename = null;
			try {
				uniqueFilename = UploadService.copyBordadoParte(imagenParteBordado);
			
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			objetoBordado.setRutaBordado(uniqueFilename);
			
			System.out.println("este es un pinche prueba");

        } // imagen
        
        if (!inputFileBordado.isEmpty()) {// imagen
			if (objetoBordado.getRutaPonchado() != null && objetoBordado.getRutaPonchado().length() > 0) {
				UploadService.deleteBordadoParte(objetoBordado.getRutaPonchado());
				
			}
			String uniqueFilename2 = null;
			try {
				uniqueFilename2 = UploadService.copyBordadoParte(inputFileBordado);
			
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
            objetoBordado.setRutaPonchado(uniqueFilename2);
            objetoBordado.setEstatus_bordado("1");
			
			System.out.println("este es un pinche prueba");

		} // imagen
		
		objetoBordado.setCreadoPor(auth.getName());
		objetoBordado.setActualizadoPor("Actualizador");
		objetoBordado.setEstatus("1");
		objetoBordado.setEstatus_bordado("1");
		objetoBordado.setFechaCreacion(hourdateFormat.format(date));

		
		bordadoService.save(objetoBordado);
		System.out.println("si pelo el save");

		return "redirect:bordados/"+objetoBordado.getIdBordado();

	}
	
	//PREVISUALIZAVION DE LA IMAGEN
	@GetMapping(value = "/uploads/bordadoParte/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;
		try {
			recurso = UploadService.loadBordadoParte(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}
	
	
	  @GetMapping("/bordados/{id}")
	    public String listBordadosNum(Model model,@PathVariable("id") Long id) {
		  
		  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    	System.out.println("aqui esta en controlador que vota el find one");
	    	ComercialBordadoParteBordado objetoBordadoParte= new ComercialBordadoParteBordado();
	    	ComercialBordado objetoBordado = bordadoService.findOne(id);
	    	String nombre = auth.getName();
	    	List<ComercialCliente> listClientes = bordadoService.findListaClientes(nombre);
	    	List<ComercialLookup> listLookup= bordadoService.findListaLookupComercial();
	    	model.addAttribute("listaCli", listClientes);   	
	    	model.addAttribute("listLookup", listLookup);	    		    
	    	model.addAttribute("objetoBordado", objetoBordado);
	    	model.addAttribute("objetoBordadoParte", objetoBordadoParte);
	    	List<DisenioLookup> listLookupsCol = disenioMaterialService.findListaColor();
	    	List<Object[]> listaParteBordadoList=bordadoService.findListaParteBordados(id);
        	model.addAttribute("listLookupsCol", listLookupsCol);
        	model.addAttribute("listaParteBordadoList", listaParteBordadoList);
	        return "agregar-bordado";
	    }
    
	  
	  @PostMapping("/guardar_bordado_parte")
		public String guardarBordardoParteBordado(@ModelAttribute ComercialBordadoParteBordado objetoBordadoParte, RedirectAttributes redirectAttrs,
				 MultipartFile imagenMaterial)
				throws IllegalStateException, IOException {
			
			System.out.println("Entre el metodo de guardar lo de uno a muchos");		
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Date date = new Date();
		    DateFormat hourdateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		    Long id =objetoBordadoParte.getIdBordado();
		    objetoBordadoParte.setCreadoPor(auth.getName());
		    objetoBordadoParte.setActualizadoPor("Actualizador");
		    objetoBordadoParte.setFechaCreacion(hourdateFormat.format(date));
		    objetoBordadoParte.setEstatus("1");
		    bordadoService.saveParte(objetoBordadoParte);
			System.out.println("Si pelo el saveParte de uno a muchos");
			
			return "redirect:bordados/"+id;

		}
    
    
}