import com.sun.org.apache.xpath.internal.operations.Bool;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

//##########################################################################################
//
// Created by Aline Schaufelberger on 01.06.2019 - 20:26
//
//##########################################################################################

public class Person implements Serializable {
    User user;
    int id;


    public Person(User user) {
        this.user = user;
        this.id = user.getId();
    }

    public User getUser() {
        if (user !=null)
            return user;
        else
            return null;
    }

    public int getId(){
        return id;
    }

    // SETTERS & GETTERS
    public void setUser(User user) {
        this.user = user;
    }


}
