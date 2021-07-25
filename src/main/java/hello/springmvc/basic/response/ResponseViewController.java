package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1() {
        return new ModelAndView("response/hello")
                .addObject("data", "hello!");
    }

    /**
     * {@link Controller} 에서 {@link String} 을 직접 반환하게 되면 ViewResolver 가 실행되어서 뷰를 찾고 렌더링 한다.
     *
     * @param model 전달할 데이터
     * @return view
     */
    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) {
        model.addAttribute("data", "hello thymeleaf!");
        return "response/hello";
    }

    /**
     * {@code void}를 반환하는 경우 (이 방법은 권장하지 않음)
     * <p>{@link Controller}를 사용하고, {@link javax.servlet.http.HttpServletResponse}, {@link
     * java.io.OutputStream}같은 HTTP 메시지 바디를 처리하는 파라미터가 없으면 요청 URL 을 참고해서 논리 뷰 이름으로 사용한다.
     *
     * @param model 전달할 데이터
     */
    @RequestMapping("/response/hello")
    public void responseViewV3(Model model) {
        model.addAttribute("data", "hello thymeleaf!");
    }

}
