package bbgon.irtsu_cas.controllers;

import bbgon.irtsu_cas.dto.response.TableElementResponse;
import bbgon.irtsu_cas.repositories.DetailsRepository;
import bbgon.irtsu_cas.services.impl.PropertiesSourceService;
import bbgon.irtsu_cas.services.impl.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/IRTSU")
@RequiredArgsConstructor
public class ElementControllers {
    private final ResourceService resourceService;

    private final DetailsRepository detailsRepository;

    @GetMapping("/elements")
    public String homePage(Model model) {
        model.addAttribute("components",resourceService.listAllElements());
        model.addAttribute("ownerNames", resourceService.listOwners());
        model.addAttribute("name", "Vitaliy");
        return "homePage";
    }

    @PostMapping("/adaptiveFilter")
    public String adaptiveFilter(@RequestParam String componentName, Model model) {
        System.out.println(componentName);

        var components = resourceService.listAllElements();
        model.addAttribute("components",components);
        return "";//Пока что возвращаем пустую таблицу по запросу при помощи htmx
    }
}
