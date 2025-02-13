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

    //момент когда попадаем на главную страницу с компонентами
    @GetMapping("/elements")
    public String homePage(Model model) {
        model.addAttribute("components", resourceService.listAllElements());
        addDefaultAttribute(model);
        return "homePage";
    }

    //обновляем таблицу в соответствии с фильтром
    @PostMapping("/elements")
    public String adaptiveFilter(
            @RequestParam(required = false) String componentName,
            @RequestParam(required = false) String componentStatus,
            @RequestParam(required = false) String ownerName, Model model) {
        model.addAttribute("ownerNames", resourceService.listOwners());
        model.addAttribute("components",
                resourceService.getDetailWitchPaginationAndPredicateFilter(
                0,10,componentName,componentStatus, ownerName));
        return "homePage";
    }

    private void addDefaultAttribute(Model model) {
        model.addAttribute("ownerNames", resourceService.listOwners());
        model.addAttribute("name", "Vitaliy");
    }
}
