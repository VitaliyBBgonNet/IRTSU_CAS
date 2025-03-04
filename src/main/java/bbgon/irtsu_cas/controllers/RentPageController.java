package bbgon.irtsu_cas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/authUser")
public class RentPageController {

    @GetMapping("/rent")
    public String rent() {
        return "rent";
    }

}
