package main.java.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

public class Utils
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
}
