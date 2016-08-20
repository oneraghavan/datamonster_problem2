package datamonster.notifier;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

import java.io.IOException;

public class SlackNotifier implements Notifier {

    private SlackSession session;

    public SlackNotifier() throws IOException {
        session = SlackSessionFactory.createWebSocketSlackSession("xoxp-71311155905-71311155953-71311405841-ee75449e68");
        session.connect();
    }

    @Override
    public void notify(String message) {
        SlackChannel channel = session.findChannelByName("alerts");
        session.sendMessage(channel, "hello");
    }
}
