package bbgon.irtsu_cas.controllers;

import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.SuccessResponse;
import bbgon.irtsu_cas.services.RentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/rentDetail")
@RequiredArgsConstructor
public class RentController {
    private final RentService rentService;

    @PostMapping
    public ResponseEntity<CustomSuccessResponse<SuccessResponse>> rentDetail(
            @RequestParam UUID uuidDetail) {

        return ResponseEntity.ok(rentService.getDetailFromAnyUserWhoAlsoInOneGroup(uuidDetail));
    }
}
