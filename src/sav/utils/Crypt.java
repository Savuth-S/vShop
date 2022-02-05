package sav.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

public class Crypt
{
        public static byte[] guidToBin(UUID guid)
	{
		byte[] compressedGUID = new byte[16];
		ByteBuffer.wrap(compressedGUID)
				.order(ByteOrder.BIG_ENDIAN)
				.putLong(guid.getMostSignificantBits())
				.putLong(guid.getLeastSignificantBits());

		return compressedGUID;
	}
        
        public static UUID binToGUID(byte[] compressedGUID)
        {
                ByteBuffer buf = ByteBuffer.wrap(compressedGUID); 

                return new UUID(buf.getLong(), buf.getLong());
        }

        public static byte[] hashPassword(String password, byte[] salt)
        {
                try{ MessageDigest md = MessageDigest.getInstance("SHA-256");
                        md.update(salt);
                        
                        return md.digest(password.getBytes(StandardCharsets.UTF_8));
                }catch (Exception e){
                        e.printStackTrace();
                }

                return null;
        }
}
