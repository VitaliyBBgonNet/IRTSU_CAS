package bbgon.irtsu_cas.controllers;

import bbgon.irtsu_cas.services.impl.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/IRTSU")
@RequiredArgsConstructor
public class ElementControllers {
    private final ResourceService resourceService;

    @GetMapping("/elements")
    public String homePage(Model model) {
        addDefaultAttribute(model);
        return "homePage";
    }

    @PostMapping("/adaptiveFilter")
    public String adaptiveFilter(
            @RequestParam String componentName,
            @RequestParam String componentStatus,
            @RequestParam String ownerName,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime addedDate, Model model) {
        System.out.println(componentName + " " + componentStatus + " " + ownerName + " " + addedDate);
        addDefaultAttribute(model);
        return "homePage";
    }

    private void addDefaultAttribute(Model model) {
        model.addAttribute("components", resourceService.listAllElements());
        model.addAttribute("ownerNames", resourceService.listOwners());
        model.addAttribute("name", "Vitaliy");
    }
}
