package be.ucll.se.groep02backend.bill.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.groep02backend.bill.model.Bill;
import be.ucll.se.groep02backend.bill.service.BillService;
import be.ucll.se.groep02backend.bill.service.BillServiceException;
import be.ucll.se.groep02backend.config.ApplicationConfig;
import be.ucll.se.groep02backend.user.service.UserServiceException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;



@Tag(name = "Bill")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/bill")
public class BillRestController {

    @Autowired
    private BillService billService;

    @GetMapping
    public List<Bill> getBills() throws UserServiceException, BillServiceException {
        return billService.getAllBills(ApplicationConfig.getAuthenticatedUser());
    }

    @PutMapping("/paid/")
    public Bill setBillPaid(@RequestParam("id") Long id) throws UserServiceException, BillServiceException {
        return billService.setBillPaid(id, ApplicationConfig.getAuthenticatedUser());
    }
    
}
