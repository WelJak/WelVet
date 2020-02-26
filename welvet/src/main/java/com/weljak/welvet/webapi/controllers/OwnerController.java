package com.weljak.welvet.webapi.controllers;

import com.weljak.welvet.domain.owner.Owner;
import com.weljak.welvet.service.owner.OwnerService;
import com.weljak.welvet.webapi.requests.CreateOwnerRequest;
import com.weljak.welvet.webapi.responses.CreateOwnerResponse;
import com.weljak.welvet.webapi.responses.FindOwnerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class OwnerController {
    private final OwnerService ownerService;
    @PostMapping("/create")
    public ResponseEntity<CreateOwnerResponse> createOwner (@RequestBody CreateOwnerRequest ownerRequest){
        Owner newOwner = ownerService.createOwner(ownerRequest.getUsername(), ownerRequest.getPassword());
        CreateOwnerResponse response = new CreateOwnerResponse(newOwner.getUuid(), newOwner.getUsername());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<FindOwnerResponse> findOwnerByUUID (@PathVariable String uuid){
        Owner owner = ownerService.findByUuid(uuid);
        FindOwnerResponse response = new FindOwnerResponse(owner.getUuid(), owner.getUsername(), owner.getRole(), owner.getOwnedAnimals());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
