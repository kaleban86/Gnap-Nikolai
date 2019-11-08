import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class TerminalSmsSend {

    public static void main(String[] args) throws IOException, InterruptedException {

    String comand = "sendsms 79827956841   'Hello' ";
    Process process = Runtime.getRuntime().exec(comand);

    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

    String line ="";
    while ((line= reader.readLine())!=null){
        System.out.println(line="\n");
    }
    process.waitFor();


}
}
