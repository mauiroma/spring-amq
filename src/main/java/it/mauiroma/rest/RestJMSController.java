package it.mauiroma.rest;

import it.mauiroma.jms.Browser;
import it.mauiroma.jms.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestJMSController {
    @Autowired
    private Sender sender;

    @Autowired
    private Browser browser;


    @RequestMapping(value = "send" ,  method = RequestMethod.GET)
    public String sendMessage(@RequestParam(value="text", defaultValue="Hello World") String text) {
        sender.send(text);
        return "OK";
    }

    @RequestMapping(value = "sendMany" ,  method = RequestMethod.GET)
    public String sendMessage(@RequestParam(value="text", defaultValue="Hello World") String text, @RequestParam(value="many", defaultValue="100") int messageNumber) {
        for (int i = 0; i < messageNumber; i++) {
            sender.send(text);
        }
        return "OK";
    }

    @RequestMapping(value = "browse" ,  method = RequestMethod.GET)
    public int browse() {
        return browser.browse();
    }

}
