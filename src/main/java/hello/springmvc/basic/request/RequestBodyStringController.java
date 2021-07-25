package hello.springmvc.basic.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class RequestBodyStringController {

    /**
     * {@link HttpServletRequest}, {@link HttpServletResponse} 를 활용한 방법
     *
     * @param request  요청
     * @param response 응답
     * @throws IOException
     */
    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);

        response.getWriter().write("ok");
    }

    /**
     * {@link InputStream}, {@link Writer} parameter 로 {@link InputStream}을 직접 받을 수 있고, response 또한
     * {@link Writer}를 직접 사용할 수 있다.
     *
     * @param inputStream    메세지 바디를 파라미터로 받으면서 바로 스트림으로 처리한다.
     * @param responseWriter 응답
     * @throws IOException
     */
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter)
            throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody = {}", messageBody);
        responseWriter.write("ok");
    }

    /**
     * {@link HttpEntity HttpEntity&lt;String&gt;} 의 사용
     * <p>Spring MVC 는 다음 파라미터를 지원한다.
     *
     * @param httpEntity message body 정보를 직접 조회, 요청 파라미터를 조회하는 기능과 관계없음.
     * @return {@link HttpEntity} 의 첫번째 파라미터로 메세지를 담아서 전달한다. 헤더 정보도 포함 가능하다.
     * @see RequestEntity
     * @see ResponseEntity
     */
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(RequestEntity<String> httpEntity) {
        String body = httpEntity.getBody();
        log.info("body = {}", body);

        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }

    /**
     * body 를 직접 조회
     * <p>{@link RequestBody} 를 파라미터에서 사용하면 요청으로 들어온 body 정보를 바로 사용할 수 있다.
     * 하지만 헤더 정보를 포함하고 있지는 않으므로 그럴 때는 {@link org.springframework.web.bind.annotation.RequestHeader}
     * 를 사용하거나, {@link #requestBodyStringV3(RequestEntity)} 를 사용하면 헤더 정보를 얻을 수 있다.
     * <p> 이렇게 메시지 바디를 직접 조회하는 기능은 요청 파라미터를 조회하는 {@link org.springframework.web.bind.annotation.RequestParam},
     * {@link org.springframework.web.bind.annotation.ModelAttribute} 와는 전혀 관계가 없다.
     *
     * @param messageBody body in request
     */
    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) {
        log.info("messageBody = {}", messageBody);

        return "ok";
    }

}
