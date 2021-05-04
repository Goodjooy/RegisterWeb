package com.jacky.register.contraller;

import com.jacky.register.dataHandle.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("/api/csrf")
public class CsrfController {
    @GetMapping("/token")
    public Result<?>getCsrfToken(HttpServletRequest request){
        CsrfToken token= (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        HashMap<String,String >tokenMap=new HashMap<>();
        if (token==null)
            return Result.failureResult("no Token Found");
        tokenMap.put("token",token.getToken());
        tokenMap.put("headName",token.getHeaderName());
        tokenMap.put("paramName",token.getParameterName());

        return Result.okResult(tokenMap);
    }
}
