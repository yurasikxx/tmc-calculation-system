package by.bsuir.tcs.controller.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        ModelAndView mav = new ModelAndView();
        mav.addObject("errorCode", status != null ? status.toString() : "500");
        mav.addObject("errorMessage", message != null ? message.toString() : "Внутренняя ошибка сервера");

        if (exception != null) {
            mav.addObject("errorDescription", exception.toString());
        } else if (status != null) {
            int code = Integer.parseInt(status.toString());
            if (code == HttpStatus.NOT_FOUND.value()) {
                mav.addObject("errorDescription", "Страница не найдена. Проверьте правильность URL.");
            } else if (code == HttpStatus.FORBIDDEN.value()) {
                mav.addObject("errorDescription", "У вас нет доступа к этой странице.");
            } else {
                mav.addObject("errorDescription", "Произошла непредвиденная ошибка. Попробуйте позже.");
            }
        }

        mav.setViewName("error/error");
        return mav;
    }
}