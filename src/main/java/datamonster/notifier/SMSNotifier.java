package datamonster.notifier;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SMSNotifier implements Notifier {

    public static final String ACCOUNT_SID = "AC9c3c2bf081352da28a32c3d4dd7f2370";
    public static final String AUTH_TOKEN = "dc14fdf44fbc72e420924b6fdf293442";


    @Override
    public void notify(String message) throws TwilioRestException {

        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        // Build a filter for the MessageList
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Body", message));
        params.add(new BasicNameValuePair("To", "+91 90801 65010"));
        params.add(new BasicNameValuePair("From", "+15005550001"));

        MessageFactory messageFactory = client.getAccount().getMessageFactory();
//        Message SMSToBeSent = messageFactory.create(params);
//        System.out.println(SMSToBeSent.getSid());
    }
}
