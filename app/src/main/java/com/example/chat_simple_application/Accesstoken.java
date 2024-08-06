//package com.example.chat_simple_application;
//
//import android.util.Log;
//
////import com.google.auth.oauth2.GoogleCredentials;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//
//public class Accesstoken {
//
//    private static final String firebaseMessagingScope="https://www.googleapis.com/auth/firebase.messaging";
//
//    public String getAccessToken(){
//        try {
//            String jsonString = "{\n" +
//                    "  \"type\": \"service_account\",\n" +
//                    "  \"project_id\": \"chat-simple-applicationbackend\",\n" +
//                    "  \"private_key_id\": \"ff06cd3bb8e6b2669cef61c54d3f27b81685d651\",\n" +
//                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCx4cIt2O1lf+eF\\nBlqGVC21WFZ+3eZFlOeypE6t1Wms3jb1V2M0BKAY1tm9luo4GTvbJzraDV/7XjQG\\nYlf3V1LB712vGHEx2otu4lofE9tz/MzgUMDE1wyoj5ip/SHt441vblxESqhHhT9m\\nP4VayYpVJxS4st2IHi4QA+ovzBXpZocI+93gQEkDLbR2ZS06N0U/S8PuKyrttXnS\\nTdpNqK/BinUvpbCbUetzN9WzppNAsYkYQCMTOcciW4D/A/XQe2xUCS08Ga45dRIw\\neCRVwmVupM2PpVbM2KH8ks/LsQ473ZjgGKwy3aiYRYEUVtldjD2pcqUIKAijaPUC\\nLpDH7IYRAgMBAAECggEADUmXZzRWm2fgA/xTA8jC91Jnd6ecvWLfoeULKYoREEQz\\nJ8WE2AxmZySZdEOTMGiWI4s9Jq+sFUtWqIltF8FSocD/Jwa04ev40WogIEUr9fCS\\n30i49Zs4y1iap0wM3kfKt2Gvi7xl8J2//CPPktYOOskmD5vw12ultjWMp1Bf7KTH\\nlCStwIjQfLqVQfpLN/T3BCGmaWwQe26Yc78E6ugsBCCBIj4arGxjb+NaSO0Q9RTf\\nVvUP4W8MUCrzg92tPWlJmftsbWeCTLyiYKn6D9o6InMVvclPV7NHWstd27d//v8w\\nrMzQ9V7h3bZ0rFhvGKwGkc2EhgEUbnF6NDV7XvbdbwKBgQDfinM4iLpKxbKcSRNc\\nG4IDQjfN0Sf1eQdlMrFZeR7nC5vcrKtzvhhespcy2QWRa2/x3ooHLeFCDxxRabFA\\nHztH4bVLljHYRX5ZEBviXO3f0EE9XBiV2L05RUNBfs7mV27LmgvxjvJLaHj6Fqy6\\npsj3ETpdFMBXIIl44O6yePJUjwKBgQDLtg4uaqi9mpWvo+rQ0bknriIUBWdNbqmJ\\ngaYkiX5B6PgFX2E092uZ99Vqs/dSniytfCeX1JBATHe0eBuCT1tVgmgLiy/v4J5r\\nqj0AOPp8vu4GY92k5dnUsnZMA8JZwPfuWXNrOUa7x5alOgFE6IoN/xoDNeqPT3lL\\n/AiJzlMLXwKBgAQcMpQmh25ahjczbMSzyMcwj4AcSWTNDeLBdNtVS8kpwHrlPd3J\\n7/7Fv9vye5S56fwDgHwzTsH9LyMcE2M/b8KZltDF+TKlzHXswuZ9mO4VkS2EpPsU\\nWh5UdLEemMVJdEMQGQoe+7uBN0A57IEeEfHhAja16/duJLjxQdJMQQBnAoGBAI8X\\no+CA9WSx+GE3ABOMrcWssK1lmkmu6RH46w6RMgn0Lnv/opWFEqVFjDj2j00OZI28\\nP0eMfYNc81zmXVRsj9iB0LoDHDogisdP/qdwXqtPpn0Am0lgV2HrteLxZQyM2tsn\\nJEb2JbRzn2MSWp/fxFHOlqQYtFnUVnzEXe8G6hh7AoGBAJObcDSTYqQ39QjHl9KL\\ngw9HxCRWwT9nzK/cjWldkmXkJ8OrT/tKdsGyGit/fkdhtVsXk1ZDnqOVGwqc3JBz\\nhXbSMK5IUpFdUuH/cAwipbEwQQy+xbuFCxy95vj0LvxYy6eG0sOB1FUMFMVR/7Ex\\nQCEL1RWMuXcFwnJPDX8jlsb7\\n-----END PRIVATE KEY-----\\n\",\n" +
//                    "  \"client_email\": \"firebase-adminsdk-ct2ix@chat-simple-applicationbackend.iam.gserviceaccount.com\",\n" +
//                    "  \"client_id\": \"113977276722090086340\",\n" +
//                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
//                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
//                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
//                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-ct2ix@chat-simple-applicationbackend.iam.gserviceaccount.com\",\n" +
//                    "  \"universe_domain\": \"googleapis.com\"\n" +
//                    "}\n";
//            InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
//            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream)
//                    .createScoped(Arrays.asList(firebaseMessagingScope));
//
//            googleCredentials.refresh();
//            return googleCredentials.getAccessToken().getTokenValue();
//
//        } catch (IOException e) {
//            Log.e("error", "" + e.getMessage());
//            return null;
//        }
////    }
//
