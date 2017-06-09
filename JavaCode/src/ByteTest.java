
public class ByteTest {
	
	
	public static void main(String[] args) {
		System.out.println(readAddressValues());
	}
	public static int readAddressValues() {
		byte[] bufReadResponse = new byte[4];
		bufReadResponse[0] = 0x62;
		bufReadResponse[1] = 0x00;
		bufReadResponse[2] = 0x3F;
		bufReadResponse[3] = 0x01;
		
		int sensorOne = (((0xFF&bufReadResponse[1])<<8)|((0xFF&bufReadResponse[0])));
		//int sensorTwo = (((0xFF&bufReadResponse[3])<<8)|((0xFF&bufReadResponse[2])));

		
		return (sensorOne);
	}
}
