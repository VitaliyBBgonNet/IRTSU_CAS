package bbgon.irtsu_cas.controllers;

import bbgon.irtsu_cas.constants.ValidationConstants;
import bbgon.irtsu_cas.dto.request.DetailProperties;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.DetailResponse;
import bbgon.irtsu_cas.dto.response.PageableResponse;
import bbgon.irtsu_cas.services.DetailsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    @GetMapping("/all")
    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<DetailResponse>>>> getDetails(
            @RequestParam(value = "page", defaultValue = "1")
            @Min(value = 1, message = ValidationConstants.PER_PAGE_MIN_NOT_VALID)
            @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1) Integer page,

            @RequestParam(value = "perPage", defaultValue = "10")
            @Min(value = 1, message = ValidationConstants.PARAM_PER_PAGE_NOT_NULL)
            @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
            @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100) Integer perPage){
        return ResponseEntity.ok(detailsService.getDetailsPagination(page, perPage));
    }

    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<DetailResponse>>>> getDetailsByFilterAndPagination(
            @RequestParam(value = "page", defaultValue = "1")
            @Min(value = 1, message = ValidationConstants.PER_PAGE_MIN_NOT_VALID)
            @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1) Integer page,

            @RequestParam(value = "perPage", defaultValue = "1")
            @Min(value = 1, message = ValidationConstants.PARAM_PER_PAGE_NOT_NULL)
            @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
            @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100) Integer perPage,

            @RequestParam(value = "name") String name,
            @RequestParam(value = "status") String status,
            @RequestParam(value = "ownerName") String ownerName,
            @RequestParam(value = "lastName") String lastName,
            @RequestParam(value = "surName") String surName,
            @RequestParam(value = "UUID") UUID ownerID,
            @RequestParam(value = "created") LocalDateTime created){
        return ResponseEntity.ok(detailsService.getDetailsPagination(page, perPage));
    }

    @DeleteMapping("/list_deleted")
    public ResponseEntity<CustomSuccessResponse<String>> deleteDetailsList(
            @RequestBody List<UUID> uuidList){
        return ResponseEntity.ok(detailsService.deleteListDetails(uuidList));
    }
}
