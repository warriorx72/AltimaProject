package com.altima.springboot.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarioAuxiliarController {
    @GetMapping("/calendario-auxiliar")
    public String CalendarioGeneral()
    {
        return "calendario-auxiliar";
    }
}