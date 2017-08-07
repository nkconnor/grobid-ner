package org.grobid.core.engines;

import org.apache.commons.io.FileUtils;
import org.grobid.core.data.Sense;
import org.grobid.core.exceptions.GrobidException;
import org.grobid.core.EngineMockTest;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Patrice Lopez
 */
//@Ignore
public class SenseTaggerTest extends EngineMockTest {

    public File getResourceDir(String resourceDir) {
        File file = new File(resourceDir);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new GrobidException("Cannot start test, because test resource folder is not correctly set.");
            }
        }
        return (file);
    }

    @Test
    public void testSenseTagger() throws Exception {
        File textFile =
                new File(this.getResourceDir("./src/test/resources/").getAbsoluteFile() + "/test.en.txt");
        if (!textFile.exists()) {
            throw new GrobidException("Cannot start test, because test resource folder is not correctly set.");
        }
        String text = FileUtils.readFileToString(textFile, UTF_8);

        SenseTagger tagger = new SenseTagger();

        List<Sense> senses = tagger.extractSenses(text);
        if (senses != null) {
            for (Sense sense : senses) {
                System.out.print(text.substring(sense.getOffsetStart(), sense.getOffsetEnd()) + "\t");
                System.out.println(sense.toString());
            }
        } else {
            System.out.println("No sense found.");
        }
    }

}