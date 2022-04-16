package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

// adapted from
// https://stackoverflow.com/questions/134492/how-to-serialize-an-object-into-a-string
public class StringSerializer {
    public static <T extends Serializable> String toString(T obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException("io exception occurred", e);
        }
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static <T extends Serializable> T fromString( String s ) {
        byte [] data = Base64.getDecoder().decode( s );
        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) )) {
            @SuppressWarnings("unchecked") T o = (T) ois.readObject();
            return o;
        } catch (IOException e) {
            throw new RuntimeException("io exception occurred", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class not found", e);
        }
    }
}
