package bbgon.irtsu_cas.controllers.restControllers;

import bbgon.irtsu_cas.constants.ValidationConstants;
import bbgon.irtsu_cas.dto.request.DetailProperties;
import bbgon.irtsu_cas.dto.request.FilterDetailRequest;
import bbgon.irtsu_cas.dto.response.*;
import bbgon.irtsu_cas.services.DetailsService;
import bbgon.irtsu_cas.services.impl.ResourceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/details")
@RequiredArgsConstructor
public class DetailsController {

    private final DetailsService detailsService;

    private final ResourceService resourceService;

    @PostMapping
    public ResponseEntity<CustomSuccessResponse<String>> createdDetail(
            @RequestBody
            @Valid DetailProperties detail) {
        return ResponseEntity.ok(detailsService.createDetail(detail));
    }

    @GetMapping("/getAllDetailsForAuthUser")
    public ResponseEntity<List<TableElementResponse>> getAllDetailsForAuthUser(){
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(detailsService.getDetailsForAuthUser());
    }

    @PostMapping("/getDetailWitchPaginationFilterForAuthUser")
    public ResponseEntity<List<TableElementResponse>> getDetailWitchPaginationFilterForAuthUser(
            @RequestBody FilterDetailRequest filterDetailRequest
            ){
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(resourceService.getDetailWitchPaginationFilterForAuthUser(filterDetailRequest));
    }

    @PostMapping("/deleteThis")
    public ResponseEntity<CustomSuccessResponse<SuccessResponse>> deleteDetail(@RequestBody Map<String, UUID> requestBody) {
        return ResponseEntity.ok(detailsService.deleteElementById(requestBody.get("id").toString()));
    }

    @GetMapping("/all")
    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<DetailResponse>>>> getDetails(
            @RequestParam(value = "page", defaultValue = "1")
            @Min(value = 1, message = ValidationConstants.PER_PAGE_MIN_NOT_VALID)
            @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1) Integer page,

            @RequestParam(value = "perPage", defaultValue = "10")
            @Min(value = 1, message = ValidationConstants.PARAM_PER_PAGE_NOT_NULL)
            @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
            @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100) Integer perPage) {
        return ResponseEntity.ok(detailsService.getDetailsPagination(page, perPage));
    }

    @GetMapping("/filter")
    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<DetailResponse>>>> getDetailsByFilterAndPagination(
            @RequestParam(value = "page", defaultValue = "1")
            @Min(value = 1, message = ValidationConstants.PER_PAGE_MIN_NOT_VALID)
            @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1) Integer page,

            @RequestParam(value = "perPage", defaultValue = "1")
            @Min(value = 1, message = ValidationConstants.PARAM_PER_PAGE_NOT_NULL)
            @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
            @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100) Integer perPage,

            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "ownerName", required = false) String ownerName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "surName", required = false) String surName,
            @RequestParam(value = "UUID", required = false) UUID ownerID,
            @RequestParam(value = "created", required = false) LocalDateTime created) {
        return ResponseEntity.ok(detailsService.getDetailsPagination(page, perPage));
    }

    @DeleteMapping("/list_deleted")
    public ResponseEntity<CustomSuccessResponse<String>> deleteDetailsList(
            @RequestBody List<UUID> uuidList) {
        return ResponseEntity.ok(detailsService.deleteListDetails(uuidList));
    }



    @GetMapping("/getDetailWitchFilter")
    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<DetailResponse>>>> getDetails(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer perPage,
            @RequestParam(required = false) String detailName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) UUID ownerId,
            @RequestParam(required = false) UUID orderHumanId,
            @RequestParam(required = false) UUID groupId,
            @RequestParam(required = false) String dataAdd
    ) {

        CustomSuccessResponse<PageableResponse<List<DetailResponse>>> response =
                detailsService.getDetailWitchPaginationAndPredicateFilter(page, perPage,
                        detailName,
                        status,
                        ownerId,
                        orderHumanId,
                        groupId,
                        dataAdd);

        return ResponseEntity.ok(response);
    }

}
