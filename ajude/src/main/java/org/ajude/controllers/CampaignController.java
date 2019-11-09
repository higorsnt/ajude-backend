package org.ajude.controllers;

import org.ajude.entities.campaigns.Campaign;
import org.ajude.exceptions.InvalidDateException;
import org.ajude.exceptions.NotFoundException;
import org.ajude.exceptions.UnauthorizedException;
import org.ajude.services.CampaignService;
import org.ajude.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.util.List;

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
            userEmail = this.jwtService.getTokenUser(token);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        campaign.setOwnerEmail(userEmail);

        try {
            return new ResponseEntity(this.campaignService.register(campaign), HttpStatus.CREATED);
        } catch (InvalidDateException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{campaign}")
    public ResponseEntity getCampaign(@PathVariable String campaign) {
        try {
            return new ResponseEntity(this.campaignService.getCampaign(campaign), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search/{substring}")
    public ResponseEntity<List<Campaign>> searchCampaigns(@PathVariable String substring)  {
        return new ResponseEntity(this.campaignService.searchCampaigns(substring, "A"), HttpStatus.OK);
    }

    @GetMapping("/search/{substring}/{status}")
    public ResponseEntity<List<Campaign>> searchCampaigns(@PathVariable("substring") String substring,
                                                          @PathVariable("status") String status)  {
        return new ResponseEntity(this.campaignService.searchCampaigns(substring, status), HttpStatus.OK);
    }

    @PutMapping("/closeCampaign/{campaignUrl}")
    public ResponseEntity closeCampaign(@RequestHeader("Authorization") String token,
                                        @PathVariable("campaignUrl") String campaignUrl){

        String userEmail = null;
        try {
            userEmail = this.jwtService.getTokenUser(token);
        } catch (ServletException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        try {
            return new ResponseEntity(this.campaignService.closeCampaign(campaignUrl, userEmail), HttpStatus.OK);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

    }
}
