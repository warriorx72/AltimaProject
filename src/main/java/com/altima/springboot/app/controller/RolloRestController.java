package com.altima.springboot.app.controller;

import java.util.List;

import com.altima.springboot.app.models.entity.AmpRolloTela;
import com.altima.springboot.app.models.service.IAmpRolloTelaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RolloRestController {
    
    @Autowired
    IAmpRolloTelaService rolloService;

    @GetMapping("getRolloByidAlmacenFisico")
    public List<AmpRolloTela> getRolloByidAlmacenFisico(@RequestParam Long idAlmacenFisico) {
        return rolloService.findByIdAlmacenFisico(idAlmacenFisico);
    }
}
