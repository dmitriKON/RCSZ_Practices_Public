package practice1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PacketBuilderTest {
    @Test
    void shouldHandlePackage(){
        Command command = new Command(CommandType.UPDATE_PRODUCT, "user 1", new byte[]{0, 0, 0, 11, 14, 22, 3, 3});
        PacketBuilder packetBuilder = new PacketBuilder();
        byte[] packet = packetBuilder.encode(command);
        Command decodedCommand = packetBuilder.decode(packet);

        Assertions.assertEquals(command, decodedCommand);
    }

    @Test
    void shouldHandleInvalidCrc_header(){
        Command command = new Command(CommandType.UPDATE_PRODUCT, "user 1", new byte[]{0, 0, 0, 11, 14, 22, 3, 3});
        PacketBuilder packetBuilder = new PacketBuilder();
        byte[] packet = packetBuilder.encode(command);

        packet[14] = 0x0;
        packet[15] = 0x0;

        assertThrows(RuntimeException.class, () -> packetBuilder.decode(packet));

    }

}