package puyo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PuyoTest {

    @Test
    public void puyoConstructor_validFilePath_initializesSuccessfully(@TempDir Path tempDir) {
        Path tempFile = tempDir.resolve("test_puyo.txt");

        assertDoesNotThrow(() -> {
            Puyo puyo = new Puyo(tempFile.toString());
            assertNotNull(puyo.getTasks());
        });
    }

    @Test
    public void getResponse_validTodoCommand_executesWithoutException(@TempDir Path tempDir) {
        Path tempFile = tempDir.resolve("test_puyo.txt");
        Puyo puyo = new Puyo(tempFile.toString());

        String response = puyo.getResponse("todo read book");
        assertNotNull(response);
    }
}