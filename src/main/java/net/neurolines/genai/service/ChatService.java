package net.neurolines.genai.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.neurolines.genai.dao.genai.AiComDeDefaultSessionDao;
import net.neurolines.genai.dao.genai.AiDepartmentPromptDao;
import net.neurolines.genai.dao.genai.AiHistoryDao;
import net.neurolines.genai.dao.genai.AiUserSessionDao;
import net.neurolines.genai.model.AuthUser;
import net.neurolines.genai.model.ChatStreamDTO;
import net.neurolines.genai.model.DeComSessionDto;
import net.neurolines.genai.model.genai.AiComDeDefaultSession;
import net.neurolines.genai.model.genai.AiHistory;
import net.neurolines.genai.model.genai.AiUserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class ChatService {

    @Autowired
    AuthService authService;

    @Autowired
    AiHistoryDao aiHistoryDao;

    @Autowired
    AiDepartmentPromptDao aiDepartmentPromptDao;

    @Autowired
    AiUserSessionDao aiUserSessionDao;

    @Autowired
    AiComDeDefaultSessionDao aiComDeDefaultSessionDao;

    @Autowired
    @Qualifier("chatApiRestTemplate")
    private RestTemplate chatApiTemplate;

    /**
     * 세션 생성
     * @return
     */
    public Map<String, Object> makeSession()
    {
        try {
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON); //API 호출 시 파일 전송을 위해 설정

            Map<String, Object> body = new HashMap<>();
            body.put("title", "new Chat");

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<Map<String, Object>> response =
                    chatApiTemplate.exchange("/api/sessions", HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});

            Map<String, Object> responseBody = response.getBody();

            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 채팅
     * @param request
     * @param response
     * @param prompt
     * @param session
     * @param file
     * @return
     */
    public ChatStreamDTO chat(HttpServletRequest request
            , HttpServletResponse response
            , String prompt
            , String session
            , MultipartFile file) throws IOException {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;

        if(userInfo != null && userInfo.size() > 0)
        {
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        if(session == null)
        {
            Map<String, Object> sessionInfo = makeSession();
            session = sessionInfo.get("id").toString();
        }

        //TODO GEN AI 통신
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //API 호출 시 파일 전송을 위해 설정
        headers.setAccept(List.of(MediaType.TEXT_EVENT_STREAM));

        Map<String, Object> body = new HashMap<>();
        body.put("session_id", session);
        body.put("message", prompt);

        if(file != null)
            setSessionFile(session, file);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, Object>> responseEntity = chatApiTemplate.exchange("/api/chat" , HttpMethod.POST, requestEntity,  new ParameterizedTypeReference<Map<String, Object>>() {});

        ChatStreamDTO returnData = new ChatStreamDTO();

        if(responseEntity.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = responseEntity.getBody();

            AiHistory history = new AiHistory();

            history.setEmail(email);
            history.setPrompt(prompt);
            history.setSession(session);
            history.setUserCompanyCode(userCompanyCode);

            aiHistoryDao.insertChatHistory(history);

            List<Map<String, Object>> messages = (List<Map<String, Object>>) responseBody.get("messages");
            List<Map<String, Object>> workflow = new ArrayList<>();

            String answer = "";

            for(int i = 0 ; i < messages.size(); i++)
            {
                Map<String, Object> content = messages.get(i);
                if("assistant".equals(content.get("role"))) {
                    answer += content.get("content").toString();

                    if(content.get("workflowActions") != null)
                        workflow.addAll((Collection<? extends Map<String, Object>>) content.get("workflowActions"));
                }
            }


            returnData.setPrompt(prompt);
            returnData.setAnswer(answer);
            returnData.setSession(session);
            returnData.setWorkflow(workflow);
            // 통신 후 히스토리 저장
        }

        return returnData;
    }

    public void setSessionFile(String session
            , MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);//API 호출 시 파일 전송을 위해 설정
        headers.setAccept(List.of(MediaType.TEXT_EVENT_STREAM));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        body.add("file", fileResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        String url = "/api/files/upload?session_id=" + session;

        ResponseEntity<Map<String, Object>> responseEntity = chatApiTemplate.exchange(url, HttpMethod.POST, requestEntity,  new ParameterizedTypeReference<Map<String, Object>>() {});

        if(responseEntity.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = responseEntity.getBody();

            if(Boolean.valueOf(responseBody.get("success").toString()))
            {
                Map<String, Object> fileInfo = (Map<String, Object>) responseBody.get("file");

                fileInfo.get("id").toString();
            }
        }

    }

    /**
     * 부서별 자주하는 질문
     * @param request
     * @param response
     * @return
     */
    public Object dmPrompt(HttpServletRequest request
            , HttpServletResponse response)
    {
        List<AuthUser> userInfo = authService.user(request, response);

        //TODO 부서코드 가져오는 로직 추가 필요

        Map<String, String> param = new HashMap<>();

        String userCompanyCode = null;

        if(userInfo != null && userInfo.size() > 0){
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        param.put("dmCode", null);
        param.put("userCompanyCode", userCompanyCode);

        return aiDepartmentPromptDao.selectDepartmentPrompt(param);
    }

    /**
     * 사용자별 히스토리 조회
     * @param request
     * @param response
     * @return
     */
    public List<AiHistory> chatHistory(HttpServletRequest request
            , HttpServletResponse response)
    {
        List<AuthUser> userInfo = authService.user(request, response);

        //TODO 부서코드 가져오는 로직 추가 필요

        Map<String, String> param = new HashMap<>();

        String email = null;

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
        }

        param.put("email", email);

        return aiHistoryDao.selectChatHistory(param);
    }

    /**
     * 사용자 세션 히스토리 조회
     * @param session
     * @return
     */
    public Object getSessionHistory(String session)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //API 호출 시 파일 전송을 위해 설정

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(new HashMap<>(), headers);

        String uri = "/api/sessions/" + session + "/items";

        ResponseEntity<Map<String, Object>> responseEntity = chatApiTemplate.exchange(uri , HttpMethod.GET, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});

        return responseEntity.getBody();

    }

    /**
     * 부서별 기본 프롬프트 및 개인 사용자 세션 조회
     * @param request
     * @param response
     * @return
     */
    public Object getDepartmentPrompt(HttpServletRequest request
            , HttpServletResponse response)
    {
        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        Map<String, String> param = new HashMap<>();
        param.put("email", email);
        param.put("userCompanyCode", userCompanyCode);

        List<AiUserSession> userList = aiUserSessionDao.selectAiUserSession(param);
        List<DeComSessionDto> deComSession = new ArrayList<>();

        for(AiUserSession session : userList)
        {
            DeComSessionDto deComSessionDto = new DeComSessionDto();

            deComSessionDto.setRegNo(session.getAiUsRegNo());
            deComSessionDto.setEmail((email));
            deComSessionDto.setUserCompanyCode(userCompanyCode);
            deComSessionDto.setDmCode(dmCode);
            deComSessionDto.setSessionName(session.getSessionName());
            deComSessionDto.setSession(session.getSession());
            deComSessionDto.setType("U");

            deComSession.add(deComSessionDto);

            if(deComSession.size() == 4)
                break;

        }

        // 리스트 4개까지 출력이므로 4개 세션 존재시 default값 미조회
        if(userList.size() < 4)
        {
            List<AiComDeDefaultSession> deList = aiComDeDefaultSessionDao.selectComDeDefaultSession(param);

            for(AiComDeDefaultSession session : deList)
            {
                DeComSessionDto deComSessionDto = new DeComSessionDto();

                deComSessionDto.setRegNo(session.getAiCddsRegNo());
                deComSessionDto.setEmail((email));
                deComSessionDto.setUserCompanyCode(userCompanyCode);
                deComSessionDto.setDmCode(dmCode);
                deComSessionDto.setSessionName(session.getSessionName());
                deComSessionDto.setType("R");

                deComSession.add(deComSessionDto);

                if(deComSession.size() == 4)
                    break;
            }
        }

        return deComSession;
    }

    //insertAiUserSession
    public Object setNewUserSession(HttpServletRequest request,
                                    HttpServletResponse response,
                                    AiUserSession aiUserSession)
    {
        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        aiUserSession.setEmail(email);
        aiUserSession.setUserCompanyCode(userCompanyCode);
        aiUserSession.setDmCode(dmCode);

        return aiUserSessionDao.insertAiUserSession(aiUserSession);
    }

    public Object deleteUserSession(HttpServletRequest request,
                                    HttpServletResponse response,
                                    AiUserSession aiUserSession)
    {
        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        aiUserSession.setEmail(email);
        aiUserSession.setUserCompanyCode(userCompanyCode);
        aiUserSession.setDmCode(dmCode);

        return aiUserSessionDao.deleteAiUserSession(aiUserSession.getAiUsRegNo());
    }

    public Object updateUserSession(HttpServletRequest request,
                                     HttpServletResponse response,
                                     AiUserSession aiUserSession)
    {
        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        aiUserSession.setEmail(email);
        aiUserSession.setUserCompanyCode(userCompanyCode);
        aiUserSession.setDmCode(dmCode);

        return aiUserSessionDao.updateAiUserSession(aiUserSession);
    }




}
