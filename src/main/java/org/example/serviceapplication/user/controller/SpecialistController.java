package org.example.serviceapplication.user.controller;

import jakarta.annotation.security.PermitAll;
import org.example.serviceapplication.offer.dto.OfferDto;
import org.example.serviceapplication.offer.dto.OfferUpdateDto;
import org.example.serviceapplication.offer.service.OfferServiceInterface;
import org.example.serviceapplication.request.dto.CustomerRequestResponseDto;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.subCategory.dto.SubServiceCategories;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryResponseDto;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.exception.UserHasWrongRole;
import org.example.serviceapplication.user.exception.UserNotFond;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.specialistService.SpecialistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/v1/specialist")
public class SpecialistController {
    private final SpecialistService specialistService;

    public SpecialistController(SpecialistService specialistService
    ) {
        this.specialistService = specialistService;

    }


    @PostMapping("/addOffer")
    public ResponseEntity createOffer(@RequestBody OfferDto offerDto) {
        Long id = offerDto.userId();
        User specialist = specialistService.getById(id);
        if (specialist.getRole() != Role.Specialist) {
            throw new UserHasWrongRole("Customer has wrong role");
        }
        specialistService.createOffer(specialist, offerDto);
        return ResponseEntity.ok(HttpStatus.CREATED);

    }


    @GetMapping("/getAllRequests/{userId}")
    public List<CustomerRequestResponseDto> getAllRequests(@PathVariable Long userId) {
        User specialist = specialistService.getById(userId);
        if (specialist.getRole() != Role.Specialist) {
            throw new UserHasWrongRole("User has wrong role");
        }
        return specialistService.getAllRequests(specialist);
    }

    @PutMapping("/editOffer/{userId}/{offerId}")
    public void editOffer(  @PathVariable Long userId,@PathVariable Long offerId,
                            @RequestBody OfferUpdateDto offerDto  ) {

        User specialist = specialistService.getById(userId);
        if (specialist.getRole() != Role.Specialist) {
            throw new UserHasWrongRole("User has wrong role");
        }
        specialistService.editOffer(offerId ,offerDto);
    }


    @GetMapping("getAllSubServiceCategories")
    public ResponseEntity<List<SubServiceCategories>> getAllSubServiceCategories() {
        return ResponseEntity.ok(specialistService.getAllSubServiceCategories());

    }


    @GetMapping("/getAllMyOffers/{userId}")
    public ResponseEntity<List<OfferDto>> getAllMyOfferWihAcceptedStatus(@PathVariable Long userId) {
        User specialist = specialistService.getById(userId);
        if (specialist.getRole() != Role.Specialist) {
            throw new UserHasWrongRole("User has wrong role");
        }
        return ResponseEntity.ok(specialistService.getAllMyOffersWithAccepetedStatus(userId));

    }


    @PutMapping("changeOrderStatus/{userId}/{offerId}")
    public ResponseEntity changeOrderStatus(@PathVariable Long userId, @PathVariable Long offerId) {
        User specialist = specialistService.getById(userId);
        if (specialist.getRole() != Role.Specialist) {
            throw new UserHasWrongRole("User has wrong role");
        }
         specialistService.changeOrderStatus(offerId);
        return ResponseEntity.ok(HttpStatus.OK);

    }

}
