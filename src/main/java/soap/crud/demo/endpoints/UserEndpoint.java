package soap.crud.demo.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.demo.soap.user.RegisterUserRequest;
import com.demo.soap.user.RegisterUserResponse;

import soap.crud.demo.entities.User;
import soap.crud.demo.services.UserService;

public class UserEndpoint {
    private static final String NAMESPACE = "http://soap.demo.com/user";

    @Autowired
    private UserService userService;

    @PayloadRoot(namespace = NAMESPACE, localPart = "RegisterUserRequest")
    @ResponsePayload
    public RegisterUserResponse register(@RequestPayload RegisterUserRequest req) {

        User saved = userService.register(req.getUsername(), req.getPassword());

        RegisterUserResponse res = new RegisterUserResponse();
        res.setId(saved.getId());
        res.setUsername(saved.getUsername());

        return res;
    }
}
