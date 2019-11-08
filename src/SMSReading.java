import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SMSReading {


    private static Logger logger = Logger.getLogger(SMSReading.class.getName());
    


    public static void main(String[] args)  {

        logger.info("Read the message");

        try {


            List<Integer> integers = new ArrayList<Integer>();
            // Reading lines from a file.
            String line0 = Files.readAllLines(Paths.get("C:\\Users\\Николай\\Downloads\\TestRider.txt")).get(0);
            String line1 = Files.readAllLines(Paths.get("C:\\Users\\Николай\\Downloads\\TestRider.txt")).get(3);
            String line2 = Files.readAllLines(Paths.get("C:\\Users\\Николай\\Downloads\\TestRider.txt")).get(13);


            //Reading key in the string
            line2 = line2;
            Pattern pattern = Pattern.compile("\\d+\\S?\\d*\\d+\\S?\\d");
            Matcher matcher = pattern.matcher(line2);
            while (matcher.find()) {
                String s = matcher.group(0);
                if (s.replaceAll("\\D", " ").length() == s.length()) {
                    integers.add(Integer.parseInt(s));
                }

                System.out.println(integers);
                //Delete after reading
                File file = new File("C:\\Users\\Николай\\Downloads\\TestRider.txt");
                file.delete();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}