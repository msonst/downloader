package com.cs.download.server;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/introspect")
public class IntrospectionController {

  private static final Logger LOGGER = LoggerFactory.getLogger(IntrospectionController.class);

  private final RequestMappingHandlerMapping mMapping;

  public IntrospectionController(RequestMappingHandlerMapping handlerMapping) {
    mMapping = handlerMapping;
  }

  @EventListener
  public void handleContextRefresh(ContextRefreshedEvent event) {
      ApplicationContext applicationContext = event.getApplicationContext();
      RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
          .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
      Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping
          .getHandlerMethods();
      map.forEach((key, value) -> LOGGER.info("{} {}", key, value));
  }

  @GetMapping("/endpoints")
  @ResponseBody
  public ObjectNode getEndpoints() {
    
    
    ObjectMapper objectMapper = new ObjectMapper();

    Map<RequestMappingInfo, HandlerMethod> handlerMethods = mMapping.getHandlerMethods();
    ObjectNode root = objectMapper.createObjectNode();
    ArrayNode entries = root.arrayNode();
    root.set("Endpoints", entries);

    handlerMethods.entrySet().forEach(e -> {
      ObjectNode entry = entries.objectNode();
      entries.add(entry);

      RequestMappingInfo info = e.getKey();

      entry.put("url", info.getPathPatternsCondition().getDirectPaths().toString());
      ObjectNode annotationNode = entry.objectNode();
      entry.set("kk", annotationNode);

      HandlerMethod handlerMethod = e.getValue();
      Method method = handlerMethod.getMethod();

      Annotation[] declaredAnnotations = method.getAnnotations();
      for (Annotation annotation : declaredAnnotations) {
        RequestMapping cast = (RequestMapping) annotation.getClass().cast(RequestMapping.class);

        Class<? extends Annotation> annotationType = annotation.annotationType();

        annotationNode.put("type", annotation.toString());
      }

      //      
      //      Class<?> returnType = method.getReturnType();
      //      entry.put("returnType", returnType.getSimpleName());
      //
      //      ArrayNode parameterNode = entry.arrayNode();
      //      entry.put("parameter", parameterNode);
      //      
      //      MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
      //      for (MethodParameter methodParameter : methodParameters) {
      //        ObjectNode parameter = parameterNode.objectNode();
      //        parameterNode.add(parameter);
      //        
      //        parameter.put("parameterType", methodParameter.getParameterType().getSimpleName());
      ////        parameter.put("parameterName", methodParameter.getParameterName());
      //      }
    });

    LOGGER.debug(root.toPrettyString());

    //    try {
    //      LOGGER.debug(" {}", objectMapper.writeValueAsString(handlerMethods));
    //    } catch (JsonProcessingException e1) {
    //      // TODO Auto-generated catch block
    //      e1.printStackTrace();
    //    }

    return root;
  }
}
