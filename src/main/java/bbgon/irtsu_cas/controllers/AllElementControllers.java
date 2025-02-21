package bbgon.irtsu_cas.controllers;

import bbgon.irtsu_cas.services.impl.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/IRTSU")
@RequiredArgsConstructor
public class AllElementControllers {
    private final ResourceService resourceService;

    @GetMapping("/elements")
    public String homePage(Model model) {
        model.addAttribute("components", resourceService.listAllElements());
        addDefaultAttribute(model);
        return "homePage";
    }

    @PostMapping("/elements")
    public String adaptiveFilter(
            @RequestParam(required = false) String componentName,
            @RequestParam(required = false) String componentStatus,
            @RequestParam(required = false) String ownerName, Model model) {
        model.addAttribute("ownerNames", resourceService.listOwners());
        model.addAttribute("components",
                resourceService.getDetailWitchPaginationAndPredicateFilter(
                0,1000000,componentName,componentStatus, ownerName, null));
        return "homePage";
    }

    private void addDefaultAttribute(Model model) {
        model.addAttribute("ownerNames", resourceService.listOwners());
        model.addAttribute("name", "Vitaliy");
    }
}
