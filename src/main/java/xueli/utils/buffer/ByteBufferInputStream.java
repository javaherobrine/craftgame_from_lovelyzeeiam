package xueli.utils.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.BufferUnderflowException;

public class ByteBufferInputStream extends InputStream {

	private final ByteBuffer in;

	public ByteBufferInputStream(ByteBuffer in) {
		this.in = in;

	}

	@Override
	public int read() throws IOException {
		try {
			return in.get();
		}catch(BufferUnderflowException e) {
			return -1;//it's EOF, act like a ByteArrayInputStream
		}
	}

}
