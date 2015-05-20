
package chatDatabase;

import java.util.ArrayList;

public class DbConnection {
    
        static ArrayList<String> dbUsers=new ArrayList<>();
        static ArrayList<String> dbPasswords=new ArrayList<>();
    
    public DbConnection(){
        dbUsers.add("admin");
        dbPasswords.add("admin");
    }
    
    public boolean authenticateMember(String username,String password) {
                
        boolean check=false;
        
        for(int count=0;count<dbUsers.size();count++) {
            
        if(username.equals(dbUsers.get(count)) && password.equals(dbPasswords.get(count))){
            check=true;}       
        }
        
        return check;
    }
    
    public boolean registerUser(String regUsername,String regPassword) {

        boolean confirm=true;
        
        if(dbUsers.contains(regUsername)){confirm=false;}
        dbUsers.add(regUsername);
        dbPasswords.add(regPassword);
        
        return confirm;
    }
}