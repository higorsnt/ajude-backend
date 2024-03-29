package org.ajude.controllers;

import org.ajude.entities.Campaign;
import org.ajude.entities.Comment;
import org.ajude.exceptions.NotFoundException;
import org.ajude.exceptions.UnauthorizedException;
import org.ajude.services.CampaignService;
import org.ajude.services.JwtService;
import org.ajude.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.time.Instant;
import java.util.Date;

@RestController
public class CommentController {

    private CampaignService campaignService;
    private UserService userService;
    private JwtService jwtService;

    @Autowired
    public CommentController(CampaignService campaignService, UserService userService, JwtService jwtService) {
        this.campaignService = campaignService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/campaign/{campaignUrl}/comment")
    public ResponseEntity<Comment> addCampaignComment(@RequestBody Comment comment,
                                                      @PathVariable("campaignUrl") String campaign,
                                                      @RequestHeader("Authorization") String header)
            throws ServletException, NotFoundException {

        String subject = this.jwtService.getSubjectByHeader(header);
        comment.setOwner(this.userService.getUserByEmail(subject).get());
        return new ResponseEntity(this.campaignService.addCampaignComment(campaign, comment), HttpStatus.OK);
    }

    @PostMapping("/campaign/{campaignUrl}/comment/{id}")
    public ResponseEntity<Comment> addCommentResponse(@RequestBody Comment reply,
                                                      @PathVariable("campaignUrl") String campaign,
                                                      @PathVariable("id") Long commentId,
                                                      @RequestHeader("Authorization") String header)
            throws ServletException, NotFoundException {

        String subject = this.jwtService.getSubjectByHeader(header);
        reply.setOwner(this.userService.getUserByEmail(subject).get());
        return new ResponseEntity(this.campaignService.addCommentResponse(campaign, commentId, reply), HttpStatus.OK);
    }

    @DeleteMapping("/campaign/{campaignUrl}/comment/{id}")
    public ResponseEntity deleteComment(@RequestHeader("Authorization") String header,
                                        @PathVariable("campaignUrl") String campaignUrl,
                                        @PathVariable("id") Long commentId) throws ServletException, UnauthorizedException, NotFoundException {
        String email = jwtService.getSubjectByHeader(header);

        if (jwtService.userHasPermission(header, email))
            return new ResponseEntity<Campaign>(campaignService.deleteComment(campaignUrl, userService.getUserByEmail(email).get(), commentId), HttpStatus.OK);

        return new ResponseEntity<Campaign>(campaignService.getCampaign(campaignUrl), HttpStatus.BAD_REQUEST);
    }

}
