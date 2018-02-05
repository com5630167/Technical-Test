/*
 * Technical Test
 * Number 1
 */

package mytestjava;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.regex.Pattern;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 *
 * @author Admin_8_1
 */
public class MyTestJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException {
        // TODO code application logic here
        String path = "C:\\Users\\Admin_8_1\\Desktop\\promotion1.log"; //อ่านค่าจาก logFile ที่เก็บข้อมูลตามโจทย์
        File file = new File(path);
        String phoneNumber = "";
        int cntServiceCharge = 0;
        JSONObject obj = new JSONObject(); //new Obj json ไว้สำหรับเก็บค่าโทร และเบอร์โทร
        JSONArray arrPhoneNum = new JSONArray();
        JSONArray arrServCharge = new JSONArray();
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                String[] stringArray = line.split(Pattern.quote("|"));
                String startTime = stringArray[1];
                String EndTime = stringArray[2]; 
                phoneNumber = stringArray[3];
                String promotion = stringArray[4];
                
                DateFormat sdf = new SimpleDateFormat("hh:mm:ss");  
                Date tmpstartTime = sdf.parse(startTime);    
                Date tmpendTime = sdf.parse(EndTime);  
                long diff = tmpendTime.getTime() - tmpstartTime.getTime(); // หาจำนวนเวลาที่โทร เวลาสิ้นสุด - เวลาเริ่มต้น     
                cntServiceCharge = CalculateServiceCharge(diff,promotion); // คำนวนค่าโทร
                //System.out.println("price: " + cntServiceCharge);
               
                arrPhoneNum.add(phoneNumber);
                arrServCharge.add(cntServiceCharge);      
            }
            obj.put("MobileNo", arrPhoneNum); //เก็บหมายเลขโทรศัพท์
            obj.put("SeviceCharge", arrServCharge); //เก็บค่าโทร
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //เก็บลงไฟล์ json
        try (FileWriter file2 = new FileWriter("D:\\result.json")) {
            file2.write(obj.toJSONString());
            file2.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int CalculateServiceCharge(long diff,String promotion){
        int result = 0;
        int tmpsec = (int) (diff / 1000); // แปลงค่าเป็นวินาที

        if(promotion.equals("P1")){
            if(tmpsec > 0 && tmpsec <= 180){ //กรณีโทรน้อยกว่า 3 นาที, 180 วินาที = 3 นาทีแรก 3 บาท
                result += 3;
            }else if(tmpsec > 180){ //กรณีโทรมากกว่า  3 นาทีขึ้นไป
                result += 3;   // คิดค่าโทร 3 นาทีแรกก่อน
                tmpsec -= 180; // หักลบนาทีที่คิดไปแล้ว
                if(tmpsec > 0){ // หากยังมีส่วนเกิน นับว่าเกินมากี่นาที  แล้วคิดนาทีละบาท
                    int count = tmpsec/60;
                    result += count*1;
                }      
            }
        } 
        return result;
    }
    
}
