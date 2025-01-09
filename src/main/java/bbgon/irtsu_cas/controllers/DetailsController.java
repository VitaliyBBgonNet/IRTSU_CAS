package bbgon.irtsu_cas.controllers;

import bbgon.irtsu_cas.dto.request.DetailProperties;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.services.DetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/details")
@RequiredArgsConstructor
public class DetailsController {

    private final DetailsService detailsService;

    @PostMapping
    public ResponseEntity<CustomSuccessResponse<String>> createdDetail(
            @RequestBody
            @Valid DetailProperties detail){
        return ResponseEntity.ok(detailsService.createDetail(detail));
    }
}
