package bbgon.irtsu_cas.controllers.restControllers;

import bbgon.irtsu_cas.dto.response.PropertiesResponse;
import bbgon.irtsu_cas.dto.response.UserListResponse;
import bbgon.irtsu_cas.entity.UsersEntity;
import bbgon.irtsu_cas.services.impl.PropertiesSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
public class PropertiesController {

    private final PropertiesSourceService serviceProperties;

    @GetMapping("/getAll")
    public  ResponseEntity<String> getAllProperties() {

        PropertiesResponse response = serviceProperties.getProperties();

        StringBuilder html = new StringBuilder();

        for (String status : response.getStatus()) {
            html.append("<option value=\"").append(status).append("\">").append(status).append("</option>");
        }

        return ResponseEntity.ok(html.toString());
    }

    @GetMapping("/getStatus")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok(serviceProperties.getStatus());
    }

    @GetMapping("/getOwners")
    public ResponseEntity<String> getOwners() {
        return ResponseEntity.ok(serviceProperties.getOwner());
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserListResponse>> getUsers(){
        return ResponseEntity.ok(serviceProperties.getAllUsers());
    }

    @GetMapping("/getGroups")
    public ResponseEntity<String> getGroups() {
        return ResponseEntity.ok(serviceProperties.getGroups());
    }
}
