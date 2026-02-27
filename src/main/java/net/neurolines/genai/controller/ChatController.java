package net.neurolines.genai.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.neurolines.genai.model.ChatStreamDTO;
import net.neurolines.genai.model.genai.AiHistory;
import net.neurolines.genai.model.genai.AiUserSession;
import net.neurolines.genai.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    ChatService chatService;

    /**
     * 채팅
     * @param request
     * @param response
     * @param file
     * @param chatStreamDTO
     * @return
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Object> chat(HttpServletRequest request
            , HttpServletResponse response
            , @RequestPart(name="file", required = false) MultipartFile file
            , @RequestPart(name="chatStreamDTO") ChatStreamDTO chatStreamDTO)
    {
        try {
            return ResponseEntity.ok(chatService.chat(request, response, chatStreamDTO.getPrompt(), chatStreamDTO.getSession(), file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 세션 생성
     * @return
     */
    @PostMapping("/session")
    public ResponseEntity<Object> session()
    {
        return ResponseEntity.ok(chatService.makeSession());
    }

    /**
     * 부서별 자주하는 질문
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/dm/prompt")
    public ResponseEntity<Object> getDepartmentPrompt(HttpServletRequest request, HttpServletResponse response )
    {
        return ResponseEntity.ok(chatService.dmPrompt(request, response));
    }

    /**
     * 개인별 질문 히스토리
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/history")
    public ResponseEntity<Object> getPromptHistory(HttpServletRequest request, HttpServletResponse response )
    {
        return ResponseEntity.ok(chatService.chatHistory(request, response));
    }

    /**
     * 세션 히스토리 조회
     * @param sessionId
     * @return
     */
    @PostMapping("/session/history/{sessionId}")
    public ResponseEntity<Object> getSessionHistory(@PathVariable String sessionId)
    {
        return ResponseEntity.ok(chatService.getSessionHistory(sessionId));
    }

    /**
     * 부서별 디폴트 프롬프트
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/dm/default")
    public ResponseEntity<Object> getDepartDefault(HttpServletRequest request, HttpServletResponse response )
    {
        return ResponseEntity.ok(chatService.getDepartmentPrompt(request, response));
    }

    @PostMapping("/user/session")
    public ResponseEntity<Object> setUserNewSession(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    @RequestBody AiUserSession aiUserSession)
    {
        return ResponseEntity.ok(chatService.setNewUserSession(request, response, aiUserSession));
    }//setNewUserSession

    @DeleteMapping("/user/session")
    public ResponseEntity<Object> deleteUserSession(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    @RequestBody AiUserSession aiUserSession)
    {
        return ResponseEntity.ok(chatService.deleteUserSession(request, response, aiUserSession));
    }

    @PatchMapping("/user/session")
    public ResponseEntity<Object> updateUserSession(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    @RequestBody AiUserSession aiUserSession)
    {
        return ResponseEntity.ok(chatService.updateUserSession(request, response, aiUserSession));
    }



}
