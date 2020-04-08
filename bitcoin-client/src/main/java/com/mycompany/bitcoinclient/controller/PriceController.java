package com.mycompany.bitcoinclient.controller;

import com.mycompany.bitcoinclient.model.ChatComment;
import com.mycompany.bitcoinclient.model.Comment;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class PriceController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public PriceController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/")
    public String getPrices() {
        return "prices";
    }

    @MessageMapping("/chat")
    public void addChatComment(@Payload Comment comment) {
        ChatComment chatComment = new ChatComment(comment.getFromUser(), comment.getMessage(), LocalDateTime.now());
        if (comment.getToUser().isEmpty()) {
            simpMessagingTemplate.convertAndSend("/topic/comments", chatComment);
        } else {
            simpMessagingTemplate.convertAndSendToUser(comment.getToUser(), "/topic/comments", chatComment);
        }
    }

}
