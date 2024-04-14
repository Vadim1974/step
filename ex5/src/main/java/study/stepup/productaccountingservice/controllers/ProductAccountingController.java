package study.stepup.productaccountingservice.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import study.stepup.productaccountingservice.models.ProductAccountingRequest;
import study.stepup.productaccountingservice.models.ProductAccountingResponse;
import study.stepup.productaccountingservice.models.ProductResponse;
import study.stepup.productaccountingservice.services.ProductAccountingService;
import study.stepup.productaccountingservice.services.ProductService;

@Slf4j
@RestController()
@AllArgsConstructor
@RequestMapping("/corporate-settlement-account")
public class ProductAccountingController {

    private final ProductAccountingService productAccountingService;

    @PostMapping(value = "/create" )
    public ResponseEntity<ProductAccountingResponse> create(@Validated @RequestBody ProductAccountingRequest productAccountingRequest)
    {
        ProductAccountingResponse response = productAccountingService.create(productAccountingRequest);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON).
                body(response);
    }
}
