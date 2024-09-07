package uam.mx.demometodologia.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uam.mx.demometodologia.dto.ClaveRequest;
import uam.mx.demometodologia.service.EntrarService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EntrarController {

    private final EntrarService entrarService;

    @GetMapping("/data")
    public String getData() {
        return "Datos del servicio RESTful";
    }

    @PostMapping("/entrar")
    public ResponseEntity<?> entrar(@RequestBody ClaveRequest claveRequest) {
        boolean esClaveValida = entrarService.verificarClave(claveRequest.getClave());
        if (esClaveValida) {
            return ResponseEntity.ok("Clave válida");
        } else {
            return ResponseEntity.badRequest().body("Clave incorrecta");
        }
    }
}