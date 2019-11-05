package org.ajude.controllers;

import org.ajude.entities.campaigns.Campaign;
import org.ajude.exceptions.InvalidDateException;
import org.ajude.services.CampaignService;
import org.ajude.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campaign")
public class CampaignController {

    private CampaignService campaignService;
    private JwtService jwtService;

    @Autowired
    public CampaignController(CampaignService campaignService, JwtService jwtService) {
        this.campaignService = campaignService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity registerCampaign(@RequestHeader("Authorization") String token,
                                           @RequestBody Campaign campaign){

        String userEmail = null;

        try {
            userEmail = jwtService.getTokenUser(token);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        campaign.setOwnerEmail(userEmail);
        try {
            return new ResponseEntity(campaignService.register(campaign), HttpStatus.CREATED);
        } catch (InvalidDateException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
