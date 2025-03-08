package bbgon.irtsu_cas.controllers.restControllers;

import bbgon.irtsu_cas.dto.request.AddNewUserFromAdmin;
import bbgon.irtsu_cas.dto.request.GroupEditDataRequest;
import bbgon.irtsu_cas.dto.request.NewGroupRequest;
import bbgon.irtsu_cas.dto.request.UpdateUser;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.SuccessResponse;

import bbgon.irtsu_cas.dto.response.Users;
import bbgon.irtsu_cas.services.AdminService;
import bbgon.irtsu_cas.services.UserService;
import bbgon.irtsu_cas.services.impl.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PutMapping("/updateUser")
    public ResponseEntity<CustomSuccessResponse<SuccessResponse>> updateUserData(
            @RequestBody UpdateUser updateUser){
        return ResponseEntity.ok(adminService.updateUserData(updateUser));
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<Users>> getUsers() {
        return ResponseEntity.ok(adminService.getAllUsersForAdmin());
    }

    @PostMapping("/createUser")
    public ResponseEntity<CustomSuccessResponse<SuccessResponse>> createUser(
            @RequestBody AddNewUserFromAdmin dataNewUserFromAdmin
            ){
        return ResponseEntity.ok(adminService.createNewUser(dataNewUserFromAdmin));
    }

    @DeleteMapping("/deleteThisUser")
    public ResponseEntity<CustomSuccessResponse<SuccessResponse>> deleteDetail(@RequestBody Map<String, UUID> requestBody) {
        return ResponseEntity.ok(adminService.deleteUserById(requestBody.get("id").toString()));
    }
}
