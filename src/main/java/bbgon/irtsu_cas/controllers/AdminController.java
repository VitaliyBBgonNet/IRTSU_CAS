package bbgon.irtsu_cas.controllers;

import bbgon.irtsu_cas.dto.request.GroupEditDataRequest;
import bbgon.irtsu_cas.dto.request.NewGroupRequest;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.SuccessResponse;

import bbgon.irtsu_cas.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PostMapping("/deleteGroup")
    public ResponseEntity<CustomSuccessResponse<SuccessResponse>> deleteGroup(
            @RequestParam UUID uuid,
            @RequestParam String name) {
        return ResponseEntity.ok(adminService.deleteGroup(uuid, name));
    }

    @PostMapping("/editGroup")
    public ResponseEntity<CustomSuccessResponse<SuccessResponse>> createNewGroup(
            @RequestParam UUID uuid,
            @RequestBody GroupEditDataRequest groupEditDataRequest ) {
        return ResponseEntity.ok(adminService.editGroupProperties(uuid, groupEditDataRequest));
    }

    @PostMapping("/addDetailInGroup")
    public ResponseEntity<CustomSuccessResponse<SuccessResponse>> addDetailInGroup(
            @RequestParam UUID uuidGroup,
            @RequestParam UUID uuidDetail) {
        return ResponseEntity.ok(adminService.addDetailsInOwnGroup(uuidGroup, uuidDetail));
    }

}
