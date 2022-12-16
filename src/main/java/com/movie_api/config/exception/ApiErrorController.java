package com.movie_api.config.exception;

import com.movie_api.response.RestResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ApiErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<?> errorhandle(HttpServletRequest request) {
        Integer code = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        // 404 에러 제외 나머지는 전부 500
        if (code != null) {
            if (code == 404) {
                return new RestResponse().customException(ErrorCode.MAPPING_NOT_FOUND).responseEntity();
            } else {
                return new RestResponse().customException(ErrorCode.UNKNOWN_ERROR).responseEntity();
            }
        }
        return new RestResponse().customException(ErrorCode.UNKNOWN_ERROR).responseEntity();
    }
}
