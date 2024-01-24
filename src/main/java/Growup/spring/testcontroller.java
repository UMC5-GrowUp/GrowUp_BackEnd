package Growup.spring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testcontroller {

    @GetMapping("/health")
    public String healthCheck(){
        return "hihi";
    }
}
