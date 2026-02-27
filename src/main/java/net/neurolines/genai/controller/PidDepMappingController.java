package net.neurolines.genai.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.neurolines.genai.model.AuthUser;
import net.neurolines.genai.model.genai.MaPidDepMapping;
import net.neurolines.genai.model.genai.MaProductId;
import net.neurolines.genai.service.AuthService;
import net.neurolines.genai.service.PidDepMappingService;
import net.neurolines.genai.service.ProductIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pidDep")
public class PidDepMappingController {

    @Autowired
    PidDepMappingService pidDepMappingService;

    @Autowired
    AuthService authService;

    @PostMapping("/list")
    public ResponseEntity<Object> list(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @RequestBody Map<String, Object> param)
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        param.put("userCompanyCode", userCompanyCode);


        return ResponseEntity.ok(pidDepMappingService.selectPagePidDepMappingList(param));
    }

    @PostMapping("/reg")
    public ResponseEntity<Object> insert(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  @RequestBody MaPidDepMapping pidDepMapping)
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        pidDepMapping.setUserCompanyCode(userCompanyCode);

        return ResponseEntity.ok(pidDepMappingService.insertPidDepMapping(pidDepMapping));
    }

    @PostMapping("/{productId}/{departmentCode}")
    public ResponseEntity<Object> get(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @PathVariable String productId,
                                               @PathVariable String departmentCode)
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        Map<String, Object> param = new HashMap<>();

        param.put("userCompanyCode", userCompanyCode);
        param.put("productId", productId);
        param.put("departmentCode", departmentCode);

        return ResponseEntity.ok(pidDepMappingService.getPidDepMapping(param));
    }

    @PostMapping("/update")
    public ResponseEntity<Object> update(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  @RequestBody MaPidDepMapping pidDepMapping)
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }


        pidDepMapping.setUserCompanyCode(userCompanyCode);

        return ResponseEntity.ok(pidDepMappingService.updatePidDepMapping(pidDepMapping));
    }

}
