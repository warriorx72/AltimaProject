package com.altima.springboot.app.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.altima.springboot.app.models.entity.HrCalendario;
import com.altima.springboot.app.models.entity.HrDepartamento;
import com.altima.springboot.app.models.entity.HrEmpleado;
import com.altima.springboot.app.models.entity.HrHorario;
import com.altima.springboot.app.models.entity.HrLookup;
import com.altima.springboot.app.models.entity.HrPuesto;
import com.altima.springboot.app.models.service.IHrCalendariosService;
import com.altima.springboot.app.models.service.IHrDepartamentoService;
import com.altima.springboot.app.models.service.IHrEmpleadoService;
import com.altima.springboot.app.models.service.IHrHorarioService;
import com.altima.springboot.app.models.service.IHrLookupService;
import com.altima.springboot.app.models.service.IHrPuestoService;
import com.altima.springboot.app.models.service.UploadServiceImpl;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class HrCatalogosRestController {

    @Autowired
    IHrLookupService hrLookupService;

    @Autowired
    IHrDepartamentoService hrDepartamentoService;

    @Autowired
    IHrPuestoService hrPuestoService;

    @Autowired
    IHrHorarioService hrHorarioService;

    @Autowired
    IHrCalendariosService hrCalendarioService;

    @Autowired
    IHrEmpleadoService empleadoService;

    @Autowired
    UploadServiceImpl uService;

    // Método para guardar Empresas
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_AGREGAR" })
    @PostMapping("/postEmpresa")
    public int postEmpresa(@RequestParam(name = "nombreEmpresa") String nombreEmpresa,
            @RequestParam(name = "idLookup") Long idLookup) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            HrLookup hr = hrLookupService.findOne(idLookup);
            hr.setNombreLookup(nombreEmpresa);
            hrLookupService.save(hr);
            return 1;
        } catch (Exception e) {
            try {
                HrLookup empresa = new HrLookup();
                empresa.setNombreLookup(nombreEmpresa);
                empresa.setTipoLookup("Empresa");
                empresa.setCreadoPor(auth.getName());
                empresa.setFechaCreacion(dtf.format(now));
                empresa.setEstatus("1");
                empresa.setIdText("EMP");
                hrLookupService.save(empresa);
                empresa.setIdText("EMP" + (10000 + empresa.getIdLookup()));
                hrLookupService.save(empresa);
                return 2;
            } catch (Exception p) {
                p.printStackTrace();
                return 3;
            }
        } finally {
        }
    }

    @GetMapping("/duplicadoEmpresa")
    public boolean duplicadoEmpresa(String nombreEmpresa) {
        boolean duplicado;
        duplicado = hrLookupService.findDuplicate(nombreEmpresa);
        return duplicado;
    }

    // Método para listar Empresas
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_LISTAR" })
    @GetMapping("/getLookupHR")
    public List<HrLookup> getEmpresa(@RequestParam(name = "tipo") String tipo) {
        return hrLookupService.findAllByTipoLookup(tipo);
    }

    // Método para dar de baja empresa
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_ELIMINAR" })
    @GetMapping("/darBajaEmpresa")
    public Object darBajaEmpresa(@RequestParam(name = "idLookup") Long idLookup) throws Exception {
        HrLookup EhrE = hrLookupService.findOne(idLookup);
        EhrE.setEstatus("0");
        hrLookupService.save(EhrE);
        return hrLookupService.findOne(idLookup);
    }

    // Método para dar de alta empresa
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_ELIMINAR" })
    @GetMapping("/darAltaEmpresa")
    public Object darAltaEmpresa(@RequestParam(name = "idLookup") Long idLookup) throws Exception {
        HrLookup EhrE = hrLookupService.findOne(idLookup);
        EhrE.setEstatus("1");
        hrLookupService.save(EhrE);
        return hrLookupService.findOne(idLookup);
    }

    // Método para editar Empresas
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_EDITAR" })
    @GetMapping("/editarEmpresa")
    public HrLookup editarEmpresa(@RequestParam(name = "idLookup") Long idLookup) {
        return hrLookupService.findOne(idLookup);
    }

    // Método para guardar Áreas
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_AGREGAR" })
    @PostMapping("/postArea")
    public int postArea(@RequestParam(name = "nombreArea") String nombreArea,
            @RequestParam(name = "idLookup") Long idLookup) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            HrLookup hr = hrLookupService.findOne(idLookup);
            hr.setNombreLookup(nombreArea);
            hrLookupService.save(hr);
            return 1;
        } catch (Exception e) {
            try {
                HrLookup area = new HrLookup();
                area.setNombreLookup(nombreArea);
                area.setTipoLookup("Area");
                area.setCreadoPor(auth.getName());
                area.setEstatus("1");
                area.setFechaCreacion(dtf.format(now));
                area.setIdText("AREA");
                hrLookupService.save(area);
                area.setIdText("AREA" + (10000 + area.getIdLookup()));
                hrLookupService.save(area);
                return 2;
            } catch (Exception p) {
                p.printStackTrace();
                return 3;
            }
        }
    }

    @GetMapping("/duplicadoArea")
    public boolean duplicadoArea(String nombreArea) {
        boolean d;
        try {
            d = hrLookupService.findDuplicateArea(nombreArea);
        } catch (Exception e) {
            d = hrLookupService.findDuplicateArea(nombreArea);
        }
        return d;
    }

    // Método para listar Áreas
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_LISTAR" })
    @GetMapping("/rh-listarAreas")
    public List<HrLookup> listarAreas(@RequestParam(name = "tipo") String tipo) {
        return hrDepartamentoService.findAllEmpresas(tipo);
    }

    // Método para dar de baja area
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_ELIMINAR" })
    @GetMapping("/darBajaArea")
    public Object darBajaArea(@RequestParam(name = "idLookup") Long idLookup) throws Exception {
        HrLookup EhrA = hrLookupService.findOne(idLookup);
        EhrA.setEstatus("0");
        hrLookupService.save(EhrA);
        return hrLookupService.findOne(idLookup);
    }

    // Método para dar de alta area
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_ELIMINAR" })
    @GetMapping("/darAltaArea")
    public Object darAltaArea(@RequestParam(name = "idLookup") Long idLookup) throws Exception {
        HrLookup EhrA = hrLookupService.findOne(idLookup);
        EhrA.setEstatus("1");
        hrLookupService.save(EhrA);
        return hrLookupService.findOne(idLookup);
    }

    // Método para editar Áreas
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_EDITAR" })
    @GetMapping("/editarArea")
    public HrLookup editarArea(@RequestParam(name = "idLookup") Long idLookup) {
        return hrLookupService.findOne(idLookup);
    }

    // Método para guardar departamentos
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_AGREGAR" })
    @PostMapping("/postDepartamento")
    public int postDepartamento(@RequestParam(name = "idDepartamento") Long idDepartamento,
            @RequestParam(name = "nombreDepartamento") String nombreDepartamento,
            @RequestParam(name = "nomArea") Long nomArea) {
        Authentication authDepa = SecurityContextHolder.getContext().getAuthentication();
        Date date1 = new Date();
        DateFormat fechaCreacion = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            HrDepartamento hrD = hrDepartamentoService.findOne(idDepartamento);
            hrD.setNombreDepartamento(nombreDepartamento);
            hrD.setIdArea(nomArea);
            hrDepartamentoService.save(hrD);
            return 1;
        } catch (Exception e) {
            try {
                HrDepartamento departamento = new HrDepartamento();
                departamento.setEstatus("1");
                departamento.setCreado_por(authDepa.getName());
                departamento.setFechaCreacion(fechaCreacion.format(date1));
                departamento.setActualizadoPor(authDepa.getName());
                departamento.setIdText("DEP");
                departamento.setNombreDepartamento(nombreDepartamento);
                departamento.setIdArea(nomArea);
                hrDepartamentoService.save(departamento);
                departamento.setIdText("DEP" + (10000 + departamento.getIdDepartamento()));
                hrDepartamentoService.save(departamento);
                return 2;
            } catch (Exception p) {
                p.printStackTrace();
                return 3;
            }
        } finally {
        }
    }

    @GetMapping("/duplicadoDepartamento")
    public boolean duplicadoDepartamento(String nombreDepartamento, String nomArea) {
        boolean d;
        try {
            d = hrDepartamentoService.duplicateDepartamento(nombreDepartamento, nomArea);
        } catch (Exception e) {
            d = hrDepartamentoService.duplicateDepartamento(nombreDepartamento, nomArea);
        }
        return d;
    }

    // Método para listar Departamentos
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_LISTAR" })
    @GetMapping("/getListarDepartamentos")
    public List<Object[]> getListarDepartamentos() {
        return hrDepartamentoService.listarDepartamentos();
    }

    // Método para dar de baja departamento
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_ELIMINAR" })
    @GetMapping("/darBajaDepartamento")
    public Object darBajaDepartamento(@RequestParam(name = "idLookup") Long idLookup) throws Exception {
        HrDepartamento EhrD = hrDepartamentoService.findOne(idLookup);
        EhrD.setEstatus("0");
        hrDepartamentoService.save(EhrD);
        return hrDepartamentoService.findOne(idLookup);
    }

    // Método para dar de alta departamento
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_ELIMINAR" })
    @GetMapping("/darAltaDepartamento")
    public Object darAltaDepartamento(@RequestParam(name = "idLookup") Long idLookup) throws Exception {
        HrDepartamento EhrD = hrDepartamentoService.findOne(idLookup);
        EhrD.setEstatus("1");
        hrDepartamentoService.save(EhrD);
        return hrDepartamentoService.findOne(idLookup);
    }

    // Método para editar Departamento
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_EDITAR" })
    @GetMapping("/editarDepartamento")
    public Object editarDepartamento(@RequestParam(name = "idLookup") Long idLookup) {
        return hrDepartamentoService.obtenerDepartamento(idLookup);
    }

    // Método para guardar Puestos
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_AGREGAR" })
    @PostMapping("/postPuesto")
    public int postPuesto(@RequestParam(name = "nombrePuesto") String nombrePuesto,
            @RequestParam(name = "nomPlazas") Integer nomPlazas, @RequestParam(name = "sueldos") String sueldos,
            @RequestParam(name = "perfiles") MultipartFile perfiles,
            @RequestParam(name = "departamento") Long departamento,
            @RequestParam(name = "checkbox", defaultValue = "false") Boolean checkbox,
            @RequestParam(name = "idPuesto") Long idPuesto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String uniqueFilename = null;
        try {
            Cloudinary cloudinary = uService.CloudinaryApi();
            uniqueFilename = uService.copy2(perfiles);
            if (idPuesto != null) {
                System.out.println(uniqueFilename);
                HrPuesto hrP = hrPuestoService.findOne(idPuesto);
                hrP.setNombrePuesto(nombrePuesto);
                hrP.setIdDepartamento(departamento);
                hrP.setTiempoExtra(checkbox);
                hrP.setNombrePlaza(nomPlazas);
                hrP.setSueldo(sueldos);
                hrP.setPerfil(uniqueFilename);
                System.out.println(uniqueFilename);
                if (!uniqueFilename.equals(null)) {
                    cloudinary.uploader().upload(uService.filePrenda(uniqueFilename), ObjectUtils.asMap("public_id",
                            "rh/" + uniqueFilename.substring(0, uniqueFilename.length() - 4)));
                    hrPuestoService.save(hrP);
                }
                return 1;
            } else {
                HrPuesto nuevoPuesto = new HrPuesto();
                nuevoPuesto.setCreadoPor(auth.getName());
                nuevoPuesto.setFechaCreacion(dtf.format(now));
                nuevoPuesto.setEstatus("1");
                nuevoPuesto.setCreadoPor(auth.getName());
                nuevoPuesto.setIdText("PUE");
                nuevoPuesto.setNombrePuesto(nombrePuesto);
                nuevoPuesto.setNombrePlaza(nomPlazas);
                nuevoPuesto.setSueldo(sueldos);
                nuevoPuesto.setPerfil(uniqueFilename);
                nuevoPuesto.setIdDepartamento(departamento);
                nuevoPuesto.setTiempoExtra(checkbox);
                cloudinary.uploader().upload(uService.filePrenda(uniqueFilename), ObjectUtils.asMap("public_id",
                        "rh/" + uniqueFilename.substring(0, uniqueFilename.length() - 4)));
                hrPuestoService.save(nuevoPuesto);
                nuevoPuesto.setIdText("PUE" + (10000 + nuevoPuesto.getIdPuesto()));
                hrPuestoService.save(nuevoPuesto);
                return 2;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 3;
        }
    }

    @GetMapping("/duplicadoPuesto")
    public boolean duplicadoPuesto(String nombrePuesto, String nomPlazas, String sueldos, String perfiles,
            String departamento, Boolean checkbox, String idPuesto) {
        boolean d;
        try {
            d = hrPuestoService.duplicatePuesto(nombrePuesto, nomPlazas, sueldos, perfiles, departamento, checkbox,
                    idPuesto);
        } catch (Exception e) {
            d = hrPuestoService.duplicatePuesto(nombrePuesto, nomPlazas, sueldos, perfiles, departamento, checkbox,
                    idPuesto);
        }
        return d;
    }

    // Método para mostrar las opciones de departamentos en puestos
    @GetMapping("/getMostrarDepartamentos")
    public List<HrDepartamento> listarDepartamentos() {
        return hrPuestoService.findAllDepartamentos();
    }

    // Método para listar los puestos insertados
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_LISTAR" })
    @GetMapping("/getListarPuestos")
    public List<Object[]> listarPuestos(Model model) {
        List<Object[]> listarPuestos = hrDepartamentoService.listarDepartamentos();
        model.addAttribute("listarPuestos", listarPuestos);
        return hrPuestoService.listarPuestos();
    }

    // Método para dar de baja puesto
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_ELIMINAR" })
    @GetMapping("/darBajaPuesto")
    public Object darBajaPuesto(@RequestParam(name = "idLookup") Long idLookup) throws Exception {
        HrPuesto EhrP = hrPuestoService.findOne(idLookup);
        EhrP.setEstatus("0");
        hrPuestoService.save(EhrP);
        return hrPuestoService.findOne(idLookup);
    }

    // Método para dar de alta puesto
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_ELIMINAR" })
    @GetMapping("/darAltaPuesto")
    public Object darAltaPuesto(@RequestParam(name = "idLookup") Long idLookup) throws Exception {
        HrPuesto EhrP = hrPuestoService.findOne(idLookup);
        EhrP.setEstatus("1");
        hrPuestoService.save(EhrP);
        return hrPuestoService.findOne(idLookup);
    }

    // Método para editar puestos
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_EDITAR" })
    @GetMapping("/editarPuesto")
    public Object editarPuesto(@RequestParam(name = "idLookup") Long idLookup) {
        return hrPuestoService.obtenerPuesto(idLookup);
    }

    // Método para guardar Horarios
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_AGREGAR" })
    @PostMapping("/postHorarioLaboral")
    public int postPuesto(@RequestParam(name = "idHorario") Long idHorario,
            @RequestParam(name = "horaInicio") String horaInicio, @RequestParam(name = "horaSalida") String horaSalida,
            @RequestParam(name = "horaComida") String horaComida,
            @RequestParam(name = "horaRegresoComida") String horaRegresoComida) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            HrHorario hrH = hrHorarioService.findOne(idHorario);
            hrH.setHoraInicial(horaInicio);
            hrH.setHoraFinal(horaSalida);
            hrH.setInicioComida(horaComida);
            hrH.setFinalComida(horaRegresoComida);
            hrHorarioService.save(hrH);
            return 1;
        } catch (Exception e) {
            try {
                HrHorario horarioLaboral = new HrHorario();
                horarioLaboral.setHoraInicial(horaInicio);
                horarioLaboral.setHoraFinal(horaSalida);
                horarioLaboral.setInicioComida(horaComida);
                horarioLaboral.setFinalComida(horaRegresoComida);
                horarioLaboral.setCreadoPor(auth.getName());
                horarioLaboral.setFechaCreacion(dtf.format(now));
                horarioLaboral.setActualizadoPor(auth.getName());
                horarioLaboral.setEstatus("1");
                horarioLaboral.setIdText("HRO");
                hrHorarioService.save(horarioLaboral);
                horarioLaboral.setIdText("HRO" + (10000 + horarioLaboral.getIdHorario()));
                hrHorarioService.save(horarioLaboral);
                return 2;
            } catch (Exception p) {
                e.printStackTrace();
                return 3;
            }
        }
    }

    @GetMapping("/duplicadoHorario")
    public boolean duplicadoHorario(String horaInicio, String horaSalida, String horaComida, String horaRegresoComida) {
        boolean d;
        try {
            d = hrHorarioService.duplicateHorario(horaInicio, horaSalida, horaComida, horaRegresoComida);
        } catch (Exception e) {
            d = hrHorarioService.duplicateHorario(horaInicio, horaSalida, horaComida, horaRegresoComida);
        }
        return d;
    }

    // Método para listar horarios insertados
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_LISTAR" })
    @GetMapping("/getListarHorarios")
    public List<HrHorario> listarHorariosInsertados() {
        return hrHorarioService.findAllHorarios();
    }

    // Método para dar de baja horario
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_ELIMINAR" })
    @GetMapping("/darBajaHorario")
    public Object darBajaHorario(@RequestParam(name = "idLookup") Long idLookup) throws Exception {
        HrHorario EhrH = hrHorarioService.findOne(idLookup);
        EhrH.setEstatus("0");
        hrHorarioService.save(EhrH);
        return hrHorarioService.findOne(idLookup);
    }

    // Método para dar de alta horario
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_ELIMINAR" })
    @GetMapping("/darAltaHorario")
    public Object darAltaHorario(@RequestParam(name = "idLookup") Long idLookup) throws Exception {
        HrHorario EhrH = hrHorarioService.findOne(idLookup);
        EhrH.setEstatus("1");
        hrHorarioService.save(EhrH);
        return hrHorarioService.findOne(idLookup);
    }

    // Método para editar horarios
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_EDITAR" })
    @GetMapping("/editarHorario")
    public List<HrHorario> editarHorario(@RequestParam(name = "idHorario") Long idHorario) {
        return hrHorarioService.obtenerHorario(idHorario);
    }

    // Método para guardar Calendarios
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_AGREGAR" })
    @PostMapping("/postCalendarios")
    public int postPuesto(@RequestParam(name = "idCalendario") Long idCalendario,
            @RequestParam(name = "fechaFestivo") String fechaFestivo,
            @RequestParam(name = "festividad") String festividad,
            @RequestParam(name = "estatusFestivo") String estatusFestivo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            HrCalendario hrC = hrCalendarioService.findOne(idCalendario);
            hrC.setFecha(fechaFestivo);
            hrC.setNombreCalendario(festividad);
            hrC.setEstatus(estatusFestivo);
            hrCalendarioService.save(hrC);
            return 1;
        } catch (Exception e) {
            try {
                HrCalendario nuevosFestivos = new HrCalendario();
                nuevosFestivos.setFecha(fechaFestivo);
                nuevosFestivos.setNombreCalendario(festividad);
                nuevosFestivos.setCreadoPor(auth.getName());
                nuevosFestivos.setFechaCreacion(dtf.format(now));
                nuevosFestivos.setEstatus(estatusFestivo);
                nuevosFestivos.setActualizadoPor(auth.getName());
                nuevosFestivos.setIdText("CAL");
                hrCalendarioService.save(nuevosFestivos);
                nuevosFestivos.setIdText("CAL" + (10000 + nuevosFestivos.getIdCalendario()));
                hrCalendarioService.save(nuevosFestivos);
                return 2;
            } catch (Exception p) {
                e.printStackTrace();
                return 3;
            }
        }
    }

    @GetMapping("/duplicadoCalendario")
    public boolean duplicadoCalendario(String fechaFestivo, String festividad, String estatusFestivo) {
        boolean d;
        try {
            d = hrCalendarioService.duplicateCalendario(fechaFestivo, festividad, estatusFestivo);
        } catch (Exception e) {
            d = hrCalendarioService.duplicateCalendario(fechaFestivo, festividad, estatusFestivo);
        }
        return d;
    }

    // Método para listar calendarios insertados
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_LISTAR" })
    @GetMapping("/getListarCalendarios")
    public List<HrCalendario> listarCalendariosInsertados() {
        return hrCalendarioService.findAllCalendarios();
    }

    // Método para dar de baja Calendario
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_ELIMINAR" })
    @GetMapping("/darBajaCalendario")
    public Object darBajaCalendario(@RequestParam(name = "idCalendario") Long idCalendario) throws Exception {
        HrCalendario EhrC = hrCalendarioService.findOne(idCalendario);
        EhrC.setEstatus("0");
        hrCalendarioService.save(EhrC);
        return hrCalendarioService.findOne(idCalendario);
    }

    // Método para dar de alta Calendario
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_ELIMINAR" })
    @GetMapping("/darAltaCalendario")
    public Object darAltaCalendario(@RequestParam(name = "idCalendario") Long idCalendario) throws Exception {
        HrCalendario EhrC = hrCalendarioService.findOne(idCalendario);
        EhrC.setEstatus("1");
        hrCalendarioService.save(EhrC);
        return hrCalendarioService.findOne(idCalendario);
    }

    // Método para editar calendarios
    @Secured({ "ROLE_ADMINISTRADOR", "ROLE_RECURSOSHUMANOS_CATALOGOS_EDITAR" })
    @GetMapping("editarCalendario")
    public Object editarCalendario(@RequestParam(name = "idCalendario") Long idCalendario) {
        return hrCalendarioService.findOne(idCalendario);
    }

    @GetMapping("/getEmpleadosSelect")
    public List<HrEmpleado> getEmpleadosSelect() {
        return empleadoService.findEmpleadosSelect();
    }

    @PutMapping("/putEmpleadosSelect/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> putEmpresa(@RequestParam Long coordinador, @PathVariable(name = "id") Long id) {

        Map<String, Object> response = new HashMap<>();
        HrDepartamento departamento = hrDepartamentoService.findOne(id);
        if (departamento == null) {
            response.put("mensaje", "La empresa con el id " + id + " no existe");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        try {
            departamento.setIdEmpleado(coordinador);
            hrDepartamentoService.save(departamento);
            response.put("mensaje", "Se inserto correctamente el Coordinador");
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al insertar en la BD");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }
}