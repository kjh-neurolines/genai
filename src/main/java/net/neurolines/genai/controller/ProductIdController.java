package net.neurolines.genai.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.neurolines.genai.model.AuthUser;
import net.neurolines.genai.model.genai.IvHeadQuarters;
import net.neurolines.genai.model.genai.MaProductId;
import net.neurolines.genai.service.AuthService;
import net.neurolines.genai.service.HeadquaterService;
import net.neurolines.genai.service.ProductIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productId")
public class ProductIdController {

    @Autowired
    ProductIdService productIdService;

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

        return ResponseEntity.ok(productIdService.selectPageProductIdList(param));
    }

    @PostMapping("/search")
    public ResponseEntity<Object> search(HttpServletRequest request,
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


        return ResponseEntity.ok(productIdService.selectProductIdList(param));
    }

    @PostMapping("/reg")
    public ResponseEntity<Object> insertProductId(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   @RequestBody MaProductId productId)
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        productId.setUserCompanyCode(userCompanyCode);

        return ResponseEntity.ok(productIdService.insertProductId(productId));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Object> getProductId(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 @PathVariable String productId)
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

        return ResponseEntity.ok(productIdService.getProductId(param));
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateProductId(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    @RequestBody MaProductId productId)
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }


        productId.setUserCompanyCode(userCompanyCode);

        return ResponseEntity.ok(productIdService.updateProductId(productId));
    }

}
