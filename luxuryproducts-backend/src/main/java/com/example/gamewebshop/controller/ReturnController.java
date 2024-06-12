package com.example.gamewebshop.controller;


import com.example.gamewebshop.dao.ReturnDAO;
import com.example.gamewebshop.Repositorys.UserRepository;
import com.example.gamewebshop.dto.ReturnDTO;
import com.example.gamewebshop.models.ReturnRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://s1148232.student.inf-hsleiden.nl:18232"})
@RequestMapping("/returns")
public class ReturnController {

    private final ReturnDAO returnDAO;

    @GetMapping("/myReturns")
    public ResponseEntity<List<ReturnRequest>> getReturnsByUserPrincipal() {

        List<ReturnRequest> returns = this.returnDAO.getReturnsByUserId();
        return ResponseEntity.ok(returns);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateReturn(@RequestBody String returnStatus, @PathVariable Long id ){
        returnDAO.updateReturn(returnStatus, id);
        return ResponseEntity.ok("status updated to "+ returnStatus);

    }

    @PostMapping
    public ResponseEntity<String> createReturn(@RequestBody ReturnDTO returnRequest, Principal principal) {
        String userEmail = principal.getName();
        this.returnDAO.saveReturnWithProducts(returnRequest, userEmail);
        return ResponseEntity.ok().body("{\"message\": \"Return request created successfully\"}");
    }

}