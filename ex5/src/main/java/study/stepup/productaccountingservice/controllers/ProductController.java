package study.stepup.productaccountingservice.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import study.stepup.productaccountingservice.models.ProductRequest;
import study.stepup.productaccountingservice.models.ProductResponse;
import study.stepup.productaccountingservice.services.ProductService;

import java.time.LocalDateTime;

@Slf4j
@RestController()
@AllArgsConstructor
@RequestMapping("/corporate-settlement-instance")
public class ProductController {

    private final ProductService productService;

    @PostMapping(value = "/create" )
    public ResponseEntity<ProductResponse> create(@Validated @RequestBody ProductRequest productRequest)
    {
        ProductResponse productResponse = productService.create(productRequest);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productResponse);
    }

    @GetMapping("hello2")
    public ResponseEntity<String> get2() throws Exception {
        throw new IllegalArgumentException ("test ex " + LocalDateTime.now() );
//        return ResponseEntity.ok("sdsd");
    }

    @GetMapping("hello")
    public ResponseEntity<String> get(){
        try {
            log.info("hello.ok " + LocalDateTime.now());
            productService.test();
            return ResponseEntity.ok("hello.ok " + LocalDateTime.now());
        }
        catch (Exception ex)
        {
         return ResponseEntity.status(500).body(ex.getMessage());
        }
    }





}
