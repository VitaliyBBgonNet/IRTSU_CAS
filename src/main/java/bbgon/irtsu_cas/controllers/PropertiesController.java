package bbgon.irtsu_cas.controllers;

import bbgon.irtsu_cas.dto.response.PropertiesResponse;
import bbgon.irtsu_cas.security.PropertiesSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
public class PropertiesController {

    private final PropertiesSourceService serviceProperties;

    @GetMapping("/getAll")
    public ResponseEntity<PropertiesResponse> getAllProperties() {

        return ResponseEntity.ok(serviceProperties.getProperties());
    }
}
