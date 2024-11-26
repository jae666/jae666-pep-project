package Util;

import Controller.SocialMediaController;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        // Create an instance of SocialMediaController
        SocialMediaController controller = new SocialMediaController();

        // Start the API server
        Javalin app = controller.startAPI();

        // Configure the server to listen on port 8080
        app.start(8080);

        System.out.println("Social Media Blog API is running on http://localhost:8080/");
    }
}
