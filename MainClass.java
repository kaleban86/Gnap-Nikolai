import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainClass
{
    private static Byte[] binary_hex;

    public static void Main() throws Exception {
//  0053 CE 7D E1 DF 15 3D BC 54
        //
        //args = new string[] { "0053BB7D41DDA78A5E19", "8B4F28DE8E9DE440A026F3BC3C8B9832" };

        //88F811DB6E6CE4405E0BEDD8E40E7F34
        //000179CE5155B3F813AD

        //B97FF05A3585E44086BA357638348D8E
        //0024E5D7ACF700CE0229

        //0003EA971CE53C8F5682
        //C4B61EDB6E6CE4408A375718ACF6EEDA

        //args = new string[] { "00026256AE889CE138BD", "77532DDB6E6CE4407D0170F20146FD85" };

        //args = new string[]   { "0008EFBA06D377FC81C9", "897CE6EE3376E44009A3B0DE88B2F23A" };


        //B97FF05A3585E44086BA357638348D8E
        //98ED1D503585E440B13C90C68925556D
        //args = new string[] { "00240657F6DC6BCD9282", "B97FF05A3585E44086BA357638348D8E" };

        //args = new string[] { "000451AFB32B3B699C59", "3E6C23DB6E6CE440F4F8C9FFAE52F75A" };     // аРГОС


        //args = new string[] { "0051FEAC52DB40394261", "639943B8D59BE440C418292FBED20B6B" };     // НБК

        //args = new string[] { "0043E82BAE499DD5E362", "FBD84AE57499E4400414E2831CF98D1E" };     // vOSTOK

        String[] args = new String[] { "0043DF44DEEC6015C808", "FBD84AE57499E4400414E2831CF98D1E" };

        if (args.length == 0) {
            System.out.println ("ERROR 0x01 ARGS");
            System.in.read();
            return;
        }

        String hex_str = String.valueOf(args [0]);
        byte[] snDEC = hex_string_to_byte_array (hex_str.substring (0, 4));
        byte[] snHEX = Helper.toPrimitives(dec_string_to_byte_array(hex_str.substring(0, 4)));
        binary_hex = Helper.toObjects(hex_string_to_byte_array (hex_str.substring (4, hex_str.length()-4)));
        byte [] key16 = hex_string_to_byte_array (String.valueOf(args [1]));

        // Расшифровываем
        TDES_ECB _tdes = new TDES_ECB();
        byte[] dec_result = _tdes.TripleDesDecryptOneBlock (Helper.toPrimitives(binary_hex), key16);


        //foreach (byte b in dec_result)
        //{
        //    Console.Write("{0:X2}", b);
        //}
        //Console.WriteLine();

        short sn1 = (short) Integer.parseInt(hex_str.substring (0, 4));//BitConverter.ToInt16 (snDEC);
        short sn2 = (short) BitConverter.toInt16 (Helper.toPrimitives(swap_bytes(dec_result)),0);

        //Console.WriteLine("SN1: {0:X2}", sn1);
        //Console.WriteLine("SN1: {0:X2}", sn2);



        // Сверка серийных номеров
        if (String.valueOf(sn1) != String.valueOf(sn2))
        {
            System.out.println("ERROR 0x02 SN");
            System.in.read();
            return;
        }

        // Проверяем контрольную сумму
        if (!compare_crc16(Helper.toObjects(dec_result)))
        {
            System.out.println("ERROR 0x03 CRC16");
            System.in.read();
            return;
        }

        // Получаем дату - тут надо подумать.
        Calendar dt = Calendar.getInstance();
        dt.add(Calendar.YEAR, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd0000");
        //Console.WriteLine ("new date: "+dt.ToString("yy MM dd hh mm"));

        int current_time = Integer.parseInt(sdf.format(dt));
        System.out.println("a1:" + current_time);

        int current_time2 = Integer.parseInt(sdf.format(dt));
        System.out.println("a2:" + current_time2);


        // добавляем серйиник
        ArrayList<Byte> l = new ArrayList<> ();
        l.add (snHEX[0]);
        l.add (snHEX[1]);

        byte[] time_arr = Helper.toPrimitives(BitConverter.GetBytes (current_time2));
        ArrayList<Byte> tmp = new ArrayList<Byte>(Arrays.asList(Helper.toObjects(time_arr)));
        Collections.reverse(tmp);

        // добавляем дату
        for (byte b : tmp)
            l.add(b);

        // считаем контрольную сумму с новыми данными
        ArrayList<Byte> last_pack = calc_crc16 (l.toArray(new Byte[0]));

        byte[] en_result = _tdes.TripleDesEncryptOneBlock(Helper.toPrimitives(last_pack.toArray(new Byte[0])), key16);

        System.out.println(hex_str.substring (0, 4));

        for (byte b: en_result)
            System.out.println("{0:X2} " + b);

        System.in.read();
    }

    private static Byte [] swap_bytes(byte [] bb)
    {
        List<Byte> res = new ArrayList<> ();
        res.add (bb [1]);
        res.add (bb [0]);
        return res.toArray(new Byte[0]);
    }

    private static ArrayList<Byte> calc_crc16(Byte[] byteIn)
    {
        int crc = 0x0000;
        crc = calc_cycle(byteIn);
        ArrayList<Byte> l = new ArrayList<Byte>(Arrays.asList(byteIn));

        l.add((byte) (crc >> 8));
        l.add((byte) (crc&255));
        return l;
    }

    private static Boolean compare_crc16(Byte[] byteIn)
    {
        int crc = calc_cycle(byteIn);
        int crc2 = (int) (byteIn [7] | (int)(byteIn [6] << 8));
        return (crc == crc2) ? true : false;
    }

    private static int calc_cycle(Byte[] byteIn)
    {
        int crc = 0xFFFF;

        for (int i=0; i<6; i++)
        {
            byte tmp = byteIn[i];
            tmp ^= (byte)(crc & 0xFF);
            tmp ^= (byte)(tmp << 4);

            crc = (int)((int)(tmp << 8) | ((crc >> 8) & 255));
            crc ^= (byte)(tmp >> 4);
            crc ^= (int)((int)tmp << 3);
        }
        return crc;
    }

    private static Byte[] dec_string_to_byte_array(String str)
    {
        ArrayList<Byte> lb = new ArrayList<Byte>(Arrays.asList(BitConverter.GetBytes(Integer.parseInt(str))));
        Collections.reverse(lb);
        return lb.toArray(new Byte[0]);
    }

    private static byte[] hex_string_to_byte_array(String str) throws Exception
    {
        if (str.length() % 2 == 1)
            throw new Exception("he binary key cannot have an odd number of digits");

        byte[] arr = new byte[str.length() >> 1];

        for (int i=0; i< str.length() >>1; i++)
        {
            arr[i] = (byte)((GetHexVal(str.charAt(i << 1)) << 4) + (GetHexVal(str.charAt((i << 1) + 1))));
        }

        return arr;
    }

    private static int GetHexVal(char hex) {
        int val = (int)hex;
        return val - (val < 58 ?48 : 55);
    }
}

class TDES_ECB
{
    //Encriptor
    public byte[] TripleDesEncryptOneBlock(byte[] plainText, byte [] key16) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException
    {
        Cipher cipher = Cipher.getInstance("DESede/EBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key16, "DESede"));

        byte[] encrypted = cipher.doFinal(plainText);

        return encrypted;
    }

    //Decrryptor
    public byte[] TripleDesDecryptOneBlock(byte[] data, byte[]key16) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException
    {
        Cipher cipher = Cipher.getInstance("DESede/EBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key16, "DESede"));

        byte[] decrypted = cipher.doFinal(data);

        return decrypted;
    }
}

class BitConverter
{
    public static Byte[] GetBytes(int value)
    {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder());
        buffer.putInt(value);
        return Helper.toObjects(buffer.array());
    }

    public static short toInt16(byte[] bytes, int index) //throws Exception
    {
        return (short)((bytes[index + 1] & 0xFF) | ((bytes[index] & 0xFF) << 0));
        //return (short)(
        //        (0xff & bytes[index]) << 8 |
        //                (0xff & bytes[index + 1]) << 0
        //);
    }
}

class Helper
{
    public static byte[] toPrimitives(Byte[] oBytes)
    {
        byte[] bytes = new byte[oBytes.length];

        for(int i = 0; i < oBytes.length; i++) {
            bytes[i] = oBytes[i];
        }

        return bytes;
    }

    public static Byte[] toObjects(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];

        int i = 0;
        for (byte b : bytesPrim) bytes[i++] = b; // Autoboxing

        return bytes;
    }
}

/*
string Decrypt(string textToDecrypt, string key)
{
    RijndaelManaged rijndaelCipher = new RijndaelManaged();
    rijndaelCipher.Mode = CipherMode.CBC;
    rijndaelCipher.Padding = PaddingMode.PKCS7;

    rijndaelCipher.KeySize = 0x80;
    rijndaelCipher.BlockSize = 0x80;
    byte[] encryptedData = Convert.FromBase64String(textToDecrypt);
    byte[] pwdBytes = Encoding.UTF8.GetBytes(key);
    byte[] keyBytes = new byte[0x10];
    int len = pwdBytes.Length;
    if (len > keyBytes.Length)
    {
    len = keyBytes.Length;
}
 */

/*
String Decrypt(String text, String key) throws Exception
{
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    byte[] keyBytes= new byte[16];
    byte[] b= key.getBytes("UTF-8");
    int len= b.length;
    if (len > keyBytes.length) len = keyBytes.length;
    System.arraycopy(b, 0, keyBytes, 0, len);
    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
    cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);

    BASE64Decoder decoder = new BASE64Decoder();
    byte [] results = cipher.doFinal(decoder.decodeBuffer(text));
    return new String(results,"UTF-8");
}
 */
