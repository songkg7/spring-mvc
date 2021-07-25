package hello.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class RequestBodyJsonController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("helloData = {}", helloData);

        response.getWriter().write("ok");

    }

    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody)
            throws IOException {

        log.info("messageBody = {}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("helloData = {}", helloData);

        return "ok";

    }

    /**
     * {@link RequestBody} 객체 변환
     * <p>{@link org.springframework.http.HttpEntity}, {@link RequestBody} 를 사용하면 HTTP 메시지 컨버터가
     * HTTP 메시지 바디의 내용을 우리가 원하는 문자나 객체 등으로 변환해준다. HTTP 메시지 컨버터는 문자 뿐만 아니라 JSON 도 객체로 변환해주는데, {@link
     * #requestBodyJsonV2(String)} 에서 했던 작업을 대신 처리해준다.
     *
     * @param helloData 내용이 변환되어 담길 객체, {@link RequestBody} 를 생략하면 {@link org.springframework.web.bind.annotation.ModelAttribute}
     *                  로 동작하므로, 요청 파라미터를 처리하려고 한다. 이 경우 원하는 데이터가 들어가지 않을 것이기 때문에 생략할 수 없다.
     * @see org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
     */
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData) {
        log.info("helloData = {}", helloData);
        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) {
        HelloData helloData = httpEntity.getBody();
        log.info("helloData = {}", helloData);
        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData helloData) {
        log.info("helloData = {}", helloData);
        return helloData;
    }

}
