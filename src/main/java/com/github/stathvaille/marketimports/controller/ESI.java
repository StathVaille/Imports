//package com.github.stathvaille.marketimports.controller;
//
//import com.fasterxml.jackson.core.JsonFactory;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.security.oauth2.client.OAuth2ClientContext;
////import org.springframework.security.oauth2.client.OAuth2RestTemplate;
////import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
////import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ESI extends ApiBinding {
////
////    @Autowired
////    private OAuth2ClientContext oauth2ClientContext;
////
////    private Integer characterId;
//
//    public ESI(String accessToken) {
//        super(accessToken);
//    }
//
//    //
//
////    public Map<String, Object> user()
////    {
////        Map<String, Object> details = (Map<String, Object>)authentication.getUserAuthentication().getDetails();
////        characterId = (Integer)details.get("CharacterID");
////        return details;
////    }
//
////    @RequestMapping("/test")
//    public Map<String, Object> getCharById(Integer characterId)
//            throws IOException
//    {
//        final String uri = "https://crest-tq.eveonline.com/characters/" + characterId + "/contacts/";
//        JsonFactory factory = new JsonFactory();
//        ObjectMapper mapper = new ObjectMapper(factory);
//        Map<String,Object> result = mapper.readValue(restTemplate.getForObject(uri, String.class), new TypeReference<HashMap<String,Object>>(){});
//        return result;
//    }
//}
