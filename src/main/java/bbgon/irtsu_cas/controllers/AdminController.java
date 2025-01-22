package bbgon.irtsu_cas.controllers;

import bbgon.irtsu_cas.dto.request.NewGroupRequest;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.SuccessResponse;

import bbgon.irtsu_cas.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/createGroup")
    public ResponseEntity<CustomSuccessResponse<SuccessResponse>> createNewGroup(
            @RequestBody NewGroupRequest newGroupRequest ) {
        return ResponseEntity.ok(adminService.createNewGroup(newGroupRequest));
    }
}
