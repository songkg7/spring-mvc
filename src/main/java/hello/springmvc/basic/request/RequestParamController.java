package hello.springmvc.basic.request;

import hello.springmvc.basic.HelloData;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username = {}, age = {}", username, age);

        response.getWriter().write("ok");
    }

    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge) {

        log.info("memberName = {}, age = {}", memberName, memberAge);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age) {

        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    /**
     * {@link String}, {@code int}, {@link Integer} 등의 단순 타입이면 {@link RequestParam}도 생략 가능
     */
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(
            String username,
            int age) {

        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    /**
     * 파라미터의 필수 여부
     * <p>{@link RequestParam#required}의 기본값은 {@code true}이다. ""은 {@code null}과 다르므로 사용에 주의한다.
     *
     * @param age {@code int}의 경우 null 이 들어갈 수 없으므로 {@link Integer}로 작성해준다.
     */
    @ResponseBody
    @RequestMapping("/request-param-v5")
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            @RequestParam(required = false) Integer age) {

        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    /**
     * 파라미터의 기본값 설정
     * <p>{@link RequestParam#defaultValue}를 설정하게 되면 파라미터가 없을 때 설정한 값을 넣어준다.
     * 이 때 파라미터에는 무조건 값이 설정되므로 {@link RequestParam#required} to {@code false}는 동작하지 않는다. 또한 blank 의 경우에도 기본값을 넣어준다.
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(defaultValue = "guest") String username,
            @RequestParam(defaultValue = "-1") int age) {

        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    /**
     * 파라미터를 {@link Map Map&lt;String, Object&gt;} or {@link MultiValueMap MultiValueMap&lt;String, Object&gt;}로 조회하기
     * <p>파라미터의 값이 하나가 확실하다면 {@link Map}을 사용해도 되지만, 그렇지 않다면 {@link MultiValueMap}을 사용하자.
     * 하지만 대부분 파라미터는 1개이다.
     */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(
            @RequestParam Map<String, Object> paramMap) {

        log.info("username = {}, age = {}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }

    /**
     * {@link ModelAttribute} 사용
     * <p>마치 마법처럼 {@link HelloData} 객체가 생성되고, 요청 파라미터의 값도 모두 들어가 있다.
     * <p>Spring MVC는 {@link ModelAttribute}가 있으면 다음을 실행한다.
     * <p> 1. {@link HelloData} 객체를 생성한다.
     * <p> 2. 요청 파라미터의 이름으로 {@link HelloData} 객체의 프로퍼티를 찾는다. 그리고 해당 프로퍼티의
     * {@code setter}를 호출해서 파라미터의 값을 입력(바인딩)한다.
     * <p>age="abc" 처럼 숫자가 틀어가야할 곳에 문자를 넣으면 {@link java.net.BindException}이 발생한다.
     *
     * <p> ex) 파라미터의 이름이 'username' 이면 'setUsername()' method 를 찾아 호출하면서 값을 입력한다.
     *
     * @param helloData Data Transfer Object
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
        log.info("helloData = {}", helloData);
        return "ok";
    }

    /**
     * {@link ModelAttribute} 생략
     * <p>{@link ModelAttribute}는 생략할 수 있다. 그런데 {@link RequestParam}도 생략할 수 있으니 혼란이 발생할 수 있다.
     * 스프링은 해당 생략시 다음과 같은 규칙을 적용한다.
     * <p>{@link String}, {@code int}, {@link Integer} 같은 단순 타입 = {@link RequestParam}
     * <p>그 외 {@link ModelAttribute} (argument resolver로 지정해둔 타입 제외)</p>
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) {
        log.info("helloData = {}", helloData);
        return "ok";
    }

}
